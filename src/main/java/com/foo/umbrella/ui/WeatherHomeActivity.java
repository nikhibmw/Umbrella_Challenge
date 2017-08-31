package com.foo.umbrella.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.foo.umbrella.R;
import com.foo.umbrella.WeatherHomePresenter;
import com.foo.umbrella.customviews.WeatherItem;
import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.WeatherUnit;
import com.foo.umbrella.data.api.WeatherService;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;
import com.foo.umbrella.databinding.ActivityWeatherBinding;
import com.foo.umbrella.utils.WatherUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherHomeActivity extends AppCompatActivity implements WeatherHomePresenter.ViewContract {

    String zipCode = "30005";
    WeatherUnit weatherUnit = WeatherUnit.CELSIUS;

    private static final int NO_ITEMS_PER_ROW = 4;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ActivityWeatherBinding viewBinding;
    private ArrayList<String> todaysWeather, tommorowWeather;
    private WeatherHomePresenter weatherHomePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view binding.
        this.viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather);

        //Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        this.zipCode = sharedPreferences.getString(getString(R.string.pref_header_zip), zipCode);
        String units = sharedPreferences.getString(getString(R.string.pref_header_Units), getString(R.string.celsius));
        if (units.equalsIgnoreCase(getString(R.string.celsius))) {
            this.weatherUnit = WeatherUnit.CELSIUS;
        } else {
            this.weatherUnit = WeatherUnit.FAHRENHEIT;
        }
        initPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.weatherHomePresenter.pullWeather(this.zipCode);
    }

    private void initPresenter(){
        this.weatherHomePresenter = new WeatherHomePresenter(this, this);
    }


    @Override
    public void renderFailure(String error) {

    }

    @Override
    public void renderWeatherData(@NonNull WeatherData weatherData) {
        String currentTemperature = getCurrentTemperature(weatherData);
        int weatherColor = WatherUtils.getWeatherColor(currentTemperature, weatherUnit);
        String location = weatherData.getCurrentObservation().getDisplayLocation().getFullName();
        this.viewBinding.toolbar.setTitle(location);
        this.viewBinding.toolbar.setBackgroundColor(weatherColor);
        this.viewBinding.rlWeatherHeader.setBackgroundColor(weatherColor);
        this.viewBinding.txtviewTemperature.setText(WatherUtils.getTemperatureFormatted(currentTemperature, weatherUnit));

        String weatherDescription = weatherData.getCurrentObservation().getWeatherDescription();
        this.viewBinding.txtviewWeatherStatus.setText(weatherDescription);

        List<ForecastCondition> todaysForecast = WatherUtils.getTodaysForecast(weatherData);
        List<ForecastCondition> tommorowForecast = WatherUtils.getTommorowForecast(weatherData);


        int[] todayMinMaxIndices = WatherUtils.getMinMaxIndices(todaysForecast, weatherUnit);
        int[] tomorrowMinMaxIndices = WatherUtils.getMinMaxIndices(tommorowForecast, weatherUnit);

        this.renderWeatherItems(todaysForecast, todayMinMaxIndices, this.viewBinding.llTodaysWeather);
        this.renderWeatherItems(tommorowForecast, tomorrowMinMaxIndices, this.viewBinding.llTomorrowWeather);
    }


    private void renderWeatherItems(@NonNull List<ForecastCondition> todaysForecast, int[] minmax, @NonNull ViewGroup layout) {
        int itemCount = todaysForecast.size();
        List<ForecastCondition> tempRows = new ArrayList<>(NO_ITEMS_PER_ROW);

        if (itemCount > 0) {
            if (layout.getChildCount() > 0) {
                layout.removeAllViews();
            }

            for (int i = 0; i < todaysForecast.size(); i++) {
                WeatherItem item = null;
                ForecastCondition forecastCondition = todaysForecast.get(i);

                if (i == minmax[0]) {
                    item = new WeatherItem(this, R.color.weather_cool);
                } else if (i == minmax[1]) {
                    item = new WeatherItem(this, R.color.weather_warm);
                } else {
                    item = new WeatherItem(this, 0);
                }

                ImageView weatherIcon = item.getWeatherIcon();
                if (forecastCondition.getCondition().equalsIgnoreCase("clear")) {
                    weatherIcon.setImageResource(R.drawable.ic_wb_sunny_24dp);
                } else if (forecastCondition.getCondition().equalsIgnoreCase("partly cloudy")) {
                    weatherIcon.setImageResource(R.drawable.ic_cloud_24dp);
                } else {
                    weatherIcon.setImageResource(R.drawable.ic_wb_sunny_24dp);
                }

                item.getTxtWeather().setText(WatherUtils.getTemperatureFormatted(forecastCondition, weatherUnit));
                item.getTxtTime().setText(forecastCondition.getDisplayTime());

                layout.addView(item);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentTemperature(@NonNull WeatherData weatherData) {
        if (weatherUnit == WeatherUnit.CELSIUS) {
            return weatherData.getCurrentObservation().getTempCelsius();
        }
        return weatherData.getCurrentObservation().getTempFahrenheit();
    }


}
