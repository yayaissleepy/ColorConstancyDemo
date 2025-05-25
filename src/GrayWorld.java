import java.awt.Color;
import java.awt.image.BufferedImage;

public class GrayWorld {
    public static BufferedImage apply(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, img.getType());

        long sumR=0, sumG=0, sumB=0;
        int n = width*height;

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color c = new Color(img.getRGB(x,y));
                sumR += c.getRed();
                sumG += c.getGreen();
                sumB += c.getBlue();
            }
        }

        double avgR = sumR/(double)n;
        double avgG = sumG/(double)n;
        double avgB = sumB/(double)n;

        double grayLevel = (avgR + avgG + avgB)/3.0;
        double scaleR = grayLevel / avgR;
        double scaleG = grayLevel / avgG;
        double scaleB = grayLevel / avgB;

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color c = new Color(img.getRGB(x,y));
                int r = (int)Math.min(255, c.getRed()*scaleR);
                int g = (int)Math.min(255, c.getGreen()*scaleG);
                int b = (int)Math.min(255, c.getBlue()*scaleB);
                out.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        return out;
    }
}
