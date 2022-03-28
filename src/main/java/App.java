import ar.edu.itba.cutCondition.ParticlePerSide;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        int N = 20;
        int D = 5;
        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 20;
        int subGridHeight, subGridWidth;
        subGridHeight = subGridWidth = 20;
        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight, subGridWidth, subGridHeight);
        fhpLatticeGas.run(new ParticlePerSide(latticeWidth, latticeHeight, 0.1));
//        fhpLatticeGas.run(new SlitParticleFlow());
    }
}
