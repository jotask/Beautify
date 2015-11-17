package uk.ac.aber.beautify.custom.filter;

/**
 * Created by Jose Vives on 17/11/2015.
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class Kernel {

    public int[][] kernel;

    public Kernel(int size) {
        kernel = new int[size][size];
    }

    public int getSize(){
        return kernel.length;
    }

}
