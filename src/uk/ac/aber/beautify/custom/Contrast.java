package uk.ac.aber.beautify.custom;

import uk.ac.aber.beautify.utils.histogram.Histogram;
import uk.ac.aber.beautify.utils.BeautifyUtils;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Auto Adjustment contrast
 *
 * This class have different methods for the algorithm
 * Basically all are the same but with a few changes and different
 * algorithm, all written from zero.
 *
 * Not working
 *
 * @author Jose Vives.
 * @since 15/11/2015
 */
public class Contrast {

    private Histogram histogram;
    private BufferedImage image;

    private final int channel = 0;

    private int pLow;
    private int pHigh;

    /**
     * Constructor create
     *
     * @param histogram
     *      The histogram created
     * @param image
     *      The image for apply the filter
     */
    public Contrast(Histogram histogram, BufferedImage image) {
        this.histogram = histogram;
        this.image = image;

        pLow = Integer.MAX_VALUE;
        pHigh = Integer.MIN_VALUE;


        int[][] hist = histogram.getHistogramHSV();
        int[][] cum = histogram.getCumulativeHSV();

        // pLow

        main:for(int i = 0; i < cum.length; i++){
            int value = cum[i][channel];
            if(value != 0 && value < pLow){
                pLow = value;
                break main;
            }
        }

        // pHigh

        main:for(int i = cum.length - 1; i >= 0; i--){
            int value = cum[i][channel];
            if(value != 0 && value > pHigh){
                pHigh = value;
                break main;
            }
        }
        System.out.println("pLow: " + pLow + " pHigh: " + pHigh);

    }

    /**
     * Apply the auto contrast filter without the pLow and pHigh calculated
     *
     * This method has expect just return the same image
     *
     * @param img
     *      The image to apply the filter
     * @return
     *      The image calculated
     */
    public BufferedImage autoContrast(BufferedImage img){
        Raster raster = img.getData();
        BufferedImage out = BeautifyUtils.getCopy(img);
        WritableRaster wr = out.getRaster();

        for(int u = 0; u < img.getWidth(); u++){
            for(int v = 0; v < img.getHeight(); v++){

                double[] rgb = new double[3];
                raster.getPixel(u, v, rgb);

                double[] hsv = BeautifyUtils.RGBtoHSV(rgb);
                hsv[channel] = (hsv[channel] - pLow) * ( 255.0 / ( pHigh - pLow) );
                rgb = BeautifyUtils.HSVtoRGB(hsv);
                BeautifyUtils.clamp(rgb);
                wr.setPixel(u, v, rgb);

            }
        }
        return  out;
    }

    /**
     * Another attempt to calculate the contrast class
     * @param input
     *      The image we want applu the filter
     * @return
     *      The image from the resulted algorithm
     */
    public BufferedImage contrast(BufferedImage input){
        Raster raster = input.getData();
        BufferedImage output = BeautifyUtils.getEmptyCopy(input);
        WritableRaster wr = output.getRaster();

        /*
         * Build the histogram for the hsv Value channel
         */

        double[] vHistogram = new double[256];
        for(int i = 0; i < vHistogram.length; i++) vHistogram[i] = 0;

        for(int u = 0; u < raster.getWidth(); u++){
            for(int v = 0; v < raster.getHeight(); v++){
                double[] rgb = new double[3];
                raster.getPixel(u, v, rgb);
                double[] hsv = BeautifyUtils.RGB2HSV(rgb);
                vHistogram[(int)hsv[channel]]++;
            }
        }

        double[] cumulative = new double[256];
        cumulative[0] = vHistogram[0];
        for(int i = 1; i < cumulative.length; i++){
            cumulative[i] = cumulative[i - 1] + vHistogram[i];
        }

        double pLow = Double.MAX_VALUE;
        double pHigh = Double.MIN_VALUE;


        main:for(int i = 0; i < vHistogram.length; i++){
            if(((int)(vHistogram[i])) > 0){
                pLow = vHistogram[i];
                break main;
            }
        }

        main:for(int i = vHistogram.length - 1; i >= 0; i--){
            if(((int)(vHistogram[i])) < 0){
                pHigh = vHistogram[i];
                break main;
            }
        }

        /*
         * Calculate primaLow and primaHigh
         */

        double primaLow = pLow;
        double primaHigh = pHigh;
        double Q = 0.005;

        for(int i = 0; i < cumulative.length; i++){
            double a = cumulative[i];
            double b = 0;
            primaHigh = Math.min(a, b);
        }

        for(int i = cumulative.length - 1; i >= 0; i--){
            double a = cumulative[i];
            double b = 0;
            primaHigh = Math.max(a, b);
        }

        /*
         * Algorithm
         */
        for(int u = 0; u < raster.getWidth(); u++) {
            for (int v = 0; v < raster.getHeight(); v++) {
                double[] rgb = new double[3];
                raster.getPixel(u, v, rgb);
                double[] hsv = BeautifyUtils.RGB2HSV(rgb);
                hsv[channel] = function(hsv[channel], pLow, pHigh, primaLow, primaHigh);
                rgb = BeautifyUtils.HSV2RGB(hsv);
                wr.setPixel(u,v, rgb);
            }
        }

        return output;
    }

    /**
     * Function for calculate the value using the formula
     * @param value
     *      The pixel value or the channel value
     * @param pLow
     *      The pLow value
     * @param pHigh
     *      The pHigh value
     * @param primaLow
     *      The pPrimaLow value
     * @param primaHigh
     *      The pPrimaHigh value
     * @return
     *      The result og the formula
     */
    private double function(double value, double pLow, double pHigh, double primaLow, double primaHigh){
        double result;
        if(value <= primaLow) {
            result = 0;
        }else if(value >= primaHigh){
            result = 255;
        }else{
            result = (value - pLow) * (255 / (pHigh - pLow));
        }
        return result;
    }

}