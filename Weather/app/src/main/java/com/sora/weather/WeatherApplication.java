package com.sora.weather;

import android.app.Application;

import com.thinkland.sdk.android.JuheSDKInitializer;


/**
 * Created by Sora on 2015/12/29.
 * 初始化数据接口
 */
public class WeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JuheSDKInitializer.initialize(getApplicationContext());
    }
}
