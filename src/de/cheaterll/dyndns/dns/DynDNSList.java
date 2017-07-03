package de.cheaterll.dyndns.dns;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CheaterLL on 01.06.2017.
 */
@XmlRootElement(name = "DynDNS")
public class DynDNSList {
    @XmlElement(name = "DynDNSHost")
    private List<DynDNSHost> hosts;

    public DynDNSList() {
        hosts = new ArrayList<>();
    }

    public DynDNSList(List<DynDNSHost> hosts) {
        this.hosts = hosts;
    }

    public static DynDNSList read(File file) throws JAXBException {
        return (DynDNSList) JAXBContext.newInstance(DynDNSList.class).createUnmarshaller().unmarshal(file);
    }

    public static void write(DynDNSList dynDNSList, File file) throws JAXBException {
        Marshaller m = JAXBContext.newInstance(DynDNSList.class).createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(dynDNSList, file);
    }

    public int size() {
        return hosts.size();
    }

    public boolean isEmpty() {
        return hosts.isEmpty();
    }

    public boolean contains(DynDNSHost host) {
        return hosts.contains(host);
    }

    public DynDNSHost find(String provider) {
        for (DynDNSHost host : hosts) {
            if (provider.equals(host.getProvider())) {
                return host;
            }
        }
        return null;
    }

    public boolean remove(DynDNSHost host) {
        return hosts.remove(host);
    }

    public DynDNSHost get(int index) {
        return hosts.get(index);
    }

    public DynDNSHost set(int index, DynDNSHost host) {
        return hosts.set(index, host);
    }

    public void add(DynDNSHost host) {
        hosts.add(host);
    }

    public DynDNSHost remove(int index) {
        return hosts.remove(index);
    }

    public int indexOf(DynDNSHost host) {
        return hosts.indexOf(host);
    }

    public List<DynDNSHost> getHosts() {
        return hosts;
    }

    public TreeNode toJTreeNodeStructure() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("DynDNS");
        if (hosts != null && hosts.size() > 0) {
            for (DynDNSHost dnsHost : hosts) {
                DefaultMutableTreeNode host = new DefaultMutableTreeNode(dnsHost);
                for (DynDNSEntry dnsEntry : dnsHost.getEntries()) {
                    DefaultMutableTreeNode entry = new DefaultMutableTreeNode(dnsEntry);
                    host.add(entry);
                }
                root.add(host);
            }
        }
        return root;
    }
}
