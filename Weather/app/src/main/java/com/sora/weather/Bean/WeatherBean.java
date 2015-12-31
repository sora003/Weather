package com.sora.weather.Bean;

/**
 * Created by Sora on 2015/12/30.
 */
public class WeatherBean {

    //时间
    private String time;
    //城市
    private String city;
    //天气
    private String weather;
    //天气唯一标识
    private String weather_id1;
    private String weather_id2;
    //温度
    private String temperature;
    //温度区间
    private String temperature_str;
    //更新时间
    private String refreshtime;
    //湿度
    private String humidity;
    //风力风向
    private String wind;
    //紫外线指数
    private String uv_index;
    //穿衣指数
    private String dressing;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDressing() {
        return dressing;
    }

    public void setDressing(String dressing) {
        this.dressing = dressing;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRefreshtime() {
        return refreshtime;
    }

    public void setRefreshtime(String refreshtime) {
        this.refreshtime = refreshtime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperature_str() {
        return temperature_str;
    }

    public void setTemperature_str(String temperature_str) {
        this.temperature_str = temperature_str;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUv_index() {
        return uv_index;
    }

    public void setUv_index(String uv_index) {
        this.uv_index = uv_index;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather_id1() {
        return weather_id1;
    }

    public void setWeather_id1(String weather_id1) {
        this.weather_id1 = weather_id1;
    }

    public String getWeather_id2() {
        return weather_id2;
    }

    public void setWeather_id2(String weather_id2) {
        this.weather_id2 = weather_id2;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
