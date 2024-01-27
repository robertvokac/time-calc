package rvc.timecalc;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

/**
 * @author Robert
 * @since 16.02.2024
 */
public class WeatherWindow extends JFrame {
    public WeatherWindow() {
        this.setSize(400, 300);

        JEditorPane jep = new JEditorPane();
        jep.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jep);
        scrollPane.setBounds(10, 10, 750, 550);
        getContentPane().add(scrollPane);

//        File proxyTxt = new File("proxy.txt");
//        if (!proxyTxt.exists()) {
//            jep.setText("Sorry, file proxy.txt was not found.");
//            return;
//        }
//        final HttpProxy httpProxy;
//        try {
//            httpProxy = new HttpProxy(proxyTxt);
//        } catch (RuntimeException e) {
//            jep.setContentType("text/html");
//            jep.setText(e.getMessage());
//            return;
//        }

        try {
            String pocasiHtml = null;
            File pocasiHtmlFile = new File("pocasi.html");
            try {
                pocasiHtml = downloadFile("https://pocasi.seznam.cz/praha");
                pocasiHtml = prettyFormatXml(pocasiHtml, 4);
                Utils.writeTextToFile(pocasiHtmlFile, pocasiHtml);
            } catch (Exception e) {
                e.printStackTrace();
                pocasiHtml = pocasiHtmlFile.exists() ? Utils.readTextFromFile(pocasiHtmlFile) : "Sorry, pocasi.html was not found.";
            }

            {
                StringBuilder sb = new StringBuilder();
                boolean ogm_detailed_forecast_Started = false;
                for(String line:pocasiHtml.split("\\r?\\n|\\r")) {

                    if(line.contains("ogm-detailed-forecast")) {
                        ogm_detailed_forecast_Started = true;
                    }
                    if(ogm_detailed_forecast_Started && line.contains("<button aria-label=")) {
                        String l = line
                                .trim()
                                .replace("<button aria-label=\"","").split("class")[0];
                        sb.append(l);
                        sb.append("<br>\n\n");
                        System.out.println("Found another line");
                    }
                }
                pocasiHtml = sb.toString();
            }

            jep.setContentType("text/html");
            jep.setText("<html><head><meta charset=\"UTF-8\"></head><body>" + pocasiHtml + "</body></html>");
            Utils.writeTextToFile(new File("aaa"),"<html><head><meta charset=\"UTF-8\"></head><body>" + pocasiHtml + "</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            jep.setContentType("text/html");
            jep.setText("<html>Could not load " + e.getMessage() + "</html>");
        }
    }

    public String downloadFile(String urlString)
            throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        if (conn.getResponseCode() == 200) {
            br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                sb.append(strCurrentLine).append("\n");
            }
        } else {
            br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                sb.append(strCurrentLine).append("\n");
            }
        }
        return sb.toString();
    }

    private static String prettyFormatXml(final String input, final int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            transformerFactory.setAttribute("indent-number", indent);

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Throwable e) {
            try {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString();
            } catch (Throwable t) {
                return input;
            }
        }
    }
}
