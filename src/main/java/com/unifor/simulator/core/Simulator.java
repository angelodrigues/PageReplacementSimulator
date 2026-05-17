package com.unifor.simulator.core;

import com.unifor.simulator.algorithms.Clock;
import com.unifor.simulator.algorithms.FIFO;
import com.unifor.simulator.algorithms.LRU;
import com.unifor.simulator.algorithms.Optimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Simulator {

    private final List<PageReplacementAlgorithm> algorithms;

    public Simulator() {
        this.algorithms = Arrays.asList(new FIFO(), new LRU(), new Clock(), new Optimal());
    }

    public List<PageReplacementAlgorithm> getAlgorithms() {
        return algorithms;
    }

    public List<SimulationResult> runAll(int[] referenceString, int frameCount) {
        List<SimulationResult> results = new ArrayList<>();
        for (PageReplacementAlgorithm alg : algorithms) {
            results.add(alg.simulate(referenceString, frameCount));
        }
        return results;
    }

    public static int[] generateRandomReferenceString(int length, int maxPage, long seed) {
        Random r = new Random(seed);
        int[] refs = new int[length];
        for (int i = 0; i < length; i++) {
            refs[i] = r.nextInt(maxPage);
        }
        return refs;
    }

    public static int[] parseReferenceString(String input) {
        String[] tokens = input.trim().split("[\\s,;]+");
        int[] refs = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            refs[i] = Integer.parseInt(tokens[i]);
        }
        return refs;
    }
}
