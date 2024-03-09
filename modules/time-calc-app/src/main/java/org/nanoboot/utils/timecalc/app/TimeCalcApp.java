package org.nanoboot.utils.timecalc.app;

import java.io.File;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.Constants;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.nanoboot.utils.timecalc.persistence.api.VersionRepositoryApi;
import org.nanoboot.utils.timecalc.persistence.impl.sqlite.SqliteConnectionFactory;
import org.nanoboot.utils.timecalc.persistence.impl.sqlite.VersionRepositorySQLiteImpl;

/**
 * @author Robert Vokac
 * @since 31.01.2024
 */
public class TimeCalcApp {

    public StringProperty visibilityProperty
            = new StringProperty("timeCalcApp.visibilityProperty",
                    Visibility.WEAKLY_COLORED.name());
    private long startNanoTime = 0l;
    
    @Getter
    private SqliteConnectionFactory sqliteConnectionFactory;

    public void start(String[] args) throws IOException {
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
        while (true) {
            boolean test = FileConstants.TEST_TXT.exists();
            String oldStartTime = Utils.readTextFromFile(
                    FileConstants.STARTTIME_TXT);
            String oldOvertime = Utils.readTextFromFile(
                    FileConstants.OVERTIME_TXT);
            String newStartTime
                    = test ? (oldStartTime != null ? oldStartTime
                                    : Constants.DEFAULT_START_TIME)
                            : (String) JOptionPane.showInputDialog(
                                    null,
                                    "Start Time:",
                                    "Start Time",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    null,
                                    oldStartTime == null
                                            ? Constants.DEFAULT_START_TIME
                                            : oldStartTime
                            );
            if (newStartTime == null) {
                break;
            }
            String newOvertime
                    = test ? (oldOvertime != null ? oldOvertime
                                    : Constants.DEFAULT_OVERTIME)
                            : (String) JOptionPane.showInputDialog(
                                    null,
                                    "Overtime:",
                                    "Overtime",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    null,
                                    oldOvertime == null
                                            ? Constants.DEFAULT_OVERTIME
                                            : oldOvertime
                            );

            if (newOvertime == null) {
                break;
            }
            Utils.writeTextToFile(FileConstants.STARTTIME_TXT, newStartTime);
            Utils.writeTextToFile(FileConstants.OVERTIME_TXT, newOvertime);
            try {
                MainWindow timeCalc
                        = new MainWindow(newStartTime, newOvertime, this);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                        e.getMessage(), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
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
