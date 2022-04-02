package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SlitParticleFlow implements CutCondition {

    private final int consecutiveIterations;
    private int consecutiveIterationsCounter;
    private final int maxFlowDifference;
    private final int measurementRegionWidth;
    private final int threshold;
    private static final String SLIT_PARTICLE_FLOW_FILE = "SlitParticleFlow.txt";


    public SlitParticleFlow(int consecutiveIterations, int maxFlowDifference, int measurementRegionWidth, int threshold) throws IOException {
        this.consecutiveIterations = consecutiveIterations;
        this.consecutiveIterationsCounter = 0;
        this.maxFlowDifference = maxFlowDifference;
        this.measurementRegionWidth = measurementRegionWidth;
        PrintWriter printWriter = new PrintWriter(new FileWriter(SLIT_PARTICLE_FLOW_FILE));
        this.threshold = threshold;
        printWriter.close();
    }

    @Override
    public boolean evaluate(Lattice lattice, int N, int D, int iteration) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(SLIT_PARTICLE_FLOW_FILE, true));

        int flow = 0;
        int width = lattice.getWidth();
        int height = lattice.getHeight();

        for (int y = (height - D) / 2; y < ((height + D) / 2); y++) {
            // Right flow
            for (int x = (width / 2) - measurementRegionWidth, xDisplacement = 0; xDisplacement < measurementRegionWidth; x++, xDisplacement++) {
                flow += lattice.getLatticeNode(y, x).getState().getRightFlow();
            }

            // Left flow
            for (int x = (width / 2) + 1, xDisplacement = 0; xDisplacement < measurementRegionWidth; x++, xDisplacement++) {
                flow -= lattice.getLatticeNode(y, x).getState().getLeftFlow();
            }
        }
        printWriter.printf("%d\t%d\n", iteration, flow);
        printWriter.close();

        if (Math.abs(flow) > threshold) {
            consecutiveIterationsCounter = 0;
            return false;
        }

        consecutiveIterationsCounter++;

        return consecutiveIterationsCounter == consecutiveIterations;
    }
}
