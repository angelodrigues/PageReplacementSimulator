package com.unifor.simulator.algorithms;

import com.unifor.simulator.core.PageReplacementAlgorithm;
import com.unifor.simulator.core.SimulationResult;

import java.util.ArrayList;
import java.util.List;

public class Optimal implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "Otimo";
    }

    @Override
    public SimulationResult simulate(int[] referenceString, int frameCount) {
        List<Integer> frames = new ArrayList<>();
        List<SimulationResult.Snapshot> snapshots = new ArrayList<>();
        int faults = 0;

        for (int i = 0; i < referenceString.length; i++) {
            int page = referenceString[i];
            boolean fault;
            String note;

            if (frames.contains(page)) {
                fault = false;
                note = "HIT";
            } else {
                fault = true;
                faults++;
                if (frames.size() < frameCount) {
                    frames.add(page);
                    note = "FALTA - carrega";
                } else {
                    int victimIndex = chooseVictim(frames, referenceString, i + 1);
                    int evicted = frames.get(victimIndex);
                    frames.set(victimIndex, page);
                    note = "FALTA - substitui " + evicted;
                }
            }
            snapshots.add(new SimulationResult.Snapshot(page, new ArrayList<>(frames), fault, note));
        }

        return new SimulationResult(getName(), faults, referenceString.length, snapshots);
    }

    private int chooseVictim(List<Integer> frames, int[] referenceString, int startIndex) {
        int farthest = -1;
        int victim = 0;
        for (int f = 0; f < frames.size(); f++) {
            int page = frames.get(f);
            int nextUse = Integer.MAX_VALUE;
            for (int j = startIndex; j < referenceString.length; j++) {
                if (referenceString[j] == page) {
                    nextUse = j;
                    break;
                }
            }
            if (nextUse > farthest) {
                farthest = nextUse;
                victim = f;
                if (nextUse == Integer.MAX_VALUE) break;
            }
        }
        return victim;
    }
}
