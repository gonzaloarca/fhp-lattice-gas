package ar.edu.itba.models;

public class Node {
    private State state;

    public Node(State state) {
        this.state = state;
    }

    public int getParticleCount(){
        int count = 0;
        if(this.state.getA()) count++;
        if (this.state.getB()) count++;
        if (this.state.getC()) count++;
        if (this.state.getD()) count++;
        if (this.state.getE()) count++;
        if (this.state.getF()) count++;

        return count;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
