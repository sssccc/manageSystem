package demo.yc.formalmanagersystem.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.fragment.TaskBaseFrag;
import demo.yc.formalmanagersystem.models.Task;
import demo.yc.formalmanagersystem.util.DateUtil;
import demo.yc.formalmanagersystem.util.DialogUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;

/**
 * Created by user on 2016/7/25.
 */
public class MySlideListViewAdapter extends BaseAdapter {


    LayoutInflater inflater;
    ArrayList<Task> list;
    Context context;
    TaskBaseFrag fragment;
    int isAll = 0;
    String[] colors = {"#e9cf6a","#7bc957","#6ad9bb","#9a77d6","#c2a561","#ffd56a"};
    public MySlideListViewAdapter(TaskBaseFrag fragment, Context context, ArrayList<Task> list)
    {

        this.fragment = fragment;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public MySlideListViewAdapter(TaskBaseFrag fragment, Context context, ArrayList<Task> list,int isAll)
    {

        this.fragment = fragment;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.isAll = -1;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Task getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.slide_listview_item, parent, false);
            holder = new ViewHolder();
            holder.time = (TextView) view.findViewById(R.id.item_time);
            holder.title = (TextView) view.findViewById(R.id.item_title);
            holder.delete =  view.findViewById(R.id.layout_left);
            holder.finish = view.findViewById(R.id.layout_right);
            holder.point = view.findViewById(R.id.item_point);
            holder.finishTv = (TextView) view.findViewById(R.id.item_finish);

            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN )
            {
                view.findViewById(R.id.layout_left).setVisibility(View.GONE);
                view.findViewById(R.id.layout_right).setVisibility(View.GONE);
            }
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();

        Task task = list.get(position);

        holder.title.setText(task.getTitle());
        if(task.getStatus() == 0)//待处理
            holder.finishTv.setText("参与");
        else
            holder.finishTv.setText("完成");

        holder.time.setText(DateUtil.getDateFromMillions(task.getDeadline()));
        holder.point.setBackgroundColor(Color.parseColor(colors[position%colors.length]));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"点击删除",Toast.LENGTH_SHORT).show();
                showDialog(1,position);
               // fragment.myDelete(position);
            }
        });
        holder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"点击完成",Toast.LENGTH_SHORT).show();
                showDialog(2,position);
            }
        });
        return view;
    }

    private void showDialog(final int choice, final int pos)
    {
        final Task task = getItem(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        int  status = task.getStatus();
        if(choice == 1) {//删除
           switch (status)
           {
               // 0  待处理（接受 或 拒绝）
               // 1  已接受（中途放弃 或 任务完成）
               // 2  已完成 （ 删除任务记录）
               // 3  已放弃  （删除任务记录）

               case 0:
               case 1:
                   builder.setTitle("是否放弃本次任务？");
                   break;
               case 2:
               case 3:
                   builder.setTitle("是否删除该任务记录？");
                   break;
           }
        }else         //2完成
        {
            if(status == 0 )
            {
                builder.setTitle("是否接受该任务？");
            }else
            {
                builder.setTitle("是否完成该任务？");
            }
        }

        builder.setMessage(task.getTitle());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DialogUtil.showDialog(context,null);
                if(choice == 1)
                {
                    new VolleyUtil().quitTask(task.getId(), new UpdateListener() {
                        @Override
                        public void onSucceed(String s) {
                            DialogUtil.dissmiss();
                            if(choice == 1)
                            {
                                fragment.myDelete(pos,isAll);
                            }else
                            {
                                fragment.myFinish(pos,isAll);
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {

                            Toast.makeText(context,"操作失败...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                {
                    new VolleyUtil().finishTask(task.getId(), new UpdateListener() {
                        @Override
                        public void onSucceed(String s) {
                            DialogUtil.dissmiss();
                            if(choice == 1)
                            {
                                fragment.myDelete(pos,isAll);
                            }else
                            {
                                fragment.myFinish(pos,isAll);
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {

                            Toast.makeText(context,"操作失败...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }


    static class ViewHolder
    {
        View delete ,finish;
        View point;
        TextView title,time,finishTv;

    }

}
