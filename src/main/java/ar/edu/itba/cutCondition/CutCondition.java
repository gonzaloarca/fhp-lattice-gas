package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

import java.io.IOException;

@FunctionalInterface
public interface CutCondition {
    boolean evaluate(Lattice lattice, int N, int D, int iteration, long time) throws IOException;
}
