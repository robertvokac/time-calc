package org.nanoboot.utils.timecalc.utils.common;

import org.nanoboot.utils.timecalc.app.Main;
import org.nanoboot.utils.timecalc.swing.common.Toaster;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Robert Vokac
 * @since 15.02.2024
 */
public class Utils {

    public static final Color ULTRA_LIGHT_GRAY = new Color(216, 216, 216);
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
            byte[] strToBytes = text.getBytes(StandardCharsets.UTF_8);
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

    /**
     * Returns version of "Time Calc" from jar file.
     *
     * @return version
     */
    public static String getVersion() {
        String version = Main.class.getPackage().getImplementationVersion();
        return version == null ? "" : version;
    }

    /**
     * Returns build date of "Time Calc" from jar file.
     *
     * @return build date
     */
    public static String getBuildDate() {
        Class clazz = Main.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            return null;
        }
        String manifestPath
                = classPath.substring(0, classPath.lastIndexOf("!") + 1)
                  + "/META-INF/MANIFEST.MF";
        Manifest manifest;
        try {
            manifest = new Manifest(new URL(manifestPath).openStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "";
        }
        Attributes attr = manifest.getMainAttributes();
        return attr.getValue("Build-Date");
    }

    public static byte[] decodeBase64ToByteArray(String s) {
        Base64.Decoder base64Decoder = Base64.getDecoder();
        return base64Decoder
                .decode(s.getBytes());
    }

    public static String readTextFromTextResourceInJar(String pathToFile)
            throws IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().
                getSystemResourceAsStream(pathToFile);
        InputStreamReader streamReader = new InputStreamReader(inputStream,
                StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(streamReader);

        StringBuilder sb = new StringBuilder();
        for (String line; (line = in.readLine()) != null; ) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static void showNotification(String message) {
        showNotification(message, 0);
    }

    public static void showNotification(String message, int displayTime) {
        showNotification(message, displayTime, 0);
    }

    public static void showNotification(String message, int displayTime,
            int height) {
        Toaster toaster = new Toaster();
        toaster.setDisplayTime(displayTime == 0 ? 15000 : displayTime);
        if (height != 0) {
            toaster.setToasterHeight(height);
        }
        toaster.showToaster(message);
    }

    public static void showNotification(Exception e) {
        showNotification("Error: " + e, 15000, 200);
    }
}
