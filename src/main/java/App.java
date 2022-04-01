import ar.edu.itba.cutCondition.CutCondition;
import ar.edu.itba.cutCondition.ParticlePerSide;
import ar.edu.itba.cutCondition.SlitParticleFlow;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        int N = 20;
        int D = 5;
        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 20;
        int subGridHeight, subGridWidth;
        subGridHeight = subGridWidth = 20;
        int consecutiveSimilarFlowBetweenSlitGrids = 3;
        double threshold = 0.1;
        int slitRangePerSide = 2;
        int maxFlowDifference = 10;
        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight, subGridWidth, subGridHeight);
        CutCondition cutCondition1 = new SlitParticleFlow(consecutiveSimilarFlowBetweenSlitGrids, maxFlowDifference, slitRangePerSide);
        CutCondition cutCondition2 = new ParticlePerSide(latticeWidth, latticeHeight, threshold);
        fhpLatticeGas.run(cutCondition1);
    }
}
