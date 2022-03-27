package ar.edu.itba.models;

public class Lattice {
    private Node[][] lattice;
    private final int height;
    private final int width;

    public Lattice(int height, int width) {
        this.height = height;
        this.width = width;
        this.lattice = new Node[height][width];
    }

    public void setLattice(Node[][] lattice) {
        this.lattice = lattice;
    }

    public Node[][] getLattice() {
        return lattice;
    }

    public Node getLatticeNode(int i, int j) {
        return lattice[i][j];
    }

    public void setLatticeNode(int i, int j, State state) {
        lattice[i][j] = new Node(state);
    }

    public void setLatticeNodeDirection(int i, int j, Direction direction, boolean s, boolean r) {
        if (lattice[i][j] == null) {
            lattice[i][j] = new Node(new State(s, r));
        }
        State state = lattice[i][j].getState();
        switch (direction) {
            case A -> state.setA(true);
            case B -> state.setB(true);
            case C -> state.setC(true);
            case D -> state.setD(true);
            case E -> state.setE(true);
            case F -> state.setF(true);
        }
    }

    public void setLatticeNodeDirection(int i, int j, Direction direction) {
        State state = this.lattice[i][j].getState();
        switch (direction) {
            case A:
                state.setA(true);
            case B:
                state.setB(true);
            case C:
                state.setC(true);
            case D:
                state.setD(true);
            case E:
                state.setE(true);
            case F:
                state.setF(true);
        }
    }

    public boolean checkLatticeNodeDirection(int i, int j, Direction direction) {
        State state = this.lattice[i][j].getState();
        return switch (direction) {
            case A -> state.getA();
            case B -> state.getB();
            case C -> state.getC();
            case D -> state.getD();
            case E -> state.getE();
            case F -> state.getF();
        };

    }

    public boolean checkIsWall(int i, int j) {
        return lattice[i][j].getState().getS();
    }

    public boolean checkIsFull(int i, int j) {
        return  lattice[i][j] != null && lattice[i][j].getState().isFull();
    }

    public boolean checkIsEmpty(int i, int j) {
        return lattice[i][j] == null || lattice[i][j].getState().isEmpty();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
