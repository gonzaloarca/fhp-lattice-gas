import ar.edu.itba.cutCondition.CutCondition;
import ar.edu.itba.cutCondition.ParticlesPerSide;
import ar.edu.itba.cutCondition.SlitParticleFlow;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        String outFileName = System.getProperty("outFileName", "gas_simulation.txt");
        int N = Integer.parseInt(System.getProperty("N", "5000"));
        int D = Integer.parseInt(System.getProperty("D", "50"));

        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 200;

        CutCondition cutCondition;

        double threshold = Double.parseDouble(System.getProperty("threshold", "0.1"));
        cutCondition = new ParticlesPerSide(latticeWidth, latticeHeight, threshold);

        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight, outFileName);
        fhpLatticeGas.printStaticLattice("static_lattice.xyz");
        fhpLatticeGas.run(cutCondition);
    }
}
