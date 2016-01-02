package com.sora.weather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.weather.Bean.HoursWeatherBean;
import com.sora.weather.Bean.PMBean;
import com.sora.weather.Bean.WeatherBean;
import com.sora.weather.Service.WeatherService;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private WeatherService weatherService;
    private Intent intent_Service;

    //字体设置
    private Typeface font;


    //TextView 控件
    private TextView tv_time;
    private TextView tv_city;
    private TextView tv_temperature;
    private TextView tv_refreshTime;
    private TextView tv_weather;
    //取消显示
//    private TextView tv_hour00;
//    private TextView tv_temperature00;
    private TextView tv_hour01;
    private TextView tv_temperature01;
    private TextView tv_hour02;
    private TextView tv_temperature02;
    private TextView tv_hour03;
    private TextView tv_temperature03;
    private TextView tv_hour04;
    private TextView tv_temperature04;
    private TextView tv_hour05;
    private TextView tv_temperature05;
    private TextView tv_hour06;
    private TextView tv_temperature06;
    private TextView tv_pm;
    private TextView tv_air;
    private TextView tv_humidity;
    private TextView tv_wind;
    private TextView tv_uv_index;
    private TextView tv_dressing;

    //Weather Icon
    private TextView iv_weather;
//    private TextView iv_weather00;
    private TextView iv_weather01;
    private TextView iv_weather02;
    private TextView iv_weather03;
    private TextView iv_weather04;
    private TextView iv_weather05;
    private TextView iv_weather06;


    //Layout
    private SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置上下文环境
        mContext = this;
        init();
        initService();
        initListener();
    }

    private void initListener() {
        //设置city选择的点击监听事件
        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, CityActivity.class), 1);
            }
        });

        //设置下拉刷新 的监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                weatherService.getCityWeather();
            }
        });

    }

    //初始化参数
    private void init() {

        font = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        //TextView 控件初始化
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_refreshTime = (TextView) findViewById(R.id.tv_refreshTime);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
//        tv_hour00 = (TextView) findViewById(R.id.tv_hour00);
//        tv_temperature00 = (TextView) findViewById(R.id.tv_temperature00);
        tv_hour01 = (TextView) findViewById(R.id.tv_hour01);
        tv_temperature01 = (TextView) findViewById(R.id.tv_temperature01);
        tv_hour02 = (TextView) findViewById(R.id.tv_hour02);
        tv_temperature02 = (TextView) findViewById(R.id.tv_temperature02);
        tv_hour03 = (TextView) findViewById(R.id.tv_hour03);
        tv_temperature03 = (TextView) findViewById(R.id.tv_temperature03);
        tv_hour04 = (TextView) findViewById(R.id.tv_hour04);
        tv_temperature04 = (TextView) findViewById(R.id.tv_temperature04);
        tv_hour05 = (TextView) findViewById(R.id.tv_hour05);
        tv_temperature05 = (TextView) findViewById(R.id.tv_temperature05);
        tv_hour06 = (TextView) findViewById(R.id.tv_hour06);
        tv_temperature06 = (TextView) findViewById(R.id.tv_temperature06);
        tv_pm = (TextView) findViewById(R.id.tv_pm);
        tv_air = (TextView) findViewById(R.id.tv_air);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_uv_index = (TextView) findViewById(R.id.tv_uv_index);
        tv_dressing = (TextView) findViewById(R.id.tv_dressing);

        //TextView 初始化
        iv_weather = (TextView) findViewById(R.id.iv_weather);
//        iv_weather00 = (TextView) findViewById(R.id.iv_weather00);
        iv_weather01 = (TextView) findViewById(R.id.iv_weather01);
        iv_weather02 = (TextView) findViewById(R.id.iv_weather02);
        iv_weather03 = (TextView) findViewById(R.id.iv_weather03);
        iv_weather04 = (TextView) findViewById(R.id.iv_weather04);
        iv_weather05 = (TextView) findViewById(R.id.iv_weather05);
        iv_weather06 = (TextView) findViewById(R.id.iv_weather06);


        //Layout初始化
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container_main);
        swipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }




    //启动Service
    private void initService() {
        //指定Service
        intent_Service = new Intent(mContext,WeatherService.class);
        //启动WeatherService
        startService(intent_Service);
        //绑定Service
        bindService(intent_Service, conn, Context.BIND_AUTO_CREATE);
    }

    //ServiceConnection 与Service交互
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //通过返回的IBinder对象得到Service的引用
            weatherService = ((WeatherService.WeatherServiceBinder) service).getService();

            //从接口读取封装数据
            weatherService.setCallBack(new WeatherService.OnParserCallBack() {
                @Override
                public void OnParserComplete(List<HoursWeatherBean> list, PMBean pmBean, WeatherBean weatherBean) {
                    //TODO Lists
                    if (list != null && list.size() == 6) {
                        setHoursWeatherView(list);
                    }

                    //pmBean
                    if (pmBean != null) {
                        setPMView(pmBean);
                    }

                    //weatherBean
                    if (weatherBean != null) {
                        setWeatherBean(weatherBean);
                    }
                }
            });


            weatherService.getCityWeather();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            weatherService.removeCallBack();
        }
    };

    //设置PM相关参数
    private void setPMView(PMBean pmBean) {
        tv_pm.setText("PM2.5 : "+pmBean.getPm());
        tv_air.setText("空气质量 : "+pmBean.getAir());
    }

    //设置Weather相关参数
    private void setWeatherBean(WeatherBean weatherBean) {
        tv_time.setText(weatherBean.getTime());
        tv_temperature.setText(weatherBean.getTemperature()+"°C");
        tv_refreshTime.setText("["+weatherBean.getRefreshtime()+"更新]");
        tv_weather.setText(weatherBean.getWeather()+" "+weatherBean.getTemperature_str());
        tv_humidity.setText("湿度 : "+ weatherBean.getHumidity());
        tv_wind.setText("风力风向 : "+weatherBean.getWind());
        tv_uv_index.setText("紫外线指数 : "+weatherBean.getUv_index());
        tv_dressing.setText("穿衣指数 : "+weatherBean.getDressing());

        setWeather_Icon(iv_weather, weatherBean.getWeather_id1());

    }

    //设置HoursWeather相关参数
    private void setHoursWeatherView(List<HoursWeatherBean> list) {
        tv_hour01.setText(list.get(0).getTime());
        tv_temperature01.setText(list.get(0).getTemperature()+"°C");
        setWeather_Icon(iv_weather01, list.get(0).getWeather_id());

        tv_hour02.setText(list.get(1).getTime());
        tv_temperature02.setText(list.get(1).getTemperature()+"°C");
        setWeather_Icon(iv_weather02, list.get(1).getWeather_id());

        tv_hour03.setText(list.get(2).getTime());
        tv_temperature03.setText(list.get(2).getTemperature()+"°C");
        setWeather_Icon(iv_weather03, list.get(2).getWeather_id());

        tv_hour04.setText(list.get(3).getTime());
        tv_temperature04.setText(list.get(3).getTemperature()+"°C");
        setWeather_Icon(iv_weather04, list.get(3).getWeather_id());

        tv_hour05.setText(list.get(4).getTime());
        tv_temperature05.setText(list.get(4).getTemperature()+"°C");
        setWeather_Icon(iv_weather05, list.get(4).getWeather_id());

        tv_hour06.setText(list.get(5).getTime());
        tv_temperature06.setText(list.get(5).getTemperature()+"°C");
        setWeather_Icon(iv_weather06, list.get(5).getWeather_id());

    }

    //设置Weather_Icon
    private void setWeather_Icon(TextView tv, String weather_id) {
        switch (weather_id){
            //晴
            case ("00"):
                tv.setText(R.string.wi_day_sunny);
                break;
            //多云
            case ("01"):
                tv.setText(R.string.wi_cloudy);
                break;
            //阴
            case ("02"):
                tv.setText(R.string.wi_day_haze);
                break;
            //阵雨
            case ("03"):
                tv.setText(R.string.wi_day_hail);
                break;
            //雷阵雨
            case ("04"):
                tv.setText(R.string.wi_day_lightning);
                break;
            //雷阵雨伴有冰雹
            case ("05"):
                tv.setText(R.string.wi_day_snow_thunderstorm);
                break;
            //雨夹雪
            case ("06"):
                tv.setText(R.string.wi_sleet);
                break;
            //小雨
            case ("07"):
                tv.setText(R.string.wi_sprinkle);
                break;
            //中雨
            case ("08"):
                tv.setText(R.string.wi_showers);
                break;
            //大雨
            case ("09"):
                tv.setText(R.string.wi_rain);
                break;
            //暴雨
            case ("10"):
                tv.setText(R.string.wi_hail);
                break;
            //大暴雨
            case ("11"):
                tv.setText(R.string.wi_storm_showers);
                break;
            //特大暴雨
            case ("12"):
                tv.setText(R.string.wi_thunderstorm);
                break;
            //阵雪
            case ("13"):
                tv.setText(R.string.wi_day_snow);
                break;
            //小雪
            case ("14"):
                tv.setText(R.string.wi_day_snow);
                break;
            //中雪
            case ("15"):
                tv.setText(R.string.wi_snow);
                break;
            //大雪
            case ("16"):
                tv.setText(R.string.wi_snow);
                break;
            //暴雪
            case ("17"):
                tv.setText(R.string.wi_snow);
                break;
            //雾
            case ("18"):
                tv.setText(R.string.wi_fog);
                break;
            //冻雨
            case ("19"):
                tv.setText(R.string.wi_rain_mix);
                break;
            //沙尘暴
            case ("20"):
                tv.setText(R.string.wi_sandstorm);
                break;
            //小雨-中雨
            case ("21"):
                tv.setText(R.string.wi_showers);
                break;
            //中雨-大雨
            case ("22"):
                tv.setText(R.string.wi_rain);
                break;
            //大雨-暴雨
            case ("23"):
                tv.setText(R.string.wi_hail);
                break;
            //暴雨-大暴雨
            case ("24"):
                tv.setText(R.string.wi_storm_showers);
                break;
            //大暴雨-特大暴雨
            case ("25"):
                tv.setText(R.string.wi_thunderstorm);
                break;
            //小雪-中雪
            case ("26"):
                tv.setText(R.string.wi_day_snow);
                break;
            //中雪-大雪
            case ("27"):
                tv.setText(R.string.wi_snow);
                break;
            //大雪-暴雪
            case ("28"):
                tv.setText(R.string.wi_snow);
                break;
            //浮尘
            case ("29"):
                tv.setText(R.string.wi_dust);
                break;
            //扬沙
            case ("30"):
                tv.setText(R.string.wi_dust);
                break;
            //强沙尘暴
            case ("31"):
                tv.setText(R.string.wi_sandstorm);
                break;
            //霾
            case ("53"):
                tv.setText(R.string.wi_day_sunny);
                break;
            default:
                break;

        }

        //设置字体
        tv.setTypeface(font);
    }


    //Activity的参数传递
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode ==1){
            String city = data.getStringExtra("city");
            weatherService.getCityWeather(city);
            tv_city.setText(city+"[切换]");
        };
    }

    //解绑Service
    @Override
    protected void onDestroy() {
        //解绑Service
        unbindService(conn);
        //关闭当前页面正在进行的请求
        //TODO 关闭请求位置是否正确不确定 可能报错
        JuheData.cancelRequests(mContext);
        stopService(intent_Service);
        super.onDestroy();
    }
}
