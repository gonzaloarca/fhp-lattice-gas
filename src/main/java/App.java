import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        int N = 2000;
        int D = 50;
        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 200;
        int subGridHeight, subGridWidth;
        subGridHeight = subGridWidth = 20;
        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight, subGridHeight, subGridWidth);
    }
}
