package uk.ac.aber.beautify.custom.filter;

import uk.ac.aber.beautify.custom.histogram.Histogram;
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
public class Jota extends Filter{

    /**
     * Filter for noise removal -> Convert to HSV ->
     * Brightness/contrast adjust on Saturation ->
     * Histogram equalisation on Value -> Convert to RGB
     */

    public Jota() {
        this.setName("JotaFilter");
    }

    @Override
    public BufferedImage filter(BufferedImage ip) {
        BufferedImage outputImage = new BufferedImage(ip.getWidth(), ip.getHeight(), ip.getType());
        WritableRaster raster = outputImage.getRaster();

        Histogram histogram = new Histogram(ip);
        histogram.showHistogram();

        for (int u = 0; u < ip.getWidth(); u++) {
            for (int v = 0; v < ip.getHeight(); v++) {

                // ---------------------------------------
                //                  RGB
                // ---------------------------------------

                int pixel = ip.getRGB(u, v);

                int[] rgb = BeautifyUtils.convertToRGB(pixel);

//                for(int i = 0; i < rgb.length; i++){
//                    rgb[i] += 5;
//                }


                // ---------------------------------------
                //                  HSV
                // ---------------------------------------
                float hsv[] = BeautifyUtils.RGBtoHSV(rgb);

                rgb = BeautifyUtils.HSVtoRGB(hsv);

                raster.setPixel(u, v, rgb);
            }
        }

        return outputImage;
    }

}
