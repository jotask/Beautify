package uk.ac.aber.beautify.custom.filter;

/**
 * Created by Jose Vives on 17/11/2015.
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class Kernel {

    private double[][] kernel;
    private int size;

    public Kernel(int size) {
        this.size = size;
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

        int radius;
        radius = size / 2;

        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        double[][] data = new double[radius * 2 + 1][radius * 2 + 1];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                float distanceI = i * i;
                float distanceJ = j * j;
                int indexI = i + radius;
                int indexJ = j + radius;
                data[indexI][indexJ] = (float)((Math.exp(-distanceI / twoSigmaSquare) + Math.exp(-distanceJ / twoSigmaSquare)) / sigmaRoot);
                total += data[indexI][indexJ];
            }
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                data[i][j] /= total;
            }
        }

        kernel = data;

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