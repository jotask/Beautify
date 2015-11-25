package uk.ac.aber.beautify.custom.filter;

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

    @Override
    public BufferedImage filter(BufferedImage ip) {

        BufferedImage out = new BufferedImage(ip.getWidth(), ip.getHeight(), ip.getType());

        return out;
    }

}
