package rvc.timecalc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Robert
 * @since 15.02.2024
 */
public class VtipyTxt {
    private VtipyTxt() {
        //Not meant to be instantiated.
    }
    public static String[] getAsArray() throws IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().
                getSystemResourceAsStream("vtipy.txt");
        InputStreamReader
                streamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader in = new BufferedReader(streamReader);

        StringBuilder sb = new StringBuilder();
        for (String line; (line = in.readLine()) != null;) {
            sb.append(line).append("\n");
        }
        return sb.toString().split("-----SEPARATOR-----");
    }
}
