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
        if (args.length != 2) {
            doDelete();
            return;
        }

        d = Double.parseDouble(args[0].toString());
        T = Double.parseDouble(args[1].toString());


        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

                ACLMessage msg = receive();

                if (msg != null && "SSV".equals(msg.getConversationId())) {
                    String payload = msg.getContent();
                    double[][] SSV = extractSSV(payload);
                    writeSSVToFile(SSV);

                    double result = 0;
                    double sum = 0;
                    for (int i = 0; i < N; i++){
                        result = mfn.getLowestTransmissionTime(42.0, SSV[i]);
                        if(result < T)
                        {
                            sum++;
                        }
                    }
                    System.out.println(sum/Double.valueOf(N));
                    ACLMessage reply = msg.createReply(); 
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("RELIABILITY=TODO"); // tutaj bedzie zwracac network reliability
                    send(reply);
                    doDelete();
                }
                else block();
            }
        });
    }

    private double[][] extractSSV(String payload) {
        String[] lines = payload.split("\n");

        int start = -1;
        
        for (int i = 0; i < lines.length; i++) {
            if(lines[i].trim().startsWith("N=")){
                N = Integer.valueOf(lines[i].trim().split("=")[1]);
            }
            if (lines[i].trim().equals("SSV:")) {
                start = i + 1;
                break;
            }
        }
        if (start == -1) throw new RuntimeException("No SSV section in payload!");

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
