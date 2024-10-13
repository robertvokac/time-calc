package com.robertvokac.utils.timecalc.app;

import java.io.File;
import com.robertvokac.utils.timecalc.entity.Visibility;
import com.robertvokac.utils.timecalc.swing.windows.MainWindow;
import com.robertvokac.utils.timecalc.utils.common.DateFormats;
import com.robertvokac.utils.timecalc.utils.common.FileConstants;
import com.robertvokac.utils.timecalc.utils.common.Utils;
import com.robertvokac.utils.timecalc.utils.property.Property;
import com.robertvokac.utils.timecalc.utils.property.ReadOnlyProperty;
import com.robertvokac.utils.timecalc.utils.property.StringProperty;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import com.robertvokac.utils.timecalc.persistence.api.VersionRepositoryApi;
import com.robertvokac.utils.timecalc.persistence.impl.sqlite.SqliteConnectionFactory;
import com.robertvokac.utils.timecalc.persistence.impl.sqlite.VersionRepositorySQLiteImpl;
import static com.robertvokac.utils.timecalc.app.Main.ONLY_ACTIVITIES_WINDOW_IS_ALLOWED;

/**
 * @author Robert Vokac
 * @since 31.01.2024
 */
public class TimeCalcApp {

    public StringProperty visibilityProperty
            = new StringProperty("timeCalcApp.visibilityProperty",
                    Visibility.WEAKLY_COLORED.name());
    private long startNanoTime = 0l;
    private Property<Boolean> allowOnlyBasicFeaturesReadWriteProperty = new Property<>("allowOnlyBasicFeaturesReadWriteProperty", false);
    public ReadOnlyProperty<Boolean> allowOnlyBasicFeaturesProperty = new ReadOnlyProperty<>(allowOnlyBasicFeaturesReadWriteProperty);
    
    @Getter
    private SqliteConnectionFactory sqliteConnectionFactory;

    public static final boolean IS_RUNNING_ON_LINUX = System.getProperty("os.name").equals("Linux");
    public void start(String[] args) throws IOException {
        File dbFile = new File(FileConstants.TC_DIRECTORY.getAbsolutePath() + "/" + "time-calc.sqlite3");
        File dbFileBackup = new File(dbFile.getAbsolutePath() + ".backup." + DateFormats.DATE_TIME_FORMATTER_SHORT.format(new Date()).substring(0, 10) + ".sqlite3");
        if(dbFile.exists() && !dbFileBackup.exists()) {
            Files.copy(dbFile.toPath(), dbFileBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        if (startNanoTime != 0l) {
            throw new TimeCalcException("TimeCalcApp was already started.");
        }
        startNanoTime = System.nanoTime();
        if (!FileConstants.TC_DIRECTORY.exists()) {
            FileConstants.TC_DIRECTORY.mkdir();
        }
        try {
        initDB();
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(FileConstants.BASIC_TXT.exists()) {
            allowOnlyBasicFeaturesReadWriteProperty.setValue(true);
        }
        
        while (true) {
            if(!allowOnlyBasicFeaturesProperty.getValue() && FileConstants.BASIC_TXT.exists()) {
                allowOnlyBasicFeaturesReadWriteProperty.setValue(true);
            }

            MainWindow timeCalcMainWindow = null;
            try {
                timeCalcMainWindow = new MainWindow(this);
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                        e.getMessage(), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                timeCalcMainWindow.setVisible(false);
                timeCalcMainWindow.dispose();
            }
            if(ONLY_ACTIVITIES_WINDOW_IS_ALLOWED) {
                break;
            }
        }

    }

    public long getCountOfMinutesSinceAppStarted() {
        return getCountOfSecondsSinceAppStarted() / 60l;
    }

    public long getCountOfSecondsSinceAppStarted() {
        return getCountOfMillisecondsSinceAppStarted() / 1000000000l;
    }

    public long getCountOfMillisecondsSinceAppStarted() {
        if (startNanoTime == 0l) {
            throw new TimeCalcException("TimeCalcApp was not yet started.");
        }
        return System.nanoTime() - startNanoTime;
    }

    private void initDB() {
        this.sqliteConnectionFactory = new SqliteConnectionFactory();
        try {
            Connection conn = sqliteConnectionFactory.createConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TimeCalcApp.class.getName()).log(Level.SEVERE, null, ex);
            throw new TimeCalcException(ex);
        }
        List<String> files;
        try {
            
            files = Utils.loadFilesFromJarResources(DB_MIGRATIONSQLITETIMECALC, getClass());
        } catch (IOException ex) {
            Logger.getLogger(TimeCalcApp.class.getName()).log(Level.SEVERE, null, ex);
            throw new TimeCalcException(ex);
        }
        Collections.sort(files);
        int lastVersion = 0;
        VersionRepositoryApi versionRepository = new VersionRepositorySQLiteImpl(sqliteConnectionFactory);
        int currentVersion = 0;
        try {
            currentVersion = versionRepository.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("currentVersion=" + currentVersion);
        for(String sql :files) {
            Integer version = Integer.valueOf(sql.split("__")[0].substring(1));
            lastVersion = version;
            System.out.println("version=" + version);
            if(currentVersion >= version) {
                continue;
            }
            try {
                System.out.println("Reading this file: " + DB_MIGRATIONSQLITETIMECALC + "/" + sql);
                String text = Utils.readTextFromTextResourceInJar(DB_MIGRATIONSQLITETIMECALC + "/" + sql);
                System.out.println("Found sql: \n\n" + text);
                sqliteConnectionFactory.runSQL(text);
            } catch (IOException ex) {
                Logger.getLogger(TimeCalcApp.class.getName()).log(Level.SEVERE, null, ex);
                throw new TimeCalcException(ex);
            }
        }
        
        if(currentVersion != lastVersion) {
            versionRepository.update(lastVersion);
        }
    }
    private static final String DB_MIGRATIONSQLITETIMECALC = "db_migrations/sqlite/timecalc";

}
