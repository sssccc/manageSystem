package demo.yc.formalmanagersystem.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import demo.yc.formalmanagersystem.activity.TaskDetailActivity;
import demo.yc.formalmanagersystem.adapter.MySlideListViewAdapter;
import demo.yc.formalmanagersystem.models.Task;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.MySlideListView2;

/**
 * A simple {@link Fragment} subclass.
 */
public class Task_Quit_Frag extends TaskBaseFrag implements SwipeRefreshLayout.OnRefreshListener{


    public Task_Quit_Frag() {
        // Required empty public constructor
    }

    View view;
    MySlideListView2 listView;
    MySlideListViewAdapter adapter;
    ArrayList<Task> list = new ArrayList<>();

    SwipeRefreshLayout refreshLayout;
    TextView numTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskManageFrag taskManageFrag = (TaskManageFrag) getParentFragment();
        taskManageFrag.setMyTitle("放弃任务");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_quit, container, false);
        setUi();
        setData();
        setListener();
        return view;
    }

    private void setData()
    {
        new VolleyUtil().getQuitTaskList(MyApplication.getUser().getId(), new UpdateListener() {
            @Override
            public void onSucceed(String s) {

                Log.w("task","quitTask访问后台服务器成功:"+s);

                if( s == null || !s.startsWith("[")) {
                    Log.w("task","quitTask后台服务器返回数据异常");
                    Toast.makeText(getContext(), "获取数据异常", Toast.LENGTH_SHORT).show();
                    getDataFromLocal();
                            return;
                }

                list = JsonUtil.parseTaskJson(s);
                if(list != null) {
                    showListView();
                }
                else
                {
                    Log.w("task","json task wei 空");
                    Toast.makeText(getContext(),"暂无数据",Toast.LENGTH_SHORT).show();
                    getDataFromLocal();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(getContext(),"quitTask数据获取异常",Toast.LENGTH_SHORT).show();
                Log.w("task","quitTask访问后台服务器失败："+error.toString());
                getDataFromLocal();
            }
        });
    }

    private void setUi()
    {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.task_quit_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN);
        refreshLayout.setDistanceToTriggerSync(250);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        onRefresh();

        numTv = (TextView) view.findViewById(R.id.quit_list_num);
        listView = (MySlideListView2) view.findViewById(R.id.task_quit_listView);
        listView.initSlideMode(1);
    }

    //网络请求数据之后，绑定adapter 显示listView
    private void showListView()
    {
        refreshLayout.setRefreshing(false);
        adapter = new MySlideListViewAdapter(this,getContext(),list,3);
        listView.setAdapter(adapter);
        numTv.setText(list.size()+"");

    }

    private void setListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("taskId", list.get(i).getId());
                intent.putExtra("pos",i);
                intent.putExtra("status",3);
                getParentFragment().startActivityForResult(intent, 1);
            }
        });
    }


    /**
     * pos  坐标  flag  在这里没有用
     * @param pos
     * @param flag
     */
    @Override
    public void myDelete(int pos,int flag) {
        Toast.makeText(getContext(), "改任务记录已删除", Toast.LENGTH_SHORT).show();
        list.remove(pos);
        adapter.notifyDataSetChanged();
        numTv.setText(list.size()+"");
        listView.slideBack();
    }

    @Override
    public void myFinish(int pos, int flag) {

    }

    @Override
    public void myRefresh(int requestCode, Intent intent) {

    }
    //没有网络，用来测试的数据
    private void getDataFromLocal()
    {
        StringBuffer sb =new StringBuffer();
        try {
            InputStream is = getResources().getAssets().open("task.txt");
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
        list = JsonUtil.parseTaskJson(sb.toString());
        showListView();
    }

    @Override
    public void onRefresh() {
        setData();
    }
}
