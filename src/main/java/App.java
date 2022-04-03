import ar.edu.itba.cutCondition.CutCondition;
import ar.edu.itba.cutCondition.ParticlesPerSide;
import ar.edu.itba.cutCondition.SlitParticleFlow;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        String simulationOutFileName = System.getProperty("simulationOutFileName", "gas_simulation.txt");
        String summaryOutFileName = System.getProperty("summaryOutFileName", "summary.txt");
        int N = Integer.parseInt(System.getProperty("N", "5000"));
        int D = Integer.parseInt(System.getProperty("D", "50"));

        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 200;

        double threshold = Double.parseDouble(System.getProperty("threshold", "0.1"));

        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight, threshold, simulationOutFileName, summaryOutFileName);
        fhpLatticeGas.printStaticLattice("static_lattice.xyz");
        fhpLatticeGas.run();
    }
}
