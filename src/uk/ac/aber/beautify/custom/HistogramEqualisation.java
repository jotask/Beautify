package uk.ac.aber.beautify.custom;

import uk.ac.aber.beautify.utils.BeautifyUtils;
import uk.ac.aber.beautify.utils.Values;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Created by Jose Vives on 29/11/2015.
 *
 * This class try to equalise an image with the cumulative histogram
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

    /**
     *  The function for calulate the hsv value using the cumulative histogram
     * @param cumulative
     *      The builded cumulative histogram
     * @param value
     *      The value from a specified channel
     * @param width
     *      The total width and height from the image
     * @return
     *      The new value
     */
    private static double function(int[][] cumulative, double value, int width){
        double result = 0.0;
        result = Math.round((cumulative[(int) value][c.getIndex()]) * ( 255 / width ));
        return  result;
    }

    /**
     * Create the HSV histogram
     * @param img
     *      The image for get the information for build the histogram
     * @return
     *      The histogram
     */
    private static int[][] hsvH(BufferedImage img){

        /*
         * Initialise the histogram
         */
        int[][] histogram = new int[256][3];

        /*
         * Create the raster for loop the image
         * More fast than BufferedImage
         */
        Raster raster = img.getData();

        /*
         * Loop all the pixels from the image and build the histogram
         */
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

    /**
     * Create the cumulative histogram for an hsv
     * @param h
     *      The histogram builded
     * @return
     *      The cumulative histogram
     */
    private static int[][] cumulativeHSV(int[][] h){

        /*
         * Initialise the variables
         */
        int[][] cumulative = new int[256][3];

        /*
         * Add the first value from the histogram to the
         * cumulative histogram fisrt value
         */
        for(int hsv = 0; hsv < cumulative[0].length; hsv++) {
            cumulative[0][hsv] = h[0][hsv];
        }

        /*
         * Add the previously cumulative histogram value and the
         * current histogram value and set that value to the current
         * cumulative histogram value
         */
        for (int i = 1; i < cumulative.length; i++) {
            for(int hsv = 0; hsv < cumulative[0].length; hsv++) {
                cumulative[i][hsv] = cumulative[i - 1][hsv] + h[i][hsv];
            }
        }

        return  cumulative;
    }

}
