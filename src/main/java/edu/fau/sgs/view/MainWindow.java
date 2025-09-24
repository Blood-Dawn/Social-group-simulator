package edu.fau.sgs.view;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Social Group Simulator");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JLabel("Welcome — Feed placeholder"));
    }
}
