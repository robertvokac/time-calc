package org.nanoboot.utils.timecalc.swing.progress;

import lombok.Getter;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author pc00289
 * @since 22.03.2024
 */
@Getter
public class WeatherForecast {
//01:00-4:00 - minimum
//13:00-16:00 - maximum
//20240101.minTempC=
//20240101.maxTempC=
// 20240101.minTempTime=
//20240101.maxTempTime=
//20240101.wind={m/s}
//20240101.cloudiness={0.0 - 1.0}
//20240101.thunder={0.0 - 1.0}
//20240101.rain={mm}
//20240101.snow={mm}
//#20240101.sunrise=7:53
//#20240101.sunset=16:02
    private String date;
    private double minimumCelsius;
    private double maximumCelsius;
    private double minimumCelsiusTime;
    private double maximumCelsiusTime;
    private double wind;
    private double cloudiness;
    private double thunder;
    private double rain;
    private double snow;
    public static List<WeatherForecast> createForecast() {
        List<WeatherForecast> list = new ArrayList<>();

        Properties climateProperties = new Properties();
        if(FileConstants.CLIMATE_TXT.exists()) {
            try {
                climateProperties.load(new FileInputStream(FileConstants.CLIMATE_TXT));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Loading file failed: " + FileConstants.WEATHER_TXT + " " + e.getMessage());
                return list;
            }
        } else {
            System.out.println("File does not exist: " + FileConstants.CLIMATE_TXT);
            return list;
        }

        double min1= Double.valueOf(climateProperties.getProperty("1.min", "0"));
        double min2= Double.valueOf(climateProperties.getProperty("2.min", "1"));
        double min3= Double.valueOf(climateProperties.getProperty("3.min", "2"));
        double min4= Double.valueOf(climateProperties.getProperty("4.min", "4"));
        double min5= Double.valueOf(climateProperties.getProperty("5.min", "6"));
        double min6= Double.valueOf(climateProperties.getProperty("6.min", "8"));
        double min7= Double.valueOf(climateProperties.getProperty("7.min", "8"));
        double min8= Double.valueOf(climateProperties.getProperty("8.min", "6"));
        double min9= Double.valueOf(climateProperties.getProperty("9.min", "4"));
        double min10= Double.valueOf(climateProperties.getProperty("10.min", "2"));
        double min11= Double.valueOf(climateProperties.getProperty("11.min", "1"));
        double min12= Double.valueOf(climateProperties.getProperty("12.min", "0"));

        return list;
    }
    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(minimumCelsius)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(maximumCelsius)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(minimumCelsiusTime)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(maximumCelsiusTime)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wind)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(cloudiness)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(thunder)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(rain)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(snow));
        return sb.toString();
    }
    public WeatherForecast fromCsv(String csv) {
        String[] values = csv.split("\t");

        int i = 0;
        minimumCelsius = Double.valueOf(values[i++]);
        maximumCelsius = Double.valueOf(values[i++]);
        minimumCelsiusTime = Double.valueOf(values[i++]);
        maximumCelsiusTime = Double.valueOf(values[i++]);
        wind = Double.valueOf(values[i++]);
        cloudiness = Double.valueOf(values[i++]);
        thunder = Double.valueOf(values[i++]);
        rain = Double.valueOf(values[i++]);
        snow = Double.valueOf(values[i++]);

        return this;
    }
    public String asPrettyString(double forHour) {
        return "14°C, wind 7/s, rain 2mm, cloudy";
    }
}

/*
climate.txt
1.max=18.8
2.max=22.0
3.max=26.2
4.max=31.8
5.max=35.0
6.max=38.9
7.max=40.2
8.max=40.4
9.max=37.4
10.max=30,3
11.max=24,0
12.max=19.8
1.min=-36.2
2.min=-42.2
3.min=-32.0
4.min=-22.0
5.min=-13.1
6.min=-8.3
7.min=-6.9
8.min=-5.0
9.min=-10.5
10.min=-19.9
11.min=-25.4
12.min=-34.0
 */
