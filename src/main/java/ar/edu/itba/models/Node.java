package ar.edu.itba.models;

public class Node {
    private State state;

    public Node(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
