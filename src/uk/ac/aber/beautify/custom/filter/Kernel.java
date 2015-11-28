package uk.ac.aber.beautify.custom.filter;

/**
 * Created by Jose Vives on 17/11/2015.
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class Kernel {

    private double[][] kernel;

    public Kernel(int size) {
        kernel = new double[size][size];
    }

    public int getWidth(){
        return kernel.length;
    }

    public int getHeight(){
        return kernel[0].length;
    }

    public void setNormalKernel(){
        for(int i = 0; i < getWidth(); i++)
            for(int j = 0; j < getHeight(); j++)
                kernel[i][j] = (1f / (getWidth() * getHeight()));
    }

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

    public void setGaussian() {

        int radius = getRadius();

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

    public int getRadius(){
        return (getWidth() - 1) / 2;
    }

    public int getFilterWidth(){
        return (getWidth() - 1) / 2;
    }

    public int getFilterHeight(){
        return (getHeight() - 1) / 2;
    }

    public double getValue(int i, int j){
        return this.kernel[i][j];
    }

}