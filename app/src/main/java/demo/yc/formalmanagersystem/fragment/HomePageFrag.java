package demo.yc.formalmanagersystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.activity.TaskDetailActivity;
import demo.yc.formalmanagersystem.adapter.MySlideListViewAdapter;
import demo.yc.formalmanagersystem.adapter.ViewPagerAdapter;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.models.Task;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.MyPagerAnimation;
import demo.yc.formalmanagersystem.view.MySlideListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFrag extends TaskBaseFrag {

    ViewPager pager;
    ArrayList<DayBaseFrag> frags = new ArrayList<>();
    ViewPagerAdapter viewPagerAdapter;
    ScrollView scrollView ;
    MySlideListView listView ;
    MySlideListViewAdapter listViewAdapter;
    ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<Plan> todayPlan;

    View refreshPd;

    View view;
    TextView numTv;

    View notifyLayout;
    TextView notifyMessage;
    ImageView notifyDelete;

    View nodataLayout ;
    Button nodateBtn ;

    public HomePageFrag() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home_page,container, false);
        setUi();
        getTaskInfoFromHttp();
        todayPlan = ((MainActivity)getActivity()).sendPlanInfo();
        setPagerAdapter();
        setListener();
        return  view;
    }


    //设置ui
    private void setUi()
    {
        frags.add(new Time_Frag_1_2());
        frags.add(new Time_Frag_3_4());
        frags.add(new Time_Frag_5_6());
        frags.add(new Time_Frag_7_8());
        frags.add(new Time_Frag_9_10());
        frags.add(new Time_Frag_11_12_13());

        refreshPd =  view.findViewById(R.id.home_frag_task_fresh_pd);
        nodataLayout = view.findViewById(R.id.home_frag_nodata_layout);
        nodateBtn = (Button) view.findViewById(R.id.home_frag_nodata_btn);

        pager = (ViewPager) view.findViewById(R.id.viewpager);
        pager.setPageTransformer(true,new MyPagerAnimation());
        pager.setOffscreenPageLimit(6);


        listView = (MySlideListView)view.findViewById(R.id.mainListView);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            listView.initSlideMode(3);
        }

        scrollView = (ScrollView) view.findViewById(R.id.main_scrollView);
        scrollView.smoothScrollTo(0,0);

        numTv = (TextView) view.findViewById(R.id.home_frag_list_num);

        notifyDelete = (ImageView) view.findViewById(R.id.notify_delete_btn);
        notifyLayout = view.findViewById(R.id.notify_layout);
        notifyMessage = (TextView) view.findViewById(R.id.notify_message);

    }

    //网络请求task数据
    private void getTaskInfoFromHttp() {

        Log.w("task","homePage:"+MyApplication.getPersonId());
        new VolleyUtil().getMyInvolveTaskList(MyApplication.getUser().getId(),new UpdateListener() {

            @Override
            public void onSucceed(String s) {
                Log.w("task","honepage访问后台服务器成功:"+s);
                refreshPd.setVisibility(View.GONE);
                if( s == null || !s.startsWith("[")) {
                    Log.w("task","后台服务器返回数据异常");
                    Toast.makeText(getContext(), "获取数据异常", Toast.LENGTH_SHORT).show();
                    nodataLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    return;
                }
                taskList = JsonUtil.parseTaskJson(s);
                if(taskList != null) {
                    showListView();
                }
                else
                {
                    Log.w("task","json task wei 空");
                    //Toast.makeText(getContext(), "暂无数据。。。", Toast.LENGTH_SHORT).show();
                    notifyLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onError(VolleyError error) {
                //Toast.makeText(getContext(), "网络连接失败。。。", Toast.LENGTH_SHORT).show();
                refreshPd.setVisibility(View.GONE);
                nodataLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                Log.w("task","homepage访问后台服务器失败："+error.toString());
            }
        });

    }


    //设置每节课的page
    private void setPagerAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), frags);
        pager.setAdapter(viewPagerAdapter);
        pager.setCurrentItem(2);

    }

    //绑定数据到 listView
    private void showListView()
    {
        notifyLayout.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        listViewAdapter = new MySlideListViewAdapter(this,getContext(),taskList,1);
        listView.setAdapter(listViewAdapter);
        numTv.setText(taskList.size()+"");
    }

    //点击事件
    private void setListener()
    {
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("taskId",taskList.get(position).getId());
                intent.putExtra("status",1);
                intent.putExtra("pos",position);
                startActivityForResult(intent,100);
            }
        });

        notifyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyLayout.setVisibility(View.GONE);
            }
        });

        nodateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshPd.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                nodataLayout.setVisibility(View.GONE);
                getTaskInfoFromHttp();
            }
        });

    }

    //获取今日行程安排，已经在主界面加载
    public void refreshTodayPlan()
    {
        todayPlan = ((MainActivity)getActivity()).sendPlanInfo();
        Log.w("plan","子frag  todayPlan"+todayPlan.size());
      //  Toast.makeText(getContext(),"已经传送过去了3333。。",Toast.LENGTH_LONG).show();
        for(int i =0;i<todayPlan.size();i++)
        {
            frags.get(i).onResume();
        }
    }

    //给今日的每节课发送数据
    public Plan sendTodayPlan(int dayTime)
    {
        for(Plan p :todayPlan)
        {
            if(p.getDayTime() == dayTime)
                return p;
        }
       return null;
    }

    @Override
    public void myDelete(int pos, int flag)
    {
        Toast.makeText(getContext(),"homePager...该任务已放弃",Toast.LENGTH_SHORT).show();
        ((MainActivity)getActivity()).updateInvolveTaskList(taskList.get(pos).getId());
        taskList.remove(pos);
        listViewAdapter.notifyDataSetChanged();
        listView.slideBack();
        numTv.setText(taskList.size()+"");
    }

    @Override
    public void myFinish(int pos, int flag) {
        Toast.makeText(getContext(),"homePager...该任务已完成",Toast.LENGTH_SHORT).show();
        taskList.remove(pos);
        listViewAdapter.notifyDataSetChanged();
        listView.slideBack();
        numTv.setText(taskList.size()+"");
    }

    @Override
    public void myRefresh(int requestCode, Intent intent)
    {
        int result = intent.getExtras().getInt("reback");
        int pos = intent.getExtras().getInt("pos");
        if(result >0)//delete
        {
            Toast.makeText(getContext(),"homePager...该任务已放弃",Toast.LENGTH_SHORT).show();
            taskList.remove(pos);
            listViewAdapter.notifyDataSetChanged();
            listView.slideBack();
            numTv.setText(taskList.size()+"");
        }else if(result<0)//finish
        {
            Toast.makeText(getContext(),"homePager...该任务已完成",Toast.LENGTH_SHORT).show();
            taskList.remove(pos);
            listViewAdapter.notifyDataSetChanged();
            numTv.setText(taskList.size()+"");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
            myRefresh(requestCode,data);
    }

    //没有网络，用来测试的数据
    private void getDataFromLocal()
    {
        Toast.makeText(getContext(), "进到测试内容。。。", Toast.LENGTH_SHORT).show();
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
        taskList = JsonUtil.parseTaskJson(sb.toString());
        showListView();
    }


    public void getNotifyDataFromMain(String msg)
    {
        Log.w("task","收到来自服务的通知");
        notifyLayout.setVisibility(View.VISIBLE);
        notifyMessage.setText(msg);
    }

    public void updateAllTaskList(ArrayList<Task> newList)
    {
        taskList = newList;
        listViewAdapter.notifyDataSetChanged();
    }
    public void updateSingleTaskList(String taskId)
    {
        for(Task t : taskList)
        {
            if(t.getId().equals(taskId))
            {
                taskList.remove(t);
                break;
            }
        }
        listViewAdapter.notifyDataSetChanged();
    }

}

