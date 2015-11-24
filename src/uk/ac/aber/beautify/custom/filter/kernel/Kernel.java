package uk.ac.aber.beautify.custom.filter.kernel;

/**
 * Created by Jose Vives on 17/11/2015.
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class Kernel extends java.awt.image.Kernel{

    /**
     * Constructs a <code>Kernel</code> object from an array of floats.
     * The first <code>width</code>*<code>height</code> elements of
     * the <code>data</code> array are copied.
     * If the length of the <code>data</code> array is less
     * than width*height, an <code>IllegalArgumentException</code> is thrown.
     * The X origin is (width-1)/2 and the Y origin is (height-1)/2.
     *
     * @param width  width of the kernel
     * @param height height of the kernel
     * @param data   kernel data in row major order
     * @throws IllegalArgumentException if the length of <code>data</code>
     *                                  is less than the product of <code>width</code> and
     *                                  <code>height</code>
     */
    public Kernel(int width, int height, float[] data) {
        super(width, height, data);
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
}
