import ar.edu.itba.models.Lattice;
import ar.edu.itba.models.Node;
import ar.edu.itba.models.State;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Function;

public class FHP {
    private final Map<State, State> collisionLookupTable;
    private final Lattice lattice;
    private final int N;
    private final int D;
    private final SubGridStatistics[][] subGridStatistics;
    private final int totalSubGrids;
    private final int maxParticlesPerSubGrid;
    private final int subGridHeight;
    private final int subGridWidth;

    private final static String LATTICE_FILE = "Lattice.txt";
    private final static String LATTICE_SUBGRIDS_FILE = "LatticeSubGrids.txt";
    private final static int MAX_PARTICLES_PER_CELL = 6;


    public FHP(int n, int d, int latticeWidth, int latticeHeight, int subGridHeight, int subGridWidth) throws IOException {
        this.N = n;
        this.D = d;
        this.lattice = new Lattice(latticeHeight, latticeWidth);
        this.collisionLookupTable = new HashMap<>();
        this.totalSubGrids = (latticeHeight / subGridHeight) * (latticeWidth / subGridWidth);
        this.maxParticlesPerSubGrid = subGridHeight * subGridWidth * MAX_PARTICLES_PER_CELL;
        this.subGridStatistics = new SubGridStatistics[latticeHeight / subGridHeight][latticeWidth / subGridWidth];
        this.subGridHeight = subGridHeight;
        this.subGridWidth = subGridWidth;
        initializeCollisionLookupTable();
    }

    //generate walls and non-walls
    private void generateLatticeSpace() {
        int height = this.lattice.getHeight();
        int width = this.lattice.getWidth();
        Random random = new Random();
        boolean isWall;
        boolean randomBit;
        for (int i = 0; i < height; i++) {
            int subGridRow = i / subGridHeight;
            for (int j = 0; j < width; j++) {
                int subGridColumn = j / subGridWidth;
                randomBit = random.nextBoolean();
                isWall = j == 0 || j == width - 1 || i == 0 || i == height - 1;

                // for middle wall with slit
                if (j == width / 2 && (i < (height - D) / 2 || i > (height + D) / 2)) {
                    isWall = true;
                }

                if (subGridStatistics[subGridRow][subGridColumn] == null) {
                    subGridStatistics[subGridRow][subGridColumn] = new SubGridStatistics(subGridHeight, subGridWidth);
                }

                lattice.setLatticeNode(i, j, new State(isWall, randomBit));
            }
        }
    }

    private void generateInitialState() {
        Random cellRandom = new Random();
        Random directionsRandom = new Random();
        int width = (this.lattice.getWidth() / 2);
        int height = this.lattice.getHeight();
        int cell, row, col;
        for (int i = 0; i < this.N; ) {
            cell = cellRandom.nextInt(height * width);
            row = cell / width;
            col = cell % width;

            if ((row == 0 && col == 0) || (row == width))
        }
    }

    private void initializeCollisionLookupTable() {
        State inputState;
        boolean[] stateValues;

        // iterate through all possible states and calculate the output state
        for (int i = 0; i < State.getMaxStates(); i++) {
            stateValues = new boolean[8];

            for (int j = 7; j > 0; j--) {
                stateValues[j] = (i & (1 << j)) != 0;
            }

            inputState = new State(stateValues[0], stateValues[1], stateValues[2], stateValues[3], stateValues[4], stateValues[5], stateValues[6], stateValues[7]);

            this.collisionLookupTable.put(inputState, calculateOutputState(inputState));
        }
    }

    private State calculateOutputState(State prevState) {
        boolean a = prevState.getA();
        boolean b = prevState.getB();
        boolean c = prevState.getC();
        boolean d = prevState.getD();
        boolean e = prevState.getE();
        boolean f = prevState.getF();
        boolean s = prevState.getS();
        boolean r = prevState.getR();

        boolean double1 = (a && d && !(b || c || e || f));
        boolean double2 = (b && e && !(a || c || d || f));
        boolean double3 = (c && f && !(a || b || d || e));

        boolean triple = (a ^ b) && (b ^ c) && (c ^ d) && (d ^ e) && (e ^ f);

        boolean newA = a ^ ((triple || double1 || (r && double2) || (!r && double3)) && !s);
        boolean newD = d ^ ((triple || double1 || (r && double2) || (!r && double3)) && !s);
        boolean newB = b ^ ((triple || double2 || (r && double3) || (!r && double1)) && !s);
        boolean newE = e ^ ((triple || double2 || (r && double3) || (!r && double1)) && !s);
        boolean newC = c ^ ((triple || double3 || (r && double1) || (!r && double2)) && !s);
        boolean newF = f ^ ((triple || double3 || (r && double1) || (!r && double2)) && !s);

        return new State(newA, newB, newC, newD, newE, newF, s, r);
    }

    private void nextLatticeState() {
        Lattice nextLattice = new Lattice(lattice.getWidth(), lattice.getHeight());
        for (int i = 0; i < lattice.getHeight(); i++) {
            for (int j = 0; j < lattice.getWidth(); j++) {
                State outputState = calculateOutputState(lattice.getLatticeNode(i, j).getState());
                updateNeighbors(nextLattice, outputState, i, j);
            }
        }
        lattice = nextLattice;
    }

