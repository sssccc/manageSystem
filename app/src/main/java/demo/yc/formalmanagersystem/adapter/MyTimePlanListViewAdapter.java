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
import demo.yc.formalmanagersystem.contentvalues.ColorContent;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class MyTimePlanListViewAdapter extends BaseAdapter {

    ArrayList<String> list;
    LayoutInflater inflater;

    String[] times = {"1,2","3,4","5,6","7,8","9,10","11,12,13"};
    public MyTimePlanListViewAdapter(Context context, ArrayList<String> list)
    {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
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
        View view1 = view;
        ViewHolder holder;
        if (view1 == null) {
            view1 = inflater.inflate(R.layout.time_plan_list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.line = view1.findViewById(R.id.time_plan_item_line);
            holder.time = (TextView) view1.findViewById(R.id.time_plan_item_time);
            holder.type = (TextView) view1.findViewById(R.id.time_plan_item_type);
            holder.title = (TextView) view1.findViewById(R.id.time_plan_item_title);
            view1.setTag(holder);
        } else
            holder = (ViewHolder) view1.getTag();

        holder.line.setBackgroundColor(Color.parseColor(ColorContent.colors[i % ColorContent.colors.length]));
        holder.time.setText(times[i%times.length]);
        return view1;
    }
    static class ViewHolder
    {
        View line;
        TextView title,type,time;
    }

}


