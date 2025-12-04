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
        fileField.setText("C:\\Users\\huber\\system_agents_project\\example_csv\\MPs0.csv");
    }

    private void sendData(ActionEvent e) {
        try {
            // int m = Integer.parseInt(mField.getText().trim());
            // int[] W = parseIntArray(WField.getText());
            // double[] C = parseDoubleArray(CField.getText());
            // int[] L = parseIntArray(LField.getText());
            // double[] R = parseDoubleArray(RField.getText());
            // double[] rho = parseDoubleArray(rhoField.getText());

            int m = 5;
            int[] W = {4,3,2,3,2};
            double[] C = {10,15,25,15,20};
            int[] L = {5,7,6,5,8};
            double[] R = {0.7,0.65,0.67,0.71,0.75};
            double[] rho = {0.1,0.3,0.5,0.7,0.9};

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
