package ar.edu.itba.models;

import java.util.Objects;

public class State {

    private static int MAX_STATES = 256;

    private boolean A;
    private boolean B;
    private boolean C;
    private boolean D;
    private boolean E;
    private boolean F;
    private boolean S;
    private boolean R;

    public State(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean s, boolean r) {
        A = a;
        B = b;
        C = c;
        D = d;
        E = e;
        F = f;
        S = s;
        R = r;
    }

    public State(boolean s, boolean r) {
        S = s;
        R = r;
        A = false;
        B = false;
        C = false;
        D = false;
        E = false;
        F = false;
    }

    public static int getMaxStates() {
        return MAX_STATES;
    }

    public boolean getA() {
        return this.A;
    }

    public void setA(boolean a) {
        A = a;
    }

    public boolean getB() {
        return this.B;
    }

    public void setB(boolean b) {
        B = b;
    }

    public boolean getC() {
        return this.C;
    }

    public void setC(boolean c) {
        C = c;
    }

    public boolean getD() {
        return this.D;
    }

    public void setD(boolean d) {
        D = d;
    }

    public boolean getE() {
        return this.E;
    }

    public void setE(boolean e) {
        E = e;
    }

    public boolean getF() {
        return this.F;
    }

    public void setF(boolean f) {
        F = f;
    }

    public boolean getS() {
        return this.S;
    }

    public void setS(boolean s) {
        S = s;
    }

    public boolean getR() {
        return this.R;
    }

    public void setR(boolean r) {
        R = r;
    }

    public boolean isFull() {
        return A && B && C && D && E && F;
    }

    public boolean isEmpty() {
        return !(A || B || C || D || E || F);
    }

    public String toString() {
        return "A: " + A + " B: " + B + " C: " + C + " D: " + D + " E: " + E + " F: " + F + " S: " + S + " R: " + R;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return A == state.A && B == state.B && C == state.C && D == state.D && E == state.E && F == state.F && S == state.S && R == state.R;
    }

    @Override
    public int hashCode() {
        return Objects.hash(A, B, C, D, E, F, S, R);
    }
}
