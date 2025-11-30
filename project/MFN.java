import java.util.*;
import java.util.stream.IntStream;

public class MFN {
    private int m;
    private int[] W;
    private double[] C;
    private int[] L;
    private double[] R;
    private double[] rho;
    private double[] beta;
    private ArrayList<int[]> Mps;
    private static Map<Integer, Long> factorialCache;

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
        factorialCache = new TreeMap<>();
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
        if (factorialCache.containsKey(n)){
            return factorialCache.get(n);
        }
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        factorialCache.put(n, result);
        return result;
    }

    public static long binomialCoefficient(int n, int k)
    {
        return factorial(n)/(factorial(k) * factorial(n-k));
    }

}
