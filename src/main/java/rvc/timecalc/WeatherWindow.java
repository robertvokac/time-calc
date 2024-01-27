package rvc.timecalc;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

/**
 * @author Robert
 * @since 16.02.2024
 */
public class WeatherWindow extends JFrame {
    public WeatherWindow() {
        this.setSize(800, 600);

        JEditorPane jep = new JEditorPane();
        jep.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jep);
        scrollPane.setBounds(10, 10, 750, 550);
        getContentPane().add(scrollPane);

        File proxyTxt = new File("proxy.txt");
        if(!proxyTxt.exists()) {
            jep.setText("Sorry, file proxy.txt was not found.");
            return;
        }
        final HttpProxy httpProxy;
        try {
             httpProxy = new HttpProxy(proxyTxt);
        } catch(RuntimeException e) {
            jep.setContentType("text/html");
            jep.setText(e.getMessage());
            return;
        }



        try {
            jep.setText(downloadFile3("https://pocasi.seznam.cz/praha",
                    httpProxy));
        }catch (Exception e) {
            e.printStackTrace();
            jep.setContentType("text/html");
            jep.setText("<html>Could not load " + e.getMessage() + "</html>");
        }
    }

    public String downloadFile2(String s, HttpProxy httpProxy) throws IOException {

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(httpProxy.getUser(),
                        httpProxy.getPassword().toCharArray());
            }
        });

        Proxy proxy = new Proxy(
                Proxy.Type.HTTP, new InetSocketAddress(httpProxy.getUrl(), Integer.valueOf(httpProxy.getPort())));

        URL url = new URL(s);
        System.getProperties().put( "proxySet", "true" );

        System.setProperty("http.proxyHost", httpProxy.getUrl());
        System.setProperty("http.proxyPort", httpProxy.getPort());

        System.setProperty("http.proxyUser", httpProxy.getUser());
        System.setProperty("http.proxyPassword", httpProxy.getPassword());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            return responseBuilder.toString();
        }
    }
    public String downloadFile3(String s, HttpProxy httpProxy)
            throws IOException {
        System.getProperties().put( "proxySet", "true" );

        System.setProperty("http.proxyHost", httpProxy.getUrl());
        System.setProperty("http.proxyPort", httpProxy.getPort());

        System.setProperty("http.proxyUser", httpProxy.getUser());
        System.setProperty("http.proxyPassword", httpProxy.getPassword());

        String uri = s;
        URL url = new URL(uri);

        Authenticator.setDefault(new Authenticator() {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(httpProxy.getUser(),
                    httpProxy.getPassword().toCharArray());
        }});

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        //connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "text/html");
        connection.setRequestProperty("Accept", "text/html");
        connection.setRequestProperty("Method", "GET");
        connection.setRequestProperty("Encoding", "UTF-8");
        connection.setReadTimeout(60000);

        InputStream stream = connection.getInputStream();

        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line + "\n");
        }

        bufferedReader.close();
        return content.toString();
    }
}
