package de.cheaterll.dyndns;

import de.cheaterll.dyndns.dns.DynDNSList;
import de.cheaterll.dyndns.encryption.Encryption;
import de.cheaterll.dyndns.gui.LoginWindow;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by CheaterLL on 01.06.2017.
 */
public class Main {
    private static DynDNSList dynDNSList;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        File xml = new File("dyndns.xml");
        if (xml.exists()) {
            try {
                dynDNSList = DynDNSList.read(xml);
            } catch (JAXBException e) {
                dynDNSList = new DynDNSList();
            }
        } else {
            dynDNSList = new DynDNSList();
        }
        SwingUtilities.invokeLater(LoginWindow::new);
    }

    public static DynDNSList getDynDNSList() {
        return dynDNSList;
    }

    private void intiTray(){
        if (!SystemTray.isSupported()) {
            System.out.println("Error: Tray Icon is not supported!");
            System.exit(1);
        }
        final PopupMenu popupMenu = new PopupMenu();
        ImageIcon icon = new ImageIcon(Main.class.getResource("/de/cheaterll/dyndns/resources/images/dyndns.png"));
        TrayIcon trayIcon = new TrayIcon(icon.getImage());
        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem showItem = new MenuItem("Show");
        MenuItem updateItem = new MenuItem("Update Now");
        MenuItem quitItem = new MenuItem("Quit");
        popupMenu.add(showItem);
        popupMenu.add(updateItem);
        popupMenu.addSeparator();
        popupMenu.add(quitItem);

        trayIcon.setPopupMenu(popupMenu);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Failed to add Tray Icon to Tray Bar.");
            System.exit(1);
        }

        showItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        updateItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        quitItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
            }
        });
    }
}
