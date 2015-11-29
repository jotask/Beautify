package uk.ac.aber.beautify.utils.histogram;

import uk.ac.aber.beautify.utils.BeautifyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Created by Jose Vives on 15/11/2015.
 *
 * @author Jose Vives.
 * @since 15/11/2015
 */
public class Histogram {

    public enum CHANNEL {
        RED(0, Color.RED, 0),
        GREEN(1, Color.GREEN, 255),
        BLUE(2, Color.BLUE, 255*2);

        private int channel;
        private Color color;
        private int offset;

        CHANNEL(int i, Color color, int offset) {
            channel = i;
            this.color = color;
            this.offset = offset;
        }

        public int getChannel(){ return this.channel; }
        public Color getColor(){ return this.color; }
        public int getOffset(){ return this.offset; }

    }

    private final BufferedImage image;

    private JFrame frame;

    private int[][] histogram;
    private int[][] cumulative;
    private int[][] histogramHSV;
    private int[][] cumulativeHSV;

    private int[] maxValueHistogram;
    private int[] maxValueCumulative;
    private int[] maxValueHistogramHSV;
    private int[] maxValueCumulativeHSV;

    public Histogram(BufferedImage image) {

        this.image = image;

        histogram = new int[256][3];
        cumulative = new int[256][3];
        histogramHSV = new int[256][3];
        cumulativeHSV = new int[256][3];
        maxValueHistogram = new int[3];
        maxValueCumulative = new int[3];
        maxValueHistogramHSV = new int[3];
        maxValueCumulativeHSV = new int[3];

        for(int i = 0; i < histogram.length; i++){
            for(int rgb = 0; rgb < histogram[0].length; rgb++){
                histogram[i][rgb] = 0;
                cumulative[i][rgb] = 0;
                histogramHSV[i][rgb] = 0;
                histogramHSV[i][rgb] = 0;
            }
        }

        for(int rgb = 0; rgb < maxValueHistogram.length; rgb++){
            maxValueHistogram[rgb] = 0;
            maxValueCumulative[rgb] = 0;
            maxValueHistogramHSV[rgb] = 0;
            maxValueCumulativeHSV[rgb] = 0;
        }

        buildHistogram();
        buildCumulative();
        buildHSVHistogram();
        buildCumulativeHSV();

    }

    public void buildHSVHistogram(){

        Raster rast = image.getRaster();

        for (int i = 0; i < rast.getWidth(); i++) {
            for (int j = 0; j < rast.getHeight(); j++) {

                double[] p = new double[3];
                rast.getPixel(i, j,p);

                double[] hsv = BeautifyUtils.RGB2HSV(p);

                for(int rgb = 0; rgb < hsv.length; rgb++) {

                    int index = (int) Math.round(hsv[rgb]);
                    histogramHSV[index][rgb]++;

                    // We ignore the low end and the high end to stop the effect of
                    // over-representation at the clipped regions
                    if (histogramHSV[index][rgb] > maxValueHistogramHSV[rgb] && index != 0 && index != 255) {
                        maxValueHistogramHSV[rgb] = histogramHSV[index][rgb];
                    }

                }

            }
        }
    }

    public void buildCumulativeHSV(){

        for(int rgb = 0; rgb < cumulativeHSV[0].length; rgb++) {
            cumulativeHSV[0][rgb] = histogramHSV[0][rgb];
        }

        for (int i = 1; i < cumulativeHSV.length; i++) {
            for(int rgb = 0; rgb < cumulativeHSV[0].length; rgb++) {
                cumulativeHSV[i][rgb] = cumulativeHSV[i - 1][rgb] + histogramHSV[i][rgb];
            }
        }

        for(int rgb = 0; rgb < maxValueCumulativeHSV.length; rgb++) {
            maxValueCumulativeHSV[rgb] = cumulativeHSV[254][rgb];
        }
    }

    private void buildHistogram() {
        Raster rast = image.getRaster();

        for (int i = 0; i < rast.getWidth(); i++) {
            for (int j = 0; j < rast.getHeight(); j++) {

                double[] pixels = new double[3];
                rast.getPixel(i, j, pixels);

                for(int rgb = 0; rgb < pixels.length; rgb++) {

                    int index = (int) Math.round(pixels[rgb]);
                    histogram[index][rgb] = histogram[index][rgb] + 1;

                    // We ignore the low end and the high end to stop the effect of
                    // over-representation at the clipped regions
                    if (histogram[index][rgb] > maxValueHistogram[rgb] && index != 0 && index != 255) {
                        maxValueHistogram[rgb] = histogram[index][rgb];
                    }

                }

            }
        }
    }

    private void buildCumulative() {

        for(int rgb = 0; rgb < cumulative[0].length; rgb++) {
            cumulative[0][rgb] = histogram[0][rgb];
        }

        for (int i = 1; i < cumulative.length; i++) {
            for(int rgb = 0; rgb < cumulative[0].length; rgb++) {
                cumulative[i][rgb] = cumulative[i - 1][rgb] + histogram[i][rgb];
            }
        }

        for(int rgb = 0; rgb < maxValueCumulative.length; rgb++) {
            maxValueCumulative[rgb] = cumulative[254][rgb];
        }
    }

    public void showHistogram(String title){
        showHistogram(title, false);
    }

    public void showHistogram(String title, boolean hsv){

        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3));
        frame.getContentPane().add(panel);
        frame.setTitle(title);
        frame.setSize(new Dimension(255*3, 120*2));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if(hsv){

            panel.add(new HistogramPanel(histogramHSV, maxValueHistogram, CHANNEL.RED));
            panel.add(new HistogramPanel(histogramHSV, maxValueHistogram, CHANNEL.GREEN));
            panel.add(new HistogramPanel(histogramHSV, maxValueHistogram, CHANNEL.BLUE));

            panel.add(new HistogramPanel(cumulativeHSV, maxValueCumulative, CHANNEL.RED));
            panel.add(new HistogramPanel(cumulativeHSV, maxValueCumulative, CHANNEL.GREEN));
            panel.add(new HistogramPanel(cumulativeHSV, maxValueCumulative, CHANNEL.BLUE));

        }else {

            panel.add(new HistogramPanel(histogram, maxValueHistogram, CHANNEL.RED));
            panel.add(new HistogramPanel(histogram, maxValueHistogram, CHANNEL.GREEN));
            panel.add(new HistogramPanel(histogram, maxValueHistogram, CHANNEL.BLUE));

            panel.add(new HistogramPanel(cumulative, maxValueCumulative, CHANNEL.RED));
            panel.add(new HistogramPanel(cumulative, maxValueCumulative, CHANNEL.GREEN));
            panel.add(new HistogramPanel(cumulative, maxValueCumulative, CHANNEL.BLUE));
        }

        this.frame.setVisible(true);
    }

    public int[][] getHistogram() { return  this.histogram; }
    public  int[][] getCumulative(){ return  this.cumulative; }
    public int[][] getHistogramHSV() { return  this.histogramHSV; }
    public  int[][] getCumulativeHSV(){ return  this.cumulativeHSV; }
}
