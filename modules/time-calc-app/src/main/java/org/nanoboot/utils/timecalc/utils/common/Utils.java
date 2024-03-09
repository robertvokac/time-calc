package org.nanoboot.utils.timecalc.utils.common;

import org.nanoboot.utils.timecalc.app.Main;
import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.swing.common.Toaster;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public static List<String> loadFilesFromJarResources(String p, Class<?> clazz) throws IOException {
        List<String> result = new ArrayList<>();
        try {
            List<Path> paths = getPathsFromResourceJar(p, clazz);
            for (Path path : paths) {
                System.out.println("Path : " + path);

                String filePathInJAR = path.toString();
                
                if (filePathInJAR.startsWith("/")) {
                    filePathInJAR = filePathInJAR.substring(1, filePathInJAR.length());
                }

                System.out.println("filePathInJAR : " + filePathInJAR);
                
                String[] array = filePathInJAR.split("/");
                result.add(array[array.length - 1]);
            }

        } catch (URISyntaxException | IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
                    
            throw new TimeCalcException(e);
        }
        return result;
    }
    
        private static List<Path> getPathsFromResourceJar(String folder, Class<?> clazz)
            throws URISyntaxException, IOException {
        List<Path> result;

        String jarPath = clazz.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        System.out.println("JAR Path : " + jarPath);

        URI uri = URI.create("jar:file:" + jarPath);
        try ( FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap());Stream<Path> stream = Files.walk(fs.getPath(folder))) {
            result = stream
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } 

        return result;

    }
    public static String readTextFromTextResourceInJar(String pathToFile)
            throws IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().
                getSystemResourceAsStream(pathToFile);
        InputStreamReader streamReader = new InputStreamReader(inputStream,
                StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(streamReader);

        StringBuilder sb = new StringBuilder();
        for (String line; (line = in.readLine()) != null;) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
    
    public static String loadStacktrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
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

    public static boolean askYesNo(Component frame, String question, String title) {
        int result = JOptionPane
                .showConfirmDialog(frame, question,
                        title,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            return true;
        } else if (result == JOptionPane.NO_OPTION) {
            return false;
        } else {
            return false;
        }
    }
}
