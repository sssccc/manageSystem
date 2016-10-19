package demo.yc.formalmanagersystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.R;


/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class MyAgeGridViewAdapter extends BaseAdapter{

    LayoutInflater inflater ;
    TextView tv;
    int value;
    int[] list ;


    public MyAgeGridViewAdapter(Context context, int[] list, int value)
    {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.value = value;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        view = inflater.inflate(R.layout.age_grid_view_item, viewGroup, false);
        tv = (TextView) view.findViewById(R.id.age_grid_item);
        tv.setText(list[i]+"");
        if((value-1) == i)
            tv.setBackgroundColor(Color.GRAY);

        return view;
    }


}
