package uk.ac.aber.beautify.custom.texture;

import uk.ac.aber.beautify.utils.BeautifyUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

/**
 * This class blends two images together
 *
 * @author Jose Vives.
 * @since 29/11/2015
 */
public abstract class Blending {

    /**
     * Get the mask for blennding
     * @param width
     *      The width of the result mask
     * @param height
     *      The height of the result mask
     * @return
     *      The mask in the proper size
     */
    private static BufferedImage getMask(int width, int height){

        URL url = Blending.class.getResource("/uk/ac/aber/beautify/custom/texture/gaussian.jpg");

        BufferedImage mask = null;
        try {
            mask = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return scaleMask(mask, width, height);
    }

    /**
     * Scale one image to the desired size.
     * Original code from stackOverFlow.com forum
     * (I lost the post where I found it)
     * @param src
     *      The image to scale
     * @param w
     *      The width for the final image
     * @param h
     *      The height for the final image
     * @return
     *      The new image width the righ size
     */
    private static BufferedImage scaleMask(BufferedImage src, int width, int heigth){
        BufferedImage img = new BufferedImage(width, heigth, src.getType());
        int ww = src.getWidth();
        int hh = src.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < heigth; y++) {
                int col = src.getRGB(x * ww / width, y * hh / heigth);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }

    /**
     * Multiply two images together with an alpha value
     * @param input
     *      The image for apply the blending
     * @return
     *      The new image with the blending done
     */
    public static BufferedImage multiply(BufferedImage input, double alpha){

        BufferedImage mask = getMask(input.getWidth(), input.getHeight());
        Raster maskRaster = mask.getData();
        Raster raster = input.getData();

        BufferedImage output = BeautifyUtils.getCopy(input);
        WritableRaster wr = output.getRaster();

        for(int u = 0; u < raster.getWidth(); u++){
            for(int v = 0; v < raster.getHeight(); v++){

                double[] rgbOriginal = new double[3];
                double[] rgbMask = new double[3];
                double[] rgbFinal = new double[3];

                maskRaster.getPixel(u, v, rgbMask);
                raster.getPixel(u, v, rgbOriginal);

                for(int i = 0; i < rgbOriginal.length; i++){
                    rgbFinal[i] = (rgbOriginal[i]) * (rgbMask[i]);
                    rgbFinal[i] = (alpha * rgbOriginal[i]) + (( 1 - alpha) * rgbMask[i]);
                }

                rgbFinal = BeautifyUtils.clamp(rgbFinal);
                wr.setPixel(u, v, rgbFinal);

            }
        }
        return output;
    }

}