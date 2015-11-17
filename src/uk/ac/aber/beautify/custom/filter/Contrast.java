package uk.ac.aber.beautify.custom.filter;

import uk.ac.aber.beautify.custom.histogram.Histogram;
import uk.ac.aber.beautify.utils.BeautifyUtils;

import java.awt.image.BufferedImage;

/**
 * Created by Jose Vives on 15/11/2015.
 *
 * @author Jose Vives.
 * @since 15/11/2015
 */
public class Contrast {

    private Histogram histogram;
    private BufferedImage image;

    private int pLow;
    private int pHigh;

    public Contrast(Histogram histogram, BufferedImage image) {
        this.histogram = histogram;
        this.image = image;

        pLow = Integer.MAX_VALUE;
        pHigh = Integer.MIN_VALUE;


        int[][] hist = histogram.getHistogram();

        // pLow
        main:for(int i = 0; i < hist.length; i++){
            for(int rgb = 0; rgb < hist[0].length; rgb++){
                int value = hist[i][rgb];
                if(value != 0 && value < pLow){
                    pLow = value;
                    break main;
                }
            }
        }

        // pHigh
        main:for(int i = hist.length - 1; i > 0; i++){
            for(int rgb = 0; rgb < hist[0].length; rgb++){
                int value = hist[i][rgb];
                if(value != 0 && value > pHigh){
                    pHigh = value;
                    break main;
                }
            }
        }

    }

    public BufferedImage autoContrast(BufferedImage img){
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for(int u = 0; u < img.getWidth(); u++){
            for(int v = 0; v < img.getHeight(); v++){
                int pixel = img.getRGB(u, v);
                int[] rgb = BeautifyUtils.convertToRGB(pixel);
                float[] hsv = BeautifyUtils.RGBtoHSV(rgb);
                int i = 2;
                hsv[i] = (hsv[i] - pLow) * ( 255 / ( pHigh - pLow) );
                rgb = BeautifyUtils.HSVtoRGB(hsv);
                BeautifyUtils.clamp(rgb);
                pixel = BeautifyUtils.convertToPixel(rgb);
                out.setRGB(u, v, pixel);
            }
        }
        return  out;
    }

}
