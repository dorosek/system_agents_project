import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int m = 5;
        int[] W = {4, 3, 2, 3, 2};
        double[] C = {10, 15, 25, 15, 20};
        int[] L = {5, 7, 6, 5, 8};
        double[] R = {0.7, 0.65, 0.67, 0.71, 0.75};
        double[] rho = {0.1, 0.3, 0.5, 0.7, 0.9};

        // --------------------------------------------
        // 2. Tworzenie obiektu
        // --------------------------------------------
        MFN mfn = new MFN(m, W, C, L, R, rho);

        // --------------------------------------------
        // 3. Wczytanie pliku ze ścieżkami Minimal Paths
        // --------------------------------------------
        String fileName = "/Users/ignacyhirsz/Desktop/Studia/AgentSystems/project/system_agents_project/example_csv/MPs0.csv";   // <-- tu wpisz swoją nazwę pliku

        try {
            mfn.getMPs(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku: " + fileName);
            return;
        }

        // --------------------------------------------
        // 4. Wyświetlenie wczytanych minimal paths
        // --------------------------------------------
        ArrayList<int[]> mps = mfn.getMps();

        System.out.println("Wczytane minimalne ścieżki:");
        for (int[] row : mps) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }

        // --------------------------------------------
        // 5. Przykład użycia metod (opcjonalnie)
        // --------------------------------------------
        System.out.println("\nBeta values:");
        for (double b : mfn.getBeta()) {
            System.out.println(b);
        }

        System.out.println("Probabilities values:");
        double[][] prs = mfn.calculcatePRCapacityStates();
        for  (double[] pr : prs) {
            for(double val : pr) {
                System.out.print(val + " ");
            }
            System.out.println();
        }

        System.out.println("TAU per Path: ");
        double[] tau = mfn.tau(42.0);
        for (double val : tau) {
            System.out.println(val);
        }

        System.out.println(mfn.getLowestTransmissionTime(42.0));

        System.out.println("CDF");
        double[][] cumProb = mfn.CDF(prs);
        for (double[] pr : cumProb){
            for(double val : pr) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}
