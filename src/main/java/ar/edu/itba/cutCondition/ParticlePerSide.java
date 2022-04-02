package ar.edu.itba.cutCondition;

import ar.edu.itba.models.Lattice;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ParticlePerSide implements CutCondition {

    private final int latticeWidth;
    private final int latticeHeight;
    private final double threshold;
    private static final String PARTICLE_PER_SIDE_FILE = "ParticlePerSide.txt";

    public ParticlePerSide(int latticeWidth, int latticeHeight, double threshold) throws IOException {
        this.latticeWidth = latticeWidth;
        this.latticeHeight = latticeHeight;
        if (threshold > 1 && threshold < 100) {
            this.threshold = threshold / 100;
        } else if (threshold > 0 && threshold <= 1) {
            this.threshold = threshold;
        } else {
            throw new IllegalArgumentException("Invalid error value. Must be in percentage or probability.");
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(PARTICLE_PER_SIDE_FILE));
        printWriter.close();
    }

    @Override
    public boolean evaluate(Lattice lattice, int N, int D, int iteration) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(PARTICLE_PER_SIDE_FILE, true));

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

        System.out.println("Left particles: " + leftLatticeParticles + " Right particles: " + rightLatticeParticles);

        printWriter.printf("%d\t%d\n", iteration, rightLatticeParticles);
        printWriter.close();
        return (Math.abs(leftLatticeParticles - rightLatticeParticles) / (double) N) < threshold;
    }
}
