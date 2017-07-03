package de.cheaterll.dyndns.gui;

import de.cheaterll.dyndns.Main;
import de.cheaterll.dyndns.dns.DynDNSEntry;
import de.cheaterll.dyndns.dns.DynDNSHost;
import de.cheaterll.dyndns.dns.DynDNSList;
import de.cheaterll.dyndns.encryption.Encryption;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.bind.JAXBException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by CheaterLL on 01.06.2017.
 */
public class MainWindow extends JFrame {

    private Container c;
    private JPanel jpLeft;
    private EditDynDNSHostPanel hostPanel;
    private EditDynDNSEntryPanel entryPanel;
    private JPanel jpRight;
    private JTree jTree;
    private JScrollPane jspTree;
    private JPanel jpTreeButtonsTop;
    private JPanel jpTreeButtonsBottom;
    private JButton jbAdd;
    private JButton jbRemove;
    private JButton jbExpandAll;
    private JButton jbCollapseAll;
    private DynDNSList dynDNSList;


    public MainWindow(char[] password) {
        Encryption.setPassword(password);
        File xml = new File("dyndns.xml");
        dynDNSList = Main.getDynDNSList();
        initComponents();
        initEvents();
        setSize(800, 500);
        setLocationRelativeTo(null);
        setTitle("DynDNS");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        c = getContentPane();

        jpLeft = new JPanel(new BorderLayout());
        jpLeft.setPreferredSize(new Dimension(400, 500));
        jTree = new JTree(dynDNSList.toJTreeNodeStructure());
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jspTree = new JScrollPane(jTree);
        jpLeft.add(jspTree, BorderLayout.CENTER);

        jpTreeButtonsTop = new JPanel();
        jbAdd = new JButton("Add");
        jbRemove = new JButton("Remove");
        jbAdd.setEnabled(false);
        jbRemove.setEnabled(false);
        jpTreeButtonsTop.add(jbAdd);
        jpTreeButtonsTop.add(jbRemove);
        jpLeft.add(jpTreeButtonsTop, BorderLayout.NORTH);

        jpTreeButtonsBottom = new JPanel();
        jbExpandAll = new JButton("Expand All");
        jbCollapseAll = new JButton("Collapse All");
        jpTreeButtonsBottom.add(jbExpandAll);
        jpTreeButtonsBottom.add(jbCollapseAll);
        jpLeft.add(jpTreeButtonsBottom, BorderLayout.SOUTH);

        entryPanel = new EditDynDNSEntryPanel(this);
        hostPanel = new EditDynDNSHostPanel(this);

        jpRight = new JPanel();

        c.add(jpLeft, BorderLayout.WEST);
        c.add(jpRight, BorderLayout.EAST);
    }

    private void initEvents() {
        jTree.addTreeSelectionListener(e -> {
            treeSelectionChanged(getSelectedNodeClass());
        });
        jTree.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                treeSelectionChanged(getSelectedNodeClass());
            }

