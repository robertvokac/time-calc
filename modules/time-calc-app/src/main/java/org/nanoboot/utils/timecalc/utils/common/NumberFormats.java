package org.nanoboot.utils.timecalc.utils.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class NumberFormats {
    private NumberFormats() {
        //Not meant to be instantiated.
    }
    public static final NumberFormat FORMATTER_ZERO_DECIMAL_PLACES = new DecimalFormat("#00");
    public static final NumberFormat FORMATTER_TWO_DECIMAL_PLACES = new DecimalFormat("#00.00");
    public static final NumberFormat FORMATTER_FIVE_DECIMAL_PLACES = new DecimalFormat("#0.00000");
    public static final NumberFormat FORMATTER_THREE_DECIMAL_PLACES = new DecimalFormat("#0.000");
}
