package uk.ac.aber.beautify.custom.filter;

import java.awt.image.BufferedImage;

/**
 * Created by Jose Vives on 26/11/2015.
 *
 * @author Jose Vives.
 * @since 26/11/2015
 */
public abstract class AbstractFilter {


    public abstract BufferedImage filter(BufferedImage src);

}
