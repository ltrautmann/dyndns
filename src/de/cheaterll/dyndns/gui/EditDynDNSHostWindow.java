package de.cheaterll.dyndns.gui;

import de.cheaterll.dyndns.dns.DynDNSHost;
import de.cheaterll.dyndns.dns.DynDNSList;
import de.cheaterll.dyndns.encryption.Encryption;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by CheaterLL on 01.06.2017.
 */
public class EditDynDNSHostWindow extends JFrame{
    private JTextField jtfProvider;
    private JTextField jtfURL;
    private JTextField jtfUname;
    private JPasswordField jpfPasswd;
    private JButton jbConfirm;
    private JButton jbCancel;
    private JPanel jpCenter;
    private JPanel jpSouth;

    public EditDynDNSHostWindow(DynDNSHost host) throws HeadlessException {
        init();
        jtfProvider.setText(host.getProvider());
        jtfURL.setText(host.getQueryUrl());
        jtfUname.setText(host.getUname());
        initEvents(host);
        setTitle("Edit Host");
        setVisible(true);
    }

    public EditDynDNSHostWindow(DynDNSList list) throws HeadlessException{
        init();
        initEvents(list);
        setTitle("Add Host");
        setVisible(true);
    }

    private void init() {
        initComponents();
        initEvents();
        setSize(850,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        jtfProvider = new JTextField(30);
        jtfURL = new JTextField(30);
        jtfUname = new JTextField(30);
        jpfPasswd = new JPasswordField(30);
        JLabel jlProvider = new JLabel("Provider:\t");
        JLabel jlURL = new JLabel("URL:\t");
        JLabel jlUname = new JLabel("Username:\t");
        JLabel jlPasswd = new JLabel("Password:\t");
        JPanel jpLabels = new JPanel(new GridLayout(4, 1));
        JPanel jpText = new JPanel(new GridLayout(4, 1));
        JPanel jPanel = new JPanel();
        jpCenter = new JPanel(new GridBagLayout());

        jpLabels.add(jlProvider);
        jpText.add(jtfProvider);
        jpLabels.add(jlURL);
        jpText.add(jtfURL);
        jpLabels.add(jlUname);
        jpText.add(jtfUname);
        jpLabels.add(jlPasswd);
        jpText.add(jpfPasswd);
        jPanel.add(jpLabels);
        jPanel.add(jpText);
        jpCenter.add(jPanel);

        jpSouth = new JPanel();
        jbConfirm = new JButton("Confirm");
        jbCancel = new JButton("Cancel");

        jpSouth.add(jbConfirm);
        jpSouth.add(jbCancel);

        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void initEvents() {
        jbCancel.addActionListener(e -> dispose());
    }

    private void initEvents(DynDNSList list) {
        jpfPasswd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addHost(list);
                    dispose();
                }
            }
        });
        jbConfirm.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHost(list);
                dispose();
            }
        });
    }

    private void initEvents(DynDNSHost host) {
        jpfPasswd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    editHost(host);
                    dispose();
                }
            }
        });
        jbConfirm.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editHost(host);
                dispose();
            }
        });
    }

    private void editHost(DynDNSHost host) {
        host.setProvider(jtfProvider.getText());
        host.setQueryUrl(jtfURL.getText());
        host.setUname(jtfUname.getText());
        try {
            host.setEncryptedPasswd(Encryption.encrypt(jpfPasswd.getPassword(), host.getSalt()));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addHost(DynDNSList list) {
        byte[] salt = Encryption.generateSalt();
        try {
            list.add(new DynDNSHost(jtfProvider.getText(), jtfURL.getText(), jtfUname.getText(), Encryption.encrypt(jpfPasswd.getPassword(), salt), Encryption.generateSalt()));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

}
