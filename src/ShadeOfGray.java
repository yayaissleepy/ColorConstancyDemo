import java.awt.Color;
import java.awt.image.BufferedImage;

public class ShadeOfGray {
    public static BufferedImage apply(BufferedImage img, int p) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, img.getType());

        double sumRp=0, sumGp=0, sumBp=0;
        int n = width*height;

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color c = new Color(img.getRGB(x,y));
                sumRp += Math.pow(c.getRed(), p);
                sumGp += Math.pow(c.getGreen(), p);
                sumBp += Math.pow(c.getBlue(), p);
            }
        }

        double normR = Math.pow(sumRp, 1.0/p);
        double normG = Math.pow(sumGp, 1.0/p);
        double normB = Math.pow(sumBp, 1.0/p);

        double meanNorm = (normR + normG + normB)/3.0;

        double scaleR = meanNorm / normR;
        double scaleG = meanNorm / normG;
        double scaleB = meanNorm / normB;

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
