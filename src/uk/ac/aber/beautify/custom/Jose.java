package uk.ac.aber.beautify.custom;

import uk.ac.aber.beautify.filters.Filter;
import uk.ac.aber.beautify.utils.BeautifyUtils;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Created by Jose Vives on 14/11/2015.
 *
 * @author Jose Vives.
 * @since 14/11/2015
 */
public class Jose extends Filter{

    /**
     * Filter for noise removal -> Convert to HSV ->
     * Brightness/contrast adjust on Saturation ->
     * Histogram equalisation on Value ->
     * Convert to RGB
     */

    public Jose() {
        this.setName("JoseFilter");
    }

    @Override
    public BufferedImage filter(BufferedImage input) {

        BufferedImage output = BeautifyUtils.getCopy(input);
        Raster inputRater = input.getData();
        WritableRaster outputRaster = output.getRaster();

        for(int u = 0; u < inputRater.getWidth(); u++){
            for(int v = 0; v < inputRater.getHeight(); v++){

                double[] rgb = new double[3];

                inputRater.getPixel(u, v, rgb);

                double[] hsv = BeautifyUtils.RGB2HSV(rgb);

                rgb = BeautifyUtils.HSV2RGB(hsv);

                double[] lab = BeautifyUtils.RGBtoLAB(rgb);

                lab[2] += 10;

                rgb = BeautifyUtils.LABtoRGB(lab);

                rgb = BeautifyUtils.clamp(rgb);
                outputRaster.setPixel(u, v, rgb);

            }
        }

        return output;
    }

}
