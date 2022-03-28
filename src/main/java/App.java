import ar.edu.itba.cutCondition.ParticleFlow;
import ar.edu.itba.cutCondition.ParticlePerSide;
import ar.edu.itba.helpers.LatticePrinter;
import ar.edu.itba.models.Lattice;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        int N = 2000;
        int D = 50;
        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 200;
        int subGridHeight, subGridWidth;
        subGridHeight = subGridWidth = 20;
        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight, subGridWidth, subGridHeight);
//        fhpLatticeGas.run(new ParticlePerSide(latticeWidth, latticeHeight, 0.1));
        fhpLatticeGas.run(new ParticleFlow());
    }
}
