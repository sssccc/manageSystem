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
import demo.yc.formalmanagersystem.models.Plan;


/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class MyTimeGridViewAdapter extends BaseAdapter{

    LayoutInflater inflater ;
    TextView tv;
    ArrayList<Plan> list = new ArrayList<>();
    int refreshType = 0;

    public MyTimeGridViewAdapter(Context context, ArrayList<Plan> list,int refreshType)
    {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.refreshType = refreshType;
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
            Plan p = list.get(i);
            view = inflater.inflate(R.layout.time_grid_view_item,viewGroup,false);
            tv = (TextView) view.findViewById(R.id.grid_item_text);
            setPlanData(p);
        return view;
    }

    private void setPlanData(Plan p)
    {
        String time = "";
        String type = "";
        //设置第几节课
        switch (p.getDayTime())
        {
            case 1:
                time = "1,2";
                break;
            case 2:
                time ="3,4";
                break;
            case 3:
                time ="5,6";
                break;
            case 4:
                time ="7,8";
                break;
            case 5:
                time ="9,10";
                break;
            case 6:
                time ="11,12,13";
                break;
        }


        switch (refreshType)
        {
            //显示全部
            case 0:
                if(p.getIsFree() == 0)
                {
                    tv.setBackgroundResource(R.drawable.grid_item);
                    tv.setText(time+"\r\n"+"空闲");
                }else {
                    if (p.getType() == 0) {
                        type = "课程";
                        tv.setBackgroundColor(Color.parseColor("#5fbbb0"));
                    } else if (p.getType() == 1) {
                        type = "项目";
                        tv.setBackgroundColor(Color.parseColor("#74c8f6"));
                    } else if (p.getType() == 2) {
                        type = "个人";
                        tv.setBackgroundColor(Color.parseColor("#ffd56a"));
                    }
                    tv.setText(time+"\r\n"+p.getTitle()+"\r\n"+type);
                }

                break;
            //显示空闲
            case 1:
                if(p.getIsFree() == 0)
                {
                    tv.setBackgroundColor(Color.parseColor("#aaff0000"));
                    tv.setText(time+"\r\n"+"空闲");
                }else
                {
                    tv.setBackgroundResource(R.drawable.grid_item);
                    tv.setText(time+"\r\n"+"工作");
                }
                break;
            //显示繁忙
            case 2:
                if(p.getIsFree() == 0)
                {
                    tv.setBackgroundResource(R.drawable.grid_item);
                    tv.setText(time+"\r\n"+"空闲");
                }else
                {
                    tv.setBackgroundColor(Color.parseColor("#FF5BD1B9"));
                    tv.setText(time+"\r\n"+"工作");
                }
                break;
        }
        //设置行程性质

    }
}
