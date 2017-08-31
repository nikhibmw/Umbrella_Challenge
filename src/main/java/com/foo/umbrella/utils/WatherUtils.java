package com.foo.umbrella.utils;


import android.support.annotation.NonNull;

import com.foo.umbrella.R;
import com.foo.umbrella.data.WeatherUnit;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;

import org.threeten.bp.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WatherUtils {


    public static List<ForecastCondition> getTodaysForecast(@NonNull WeatherData weatherData) {
        ArrayList<ForecastCondition> forecastConditions = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int todayDayOfYear = LocalDateTime.now().getDayOfYear();
        List<ForecastCondition> weatherDataForecast = weatherData.getForecast();
        if (weatherDataForecast != null && weatherDataForecast.size() > 0) {
            forecastConditions = new ArrayList<>();
            for (int i = 0; i < weatherDataForecast.size(); i++) {
                ForecastCondition forecastCondition = weatherDataForecast.get(i);
                LocalDateTime dateTime = forecastCondition.getDateTime();
                if (todayDayOfYear == dateTime.getDayOfYear()) {
                    forecastConditions.add(forecastCondition);
                }
            }
        }
        return forecastConditions;
    }


    public static List<ForecastCondition> getTommorowForecast(@NonNull WeatherData weatherData) {
        ArrayList<ForecastCondition> forecastConditions = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int todayDayOfYear = LocalDateTime.now().getDayOfYear();
        List<ForecastCondition> weatherDataForecast = weatherData.getForecast();
        if (weatherDataForecast != null && weatherDataForecast.size() > 0) {
            forecastConditions = new ArrayList<>();
            for (int i = 0; i < weatherDataForecast.size(); i++) {
                ForecastCondition forecastCondition = weatherDataForecast.get(i);
                LocalDateTime dateTime = forecastCondition.getDateTime();
                if (todayDayOfYear + 1 == dateTime.getDayOfYear()) {
                    forecastConditions.add(forecastCondition);
                }
            }
        }
        return forecastConditions;
    }


    public static int[] getMinMaxIndices(@NonNull List<ForecastCondition> forecastConditions, WeatherUnit weatherUnit) {
        int min = -1, max = -1;
        Comparator<ForecastCondition> comparator = new Comparator<ForecastCondition>() {
            @Override
            public int compare(ForecastCondition one, ForecastCondition two) {
                Double oneValue, twoValue;
                if (weatherUnit == WeatherUnit.CELSIUS) {
                    oneValue = Double.parseDouble(one.getTempCelsius());
                    twoValue = Double.parseDouble(two.getTempCelsius());
                } else {
                    oneValue = Double.parseDouble(one.getTempFahrenheit());
                    twoValue = Double.parseDouble(two.getTempFahrenheit());
                }
                if (oneValue == twoValue) return 0;
                else if (oneValue > twoValue) return 1;
                else return -1;
            }

        };
        if (forecastConditions.size() > 0) {
            ForecastCondition minValue = Collections.min(forecastConditions, comparator);
            ForecastCondition maxValue = Collections.max(forecastConditions, comparator);

            min = forecastConditions.indexOf(minValue);
            max = forecastConditions.indexOf(maxValue);
        }
        return new int[]{min, max};
    }


    public static int getWeatherColor(@NonNull ForecastCondition forecastCondition, WeatherUnit weatherUnit) {
        if (weatherUnit == WeatherUnit.FAHRENHEIT) {
            return getWeatherColor(forecastCondition.getTempFahrenheit(), weatherUnit);
        }
        return getWeatherColor(forecastCondition.getTempCelsius(), weatherUnit);
    }

    public static int getWeatherColor(@NonNull String temperature, WeatherUnit weatherUnit) {
        int colorCode = R.color.weather_cool;
        double doubleTemp = Double.parseDouble(temperature);

        if ((weatherUnit == WeatherUnit.FAHRENHEIT && doubleTemp > 60) || (weatherUnit == WeatherUnit.CELSIUS && doubleTemp > 16)) {
            colorCode = R.color.weather_warm;
        }

        return colorCode;
    }

    public static String getTemperatureFormatted(@NonNull String temperature, WeatherUnit weatherUnit) {
        if (weatherUnit == WeatherUnit.FAHRENHEIT) {
            return temperature + " \u2109";
        } else {
            return temperature + " \u2103";
        }
    }

    public static String getTemperatureFormatted(@NonNull ForecastCondition forecastCondition, WeatherUnit weatherUnit) {

        if (weatherUnit == WeatherUnit.FAHRENHEIT) {
            return getTemperatureFormatted(forecastCondition.getTempFahrenheit(), weatherUnit);
        }
        return getTemperatureFormatted(forecastCondition.getTempCelsius(), weatherUnit);
    }

}