            @Override
            public void focusLost(FocusEvent e) {
                treeSelectionChanged(getSelectedNodeClass());
            }
        });

        jbAdd.addActionListener(e -> {
            jTree.requestFocus();
            Class selectedNodeClass = getSelectedNodeClass();
            DefaultMutableTreeNode parent = null;
            DefaultMutableTreeNode node = null;
            if (DynDNSList.class.equals(selectedNodeClass)) {
                parent = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                DynDNSHost newHost = new DynDNSHost("New Provider", "", "", "", Encryption.generateSalt());
                dynDNSList.add(newHost);
                node = new DefaultMutableTreeNode(newHost);
            } else if (DynDNSHost.class.equals(selectedNodeClass)) {
                parent = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                DynDNSEntry newEntry = new DynDNSEntry("New Entry URL");
                DynDNSHost host = (DynDNSHost) parent.getUserObject();
                host.add(newEntry);
                node = new DefaultMutableTreeNode(newEntry);
            }
            if (node != null) {
                DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                int index = parent.getChildCount();
                model.insertNodeInto(node, parent, index);
                jTree.expandPath(new TreePath(parent.getPath()));
                jTree.setSelectionPath(new TreePath(node.getPath()));
            }
        });
        jbRemove.addActionListener(e -> {
            jTree.requestFocus();
            Class selectedNodeClass = getSelectedNodeClass();
            DefaultMutableTreeNode parent = null;
            DefaultMutableTreeNode node = null;
            if (DynDNSEntry.class.equals(selectedNodeClass)) {
                node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                parent = (DefaultMutableTreeNode) node.getParent();
                ((DynDNSHost) parent.getUserObject()).remove((DynDNSEntry) node.getUserObject());
            } else if (DynDNSHost.class.equals(selectedNodeClass)) {
                node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                parent = (DefaultMutableTreeNode) node.getParent();
                dynDNSList.remove((DynDNSHost) node.getUserObject());
            }
            if (node != null) {
                DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                model.removeNodeFromParent(node);
                jTree.expandPath(new TreePath(parent.getPath()));
                jTree.setSelectionPath(new TreePath(parent.getPath()));
            }
        });
        jbExpandAll.addActionListener(e -> {
            for (int i = 0; i < jTree.getRowCount(); i++) {
                jTree.expandRow(i);
            }
        });
        jbCollapseAll.addActionListener(e -> {
            for (int i = 0; i < jTree.getRowCount(); i++) {
                jTree.collapseRow(i);
            }
//            ((DefaultMutableTreeNode)jTree.getModel().getRoot())
        });
        MainWindow.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int i = JOptionPane.showConfirmDialog(MainWindow.this, "Speichern?", "Speichern?", JOptionPane.YES_NO_CANCEL_OPTION);
                if (i == JOptionPane.YES_OPTION) {
                    try {
                        DynDNSList.write(dynDNSList, new File("dyndns.xml"));
                        System.exit(0);
                    } catch (JAXBException e1) {
                        e1.printStackTrace();
                    }
                } else if (i == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void treeSelectionChanged(Class selectedNodeClass) {
        if (DynDNSEntry.class.equals(selectedNodeClass)) {
            this.c.remove(jpRight);
            jpRight = entryPanel;
            entryPanel.setEntry((DynDNSEntry) ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject());
            this.c.add(jpRight, BorderLayout.EAST);
            jbAdd.setText("Add");
            jbRemove.setText("Remove Entry");
            jbAdd.setEnabled(false);
            jbRemove.setEnabled(true);
            revalidate();
            repaint();
        }
        if (DynDNSHost.class.equals(selectedNodeClass)) {
            this.c.remove(jpRight);
            jpRight = hostPanel;
            hostPanel.setHost((DynDNSHost) ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject());
            this.c.add(jpRight, BorderLayout.EAST);
            jbAdd.setText("Add Entry");
            jbRemove.setText("Remove Host");
            jbAdd.setEnabled(true);
            jbRemove.setEnabled(true);
            revalidate();
            repaint();
        }
        if (DynDNSList.class.equals(selectedNodeClass)) {
            this.c.remove(jpRight);
            jpRight = new JPanel();
            this.c.add(jpRight, BorderLayout.EAST);
            jbAdd.setText("Add Host");
            jbRemove.setText("Remove");
            jbAdd.setEnabled(true);
            jbRemove.setEnabled(false);
            revalidate();
            repaint();
        }
    }

    private Class getSelectedNodeClass() {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
            if (node == null) {
                return null;
            }
            if (node.isRoot()) {
                return DynDNSList.class;
            }
            if (((DefaultMutableTreeNode) node.getParent()).isRoot()) {
                return DynDNSHost.class;
            } else {
                return DynDNSEntry.class;
            }
        } catch (Exception e) {
            System.out.println("EXCEPTIONS WERE THROWN. ALSO FUCK YOU");
            return null;
        }
    }

    protected void saveDynDNSEntry(String url) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
        DynDNSEntry entry = (DynDNSEntry) node.getUserObject();
        entry.setUrl(url);
        node.setUserObject(entry);
        jTree.requestFocus();
    }

    protected void saveDynDNSHost(String provider, String queryurl, String username, char[] password) {
        if (provider == null || queryurl == null || username == null || password == null || password.length < 1) {
            if (JOptionPane.CANCEL_OPTION == JOptionPane.showConfirmDialog(MainWindow.this, "Das Password-Feld ist leer!", "Achtung!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)) {
                return;
            }
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
        DynDNSHost host = (DynDNSHost) node.getUserObject();
        host.setProvider(provider);
        host.setQueryUrl(queryurl);
        host.setUname(username);
        if (password != null && password != new char[0]) {
            try {
                String encryptedPassword = Encryption.encrypt(password, host.getSalt());
                host.setEncryptedPasswd(encryptedPassword);
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace(); //Should not happen
            }
        }
        node.setUserObject(host);
        jTree.requestFocus();
    }


}
