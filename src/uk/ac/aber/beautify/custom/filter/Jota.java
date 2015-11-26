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

    /*
    Overthinking how affort this assignment, and i think i have an idea how I gonna finish this one
     */

    /**
     * Reduce noise
     * apply the gaussian filter
     * apply unsharp filter
     */

    public Jota() {
        this.setName("JoseFilter");
    }

    private boolean test = false;

    @Override
    public BufferedImage filter(BufferedImage ip) {

        if(test)
            return test(ip);
        return realFilter(ip);
    }

    private BufferedImage realFilter(BufferedImage input){

        BufferedImage output = BeautifyUtils.getCopy(input);
        Raster inputRater = input.getData();
        WritableRaster outputRaster = output.getRaster();

        for(int u = 0; u < inputRater.getWidth(); u++){
            for(int v = 0; v < inputRater.getHeight(); v++){

                int[] inputPixels = new int[3];

                inputRater.getPixel(u, v, inputPixels);

                float[] hsv = BeautifyUtils.RGBtoHSV(inputPixels);

                inputPixels = BeautifyUtils.HSVtoRGB(hsv);

                outputRaster.setPixel(u, v, inputPixels);
            }
        }

        return output;

    }

    private BufferedImage test(BufferedImage input){

        BufferedImage output = BeautifyUtils.getCopy(input);

        return output;

    }

}
