package com.unifor.simulator.gui;

import com.unifor.simulator.core.SimulationResult;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.List;

public class ChartPanel extends JPanel {

    private List<SimulationResult> results = Collections.emptyList();
    private int totalReferences = 0;
    private final Color[] palette = {
            new Color(231, 76, 60),
            new Color(52, 152, 219),
            new Color(46, 204, 113),
            new Color(155, 89, 182),
            new Color(241, 196, 15),
            new Color(52, 73, 94)
    };

    public ChartPanel() {
        setBackground(Color.WHITE);
    }

    public void setResults(List<SimulationResult> results) {
        this.results = results;
        this.totalReferences = results.isEmpty() ? 0 : results.get(0).getTotalReferences();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(44, 62, 80));
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        String title = "Comparativo de Faltas de Pagina";
        Rectangle2D tb = g2.getFontMetrics().getStringBounds(title, g2);
        g2.drawString(title, (int) (w / 2 - tb.getWidth() / 2), 22);

        if (results.isEmpty() || totalReferences == 0) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("SansSerif", Font.ITALIC, 13));
            String msg = "Execute a simulacao para ver o grafico";
            Rectangle2D mb = g2.getFontMetrics().getStringBounds(msg, g2);
            g2.drawString(msg, (int) (w / 2 - mb.getWidth() / 2), h / 2);
            g2.dispose();
            return;
        }

        int marginLeft = 60;
        int marginRight = 30;
        int marginTop = 45;
        int marginBottom = 70;
        int chartW = w - marginLeft - marginRight;
        int chartH = h - marginTop - marginBottom;

        int maxFaults = totalReferences;

        g2.setColor(new Color(189, 195, 199));
        g2.setStroke(new BasicStroke(1f));
        int gridLines = 5;
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (int i = 0; i <= gridLines; i++) {
            int y = marginTop + chartH - (chartH * i / gridLines);
            g2.setColor(new Color(220, 220, 220));
            g2.drawLine(marginLeft, y, marginLeft + chartW, y);
            int value = maxFaults * i / gridLines;
            g2.setColor(new Color(100, 100, 100));
            g2.drawString(String.valueOf(value), marginLeft - 30, y + 4);
        }

        g2.setColor(new Color(44, 62, 80));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(marginLeft, marginTop, marginLeft, marginTop + chartH);
        g2.drawLine(marginLeft, marginTop + chartH, marginLeft + chartW, marginTop + chartH);

        int n = results.size();
        int barGroupW = chartW / n;
        int barW = (int) (barGroupW * 0.6);

        for (int i = 0; i < n; i++) {
            SimulationResult r = results.get(i);
            int barH = (int) ((double) r.getPageFaults() / maxFaults * chartH);
            int x = marginLeft + i * barGroupW + (barGroupW - barW) / 2;
            int y = marginTop + chartH - barH;

            Color c = palette[i % palette.length];
            g2.setColor(c);
            g2.fillRoundRect(x, y, barW, barH, 8, 8);
            g2.setColor(c.darker());
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, barW, barH, 8, 8);

            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(new Color(44, 62, 80));
            String value = String.valueOf(r.getPageFaults());
            Rectangle2D vb = g2.getFontMetrics().getStringBounds(value, g2);
            g2.drawString(value, (int) (x + barW / 2 - vb.getWidth() / 2), y - 6);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            String label = r.getAlgorithmName();
            Rectangle2D lb = g2.getFontMetrics().getStringBounds(label, g2);
            g2.drawString(label, (int) (x + barW / 2 - lb.getWidth() / 2), marginTop + chartH + 18);

            String rate = String.format("%.1f%%", r.getFaultRate() * 100);
            Rectangle2D rb = g2.getFontMetrics().getStringBounds(rate, g2);
            g2.setColor(new Color(127, 140, 141));
            g2.drawString(rate, (int) (x + barW / 2 - rb.getWidth() / 2), marginTop + chartH + 34);
        }

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(new Color(127, 140, 141));
        String footer = "Total de referencias: " + totalReferences;
        g2.drawString(footer, marginLeft, h - 10);

        g2.dispose();
    }
}
