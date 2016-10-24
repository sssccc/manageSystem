package demo.yc.formalmanagersystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import demo.yc.formalmanagersystem.view.RefreshableView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Task_History_Frag extends TaskBaseFrag {


    View view;
    RefreshableView refreshableView;
    MySlideListView2 listView;
    MySlideListViewAdapter adapter;
    ArrayList<Task> list = new ArrayList<>();


    TextView numTv;

    public Task_History_Frag() {
        // Required empty public constructor
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskManageFrag taskManageFrag = (TaskManageFrag) getParentFragment();
        taskManageFrag.setMyTitle("历史任务");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_history, container, false);
        setUi();
        setListener();
        return view;
    }

    //网络请求数据
    private void setData() {
        new VolleyUtil().getHistoryTaskList(MyApplication.getUser().getId(), new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                Log.w("task", "historyTask访问后台服务器成功:" + s);

                if (s == null || !s.startsWith("[")) {
                    Log.w("task", "history后台服务器返回数据异常");
                    Toast.makeText(getContext(), "获取数据异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                list = JsonUtil.parseTaskJson(s);
                if (list != null) {
                    Log.w("task", "history获取数据成功" + list.size());
                    showListView();
                } else {
                    Log.w("task", "json task 为 空");
                    Toast.makeText(getContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                getDataFromLocal();
                refreshableView.finishRefreshing("task_history");
            }
        });
    }

    private void setUi() {
        refreshableView = (RefreshableView) view.findViewById(R.id.refresh_view_in_history);
        numTv = (TextView) view.findViewById(R.id.history_list_num);
        listView = (MySlideListView2) view.findViewById(R.id.task_history_listView);
        listView.initSlideMode(1);
    }

    //请求数据之后，绑定adapter
    private void showListView() {
        refreshableView.finishRefreshing("history");
        adapter = new MySlideListViewAdapter(this, getContext(), list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        numTv.setText(list.size() + "");
    }

    @Override
    public void myDelete(int pos, int flag) {
        //网络请求删除记录
        Toast.makeText(getContext(), "该任务记录已删除", Toast.LENGTH_SHORT).show();
        list.remove(pos);
        adapter.notifyDataSetChanged();
        listView.slideBack();

        numTv.setText(list.size() + "");
    }

    @Override
    public void myFinish(int pos, int flag) {

    }

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("taskId", list.get(i).getId());
                intent.putExtra("pos", i);
                getParentFragment().startActivityForResult(intent, 1);
            }
        });

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessage(0x112);
                setData();
            }
        }, 12);
//
    }


    @Override
    public void myRefresh(int requestCode, Intent intent) {
    }


    //没有网络，用来测试的数据
    private void getDataFromLocal() {

        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = getResources().getAssets().open("task.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
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

}
