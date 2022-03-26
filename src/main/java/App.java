public class App {
    public static void main(String[] args) {
        int N = 2000;
        int D = 50;
        int latticeHeight, latticeWidth;
        latticeHeight = latticeWidth = 200;
        FHP fhpLatticeGas = new FHP(N, D, latticeWidth, latticeHeight);
    }
}
