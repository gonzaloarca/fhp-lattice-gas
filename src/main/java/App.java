import ar.edu.itba.cutCondition.CutCondition;
import ar.edu.itba.cutCondition.ParticlesPerSide;
import ar.edu.itba.cutCondition.SlitParticleFlow;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        String cutConditionName = System.getProperty("cutCondition");

        if (cutConditionName == null) {
            System.out.println("No cut condition specified. Using default.");
            cutConditionName = "ParticlesPerSide";
        }

        int N = Integer.parseInt(System.getProperty("N", "5000"));
        int D = Integer.parseInt(System.getProperty("D", "50"));

        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 200;
        int subGridHeight, subGridWidth;
        subGridHeight = subGridWidth = 10;

        CutCondition cutCondition;
        if (cutConditionName.equals("ParticlesPerSide")) {
            double threshold = Double.parseDouble(System.getProperty("threshold", "0.1"));
            cutCondition = new ParticlesPerSide(latticeWidth, latticeHeight, threshold);
        } else if(cutConditionName.equals("SlitParticleFlow")) {
            int threshold = Integer.parseInt(System.getProperty("threshold", "10"));
            int consecutiveIterations = Integer.parseInt(System.getProperty("consecutiveIterations", "10"));
            int measurementRegionWidth = Integer.parseInt(System.getProperty("measurementRegionWidth", "10"));
            cutCondition = new SlitParticleFlow(consecutiveIterations, measurementRegionWidth, threshold);
        } else {
            throw new IllegalArgumentException("Invalid cut condition name");
        }
        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight, subGridWidth, subGridHeight);
        fhpLatticeGas.printStaticLattice("static_lattice.xyz");
        fhpLatticeGas.run(cutCondition);
    }
}
