package demo.yc.formalmanagersystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Task_Involve_Frag extends TaskBaseFrag {

    MySlideListView allListView,myListView;
    ScrollView scrollView;
    View view;

    ArrayList<Task> allList = new ArrayList<>();
    ArrayList<Task> myList = new ArrayList<>();
    MySlideListViewAdapter allAdapter,myAdapter;

    TextView allNumTv,myNumTv;
    public Task_Involve_Frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskManageFrag taskManageFrag = (TaskManageFrag) getParentFragment();
        taskManageFrag.setMyTitle("参与任务");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_involve, container, false);
        setUi();
        setData();
        setListener();
        return view;
    }
    private void setUi()
    {
        allNumTv = (TextView) view.findViewById(R.id.involve_all_list_num);
        myNumTv = (TextView) view.findViewById(R.id.involve_my_list_num);

        allListView = (MySlideListView) view.findViewById(R.id.task_involve_all_people_listView);
        myListView = (MySlideListView) view.findViewById(R.id.task_involve_my_group_listView);
        scrollView = (ScrollView) view.findViewById(R.id.task_involve_scrollView);
        scrollView.smoothScrollTo(0,0);
    }

    private void setData()
    {
        //获取所有人的
        new VolleyUtil().getAllInvolveTaskList(MyApplication.getUser().getId(), new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                Log.w("task","all involve = "+s);
                allList = JsonUtil.parseTaskJson(s);
                if(allList != null)
                    showMyListView(0);
                else
                    Toast.makeText(getContext(), "all involve nothing", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(VolleyError error) {
                getDataFromLocal(0);
            }
        });
         //获取我的任务
//        new VolleyUtil().getMyInvolveTaskList(MyApplication.getUser().getId(), new UpdateListener() {
//            @Override
//            public void onSucceed(String s) {
//                Log.w("task","my involve = "+s);
//                myList = JsonUtil.parseTaskJson(s);
//                if(myList != null)
//                    showMyListView(0);
//                else
//                    Toast.makeText(getContext(), "my involve nothing", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onError(VolleyError error) {
//            getDataFromLocal(1);
//        }
//    });

}

    private static final int ALL_INVOLVE = 1111;
    private static final int MY_INVOLVE = 1112;
    private void setListener()
    {
        allListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("taskId",allList.get(i).getId());
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
                getParentFragment().startActivityForResult(intent,MY_INVOLVE);
            }
        });
    }

    @Override
    public void myDelete(int pos,int flag) {
        //网络请求放弃任务
        //网络将该任务添加到放弃任务列表
        Toast.makeText(getContext(),"involve...该任务已放弃",Toast.LENGTH_SHORT).show();
        if(flag == -1) {
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
    public void myFinish(int pos,int flag) {
        Toast.makeText(getContext(),"involve...该任务已完成",Toast.LENGTH_SHORT).show();
        if(flag == -1) {
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


    //在detail 操作后，刷新
    @Override
    public void myRefresh(int requestCode, Intent data) {
        Toast.makeText(getContext(),"involve。。",Toast.LENGTH_SHORT).show();


            int result = data.getExtras().getInt("reback");
            int pos = data.getExtras().getInt("pos");

            Toast.makeText(getContext(),result+"involve。。"+pos,Toast.LENGTH_SHORT).show();

        if(result >0)//delete
            {
                Toast.makeText(getContext(),"involve...该任务已放弃",Toast.LENGTH_SHORT).show();

                if(requestCode == ALL_INVOLVE)
                {
                    allList.remove(pos);
                    allAdapter.notifyDataSetChanged();
                    allNumTv.setText(allList.size()+"");
                 //   allListView.slideBack();
                }else if(requestCode == MY_INVOLVE)
                {
                    myList.remove(pos);
                    myAdapter.notifyDataSetChanged();
                    myNumTv.setText(myList.size()+"");
                  //  myListView.slideBack();
                }
            }else if(result<0)//finish
            {
                Toast.makeText(getContext(),"involve...该任务已完成",Toast.LENGTH_SHORT).show();
                if(requestCode == ALL_INVOLVE)
                {
                    allList.remove(pos);
                    allAdapter.notifyDataSetChanged();
                    allNumTv.setText(allList.size()+"");
                 //   allListView.slideBack();
                }else if(requestCode == MY_INVOLVE)
                {
                    myList.remove(pos);
                    myAdapter.notifyDataSetChanged();
                    myNumTv.setText(myList.size()+"");
                   // myListView.slideBack();
                }
            }
        }


    //绑定dapter显示listView
    private void showAllListView(int flag)
    {
        //判断是否为空。。然后显示图片  还是listView
        allAdapter = new MySlideListViewAdapter(this,getContext(),allList,-1);
        allListView.setAdapter(allAdapter);
        allNumTv.setText(allList.size()+"");
    }
    private void showMyListView(int flag)
    {
        myAdapter = new MySlideListViewAdapter(this,getContext(),myList,-1);
        myListView.setAdapter(myAdapter);
        myNumTv.setText(myList.size()+"");

    }


    //网络请求数据失败，用于测试的数据
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
}
