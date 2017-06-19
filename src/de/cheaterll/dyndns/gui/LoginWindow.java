package de.cheaterll.dyndns.gui;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by CheaterLL on 01.06.2017.
 */
public class LoginWindow extends JFrame {
    private JPasswordField jPasswordField;

    public LoginWindow() throws HeadlessException {
        initComponents();
        initEvents();
        setSize(500,100);
        setLocationRelativeTo(null);
        setTitle("Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        jPasswordField = new JPasswordField(30);
        JLabel jLabel = new JLabel("Enter Password:\t");
        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.add(jLabel);
        jPanel.add(jPasswordField);
        getContentPane().add(jPanel);
    }

    private void initEvents() {
        jPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    new MainWindow(jPasswordField.getPassword());
                    dispose();
                }
            }
        });
    }
}
