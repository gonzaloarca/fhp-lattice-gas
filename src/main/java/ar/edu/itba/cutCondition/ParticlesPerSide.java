package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

import java.io.IOException;

public class ParticlesPerSide implements CutCondition {

    private final int latticeWidth;
    private final int latticeHeight;
    private final double threshold;

    public ParticlesPerSide(int latticeWidth, int latticeHeight, double threshold) {
        this.latticeWidth = latticeWidth;
        this.latticeHeight = latticeHeight;
        if (threshold > 1 && threshold < 100) {
            this.threshold = threshold / 100;
        } else if (Double.compare(threshold, 0) > 0 && Double.compare(threshold, 1) <= 0) {
            this.threshold = threshold;
        } else {
            throw new IllegalArgumentException("Invalid threshold value. Must be in percentage or probability.");
        }
    }

    @Override
    public boolean evaluate(Lattice lattice, int N, int D, int iteration) {

        int leftLatticeParticles = 0;

        for (int y = 0; y < latticeHeight; y++) {
            for (int x = 0; x < (latticeWidth / 2); x++) {
                leftLatticeParticles += lattice.getLatticeNode(y, x).getParticleCount();
            }
        }

        int rightLatticeParticles = 0;

        for (int y = 0; y < latticeHeight; y++) {
            for (int x = (latticeWidth / 2) + 1; x < latticeWidth; x++) {
                rightLatticeParticles += lattice.getLatticeNode(y, x).getParticleCount();
            }
        }

//        System.out.println("Left particles: " + leftLatticeParticles + " Right particles: " + rightLatticeParticles);
        return Double.compare((Math.abs(leftLatticeParticles - rightLatticeParticles) / (double) N), threshold) < 0;
    }
}
