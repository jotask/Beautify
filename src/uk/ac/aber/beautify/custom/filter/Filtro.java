package uk.ac.aber.beautify.custom.filter;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Created by Jose Vives on 20/11/2015.
 *
 * @author Jose Vives.
 * @since 20/11/2015
 */
public class Filtro {

    private BufferedImage inputImage;
    private Raster input;

    private BufferedImage outputImage;
    private WritableRaster output;

    public Filtro(BufferedImage inputImage) {
        this.inputImage = inputImage;
        this.input = inputImage.getRaster();

        outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
        output = outputImage.getRaster();
    }

    public void gaussianFilter(){

    }

    public BufferedImage getOut(){
        return outputImage;
    }
}
