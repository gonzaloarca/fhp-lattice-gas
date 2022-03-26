import ar.edu.itba.models.Lattice;
import ar.edu.itba.models.State;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FHP {
    private final Map<State, State> collisionLookupTable;
    private final Lattice lattice;
    private final int N;
    private final int D;

    public FHP(int n, int d, int latticeWidth, int latticeHeight) {
        this.N = n;
        this.D = d;
        this.lattice = new Lattice(latticeHeight, latticeWidth);
        this.collisionLookupTable = new HashMap<>();
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
            for (int j = 0; j < width; j++) {
                randomBit = random.nextBoolean();
                isWall = j == 0 || j == width - 1 || i == 0 || i == height - 1;

                // for middle wall with slit
                if (j == width / 2 && (i < (height - D) / 2 || i > (height + D) / 2)) {
                    isWall = true;
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
        boolean[] directions = new boolean[6];
        //List<Integer> possibleDirections = IntStream.range(0, 6).boxed().toList();
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
}
