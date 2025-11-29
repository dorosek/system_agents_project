import java.util.*;
import java.io.*;
import java.math.*;

public class MFN {
    private int m;
    private int[] W;
    private double[] C;
    private int[] L;
    private double[] R;
    private double[] rho;
    private double[] beta;
    private ArrayList<int[]> Mps;

    public MFN(int m, int[] W, double[] C, int[] L, double[] R, double[] rho)
    {
        int size = W.length;

        if (C.length != size || L.length != size || R.length != size || rho.length != size)
        {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < size; i++)
        {
            if (R[i] < 0 || R[i] > 1 || rho[i] < 0 || rho[i] > 1)
            {
                throw new IllegalArgumentException();
            }

        }
        this.m = m;
        this.W = W;
        this.C = C;
        this.L = L;
        this.R = R;
        this.rho = rho;
        this.beta = new double[size];
        for(int i = 0; i < size; i++)
        {
            this.beta[i] = 1 + (rho[i]*(1-R[i])/R[i]);
        }
    }
    public int getM() {
        return m;
    }

    public int[] getW() {
        return W;
    }

    public double[] getC() {
        return C;
    }

    public int[] getL() {
        return L;
    }

    public double[] getR() {
        return R;
    }

    public double[] getRho() {
        return rho;
    }

    public double[] getBeta() {
        return beta;
    }

    public ArrayList<int[]> getMps() {
        return Mps;
    }
    public void getMPs(String fileName) {
        Mps = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] tokens = line.split(",");
                int[] row = new int[tokens.length];

                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i].trim());
                }

                Mps.add(row);
            }

        }
    }
    public void setM(int m) {
        this.m = m;
    }

    public void setW(int[] W) {
        this.W = W;
    }

    public void setC(double[] C) {
        this.C = C;
    }

    public void setL(int[] L) {
        this.L = L;
    }

    public void setR(double[] R) {
        this.R = R;
    }

    public void setRho(double[] rho) {
        this.rho = rho;
    }

    public void setMps(ArrayList<int[]> Mps) {
        this.Mps = Mps;
    }

    public static long factorial(int n) {
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static long doubleFactorial(int n)
    {
        long result = 1;
        for (int i = n; i > 0; i -= 2) {
            result *= i;
        }
        return result;
    }

    public static long binomialCoefficient(int n, int k)
    {
        return factorial(n)/(factorial(k) * factorial(n-k));
    }

    static double normalCDF(double z)
    {
        double sum = 0;
        for(int i = 1; i <= 100; i++)
        {
            sum += (Math.pow(z, 2*i+1)/doubleFactorial(2*i+1));
        }
        return 0.5 + (1.0/Math.sqrt(2*Math.PI)) * Math.pow(Math.E, -(Math.pow(z, 2)/2)) * sum;
    }

}
