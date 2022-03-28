package ar.edu.itba.models;

import java.util.ArrayList;
import java.util.List;

public class SubGridStatistics {
    long totalParticles;
    int height;
    int width;
    int particlesA;
    int particlesB;
    int particlesC;
    int particlesD;
    int particlesE;
    int particlesF;

    public SubGridStatistics(int height, int width) {
        this.height = height;
        this.width = width;
        totalParticles = 0;
        particlesA = 0;
        particlesB = 0;
        particlesC = 0;
        particlesD = 0;
        particlesE = 0;
        particlesF = 0;
    }

    public void processState(State state) {
        if (state.getA()) {
            addParticleA();
        }
        if (state.getB()) {
            addParticleB();
        }
        if (state.getC()) {
            addParticleC();
        }
        if (state.getD()) {
            addParticleD();
        }
        if (state.getE()) {
            addParticleE();
        }
        if (state.getF()) {
            addParticleF();
        }
    }

    public boolean hasParticles() {
        return totalParticles > 0;
    }

    public DirectionStatistics getAverageDirection() {
        List<DirectionStatistics> directionStatisticsList = new ArrayList<>(6);
        directionStatisticsList.add(new DirectionStatistics(Direction.A, particlesA));
        directionStatisticsList.add(new DirectionStatistics(Direction.B, particlesB));
        directionStatisticsList.add(new DirectionStatistics(Direction.C, particlesC));
        directionStatisticsList.add(new DirectionStatistics(Direction.D, particlesD));
        directionStatisticsList.add(new DirectionStatistics(Direction.E, particlesE));
        directionStatisticsList.add(new DirectionStatistics(Direction.F, particlesF));
        return directionStatisticsList.stream().max(DirectionStatistics::compareTo).get();
    }

    public void reset() {
        totalParticles = 0;
        particlesA = 0;
        particlesB = 0;
        particlesC = 0;
        particlesD = 0;
        particlesE = 0;
        particlesF = 0;
    }

    public void addParticleA() {
        particlesA++;
        totalParticles++;
    }

    public void addParticleB() {
        particlesB++;
        totalParticles++;
    }

    public void addParticleC() {
        particlesC++;
        totalParticles++;
    }

    public void addParticleD() {
        particlesD++;
        totalParticles++;
    }

    public void addParticleE() {
        particlesE++;
        totalParticles++;
    }

    public void addParticleF() {
        particlesF++;
        totalParticles++;
    }

    public long getTotalParticles() {
        return totalParticles;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getParticlesA() {
        return particlesA;
    }

    public int getParticlesB() {
        return particlesB;
    }

    public int getParticlesC() {
        return particlesC;
    }

    public int getParticlesD() {
        return particlesD;
    }

    public int getParticlesE() {
        return particlesE;
    }

    public int getParticlesF() {
        return particlesF;
    }

    public class DirectionStatistics implements Comparable<DirectionStatistics> {
        private final Direction direction;
        private final int particles;

        public DirectionStatistics(Direction direction, int particles) {
            this.direction = direction;
            this.particles = particles;
        }

        public Direction getDirection() {
            return direction;
        }

        public int getParticles() {
            return particles;
        }

        @Override
        public int compareTo(DirectionStatistics directionStatistics) {
            return this.particles - directionStatistics.getParticles();
        }
    }
}
