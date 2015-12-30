package com.sora.weather.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import java.util.concurrent.Delayed;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Sora on 2015/12/30.
 */
public class WeatherService extends Service {

    private String city;

    private final int DELAYMILLIS = 30*60*1000;
    private final int REPEAT_MSG = 0x01;


    @Nullable
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

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REPEAT_MSG:
                    getCityWeather();
                    //更新数据 时间间隔为DELAYMILLIS
                    sendEmptyMessageDelayed(REPEAT_MSG,DELAYMILLIS);
                    break;
            }
        }
    };

    private void getCityWeather() {
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
        params.add("ip","www.juhe.cn");
        params.add("dtype","json");
        params.add("city",city);

        /**
         * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
         * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
         * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
         *
         */
        JuheData.executeWithAPI(getApplicationContext(), 1, "http://v.juhe.cn/weather/index", JuheData.GET, params, new DataCallBack() {
            /**
             * 请求成功时调用的方法 statusCode为http状态码,responseString    *为请求返回数据.
             */
            @Override
            public void onSuccess(int statusCode, String responseString) {
                // TODO Auto-generated method stub
                tv.append(responseString + "\n");
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
    }
}

