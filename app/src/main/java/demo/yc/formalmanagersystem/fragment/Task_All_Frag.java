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
import demo.yc.formalmanagersystem.activity.TaskDetailActivity;
import demo.yc.formalmanagersystem.adapter.MySlideListViewAdapter;
import demo.yc.formalmanagersystem.models.Task;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.MySlideListView;
import demo.yc.formalmanagersystem.view.RefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class Task_All_Frag extends TaskBaseFrag implements SwipeRefreshLayout.OnRefreshListener{
    MySlideListView allListView, myListView;
    ScrollView scrollView;
    View view;

    ArrayList<Task> allList = new ArrayList<>();
    ArrayList<Task> myList = new ArrayList<>();
    MySlideListViewAdapter allAdapter, myAdapter;
    TextView allNumTv,myNumTv;

    RefreshLayout refreshLayout;

    boolean isAll ,isMy;
    public Task_All_Frag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskManageFrag taskManageFrag = (TaskManageFrag) getParentFragment();
        taskManageFrag.setMyTitle("所有任务");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_all, container, false);
        setUi();
        setData();
        setListener();
        return view;
    }

    private void setUi() {

        refreshLayout = (RefreshLayout) view.findViewById(R.id.task_all_refresh_layout);
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
        allListView = (MySlideListView) view.findViewById(R.id.task_all_all_people_listView);
        myListView = (MySlideListView) view.findViewById(R.id.task_all_my_group_listView);
        allListView.initSlideMode(3);
        myListView.initSlideMode(3);
        scrollView = (ScrollView) view.findViewById(R.id.task_all_scrollView);
        scrollView.smoothScrollTo(0, 0);

        allNumTv = (TextView) view.findViewById(R.id.all_all_list_num);
        myNumTv = (TextView) view.findViewById(R.id.all_my_list_num);
    }

    //网络请求数据
    private void setData() {
        //获取所有人的
        new VolleyUtil().getAllAcceptableTaskList(new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                Log.w("task","all accept ok:"+s);
                if(JsonUtil.isListCorrected(s))
                {
                    allList = JsonUtil.parseTaskJson(s);
                    isAll = true;
                    if(allList != null) {
                        showAllListView(0);
                    }
                }else
                {
                    Log.w("task","taskAll解析错误");
                    refreshLayout.setRefreshing(false);
                    //getDataFromLocal(0);
                }

        }

            @Override
            public void onError(VolleyError error) {
                isAll = true;
                refreshLayout.setRefreshing(false);
                Log.w("task","all accept:"+error.toString());
                getDataFromLocal(0);
            }
        });

        //获取我的任务
        Log.w("task",MyApplication.getPersonId());
        new VolleyUtil().getMyAcceptableTaskList(MyApplication.getUser().getId(), new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                if(JsonUtil.isListCorrected(s)) {
                    isMy = true;
                    Log.w("task", "my accept ok:" + s.toString());
                    myList = JsonUtil.parseTaskJson(s);
                    showMyListView(0);
                }else
                {
                    Log.w("task","mytask解析错误");
                    //getDataFromLocal(0);
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                isMy = true;
                Log.w("task","my accept:"+error.toString());
               // getDataFromLocal(1);
                refreshLayout.setRefreshing(false);
            }
        });

    }


    //绑定dapter显示listView
    private void showAllListView(int flag)
    {
        //判断是否为空。。然后显示图片  还是listView
        showList();
        allAdapter = new MySlideListViewAdapter(this,getContext(),allList,0,0);
        allListView.setAdapter(allAdapter);
        allNumTv.setText(allList.size()+"");
    }

    private void showMyListView(int flag)
    {
        showList();
        myAdapter = new MySlideListViewAdapter(this,getContext(),myList,0,1);
        myListView.setAdapter(myAdapter);
        myNumTv.setText(myList.size()+"");
    }


    /**
     *  pos 位置  isAll 0 all 1 my
     * @param pos
     * @param isAll
     */
    @Override
    public void myDelete(int pos,int isAll) {
        //网络请求放弃任务
        //网络将该任务添加到放弃任务列表
        Toast.makeText(getContext(),"all....该任务已放弃",Toast.LENGTH_SHORT).show();
        if(isAll == 0) {
            allList.remove(pos);
            allAdapter.notifyDataSetChanged();
            allListView.slideBack();
            allNumTv.setText(allList.size()+"");
        }else
        {
            myList.remove(pos);
            myAdapter.notifyDataSetChanged();
            myListView.slideBack();
            myNumTv.setText(myList.size()+"");
        }
    }

    @Override
    public void myFinish(int pos,int isAll) {
        Toast.makeText(getContext(),"all....该任务已接受",Toast.LENGTH_SHORT).show();
        if(isAll == 0) {
            allList.remove(pos);
            allAdapter.notifyDataSetChanged();
            allListView.slideBack();
            allNumTv.setText(allList.size()+"");
        }else
        {
            myList.remove(pos);
            myAdapter.notifyDataSetChanged();
            myListView.slideBack();
            myNumTv.setText(myList.size()+"");
    }
}

    private static final int ALL_INVOLVE = 1111;
    private static final int MY_INVOLVE = 11112;
    private void setListener()
    {
        allListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("taskId",allList.get(i).getId());
                intent.putExtra("status",0);
                intent.putExtra("pos",i);
                getParentFragment().startActivityForResult(intent,ALL_INVOLVE);
            }
        });

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("taskId",myList.get(i).getId());
                intent.putExtra("pos",i);
                intent.putExtra("status",0);
                getParentFragment().startActivityForResult(intent,MY_INVOLVE);
            }
        });
    }


    //在Detail里面删除后刷新
    @Override
    public void myRefresh(int requestCode, Intent data) {
        int result = data.getExtras().getInt("reback");
        int pos = data.getExtras().getInt("pos");
        if(result >0)//delete
        {
            Toast.makeText(getContext(),"all....该任务已放弃",Toast.LENGTH_SHORT).show();
            if(requestCode == ALL_INVOLVE)
            {
                allList.remove(pos);
                allAdapter.notifyDataSetChanged();
                allNumTv.setText(allList.size()+"");
            }else if(requestCode == MY_INVOLVE)
            {
                myList.remove(pos);
                myAdapter.notifyDataSetChanged();
                myNumTv.setText(myList.size()+"");

            }
        }else if(result<0)//finish
        {
            Toast.makeText(getContext(),"all....该任务已接受",Toast.LENGTH_SHORT).show();
            if(requestCode == ALL_INVOLVE)
            {
                allList.remove(pos);
                allAdapter.notifyDataSetChanged();
                allNumTv.setText(allList.size()+"");
            }else if(requestCode == MY_INVOLVE)
            {
                myList.remove(pos);
                myAdapter.notifyDataSetChanged();
                myNumTv.setText(myList.size()+"");
            }
        }
    }

    //没有网络，用来测试的数据
    private void getDataFromLocal(int flag)
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
        if(flag == 0)
        {
            allList = JsonUtil.parseTaskJson(sb.toString());
            showAllListView(0);
        }else
        {
            myList = JsonUtil.parseTaskJson(sb.toString());
            showMyListView(0);
        }
    }

    @Override
    public void onRefresh() {
        setData();
    }

    private synchronized void showList()
    {
        if(isAll && isMy)
        {
            refreshLayout.setRefreshing(false);
            isAll = false;
            isAll = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshLayout.setRefreshing(false);
        MyApplication.getInstance().getMyQueue().cancelAll("getMyAcceptableTaskList");
        MyApplication.getInstance().getMyQueue().cancelAll("getAllAcceptableTaskList");
    }

}
