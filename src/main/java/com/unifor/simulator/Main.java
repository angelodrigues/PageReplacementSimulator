package com.unifor.simulator;

import com.unifor.simulator.core.SimulationResult;
import com.unifor.simulator.core.Simulator;
import com.unifor.simulator.gui.SimulatorFrame;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equalsIgnoreCase("--gui") || args[0].equalsIgnoreCase("-g"))) {
            SimulatorFrame.launch();
            return;
        }
        runConsole();
    }

    private static void runConsole() {
        Scanner sc = new Scanner(System.in);
        Simulator simulator = new Simulator();

        System.out.println("=========================================================");
        System.out.println(" Simulador de Algoritmos de Substituicao de Paginas");
        System.out.println(" Unifor - Ciencia da Computacao");
        System.out.println("=========================================================");

        int[] refs = readReferenceString(sc);
        int frames = readFrameCount(sc);
        boolean verbose = readVerbose(sc);

        System.out.println();
        System.out.println("Cadeia de referencia: " + arrayToString(refs));
        System.out.println("Numero de molduras: " + frames);
        System.out.println("Total de referencias: " + refs.length);
        System.out.println();

        List<SimulationResult> results = simulator.runAll(refs, frames);

        if (verbose) {
            for (SimulationResult r : results) {
                printVerbose(r);
            }
        }

        printSummary(results);
        sc.close();
    }

    private static int[] readReferenceString(Scanner sc) {
        System.out.println("Digite a cadeia de referencia (numeros separados por espaco/virgula)");
        System.out.println("ou pressione ENTER para gerar aleatoriamente:");
        System.out.print("> ");
        String line = sc.nextLine();
        if (line.trim().isEmpty()) {
            int[] refs = Simulator.generateRandomReferenceString(20, 8, System.currentTimeMillis());
            System.out.println("Gerada: " + arrayToString(refs));
            return refs;
        }
        return Simulator.parseReferenceString(line);
    }

    private static int readFrameCount(Scanner sc) {
        System.out.print("Numero de molduras (frames) [padrao 3]: ");
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return 3;
        return Integer.parseInt(line);
    }

    private static boolean readVerbose(Scanner sc) {
        System.out.print("Mostrar passo a passo? (s/N): ");
        String line = sc.nextLine().trim().toLowerCase();
        return line.equals("s") || line.equals("sim") || line.equals("y");
    }

    private static void printVerbose(SimulationResult r) {
        System.out.println();
        System.out.println("--- " + r.getAlgorithmName() + " ---");
        System.out.printf("%-5s %-8s %-25s %s%n", "PASSO", "REF", "MOLDURAS", "EVENTO");
        int step = 1;
        for (SimulationResult.Snapshot s : r.getSnapshots()) {
            System.out.printf("%-5d %-8d %-25s %s%n", step++, s.reference, s.frames.toString(), s.note);
        }
        System.out.println("Total de faltas: " + r.getPageFaults());
    }

    private static void printSummary(List<SimulationResult> results) {
        System.out.println();
        System.out.println("=========================================================");
        System.out.println(" RESULTADOS - Faltas de pagina por algoritmo");
        System.out.println("=========================================================");
        int idx = 1;
        for (SimulationResult r : results) {
            System.out.printf("Metodo %d (%s) - %d faltas de pagina (taxa = %.2f%%)%n",
                    idx++, r.getAlgorithmName(), r.getPageFaults(), r.getFaultRate() * 100);
        }
        System.out.println("=========================================================");
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i]);
        }
        return sb.append("]").toString();
    }
}
