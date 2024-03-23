/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.nanoboot.utils.timecalc.swing.progress.weather;

import lombok.Getter;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

/**
 *
 * @author robertvokac
 */
public enum Cloudiness {
    CLOUDY("Cloudy", 7d/8d),
    MOSTLY_CLOUDY("Mostly cloudy", 5d/8d),
    PARTLY_CLOUDY("Partly cloudy and partly sunny", 3d/8d),
    MOSTLY_SUNNY("Mostly clear and mostly sunny", 1d/8d),
    SUNNY("Clear/Sunny", 0/8);
    @Getter
    private String description;
    private double ifMoreOrEqual;
    Cloudiness(String description, double ifMoreOrEqual) {
        this.description = description;
        this.ifMoreOrEqual = ifMoreOrEqual;
    }
    public static Cloudiness forValue(double value) {
        for (Cloudiness c : Cloudiness.values()) {
            if (value >= c.ifMoreOrEqual) {
                return c;
            }
        }
        throw new IllegalStateException("Unsupported value: " + NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(value));
    }
    
//    Cloudy: 90-100% sky covered
//    Mostly cloudy: 70-80% sky covered
//    Partly Cloudy/Partly Sunny: 30-60% sky covered
//    Mostly Clear/Mostly Sunny: 10-30% sky covered
//    Clear/Sunny: 0-10% sky covered

    
}
