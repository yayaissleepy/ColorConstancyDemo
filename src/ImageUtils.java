public class ImageUtils {

    public static double[][] gaussianBlur(int[][] channel, int width, int height, int radius) {
        double sigma = radius / 3.0;
        double[][] kernel = new double[2*radius+1][2*radius+1];
        double sum = 0;

        for (int y=-radius; y<=radius; y++) {
            for (int x=-radius; x<=radius; x++) {
                kernel[y+radius][x+radius] = Math.exp(-(x*x + y*y) / (2*sigma*sigma));
                sum += kernel[y+radius][x+radius];
            }
        }

        for (int y=0; y<2*radius+1; y++) {
            for (int x=0; x<2*radius+1; x++) {
                kernel[y][x] /= sum;
            }
        }

        double[][] output = new double[height][width];

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                double val = 0;
                for (int ky=-radius; ky<=radius; ky++) {
                    for (int kx=-radius; kx<=radius; kx++) {
                        int yy = Math.min(height-1, Math.max(0, y+ky));
                        int xx = Math.min(width-1, Math.max(0, x+kx));
                        val += kernel[ky+radius][kx+radius] * channel[yy][xx];
                    }
                }
                output[y][x] = val;
            }
        }

        return output;
    }
}
