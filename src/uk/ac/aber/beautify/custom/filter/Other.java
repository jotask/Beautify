package uk.ac.aber.beautify.custom.filter;

import uk.ac.aber.beautify.custom.histogram.Histogram;
import uk.ac.aber.beautify.utils.BeautifyUtils;

import java.awt.image.BufferedImage;

/**
 * Created by Jose Vives on 16/11/2015.
 *
 * @author Jose Vives.
 * @since 16/11/2015
 */
public class Other {

    private Histogram histogram;

    public Other(Histogram histogram) {
        this.histogram = histogram;
    }

    public BufferedImage algorithm(BufferedImage img){
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for(int u = 0; u < img.getWidth(); u++){
            for(int v = 0; v < img.getHeight(); v++){

                int p = 0;
                p = img.getRGB(u, v);
                int[] rgb = BeautifyUtils.convertToRGB(p);

                p = BeautifyUtils.convertToPixel(rgb);
                out.setRGB(u, v, p);

            }
        }
        return  out;
    }

}
