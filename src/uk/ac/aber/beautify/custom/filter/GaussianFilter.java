package uk.ac.aber.beautify.custom.filter;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Created by Jose Vives on 19/11/2015.
 *
 * @author Jose Vives.
 * @since 19/11/2015
 */
public class GaussianFilter {

    BufferedImageOp opHor;
    BufferedImageOp opVer;

    double sigma;
    int size;

    public GaussianFilter(double sigma, int size) {
        this.sigma = sigma;
        this.size = size;

        BufferedImageOp horizontal = null;
        BufferedImageOp vertical = null;

        if(sigma != 0){
            {
                float[] kernelHorizontal = gausianKernel(sigma, size);
                Kernel kernelH = new Kernel(kernelHorizontal.length, 1, kernelHorizontal);
                horizontal = new ConvolveOp(kernelH, ConvolveOp.EDGE_NO_OP, null);
            }

            {
                float[] kernelVertical = gausianKernel(sigma, size);
                Kernel kernelV = new Kernel(kernelVertical.length, 1, kernelVertical);
                vertical = new ConvolveOp(kernelV, ConvolveOp.EDGE_NO_OP, null);
            }
        }
        this.opHor = horizontal;
        this.opVer = vertical;
    }

    private float[] gausianKernel(double sigma, int size){
        double[] dvs = new double[size * 2 + 1];
        double sum = 0.0;
        for(int i = 0; i < dvs.length; i++){

            double x = i - size;
            double v = 1.0 / Math.sqrt( 2 * Math.PI * sigma * sigma) * Math.exp(-x * x / ( 2 * sigma * sigma));

            dvs[i] = v;
            sum += v;

        }

        float[] res = new float[size * 2 +1];
        for(int i = 0; i < res.length; i++){
            res[i] = (float)(dvs[i] / sum);
        }

        return res;

    }

    public BufferedImage filter(BufferedImage img){
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        if(opHor != null){
            out = opHor.filter(img, null);
        }
        if(opVer != null){
            out = opVer.filter(img, null);
        }
        return out;
    }

}
