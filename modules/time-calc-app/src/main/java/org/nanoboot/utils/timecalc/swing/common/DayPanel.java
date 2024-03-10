package org.nanoboot.utils.timecalc.swing.common;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author robertvokac
 */
public class DayPanel extends JPanel {

    private final String year;
    private final String month;
    private final String day;

    private final Map<String, DayPanel> map = new HashMap<>();

    public DayPanel(String yearIn, String monthIn, String dayIn) {
        super();

        this.year = yearIn;
        this.month = monthIn;
        this.day = dayIn;
        setSize(1050, 600);
    }

}
