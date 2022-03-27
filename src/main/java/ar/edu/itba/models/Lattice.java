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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
