package examples.project;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

import javax.swing.*;
import java.util.Arrays;

public class SSVGenerator extends Agent {

    private double epsilon;
    private double delta;
    private int N;
    public double[][] SSV;

    @Override
    protected void setup() {
        Object[] args = getArguments();

        epsilon = Double.parseDouble(args[0].toString());
        delta = Double.parseDouble(args[1].toString());

        if (epsilon <= 0 || epsilon >= 1 || delta <= 0 || delta >= 1) {
            System.out.println("wrong epsilon or delta");
            doDelete();
            return;
        }

        SwingUtilities.invokeLater(() -> new SSVGeneratorGui(this));
    }

    public void buildMFNAndPrint(int m, int[] W, double[] C, int[] L, double[] R, double[] rho, String csvPath) {
        MFN mfn;
        mfn = new MFN(m, W, C, L, R, rho);


        try {
            mfn.getMPs(csvPath);
        } catch (Exception e) {
            System.out.println("Error reading MPs file: " + e.getMessage());
            return;
        }

        System.out.println("Parameters:");
        System.out.println("W = " + Arrays.toString(mfn.getW()));
        System.out.println("C = " + Arrays.toString(mfn.getC()));
        System.out.println("L = " + Arrays.toString(mfn.getL()));
        System.out.println("R = " + Arrays.toString(mfn.getR()));
        System.out.println("rho = " + Arrays.toString(mfn.getRho()));
        System.out.println("beta = " + Arrays.toString(mfn.getBeta()));

        N = mfn.worstCaseSampleSize(epsilon, delta);
        System.out.println("N = " + N);

        SSV = mfn.randomSSV(N, mfn.CDF(mfn.calculcatePRCapacityStates()));
        sendSSVMessage(W, C, L, R, rho, csvPath, SSV, N);
    }

    private void sendSSVMessage(int[] W, double[] C, int[] L, double[] R, double[] rho, String csvPath, double[][] SSV, int N) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("TT", AID.ISLOCALNAME));
        msg.setConversationId("SSV");
        msg.setContent(buildPayload(W, C, L, R, rho, csvPath, SSV, N));
        send(msg);
    }


    private String buildPayload(int[] W, double[] C, int[] L, double[] R, double[] rho, String csvPath, double[][] SSV, int N)
     {
        StringBuilder sb = new StringBuilder();
        sb.append("W=").append(arrayToCSV(W)).append("\n");
        sb.append("C=").append(arrayToCSV(C)).append("\n");
        sb.append("L=").append(arrayToCSV(L)).append("\n");
        sb.append("R=").append(arrayToCSV(R)).append("\n");
        sb.append("rho=").append(arrayToCSV(rho)).append("\n");
        sb.append("N=").append(String.valueOf(N)).append("\n");
        sb.append("MPsPath=").append(csvPath).append("\n");
        sb.append("SSV:\n");
        
        for (double[] row : SSV) {
            sb.append(arrayToCSV(row)).append("\n");
        }

        return sb.toString();
    }

    private String arrayToCSV(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(",");
        }
        return sb.toString();
    }

    private String arrayToCSV(double[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(",");
        }
        return sb.toString();
    }
}
