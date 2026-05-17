package com.unifor.simulator.gui;

import com.unifor.simulator.core.SimulationResult;
import com.unifor.simulator.core.Simulator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class SimulatorFrame extends JFrame {

    private final Simulator simulator = new Simulator();
    private final JTextField referenceField = new JTextField("7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1");
    private final JSpinner frameSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 64, 1));
    private final JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(20, 1, 1000, 1));
    private final JSpinner maxPageSpinner = new JSpinner(new SpinnerNumberModel(8, 2, 100, 1));
    private final ChartPanel chartPanel = new ChartPanel();
    private final JTextArea summaryArea = new JTextArea(6, 30);
    private final JTabbedPane stepsTabs = new JTabbedPane();

    public SimulatorFrame() {
        super("Simulador de Algoritmos de Substituicao de Paginas - Unifor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(buildHeader(), BorderLayout.NORTH);

        JSplitPane center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildLeftPanel(), buildRightPanel());
        center.setResizeWeight(0.55);
        center.setDividerLocation(580);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(44, 62, 80));
        header.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JLabel title = new JLabel("Simulador de Algoritmos de Substituicao de Paginas");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("FIFO  |  LRU  |  Relogio  |  Otimo");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(189, 195, 199));

        header.add(title);
        header.add(Box.createVerticalStrut(2));
        header.add(subtitle);
        return header;
    }

    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(buildInputPanel(), BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        chartPanel.setPreferredSize(new Dimension(560, 360));
        return panel;
    }

    private JPanel buildInputPanel() {
        JPanel input = new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
        input.setBorder(BorderFactory.createTitledBorder("Entrada"));

        JPanel row1 = new JPanel(new BorderLayout(6, 0));
        row1.add(new JLabel("Cadeia: "), BorderLayout.WEST);
        row1.add(referenceField, BorderLayout.CENTER);
        input.add(row1);

        JPanel row2 = new JPanel(new GridLayout(1, 4, 6, 0));
        row2.add(labeled("Molduras:", frameSpinner));
        row2.add(labeled("Tamanho aleat.:", lengthSpinner));
        row2.add(labeled("Pag. max:", maxPageSpinner));
        JButton genBtn = new JButton("Gerar aleatoria");
        genBtn.addActionListener(e -> generateRandom());
        row2.add(genBtn);
        input.add(Box.createVerticalStrut(6));
        input.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton runBtn = new JButton("Executar simulacao");
        runBtn.setBackground(new Color(46, 204, 113));
        runBtn.setForeground(Color.WHITE);
        runBtn.setFocusPainted(false);
        runBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        runBtn.addActionListener(e -> runSimulation());

        JButton clearBtn = new JButton("Limpar");
        clearBtn.addActionListener(e -> {
            referenceField.setText("");
            chartPanel.setResults(java.util.Collections.emptyList());
            summaryArea.setText("");
            stepsTabs.removeAll();
        });

        row3.add(runBtn);
        row3.add(clearBtn);
        input.add(Box.createVerticalStrut(6));
        input.add(row3);

        return input;
    }

    private JPanel labeled(String label, JSpinner spinner) {
        JPanel p = new JPanel(new BorderLayout(4, 0));
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(spinner, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildRightPanel() {
        JPanel right = new JPanel(new BorderLayout(8, 8));

        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Resumo"));
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryArea.setBackground(new Color(245, 245, 245));
        summaryPanel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        JPanel stepsPanel = new JPanel(new BorderLayout());
        stepsPanel.setBorder(BorderFactory.createTitledBorder("Passo a passo"));
        stepsPanel.add(stepsTabs, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, summaryPanel, stepsPanel);
        split.setResizeWeight(0.3);
        split.setDividerLocation(170);
        right.add(split, BorderLayout.CENTER);
        return right;
    }

    private void generateRandom() {
        int length = (int) lengthSpinner.getValue();
        int maxPage = (int) maxPageSpinner.getValue();
        int[] refs = Simulator.generateRandomReferenceString(length, maxPage, System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < refs.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(refs[i]);
        }
        referenceField.setText(sb.toString());
    }

    private void runSimulation() {
        String text = referenceField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a cadeia de referencia.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int[] refs;
        try {
            refs = Simulator.parseReferenceString(text);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cadeia invalida. Use apenas numeros inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int frames = (int) frameSpinner.getValue();

        List<SimulationResult> results = simulator.runAll(refs, frames);
        chartPanel.setResults(results);
        showSummary(results, refs, frames);
        showSteps(results);
    }

    private void showSummary(List<SimulationResult> results, int[] refs, int frames) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cadeia (").append(refs.length).append(" refs), molduras = ").append(frames).append("\n");
        sb.append("---------------------------------------------------\n");
        int idx = 1;
        SimulationResult best = results.get(0);
        for (SimulationResult r : results) {
            sb.append(String.format("Metodo %d - %-8s : %3d faltas (%.2f%%)%n",
                    idx++, r.getAlgorithmName(), r.getPageFaults(), r.getFaultRate() * 100));
            if (r.getPageFaults() < best.getPageFaults()) best = r;
        }
        sb.append("---------------------------------------------------\n");
        sb.append("Melhor: ").append(best.getAlgorithmName())
                .append(" com ").append(best.getPageFaults()).append(" faltas\n");
        summaryArea.setText(sb.toString());
        summaryArea.setCaretPosition(0);
    }

    private void showSteps(List<SimulationResult> results) {
        stepsTabs.removeAll();
        for (SimulationResult r : results) {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"#", "Ref", "Molduras", "Evento"}, 0) {
                @Override public boolean isCellEditable(int row, int col) { return false; }
            };
            int step = 1;
            for (SimulationResult.Snapshot s : r.getSnapshots()) {
                model.addRow(new Object[]{step++, s.reference, s.frames.toString(), s.note});
            }
            JTable table = new JTable(model);
            table.setRowHeight(22);
            table.getColumnModel().getColumn(0).setMaxWidth(45);
            table.getColumnModel().getColumn(1).setMaxWidth(55);
            stepsTabs.addTab(r.getAlgorithmName() + " (" + r.getPageFaults() + ")", new JScrollPane(table));
        }
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new SimulatorFrame().setVisible(true);
        });
    }
}
