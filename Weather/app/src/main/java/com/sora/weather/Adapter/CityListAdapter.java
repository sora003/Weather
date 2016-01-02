package com.sora.weather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.weather.R;

import java.util.List;

/**
 * Created by Sora on 2016/1/2.
 */
public class CityListAdapter extends BaseAdapter {

    private List<String> list = null;
    LayoutInflater layoutInflater = null;


    //构造方法
    public CityListAdapter(Context context,List<String> list) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view = null;
        if (convertView == null || convertView.getTag() == null){
            view = layoutInflater.inflate(R.layout.activity_city,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_city.setText(getItem(position).toString());

        return view;
    }

    class ViewHolder{
        TextView tv_city;

        public ViewHolder(View view) {

            this.tv_city = (TextView) view.findViewById(R.id.tv_city);

        }
    }
}
