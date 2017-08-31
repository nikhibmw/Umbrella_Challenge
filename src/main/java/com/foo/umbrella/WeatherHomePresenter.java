package com.foo.umbrella;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.api.WeatherService;
import com.foo.umbrella.data.model.WeatherData;

import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherHomePresenter {


    public interface ViewContract {

        void renderFailure(String error);

        void renderWeatherData(WeatherData weatherData);
    }


    private ViewContract viewContract;

    private WeatherService weatherService;

    private Activity activity;


    public WeatherHomePresenter(Activity activity, ViewContract contract) {
        this.activity = activity;
        setViewContract(contract);

    }



    public void pullWeather(@NonNull String zipCode) {

        getWeatherService()
                .forecastForZipObservable(zipCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<Result<WeatherData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<WeatherData> weatherDataResult) {
                WeatherData weatherData = weatherDataResult.response().body();
                getViewContract().renderWeatherData(weatherData);
            }
        });
    }
    public ViewContract getViewContract() {
        if (viewContract == null) {
            this.viewContract = new ViewContract() {

                @Override
                public void renderFailure(String error) {

                }

                @Override
                public void renderWeatherData(WeatherData weatherData) {

                }
            };
        }
        return viewContract;
    }

    private WeatherService getWeatherService() {
        if (this.weatherService == null) {
            ApiServicesProvider apiServicesProvider = new ApiServicesProvider(this.activity.getApplication());
            weatherService = apiServicesProvider.getWeatherService();
        }
        return this.weatherService;
    }

    public void setViewContract(ViewContract viewContract) {
        this.viewContract = viewContract;
    }
}
