package com.sora.weather.Bean;

/**
 * Created by Sora on 2015/12/30.
 * 未来数小时的天气情况
 */
public class HoursWeatherBean {
    //时间
    private String time;
    //天气
    private String weather_id;
    //温度
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }
}
