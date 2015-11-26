package uk.ac.aber.beautify.custom.filter.kernel;

/**
 * Created by Jose Vives on 17/11/2015.
 *
 * @author Jose Vives.
 * @since 17/11/2015
 */
public class Kernel {

    protected double[][] kernel;

    public Kernel(int size) {
        this(size, size);
    }

    protected Kernel(int width, int height){
        kernel = new double[width][height];
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
                kernel[i][j] = (1d / (getWidth() * getHeight()));
    }

    public void setGaussianKernel(){
        int sigmaX = getWidth();
        int sigmaY = getHeight();
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                kernel[i][j] = Math.exp(-(( i*i + j*j) / ( 2d * (sigmaX * sigmaY) )));
            }
        }
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