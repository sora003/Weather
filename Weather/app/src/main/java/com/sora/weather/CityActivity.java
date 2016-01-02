package com.sora.weather;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.weather.Adapter.CityListAdapter;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sora on 2016/1/2.
 */
public class CityActivity extends AppCompatActivity {

    private final int CITY_GETED = 0x04;

    //字体设置
    private Typeface font;

    private TextView tv_back;

    private List<String> list;

    private ListView lv_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        init();
        //读取App数据 导入存储的CityList

        //测试代码
//        List<String> testList = new ArrayList<>();
//        testList.add("无锡");
//        testList.add("大浦");
//        testList.add("上海");
//        testList.add("北京");
//        testList.add("南京");
//        testList.add("重庆");
//        showCityList(testList);
//        list = testList;


        //TODO 下拉刷新从网上获取城市数据 获取最新的CityList
        getCityList();

        //ListView 点击事件监听
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                //返回城市信息
                intent.putExtra("city",list.get(position));
                setResult(1, intent);
                finish();
            }
        });

    }


//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case CITY_GETED:
//                    showCityList(list);
//                    break;
//            };
//        }
//    };


    //获取历史数据
    private List<String> getHistoryCityList() {
        List<String> list = new ArrayList<>();

        return list;
    }

    private void getCityList() {


        //查询City列表相关参数
        //请求参数
        Parameters params = new Parameters();
        /**
         * 请求参数说明
         *  	名称 	类型 	必填 	说明
         *dtype string 	N 	返回数据格式：json或xml,默认json
         *key 	string 	Y 	你申请的key
         *
         */




        /**
         * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
         * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
         * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
         *
         * url:http://v.juhe.cn/weather/citys
         */
        JuheData.executeWithAPI(getApplicationContext(), 39, "http://v.juhe.cn/weather/citys", JuheData.GET, params, new DataCallBack() {
            /**
             * 请求成功时调用的方法 statusCode为http状态码,responseString    *为请求返回数据.
             */
            @Override
            public void onSuccess(int statusCode, String responseString) {
                //解析城市列表
                list = parseCities(responseString);
                //更新界面
                showCityList(list);
            }

            /**
             * 请求完成时调用的方法,无论成功或者失败都会调用.
             */
            @Override
            public void onFinish() {

            }

            /**
             * 请求失败时调用的方法,statusCode为http状态码,throwable为捕获到的异常
             * statusCode:30002 没有检测到当前网络. 30003 没有进行初始化. 0
             * 未明异常,具体查看Throwable信息. 其他异常请参照http状态码.
             */
            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //解析城市列表
    private List<String> parseCities(String responseString) {

        List<String> list = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(responseString);
            int code = json.getInt("resultcode");
            if (code == 200){
                JSONArray cityArray = json.getJSONArray("result");
                //确保元素的唯一性
                Set<String> citySet = new HashSet<String>();
                for (int i=0;i<cityArray.length();i++){
                    String city = cityArray.getJSONObject(i).getString("city");
                    citySet.add(city);
                }
                //将集合整体加入list
                list.addAll(citySet);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    //导入CityList 更新界面
    private void showCityList(List<String> list) {
        CityListAdapter adapter = new CityListAdapter(CityActivity.this,list);
        lv_city.setAdapter(adapter);
    }

    //初始化界面信息
    private void init() {
        //设置字体
        font = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");

        lv_city = (ListView) findViewById(R.id.lv_ity);

        tv_back = (TextView) findViewById(R.id.tv_back);
        //设置返回按钮
        tv_back.setText(R.string.wi_direction_left);


    }
}
