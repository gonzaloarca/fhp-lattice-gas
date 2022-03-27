package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

@FunctionalInterface
public interface CutCondition {
    boolean evaluate(Lattice lattice, int N, int D);
}
