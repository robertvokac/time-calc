package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.persistence.impl.sqlite.ActivityRepositorySQLiteImpl;
import org.nanoboot.utils.timecalc.persistence.impl.sqlite.SqliteConnectionFactory;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.swing.windows.ActivitiesWindow;

/**
 * @author pc00289
 * @since 05.04.2024
 */
public class ActivitiesMain {

    public static void main(String[] args) {
        TimeCalcConfiguration timeCalcConfiguration =
                new TimeCalcConfiguration();
        timeCalcConfiguration
                .loadFromTimeCalcProperties(TimeCalcProperties.getInstance());
        ActivitiesWindow activitiesWindow = new ActivitiesWindow(
                new ActivityRepositorySQLiteImpl(new SqliteConnectionFactory()),
                new Time(),
                timeCalcConfiguration
        );
        activitiesWindow.setVisible(true);
        ;
    }
}
