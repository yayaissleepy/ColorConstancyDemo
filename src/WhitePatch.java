import java.awt.Color;
import java.awt.image.BufferedImage;

public class WhitePatch {
    public static BufferedImage apply(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, img.getType());

        int maxR=0, maxG=0, maxB=0;
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color c = new Color(img.getRGB(x,y));
                if (c.getRed() > maxR) maxR = c.getRed();
                if (c.getGreen() > maxG) maxG = c.getGreen();
                if (c.getBlue() > maxB) maxB = c.getBlue();
            }
        }

        double scaleR = 255.0 / maxR;
        double scaleG = 255.0 / maxG;
        double scaleB = 255.0 / maxB;

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
