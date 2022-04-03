import ar.edu.itba.cutCondition.CutCondition;
import ar.edu.itba.helpers.LatticePrinter;
import ar.edu.itba.models.Direction;
import ar.edu.itba.models.Lattice;
import ar.edu.itba.models.State;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class FHP {
    private final static int MAX_PARTICLES_PER_CELL = 6;
    private final Map<State, State> collisionLookupTable;
    private final int N;
    private final int D;
    private Lattice lattice;


    public FHP(int n, int d, int latticeWidth, int latticeHeight) {
        this.N = n;
        this.D = d;
        this.lattice = new Lattice(latticeHeight, latticeWidth);
        this.collisionLookupTable = new HashMap<>();
        initializeCollisionLookupTable();
        generateLatticeSpace();
        generateInitialState();
    }

    public void printStaticLattice(String outputName) throws IOException {
        LatticePrinter latticePrinter = new LatticePrinter(lattice.getHeight(), lattice.getWidth(), N, D);
        latticePrinter.printStaticLattice(outputName,this.lattice);
    }

    public void run(CutCondition cutCondition) throws IOException {
        LatticePrinter latticePrinter = new LatticePrinter(lattice.getHeight(), lattice.getWidth(), N, D);
        latticePrinter.printInitialParameters();
        int iteration = 0;
        while (!cutCondition.evaluate(lattice, N, D, iteration)) {
//        for (int i = 0; i < 1000; i++) {
            latticePrinter.printLattice(lattice, iteration, true);
            calculateAndPropagate();
            iteration++;
        }
        System.out.println("Iterations: " + iteration);
    }

    //generate walls and non-walls
    private void generateLatticeSpace() {
        int height = this.lattice.getHeight();
        int width = this.lattice.getWidth();
        Random random = new Random();
        boolean isXWall;
        boolean isYWall;
        boolean randomBit;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                randomBit = random.nextBoolean();
                isYWall = j == 0 || (j == width / 2 && (i < (height - D) / 2 || i > (height + D) / 2)) || j == width - 1;
                isXWall = i == 0 || i == height - 1;
                lattice.setLatticeNode(i, j, new State(isXWall, isYWall, randomBit));
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
            Direction newDirection = Direction.values()[directionsRandom.nextInt(MAX_PARTICLES_PER_CELL)];
            row = cell / width;
            col = cell % width;
            if (lattice.checkIsWall(row, col) || lattice.checkIsFull(row, col) || lattice.checkLatticeNodeDirection(row, col, newDirection))
                continue;

            lattice.setLatticeNodeDirection(row, col, newDirection);
            i++;
        }
    }

    private void initializeCollisionLookupTable() {
        State inputState;
        boolean[] stateValues;

        // iterate through all possible states and calculate the output state
        for (int i = 0; i < State.getMaxStates(); i++) {
            stateValues = new boolean[9];

            for (int j = 0; j < 9; j++) {
                stateValues[j] = (i & (1 << j)) != 0;
            }

            inputState = new State(stateValues[0], stateValues[1], stateValues[2], stateValues[3], stateValues[4], stateValues[5], stateValues[6], stateValues[7], stateValues[8]);

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
        boolean xs = prevState.getXS();
        boolean ys = prevState.getYS();
        boolean r = prevState.getR();

        boolean s = xs || ys;
        boolean corner = xs && ys;

        boolean double1 = (a && d && !(b || c || e || f));
        boolean double2 = (b && e && !(a || c || d || f));
        boolean double3 = (c && f && !(a || b || d || e));

        boolean triple = (a ^ b) && (b ^ c) && (c ^ d) && (d ^ e) && (e ^ f);

        boolean quad1 = (b && c && e && f && !(a || d));
        boolean quad2 = (a && c && d && f && !(b || e));
        boolean quad3 = (a && b && d && e && !(c || f));

        boolean collision1 = (quad1 || (r && quad2) || (!r && quad3) || triple || double1 || (r && double2) || (!r && double3));
        boolean collision2 = (quad2 || (r && quad3) || (!r && quad1) || triple || double2 || (r && double3) || (!r && double1));
        boolean collision3 = (quad3 || (r && quad1) || (!r && quad2) || triple || double3 || (r && double1) || (!r && double2));

        boolean newA = (!s && (a ^ collision1)) || (ys && d);
        boolean newD = (!s && (d ^ collision1)) || (ys && a);
        boolean newB = (!s && (b ^ collision2)) || ((xs ^ ys) && ((xs && f) || (ys && c))) || (corner && e);
        boolean newE = (!s && (e ^ collision2)) || ((xs ^ ys) && ((xs && c) || (ys && f))) || (corner && b);
        boolean newC = (!s && (c ^ collision3)) || ((xs ^ ys) && ((xs && e) || (ys && b))) || (corner && f);
        boolean newF = (!s && (f ^ collision3)) || ((xs ^ ys) && ((xs && b) || (ys && e))) || (corner && c);

        return new State(newA, newB, newC, newD, newE, newF, xs, ys, r);
    }

    private void calculateAndPropagate() {
        Lattice nextLattice = new Lattice(lattice.getWidth(), lattice.getHeight());
        for (int i = 0; i < lattice.getHeight(); i++) {
            for (int j = 0; j < lattice.getWidth(); j++) {
                State state = lattice.getLatticeNode(i, j).getState();

                if (nextLattice.checkIsEmpty(i, j)) {
                    nextLattice.setLatticeNode(i, j, new State(state.getXS(), state.getYS(), state.getR()));
                }

                if (lattice.checkIsEmpty(i, j)) {
                    continue;
                }

                State outputState = collisionLookupTable.get(lattice.getLatticeNode(i, j).getState());
                if (outputState == null) throw new RuntimeException("Something's not right...");
                updateNeighbors(nextLattice, outputState, i, j);
            }
        }
        lattice = nextLattice;
    }

    private void updateNeighbors(Lattice nextLattice, State nextState, int i, int j) {

        State nodeState;

        if (nextState.getA()) {
            nodeState = lattice.getLatticeNode(i, j + 1).getState();
            nextLattice.setLatticeNodeDirection(i, j + 1, Direction.A, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
        }

        if (nextState.getD()) {
            nodeState = lattice.getLatticeNode(i, j - 1).getState();
            nextLattice.setLatticeNodeDirection(i, j - 1, Direction.D, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
        }

        boolean oddRow = (i % 2) == 1;

        if (nextState.getB()) {
            if (oddRow) {
                nodeState = lattice.getLatticeNode(i - 1, j).getState();
                nextLattice.setLatticeNodeDirection(i - 1, j, Direction.B, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
            } else {
                nodeState = lattice.getLatticeNode(i - 1, j + 1).getState();
                nextLattice.setLatticeNodeDirection(i - 1, j + 1, Direction.B, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
            }
        }

        if (nextState.getC()) {
            if (oddRow) {
                nodeState = lattice.getLatticeNode(i - 1, j - 1).getState();
                nextLattice.setLatticeNodeDirection(i - 1, j - 1, Direction.C, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
            } else {
                nodeState = lattice.getLatticeNode(i - 1, j).getState();
                nextLattice.setLatticeNodeDirection(i - 1, j, Direction.C, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
            }
        }

        if (nextState.getE()) {
            if (oddRow) {
                nodeState = lattice.getLatticeNode(i + 1, j - 1).getState();
                nextLattice.setLatticeNodeDirection(i + 1, j - 1, Direction.E, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
            } else {
                nodeState = lattice.getLatticeNode(i + 1, j).getState();
                nextLattice.setLatticeNodeDirection(i + 1, j, Direction.E, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
            }
        }

        if (nextState.getF()) {
            if (oddRow) {
                nodeState = lattice.getLatticeNode(i + 1, j).getState();
                nextLattice.setLatticeNodeDirection(i + 1, j, Direction.F, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
            } else {
                nodeState = lattice.getLatticeNode(i + 1, j + 1).getState();
                nextLattice.setLatticeNodeDirection(i + 1, j + 1, Direction.F, nodeState.getXS(), nodeState.getYS(), nodeState.getR());
            }
        }
    }
}
