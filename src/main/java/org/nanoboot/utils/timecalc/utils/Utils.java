package org.nanoboot.utils.timecalc.utils;

import org.nanoboot.utils.timecalc.main.Main;
import org.nanoboot.utils.timecalc.main.TimeCalcException;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author pc00289
 * @since 15.02.2024
 */
public class Utils {
    private static long startNanoTime;
    public static final BooleanHolder highlighted = new BooleanHolder();
    public static final BooleanHolder ultraLight = new BooleanHolder();
    public static final BooleanHolder everythingHidden = new BooleanHolder();
    public static final BooleanHolder toastsAreEnabled = new BooleanHolder(true);
    public static final Color ULTRA_LIGHT_GRAY = new Color(216,216,216);
    /**
     * Count of bytes per one kilobyte.
     */
    private static final int COUNT_OF_BYTES_PER_ONE_KILOBYTE = 1024;
    public static void startApp() {
        if(startNanoTime == 0) {
            throw new TimeCalcException("App is already started.");
        }
        startNanoTime = System.nanoTime();
    }
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

    public static int getCountOfMinutesSinceAppStarted() {
        return ((int)((System.nanoTime() - startNanoTime) / 1000000000 / 60));
    }

    /**
     * Returns version of "Time Calc" from jar file.
     * @return version
     */
    public static String getVersion() {
        String version = Main.class.getPackage().getImplementationVersion();
        return version;
    }

    /**
     * Returns build date of "Time Calc" from jar file.
     * @return build date
     */
    public static String getBuildDate() {
        Class clazz = Main.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            return null;
        }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1)
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
}
