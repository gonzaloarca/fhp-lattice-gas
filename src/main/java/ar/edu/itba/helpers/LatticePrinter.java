package ar.edu.itba.helpers;

import ar.edu.itba.models.Lattice;
import ar.edu.itba.models.State;
import ar.edu.itba.models.SubGridStatistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.function.Function;

public class LatticePrinter {
    private final static String LATTICE_FILE = "Lattice.txt";
    private final static String LATTICE_SUBGRIDS_FILE = "LatticeSubGrids.txt";
    private final static int MAX_PARTICLES_PER_CELL = 6;

    private final int N;
    private final int D;
    private final int latticeHeight;
    private final int latticeWidth;
    private final int subgridHeight;
    private final int subgridWidth;
    private final SubGridStatistics[][] subGridStatistics;
    private final int totalSubGrids;
    private final int maxParticlesPerSubGrid;


    public LatticePrinter(int latticeHeight, int latticeWidth, int subGridHeight, int subGridWidth, int n, int d) {
        this.N = n;
        this.D = d;
        this.latticeHeight = latticeHeight;
        this.latticeWidth = latticeWidth;
        this.subgridHeight = subGridHeight;
        this.subgridWidth = subGridWidth;
        this.totalSubGrids = (latticeHeight / subGridHeight) * (latticeWidth / subGridWidth);
        this.maxParticlesPerSubGrid = subGridHeight * subGridWidth * MAX_PARTICLES_PER_CELL;
        this.subGridStatistics = new SubGridStatistics[latticeHeight / subGridHeight][latticeWidth / subGridWidth];

        for (int i = 0; i < latticeHeight / subGridHeight; i++) {
            for (int j = 0; j < latticeWidth / subGridWidth; j++) {
                this.subGridStatistics[i][j] = new SubGridStatistics(subGridHeight, subGridWidth);
            }
        }
    }


    public void printInitialParameters() throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(LATTICE_FILE));
        printWriter.printf("%d\n%d\n%d\t%d\n", N, D, latticeHeight, latticeWidth);
        printWriter.close();

        printWriter = new PrintWriter(new FileWriter(LATTICE_SUBGRIDS_FILE));
        printWriter.printf("%d\n%d\n%d\t%d\n%d\t%d\n%d\n%d\n", N, D, latticeHeight, latticeWidth, subgridHeight, subgridWidth, totalSubGrids, maxParticlesPerSubGrid);
        printWriter.close();
    }

    public void printLattice(Lattice lattice, int step, boolean append) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(LATTICE_FILE, append));

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

    public void printLatticeSubGrids(Lattice lattice, int step, boolean append) throws IOException {

        PrintWriter printWriter = new PrintWriter(new FileWriter(LATTICE_SUBGRIDS_FILE, append));

        for (int i = 0; i < lattice.getHeight(); i++) {
            int subGridRow = i / subgridHeight;
            for (int j = 0; j < lattice.getWidth(); j++) {
                if (lattice.checkIsEmpty(i, j)) continue;

                State state = lattice.getLatticeNode(i, j).getState();

                int subGridColumn = j / subgridWidth;
                subGridStatistics[subGridRow][subGridColumn].processState(state);
            }
        }

        printWriter.println(step);

        for (int i = 0; i < (lattice.getHeight() / subgridHeight); i++) {
            for (int j = 0; j < (lattice.getWidth() / subgridWidth); j++) {
                SubGridStatistics subGridStats = subGridStatistics[i][j];
                if (!subGridStats.hasParticles()) continue;

                SubGridStatistics.DirectionStatistics directionStatistics = subGridStats.getAverageDirection();

                printWriter.printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%s\t%d\n", i, j, subGridStats.getTotalParticles(),
                        subGridStats.getParticlesA(), subGridStats.getParticlesB(), subGridStats.getParticlesC(),
                        subGridStats.getParticlesD(), subGridStats.getParticlesE(), subGridStats.getParticlesF(),
                        directionStatistics.getDirection(), directionStatistics.getParticles());
            }
        }

        printWriter.close();

        Arrays.stream(subGridStatistics).forEach(subGridStatisticsRow -> Arrays.stream(subGridStatisticsRow).forEach(SubGridStatistics::reset));
    }
}
