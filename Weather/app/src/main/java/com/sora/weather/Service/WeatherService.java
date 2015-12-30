package com.sora.weather.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.sora.weather.Bean.HoursWeatherBean;
import com.sora.weather.Bean.WeatherBean;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Sora on 2015/12/30.
 */
public class WeatherService extends Service {

    private String city;
    //Weather Info
    private WeatherBean weatherBean = null;
    private List<HoursWeatherBean> list = null;
//    private WeatherServiceBinder binder = new WeatherServiceBinder();

    private final String tag = "Weather Service";
    private final int DELAYMILLIS = 30*60*1000;
    private final int REPEAT_MSG = 0x01;
    private final int CALLBACK_SUCCESS = 0x02;
    private final int CALLBACK_ERROR = 0x03;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        city = "南京";
        handler.sendEmptyMessage(REPEAT_MSG);
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
//    }

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REPEAT_MSG:
                    getCityWeather();
                    //更新数据 时间间隔为DELAYMILLIS
                    sendEmptyMessageDelayed(REPEAT_MSG,DELAYMILLIS);
                    break;
                case CALLBACK_SUCCESS:
                    //TODO 将WeatherBean和list返回给MainActivity
                    break;
                case CALLBACK_ERROR:
                    Toast.makeText(getApplicationContext(), "loading error", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void getCityWeather() {
        //构造同步计数器 确保线程执行顺序
        final CountDownLatch countDownLatch = new CountDownLatch(3);

        //请求参数
        Parameters params = new Parameters();
        /**
         * 请求参数说明
         *  	名称 	类型 	必填 	说明
         *cityname 	string 	Y 	城市名或城市ID，如："苏州"，需要utf8 urlencode
         *dtype 	string 	N 	返回数据格式：json或xml,默认json
         *format 	int 	N 	未来6天预报(future)两种返回格式，1或2，默认1
         *key 	string 	Y 	你申请的key
         *
         */
        params.add("cityname",city);

        /**
         * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
         * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
         * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
         *
         * url:http://v.juhe.cn/weather/index
         */
        JuheData.executeWithAPI(getApplicationContext(), 1, "http://v.juhe.cn/weather/index", JuheData.GET, params, new DataCallBack() {
            /**
             * 请求成功时调用的方法 statusCode为http状态码,responseString    *为请求返回数据.
             */
            @Override
            public void onSuccess(int statusCode, String responseString) {
                // TODO Auto-generated method stub
                //解析Weather
                parseWeather(responseString);
                //计数减少1
                countDownLatch.countDown();
            }

            /**
             * 请求完成时调用的方法,无论成功或者失败都会调用.
             */
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();
            }

            /**
             * 请求失败时调用的方法,statusCode为http状态码,throwable为捕获到的异常
             * statusCode:30002 没有检测到当前网络. 30003 没有进行初始化. 0
             * 未明异常,具体查看Throwable信息. 其他异常请参照http状态码.
             */
            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //请求参数
        params = new Parameters();
        /**
         * 请求参数说明
         *  	名称 	类型 	必填 	说明
         *cityname 	string 	Y 	城市名或城市ID，如："苏州"，需要utf8 urlencode
         *dtype 	string 	N 	返回数据格式：json或xml,默认json
         *key 	string 	Y 	你申请的key
         *
         */
        params.add("cityname",city);

        /**
         * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
         * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
         * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
         *
         * url:"http://v.juhe.cn/weather/forecast3h"
         */
        JuheData.executeWithAPI(getApplicationContext(), 1, "http://v.juhe.cn/weather/forecast3h", JuheData.GET, params, new DataCallBack() {
            /**
             * 请求成功时调用的方法 statusCode为http状态码,responseString    *为请求返回数据.
             */
            @Override
            public void onSuccess(int statusCode, String responseString) {
                // TODO Auto-generated method stub
                //解析每3小时的Weather
                parseHoursWeather(responseString);
                //计数减少1
                countDownLatch.countDown();
            }

            /**
             * 请求完成时调用的方法,无论成功或者失败都会调用.
             */
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();
            }

            /**
             * 请求失败时调用的方法,statusCode为http状态码,throwable为捕获到的异常
             * statusCode:30002 没有检测到当前网络. 30003 没有进行初始化. 0
             * 未明异常,具体查看Throwable信息. 其他异常请参照http状态码.
             */
            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //请求参数
        params = new Parameters();
        /**
         * 请求参数说明
         *  	名称 	类型 	必填 	说明
         *city	string 	Y 	城市名或城市ID，如："苏州"，需要utf8 urlencode
         *key 	string 	Y 	你申请的key
         *
         */
        params.add("city",city);

        /**
         * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
         * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
         * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
         *
         * url:"http://v.juhe.cn/weather/forecast3h"
         */
        JuheData.executeWithAPI(getApplicationContext(), 1, "http://v.juhe.cn/weather/forecast3h", JuheData.GET, params, new DataCallBack() {
            /**
             * 请求成功时调用的方法 statusCode为http状态码,responseString    *为请求返回数据.
             */
            @Override
            public void onSuccess(int statusCode, String responseString) {
                // TODO Auto-generated method stub
                //解析Weather
                parseWeather(responseString);
                //计数减少1
                countDownLatch.countDown();
            }

            /**
             * 请求完成时调用的方法,无论成功或者失败都会调用.
             */
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();
            }

            /**
             * 请求失败时调用的方法,statusCode为http状态码,throwable为捕获到的异常
             * statusCode:30002 没有检测到当前网络. 30003 没有进行初始化. 0
             * 未明异常,具体查看Throwable信息. 其他异常请参照http状态码.
             */
            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        new Thread(){
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    //向Handler发送线程运行成功消息
                    handler.sendEmptyMessage(CALLBACK_SUCCESS);
                } catch (InterruptedException e) {
                    //发送Error 消息
                    handler.sendEmptyMessage(CALLBACK_ERROR);
                    return;
                }
            }
        }.start();
    }


    //解析Weather
    private void parseWeather(String responseString) {

    }

    //解析每3小时的Weather
    private void parseHoursWeather(String responseString) {

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.v(tag,"onDestroy");
//    }

//    public class WeatherServiceBinder extends Binder{
//
//        public WeatherService getService(){
//            return WeatherService.this;
//        }
//    }

}

