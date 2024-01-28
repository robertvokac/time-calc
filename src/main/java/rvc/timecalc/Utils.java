package rvc.timecalc;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author Robert
 * @since 15.02.2024
 */
public class Utils {
    public static final BooleanHolder highlighted = new BooleanHolder();
    public static final BooleanHolder ultraLight = new BooleanHolder();
    public static final Color ULTRA_LIGHT_GRAY = new Color(216,216,216);
    /**
     * Count of bytes per one kilobyte.
     */
    private static final int COUNT_OF_BYTES_PER_ONE_KILOBYTE = 1024;
    private Utils() {
        //Not meant to be instantiated.
    }

    /**
     * Writes text to a file.
     *
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
     *
     * @param file file
     * @return String
     * @throws IOException thrown, if an error during reading happens
     */
    public static String readTextFromFile(final File file)
            throws IOException {
        if (!file.exists()) {
            //nothing to do
            return null;
        }
        return new String(Files.readAllBytes(file.toPath()),
                StandardCharsets.UTF_8);
    }

    public static Color[] getRandomColors() {
        Color[] result = new Color[12];
        for (int i = 0; i < 12; i++) {
            result[i] = getRandomColor();
        }
        return result;
    }

    public static Color getRandomColor() {
        return new Color(((int) (Math.random() * 256)),
                ((int) (Math.random() * 256)), ((int) (Math.random() * 256)));
    }
}
