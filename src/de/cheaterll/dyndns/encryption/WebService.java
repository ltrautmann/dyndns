package de.cheaterll.dyndns.encryption;

import de.cheaterll.dyndns.Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by CheaterLL on 18.04.2017.
 */
public class WebService {

    public static String getPublicIP() throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()));
            return in.readLine();
        }finally {
            if (in != null) {
                    in.close();
            }
        }
    }

    public static String executeConnection(HttpURLConnection con) throws IOException {
        BufferedReader br;
        if (con.getResponseCode() == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String out;
        StringBuilder ret = new StringBuilder();
        while ((out = br.readLine()) != null) {
            ret.append(out);
        }
        return ret.toString();
    }

    private static String urlEncode(String s) {
        return s.replaceAll(" ", "%20")
                .replaceAll(",", "%2C");
    }

    public static HttpURLConnection authenticateConnection(HttpURLConnection con, String uname, char[] passwd) {
        char[] unameChars = Utils.bytesToChars(uname.getBytes(), Charset.forName("UTF-8"));
        byte[] tmparr = Utils.charsToBytes(Utils.concatAll(unameChars, new char[]{':'}, passwd), Charset.forName("UTF-8"));
        String login = Encryption.base64Encode(tmparr);
        con.setRequestProperty("Authorization", "Basic " + login);
        return con;
    }

    public static HttpURLConnection getAuthenticatedConnection(URL url, String uname, char[] passwd) throws IOException {
        return authenticateConnection(getConnection(url), uname, passwd);
    }

    public static HttpURLConnection getAuthenticatedConnection(String url, String uname, char[] passwd) throws IOException {
        return authenticateConnection(getConnection(url), uname, passwd);
    }

    public static HttpURLConnection getConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    public static HttpURLConnection getConnection(String url) throws IOException {
        return getConnection(new URL(url));
    }
}

