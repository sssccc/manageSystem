package demo.yc.formalmanagersystem.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.adapter.MyTaskProgressListViewAdapter;
import demo.yc.formalmanagersystem.models.Task;
import demo.yc.formalmanagersystem.models.TaskProcess;
import demo.yc.formalmanagersystem.util.DateUtil;
import demo.yc.formalmanagersystem.util.DialogUtil;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.CircleImageView;
import demo.yc.formalmanagersystem.view.DialogCancelListener;
import demo.yc.formalmanagersystem.view.MyHeightListView;

public class TaskDetailActivity extends BaseActivity {

    ImageView backBtn,images;
    CircleImageView head;
    Button involveBtn,quitBtn,imagesBtn;

    View bottomLayout;
    View processLayout;
    ImageView textBtn,sendBtn,cycleBtn;
    View textLayout,operLayout;
    EditText editText ;

    TextView title,name,describe,cateName,groupName,time;

    MyHeightListView listView;
    ScrollView scrollView;
    MyTaskProgressListViewAdapter adapter;
    ArrayList<TaskProcess> list= new ArrayList<>();
    Intent lastIntent ;

    Task task ;
    String taskId;
    int status = -1;
    int pos;    //当前item position


    private ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                setResult(RESULT_OK);
                finish();
            }
        });
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
        processLayout = findViewById(R.id.task_detail_progress_layout);

        textBtn = (ImageView) findViewById(R.id.task_detail_bottom_text_btn);
        cycleBtn = (ImageView) findViewById(R.id.task_detail_bottom_oper_btn);
        sendBtn = (ImageView) findViewById(R.id.task_detail_bottom_send_btn);
        textLayout = findViewById(R.id.task_detail_bottom_text_layout);
        operLayout = findViewById(R.id.task_detail_bottom_oper_layout);
        editText = (EditText) findViewById(R.id.task_detail_bottom_edit);
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


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskRecodeToProcess();
            }
        });

        cycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLayout.setVisibility(View.GONE);
                operLayout.setVisibility(View.VISIBLE);
            }
        });

        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLayout.setVisibility(View.VISIBLE);
                operLayout.setVisibility(View.GONE);
            }
        });
    }

    private void setData()
    {
        switch (status)
        {
            case 0:
                processLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                textBtn.setVisibility(View.GONE);
                break;
            case 1:
                getTaskRecode();
                textLayout.setVisibility(View.VISIBLE);
                operLayout.setVisibility(View.GONE);
                involveBtn.setText("完成");
                break;
            case 2:
                getTaskRecode();
                bottomLayout.setVisibility(View.GONE);
                break;
            case 3:
                processLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
                break;
        }
        Log.w("task","show date-->"+task.getDeadline()+"....."+task.getStartDate());
        title.setText(task.getTitle());
        name.setText(task.getAdministor());
        time.setText(DateUtil.getDateFromMillions(task.getStartDate())+"至"+DateUtil.getDateFromMillions(task.getDeadline()));
        describe.setText(task.getContent());
        cateName.setText(task.getQuarters().getName());
        groupName.setText(task.getProjectTeam().getName());

    }
    private void setAdapter()
    {
        if(adapter == null) {
            adapter = new MyTaskProgressListViewAdapter(this, list);
            listView.setAdapter(adapter);
        }else
        {
            adapter.notifyDataSetChanged();
        }
    }

    //接收  待处理，， 已参与，，已完成，，已放弃的  详情查询
    private void getMyIntent() {
        lastIntent = getIntent();
        status = lastIntent.getExtras().getInt("status");
        pos = lastIntent.getExtras().getInt("pos");
        taskId = lastIntent.getExtras().getString("taskId");
        Log.w("detail",status + "-----" + pos + "-----" + taskId);
        new VolleyUtil().getTaskDetail(taskId, new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                Log.w("task","task-->detail-->"+s);
                if(JsonUtil.isSingleCorrected(s))
                {
                    task = JsonUtil.parseSingleJson(s);
                    if (task == null) {
                        //获取失败
                        Toast.makeText(TaskDetailActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
                       // getDataFromLocal();
                    } else {
                        setContentView(R.layout.activity_task_detail);
                        setUi();
                        setListener();
                        setData();
                        pd.dismiss();
                    }
                }else
                {
                    //格式错误
                    Toast.makeText(TaskDetailActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
                    //getDataFromLocal();
                }

            }

            @Override
            public void onError(VolleyError error) {
                getDataFromLocal();
            }
        });
    }


    /**
     * choice  表示是要放弃  还是参与（接收） 1 放弃   2参与
     * status  表示从哪个地方传进来的请求
     * @param choice
     */
    public void myDialog(final int choice)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(choice == 1)
            builder.setTitle("是否放弃本次任务？");
        else         //2完成
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

                DialogUtil.showDialog(TaskDetailActivity.this,null).show();
                DialogUtil.onCancelListener(new DialogCancelListener() {
                    @Override
                    public void onCancel() {

                        DialogUtil.dissmiss();
                        Toast.makeText(TaskDetailActivity.this,"操作已取消",Toast.LENGTH_SHORT).show();
                        MyApplication.getInstance().getMyQueue().cancelAll("quitTask");
                        MyApplication.getInstance().getMyQueue().cancelAll("finishTask");
                    }
                });
               quitOrInvolve(choice);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }

    //网络申请放弃获取参与任务
    private void quitOrInvolve(int choice)
    {
        //删除
        if(choice == 1)
        {
            new VolleyUtil().quitTask(task.getId(),new UpdateListener() {
                @Override
                public void onSucceed(String s) {
                    lastIntent.putExtra("reback", -1);
                    onsuccess(s);
                }

                @Override
                public void onError(VolleyError error) {
                    DialogUtil.dissmiss();
                    Log.w("task","delete---->taskdetail-->error"+error.toString());
                    Toast.makeText(TaskDetailActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            new VolleyUtil().finishTask(task.getId(),status,new UpdateListener() {
                @Override
                public void onSucceed(String s) {
                    lastIntent.putExtra("reback", -1);
                    onsuccess(s);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.w("task","finish---->taskdetail-->error"+error.toString());
                    DialogUtil.dissmiss();
                    Toast.makeText(TaskDetailActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //操作成后的回调
    private void onsuccess(String s)
    {
        if( s!= null && s.equals("1")) {
            DialogUtil.dissmiss();
            Toast.makeText(TaskDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();

            lastIntent.putExtra("status", status);
            lastIntent.putExtra("pos", pos);
            setResult(RESULT_OK, lastIntent);
            finish();
        }else
        {
            DialogUtil.dissmiss();
            Toast.makeText(TaskDetailActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
        }
    }

    //如果是参与任务，则添加任务进度描述
    private void addTaskRecodeToProcess()
    {
        String content = editText.getText().toString();
        if(content == null || content.isEmpty())
        {
            Toast.makeText(TaskDetailActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
        }else
        {
            final TaskProcess t = new TaskProcess(content,MyApplication.getPersonName(),System.currentTimeMillis()+"");
            DialogUtil.showDialog(this,"正在添加").show();
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(TaskDetailActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            new VolleyUtil().updateTaskProcessRecord(taskId,t, new UpdateListener() {
                @Override
               public void onSucceed(String s) {
                    DialogUtil.dissmiss();
                    Log.w("process","task-->process"+s);
                    if(s != null && s.equals("1")) {
                        list.add(t);
                        editText.setText("");
                        setAdapter();
                    }else
                    {
                        Toast.makeText(TaskDetailActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    DialogUtil.dissmiss();
                    Toast.makeText(TaskDetailActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                    Log.w("process","task-->process---访问错误");
                }
            });

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    if(adapter == null)
//                    {
//                        adapter = new MyTaskProgressListViewAdapter(TaskDetailActivity.this,list);
//                        listView.setAdapter(adapter);
//                    }else
//                        adapter.notifyDataSetChanged();
//                    DialogUtil.dissmiss();
//                    editText.setText("");
//                  //  Toast.makeText(TaskDetailActivity.this,t.getCreateAt(),Toast.LENGTH_SHORT).show();
//                }
//            },3000);


        }
    }

    //如果是参与任务和历史完成任务，查询进度记录
    private void getTaskRecode()
    {
        new VolleyUtil().getTaskProcessRecord(taskId,new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                Log.w("task","process--->返回的字符串"+s);
                if(s == null || s.equals("") || !s.startsWith("["))
                {
                    Log.w("task","process--->格式错误");
                    getProcessDataFromLocal();
                }else
                {
                    Log.w("task","process--->解析成功");
                    list = JsonUtil.parseTaskProcess(s);
                    if(list.size() != 0)
                    {
                        setAdapter();
                    }else
                    {
                        Log.w("task","process--->没有记录");
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                getProcessDataFromLocal();
                Log.w("task","process--->访问失败"+error.toString());
            }
        });
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


    private void getProcessDataFromLocal()
    {
        StringBuffer sb =new StringBuffer();
        try {
            InputStream is = getResources().getAssets().open("process.txt");
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
        list = JsonUtil.parseTaskProcess(sb.toString());
        setAdapter();
    }


}
