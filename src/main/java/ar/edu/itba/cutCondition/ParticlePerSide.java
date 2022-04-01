package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

public class ParticlePerSide implements CutCondition {

    private final int latticeWidth;
    private final int latticeHeight;
    private final double threshold;

    public ParticlePerSide(int latticeWidth, int latticeHeight, double threshold) {
        this.latticeWidth = latticeWidth;
        this.latticeHeight = latticeHeight;
        if (threshold > 1 && threshold < 100) {
            this.threshold = threshold / 100;
        } else if (threshold > 0 && threshold <= 1) {
            this.threshold = threshold;
        } else {
            throw new IllegalArgumentException("Invalid error value. Must be in percentage or probability.");
        }
    }

    @Override
    public boolean evaluate(Lattice lattice, int N, int D) {

        int leftLatticeParticles = 0;

        for (int i = 0; i < latticeHeight; i++) {
            for (int j = 0; j < latticeWidth / 2; j++) {
                leftLatticeParticles += lattice.getLatticeNode(i, j).getParticleCount();
            }
        }

        int rightLatticeParticles = 0;

        for (int i = 0; i < latticeHeight; i++) {
            for (int j = latticeWidth / 2; j < latticeWidth; j++) {
                rightLatticeParticles += lattice.getLatticeNode(i, j).getParticleCount();
            }
        }

        System.out.println("Left particles: " + leftLatticeParticles + " Right particles: " + rightLatticeParticles);

        return (Math.abs(leftLatticeParticles - rightLatticeParticles) / (double) N) < threshold;
    }
}
