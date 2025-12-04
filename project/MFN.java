package examples.project;

import javax.lang.model.type.NullType;
import java.util.*;
import java.io.*;
import java.math.*;

public class MFN {
    private static int m;
    private static int[] W;
    private static double[] C;
    private static int[] L;
    private static double[] R;
    private static double[] rho;
    private static double[] beta;
    private static ArrayList<int[]> Mps;
    private static TreeMap<Integer, Long> factorialCache;

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
        factorialCache.put(0, 1L);
        factorialCache.put(1, 1L);
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
    public void getMPs(String fileName) throws FileNotFoundException {
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

        } catch (IOException e) {
            throw new RuntimeException(e);
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
        // If factorial was already calculated, retrieve it
        if (factorialCache.containsKey(n)) {
            return factorialCache.get(n);
        }
        // If n is not it factorialCache -> we have not calculated it yet
        int nearestLowerFactorialKey = factorialCache.lastKey();
        // Get value of the factorial (highest in cache)
        long result = factorialCache.get(nearestLowerFactorialKey);
        // Calculate next factorials and save them in cache
        for (int i = nearestLowerFactorialKey + 1; i <= n; i++) {
            result *= i;
            factorialCache.put(i, result);
        }
        factorialCache.put(n, result);
        return result;
    }

    public void test(){
        for (int i = 0; i <= 50; i++){
            System.out.println("Factorial: " + i + " = " + factorial(i));
        }
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

    public static double normalCDF(double z)
    {
        double sum = 0;
        // TODO n = 100 a nie 15
        for(int i = 1; i <= 17; i++)
        {
            sum += (Math.pow(z, 2*i-1)/doubleFactorial(2*i-1));
        }
        return 0.5 + (1.0/Math.sqrt(2*Math.PI)) * Math.pow(Math.E, -(Math.pow(z, 2)/2)) * sum;
    }

    private static double derivativeCDF(double z){
        return Math.pow(Math.E, -Math.pow(z, 2) / 2) / Math.sqrt(2 * Math.PI);
    }

    public double normalICDF(double u){
        double x = 0.0;
        double tol = 0.1;
        int max_iter = 100;
        for (int i = 0; i <= max_iter; i++){
            double fx = normalCDF(x) - u;
            double fpx = derivativeCDF(x);

            if (fpx == 0){
                System.out.println("Derivative is zero. No convergence possible.");
                return x;
            }

            double x_new = x - fx / fpx;

            if (Math.abs(x_new - x) < tol) {
                System.out.println("Iters : " + i);
                return x_new;
            }

            x = x_new;
        }
        System.out.println("Method did not converge");
        return x;
    }

    // Formula 1
    private static double prCapacityState(int w, double r, double beta, int k){
        if (k == 0){
            return 1 - (1 / beta) * (1 - Math.pow(1 - r * beta, w));
        }
        else{
            return (1 / beta) * binomialCoefficient(w, k) * Math.pow(r * beta, k) * Math.pow(1 - r * beta, w - k);
        }
    }

    public double[][] calculcatePRCapacityStates(){
        double[][] probs = new double[W.length][];
        for (int i = 0; i < W.length; i++){
            probs[i] = new double[W[i] + 1];
            for (int j = 0; j <= W[i]; j++){
                probs[i][j] = prCapacityState(W[i], R[i], beta[i], j);
            }
        }
        return probs;
    }

    public double[][] CDF(double[][] arPMF){
        double[][] cumProbs =  new double[W.length][];
        for (int i = 0; i < arPMF.length; i++){
            cumProbs[i] = arPMF[i];
            for (int j = 1; j < arPMF[i].length; j++){
                cumProbs[i][j] = arPMF[i][j] + cumProbs[i][j-1];
            }
        }
        return cumProbs;
    }

    // Formula 3
    // tau(P, d, X)
    public double[] tau(double d, double[] X){
        double[] maxFlow = getMaxFlow(getMps(), X);
        double[] pathTime = timeOfPath(getMps(), getL());
        double[] result = new double[getMps().size()];
        for (int i = 0; i < getMps().size(); i++){
            // System.out.println("L(P) = " + pathTime[i] + " C(P,X) = " + maxFlow[i]);
            if (maxFlow[i] > 0){
                result[i] = pathTime[i] + (d / maxFlow[i]);
            }
            else{
                result[i] = Double.POSITIVE_INFINITY;
            }
        }
        return result;
    }

    // Formula 4
    // L(P)
    private double[] timeOfPath(ArrayList<int[]> mps, int[] l){
        double[] result = new double[mps.size()];
        double l_summed = 0.0;
        for (int i = 0; i < mps.size(); i++){
            for (int j = 0; j < mps.get(i).length; j++){
                l_summed += l[mps.get(i)[j] - 1];
            }
            result[i] = l_summed;
            l_summed = 0.0;
        }
        return result;
    }

    // Formula 5
    // C(P, X)
    private static double[] getMaxFlow(ArrayList<int[]> mps, double[] X){
        double[] result = new double[mps.size()];
        double minCapacity = Double.MAX_VALUE;
        for (int i = 0; i < mps.size(); i++){
            for (int j = 0; j < mps.get(i).length; j++){
                if (X[mps.get(i)[j] - 1] < minCapacity){
                    minCapacity = X[mps.get(i)[j] - 1];
                }
            }
            result[i] = minCapacity;
            minCapacity = Double.MAX_VALUE;
        }
        return result;
    }

    // Formula 8
    // T(d, X)
    public double getLowestTransmissionTime(double d, double[] X){
        double[] times = tau(d, X);
        return Arrays.stream(times).min().getAsDouble();
    }


    // Formula 12b
    public int worstCaseSampleSize(double epsilon, double delta){
        return (int) Math.pow(normalICDF(1 - (delta / 2)) / (2 * epsilon), 2);
    }

    public double[][] randomSSV(int N, double[][] arCDF){
        Random r1 = new Random(System.currentTimeMillis());
        double[][] result = new double[N][arCDF.length];
        for (int i = 0; i < N; i++){
            for  (int j = 0; j < arCDF.length; j++){
                // TODO spytac sie czy mozna uzyc random
                double random = r1.nextDouble();
                for (int k = 0; k < arCDF[j].length; k++){
                    if (arCDF[j][k] <= random){
                        // TODO moze zmienic
                        result[i][j] = (k + 1) * C[j];
                    }
                }
            }
        }
        return result;
    }
}
