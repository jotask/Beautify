package uk.ac.aber.beautify.custom;

/**
 * Kernel class for build kernels.
 *
 * This class can generate kernels for few type of kernels
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class Kernel {

    /**
     * The kernel we generate
     */
    private double[][] kernel;

    /**
     * Constructor for this class
     * Initialise the kernel variable with the size we want
     * This is for a square kernnel
     *
     * @param size
     *      The size for the kernel we want
     */
    public Kernel(int size) {
        this(size, size);
    }

    /**
     * Constructor for intialise the kernel
     *
     * This method can create a no squared kernel
     *
     * @param width
     *      The width of the kernel we want build
     * @param height
     *      The height of the kernel we wan build
     */
    public Kernel(int width, int height){
        kernel = new double[width][height];
    }

    /**
     * Get the with of the actual kernel
     * @return
     *  The width of the kernel
     */
    public int getWidth(){
        return kernel.length;
    }

    /**
     * Get the height of the kernel
     * @return
     *      The height of the kernel
     */
    public int getHeight(){
        return kernel[0].length;
    }

    /**
     * Set this kernel to a normal distribution kernel
     */
    public void setNormalKernel(){
        for(int i = 0; i < getWidth(); i++)
            for(int j = 0; j < getHeight(); j++)
                kernel[i][j] = (1f / (getWidth() * getHeight()));
    }

    /**
     * For debug purposes. This print the actual kernel
     */
    public void printKernel(){
        StringBuilder builder = new StringBuilder();
        System.out.println("Width: " + getWidth() + " Height: " + getHeight());
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                builder.append(kernel[i][j]);
                if(j != getHeight() - 1){
                    builder.append(" - ");
                }
            }
            builder.append("\n");
        }
        System.out.println(builder.toString());
    }

    /**
     * Set this kernel to be a gaussian kernel
     *
     * All the values for the kernel is calcualted from the size of the kernel
     *
     */
    public void setGaussian() {

        int radius = getFilterWidth();

        double[][] k = new double[radius * 2 + 1][radius * 2 + 1];
        double total = 0.0;

        double sigma = radius / 3.0;
        double twoSigmaSquare = 2.0 * sigma * sigma;
        double sigmaRoot = Math.sqrt(twoSigmaSquare * Math.PI);

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                double distance = i*i + j*j ;
                int indexI = i + radius;
                int indexJ = j + radius;
                k[indexI][indexJ] = Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
                total += k[indexI][indexJ];
            }
        }

        for (int i = 0; i < k.length; i++) {
            for (int j = 0; j < k[0].length ; j++) {
                k[i][j] /= total;
            }
        }

        kernel = k;

    }

    /**
     * Get the radio width of the actual kernel
     * @return
     *      The radio width of the actual kernel
     */
    public int getFilterWidth(){
        return (getWidth() - 1) / 2;
    }

    /**
     * Get the radio height of the actual kernel
     * @return
     *      The radio height of the actual kernel
     */
    public int getFilterHeight(){
        return (getHeight() - 1) / 2;
    }

    /**
     * Get the value in position i and j from the actual kernel
     * @param x
     *      The i position
     * @param y
     *      The y position
     * @return
     *      the value of that position in kernel
     */
    public double getValue(int x, int y){
        return this.kernel[x][y];
    }

}