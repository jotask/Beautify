package uk.ac.aber.beautify.custom;

import uk.ac.aber.beautify.utils.BeautifyUtils;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Random;

/**
 * Class for filter convolution algorithms
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class FilterConvolution {

    /**
     * Blurr filter
     *
     * This algorithm uses the 1/size kernel algorithm for apply this filter
     * @param input
     *      The image input we want use
     * @param size
     *      The size of the kernel we want use
     * @return
     *      The image with the filter applied
     */
    public static BufferedImage blur(BufferedImage input, int size){

        BufferedImage in = BeautifyUtils.getCopy(input);
        Raster raster = in.getData();

        BufferedImage out = BeautifyUtils.getCopy(input);
        WritableRaster wr = out.getRaster();

        Kernel kernel = new Kernel(size);
        kernel.setNormalKernel();

        int filterWidth = kernel.getFilterWidth();
        int filterHeight = kernel.getFilterHeight();

        for(int u = 0; u < input.getWidth(); u++){
            for(int v = 0; v < input.getHeight(); v++) {

                double[] inputPixels =  new double[3];

                double[] sum = {0.0, 0.0, 0.0};

                for(int i = -filterWidth; i <= filterWidth; i++){
                    for(int j = -filterHeight; j <= filterHeight; j++){

                        raster.getPixel(Math.max(Math.min(u + i, input.getWidth() - 1), 0),
                                        Math.max(Math.min(v + j, input.getHeight() - 1), 0),
                                        inputPixels);

                        sum[0] += inputPixels[0] * kernel.getValue(i + filterWidth, j + filterHeight);
                        sum[1] += inputPixels[1] * kernel.getValue(i + filterWidth, j + filterHeight);
                        sum[2] += inputPixels[2] * kernel.getValue(i + filterWidth, j + filterHeight);

                    }
                }

                double[] outPixel = new double[3];
                outPixel[0] = sum[0];
                outPixel[1] = sum[1];
                outPixel[2] = sum[2];
                wr.setPixel(u, v, outPixel);

            }
        }
        return out;
    }

    /**
     * Debug test method purposes
     * Create random black and white (salt and pepper) noise for tesint purposes
     * @param input
     *      The image we want apply the noise
     * @return
     *      The image with the noise created
     */
    public static BufferedImage createNoise(BufferedImage input){

        BufferedImage in = BeautifyUtils.getCopy(input);
        Raster raster = in.getData();

        BufferedImage out = BeautifyUtils.getCopy(input);
        WritableRaster wr = out.getRaster();

        Random rand = new Random();

        for(int u = 0; u < input.getWidth(); u++) {
            for (int v = 0; v < input.getHeight(); v++) {

                double[] p = new double[3];
                raster.getPixel(u, v, p);

                int r = rand.nextInt(100);
                boolean random = (r > 97) ? true : false;
                if(random){
                    r = rand.nextInt(100);
                    random = (r > 50) ? true : false;
                    for(int i = 0; i < p.length; i++) {
                        if (random){
                            p[i] = 0;
                        }else{
                            p[i] = 255;
                        }
                    }
                }
                wr.setPixel(u, v, p);
            }
        }
        return out;
    }

    /**
     * Median filter algorithm
     * They get all the values that the kernel have (all the pixel surrounded
     * delimited by the size), sum this values and get the median value
     * @param input
     *      The image to apply the filter
     * @param size
     *      The size of the filter we want use
     * @return
     *      The image with the filter created
     */
    public static BufferedImage median(BufferedImage input, int size) {

        BufferedImage in = BeautifyUtils.getCopy(input);
        Raster raster = in.getData();

        BufferedImage out = BeautifyUtils.getCopy(input);
        WritableRaster wr = out.getRaster();

        Kernel kernel = new Kernel(size);
        kernel.setNormalKernel();

        double[][] values = new double[3][kernel.getWidth() * kernel.getHeight()];
        int count;

        int filterWidth = kernel.getFilterWidth();
        int filterHeight = kernel.getFilterHeight();

        for(int u = 0; u < input.getWidth(); u++) {
            for (int v = 0; v < input.getHeight(); v++) {

                double[] inputPixels =  new double[3];
                count = 0;

                for(int i = -filterWidth; i <= filterWidth; i++){
                    for(int j = -filterHeight; j <= filterHeight; j++){
                        raster.getPixel(Math.max(Math.min(u + i, input.getWidth() - 1), 0),
                                Math.max(Math.min(v + j, input.getHeight() - 1), 0),
                                inputPixels);
                        for(int rgb = 0; rgb < values.length; rgb++)
                            values[rgb][count] = inputPixels[rgb];
                        count++;
                    }
                }
                double[] median = calculateTheMedian(values);
                wr.setPixel(u, v, median);
            }
        }
        return out;
    }

    /**
     * Method for apply the gaussian filter to an image
     * @param input
     *      The image to apply the filter
     * @param size
     *      The size of the kernel for this algorithm
     * @return
     *      The image from the result algorithm
     */
    public static BufferedImage gaussian(BufferedImage input, int size){

        Kernel kernel = new Kernel(size);
        kernel.setGaussian();

        BufferedImage in = BeautifyUtils.getCopy(input);
        Raster raster = in.getData();

        BufferedImage out = BeautifyUtils.getCopy(input);
        WritableRaster wr = out.getRaster();

        int filterWidth = kernel.getFilterWidth();
        int filterHeight = kernel.getFilterHeight();

        for(int u = 0; u < input.getWidth(); u++){
            for(int v = 0; v < input.getHeight(); v++) {

                double[] inputPixels =  new double[3];

                double[] outPixel = new double[3];

                for(int i = -filterWidth; i <= filterWidth; i++){
                    for(int j = -filterHeight; j <= filterHeight; j++){

                        raster.getPixel(Math.max(Math.min(u + i, input.getWidth() - 1), 0),
                                Math.max(Math.min(v + j, input.getHeight() - 1), 0),
                                inputPixels);

                        double blurFactor = kernel.getValue(i + filterWidth, j + filterHeight);

                        for(int tmp = 0; tmp < outPixel.length; tmp++) {
                            outPixel[tmp] += inputPixels[tmp] * blurFactor;
                        }

                    }
                }
                outPixel = BeautifyUtils.clamp(outPixel);
                wr.setPixel(u, v, outPixel);
            }
        }
        return out;
    }

    /**
     * Method for apply and unsharp filter
     * @param input
     *      The image to apply the filter
     * @param size
     *      The size of the kernel to apply the filter
     * @return
     *      The image with the filter applied
     */
    public static BufferedImage unsharpMaskFilter(BufferedImage input, int size){

        BufferedImage output = BeautifyUtils.getCopy(input);
        Raster raster = output.getData();

        BufferedImage mask = gaussian(output, size);
        WritableRaster wr = mask.getRaster();

        double alpha =  1.0;

        for(int u = 0; u < mask.getWidth(); u++){
            for(int v = 0; v < mask.getHeight(); v++){

                double[] maskPixels = new double[3];
                double[] inputPixels = new double[3];

                raster.getPixel(u, v, inputPixels);
                wr.getPixel(u, v, maskPixels);

                for(int i = 0; i < inputPixels.length; i++){
                    maskPixels[i] = Math.max(Math.min((( 1.0 + alpha) * inputPixels[i]) - (alpha * maskPixels[i]), 255.0), 0.0);
                }

                wr.setPixel(u, v, maskPixels);

            }
        }
        return mask;
    }

    /**
     * Private method for calcualte the median values for an array
     * @param values
     *      The values we want know the median
     * @return
     *      The median values
     */
    private static double[] calculateTheMedian(double[][] values) {

        /*
         * Temp array for store the median
         */
        double[] result = new double[3];

        /*
         * See each channel
         */
        for(int i = 0; i < values.length; i++){

            /*
             * First we sort the array for know the middle
             */
            Arrays.sort(values[i]);

            /*
             * Now we see the value that is in the middle
             * */
            double median;
            if(values[i].length % 2 == 0){
                // If the array is even, is not middle, so we get the two middles
                // values, we do the sum, and divided by two, that give us the median
                median = values[i][values[0].length / 2] + values[i][values[0].length / 2 - 1] / 2;
            }else{
                // of the array is even just return the middle value
                median = values[i][values[0].length / 2];
            }
            // Add the median value to the return array
            result[i] = median;
        }
        return result;
    }

}
