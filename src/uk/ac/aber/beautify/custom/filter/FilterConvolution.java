package uk.ac.aber.beautify.custom.filter;

import uk.ac.aber.beautify.utils.BeautifyUtils;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Jose Vives on 17/11/2015.
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class FilterConvolution {

    private Kernel kernel;

    private BufferedImage inputBI;
    private Raster input;
    private BufferedImage outputBI;
    private WritableRaster output;

    public FilterConvolution(int size, BufferedImage original) {
        kernel = new Kernel(size);
        this.inputBI = original;
        this.input = original.getData();
        outputBI = getNewBI();
        outputBI.setData(input);
        this.output = outputBI.getRaster();
    }

    private BufferedImage getNewBI(){
        return new BufferedImage(inputBI.getWidth(), inputBI.getHeight(), inputBI.getType());
    }

    public void blur(){

        kernel.setNormalKernel();

        int filterWidth = kernel.getFilterWidth();
        int filterHeight = kernel.getFilterHeight();

        for(int u = 0; u < input.getWidth(); u++){
            for(int v = 0; v < input.getHeight(); v++) {

                double[] inputPixels =  new double[3];

                double[] sum = {0.0, 0.0, 0.0};

                for(int i = -filterWidth; i <= filterWidth; i++){
                    for(int j = -filterHeight; j <= filterHeight; j++){

                        output.getPixel(Math.max(Math.min(u + i, input.getWidth() - 1), 0),
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
                output.setPixel(u, v, outPixel);

            }
        }
    }

    public void createNoise(){

        Random rand = new Random();

        for(int u = 0; u < input.getWidth(); u++) {
            for (int v = 0; v < input.getHeight(); v++) {

                double[] p = new double[3];
                input.getPixel(u, v, p);

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
                output.setPixel(u, v, p);

            }
        }
    }

    public void median() {

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
                        output.getPixel(Math.max(Math.min(u + i, input.getWidth() - 1), 0),
                                Math.max(Math.min(v + j, input.getHeight() - 1), 0),
                                inputPixels);
                        for(int rgb = 0; rgb < values.length; rgb++)
                            values[rgb][count] = inputPixels[rgb];
                        count++;
                    }
                }
                double[] median = calculateTheMedian(values);
                output.setPixel(u, v, median);
            }
        }
    }

    public void gaussian(){

        kernel.setGaussian();

        int filterWidth = kernel.getFilterWidth();
        int filterHeight = kernel.getFilterHeight();

        for(int u = 0; u < input.getWidth(); u++){
            for(int v = 0; v < input.getHeight(); v++) {

                double[] inputPixels =  new double[3];

                double[] outPixel = {0.0, 0.0, 0.0};

                for(int i = -filterWidth; i <= filterWidth; i++){
                    for(int j = -filterHeight; j <= filterHeight; j++){

                        output.getPixel(Math.max(Math.min(u + i, input.getWidth() - 1), 0),
                                Math.max(Math.min(v + j, input.getHeight() - 1), 0),
                                inputPixels);

                        double blurFactor = kernel.getValue(i + filterWidth, j + filterHeight);

                        outPixel[0] += inputPixels[0] * blurFactor;
                        outPixel[1] += inputPixels[1] * blurFactor;
                        outPixel[2] += inputPixels[2] * blurFactor;

                    }
                }

                output.setPixel(u, v, outPixel);

            }
        }
    }

    public BufferedImage unsharpMaskFilter(){

        BufferedImage output = BeautifyUtils.getCopy(outputBI);
        Raster in = outputBI.getData();

        BufferedImage mask = BeautifyUtils.getCopy(output);
        WritableRaster wr = mask.getRaster();

        int alpha =  1;

        for(int u = 0; u < mask.getWidth(); u++){
            for(int v = 0; v < mask.getHeight(); v++){

                double[] maskPixels = new double[3];
                double[] inputPixels = new double[3];

                in.getPixel(u, v, inputPixels);
                wr.getPixel(u, v, maskPixels);

                for(int i = 0; i < inputPixels.length; i++){
                    maskPixels[i] = Math.max(Math.min((( 1 + alpha) * inputPixels[i]) - (alpha * maskPixels[i]), 255), 0);
                }

                wr.setPixel(u, v, maskPixels);

            }
        }
        return mask;
    }

    private double[] calculateTheMedian(double[][] values) {
        double[] result = new double[3];
        for(int i = 0; i < values.length; i++){
            Arrays.sort(values[i]);
            double median;
            if(values[i].length % 2 == 0){
                median = values[i][values[0].length / 2] + values[i][values[0].length / 2 - 1] / 2;
            }else{
                median = values[i][values[0].length / 2];
            }
            result[i] = median;
        }
        return result;
    }

    public BufferedImage getOutput(){
        return this.outputBI;
    }
}
