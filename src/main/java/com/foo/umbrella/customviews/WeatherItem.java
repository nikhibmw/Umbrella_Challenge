package com.foo.umbrella.customviews;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foo.umbrella.R;

public class WeatherItem extends LinearLayout {

    private ImageView weatherIcon;
    private TextView txtTime;
    private TextView txtWeather;
    private int imageResID = 0;

    public WeatherItem(Context context) {
        super(context);
        init();
    }

    public WeatherItem(Context context, int resid) {
        super(context);
        this.imageResID = resid;
        init();
    }

    public WeatherItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeatherItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_weather, this, true);
        this.weatherIcon = (ImageView) this.findViewById(R.id.img_view_weather_icon);
        this.txtTime = (TextView) this.findViewById(R.id.txt_view_datetime);
        this.txtWeather = (TextView) this.findViewById(R.id.txt_view_weather_temp);
        if (imageResID != 0) {
            DrawableCompat.setTint(weatherIcon.getDrawable(), ContextCompat.getColor(getContext(), imageResID));
        }
    }


    public ImageView getWeatherIcon() {
        return weatherIcon;
    }

    public TextView getTxtTime() {
        return txtTime;
    }

    public TextView getTxtWeather() {
        return txtWeather;
    }
}
