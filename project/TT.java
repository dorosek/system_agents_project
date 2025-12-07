package examples.project;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.FileWriter;
import java.io.IOException;

public class TT extends Agent {

    private double d;
    private double T;
    private int N;

    private MFN mfn;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        d = Double.parseDouble(args[0].toString());
        T = Double.parseDouble(args[1].toString());

        System.out.println("Hallo! Transmission times computing-agent");
        System.out.println(getAID().getName() + " is ready.");
        System.out.println("The aim is to estimate the probability of sending " + d + " units of flow");
        System.out.println("within time " + T);
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

                ACLMessage msg = receive();

                if (msg != null && "SSV".equals(msg.getConversationId())) {

                    String payload = msg.getContent();

                    int[] W   = parseIntArray(findLine(payload, "W="));
                    double[] C = parseDoubleArray(findLine(payload, "C="));
                    int[] L   = parseIntArray(findLine(payload, "L="));
                    double[] R = parseDoubleArray(findLine(payload, "R="));
                    double[] rho = parseDoubleArray(findLine(payload, "rho="));
                    N = Integer.parseInt(findLine(payload, "N="));
                    String MPsPath = findLine(payload, "MPsPath=");

                    mfn = new MFN(W.length, W, C, L, R, rho);

                    try {
                        mfn.getMPs(MPsPath);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    double[][] SSV = extractSSV(payload);
                    writeSSVToFile(SSV);

                    int result = 0;

                    for (int i = 0; i < N; i++) {
                        double time = mfn.getLowestTransmissionTime(d, SSV[i]);
                        if (time <= T) {
                            result++;
                        }
                    }

                    double reliability = result / (double) N;

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("RELIABILITY=" + reliability);
                    send(reply);

                    doDelete();
                }
                else block();
            }
        });
    }

    private String findLine(String payload, String prefix) {
        for (String line : payload.split("\n")) {
            if (line.startsWith(prefix)) {
                return line.substring(prefix.length());
            }
        }
        throw new RuntimeException("Missing field: " + prefix);
    }

    private int[] parseIntArray(String s) {
        String[] parts = s.split(",");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) arr[i] = Integer.parseInt(parts[i].trim());
        return arr;
    }

    private double[] parseDoubleArray(String s) {
        String[] parts = s.split(",");
        double[] arr = new double[parts.length];
        for (int i = 0; i < parts.length; i++) arr[i] = Double.parseDouble(parts[i].trim());
        return arr;
    }

    private double[][] extractSSV(String payload) {
        String[] lines = payload.split("\n");

        int start = -1;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().equals("SSV:")) {
                start = i + 1;
                break;
            }
        }

        int numRows = lines.length - start;
        double[][] SSV = new double[numRows][];

        for (int r = 0; r < numRows; r++) {
            String[] parts = lines[start + r].split(",");
            SSV[r] = new double[parts.length];
            for (int c = 0; c < parts.length; c++) {
                SSV[r][c] = Double.parseDouble(parts[c].trim());
            }
        }

        return SSV;
    }

    private void writeSSVToFile(double[][] ssv) {
        try (FileWriter fw = new FileWriter("SSV.csv")) {
            for (double[] row : ssv) {
                for (int i = 0; i < row.length; i++) {
                    fw.write(row[i] + (i < row.length - 1 ? "," : ""));
                }
                fw.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
