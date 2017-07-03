package de.cheaterll.dyndns.gui;

import de.cheaterll.dyndns.dns.DynDNSEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by CheaterLL on 15.06.2017.
 */
public class EditDynDNSEntryPanel extends JPanel {
    JTextField jtfEntryURL;
    JButton jbSave;
    MainWindow parent;

    public EditDynDNSEntryPanel(MainWindow parent) {
        this.parent = parent;
        initComponents();
        initEvents();
    }

    private void initComponents() {
        jbSave = new JButton();
        JPanel jpCenter = new JPanel();
        JPanel jpInner = new JPanel();
        JPanel jpSouth = new JPanel();
        JLabel jlEntryURL = new JLabel("URL to update");
        jtfEntryURL = new JTextField(25);

        GroupLayout layout = new GroupLayout(jpInner);
        jpInner.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(jlEntryURL));
        hGroup.addGroup(layout.createParallelGroup().addComponent(jtfEntryURL));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jlEntryURL).addComponent(jtfEntryURL));
        layout.setVerticalGroup(vGroup);

        jbSave = new JButton("Save");
        jpSouth.add(jbSave);

        setLayout(new BorderLayout());
        jpCenter.setLayout(new GridBagLayout());
        jpCenter.add(jpInner);
        add(jpCenter, BorderLayout.CENTER);
        add(jpSouth, BorderLayout.SOUTH);
    }

    private void initEvents() {
        jbSave.addActionListener(e -> parent.saveDynDNSEntry(jtfEntryURL.getText()));
    }

    public void setEntry(DynDNSEntry entry) {
        jtfEntryURL.setText(entry.getUrl());
    }
}
