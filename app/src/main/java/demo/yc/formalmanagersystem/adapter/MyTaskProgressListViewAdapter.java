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
import demo.yc.formalmanagersystem.models.TaskProcess;


/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class MyTaskProgressListViewAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<TaskProcess> list = new ArrayList<>();
    String[] colors = {"#e9cf6a","#7bc957","#6ad9bb","#9a77d6","#c2a561"};

    public MyTaskProgressListViewAdapter(Context context, ArrayList<TaskProcess> list)
    {
        inflater = LayoutInflater.from(context);
        this.list = list;
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
        ViewHolder holder ;
        if(view1 == null)
        {
            view1 = inflater.inflate(R.layout.task_detail_progress_list_item,viewGroup,false);
            holder = new ViewHolder();
            holder.point = view1.findViewById(R.id.task_detail_list_item_point);
            holder.time = (TextView) view1.findViewById(R.id.task_detail_list_item_time);
            holder.name = (TextView) view1.findViewById(R.id.task_detail_list_item_name);
            holder.title = (TextView) view1.findViewById(R.id.task_detail_list_item_title);
            view1.setTag(holder);
        }else
            holder = (ViewHolder) view1.getTag();

           TaskProcess tp = list.get(i);
           holder.title.setText(tp.getContent());
           holder.name.setText(tp.getName());
           holder.time.setText(tp.getTime());
           holder.point.setBackgroundColor(Color.parseColor(colors[i%colors.length]));
        return view1;
    }

    static class ViewHolder
    {
        View point;
        TextView title,name,time;
    }
}
