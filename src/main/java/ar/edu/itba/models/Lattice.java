package ar.edu.itba.models;

import java.util.HashMap;
import java.util.Map;

public class Lattice {
    private final Node[][] lattice;
    private final int height;
    private final int width;

    public Lattice(int height, int width) {
        this.height = height;
        this.width = width;
        this.lattice = new Node[height][width];
    }

    public void setLatticeNode(int i, int j, State state) {
        this.lattice[i][j] = new Node(state);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
