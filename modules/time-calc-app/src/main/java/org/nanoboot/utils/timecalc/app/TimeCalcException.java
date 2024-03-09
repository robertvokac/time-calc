package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.utils.common.Utils;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TimeCalcException extends RuntimeException {

    public TimeCalcException(String msg) {
        super(msg);
        Utils.showNotification("Error happened: " + msg);
    }

    public TimeCalcException(Exception e) {
        super(e);
        Utils.showNotification("Error happened: " + e.getMessage());
    }
}
