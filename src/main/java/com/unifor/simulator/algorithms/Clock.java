package com.unifor.simulator.algorithms;

import com.unifor.simulator.core.PageReplacementAlgorithm;
import com.unifor.simulator.core.SimulationResult;

import java.util.ArrayList;
import java.util.List;

public class Clock implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "Relogio";
    }

    @Override
    public SimulationResult simulate(int[] referenceString, int frameCount) {
        Integer[] frames = new Integer[frameCount];
        boolean[] referenceBit = new boolean[frameCount];
        int pointer = 0;
        int filled = 0;
        int faults = 0;

        List<SimulationResult.Snapshot> snapshots = new ArrayList<>();

        for (int page : referenceString) {
            boolean fault = true;
            String note;

            int hitIndex = -1;
            for (int i = 0; i < filled; i++) {
                if (frames[i] != null && frames[i] == page) {
                    hitIndex = i;
                    break;
                }
            }

            if (hitIndex != -1) {
                referenceBit[hitIndex] = true;
                fault = false;
                note = "HIT - bit R = 1";
            } else {
                faults++;
                if (filled < frameCount) {
                    frames[filled] = page;
                    referenceBit[filled] = false;
                    filled++;
                    note = "FALTA - carrega";
                } else {
                    while (referenceBit[pointer]) {
                        referenceBit[pointer] = false;
                        pointer = (pointer + 1) % frameCount;
                    }
                    int evicted = frames[pointer];
                    frames[pointer] = page;
                    referenceBit[pointer] = false;
                    note = "FALTA - substitui " + evicted + " (pos " + pointer + ")";
                    pointer = (pointer + 1) % frameCount;
                }
            }

            List<Integer> snap = new ArrayList<>();
            for (int i = 0; i < filled; i++) snap.add(frames[i]);
            snapshots.add(new SimulationResult.Snapshot(page, snap, fault, note));
        }

        return new SimulationResult(getName(), faults, referenceString.length, snapshots);
    }
}
