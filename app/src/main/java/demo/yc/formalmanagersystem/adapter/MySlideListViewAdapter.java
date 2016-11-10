package demo.yc.formalmanagersystem.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.fragment.TaskBaseFrag;
import demo.yc.formalmanagersystem.models.Task;
import demo.yc.formalmanagersystem.util.DateUtil;
import demo.yc.formalmanagersystem.util.DialogUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.DialogCancelListener;

/**
 * Created by user on 2016/7/25.
 */
public class MySlideListViewAdapter extends BaseAdapter {


    LayoutInflater inflater;
    ArrayList<Task> list;
    Context context;
    TaskBaseFrag fragment;
    int isAll = -1;   // 表示数据来自all 0  还是my1   -1 没有
    int status = -1; //表示数据来自待处理0，已参与1，已完成2，已放弃3
    int taken = -1;  //0 未接， 1 已接
    String[] colors = {"#e9cf6a","#7bc957","#6ad9bb","#9a77d6","#c2a561","#ffd56a"};
    public MySlideListViewAdapter(TaskBaseFrag fragment, Context context, ArrayList<Task> list,int status)
    {
        this(fragment,context,list,status,0);
    }

    public MySlideListViewAdapter(TaskBaseFrag fragment, Context context, ArrayList<Task> list,int status,int isAll)
    {

        this.fragment = fragment;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.isAll = isAll;
        this.status = status;


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
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();

        Task task = list.get(position);

        holder.title.setText(task.getTitle());
        if(status == 0)//待处理
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
                DialogUtil.showDialog(context,null).show();
                DialogUtil.onCancelListener(new DialogCancelListener() {
                    @Override
                    public void onCancel() {
                        MyApplication.getInstance().getMyQueue().cancelAll("quitTask");
                        MyApplication.getInstance().getMyQueue().cancelAll("finishTask");
                    }
                });
                if(choice == 1)
                {
                    new VolleyUtil().quitTask(task.getId(), new UpdateListener() {
                        @Override
                        public void onSucceed(String s) {
                            Log.w("task","task_delete---->ok--->"+s);
                            DialogUtil.dissmiss();
                            if(s == null || s.isEmpty() || s.equals("0"))
                            {
                                Toast.makeText(context,"操作失败...",Toast.LENGTH_SHORT).show();
                            }else if(s.equals("1")){
                              //  Toast.makeText(context,"操作成功...",Toast.LENGTH_SHORT).show();
                                fragment.myDelete(pos, isAll);
                            }else
                            {
                                Toast.makeText(context,"操作失败...",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.w("task","task_delete---->error-->"+error.toString());
                            DialogUtil.dissmiss();
                            Toast.makeText(context,"操作失败...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                {

                    new VolleyUtil().finishTask(task.getId(),status, new UpdateListener() {
                        @Override
                        public void onSucceed(String s) {
                            DialogUtil.dissmiss();
                            Log.w("task","task_finish---->ok--->"+s);
                            if(s == null || s.isEmpty() || s.equals("0"))
                            {
                                Toast.makeText(context,"操作失败...",Toast.LENGTH_SHORT).show();
                            }else if(s.equals("1")){
                                Toast.makeText(context,"操作成功...",Toast.LENGTH_SHORT).show();
                                fragment.myFinish(pos, isAll);
                            }else
                            {
                                Toast.makeText(context,"操作失败...",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            DialogUtil.dissmiss();
                            Log.w("task","task_finish---->error--->"+error.toString());
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
