package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

public class ParticlePerSide implements CutCondition {

    private final int latticeWidth;
    private final int latticeHeight;
    private final double threshold;

    public ParticlePerSide(int latticeWidth, int latticeHeight, double error) {
        this.latticeWidth = latticeWidth;
        this.latticeHeight = latticeHeight;
        if (error > 1 && error < 100) {
            this.threshold = error / 100;
        } else if (error > 0 && error <= 1) {
            this.threshold = error;
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
