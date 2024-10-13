package com.robertvokac.utils.timecalc.utils.common;

import java.io.File;
import java.io.IOException;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class HttpProxy {

    private final String url, port, user, password;

    public HttpProxy(String url, String port, String user,
            String password) {
        this.url = url;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public HttpProxy(File proxyTxt) {
        try {
            String[] str = Utils.readTextFromFile(proxyTxt).split(":");
            if (str.length < 4) {
                proxyTxt.delete();
                throw new IOException(
                        "Invalid content of proxy.txt: str.length < 4");
            }
            this.url = str[0];
            this.port = str[1];
            this.user = str[2];
            this.password = str[3];
        } catch (IOException e) {
            throw new RuntimeException(
                    "Sorry, reading file proxy.txt failed. " + e.getMessage());
        }
    }

    public String getUrl() {
        return url;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
