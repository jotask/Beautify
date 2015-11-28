package uk.ac.aber.beautify.custom.filter;

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
public class Jota extends Filter{

    /**
     * Filter for noise removal -> Convert to HSV ->
     * Brightness/contrast adjust on Saturation ->
     * Histogram equalisation on Value -> Convert to RGB
     */

    /**
     * Reduce noise
     * apply the gaussian filter
     * apply unsharp filter
     */

    public Jota() {
        this.setName("JoseFilter");
    }

    private boolean debug = true;

    @Override
    public BufferedImage filter(BufferedImage ip) {

        if(debug)
            return test(ip);
        return realFilter(ip);
    }

    private BufferedImage realFilter(BufferedImage input){

        BufferedImage output = BeautifyUtils.getCopy(input);
        Raster inputRater = input.getData();
        WritableRaster outputRaster = output.getRaster();

        for(int u = 0; u < inputRater.getWidth(); u++){
            for(int v = 0; v < inputRater.getHeight(); v++){

                double[] rgb = new double[3];

                inputRater.getPixel(u, v, rgb);

                double[] hsv = BeautifyUtils.RGBtoHSV(rgb);

                rgb = BeautifyUtils.HSVtoRGB(hsv);

                double[] lab = BeautifyUtils.RGBtoLAB(rgb);

                rgb = BeautifyUtils.LABtoRGB(lab);

                outputRaster.setPixel(u, v, rgb);

            }
        }

        return output;

    }

    private BufferedImage test(BufferedImage input){

        BufferedImage output = null;

        FilterConvolution fc = new FilterConvolution(7);
        output = fc.unsharpMaskFilter(input);

        return output;

    }

}
