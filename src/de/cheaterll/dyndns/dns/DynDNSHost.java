package de.cheaterll.dyndns.dns;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CheaterLL on 01.06.2017.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DynDNSHost {
    @XmlElement
    private String provider;
    @XmlElement
    private String queryUrl;
    @XmlElement
    private String uname;
    @XmlElement
    private String encryptedPasswd;
    @XmlElement
    private byte[] salt;
    @XmlElement
    private List<DynDNSEntry> entries;

    public DynDNSHost() {
    }

    public DynDNSHost(String provider, String queryUrl, String uname, String encryptedPasswd, byte[] salt) {
        this.provider = provider;
        this.queryUrl = queryUrl;
        this.uname = uname;
        this.encryptedPasswd = encryptedPasswd;
        this.salt = salt;
        entries = new ArrayList<>();
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public boolean contains(DynDNSEntry dynDNSEntry) {
        return entries.contains(dynDNSEntry);
    }

    public boolean add(DynDNSEntry dynDNSEntry) {
        return entries.add(dynDNSEntry);
    }

    public boolean remove(DynDNSEntry dynDNSEntry) {
        return entries.remove(dynDNSEntry);
    }

    public DynDNSEntry get(int index) {
        return entries.get(index);
    }

    public DynDNSEntry remove(int index) {
        return entries.remove(index);
    }

    public int indexOf(DynDNSEntry dynDNSEntry) {
        return entries.indexOf(dynDNSEntry);
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEncryptedPasswd() {
        return encryptedPasswd;
    }

    public void setEncryptedPasswd(String encryptedPasswd) {
        this.encryptedPasswd = encryptedPasswd;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DynDNSHost that = (DynDNSHost) o;

        if (getProvider() != null ? !getProvider().equals(that.getProvider()) : that.getProvider() != null)
            return false;
        if (getQueryUrl() != null ? !getQueryUrl().equals(that.getQueryUrl()) : that.getQueryUrl() != null)
            return false;
        if (getUname() != null ? !getUname().equals(that.getUname()) : that.getUname() != null) return false;
        if (!getEncryptedPasswd().equals(that.getEncryptedPasswd())) return false;
        if (!Arrays.equals(getSalt(), that.getSalt())) return false;
        return entries != null ? entries.equals(that.entries) : that.entries == null;
    }

    @Override
    public int hashCode() {
        int result = getProvider() != null ? getProvider().hashCode() : 0;
        result = 31 * result + (getQueryUrl() != null ? getQueryUrl().hashCode() : 0);
        result = 31 * result + (getUname() != null ? getUname().hashCode() : 0);
        result = 31 * result + (getEncryptedPasswd() != null ? getEncryptedPasswd().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getSalt());
        result = 31 * result + (entries != null ? entries.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return provider;
    }

    public List<DynDNSEntry> getEntries() {
        return entries;
    }
}