    private void updateNeighbors(Lattice nextLattice, State nextState, int i, int j) {

        State nodeState;

        if (nextState.getA()) {
            nodeState = lattice.getLatticeNode(i, j + 1).getState();
            nextLattice.setLatticeNodeDirection(i, j + 1, Direction.A, nodeState.getS(), nodeState.getR());
        }

        if (nextState.getD()) {
            nodeState = lattice.getLatticeNode(i, j - 1).getState();
            nextLattice.setLatticeNodeDirection(i, j - 1, Direction.D, nodeState.getS(), nodeState.getR());
        }

        boolean oddRow = i % 2 == 1;

        if (nextState.getB()) {
            if (oddRow) {
                nodeState = lattice.getLatticeNode(i - 1, j).getState();
                nextLattice.setLatticeNodeDirection(i - 1, j, Direction.B, nodeState.getS(), nodeState.getR());
            } else {
                nodeState = lattice.getLatticeNode(i - 1, j + 1).getState();
                nextLattice.setLatticeNodeDirection(i - 1, j + 1, Direction.B, nodeState.getS(), nodeState.getR());
            }
        }

        if (nextState.getC()) {
            if (oddRow) {
                nodeState = lattice.getLatticeNode(i - 1, j - 1).getState();
                nextLattice.setLatticeNodeDirection(i - 1, j - 1, Direction.C, nodeState.getS(), nodeState.getR());
            } else {
                nodeState = lattice.getLatticeNode(i - 1, j).getState();
                nextLattice.setLatticeNodeDirection(i - 1, j, Direction.C, nodeState.getS(), nodeState.getR());
            }
        }

        if (nextState.getE()) {
            if (oddRow) {
                nodeState = lattice.getLatticeNode(i + 1, j - 1).getState();
                nextLattice.setLatticeNodeDirection(i + 1, j - 1, Direction.E, nodeState.getS(), nodeState.getR());
            } else {
                nodeState = lattice.getLatticeNode(i + 1, j).getState();
                nextLattice.setLatticeNodeDirection(i + 1, j, Direction.E, nodeState.getS(), nodeState.getR());
            }
        }

        if (nextState.getF()) {
            if (oddRow) {
                nodeState = lattice.getLatticeNode(i + 1, j).getState();
                nextLattice.setLatticeNodeDirection(i + 1, j, Direction.F, nodeState.getS(), nodeState.getR());
            } else {
                nodeState = lattice.getLatticeNode(i + 1, j + 1).getState();
                nextLattice.setLatticeNodeDirection(i + 1, j + 1, Direction.F, nodeState.getS(), nodeState.getR());
            }
        }
    }

    private void printInitialParameters() throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(LATTICE_FILE));
        printWriter.printf("%d\n%d\n", N, D);
        printWriter.close();

        printWriter = new PrintWriter(new FileWriter(LATTICE_SUBGRIDS_FILE));
        printWriter.printf("%d\n%d\n%d\n%d\n%d\n%d\n", N, D, subGridHeight, subGridWidth, totalSubGrids, maxParticlesPerSubGrid);
        printWriter.close();
    }

    private void printLattice(boolean append) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(LATTICE_FILE, append));

        Function<Boolean, Integer> booleanToInt = b -> b ? 1 : 0;

        for (int i = 0; i < lattice.getHeight(); i++) {
            for (int j = 0; j < lattice.getWidth(); j++) {
                if (lattice.checkIsEmpty(i, j)) continue;
                State state = lattice.getLatticeNode(i, j).getState();
                printWriter.printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n", i, j, booleanToInt.apply(state.getA()),
                        booleanToInt.apply(state.getB()), booleanToInt.apply(state.getC()), booleanToInt.apply(state.getD()),
                        booleanToInt.apply(state.getE()), booleanToInt.apply(state.getF()), booleanToInt.apply(state.getS()),
                        booleanToInt.apply(state.getR()));
            }
        }
        printWriter.println();
        printWriter.close();
    }

    private void printLatticeSubGrids(boolean append) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(LATTICE_SUBGRIDS_FILE, append));

        for (int i = 0; i < lattice.getHeight(); i++) {
            int subGridRow = i / subGridHeight;
            for (int j = 0; j < lattice.getWidth(); j++) {
                if (lattice.checkIsEmpty(i, j)) continue;
                int subGridColumn = j / subGridWidth;
                State state = lattice.getLatticeNode(i, j).getState();
                subGridStatistics[subGridRow][subGridColumn].processState(state);
            }
        }

        for (int i = 0; i < (lattice.getHeight() / subGridHeight); i++) {
            for (int j = 0; j < (lattice.getWidth() / subGridWidth); j++) {
                SubGridStatistics subGridStats = subGridStatistics[i][j];
                if(!subGridStats.hasParticles()) continue;
                printWriter.printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%s\t%d\n", i, j, subGridStats.getTotalParticles(),
                        subGridStats.getParticlesA(), subGridStats.getParticlesB(), subGridStats.getParticlesC(),
                        subGridStats.getParticlesD(), subGridStats.getParticlesE(), subGridStats.getParticlesF(),
                        subGridStats.getAverageDirection().getDirection(), subGridStats.getAverageDirection().getParticles());
            }
        }

        printWriter.println();
        printWriter.close();
    }
}
