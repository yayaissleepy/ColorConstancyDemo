import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ColorConstancyDemo extends JPanel {
    private BufferedImage original, grayWorldImg, whitePatchImg, shadeOfGrayImg, retinexImg;

    public ColorConstancyDemo(String filename) throws IOException {
        original = ImageIO.read(new File(filename));
        grayWorldImg = grayWorld(original);
        whitePatchImg = whitePatch(original);
        shadeOfGrayImg = shadeOfGray(original, 6);
        retinexImg = simpleRetinex(original);
    }

    private BufferedImage grayWorld(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, img.getType());

        long sumR = 0, sumG = 0, sumB = 0;
        int n = width * height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(img.getRGB(x, y));
                sumR += c.getRed();
                sumG += c.getGreen();
                sumB += c.getBlue();
            }
        }

        double avgR = sumR / (double) n;
        double avgG = sumG / (double) n;
        double avgB = sumB / (double) n;

        double grayLevel = (avgR + avgG + avgB) / 3.0;
        double scaleR = grayLevel / avgR;
        double scaleG = grayLevel / avgG;
        double scaleB = grayLevel / avgB;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(img.getRGB(x, y));
                int r = (int) Math.min(255, c.getRed() * scaleR);
                int g = (int) Math.min(255, c.getGreen() * scaleG);
                int b = (int) Math.min(255, c.getBlue() * scaleB);
                Color newColor = new Color(r, g, b);
                out.setRGB(x, y, newColor.getRGB());
            }
        }
        return out;
    }

    private BufferedImage whitePatch(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, img.getType());

        int maxR = 0, maxG = 0, maxB = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(img.getRGB(x, y));
                if (c.getRed() > maxR) maxR = c.getRed();
                if (c.getGreen() > maxG) maxG = c.getGreen();
                if (c.getBlue() > maxB) maxB = c.getBlue();
            }
        }

        double scaleR = 255.0 / maxR;
        double scaleG = 255.0 / maxG;
        double scaleB = 255.0 / maxB;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(img.getRGB(x, y));
                int r = (int) Math.min(255, c.getRed() * scaleR);
                int g = (int) Math.min(255, c.getGreen() * scaleG);
                int b = (int) Math.min(255, c.getBlue() * scaleB);
                Color newColor = new Color(r, g, b);
                out.setRGB(x, y, newColor.getRGB());
            }
        }
        return out;
    }

    private BufferedImage shadeOfGray(BufferedImage img, int p) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, img.getType());

        double sumRp = 0, sumGp = 0, sumBp = 0;
        int n = width * height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(img.getRGB(x, y));
                sumRp += Math.pow(c.getRed(), p);
                sumGp += Math.pow(c.getGreen(), p);
                sumBp += Math.pow(c.getBlue(), p);
            }
        }

        double normR = Math.pow(sumRp, 1.0 / p);
        double normG = Math.pow(sumGp, 1.0 / p);
        double normB = Math.pow(sumBp, 1.0 / p);

        double meanNorm = (normR + normG + normB) / 3.0;

        double scaleR = meanNorm / normR;
        double scaleG = meanNorm / normG;
        double scaleB = meanNorm / normB;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(img.getRGB(x, y));
                int r = (int) Math.min(255, c.getRed() * scaleR);
                int g = (int) Math.min(255, c.getGreen() * scaleG);
                int b = (int) Math.min(255, c.getBlue() * scaleB);
                Color newColor = new Color(r, g, b);
                out.setRGB(x, y, newColor.getRGB());
            }
        }
        return out;
    }

    private BufferedImage simpleRetinex(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, img.getType());

        int radius = 15;

        double[][] blurR = gaussianBlur(getChannel(img, 'r'), width, height, radius);
        double[][] blurG = gaussianBlur(getChannel(img, 'g'), width, height, radius);
        double[][] blurB = gaussianBlur(getChannel(img, 'b'), width, height, radius);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(img.getRGB(x, y));
                int r = clamp(c.getRed() - (int) blurR[y][x] + 128);
                int g = clamp(c.getGreen() - (int) blurG[y][x] + 128);
                int b = clamp(c.getBlue() - (int) blurB[y][x] + 128);
                Color newColor = new Color(r, g, b);
                out.setRGB(x, y, newColor.getRGB());
            }
        }
        return out;
    }

    private int[][] getChannel(BufferedImage img, char channel) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] result = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(img.getRGB(x, y));
                if (channel == 'r') result[y][x] = c.getRed();
                else if (channel == 'g') result[y][x] = c.getGreen();
                else result[y][x] = c.getBlue();
            }
        }
        return result;
    }

    private double[][] gaussianBlur(int[][] channel, int width, int height, int radius) {
        double sigma = radius / 3.0;
        double[][] kernel = new double[2 * radius + 1][2 * radius + 1];
        double sum = 0;
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                kernel[y + radius][x + radius] = Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
                sum += kernel[y + radius][x + radius];
            }
        }
        for (int y = 0; y < 2 * radius + 1; y++) {
            for (int x = 0; x < 2 * radius + 1; x++) {
                kernel[y][x] /= sum;
            }
        }

        double[][] output = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double val = 0;
                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int yy = Math.min(height - 1, Math.max(0, y + ky));
                        int xx = Math.min(width - 1, Math.max(0, x + kx));
                        val += kernel[ky + radius][kx + radius] * channel[yy][xx];
                    }
                }
                output[y][x] = val;
            }
        }
        return output;
    }

    private int clamp(int val) {
        return Math.min(255, Math.max(0, val));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = original.getWidth();
        int h = original.getHeight();

        g.drawString("Original", 10, 15);
        g.drawImage(original, 10, 20, null);

        g.drawString("Gray World", w + 20, 15);
        g.drawImage(grayWorldImg, w + 20, 20, null);

        g.drawString("White Patch", 10, h + 55);
        g.drawImage(whitePatchImg, 10, h + 60, null);

        g.drawString("Shade of Gray", w + 20, h + 55);
        g.drawImage(shadeOfGrayImg, w + 20, h + 60, null);

        g.drawString("Simple Retinex", 2 * w + 30, 15);
        g.drawImage(retinexImg, 2 * w + 30, 20, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Color Constancy Algorithms Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            String filePath = "C:\\Users\\LENOVO\\OneDrive\\Pictures\\Screenshots\\Screenshot 2025-05-25 154528.png";
            ColorConstancyDemo panel = new ColorConstancyDemo(filePath);
            frame.add(panel);
            frame.setSize(panel.original.getWidth() * 3 + 100, panel.original.getHeight() * 2 + 100);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}