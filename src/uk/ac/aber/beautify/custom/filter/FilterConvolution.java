package uk.ac.aber.beautify.custom.filter;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Created by Jose Vives on 17/11/2015.
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class FilterConvolution {

    private Kernel kernel;

    public FilterConvolution(int size) {
        kernel = new Kernel(size);
    }

    public BufferedImage algorithm(BufferedImage img){
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        Raster input = img.getRaster();
        WritableRaster raster = out.getRaster();

        int size = 20;
        double[][] k = new double[size][size];
        for(int i = 0; i < k.length; i++)
            for(int j = 0; j < k[0].length; j++)
                k[i][j] = (1d / (k.length * k[0].length));

        int filterWidth = (k.length - 1) / 2;

        for(int u = 0; u < input.getWidth(); u++){
            for(int v = 0; v < input.getHeight(); v++) {

                double[] inputPixels =  new double[3];

                double[] sum = {0.0, 0.0, 0.0};

                for(int i = -filterWidth; i <= filterWidth; i++){
                    for(int j = -filterWidth; j <= filterWidth; j++){

                        input.getPixel(Math.max(Math.min(u + i, input.getWidth() - 1), 0),
                                        Math.max(Math.min(v + j, input.getHeight() - 1), 0),
                                        inputPixels);

                        sum[0] += inputPixels[0] * k[i + filterWidth][j + filterWidth];
                        sum[1] += inputPixels[1] * k[i + filterWidth][j + filterWidth];
                        sum[2] += inputPixels[2] * k[i + filterWidth][j + filterWidth];

                    }
                }

                double[] outPixel = new double[3];
                outPixel[0] = sum[0];// / (k.length * k[0].length);
                outPixel[1] = sum[1];// / (k.length * k[0].length);
                outPixel[2] = sum[2];// / (k.length * k[0].length);
                raster.setPixel(u, v, outPixel);

            }
        }
        return  out;
    }


}
