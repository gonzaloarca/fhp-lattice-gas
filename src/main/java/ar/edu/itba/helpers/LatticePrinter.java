package ar.edu.itba.helpers;

import ar.edu.itba.models.Lattice;
import ar.edu.itba.models.State;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

public class LatticePrinter {

    private final int N;
    private final int D;
    private final int latticeHeight;
    private final int latticeWidth;
    private final String outputFileName;

    public LatticePrinter(int latticeHeight, int latticeWidth, int n, int d, String outputFileName){
        this.N = n;
        this.D = d;
        this.latticeHeight = latticeHeight;
        this.latticeWidth = latticeWidth;
        this.outputFileName = outputFileName;
    }

    public void printStaticLattice(String outputFile, Lattice lattice) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(outputFile));
        printWriter.printf("%d\ncomment\n", latticeHeight * latticeWidth);
        for (int y = 0; y < latticeHeight; y++) {
            for (int x = 0; x < latticeWidth; x++) {
                printWriter.printf("%f\t%f\t0\t%s\n", (x + (y % 2 == 0 ? 0.5 : 0)), (y * 0.866), lattice.getLatticeNode(y, x).getState().getYS() || lattice.getLatticeNode(y, x).getState().getXS() ? "1 0 0" : "0 0 1");
            }
        }
        printWriter.close();
    }

    public void printInitialParameters() throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(outputFileName));
        printWriter.printf("%d\n%d\n%d\t%d\n", N, D, latticeHeight, latticeWidth);
        printWriter.close();
    }

    public void printLattice(Lattice lattice, int step, boolean append) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(outputFileName, append));

        Function<Boolean, Integer> booleanToInt = b -> b ? 1 : 0;

        printWriter.println(step);

        for (int y = 0; y < lattice.getHeight(); y++) {
            for (int x = 0; x < lattice.getWidth(); x++) {
                if (lattice.checkIsEmpty(y, x)) continue;
                State state = lattice.getLatticeNode(y, x).getState();
                printWriter.printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n", x, y, booleanToInt.apply(state.getA()),
                        booleanToInt.apply(state.getB()), booleanToInt.apply(state.getC()), booleanToInt.apply(state.getD()),
                        booleanToInt.apply(state.getE()), booleanToInt.apply(state.getF()), booleanToInt.apply(state.getXS()),
                        booleanToInt.apply(state.getYS()), booleanToInt.apply(state.getR()));
            }
        }

        printWriter.close();
    }


}
