package uk.ac.aber.beautify.custom;

import uk.ac.aber.beautify.custom.texture.Blending;
import uk.ac.aber.beautify.filters.Filter;
import uk.ac.aber.beautify.utils.BeautifyUtils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Created by Jose Vives on 14/11/2015.
 *
 * @author Jose Vives.
 * @since 14/11/2015
 */
public class Jose extends Filter{

    public Jose() {
        this.setName("JoseFilter");
    }

    @Override
    public BufferedImage filter(BufferedImage input) {

        // Create a copy from the original image for work on this
        // new image
        BufferedImage output = BeautifyUtils.getCopy(input);

        // Firstly we sharp the image using a gaussian filter, and
        // later a unsharp mask filter
        output = FilterConvolution.unsharpMaskFilter(output, 15, 2.0);

        // Start the point pixel value operations

        WritableRaster raster = output.getRaster();

        // Loop all the pixels
        for(int u = 0; u < raster.getWidth(); u++){
            for(int v = 0; v < raster.getHeight(); v++){

                // Get rgb values from the current pixel
                double[] rgb = new double[3];
                raster.getPixel(u, v, rgb);

                // Convert from rgb to lab
                double[] lab = BeautifyUtils.RGBtoLAB(rgb);

                // Decrease the Lightness
                lab[0] += -5;

                // The a and b values are the color-component dimension and
                // I find that adding 10 to the b channel makes a sepia color
                // effect
                lab[2] += 10;

                // Convert LAB value to RGB values
                rgb = BeautifyUtils.LABtoRGB(lab);

                // Just for safety properties clamp the RGG values
                rgb = BeautifyUtils.clamp(rgb);

                // Set this to the result image
                raster.setPixel(u, v, rgb);

            }
        }

        // Blending this image with a overlay image for make a the border darker
        output = Blending.multiply(output, 0.75);

        return output;
    }

}
