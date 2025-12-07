package examples.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SSVGeneratorGui extends JFrame {

    private final SSVGenerator agent;

    private JTextField mField, WField, CField, LField, RField, rhoField;
    private JTextField fileField;

    public SSVGeneratorGui(SSVGenerator agent) {
        this.agent = agent;

        setTitle("SSV Generator GUI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        fileField = new JTextField("Select MPs CSV...");
        JButton fileButton = new JButton("Choose CSV");
        fileButton.addActionListener(e -> chooseFile());

        mField = new JTextField();
        WField = new JTextField();
        CField = new JTextField();
        LField = new JTextField();
        RField = new JTextField();
        rhoField = new JTextField();

        JButton sendButton = new JButton("Send Data");
        sendButton.addActionListener(this::sendData);

        add(fileField);
        add(fileButton);
        add(new JLabel("m:"));
        add(mField);
        add(new JLabel("W (comma-separated):"));
        add(WField);
        add(new JLabel("C (comma-separated):"));
        add(CField);
        add(new JLabel("L:"));
        add(LField);
        add(new JLabel("R:"));
        add(RField);
        add(new JLabel("rho:"));
        add(rhoField);
        add(sendButton);

        setVisible(true);
    }

    private void chooseFile() {
        // JFileChooser chooser = new JFileChooser();
        // if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        //     fileField.setText(chooser.getSelectedFile().getAbsolutePath());
        // }
        fileField.setText("MPs1_filepath");
    }

    private void sendData(ActionEvent e) {
        try {
            // int m = Integer.parseInt(mField.getText().trim());
            // int[] W = parseIntArray(WField.getText());
            // double[] C = parseDoubleArray(CField.getText());
            // int[] L = parseIntArray(LField.getText());
            // double[] R = parseDoubleArray(RField.getText());
            // double[] rho = parseDoubleArray(rhoField.getText());

            int m = 41;
            int[] W = {43, 32, 41, 19, 15, 12, 29, 27, 20, 11, 45, 25, 25, 37, 46, 17, 26,
 16, 28, 27, 33, 30, 47, 23, 31, 10, 20, 28, 31, 36, 12, 44, 26, 41,
 38, 16, 21, 28, 35, 37, 42};
            
            double[] C= {6.25, 14.65, 9.55, 13.95, 12. , 6.5 , 6.89, 9.36, 13.72,
 7.9 , 6.66, 8.14, 13.84, 10.53, 7.28, 8.53, 8.92, 10.22,
 5.39, 7.09, 12.2 , 11.32, 10.15, 7.7 , 11.25, 9.2 , 13.91,
 8.2 , 13.45, 8.54, 13.34, 5.17, 7.7 , 7.44, 8.74, 14.01,
 5.53, 10.11, 6.39, 12.59, 6.03} 
;
            int[] L = {8, 7, 9, 8, 7, 8, 8, 6, 9, 9, 9, 7, 9, 6, 7, 7, 8,
 7, 9, 7, 9, 5, 6, 6, 8, 7, 7, 8, 6, 5, 7, 7, 10, 5,
 6, 5, 6, 7, 5, 6, 8} 
;
            double[]  R = {0.72363754, 0.65135398, 0.79674574, 0.69468584, 0.74442395,
 0.72748567, 0.61427651, 0.65477932, 0.6619443 , 0.73156973,
 0.63021967, 0.66195206, 0.69852031, 0.69098019, 0.65741823,
 0.68618529, 0.75644166, 0.65312796, 0.75663052, 0.65274948,
 0.72860079, 0.73703561, 0.7839319 , 0.75942205, 0.72008043,
 0.71230719, 0.7315298 , 0.70147423, 0.57798889, 0.71766172,
 0.71055233, 0.69250801, 0.6521953 , 0.66585477, 0.61215604,
 0.67304647, 0.73438835, 0.65560526, 0.66807371, 0.65957549,
 0.7917005} 
;
            double[] rho = {0.63, 0.55, 0.56, 0.63, 0.44, 0.17, 0.51, 0.83, 0.46, 0.59, 0.93,
 0.79, 0.64, 0.57, 0.97, 0.68, 0.7 , 0.26, 0.23, 0.29, 0.25, 0.19,
 0.19, 0.66, 0.09, 0.8 , 0.84, 0.83, 0.14, 0.46, 0.06, 0.91, 0.92,
 0.56, 0.42, 0.37, 0.93, 0.87, 0.11, 0.29, 0.82};

            agent.buildMFNAndPrint(m, W, C, L, R, rho, fileField.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private int[] parseIntArray(String s) {
        String[] parts = s.split(",");
        int[] arr = new int[parts.length];
        for (int i=0;i<parts.length;i++) arr[i] = Integer.parseInt(parts[i].trim());
        return arr;
    }

    private double[] parseDoubleArray(String s) {
        String[] parts = s.split(",");
        double[] arr = new double[parts.length];
        for (int i=0;i<parts.length;i++) arr[i] = Double.parseDouble(parts[i].trim());
        return arr;
    }
}
