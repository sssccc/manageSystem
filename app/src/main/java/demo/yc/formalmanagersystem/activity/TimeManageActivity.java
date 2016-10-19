package demo.yc.formalmanagersystem.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.adapter.MyTimeGridViewAdapter;
import demo.yc.formalmanagersystem.adapter.ViewPagerAdapter;
import demo.yc.formalmanagersystem.contentvalues.TimePlanContent;
import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.fragment.DayBaseFrag;
import demo.yc.formalmanagersystem.fragment.Day_Frag_FriDay;
import demo.yc.formalmanagersystem.fragment.Day_Frag_MonDay;
import demo.yc.formalmanagersystem.fragment.Day_Frag_SatDay;
import demo.yc.formalmanagersystem.fragment.Day_Frag_SunDay;
import demo.yc.formalmanagersystem.fragment.Day_Frag_ThuDay;
import demo.yc.formalmanagersystem.fragment.Day_Frag_TueDay;
import demo.yc.formalmanagersystem.fragment.Day_Frag_WedDay;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.util.DateUtil;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.NetWorkUtil;
import demo.yc.formalmanagersystem.util.ThreadUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.MyLineView;


//先调用本地内容，然后后台更新内容，
public class TimeManageActivity extends BaseActivity implements View.OnClickListener{



    RadioButton[]  btns;
    Button typeBtn;
    TextView dayTv;
    String today;
    ImageButton menuBtn,backBtn;
    MyLineView line;
    ViewPager viewPager;
    RadioGroup radioGroup;
    int currentPage = 0;
    View dayLayout,weekLayout;
    AlertDialog.Builder builder;

    ArrayList<DayBaseFrag> frags = new ArrayList<>();
    ViewPagerAdapter adapter;

    ArrayList<Plan> planTitles = new ArrayList<>();
    ArrayList<Plan> weekPlans = new ArrayList<>();
    GridView gridView;
    MyTimeGridViewAdapter gridViewAdapter;
    boolean isTodayPlanChanges = false;

    View popView;
    ListView listView;
    PopupWindow popupWindow;

    Intent lastIntent ;
    //这两个参数需要保存。
    int refreshType = 0;
    boolean isDay = true;

    String account = "";
    MyDBHandler db;

    boolean isLoad = false;

