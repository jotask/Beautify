package uk.ac.aber.beautify.custom.filter;

import uk.ac.aber.beautify.custom.histogram.Histogram;
import uk.ac.aber.beautify.filters.Filter;

import java.awt.image.BufferedImage;

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
        this.setName("JoseFilter");
    }

    @Override
    public BufferedImage filter(BufferedImage ip) {
        BufferedImage outputImage = new BufferedImage(ip.getWidth(), ip.getHeight(), ip.getType());

        Histogram histogram = new Histogram("Initial Histogram", ip);
        histogram.showHistogram();

        FilterConvolution f = new FilterConvolution(3);
        outputImage = f.algorithm(ip);

        Histogram result = new Histogram("Result Histogram", outputImage);
        result.showHistogram();

        return outputImage;
    }

}
