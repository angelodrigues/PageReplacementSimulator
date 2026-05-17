package com.unifor.simulator.algorithms;

import com.unifor.simulator.core.PageReplacementAlgorithm;
import com.unifor.simulator.core.SimulationResult;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FIFO implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "FIFO";
    }

    @Override
    public SimulationResult simulate(int[] referenceString, int frameCount) {
        Deque<Integer> queue = new ArrayDeque<>();
        Set<Integer> resident = new HashSet<>();
        List<SimulationResult.Snapshot> snapshots = new ArrayList<>();
        int faults = 0;

        for (int page : referenceString) {
            boolean fault;
            String note;
            if (resident.contains(page)) {
                fault = false;
                note = "HIT";
            } else {
                fault = true;
                faults++;
                if (queue.size() >= frameCount) {
                    int evicted = queue.pollFirst();
                    resident.remove(evicted);
                    note = "FALTA - substitui " + evicted;
                } else {
                    note = "FALTA - carrega";
                }
                queue.addLast(page);
                resident.add(page);
            }
            snapshots.add(new SimulationResult.Snapshot(page, new ArrayList<>(queue), fault, note));
        }

        return new SimulationResult(getName(), faults, referenceString.length, snapshots);
    }
}
