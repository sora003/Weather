package com.sora.weather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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

    //ImageView控件
    private ImageView iv_weather;
//    private ImageView iv_weather00;
    private ImageView iv_weather01;
    private ImageView iv_weather02;
    private ImageView iv_weather03;
    private ImageView iv_weather04;
    private ImageView iv_weather05;
    private ImageView iv_weather06;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置上下文环境
        mContext = this;
        init();
        initService();
    }

    //初始化参数
    private void init() {

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

        //ImageView 初始化
        iv_weather = (ImageView) findViewById(R.id.iv_weather);
//        iv_weather00 = (ImageView) findViewById(R.id.iv_weather00);
        iv_weather01 = (ImageView) findViewById(R.id.iv_weather01);
        iv_weather02 = (ImageView) findViewById(R.id.iv_weather02);
        iv_weather03 = (ImageView) findViewById(R.id.iv_weather03);
        iv_weather04 = (ImageView) findViewById(R.id.iv_weather04);
        iv_weather05 = (ImageView) findViewById(R.id.iv_weather05);
        iv_weather06 = (ImageView) findViewById(R.id.iv_weather06);

    }


    //启动Service
    private void initService() {
        //指定Service
        Intent intent = new Intent(mContext,WeatherService.class);
        //启动WeatherService
        startService(intent);
        //绑定Service
        bindService(intent,conn,Context.BIND_AUTO_CREATE);
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

    }

    //设置HoursWeather相关参数
    private void setHoursWeatherView(List<HoursWeatherBean> list) {
        tv_hour01.setText(list.get(0).getTime());
        tv_temperature01.setText(list.get(0).getTemperature()+"°C");
        tv_hour02.setText(list.get(1).getTime());
        tv_temperature02.setText(list.get(1).getTemperature()+"°C");
        tv_hour03.setText(list.get(2).getTime());
        tv_temperature03.setText(list.get(2).getTemperature()+"°C");
        tv_hour04.setText(list.get(3).getTime());
        tv_temperature04.setText(list.get(3).getTemperature()+"°C");
        tv_hour05.setText(list.get(4).getTime());
        tv_temperature05.setText(list.get(4).getTemperature()+"°C");
        tv_hour06.setText(list.get(5).getTime());
        tv_temperature06.setText(list.get(5).getTemperature()+"°C");
    }


    //解绑Service
    @Override
    protected void onDestroy() {
        //解绑Service
        unbindService(conn);
        //关闭当前页面正在进行的请求
        //TODO 关闭请求位置是否正确不确定 可能报错
        JuheData.cancelRequests(mContext);
        super.onDestroy();
    }
}
