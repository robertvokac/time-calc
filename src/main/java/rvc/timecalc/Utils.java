package rvc.timecalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author Robert
 * @since 15.02.2024
 */
public class Utils {
    private Utils() {
        //Not meant to be instantiated.
    }

    /**
     * Count of bytes per one kilobyte.
     */
    private static final int COUNT_OF_BYTES_PER_ONE_KILOBYTE = 1024;
    public static final File highlightTxt = new File("highlight.txt");
    /**
     * Writes text to a file.
     * @param file file
     * @param text text
     */
    public static void writeTextToFile(final File file, final String text) {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file.getAbsolutePath());
            byte[] strToBytes = text.getBytes();
            outputStream.write(strToBytes);

            outputStream.close();
        } catch (IOException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Reads text from file.
     * @param file file
     * @return String
     * @throws IOException thrown, if an error during reading happens
     */
    public static String readTextFromFile(final File file)
            throws IOException {
        if(!file.exists()) {
            //nothing to do
            return null;
        }
        return new String(Files.readAllBytes(file.toPath()));
    }
}
