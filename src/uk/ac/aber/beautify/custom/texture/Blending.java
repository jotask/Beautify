package uk.ac.aber.beautify.custom.texture;

import uk.ac.aber.beautify.utils.BeautifyUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Jose Vives on 29/11/2015.
 *
 * @author Jose Vives.
 * @since 29/11/2015
 */
public abstract class Blending {

    private static BufferedImage getMask(int width, int height){

        URL url = Blending.class.getResource("/uk/ac/aber/beautify/custom/texture/gaussian.jpg");

        System.out.println(url);

        BufferedImage mask = null;
        try {
            mask = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return scaleMask(mask, width, height);
    }

    private static BufferedImage scaleMask(BufferedImage src, int w, int h){
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        for (x = 0; x < w; x++) {
            for (y = 0; y < h; y++) {
                int col = src.getRGB(x * ww / w, y * hh / h);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }

    public static BufferedImage multiply(BufferedImage input){

        double alpha = 0.75;

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