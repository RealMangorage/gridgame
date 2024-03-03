package org.mangorage.gridgame.common.core.bootstrap;

import javax.swing.*;

public class MenuUtils {
    public static String showInputDialog(String title) {
        // Create a text field
        JTextField textField = new JTextField(10);
        textField.setText("localhost:25565");

        // Create a panel to hold the text field
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter IP:"));
        panel.add(textField);

        // Show the popup dialog
        int result = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION);

        // Check if OK button is clicked
        if (result == JOptionPane.OK_OPTION) {
            return textField.getText();
        } else {
            return null;
        }
    }
}
