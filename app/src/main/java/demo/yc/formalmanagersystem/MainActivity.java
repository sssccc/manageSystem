package demo.yc.formalmanagersystem;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import demo.yc.formalmanagersystem.activity.BaseActivity;
import demo.yc.formalmanagersystem.activity.PropertyInfoActivity;
import demo.yc.formalmanagersystem.activity.SettingActivity;
import demo.yc.formalmanagersystem.activity.TimeManageActivity;
import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.fragment.HomePageFrag;
import demo.yc.formalmanagersystem.fragment.P_PropertyManagementFragment;
import demo.yc.formalmanagersystem.fragment.PersonInfoFrag;
import demo.yc.formalmanagersystem.fragment.TaskManageFrag;
import demo.yc.formalmanagersystem.fragment.Task_Involve_Frag;
import demo.yc.formalmanagersystem.models.FileInfo;
import demo.yc.formalmanagersystem.models.Person;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.models.Property;
import demo.yc.formalmanagersystem.models.Task;
import demo.yc.formalmanagersystem.services.DownLoadService;
import demo.yc.formalmanagersystem.util.ActivityCollector;
import demo.yc.formalmanagersystem.util.DateUtil;
import demo.yc.formalmanagersystem.util.FileUtil;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.PersonUtil;
import demo.yc.formalmanagersystem.util.QR_Util;
import demo.yc.formalmanagersystem.util.ThreadUtil;
import demo.yc.formalmanagersystem.util.ToastUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener, PersonInfoFrag.PersonListener {

    MyDBHandler db;

    String account = "";

    ImageView menuBtn;
    TextView title;

    View[] itemLayouts;

    CircleImageView personHead;
    TextView personName;
    TextView personPosition;

    DrawerLayout drawerLayout;
    View leftLayout, rightLayout;

    private LinearLayout qrLayout;

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment[] fragments;


    int currentPager = 0;
    long currentTime = 0;

    static boolean isFirst = true;
    String todayName;
    int todayCode = 0;
    Person person;
    ArrayList<Plan> todayPlanList = new ArrayList<>();

    View welcomeLayout;
    private static final int SEND_DATA_TO_TIME = 0x003;
    public static final int QR_SCAN = 0x005;

    ProgressDialog pd;
    View pullImage;

    boolean isPerson, isPlan;//isTask;


    ThreadUtil threadUtil = new ThreadUtil(this) {
        @Override
        public void error(int code) {
            switch (code) {
                case 1:
                    getPersonDataFromLocal();
                    break;
                case 4:
                    getPlanDataFromLocal();
                    break;
            }
        }

        @Override
        public void success(int code, Object obj) {
            switch (code) {
                case 1:
                    isPerson = true;
                    person = (Person) obj;
                    updatePersonInfo(person);
                    showMain();
                    break;
                case 4:
                    isPlan = true;
                    todayPlanList = (ArrayList<Plan>) obj;
                    if (isFirst) {
                        showMain();
                    } else {
                        ((HomePageFrag) fragments[1]).refreshTodayPlan();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.show();

        fm = getSupportFragmentManager();
        todayName = DateUtil.getTodayName();
        todayCode = DateUtil.getWeekDayCode(todayName);
        db = MyDBHandler.getInstance(this);
        setUi();
        getDataFromDB();
       // getPersonDataFromLocal();
       // getPlanDataFromLocal();
        setListener();
//
////        IntentFilter filter = new IntentFilter();
////        filter.addAction(DownLoadService.ACTION_CHANGE_IMAGE);
////        filter.addAction(DownLoadService.ACTION_PROPERTY_NOTIFY);
//        registerReceiver(mReceiver,filter);
    }

    //程序开始，为控件找到id，并且设置监听事件
    private void setUi() {
        qrLayout = (LinearLayout) findViewById(R.id.qr_scan);
        pullImage = findViewById(R.id.direction_in_top);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        leftLayout = drawerLayout.findViewById(R.id.left_layout_of_main);
        rightLayout = drawerLayout.findViewById(R.id.right_layout_of_main);
        welcomeLayout = findViewById(R.id.weelcome_layout);
        menuBtn = (ImageView) findViewById(R.id.top_layout_menu);
        title = (TextView) findViewById(R.id.top_layout_title);
        itemLayouts = new View[]
                {
                        findViewById(R.id.one_of_left_layout),
                        findViewById(R.id.two_of_left_layout),
                        findViewById(R.id.three_of_left_layout),
                        findViewById(R.id.four_of_left_layout),
                        findViewById(R.id.five_of_left_layout),
                        findViewById(R.id.six_of_left_layout)

                };

        personHead = (CircleImageView) findViewById(R.id.one_head_photo);
        personName = (TextView) findViewById(R.id.one_name);
        personPosition = (TextView) findViewById(R.id.one_position);


        fragments = new Fragment[]
                {
                        new PersonInfoFrag(),
                        new HomePageFrag(),
                        new TaskManageFrag(),
                        new P_PropertyManagementFragment()
                };

    }

    private void setListener() {
        for (int i = 0; i < itemLayouts.length; i++) {
            itemLayouts[i].setOnClickListener(this);
        }
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });
        qrLayout.setOnClickListener(this);
    }

    //给伟钊调用
    public void showMenu() {
        drawerLayout.openDrawer(leftLayout);
    }


    //然后进入数据库查找信息。。。找不到，进入后台访问。。。。
    private void getDataFromDB() {
        getPersonInfoFromHttp();
        getPlanInfoFromHttp();
    }


    private void getPersonInfoFromHttp() {


        //userId   回调接口
        Log.w("person", MyApplication.getUser().getId());
        new VolleyUtil().getPersonInfo(MyApplication.getUser().getId(), new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                //解析json
                Log.w("person", s);
                Person p = JsonUtil.parsePersonJson(s);
                if (p != null) {
                    MyApplication.setPersonId(p.getId());
                    if (FileUtil.isExternalStorageOk()) {
                        Intent intent = new Intent(DownLoadService.ACTION_DOWNLAOD_IMAGE);
                        FileInfo fileInfo = new FileInfo(MyApplication.getUser().getId(), p.getPicture());
                        intent.putExtra("image", fileInfo);
                        startService(intent);
                    }

//                    if(person == null)
//                        db.addPersonInfo(p);
//                    else
//                        db.updatePersonInfo(p);
                    updatePersonInfo(p);

                    if (!isPerson) {
                        isPerson = true;
                        showMain();
                    }
                } else {
                    getPersonDataFromLocal();
                }

            }

            @Override
            public void onError(VolleyError error) {
                Log.w("person", error.toString());
                getPersonDataFromLocal();
            }
        });
    }

    private void getPlanInfoFromHttp() {
        //星期一
        new VolleyUtil().getSingleDayPlan(MyApplication.getUser().getId(), todayCode, new UpdateListener() {
            @Override
            public void onSucceed(String s) {

                Log.w("plan", "main singleplan 返回数据" + s);


                if (s == null || !s.startsWith("[")) {
                    Log.w("plan", "main singleplan json格式错误:");
                }
                ArrayList<Plan> tempList = JsonUtil.parsePlanJson(s);
                if (tempList != null && tempList.size() != 0) {

                    if (todayPlanList.size() != 0)
                        todayPlanList.clear();

                    todayPlanList = tempList;
                    isPlan = true;
                    Log.w("plan", "main plan todayList.size-->" + todayPlanList.size());
                    showMain();
                } else {
                    getPlanDataFromLocal();
                }
            }

            @Override
            public void onError(VolleyError error) {
               // Toast.makeText(MainActivity.this, "plan...mainactivity..error", Toast.LENGTH_LONG).show();
                Log.w("plen", error.toString());
                getPlanDataFromLocal();
            }
        });
    }

    //然后，点击切换fragment，在fragment中调用下面几个方面，获取各自需要的信息
    //这个三个是分别传递给个人信息，任务信息 今日行程信息
    private void setFrags(int index) {
        pullImage.setVisibility(View.GONE);
        ft = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == index) {
                if (!fragments[i].isAdded())
                    ft.add(R.id.frame_layout_of_right, fragments[i]);
                else
                    ft.show(fragments[i]);
            } else {
                if (fragments[i].isAdded())
                    ft.hide(fragments[i]);
            }
        }

        ft.commit();
    }

    //设置不同的frag时，修改不同标题
    public void setMyTitle(String title) {
        this.title.setText(title);
    }

    //将该信息传递给个人信息界面
    public Person SendPersonInfo() {
        return this.person;
    }   //一个人的详细信息

    //将今天的行程安排呢传递给首页信息
    public ArrayList<Plan> sendPlanInfo() {
        return todayPlanList;
    }  //今天6节课的安排


    //获取到数据或要在主界面显示一部分数据
    // 或者 在某些frag中改变了某些数据，则调用对应的方法，在主界面中跟新数据
    @Override
    public void updatePersonInfo(Person p) {
        this.person = p;

        //修改头像,先加载本地图片，如果网络可用，就加载网络图片
        if (!MyApplication.getPersonHeadPath().equals(""))
            Glide.with(this).load(MyApplication.getPersonHeadPath()).into(personHead);
        else {
            String tempPath = FileUtil.getUserImagePath(MyApplication.getUser().getId());
            File file = new File(tempPath);
            if (file.exists())
                Glide.with(this).load(tempPath).into(personHead);
            else
                Glide.with(this).load(p.getPicture()).into(personHead);
        }
        MyApplication.setPersonName(person.getName());
        MyApplication.setPersonId(person.getId());
        personPosition.setText(PersonUtil.getPositonName(person.getQuartersId()));
        personName.setText(person.getName());
    }

    @Override
    public void updatePersonInfo() {

        Glide.with(MainActivity.this).load(MyApplication.getPersonHeadPath()).into(personHead);
    }

    //该方法是在课程表修改了今天的安排，在onactivityresult中调用。
    //因为在课表修改时，已经更新了数据库的数据，直接去数据库获取新数据就好
    private void updatePlanInfo() {
        getPlanInfoFromHttp();
    }

    //获取数据成功后显示界面
    public synchronized void showMain() {
        if (isPlan && isPerson) {
            if (!isFirst)
                ((HomePageFrag) fragments[1]).refreshTodayPlan();
//            Intent intent = new Intent(this,DownLoadService.class);
//            intent.setAction(DownLoadService.ACTION_PROPERTY_NOTIFY);
//            startService(intent);
            setFrags(1);
            pd.dismiss();
        }
    }

    //如果本地没有用户信息，并且无法通过网络加载出数据。则无法进行任何操作，提示刷新或者退出
    private AlertDialog dialog;

    public void showDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示")
                    .setMessage("连接出错了...")
                    .setPositiveButton("再试一次", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getPersonInfoFromHttp();
                            getPlanInfoFromHttp();
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCollector.finishAll();
                        }
                    })
                    .setCancelable(false);
            dialog = builder.create();
            dialog.show();
        } else
            dialog.show();

    }

    //点击事件
    @Override
    public void onClick(View view) {

        //跳转到设置界面
        if (view.getId() == R.id.six_of_left_layout) {
            Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(settingIntent);
            //跳转到课程表界面
        } else if (view.getId() == R.id.five_of_left_layout) {
            startIntetnToTimeManager();
            //跳转到二维码扫描界面
        } else if (view.getId() == R.id.qr_scan) {
            QR_Util.startQRScan(this);
        } else {
            switch (view.getId()) {
                //跳转到个人信息界面
                case R.id.one_of_left_layout:
                    currentPager = 0;
                    title.setText("个人信息");
                    break;
                //跳转到首页界面
                case R.id.two_of_left_layout:
                    currentPager = 1;
                    title.setText("首页");
                    break;
                //跳转到任务列表界面
                case R.id.three_of_left_layout:
                    currentPager = 2;
                    break;
                //跳转到资产管理界面
                case R.id.four_of_left_layout:
                    currentPager = 3;
                    break;
                default:
                    break;
            }
            setFrags(currentPager);
        }
        drawerLayout.closeDrawer(leftLayout);

    }

    //双击退出界面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(leftLayout))
                drawerLayout.closeDrawer(leftLayout);
            else {
                if (currentPager == 1) {
                    if (System.currentTimeMillis() - currentTime > 3000) {
                        Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        currentTime = System.currentTimeMillis();
                    } else
                        ActivityCollector.finishAll();
                } else {
                    setFrags(1);
                    currentPager = 1;
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startIntetnToTimeManager() {
        Intent timeIntent = new Intent(MainActivity.this, TimeManageActivity.class);
        startActivityForResult(timeIntent, SEND_DATA_TO_TIME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == SEND_DATA_TO_TIME) {
                isFirst = false;
                updatePlanInfo();
            }
            //处理二维码扫描
            else if (requestCode == QR_SCAN) {
                String result = data.getStringExtra("result");
                //解析二维码
                Property p = JsonUtil.parseQRCode(result);
                //解析错误
                if (p == null) {
                    ToastUtil.createToast(this, "解析错误！");
                }
                //解析成功，跳转至PropertyInfo Activity显示详情页面
                else {
                    Intent intent = new Intent(MainActivity.this, PropertyInfoActivity.class);
                    intent.putExtra("data_extra", p);
                    startActivity(intent);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
//                Toast.makeText(MainActivity.this,"MainActivity。。HomePager",Toast.LENGTH_LONG).show();
//               ((HomePageFrag)fragments[1]).myRefresh(requestCode,data);
            }
        }
    }


    //没有网络，用来测试的数据
    private void getPlanDataFromLocal() {

        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = getResources().getAssets().open("singleplan.txt");
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
        todayPlanList = JsonUtil.parsePlanJson(sb.toString());
        isPlan = true;
        showMain();
    }

    //没有网络，用来测试的数据
    private void getPersonDataFromLocal() {

        Person p = new Person(20, "2", "数学与信息", "软件工程", "heyongcai", "201430330210", "sdfsdf.jpg", "dfdf", "1", "1", account);
        updatePersonInfo(p);
        isPerson = true;
        showMain();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mReceiver);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(DownLoadService.ACTION_CHANGE_IMAGE)) {
                Log.w("file", "后台下载图片成功.......");
                updatePersonInfo();
            } else if (intent.getAction().equals(DownLoadService.ACTION_PROPERTY_NOTIFY)) {
                Log.w("file", "notify.......");
                String msg = intent.getStringExtra("messge");
                ((HomePageFrag) (fm.getFragments().get(1))).getNotifyDataFromMain(msg);
            } else if (intent.getAction().equals("")) {

            }
        }
    };

    public void updateHomePageTaskList(ArrayList<Task> newList)
    {
       ((HomePageFrag)fragments[1]).updateAllTaskList(newList);
    }
    public void updateHomePageTaskList(String taskId)
    {
        ((HomePageFrag)fragments[1]).updateSingleTaskList(taskId);
    }

    public void updateInvolveTaskList(String taskId)
    {
        ((Task_Involve_Frag)(((TaskManageFrag)fragments[2]).list.get(1))).updateTaskList(taskId);
    }
}
