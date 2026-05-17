package com.unifor.simulator.algorithms;

import com.unifor.simulator.core.PageReplacementAlgorithm;
import com.unifor.simulator.core.SimulationResult;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class LRU implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "LRU";
    }

    @Override
    public SimulationResult simulate(int[] referenceString, int frameCount) {
        LinkedHashSet<Integer> usage = new LinkedHashSet<>();
        List<SimulationResult.Snapshot> snapshots = new ArrayList<>();
        int faults = 0;

        for (int page : referenceString) {
            boolean fault;
            String note;
            if (usage.contains(page)) {
                usage.remove(page);
                usage.add(page);
                fault = false;
                note = "HIT - atualiza recencia";
            } else {
                fault = true;
                faults++;
                if (usage.size() >= frameCount) {
                    int oldest = usage.iterator().next();
                    usage.remove(oldest);
                    note = "FALTA - substitui " + oldest;
                } else {
                    note = "FALTA - carrega";
                }
                usage.add(page);
            }
            snapshots.add(new SimulationResult.Snapshot(page, new ArrayList<>(usage), fault, note));
        }

        return new SimulationResult(getName(), faults, referenceString.length, snapshots);
    }
}
