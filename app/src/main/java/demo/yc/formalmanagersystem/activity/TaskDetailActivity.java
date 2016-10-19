package demo.yc.formalmanagersystem.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.adapter.MyTaskProgressListViewAdapter;
import demo.yc.formalmanagersystem.models.Task;
import demo.yc.formalmanagersystem.util.DialogUtil;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.CircleImageView;
import demo.yc.formalmanagersystem.view.MyHeightListView;

public class TaskDetailActivity extends BaseActivity {

    ImageView backBtn,images;
    CircleImageView head;
    Button involveBtn,quitBtn,imagesBtn;
    View bottomLayout;

    TextView title,name,describe,cateName,groupName,time;

    MyHeightListView listView;
    ScrollView scrollView;
    MyTaskProgressListViewAdapter adapter;
    ArrayList<String> list= new ArrayList<>();
    Intent lastIntent ;

    Task task ;


    String taskId;
    int status ;  //0 未处理  1 已参与  2已完成  3 已放弃
    int pos;    //当前item position
   // int reback;//  >0 delete   <0  finish


    private ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.show();
        getMyIntent();
    }

    private void setUi()
    {

        involveBtn = (Button) findViewById(R.id.task_detail_involve_btn);
        quitBtn = (Button) findViewById(R.id.task_detail_quit_btn);
        imagesBtn = (Button) findViewById(R.id.task_detail_image_btn);
        bottomLayout = findViewById(R.id.task_detail_btn_layout);

        time = (TextView) findViewById(R.id.task_detail_time);
        name = (TextView) findViewById(R.id.task_detail_administor_name);
        title = (TextView) findViewById(R.id.task_detail_title);
        cateName = (TextView) findViewById(R.id.task_detail_cate_name);
        groupName = (TextView) findViewById(R.id.task_detail_group_name);
        describe = (TextView) findViewById(R.id.task_detail_describe);
        head = (CircleImageView) findViewById(R.id.task_detail_administor_head);
        head.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        images = (ImageView) findViewById(R.id.task_detail_images);
        backBtn = (ImageView) findViewById(R.id.task_detail_back_btn);

        scrollView = (ScrollView) findViewById(R.id.task_detail_scrollView);
        scrollView.smoothScrollTo(0,0);
        listView = (MyHeightListView) findViewById(R.id.task_detail_progress_listView);
    }

    private void setListener()
    {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED,lastIntent);
                finish();
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog(1);

            }
        });
        involveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog(2);

            }
        });
    }

    private void setData()
    {
//        list.add("界面设计");
//        list.add("界面实现");
//        list.add("头像修改");


        switch (task.getStatus())
        {
            case 0:
                break;
            case 1:
                involveBtn.setText("完成");
                break;
            case 2:
            case 3:
                bottomLayout.setVisibility(View.GONE);
                break;
        }
        title.setText(task.getTitle());
        name.setText(task.getAdministor());
        time.setText(task.getStart()+"至"+task.getDead());
        describe.setText(task.getContent());
        cateName.setText(task.getProjectId());
        groupName.setText(task.getQuartersId());
    }

    private void setAdapter()
    {
        adapter = new MyTaskProgressListViewAdapter(this,list);
        listView.setAdapter(adapter);
    }

    private void getMyIntent() {
        lastIntent = getIntent();
        pos = lastIntent.getExtras().getInt("pos");
        taskId = lastIntent.getExtras().getString("taskId");
        new VolleyUtil().getTaskDetail(taskId, new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                task = JsonUtil.parseSingleJson(s);
                if (task == null) {
                    //获取失败
                    getDataFromLocal();
                } else {
                    task = JsonUtil.parseSingleJson(s);
                    setContentView(R.layout.activity_task_detail);
                    setUi();
                    setListener();
                    setData();
                    pd.dismiss();
                }
            }

            @Override
            public void onError(VolleyError error) {
                getDataFromLocal();
            }
        });
    }

    public void myDialog(final int choice)
    {
        //final Task task = getItem(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(choice == 1) {//删除
            switch (task.getStatus())
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
            if(task.getStatus() == 0 )
            {
                builder.setTitle("是否接受该任务？");
            }else
            {
                builder.setTitle("是否完成该任务？");
            }
            this.getExternalCacheDir();
        }

        builder.setMessage(task.getTitle());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DialogUtil.showDialog(TaskDetailActivity.this,null).show();

                new VolleyUtil().quitTask(task.getId(), new UpdateListener() {
                    @Override
                    public void onSucceed(String s) {
                        DialogUtil.dissmiss();
                        Toast.makeText(TaskDetailActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
                        if(choice == 1)
                        {
                            lastIntent.putExtra("reback",1);
                        }else
                        {
                            lastIntent.putExtra("reback",-1);
                        }
                        //status = lastIntent.getExtras().getInt("status");
                        lastIntent.putExtra("status",task.getStatus());
                        lastIntent.putExtra("pos",pos);
                        setResult(RESULT_OK,lastIntent);
                        finish();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        DialogUtil.dissmiss();
                        Toast.makeText(TaskDetailActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }

    //没有网络，用来测试的数据
    private void getDataFromLocal()
    {

        StringBuffer sb =new StringBuffer();
        try {
            InputStream is = getResources().getAssets().open("singletask.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line ;
            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        task = JsonUtil.parseSingleJson(sb.toString());
        setContentView(R.layout.activity_task_detail);
        setUi();
        setListener();
        setData();
        pd.dismiss();

//        list = JsonUtil.parseTaskJson(sb.toString());
//        showListView();
    }
}
