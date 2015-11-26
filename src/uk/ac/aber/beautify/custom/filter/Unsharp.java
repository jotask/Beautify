package uk.ac.aber.beautify.custom.filter;

import uk.ac.aber.beautify.utils.BeautifyUtils;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Created by Jose Vives on 26/11/2015.
 *
 * @author Jose Vives.
 * @since 26/11/2015
 */
public class Unsharp {

    public BufferedImage filter(BufferedImage input){

        BufferedImage output = BeautifyUtils.getCopy(input);

        Raster inputRaster = input.getData();

        double alpha = 1;

        BufferedImage mask = input;
        WritableRaster maskRaster = mask.getRaster();

        for(int u = 0; u < input.getWidth(); u++){
            for(int v = 0; v < input.getHeight(); v++){
                double[] maskPixel = new double[3];
                double[] inputPixel = new double[3];

                inputRaster.getPixel(u, v, inputPixel);
                maskRaster.getPixel(u, v, maskPixel);

                for(int i = 0; i < inputPixel.length; i++){
                    maskPixel[i] = Math.max(Math.min(((1 + alpha) * inputPixel[i]) - (alpha * maskPixel[i]), 255), 0);
                }
                maskRaster.setPixel(u, v, maskPixel);
            }
        }

        return mask;

    }

//    private BufferedImage gaussianBlur(BufferedImage input, double sigma){
//        BufferedImage xFilterImage = BeautifyUtils.getCopy(input);
//        BufferedImage outputImg = BeautifyUtils.getCopy(input);
//        Raster inputRaster = input.getData();
//        WritableRaster writabeRaster = outputImg.getRaster();
//
//
//
//        return outputImg;
//    }

}
