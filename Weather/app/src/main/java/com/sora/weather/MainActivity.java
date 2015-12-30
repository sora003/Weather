package com.sora.weather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sora.weather.Service.WeatherService;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置上下文环境
        mContext = this;
        initService();
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

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //解绑Service
    @Override
    protected void onDestroy() {
        //解绑Service
        unbindService(conn);
        //关闭当前页面正在进行的请求
        //TODO 关闭请求位置是否正确不确定 可能报错
//        JuheData.cancelRequests(mContext);
        super.onDestroy();
    }
}
