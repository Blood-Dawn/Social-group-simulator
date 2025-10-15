package org.campusboard.sgs.view.dialogs;

import org.campusboard.sgs.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Modal dialog used to authenticate users with username and password.
 */
public class LoginDialog extends JDialog {
    private final Controller controller;
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel errorLabel = new JLabel(" ");

    public LoginDialog(Frame owner, Controller controller) {
        super(owner, "Sign In", true);
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField.setColumns(20);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField.setColumns(20);
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel feedbackPanel = new JPanel(new BorderLayout());
        errorLabel.setForeground(Color.RED.darker());
        errorLabel.setFont(errorLabel.getFont().deriveFont(Font.PLAIN, 12f));
        feedbackPanel.add(errorLabel, BorderLayout.CENTER);
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(feedbackPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton loginButton = new JButton("Login");
        buttonPanel.add(cancelButton);
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());
        loginButton.addActionListener(e -> attemptLogin());

        KeyAdapter submitOnEnter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        };
        usernameField.addKeyListener(submitOnEnter);
        passwordField.addKeyListener(submitOnEnter);

        getRootPane().setDefaultButton(loginButton);

        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
        SwingUtilities.invokeLater(usernameField::requestFocusInWindow);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        Controller.AuthenticationResult result = controller.authenticateUser(username, password);
        Arrays.fill(password, '\0');
        passwordField.setText("");

        if (result.isSuccess()) {
            errorLabel.setText(" ");
            dispose();
        } else {
            errorLabel.setText(result.getMessage());
        }
    }
}
