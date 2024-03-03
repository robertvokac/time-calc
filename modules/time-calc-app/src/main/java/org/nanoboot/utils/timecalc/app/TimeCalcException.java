package org.nanoboot.utils.timecalc.app;

import java.io.IOException;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TimeCalcException extends RuntimeException {

    public TimeCalcException(String msg) {
        super(msg);
    }

    public TimeCalcException(Exception e) {
        super(e);
    }
}
