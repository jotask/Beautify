package uk.ac.aber.beautify.custom.histogram;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jose Vives on 15/11/2015.
 *
 * @author Jose Vives.
 * @since 15/11/2015
 */
public class HistogramPanel extends JPanel {

    public enum CHANNEL {
        RED(0, Color.RED),
        GREEN(1, Color.GREEN),
        BLUE(2, Color.BLUE);

        private int channel;
        private Color color;

        CHANNEL(int i, Color color) {
            channel = i;
            this.color = color;
    }

    public int getChannel(){ return channel;}
    public Color getColor(){ return color;}
}

    private int[][] histogram;
    private int[] maxVal;
    private CHANNEL channel;

    private double stepSize;

    private int thresholdLine = -1;

    public static final int OFFSET = 25;

    public HistogramPanel(int[][] histogram, int[] maxVal) {
        super();
        this.histogram = histogram;
        this.maxVal = maxVal;
        this.channel = CHANNEL.BLUE;
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(channel.getColor());

            if (maxVal[channel.getChannel()] != 0) {
                stepSize = ((double) this.getWidth() - OFFSET * 2) / (double) 255;

                for (int i = 0; i < histogram.length; i++) {
                    g.drawLine((int) Math.round(i * stepSize) + OFFSET, this.getHeight(), (int) Math.round(i * stepSize) + OFFSET, this.getHeight() - (int) ((((double) histogram[i][channel.getChannel()]) / ((double) maxVal[channel.getChannel()])) * this.getHeight()));
                }
        }
    }

}
