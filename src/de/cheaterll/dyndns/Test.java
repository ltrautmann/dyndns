package de.cheaterll.dyndns;

import de.cheaterll.dyndns.dns.DynDNSList;
import de.cheaterll.dyndns.encryption.WebService;
import de.cheaterll.dyndns.gui.EditDynDNSHostWindow;

/**
 * Created by CheaterLL on 01.06.2017.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(WebService.getPublicIP());
    }
}
