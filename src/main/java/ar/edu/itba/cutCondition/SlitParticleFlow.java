package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

public class SlitParticleFlow implements CutCondition {

    private int previousFlow;

    public SlitParticleFlow() {
        this.previousFlow = 0;
    }

    @Override
    public boolean evaluate(Lattice lattice, int N, int D) {
        int flow = 0;
        int width = lattice.getWidth() / 2;
        int height = lattice.getHeight() / 2;

        for (int i = 0; i < D; i++) {
            flow += lattice.getLatticeNode(height - (D / 2) + i, width).getParticleCount();
        }

        if (previousFlow == flow) return true;

        this.previousFlow = flow;

        return false;
    }
}
