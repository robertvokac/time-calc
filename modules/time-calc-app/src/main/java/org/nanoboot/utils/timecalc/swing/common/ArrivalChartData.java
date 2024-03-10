package org.nanoboot.utils.timecalc.swing.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Robert
 * @since 11.03.2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalChartData {
    private String[] days;
    private double[] arrival;
    private double target;
    private double[] ma7;
    private double[] ma14;
    private double[] ma28;
    private double[] ma56;
    private String startDate;
    private String endDate;
    private boolean arrivalEnabled = true, ma7Enabled = true, ma14Enabled = true, ma28Enabled = true, ma56Enabled = true;
}
