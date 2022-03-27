import ar.edu.itba.cutCondition.ParticleFlow;
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
        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight);
        List<Lattice> lattices = fhpLatticeGas.run(new ParticleFlow());
        LatticePrinter latticePrinter = new LatticePrinter(latticeHeight, latticeWidth, subGridHeight, subGridWidth, N, D);
        latticePrinter.printInitialParameters();
        for (int i = 0; i < lattices.size(); i++) {
            latticePrinter.printLattice(lattices.get(i), i, true);
            latticePrinter.printLatticeSubGrids(lattices.get(i), i, true);
        }
    }
}
