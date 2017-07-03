package de.cheaterll.dyndns.gui;

import de.cheaterll.dyndns.dns.DynDNSHost;
import de.cheaterll.dyndns.encryption.Encryption;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by CheaterLL on 15.06.2017.
 */
public class EditDynDNSHostPanel extends JPanel {
    private JTextField jtfProvider;
    private JTextField jtfQueryUrl;
    private JTextField jtfUsername;
    private JPasswordField jpfPassword;
    JButton jbSave;
    MainWindow parent;

    public EditDynDNSHostPanel(MainWindow parent) {
        this.parent = parent;
        initComponents();
        initEvents();
    }

    private void initComponents(){
        jbSave = new JButton();
        JPanel jpCenter = new JPanel();
        JPanel jpInner = new JPanel();
        JPanel jpSouth = new JPanel();
        JLabel jlProvider = new JLabel("Provider Name");
        jtfProvider = new JTextField(25);
        JLabel jlQueryURL = new JLabel("Update Query URL");
        jtfQueryUrl = new JTextField(25);
        JLabel jlUsername = new JLabel("Login User Name");
        jtfUsername = new JTextField(25);
        JLabel jlPassword = new JLabel("Login Password");
        jpfPassword = new JPasswordField(25);

        GroupLayout layout = new GroupLayout(jpInner);
        jpInner.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(jlProvider).addComponent(jlQueryURL).addComponent(jlUsername).addComponent(jlPassword));
        hGroup.addGroup(layout.createParallelGroup().addComponent(jtfProvider).addComponent(jtfQueryUrl).addComponent(jtfUsername).addComponent(jpfPassword));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jlProvider).addComponent(jtfProvider));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jlQueryURL).addComponent(jtfQueryUrl));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jlUsername).addComponent(jtfUsername));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jlPassword).addComponent(jpfPassword));
        layout.setVerticalGroup(vGroup);

        jbSave = new JButton("Save");
        jpSouth.add(jbSave);

        setLayout(new BorderLayout());
        jpCenter.setLayout(new GridBagLayout());
        jpCenter.add(jpInner);
        add(jpCenter, BorderLayout.CENTER);
        add(jpSouth, BorderLayout.SOUTH);
    }

    private void initEvents(){
        jbSave.addActionListener(e -> parent.saveDynDNSHost(jtfProvider.getText(), jtfQueryUrl.getText(), jtfUsername.getText(), jpfPassword.getPassword()));
    }

    public void setHost(DynDNSHost host) {
        jtfProvider.setText(host.getProvider());
        jtfQueryUrl.setText(host.getQueryUrl());
        jtfUsername.setText(host.getUname());
    }
}