    ProgressDialog pd;
    ProgressBar freshPd;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0x111) {
                setUi();
                setData();
                setColor(currentPage);
                setListener();
                pd.dismiss();
                getPlanInfoFromHttp();
            }
        }
    };

    ThreadUtil threadUtil = new ThreadUtil(this) {
        @Override
        public void error(int code) {
            isLoad = false;
            getPlanInfoFromHttp();
        }

        @Override
        public void success(int code, Object obj) {
            planTitles = (ArrayList<Plan>)obj;
            isLoad = true;
            freshPd.setVisibility(View.VISIBLE);
            getPlanInfoFromHttp();
            handler.sendEmptyMessage(0x111);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_manage);
        db = MyDBHandler.getInstance(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.show();
        calculateDay();
        freshPd = (ProgressBar) findViewById(R.id.time_manage_fresh_pd);
        line = (MyLineView) findViewById(R.id.time_manage_line);
        line.setTabNum(7);
        line.setCurrentNum(currentPage);
        lastIntent = getIntent();
        account = lastIntent.getStringExtra("account");
        threadUtil.getPlanInfo(account);

    }

    private void setUi() {
        btns = new RadioButton[]{
                (RadioButton) findViewById(R.id.time_manage_mon_btn),
                (RadioButton) findViewById(R.id.time_manage_tue_btn),
                (RadioButton) findViewById(R.id.time_manage_wed_btn),
                (RadioButton) findViewById(R.id.time_manage_thu_btn),
                (RadioButton) findViewById(R.id.time_manage_fri_btn),
                (RadioButton) findViewById(R.id.time_manage_sat_btn),
                (RadioButton) findViewById(R.id.time_manage_sun_btn)
        };
        backBtn = (ImageButton) findViewById(R.id.time_manage_back_btn);
        dayTv = (TextView) findViewById(R.id.time_manage_day_tv);
        typeBtn = (Button) findViewById(R.id.time_manage_type_btn);
        menuBtn = (ImageButton) findViewById(R.id.time_manage_menu_btn);
        viewPager = (ViewPager) findViewById(R.id.time_manage_view_pager);
        radioGroup = (RadioGroup) findViewById(R.id.time_manage_radio_group);
        dayLayout = findViewById(R.id.time_manage_day_layout);
        weekLayout = findViewById(R.id.time_manage_week_layout);
        gridView = (GridView) findViewById(R.id.time_manage_gridView);
        line.setVisibility(View.VISIBLE);

    }

    private void setData()
    {
        frags.add(new Day_Frag_MonDay());
        frags.add(new Day_Frag_TueDay());
        frags.add(new Day_Frag_WedDay());
        frags.add(new Day_Frag_ThuDay());
        frags.add(new Day_Frag_FriDay());
        frags.add(new Day_Frag_SatDay());
        frags.add(new Day_Frag_SunDay());
        if(!isDay)
        {
            showWeekPlan(refreshType);
        }
        dayTv.setText(today);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),frags);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPage);
        viewPager.setOffscreenPageLimit(6);
    }

    private void setListener()
    {
        backBtn.setOnClickListener(this);
        typeBtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.time_manage_mon_btn:
                        currentPage = 0;
                        break;
                    case R.id.time_manage_tue_btn:
                        currentPage = 1;
                        break;
                    case R.id.time_manage_wed_btn:
                        currentPage = 2;
                        break;
                    case R.id.time_manage_thu_btn:
                        currentPage = 3;
                        break;
                    case R.id.time_manage_fri_btn:
                        currentPage = 4;
                        break;
                    case R.id.time_manage_sat_btn:
                        currentPage = 5;
                        break;
                    case R.id.time_manage_sun_btn:
                        currentPage = 6;
                        break;
                }
                viewPager.setCurrentItem(currentPage);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                line.setOffSet(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                line.setCurrentNum(currentPage);
                setColor(currentPage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //viewPager.setPageTransformer(true,new MyPageTransformer());
    }
    private void setColor(int pos)
    {
        for(int i=0;i<btns.length;i++)
        {
            if (i == pos)
            {
                btns[i].setTextColor(Color.BLACK);
            }else
            {
                btns[i].setTextColor(Color.GRAY);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.time_manage_back_btn:
                if(isTodayPlanChanges)
                {
                    setResult(RESULT_OK);
                }else
                {
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
            case R.id.time_manage_menu_btn:
                if(popupWindow == null)
                    initPopupWindow(view);
                else
                    popupWindow.showAsDropDown(view,-20,5);
                break;
            case R.id.time_manage_type_btn:
                if(isDay)
                {
                    typeBtn.setText("日");
                    isDay = false;
                    showWeekPlan(refreshType);
                }else
                {
                    typeBtn.setText("周");
                    isDay = true;
                    weekLayout.setVisibility(View.GONE);
                    dayLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {

            if(!isLoad)
            {
                pd.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            }
            if(isTodayPlanChanges)
            {
                setResult(RESULT_OK);
            }else
            {
                setResult(RESULT_CANCELED);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //初始化popwindow
    private void initPopupWindow(View view)
    {
        popView = LayoutInflater.from(this).inflate(R.layout.time_manage_popup_window,null,false);
        listView = (ListView) popView.findViewById(R.id.popup_listview);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xFF000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.showAsDropDown(view,-20,5);

        ArrayList<HashMap<String,String>> list = new ArrayList<>();

        HashMap<String,String> map1 = new HashMap<>();
        HashMap<String,String> map2 = new HashMap<>();
        HashMap<String,String> map3 = new HashMap<>();
        map1.put("time","显示所有时间");
        list.add(map1);
        map2.put("time","显示空闲时间");
        list.add(map2);
        map3.put("time","显示工作时间");
        list.add(map3);
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.popup_list_item,new String[]{"time"},new int[]{R.id.popup_listview_item_title});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(refreshType != i) {
                    refreshPlanList(i);
                    refreshType = i;
                    gridViewAdapter = new MyTimeGridViewAdapter(TimeManageActivity.this, weekPlans,refreshType);
                    gridView.setAdapter(gridViewAdapter);
                }
                popupWindow.dismiss();
            }
        });
    }

    //计算几天是星期几
    private void calculateDay() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd EEEE");
        today = format.format(date);
        currentPage = (DateUtil.getWeekDayCode(today)-1);
    }

    //根据popwindow 显示行程安排后刷新
    public void refreshPlanList(int type)
    {
        for(DayBaseFrag f: frags)
        {
            f.myRefresh(type);
        }
    }

    //修改行程安排后刷新
    public void refreshPlanList(Plan p)
    {
        //对p进行上传处理
        frags.get(p.getWeekDay()-1).onResume();
    }

    //点击了周日按钮，用来显示不同布局内容。
    private void showWeekPlan(int type)
    {
        //getAllDayPlan();
        if(weekPlans.size() == 0)
        {
            for(int i=0;i<6;i++)
            {
                weekPlans.add(planTitles.get(i));
                weekPlans.add(planTitles.get(i+6));
                weekPlans.add(planTitles.get(i+12));
                weekPlans.add(planTitles.get(i+18));
                weekPlans.add(planTitles.get(i+24));
                weekPlans.add(planTitles.get(i+30));
                weekPlans.add(planTitles.get(i+36));
            }
        }
        if(gridViewAdapter == null) {
            gridViewAdapter = new MyTimeGridViewAdapter(this, weekPlans, type);
            gridView.setAdapter(gridViewAdapter);
        }
        weekLayout.setVisibility(View.VISIBLE);
        dayLayout.setVisibility(View.GONE);

    }

    //给各个子fragment 传送信息
    public ArrayList<Plan> sendPlanInfo(int weeK_day)
    {
        ArrayList<Plan> pp = new ArrayList<>();
        for(int i=0;i<planTitles.size();i+=6)
        {
            if(planTitles.get(i).getWeekDay() == weeK_day)
            {
                pp.add(planTitles.get(i));
                pp.add(planTitles.get(i+1));
                pp.add(planTitles.get(i+2));
                pp.add(planTitles.get(i+3));
                pp.add(planTitles.get(i+4));
                pp.add(planTitles.get(i+5));
                return pp;
            }
        }
        return pp;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == TimePlanContent.UPDATE_PLAN_INFO_CODE)
            {
                Plan p = (Plan)data.getSerializableExtra(TimePlanContent.UPDATE_PLAN_INFO_TAG);
                if(p.getWeekDay() == DateUtil.getWeekDayCode(today))
                {
                    isTodayPlanChanges = true;
                }
                for(int i=0;0<planTitles.size();)
                {
                    Plan pp = planTitles.get(i);
                    if(pp.getWeekDay() == p.getWeekDay())
                    {
                        if(pp.getDayTime() == p.getDayTime())
                        {
                            planTitles.get(i).setIsFixed(p.getIsFixed());
                            planTitles.get(i).setIsFree(p.getIsFree());
                            planTitles.get(i).setType(p.getType());
                            planTitles.get(i).setTitle(p.getTitle());
                            planTitles.get(i).setContent(p.getContent());
                            break;
                        }else
                        {
                            i++;
                        }
                    }else
                    {
                        i+=6;
                    }
                }
                refreshPlanList(p);
                gridViewAdapter.notifyDataSetChanged();
               // Toast.makeText(this,p.title+p.day_time,Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //从后台获取数据
    private void getPlanInfoFromHttp()
    {
        if(NetWorkUtil.getInstance(TimeManageActivity.this).isWork()) {
            new VolleyUtil().getAllDayPlan(MyApplication.getUser().getId(), new UpdateListener() {
                @Override
                public void onSucceed(String s) {
                    ArrayList<Plan> tempList = JsonUtil.parsePlanJson(s);
                    //获取网络数据失败
                    freshPd.setVisibility(View.INVISIBLE);
                    if(tempList == null || tempList.size()==0)
                    {
                        //如果数据库已有内容，则不做处理
                        //否则，进入测试数据
                        if(!isLoad)
                        {
                            Toast.makeText(TimeManageActivity.this, "数据获取失败，插入测试数据", Toast.LENGTH_SHORT).show();
                            getDateFromLocal();
                        }
                    }else // 获取网络数据成功
                    {
                        planTitles = tempList;
                        //原本已经加载了本地数据，需要对数据进行更新
                        if(isLoad)
                        {

                        }else   //获取网络数据前没有本地数据，直接显示布局。
                        {
                            pd.dismiss();
                            isLoad = true;
                            handler.sendEmptyMessage(0x111);
                        }
                    }
                }
                @Override
                public void onError(VolleyError error) {
                    //Log.w("plan","planall error = "+error.toString());
                    freshPd.setVisibility(View.INVISIBLE);
                    if (isLoad) {
                        Toast.makeText(TimeManageActivity.this, "数据更新失败", Toast.LENGTH_LONG).show();
                    } else {
                       getDateFromLocal();
                    }
                    // if(planTitles.size() != 0)
                }
            });
        }else
        {
            freshPd.setVisibility(View.INVISIBLE);
            //网络无法使用，并且没有加载本地数据，进去数据测试阶段
            if(!isLoad)
            {
               getDateFromLocal();
            }
            //Toast.makeText(TimeManageActivity.this,"网络无法使用...",Toast.LENGTH_SHORT).show();
        }

    }

    private void showDialog(String message)
    {
        if (builder != null) {
            builder.setMessage(message);
            builder.show();
        } else {
            builder = new AlertDialog.Builder(TimeManageActivity.this);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("刷新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pd.show();
                            getPlanInfoFromHttp();
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }).create().show();
        }
    }


    private void getDateFromLocal()
    {
        StringBuffer sb =new StringBuffer();
        try {
            InputStream is = getResources().getAssets().open("plan.txt");
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
        planTitles = JsonUtil.parsePlanJson(sb.toString());
        pd.dismiss();
        isLoad = true;
        handler.sendEmptyMessage(0x111);
    }

}
