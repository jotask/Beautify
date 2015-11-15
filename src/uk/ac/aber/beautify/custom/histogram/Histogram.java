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

    private int[] maxValueHistogram;
    private int[] maxValueCumulative;

    public Histogram(BufferedImage image) {

        this.image = image;

        histogram = new int[256][3];
        cumulative = new int[256][3];
        maxValueHistogram = new int[3];
        maxValueCumulative = new int[3];

        for(int i = 0; i < histogram.length; i++){
            for(int rgb = 0; rgb < histogram[0].length; rgb++){
                histogram[i][rgb] = 0;
                cumulative[i][rgb] = 0;
            }
        }

        for(int rgb = 0; rgb < maxValueHistogram.length; rgb++){
            maxValueHistogram[rgb] = 0;
            maxValueCumulative[rgb] = 0;
        }


        {

            frame = new JFrame();
            frame.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 3));
            frame.getContentPane().add(panel);
            frame.setTitle("Histogram");
            frame.setSize(new Dimension(255*3, 120*2));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            panel.add(new HistogramPanel(histogram, maxValueHistogram, CHANNEL.RED));
            panel.add(new HistogramPanel(histogram, maxValueHistogram, CHANNEL.GREEN));
            panel.add(new HistogramPanel(histogram, maxValueHistogram, CHANNEL.BLUE));

            panel.add(new HistogramPanel(cumulative, maxValueCumulative, CHANNEL.RED));
            panel.add(new HistogramPanel(cumulative, maxValueCumulative, CHANNEL.GREEN));
            panel.add(new HistogramPanel(cumulative, maxValueCumulative, CHANNEL.BLUE));

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

        for(int rgb = 0; rgb < maxValueCumulative.length; rgb++) {
            maxValueCumulative[rgb] = cumulative[254][rgb];
        }
    }

    public void showHistogram(){
        this.frame.setVisible(true);
    }
}
