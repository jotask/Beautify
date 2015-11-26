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
public class UnsharpMaskFilter extends AbstractFilter {

    private final float amount;
    private final int radius;

    public UnsharpMaskFilter() {
        this(0.7f, 2);
    }

    public UnsharpMaskFilter(float amount, int radius) {
        this.amount = amount;
        this.radius = radius;
    }

    @Override
    public BufferedImage filter(BufferedImage input) {

        Raster raster = input.getData();
        BufferedImage output = BeautifyUtils.getCopy(input);
        WritableRaster wRaster = output.getRaster();

        int width = input.getWidth();
        int height = input.getHeight();

        GaussianFilter gaussianFilter = new GaussianFilter();


        int[] srcPixels = new int[width * height];
        int[] dstPixels = new int[width * height];

        float[] kernel = gaussianFilter.createGaussianKernel(radius);

        raster.getDataElements(0, 0, width, height, srcPixels);

        // horizontal pass
        gaussianFilter.blur(srcPixels, dstPixels, width, height, kernel, radius);

        // vertical pass
        //noinspection SuspiciousNameCombination
        gaussianFilter.blur(dstPixels, srcPixels, height, width, kernel, radius);

        // blurred image is in srcPixels, we copy the original in dstPixels
        raster.getDataElements(0, 0, width, height, dstPixels);

        // we compare original and blurred images,
        // and store the result in srcPixels
        sharpen(dstPixels, srcPixels, width, height, amount);

        // the result is now stored in srcPixels due to the 2nd pass
        wRaster.setDataElements(0, 0, width, height, srcPixels);

        return output;
    }

    static void sharpen(int[] original, int[] blurred, int width, int height, float amount) {

        int index = 0;

        amount *= 1.6f;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int srcColor[] = BeautifyUtils.convertToRGB(original[index]);
                int dstColor[] = BeautifyUtils.convertToRGB(blurred[index]);

                for(int i = 0; i < 3; i++){
                    srcColor[i] = (int) (amount * (srcColor[i] - dstColor[i]) + srcColor[i]);
                    srcColor[i] = srcColor[i] > 255 ? 255 : srcColor[i] < 0 ? 0 : srcColor[i];
                }

                int alpha = 1;
                blurred[index] = alpha | (srcColor[0] << 16) | (srcColor[1] << 8) | srcColor[2];

                index++;
            }
        }
    }

}
