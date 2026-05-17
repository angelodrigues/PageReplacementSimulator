package com.unifor.simulator.core;

import java.util.ArrayList;
import java.util.List;

public class SimulationResult {
    private final String algorithmName;
    private final int pageFaults;
    private final int totalReferences;
    private final List<Snapshot> snapshots;

    public SimulationResult(String algorithmName, int pageFaults, int totalReferences, List<Snapshot> snapshots) {
        this.algorithmName = algorithmName;
        this.pageFaults = pageFaults;
        this.totalReferences = totalReferences;
        this.snapshots = snapshots;
    }

    public String getAlgorithmName() { return algorithmName; }
    public int getPageFaults() { return pageFaults; }
    public int getTotalReferences() { return totalReferences; }
    public int getHits() { return totalReferences - pageFaults; }
    public double getFaultRate() { return (double) pageFaults / totalReferences; }
    public List<Snapshot> getSnapshots() { return snapshots; }

    public static class Snapshot {
        public final int reference;
        public final List<Integer> frames;
        public final boolean fault;
        public final String note;

        public Snapshot(int reference, List<Integer> frames, boolean fault, String note) {
            this.reference = reference;
            this.frames = new ArrayList<>(frames);
            this.fault = fault;
            this.note = note;
        }
    }
}
