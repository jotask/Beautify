package uk.ac.aber.beautify.utils.histogram;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jose Vives on 15/11/2015.
 *
 * @author Jose Vives.
 * @since 15/11/2015
 */
public class HistogramPanel extends JPanel {

    private int[][] histogram;
    private int[] maxVal;
    private Histogram.CHANNEL channel;

    private double stepSize;

    public int OFFSET = 0;

    public HistogramPanel(int[][] histogram, int[] maxVal, Histogram.CHANNEL channel) {
        super();
        this.histogram = histogram;
        this.maxVal = maxVal;
        this.channel = channel;
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(channel.getColor());

        OFFSET *= this.channel.getOffset();

            if (maxVal[channel.getChannel()] != 0) {
                stepSize = ((double) this.getWidth() - OFFSET * 2) / (double) 255;

                for (int i = 0; i < histogram.length; i++) {
                    g.drawLine((int) Math.round(i * stepSize) + OFFSET, this.getHeight(), (int) Math.round(i * stepSize) + OFFSET, this.getHeight() - (int) ((((double) histogram[i][channel.getChannel()]) / ((double) maxVal[channel.getChannel()])) * this.getHeight()));
                }
        }
    }

}
