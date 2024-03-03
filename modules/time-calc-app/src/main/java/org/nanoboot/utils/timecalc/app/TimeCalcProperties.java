package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class TimeCalcProperties {

    public static final File FILE_WITHOUT_ANY_PROFILE = new File("timecalc.conf");
    private static TimeCalcProperties INSTANCE;
    private static final File timeCalcCurrentProfileTxtFile = new File("time-calc-current-profile.txt");
    private final Properties properties = new Properties();
    private final Map<String, String> defaultProperties = new HashMap<>();

    private TimeCalcProperties() {
        System.out.println("Loading configuration - start");
        String profileName = "";
        try {
            profileName = timeCalcCurrentProfileTxtFile.exists() ? Utils.readTextFromFile(
                    timeCalcCurrentProfileTxtFile) : "";
        } catch (IOException e) {
            e.printStackTrace();
            throw new TimeCalcException(e);
        }
        File file = getFile(profileName);
        if(file.exists()) {
            try {
                this.properties.load(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e);
            }
        }
        System.out.println("Loading configuration - end");
        System.out.println("Loading default configuration - start");
        try {
            String defaultConfiguration = Utils.readTextFromTextResourceInJar(
                    "timecalc-default.conf");
            Arrays.stream(defaultConfiguration.split("\n"))
                    .filter(l -> !l.trim().isEmpty())
                    .filter(l -> !l.trim().startsWith("#"))
                    .filter(l -> l.contains("="))
                    .forEach(l -> {
                        String[] array = l.split("=");
                        defaultProperties.put(array[0], array.length > 1 ? array[1] : "");
                    });

        } catch (IOException e) {
            e.printStackTrace();
            throw new TimeCalcException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println("Loading default configuration - end");
    }

    public static TimeCalcProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimeCalcProperties();
        }
        return INSTANCE;
    }

    public boolean getBooleanProperty(TimeCalcProperty timeCalcProperty) {
        return getBooleanProperty(timeCalcProperty, Boolean.valueOf(
                getDefaultStringValue(timeCalcProperty)));
    }

    private String getDefaultStringValue(TimeCalcProperty timeCalcProperty) {
        if (!defaultProperties.containsKey((String) timeCalcProperty.getKey())) {
            throw new TimeCalcException("timecalc-default.conf is missing key: \"" + timeCalcProperty.getKey() + "\"");
        }
        return defaultProperties.get(timeCalcProperty.getKey());
    }

    private boolean getBooleanProperty(TimeCalcProperty timeCalcProperty,
            Boolean defaultValue) {
        String key = timeCalcProperty.getKey();
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return properties.get(key).equals("true");
    }

    public String getStringProperty(TimeCalcProperty timeCalcProperty) {
        return getStringProperty(timeCalcProperty, getDefaultStringValue(timeCalcProperty));
    }

    private String getStringProperty(TimeCalcProperty timeCalcProperty,
            String defaultValue) {
        String key = timeCalcProperty.getKey();
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return (String) properties.get(key);
    }

    private String getVisibilityProperty(TimeCalcProperty timeCalcProperty) {
        return getStringProperty(timeCalcProperty, Visibility.STRONGLY_COLORED.name());
    }

    private void setBooleanProperty(TimeCalcProperty timeCalcProperty,
            Boolean value) {
        String key = timeCalcProperty.getKey();
        properties.replace(key, value.toString());
    }

    private void setStringProperty(TimeCalcProperty timeCalcProperty,
            String value) {
        String key = timeCalcProperty.getKey();
        properties.replace(key, value);
    }

    private void setVisibilityProperty(TimeCalcProperty timeCalcProperty,
            Visibility value) {
        String key = timeCalcProperty.getKey();
        properties.replace(key, value.name());
    }

    public void save(Properties properties, String profileName) {
        properties.entrySet().stream().forEach(e
                -> {
            if (this.properties.containsKey(e.getKey())) {
                this.properties
                        .replace(e.getKey(), e.getValue().toString());
            } else {
                this.properties
                        .put(e.getKey(), e.getValue().toString());
            }
        }
        );
        File file = getFile(profileName);
        try {
            this.properties.store(new FileOutputStream(file), null);
            System.out.println("Saving to " + file + " was successful");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(
                    "Saving to " + file + " failed: " + e.getMessage());
        }

        Utils.writeTextToFile(timeCalcCurrentProfileTxtFile, profileName);
    }

    private File getFile(String profileName) {
        return profileName == null || profileName.isEmpty() ?
                FILE_WITHOUT_ANY_PROFILE :
                new File("timecalc." + profileName + ".conf");
    }

    public void loadProfile(String profileName) {
        try {
            this.properties.load( new FileInputStream(getFile(profileName)));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
