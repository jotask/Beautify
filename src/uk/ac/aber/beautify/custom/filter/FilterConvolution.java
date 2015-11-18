package uk.ac.aber.beautify.custom.filter;

import uk.ac.aber.beautify.custom.filter.kernel.Kernel;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
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
        this.input = original.getRaster();
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

//    public void median() {
//
//        double[] values = new double[kernel.getWidth() * kernel.getHeight()];
//        int count;
//
//        int filterWidth = kernel.getFilterWidth();
//        int filterHeight = kernel.getFilterHeight();
//
//        for(int u = 0; u < input.getWidth(); u++) {
//            for (int v = 0; v < input.getHeight(); v++) {
//
//                double[] inputPixels =  new double[3];
//                count = 0;
//
//                for(int i = -filterWidth; i <= filterWidth; i++){
//                    for(int j = -filterHeight; j <= filterHeight; j++){
//                        output.getPixel(Math.max(Math.min(u + i, input.getWidth() - 1), 0),
//                                Math.max(Math.min(v + j, input.getHeight() - 1), 0),
//                                inputPixels);
//                        for(int rgb = 0; rgb < values.length; rgb++)
//                            values[count] = inputPixels[0];
//                        count++;
//                    }
//                }
//                double middle;
//                    Arrays.sort(values);
//                    middle = values[values.length / 2];
//                }
//                output.setPixel(u, v, middle);
//            }
//        }
//
//    }

    public BufferedImage getOutput(){
        return this.outputBI;
    }
}
