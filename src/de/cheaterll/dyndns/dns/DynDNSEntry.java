package de.cheaterll.dyndns.dns;

import javax.xml.bind.annotation.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by CheaterLL on 01.06.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DynDNSEntry {
    @XmlElement
    private String url;

    public DynDNSEntry() {
    }

    public DynDNSEntry(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DynDNSEntry dnsEntry = (DynDNSEntry) o;

        return getUrl() != null ? getUrl().equals(dnsEntry.getUrl()) : dnsEntry.getUrl() == null;
    }

    @Override
    public int hashCode() {
        return getUrl() != null ? getUrl().hashCode() : 0;
    }

    @Override
    public String toString() {
        return url;
    }
}
