package de.cheaterll.dyndns.dns;

import de.cheaterll.dyndns.encryption.Encryption;
import de.cheaterll.dyndns.encryption.WebService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.GeneralSecurityException;

/**
 * Created by CheaterLL on 21.06.2017.
 */
public class DynDNSUpdateThread {
    private static DynDNSUpdateThread instance;
    private static Thread thread;

    public static DynDNSUpdateThread getInstance() {
        if (instance == null) {
            instance = new DynDNSUpdateThread();
        }
        return instance;
    }

    private DynDNSUpdateThread() {
        thread = new Thread(() -> {

        });
    }

    private void updateAll(DynDNSList list) throws GeneralSecurityException, IOException {
        if (list == null || list.size() < 1) return;
        for (DynDNSHost host : list.getHosts()) {
            for (DynDNSEntry entry : host.getEntries()) {
                update(host.getQueryUrl(), entry.getUrl(), host.getUname(), Encryption.decrypt(host.getEncryptedPasswd(), host.getSalt()));
            }
        }
    }

    private void update(String queryURL, String updateURL, String username, char[] password) throws IOException {
        queryURL = queryURL.replace("$url", updateURL);
        queryURL = queryURL.replace("$ip", WebService.getPublicIP());
        HttpURLConnection connection = WebService.getAuthenticatedConnection(queryURL, username, password);

    }
}
