package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

public class SlitParticleFlow implements CutCondition {

    private Integer previousFlow;
    private final int consecutiveSimilarFlowBetweenSlitGrids;
    private int consecutiveSimilarFlowBetweenSlitGridsCounter;
    private final int maxFlowDifference;
    private final int slitRangePerSide;


    public SlitParticleFlow(int consecutiveSimilarFlowBetweenSlitGrids, int maxFlowDifference, int slitRangePerSide) {
        this.previousFlow = null;
        this.consecutiveSimilarFlowBetweenSlitGrids = consecutiveSimilarFlowBetweenSlitGrids;
        this.consecutiveSimilarFlowBetweenSlitGridsCounter = 0;
        this.maxFlowDifference = maxFlowDifference;
        this.slitRangePerSide = slitRangePerSide;

    }

    @Override
    public boolean evaluate(Lattice lattice, int N, int D) {
        int flow = 0;
        int width = lattice.getWidth();
        int height = lattice.getHeight();

        for (int y = (height - D) / 2; y < ((height + D) / 2); y++) {
            // Rigth flow
            for (int x = (width / 2) - slitRangePerSide, counter = 0; counter < slitRangePerSide; x++, counter++) {
                flow += lattice.getLatticeNode(y, x).getState().getRightFlow();
            }

            // Left flow
            for (int x = (width / 2) + 1, counter = 0; counter < slitRangePerSide; x++, counter++) {
                flow -= lattice.getLatticeNode(y, x).getState().getLeftFlow();
            }
        }

        if (previousFlow == null || flow == 0 || previousFlow == 0 || Integer.signum(flow) == Integer.signum(previousFlow)) {
            previousFlow = flow;
            consecutiveSimilarFlowBetweenSlitGridsCounter = 0;
            return false;
        }

        if ((Math.abs(previousFlow) - Math.abs(flow)) < maxFlowDifference) {
            consecutiveSimilarFlowBetweenSlitGridsCounter++;
        }

        previousFlow = flow;


        return consecutiveSimilarFlowBetweenSlitGridsCounter == consecutiveSimilarFlowBetweenSlitGrids;
    }
}
