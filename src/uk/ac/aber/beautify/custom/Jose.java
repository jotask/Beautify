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

        BufferedImage output = BeautifyUtils.getCopy(input);

        output = FilterConvolution.unsharpMaskFilter(output, 15, 2.0);

        WritableRaster raster = output.getRaster();

        for(int u = 0; u < raster.getWidth(); u++){
            for(int v = 0; v < raster.getHeight(); v++){

                double[] rgb = new double[3];

                raster.getPixel(u, v, rgb);

                double[] lab = BeautifyUtils.RGBtoLAB(rgb);

                lab[0] += -5;
                lab[2] += 10;

                rgb = BeautifyUtils.LABtoRGB(lab);

                rgb = BeautifyUtils.clamp(rgb);
                raster.setPixel(u, v, rgb);

            }
        }

        output = Blending.multiply(output, 0.75);

        return output;
    }

}
