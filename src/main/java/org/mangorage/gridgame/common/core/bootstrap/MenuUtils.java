package org.mangorage.gridgame.common.core.bootstrap;

import javax.swing.*;

public class MenuUtils {

    public static MenuOption showInputDialog(String title) {
        // Create a text field
        JTextField USERNAME = new JTextField(10);
        USERNAME.setText("Username");
        JTextField IP = new JTextField(10);
        IP.setText("localhost:25565");

        // Create a panel to hold the text field
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Username:"));
        panel.add(USERNAME);
        panel.add(new JLabel("Enter IP:"));
        panel.add(IP);

        // Show the popup dialog
        int result = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION);

        // Check if OK button is clicked
        if (result == JOptionPane.OK_OPTION) {
            return new MenuOption(IP.getText(), USERNAME.getText());
        } else {
            return null;
        }
    }
}
