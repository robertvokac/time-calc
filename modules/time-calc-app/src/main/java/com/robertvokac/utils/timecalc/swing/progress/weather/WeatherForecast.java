package com.robertvokac.utils.timecalc.swing.progress.weather;

import lombok.Getter;
import lombok.Setter;
import com.robertvokac.utils.timecalc.utils.common.FileConstants;
import com.robertvokac.utils.timecalc.utils.common.NumberFormats;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.ToString;
import com.robertvokac.utils.timecalc.utils.common.DateFormats;

/**
 * @author pc00289
 * @since 22.03.2024
 */
@Getter
@Setter
@ToString
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

    public static final String DOUBLE_COLON = "::";

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
    private double humidity;
    private double heatOrColdWave;
    private double startCelsius;
    private double endCelsius;

    public static List<WeatherForecast> createForecast(int year) {
        List<WeatherForecast> list = new ArrayList<>();

        Properties climateProperties = new Properties();
        if (FileConstants.CLIMATE_TXT.exists()) {
            try {
                climateProperties.load(new FileInputStream(FileConstants.CLIMATE_TXT));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Loading file failed: " + FileConstants.WEATHER_CSV + " " + e.getMessage());
                //return list;
            }
        } else {
            System.out.println("File does not exist: " + FileConstants.CLIMATE_TXT);
            //return list;
        }

        double min1 = Double.valueOf(climateProperties.getProperty("1.min", "-20")) + 10;
        double min2 = Double.valueOf(climateProperties.getProperty("2.min", "-16")) + 10;
        double min3 = Double.valueOf(climateProperties.getProperty("3.min", "-12")) + 10;
        double min4 = Double.valueOf(climateProperties.getProperty("4.min", "-8")) + 10;
        double min5 = Double.valueOf(climateProperties.getProperty("5.min", "-4")) + 10;
        double min6 = Double.valueOf(climateProperties.getProperty("6.min", "-2")) + 10;
        double min7 = Double.valueOf(climateProperties.getProperty("7.min", "0")) + 10;
        double min8 = Double.valueOf(climateProperties.getProperty("8.min", "0")) + 10;
        double min9 = Double.valueOf(climateProperties.getProperty("9.min", "-2")) + 10;
        double min10 = Double.valueOf(climateProperties.getProperty("10.min", "-4")) + 10;
        double min11 = Double.valueOf(climateProperties.getProperty("11.min", "-8")) + 10;
        double min12 = Double.valueOf(climateProperties.getProperty("12.min", "-16")) + 10;

        double max1 = Double.valueOf(climateProperties.getProperty("1.max", "20"));
        double max2 = Double.valueOf(climateProperties.getProperty("2.max", "15"));
        double max3 = Double.valueOf(climateProperties.getProperty("3.max", "20"));
        double max4 = Double.valueOf(climateProperties.getProperty("4.max", "25"));
        double max5 = Double.valueOf(climateProperties.getProperty("5.max", "30"));
        double max6 = Double.valueOf(climateProperties.getProperty("6.max", "35"));
        double max7 = Double.valueOf(climateProperties.getProperty("7.max", "40"));
        double max8 = Double.valueOf(climateProperties.getProperty("8.max", "40"));
        double max9 = Double.valueOf(climateProperties.getProperty("9.max", "35"));
        double max10 = Double.valueOf(climateProperties.getProperty("10.max", "30"));
        double max11 = Double.valueOf(climateProperties.getProperty("11.max", "25"));
        double max12 = Double.valueOf(climateProperties.getProperty("12.max", "20"));

        Map<Integer, Map<Integer, Double>> minExpected = new HashMap<>();
        Map<Integer, Map<Integer, Double>> maxExpected = new HashMap<>();
        Calendar cal_ = Calendar.getInstance();
        cal_.set(Calendar.YEAR, 2021);
        cal_.set(Calendar.MONTH, 0);
        cal_.set(Calendar.DAY_OF_MONTH, 1);

        while (cal_.get(Calendar.YEAR) == 2021) {
            int month = cal_.get(Calendar.MONTH) + 1;
            int dayOfMonth = cal_.get(Calendar.DAY_OF_MONTH);
            double min = 0d;
            double max = 0d;

            double minStart = 0d;
            double minEnd = 0d;
            double maxStart = 0d;
            double maxEnd = 0d;
            double transition = 0d;
            LocalDate today = LocalDate.of(2021, Month.of(month), dayOfMonth);

            LocalDate startDay = null;
            LocalDate endDay = null;

            if ((month == 12 && dayOfMonth >= 16) || (month == 1 && dayOfMonth <= 15)) {
                minStart = min12;
                minEnd = min1;
                maxStart = max12;
                maxEnd = max1;
                startDay = LocalDate.of(2020, Month.DECEMBER, 16);
                endDay = LocalDate.of(2021, Month.JANUARY, 15);

            }
            if ((month == 1 && dayOfMonth >= 16) || (month == 2 && dayOfMonth <= 14)) {
                minStart = min1;
                minEnd = min2;
                maxStart = max1;
                maxEnd = max2;
                startDay = LocalDate.of(2021, Month.of(1), 16);
                endDay = LocalDate.of(2021, Month.of(2), 14);
            }
            if ((month == 2 && dayOfMonth >= 15) || (month == 3 && dayOfMonth <= 15)) {
                minStart = min2;
                minEnd = min3;
                maxStart = max2;
                maxEnd = max3;
                startDay = LocalDate.of(2021, Month.of(2), 15);
                endDay = LocalDate.of(2021, Month.of(3), 16);
            }
            if ((month == 3 && dayOfMonth >= 16) || (month == 4 && dayOfMonth <= 15)) {
                minStart = min3;
                minEnd = min4;
                maxStart = max3;
                maxEnd = max4;
                startDay = LocalDate.of(2021, Month.of(3), 16);
                endDay = LocalDate.of(2021, Month.of(4), 15);
            }
            if ((month == 4 && dayOfMonth >= 16) || (month == 5 && dayOfMonth <= 15)) {
                minStart = min4;
                minEnd = min5;
                maxStart = max4;
                maxEnd = max5;
                startDay = LocalDate.of(2021, Month.of(4), 16);
                endDay = LocalDate.of(2021, Month.of(5), 15);
            }
            if ((month == 5 && dayOfMonth >= 16) || (month == 6 && dayOfMonth <= 15)) {
                minStart = min5;
                minEnd = min6;
                maxStart = max5;
                maxEnd = max6;
                startDay = LocalDate.of(2021, Month.of(5), 16);
                endDay = LocalDate.of(2021, Month.of(6), 15);
            }
            if ((month == 6 && dayOfMonth >= 16) || (month == 7 && dayOfMonth <= 15)) {
                minStart = min6;
                minEnd = min7;
                maxStart = max6;
                maxEnd = max7;
                startDay = LocalDate.of(2021, Month.of(6), 16);
                endDay = LocalDate.of(2021, Month.of(7), 15);
            }
            if ((month == 7 && dayOfMonth >= 16) || (month == 8 && dayOfMonth <= 15)) {
                minStart = min7;
                minEnd = min8;
                maxStart = max7;
                maxEnd = max8;
                startDay = LocalDate.of(2021, Month.of(7), 16);
                endDay = LocalDate.of(2021, Month.of(8), 15);
            }
            if ((month == 8 && dayOfMonth >= 16) || (month == 9 && dayOfMonth <= 15)) {
                minStart = min8;
                minEnd = min9;
                maxStart = max8;
                maxEnd = max9;
                startDay = LocalDate.of(2021, Month.of(8), 16);
                endDay = LocalDate.of(2021, Month.of(9), 15);
            }
            if ((month == 9 && dayOfMonth >= 16) || (month == 10 && dayOfMonth <= 15)) {
                minStart = min9;
                minEnd = min10;
                maxStart = max9;
                maxEnd = max10;
                startDay = LocalDate.of(2021, Month.of(9), 16);
                endDay = LocalDate.of(2021, Month.of(10), 15);
            }
            if ((month == 10 && dayOfMonth >= 16) || (month == 11 && dayOfMonth <= 15)) {
                minStart = min10;
                minEnd = min11;
                maxStart = max10;
                maxEnd = max11;
                startDay = LocalDate.of(2021, Month.of(10), 16);
                endDay = LocalDate.of(2021, Month.of(11), 15);
            }
            if ((month == 11 && dayOfMonth >= 16) || (month == 12 && dayOfMonth <= 15)) {
                minStart = min11;
                minEnd = min12;
                maxStart = max11;
                maxEnd = max12;
                startDay = LocalDate.of(2021, Month.of(11), 16);
                endDay = LocalDate.of(2021, Month.of(12), 15);
            }

            double daysBetween = ChronoUnit.DAYS.between(startDay, endDay) + 1;
            double daysDone = ChronoUnit.DAYS.between(startDay, today) + 1;
            if (daysDone > 365) {
                daysDone = daysDone - 365;
            }
            transition = daysDone / daysBetween;
            if (!minExpected.containsKey(month)) {
                minExpected.put(month, new HashMap<Integer, Double>());
            }
            if (!maxExpected.containsKey(month)) {
                maxExpected.put(month, new HashMap<Integer, Double>());
            }
            double minDiff = Math.abs(minEnd - minStart);
            double maxDiff = Math.abs(maxEnd - maxStart);

            double minAdd = (minDiff * transition * (minStart < maxEnd ? 1 : (-1)));
            double maxAdd = (maxDiff * transition * (maxStart < maxEnd ? 1 : (-1)));
            min = minStart + minAdd;
            max = maxStart + maxAdd;

//            System.out.print("\n" + month + "-" + dayOfMonth + ": ");
//            System.out.print(" minStart= " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(minStart));
//            System.out.print(" minEnd= " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(minEnd));
//            System.out.print(" maxStart= " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(maxStart));
//            System.out.print(" maxEnd= " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(maxEnd));
//            System.out.println(" min and max: " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(min) + " " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(max));
//            System.out.println(" min and max diff: " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(minDiff) + " " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(maxDiff));
//            System.out.println("transition=" + transition);
//            System.out.println("daysDone=" + daysDone + " daysBetween=" + daysBetween);
//            System.out.println("startDay=" + startDay.toString());
//            System.out.println("endDay=" + endDay.toString());
            minExpected.get(month).put(dayOfMonth, min);
            maxExpected.get(month).put(dayOfMonth, max);

            cal_.add(Calendar.DAY_OF_MONTH, 1);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        double humidity = Math.random() * 0.6;
        double heatOrColdWave = Math.random();
        int tooMuchHumidityDays = 0;
        int tooMuchHeatDays = 0;
        int tooMuchColdDays = 0;
        double windiness = Math.random();
        List<Double> maxCLastWeekAverage = new ArrayList<Double>();
        List<Double> minCLastWeekAverage = new ArrayList<Double>();
        double endCelsius = Double.MIN_VALUE;
        while (cal.get(Calendar.YEAR) == year) {
            if (humidity > 0.7) {
                ++tooMuchHumidityDays;
            }
            if (heatOrColdWave > 0.8) {
                ++tooMuchHeatDays;
            }
            if (heatOrColdWave < 0.3) {
                ++tooMuchColdDays;
            }
            if (tooMuchHumidityDays > 5) {
                if (Math.random() > 0.5) {
                    humidity = Math.random() * 0.15;
                }
            }
            if (tooMuchHeatDays > 5) {
                if (Math.random() > 0.5) {
                    heatOrColdWave = Math.random() * 0.1;
                }
            }
            if (tooMuchColdDays > 5) {
                if (Math.random() > 0.5) {
                    heatOrColdWave = Math.random() * 0.9;
                }
            }
            if (Math.random() > 0.75) {
                humidity = humidity + Math.random() * 0.3 * (Math.random() > 0.9 ? (-1) : 1);
                if (humidity > 1.0) {
                    humidity = 1.0;
                }
                if (humidity < 0.0) {
                    humidity = 0.0;
                }
            }
            if (Math.random() > 0.95) {
                humidity = 0.9 + Math.random() * 0.1;
            }
            if (Math.random() > 0.75) {
                heatOrColdWave = heatOrColdWave + Math.random() * 0.1 * (Math.random() > 0.9 ? (-1) : 1);
                if (heatOrColdWave > 1.0) {
                    heatOrColdWave = 1.0;
                }
                if (heatOrColdWave < 0.0) {
                    heatOrColdWave = 0.0;
                }
                if (heatOrColdWave > 0.85 && Math.random() > 0.25) {
                    heatOrColdWave = 0.85;
                }
                if (heatOrColdWave < 0.15 && Math.random() > 0.25) {
                    heatOrColdWave = 0.15;
                }
            }

            int month = cal.get(Calendar.MONTH) + 1;
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            WeatherForecast forecast = new WeatherForecast();
            list.add(forecast);
            forecast.setDate(DateFormats.DATE_TIME_FORMATTER_YYYYMMDD.format(cal.getTime()));
            forecast.setMaximumCelsius(maxExpected.get(month).get((month == 2 && dayOfMonth == 29) ? 28 : dayOfMonth));
            forecast.setMinimumCelsius(minExpected.get(month).get((month == 2 && dayOfMonth == 29) ? 28 : dayOfMonth));
            double maxE = forecast.getMaximumCelsius();
            double minE = forecast.getMinimumCelsius();
            double maxExpectedCelsius = forecast.getMaximumCelsius();
            double minExpectedCelsius = forecast.getMinimumCelsius();

            if (Math.random() > 0.4) {
                if (minE < -10) {
                    minE = minE + 10 + 10 * Math.random();
                }
                if (maxE > 40) {
                    maxE = maxE - 10 - 10 * Math.random();
                }
            }
            if (minE < -10 && Math.random() > 0.5) {
                heatOrColdWave = 0.7 + Math.random() * 0.2;
            }
            if (maxE > 38 && Math.random() > 0.5) {
                heatOrColdWave = 0.1 + Math.random() * 0.1;
            }
            if (maxExpectedCelsius - 10 > maxCLastWeekAverage.stream().mapToDouble(e -> Double.valueOf(e)).average().orElse(maxExpectedCelsius - 10)) {
                heatOrColdWave = heatOrColdWave + ((1.0d - heatOrColdWave) / 2d);
            } else {
                heatOrColdWave = heatOrColdWave / 2d;
            }
            if (minExpectedCelsius + 10 > minCLastWeekAverage.stream().mapToDouble(e -> Double.valueOf(e)).average().orElse(minExpectedCelsius + 10)) {
                heatOrColdWave = heatOrColdWave + ((1.0d - heatOrColdWave) / 2d);
            } else {
                heatOrColdWave = heatOrColdWave / 2d;
            }
            double mediumHalf = minE + Math.abs(maxE - minE) / 2;
            forecast.setMaximumCelsius(mediumHalf + heatOrColdWave * 1.1 * mediumHalf);
            forecast.setMinimumCelsius(minE + heatOrColdWave * 1.5 * mediumHalf);
            windiness = Math.random();
            if (Math.random() > 0.9 && windiness < 0.15) {
                windiness = 0d;
            }
            forecast.setWind(windiness * (Math.random() > 0.95 ? 40 : Math.random() * 150));
            forecast.setCloudiness(Math.random() > 0.3 ? humidity : Math.random());
            if (forecast.getMaximumCelsius() > 25 && Math.random() > 0.9) {
                forecast.setThunder(Math.random());
            }
            if (humidity > 0.2) {
                if (forecast.getMinimumCelsius() > 3) {
                    forecast.setRain(humidity * Math.random() * 50 * (Math.random() > 0.90 ? Math.random() * 10 : 1));
                }
                if (forecast.getMinimumCelsius() < 0) {
                    forecast.setSnow(humidity * Math.random() * 50 * (Math.random() > 0.90 ? Math.random() * 10 : 1));
                }
                if (forecast.getMinimumCelsius() > 0 && forecast.getMinimumCelsius() < 3) {
                    if (Math.random() > 0.5) {
                        forecast.setRain(humidity * Math.random() * 50 * (Math.random() > 0.90 ? Math.random() * 10 : 1));
                    } else {
                        forecast.setSnow(humidity * Math.random() * 50 * (Math.random() > 0.90 ? Math.random() * 10 : 1));
                    }
                }
            }
            forecast.setHumidity(humidity);
            forecast.setHeatOrColdWave(heatOrColdWave);
            forecast.setMaximumCelsiusTime(12.0 + Math.random() * 5);
            forecast.setMinimumCelsiusTime(02.0 + Math.random() * 4);

            maxCLastWeekAverage.add(forecast.getMaximumCelsius());
            minCLastWeekAverage.add(forecast.getMinimumCelsius());
            while (maxCLastWeekAverage.size() > 7) {
                maxCLastWeekAverage.remove(0);
            }
            while (minCLastWeekAverage.size() > 7) {
                minCLastWeekAverage.remove(0);
            }
            if(endCelsius == Double.MIN_VALUE) {
                forecast.setStartCelsius(forecast.getMinimumCelsius() + Math.random() * 8);
            } else {
                forecast.setStartCelsius(endCelsius);
            }
            forecast.setEndCelsius(forecast.getMinimumCelsius() + Math.random() * 5);
            endCelsius = forecast.getEndCelsius();

            //    private String date;
//    private double minimumCelsius;
//    private double maximumCelsius;
//    private double minimumCelsiusTime;
//    private double maximumCelsiusTime;
//    private double wind;
//    private double cloudiness;
//    private double thunder;
//    private double rain;
//    private double snow;
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append(date).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(minimumCelsius)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(maximumCelsius)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(minimumCelsiusTime)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(maximumCelsiusTime)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wind)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(cloudiness)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(thunder)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(rain)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(snow)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(humidity)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(heatOrColdWave)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(startCelsius)).append('\t');
        sb.append(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(endCelsius));
        return sb.toString().replace(",", ".");
    }

    public WeatherForecast fromCsv(String csv) {
        String[] values = csv.split("\t");

        int i = 0;
        date = values[i++];
        minimumCelsius = Double.valueOf(values[i++]);
        maximumCelsius = Double.valueOf(values[i++]);
        minimumCelsiusTime = Double.valueOf(values[i++]);
        maximumCelsiusTime = Double.valueOf(values[i++]);
        wind = Double.valueOf(values[i++]);
        cloudiness = Double.valueOf(values[i++]);
        thunder = Double.valueOf(values[i++]);
        rain = Double.valueOf(values[i++]);
        snow = Double.valueOf(values[i++]);
        humidity = Double.valueOf(values[i++]);
        heatOrColdWave = Double.valueOf(values[i++]);
        startCelsius = Double.valueOf(values[i++]);
        endCelsius = Double.valueOf(values[i++]);

        return this;
    }

    public String asPrettyString() {
        return asPrettyString(-1);
    }

    public String asPrettyString(double forHour) {
        StringBuilder sb = new StringBuilder();
        sb.append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(maximumCelsius)).append("°C/").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(minimumCelsius)).append("°C").append(DOUBLE_COLON);
        if (forHour >= 0d) {
            double value = getCelsiusForHour(forHour);
            sb.append("Now: " + NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(value)).append("°C").append(DOUBLE_COLON);
        }
        //
        sb
                .append((int) Math.floor(this.wind))
                .append("m/s");
        if (snow == 0d) {
            sb
                    .append("🌧")
                    .append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(rain))
                    .append("mm").append(DOUBLE_COLON);
        } else {
            sb
                    .append("❄")
                    .append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(snow))
                    .append("mm").append(DOUBLE_COLON);
        }
        sb
                .append(Cloudiness.forValue(cloudiness).getDescription().toLowerCase())
                .append(DOUBLE_COLON);
        return sb.toString();
//        return "14°C::wind 7/s::rain 2mm::cloudy";
    }
    
    public Double getCelsiusForHour(double forHour) {
        if(forHour > 24) {
            throw new UnsupportedOperationException("Hour must less than 24: " + NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(forHour));
        }
        double minTime = this.minimumCelsiusTime;
        double maxTime = this.maximumCelsiusTime;
        if (maxTime < minTime) {
            minTime = 4d;
            maxTime = 14d;
        }
        double endTime = 24d;
        double value = 0d;
        if (forHour < minTime) {
            return this.startCelsius - ((this.startCelsius - this.minimumCelsius) * forHour / minTime);
        }
        if (forHour >= minTime && forHour <= maxTime) {
            return minimumCelsius + ((forHour - minTime) / (maxTime - minTime)) * (maximumCelsius - minimumCelsius);
        }
        if (forHour > maxTime) {
            return maximumCelsius - (((forHour - maxTime) / (endTime - maxTime)) * (maximumCelsius - this.endCelsius));
        }
        throw new IllegalStateException();
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
10.max=30.3
11.max=24.0
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
