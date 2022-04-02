import ar.edu.itba.cutCondition.CutCondition;
import ar.edu.itba.cutCondition.ParticlePerSide;
import ar.edu.itba.cutCondition.SlitParticleFlow;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        System.out.println(System.getProperty("test"));
        int N = Integer.parseInt(System.getProperty("N", "500"));
        int D = Integer.parseInt(System.getProperty("D", "30"));
//        int N = 200;
//        int D = 40;
        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 200;
        int subGridHeight, subGridWidth;
        subGridHeight = subGridWidth = 10;
        int consecutiveSimilarFlowBetweenSlitGrids = 10;
        int threshold = 5;
        int slitRangePerSide = 2;
        int maxFlowDifference = 10;
        CutCondition cutCondition1 = new SlitParticleFlow(consecutiveSimilarFlowBetweenSlitGrids, maxFlowDifference, slitRangePerSide, threshold);
        CutCondition cutCondition2 = new ParticlePerSide(latticeWidth, latticeHeight, 0.1);
        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight, subGridWidth, subGridHeight);
        fhpLatticeGas.run(cutCondition1);
    }
}
