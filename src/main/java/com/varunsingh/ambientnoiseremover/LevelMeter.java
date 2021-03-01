package com.varunsingh.ambientnoiseremover;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;

/**
 * Code by Radiodef on StackOverflow
 * @see https://stackoverflow.com/a/26576548/9860982
 */
public class LevelMeter extends JComponent {
    private static final long serialVersionUID = -4525650525517436600L;

    private int meterWidth = 10;

    private float amp = 0f;
    private float peak = 0f;

    public void setAmplitude(float amp) {
        this.amp = Math.abs(amp);
        repaint();
    }

    public void setPeak(float peak) {
        this.peak = Math.abs(peak);
        repaint();
    }

    public void setMeterWidth(int meterWidth) {
        this.meterWidth = meterWidth;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = Math.min(meterWidth, getWidth());
        int h = getHeight();
        int x = getWidth() / 2 - w / 2;
        int y = 0;

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, w, h);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, w - 1, h - 1);

        int a = Math.round(amp * (h - 2));
        g.setColor(Color.GREEN);
        g.fillRect(x + 1, y + h - 1 - a, w - 2, a);

        int p = Math.round(peak * (h - 2));
        g.setColor(Color.RED);
        g.drawLine(x + 1, y + h - 1 - p, x + w - 1, y + h - 1 - p);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension min = super.getMinimumSize();
        if(min.width < meterWidth)
            min.width = meterWidth;
        if(min.height < meterWidth)
            min.height = meterWidth;
        return min;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension pref = super.getPreferredSize();
        pref.width = meterWidth;
        return pref;
    }

    @Override
    public void setPreferredSize(Dimension pref) {
        super.setPreferredSize(pref);
        setMeterWidth(pref.width);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Meter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel content = new JPanel(new BorderLayout());
                content.setBorder(new EmptyBorder(25, 50, 25, 50));

                LevelMeter meter = new LevelMeter();
                meter.setPreferredSize(new Dimension(9, 100));
                content.add(meter, BorderLayout.CENTER);

                frame.setContentPane(content);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                new Thread(new Recorder(meter)).start();
            }
        });
    }

    static class Recorder implements Runnable {
        final LevelMeter meter;

        Recorder(final LevelMeter meter) {
            this.meter = meter;
        }

        @Override
        public void run() {
            AudioFormat fmt = new AudioFormat(44100f, 16, 1, true, false);
            final int bufferByteSize = 2048;
            
            TargetDataLine line;
            try {
                line = AudioSystem.getTargetDataLine(fmt);
                line.open(fmt, bufferByteSize);
            } catch(LineUnavailableException e) {
                System.err.println(e);
                return;
            }

            byte[] buf = new byte[bufferByteSize];
            float[] samples = new float[bufferByteSize / 2];

            float lastPeak = 0f;

            line.start();
            for(int b; (b = line.read(buf, 0, buf.length)) > -1;) {

                // convert bytes to samples here
                for(int i = 0, s = 0; i < b;) {
                    int sample = 0;

                    sample |= buf[i++] & 0xFF; // (reverse these two lines
                    sample |= buf[i++] << 8;   //  if the format is big endian)

                    // normalize to range of +/-1.0f
                    samples[s++] = sample / 32768f;
                }

                float rms = 0f;
                float peak = 0f;
                for(float sample : samples) {

                    float abs = Math.abs(sample);
                    if(abs > peak) {
                        peak = abs;
                    }

                    rms += sample * sample;
                }

                rms = (float)Math.sqrt(rms / samples.length);
                
                System.out.println(rms);

                if(lastPeak > peak) {
                    peak = lastPeak * 0.875f;
                }

                lastPeak = peak;

                setMeterOnEDT(rms, peak);
            }
        }

        void setMeterOnEDT(final float rms, final float peak) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    meter.setAmplitude(rms);
                    meter.setPeak(peak);
                }
            });
        }
    }
}
