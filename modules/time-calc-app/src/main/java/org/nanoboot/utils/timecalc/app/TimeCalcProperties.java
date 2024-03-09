package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
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

    private static TimeCalcProperties INSTANCE;

    private final Properties properties = new Properties();
    private final Map<String, String> defaultProperties = new HashMap<>();

    private TimeCalcProperties() {
        System.out.println("Loading configuration - start");
        String profileName = "";
        try {
            profileName =
                    FileConstants.TIME_CALC_CURRENT_PROFILE_TXT_FILE.exists() ?
                            Utils.readTextFromFile(
                                    FileConstants.TIME_CALC_CURRENT_PROFILE_TXT_FILE) :
                            "";
        } catch (IOException e) {
            e.printStackTrace();
            throw new TimeCalcException(e);
        }
        File file = getFile(profileName);
        if (file.exists()) {
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
                        defaultProperties.put(array[0],
                                array.length > 1 ? array[1] : "");
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
        if (!defaultProperties
                .containsKey(timeCalcProperty.getKey())) {
            throw new TimeCalcException(
                    "timecalc-default.conf is missing key: \""
                    + timeCalcProperty.getKey() + "\"");
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
        return getStringProperty(timeCalcProperty,
                getDefaultStringValue(timeCalcProperty));
    }

    private String getStringProperty(TimeCalcProperty timeCalcProperty,
            String defaultValue) {
        String key = timeCalcProperty.getKey();
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return (String) properties.get(key);
    }
    public int getIntegerProperty(TimeCalcProperty timeCalcProperty) {
        return getIntegerProperty(timeCalcProperty,
                Integer.valueOf(getDefaultStringValue(timeCalcProperty)));
    }

    private int getIntegerProperty(TimeCalcProperty timeCalcProperty,
            int defaultValue) {
        String key = timeCalcProperty.getKey();
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return Integer.valueOf((String) properties.get(key));
    }
    private String getVisibilityProperty(TimeCalcProperty timeCalcProperty) {
        return getStringProperty(timeCalcProperty,
                Visibility.STRONGLY_COLORED.name());
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

        Utils.writeTextToFile(FileConstants.TIME_CALC_CURRENT_PROFILE_TXT_FILE,
                profileName);
    }

    private File getFile(String profileName) {
        return profileName == null || profileName.isEmpty() ?
                FileConstants.FILE_WITHOUT_ANY_PROFILE :
                new File(FileConstants.FILE_WITHOUT_ANY_PROFILE.getParentFile(),
                        "timecalc." + profileName + ".conf");
    }

    public void loadProfile(String profileName) {
        File file = getFile(profileName);
        if (!file.exists()) {
            Utils.showNotification(
                    "There is no profile with name: " + profileName);
            return;
        }
        try {
            this.properties.load(new FileInputStream(file));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
