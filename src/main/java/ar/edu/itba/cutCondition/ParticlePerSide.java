package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

public class ParticlePerSide implements CutCondition {

    private final int latticeWidth;
    private final int latticeHeight;
    private final double error;

    public ParticlePerSide(int latticeWidth, int latticeHeight, double error) {
        this.latticeWidth = latticeWidth;
        this.latticeHeight = latticeHeight;
        this.error = error;
    }

    @Override
    public boolean evaluate(Lattice lattice, int N, int D) {

        int leftLatticeParticles = 0;

        for(int i = 0; i < latticeWidth / 2; i++) {
            for(int j = 0; j < latticeHeight; j++) {
                leftLatticeParticles += lattice.getLatticeNode(i, j).getParticleCount();
            }
        }

        int rightLatticeParticles = 0;

        for(int i = latticeWidth / 2; i < latticeWidth; i++) {
            for(int j = 0; j < latticeHeight; j++) {
                rightLatticeParticles += lattice.getLatticeNode(i, j).getParticleCount();
            }
        }

        return true;
    }
}
