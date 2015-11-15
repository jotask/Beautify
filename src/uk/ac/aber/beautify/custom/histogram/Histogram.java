package uk.ac.aber.beautify.custom.histogram;

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

    private final BufferedImage image;

    private JFrame histogramFrame;
    private HistogramPanel histogramPanel;
    private int[][] histogram;

    private JFrame cumulativeFrame;
    private HistogramPanel cumulativePanel;
    private int[][] cumulative;

    private int[] maxValueHistogram;
    private int[] macValueCumulative;

    public Histogram(BufferedImage image) {

        this.image = image;

        histogram = new int[256][3];
        cumulative = new int[256][3];
        maxValueHistogram = new int[3];
        macValueCumulative = new int[3];

        for(int i = 0; i < histogram.length; i++){
            for(int rgb = 0; rgb < histogram[0].length; rgb++){
                histogram[i][rgb] = 0;
                cumulative[i][rgb] = 0;
            }
        }

        for(int rgb = 0; rgb < maxValueHistogram.length; rgb++){
            maxValueHistogram[rgb] = 0;
            macValueCumulative[rgb] = 0;
        }


        {

            histogramFrame = new JFrame();
            histogramFrame.setTitle("Histogram");

            histogramFrame.setSize(new Dimension(255, 120));

            histogramPanel = new HistogramPanel(histogram, maxValueHistogram);

            histogramFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            histogramFrame.getContentPane().add(histogramPanel);

        }

        {

            cumulativeFrame = new JFrame();
            cumulativeFrame.setTitle("Cumulative");

            cumulativeFrame.setSize(new Dimension(255, 120));

            cumulativePanel = new HistogramPanel(cumulative, macValueCumulative);

            cumulativeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cumulativeFrame.getContentPane().add(cumulativePanel);

        }

        buildHistogram();
        buildCumulative();

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

        for(int rgb = 0; rgb < macValueCumulative.length; rgb++) {
            macValueCumulative[rgb] = cumulative[254][rgb];
        }
    }

    public void showHistogram(){
        this.histogramPanel.repaint();
        this.histogramFrame.setVisible(true);
    }

    public void showCumulative(){
        this.cumulativePanel.repaint();
        this.cumulativeFrame.setVisible(true);
    }
}
