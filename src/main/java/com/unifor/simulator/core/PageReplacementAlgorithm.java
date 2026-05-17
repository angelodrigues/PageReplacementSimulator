package com.unifor.simulator.core;

public interface PageReplacementAlgorithm {
    String getName();
    SimulationResult simulate(int[] referenceString, int frameCount);
}
