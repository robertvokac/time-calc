package rvc.timecalc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author pc00289
 * @since 15.02.2024
 */
public class JokesTxt {
    private JokesTxt() {
        //Not meant to be instantiated.
    }

    public static String[] getAsArray() throws IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().
                getSystemResourceAsStream("vtipy.txt");
        InputStreamReader
                streamReader = new InputStreamReader(inputStream,
                StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(streamReader);

        StringBuilder sb = new StringBuilder();
        for (String line; (line = in.readLine()) != null; ) {
            sb.append(line).append("\n");
        }
        return sb.toString().split("-----SEPARATOR-----");
    }
}
