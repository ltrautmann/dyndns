package de.cheaterll.dyndns.gui;

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
import java.io.File;

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
        if (xml.exists()) {
            try {
                dynDNSList = DynDNSList.read(xml);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        } else {
            dynDNSList = new DynDNSList();
        }
        initComponents();
        initEvents();
        setSize(800, 500);
        setLocationRelativeTo(null);
        setTitle("DynDNS");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        c = getContentPane();

        jpLeft = new JPanel(new BorderLayout());
        jpLeft.setPreferredSize(new Dimension(400, 500));
        jTree = new JTree(dynDNSList.toJTreeNodeStructure());
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        //((DefaultTreeModel)jTree.getModel()).setAsksAllowsChildren(true);
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
        //jpRight = entryPanel;

        c.add(jpLeft, BorderLayout.WEST);
        c.add(jpRight, BorderLayout.EAST);
    }

    private void initEvents() {
        jTree.addTreeSelectionListener(e -> {
            Class selectedNodeClass = getSelectedNodeClass();
            System.out.println("JTree Selection Changed to " + e.getPath() + " of " + selectedNodeClass);
            treeSelectionChanged(selectedNodeClass);
        });
        jTree.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Class selectedNodeClass = getSelectedNodeClass();
                System.out.println("JTree gained focus, selected element is of " + selectedNodeClass);
                treeSelectionChanged(selectedNodeClass);
            }

            @Override
            public void focusLost(FocusEvent e) {
                Class selectedNodeClass = getSelectedNodeClass();
                System.out.println("JTree lost focus, selected element is of " + selectedNodeClass);
                treeSelectionChanged(selectedNodeClass);
            }
        });

        jbAdd.addActionListener(e -> {
            jTree.requestFocus();
            System.out.println("Add");
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
                model.nodesWereInserted(parent, new int[]{index});
                pleaseJustScrollToThatFuckingPieceOfFuckingCrapBullShit(new TreePath(node), selectedNodeClass);
                pleaseJustScrollToThatFuckingPieceOfFuckingCrapBullShit(new TreePath(parent), selectedNodeClass);
            }
        });
        jbRemove.addActionListener(e -> {
            jTree.requestFocus();
            System.out.println("Remove");
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
//                model.reload();
                TreePath treePath = new TreePath(parent);
                pleaseJustScrollToThatFuckingPieceOfFuckingCrapBullShit(new TreePath(node), selectedNodeClass);
                pleaseJustScrollToThatFuckingPieceOfFuckingCrapBullShit(new TreePath(parent), selectedNodeClass);
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
    }

    private void pleaseJustScrollToThatFuckingPieceOfFuckingCrapBullShit(TreePath treePath, Class selectedNodeClass) {
        jTree.requestFocus();
        jTree.makeVisible(treePath);
        jTree.setSelectionPath(treePath);
        jTree.scrollPathToVisible(treePath);
        jTree.requestFocus();
        treeSelectionChanged(selectedNodeClass);
    }

    private void treeSelectionChanged(Class selectedNodeClass) {
        if (DynDNSEntry.class.equals(selectedNodeClass)) {
            System.out.println("Entry selected");
            this.c.remove(jpRight);
            jpRight = entryPanel;
            this.c.add(jpRight, BorderLayout.EAST);
            jbAdd.setText("Add");
            jbRemove.setText("Remove");
            jbAdd.setEnabled(false);
            jbRemove.setEnabled(true);
            revalidate();
            repaint();
        }
        if (DynDNSHost.class.equals(selectedNodeClass)) {
            System.out.println("Host selected");
            this.c.remove(jpRight);
            jpRight = hostPanel;
            this.c.add(jpRight, BorderLayout.EAST);
            jbAdd.setText("Add Entry");
            jbRemove.setText("Remove Entry");
            jbAdd.setEnabled(true);
            jbRemove.setEnabled(true);
            revalidate();
            repaint();
        }
        if (DynDNSList.class.equals(selectedNodeClass)) {
            System.out.println("Root selected");
            this.c.remove(jpRight);
            jpRight = new JPanel();
            this.c.add(jpRight, BorderLayout.EAST);
            jbAdd.setText("Add Host");
            jbRemove.setText("Remove Host");
            jbAdd.setEnabled(true);
            jbRemove.setEnabled(false);
            revalidate();
            repaint();
        }
    }

    private Class getSelectedNodeClass() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
        if (node == null) {
            return null;
        }
        if (node.isRoot()) {
            System.out.println(DynDNSList.class);
            return DynDNSList.class;
        }
        if (((DefaultMutableTreeNode) node.getParent()).isRoot()) {
            System.out.println(DynDNSHost.class);
            return DynDNSHost.class;
        } else {
            System.out.println(DynDNSEntry.class);
            return DynDNSEntry.class;
        }
    }

    protected void saveDynDNSEntry(String url) {

    }

    protected void saveDynDNSHost(String provider, String queryurl, String username, char[] password) {

    }


}
