package uk.ac.aber.beautify.custom;

import uk.ac.aber.beautify.utils.BeautifyUtils;
import uk.ac.aber.beautify.utils.Values;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Created by Jose Vives on 29/11/2015.
 *
 * This class try to equalisate an image with the cumulative histogram
 * for the hsv value channel
 *
 * Not working
 *
 * @author Jose Vives.
 * @since 29/11/2015
 */
public abstract class HistogramEqualisation {

    /**
     * Channel for make the equalisation
     */
    private static Values c = Values.VALUE;

    /**
     * Equalisation algorithm
     * @param input the input image to be processed
     * @return
     *      The image processed
     */
    public static BufferedImage equalisation(BufferedImage input){

        BufferedImage in = BeautifyUtils.getCopy(input);
        Raster raster = in.getData();

        BufferedImage out = BeautifyUtils.getCopy(input);
        WritableRaster wr = out.getRaster();

        int[][] histogram = hsvH(input);
        int[][] cumulative = cumulativeHSV(histogram);

        int width = in.getWidth() * in.getHeight();

        for(int u = 0; u < raster.getWidth(); u++){
            for(int v = 0; v < raster.getHeight(); v++) {

                double[] rgb = new double[3];
                double[] hsv = BeautifyUtils.RGB2HSV(rgb);

                hsv[c.getIndex()] = function(cumulative, hsv[c.getIndex()], width);

                rgb = BeautifyUtils.clamp(BeautifyUtils.HSV2RGB(hsv));
                wr.setPixel(u, v, rgb);
            }
        }
        return  out;
    }

    private static double function(int[][] cumulative, double value, int width){
        double result = 0.0;
        result = Math.round((cumulative[(int) value][c.getIndex()]) * ( 255 / width ));
        return  result;
    }

    private static int[][] hsvH(BufferedImage img){

        int[][] histogram = new int[256][3];

        Raster raster = img.getData();

        for(int u = 0; u < raster.getWidth(); u++){
            for(int v = 0; v < raster.getHeight(); v++) {

                double[] rgb = new double[3];
                raster.getPixel(u, v, rgb);
                double[] hsv = BeautifyUtils.RGB2HSV(rgb);

                for(int i = 0; i < hsv.length; i++){
                    int index = (int)hsv[i];
                    histogram[index][i]++;
                }

            }
        }

        return  histogram;

    }

    private static int[][] cumulativeHSV(int[][] h){
        int[][] cumulative = new int[256][3];

        for(int hsv = 0; hsv < cumulative[0].length; hsv++) {
            cumulative[0][hsv] = h[0][hsv];
        }

        for (int i = 1; i < cumulative.length; i++) {
            for(int hsv = 0; hsv < cumulative[0].length; hsv++) {
                cumulative[i][hsv] = cumulative[i - 1][hsv] + h[i][hsv];
            }
        }

        return  cumulative;
    }

}
