import java.awt.Color;
import java.awt.image.BufferedImage;

public class RetinexSimple {

    public static BufferedImage apply(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, img.getType());

        int radius = 15;

        int[][] red = getChannel(img, 'r');
        int[][] green = getChannel(img, 'g');
        int[][] blue = getChannel(img, 'b');

        double[][] blurR = ImageUtils.gaussianBlur(red, width, height, radius);
        double[][] blurG = ImageUtils.gaussianBlur(green, width, height, radius);
        double[][] blurB = ImageUtils.gaussianBlur(blue, width, height, radius);

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color c = new Color(img.getRGB(x,y));
                int r = clamp(c.getRed() - (int)blurR[y][x] + 128);
                int g = clamp(c.getGreen() - (int)blurG[y][x] + 128);
                int b = clamp(c.getBlue() - (int)blurB[y][x] + 128);
                out.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        return out;
    }

    private static int[][] getChannel(BufferedImage img, char channel) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] result = new int[height][width];
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color c = new Color(img.getRGB(x,y));
                switch (channel) {
                    case 'r': result[y][x] = c.getRed(); break;
                    case 'g': result[y][x] = c.getGreen(); break;
                    case 'b': result[y][x] = c.getBlue(); break;
                }
            }
        }
        return result;
    }

    private static int clamp(int val) {
        return Math.min(255, Math.max(0, val));
    }
}
