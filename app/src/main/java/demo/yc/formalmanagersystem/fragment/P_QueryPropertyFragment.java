package demo.yc.formalmanagersystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.activity.PropertyInfoActivity;
import demo.yc.formalmanagersystem.adapter.MyAdapterForProperty;
import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.models.Property;
import demo.yc.formalmanagersystem.models.Purchase;
import demo.yc.formalmanagersystem.models.Repair;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.RefreshableView;

/**
 * Created by Administrator on 2016/7/26.
 */
public class P_QueryPropertyFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout topBar;
    private ImageView top_layout_menu;
    private LinearLayout backToTop;

    //本地数据库为空时，只进行一次的服务器访问
    private boolean once = true;

    private LinearLayout parent;

    private int position1;//记录第一行筛选位置
    private int position2;//记录第二行筛选位置
    private String[] types = {"全部分类", "电脑", "打印机", "投影仪", "其他"};
    private ImageView search;   //搜索框
    private EditText input;     //输入框
    private ListView listView;  //数据展示
    private MyAdapterForProperty allPropertyAdapter;
    //所有资产
    private List<Property> properties = new ArrayList<>();
    //根据第一部分筛选
    private List<Property> temp1 = new ArrayList<>();
    //根据第一二部分筛选
    private List<Property> temp2 = new ArrayList<>();
    //用于搜索
    private List<Property> temp3 = new ArrayList<>();

    private boolean down = false;
    //用于分类
    private ImageView direction;
    private PopupWindow popupwindow;
    private TextView allProperty;
    private TextView toBeRepair;
    private TextView borrowedProperty;
    private TextView allType;
    private TextView computer;
    private TextView printer;
    private TextView presenter;
    private TextView other;
    private View customView;
    private boolean flag;
    float dY = 0;
    float uY = 0;

    private RefreshableView refreshableView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            P_PropertyManagementFragment.isInitial = true;
            executor.shutdownNow();
            //更新失败时
            if (msg.what == 1) {
                Log.d("myTag", "failure");
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "更新失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            }
            //更新成功时
            else {
                properties = (List<Property>) msg.obj;
                if (properties.size() != 0) {
                    Log.d("myTag", "success");
                    for (Property property : properties
                            ) {
                        temp2.add(property);
                    }
                    if (temp2.size() != 0) {
                        if (getActivity() != null) {    //快速切换时候，新开的线程中Fragment的Activity还没Created
                            allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                        }
                        if (allPropertyAdapter != null) {
                            listView.setAdapter(allPropertyAdapter);
                            allPropertyAdapter.notifyDataSetChanged();
                        }
                    }
                    if (flag) {
                        flag = false;
                        Toast.makeText(getActivity(), "更新成功！", Toast.LENGTH_SHORT).show();
                    }
                    doAfterAsyTask();
                } else {
                    if (once) {
                        refreshData();
                        once = false;
                    }
                }
            }
        }
    };
    private VolleyUtil volleyUtil = new VolleyUtil();
    public static ExecutorService executor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.query_property_layout, container, false);
        input = (EditText) view.findViewById(R.id.input);
        search = (ImageView) view.findViewById(R.id.search);
        listView = (ListView) view.findViewById(R.id.list_view_in_query);
        parent = (LinearLayout) view.findViewById(R.id.parent);
        direction = (ImageView) view.findViewById(R.id.direction);
        backToTop = (LinearLayout) view.findViewById(R.id.back_to_top_in_query);
        refreshableView = (RefreshableView) view.findViewById(R.id.refresh_view_in_query_property);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        //new MyQueryAsynTask().execute();
        List<Repair> repairs = new ArrayList<>();
       /* for (int i = 0; i < 100; i++) {
            Repair repair = new Repair("20160802001", "神舟笔记本", "2016/08/02", "2016/08/02", "通过", "已维修", "硬盘坏了", "201430340601", "曹伟钊");
            repairs.add(repair);
        }
        */
        MyDBHandler.getInstance(getActivity()).updateRepair(getActivity(), repairs);

        List<Purchase> purchases = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Purchase purchase = new Purchase("神舟笔记本","2016/08/09","2016/08/10","通过","已购买","购买测试","admin","曹伟钊","神舟","K620c");
            purchases.add(purchase);
        }
        MyDBHandler.getInstance(getActivity()).updatePurchase(getActivity(), purchases);

        List<Property> p = new ArrayList<>();
        for (int i = 0; i < 99; i++) {
            Property property = new Property("神舟笔记本", "电脑", "2016/08/02", "20160802001", false, "K620c", "战神", "5200", "神舟供应商", "13560321319", "已修理");
            p.add(property);
        }

        MyDBHandler.getInstance(getActivity()).updateProperty(getActivity(), p);

        if (getActivity() != null) {
            refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshData();
                }
            }, 3);
            top_layout_menu = (ImageView)getActivity().findViewById(R.id.top_layout_menu);
            top_layout_menu.setOnClickListener(this);
            topBar = (RelativeLayout)getActivity().findViewById(R.id.top_layout_root);
            topBar.setOnClickListener(this);
            getActivity().findViewById(R.id.direction_in_top).setVisibility(View.GONE);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            initEvents();
            input.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
                        input.setFocusable(false);
                        Toast.makeText(MyApplication.getContext(),"ddddddd",Toast.LENGTH_SHORT).show();
                        Log.w("aaa","ddddddddddd");
                        return true;
                    }
                    Log.w("aaa","ffffffff");
                    return false;
                }
            });
            input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        input.setHint("");
                    } else {
                        View view = getActivity().getWindow().peekDecorView();
                        if (view != null) {
                            InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        input.setHint("Search");
                    }
                }
            });
            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(input.getText())) {
                        doSearch();
                    } else {
                    /*allPropertyAdapter = new MyAdapterForProperty(QueryPropertyActivity.this, R.layout.item, temp3);
                    listView.setAdapter(allPropertyAdapter);
                    allPropertyAdapter.notifyDataSetChanged();*/
                        temp3.clear();
                        temp3.addAll(properties);
                        MyAdapterForProperty myAdapterForProperty = new MyAdapterForProperty(getActivity(), R.layout.item, temp3);
                        allProperty.setAlpha(1);
                        allProperty.setTextColor(Color.rgb(95, 187, 176));
                        allProperty.setBackgroundResource(R.drawable.bg_border_green);
                        allType.setAlpha(1);
                        allType.setTextColor(Color.rgb(95, 187, 176));
                        allType.setBackgroundResource(R.drawable.bg_border_green);
                        position1 = position2 = 0;
                        listView.setAdapter(myAdapterForProperty);
                        myAdapterForProperty.notifyDataSetChanged();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            executor = Executors.newFixedThreadPool(2);
            //从本地数据库读取数据
            readDataFromSQLite();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //P_PropertyManagementFragment.isInitial = true;
    }

    //注册事件
    private void initEvents() {
        backToTop.setOnClickListener(this);
        search.setOnClickListener(this);
        direction.setOnClickListener(this);
        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                input.clearFocus();
                return false;
            }
        });

    }

    //读取数据后的初始化
    private void doAfterAsyTask() {
        if (getActivity() != null) {
            temp3 = new ArrayList<>(properties);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Property property = temp2.get(position);
                    Intent intent = new Intent(getActivity(), PropertyInfoActivity.class);
                    intent.putExtra("data_extra", property);
                    startActivityForResult(intent, 0);
                }
            });
            /*listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dY = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            uY = event.getY();
                            if (getScrollY() == 0) {
                                if ((uY - dY) > 180) {
                                    flag = true;
                                    refreshData();
                                }
                            }
                            break;
                    }
                    return false;
                }
            });*/
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem > 20) {
                        backToTop.setVisibility(View.VISIBLE);
                    } else backToTop.setVisibility(View.INVISIBLE);
                }
            });

            getPopupWindowView();
            resetTypeStyle();
            resetStatusStyle();

            //默认选中全部资产与全部分类
            allProperty.setAlpha(1);
            allProperty.setTextColor(Color.rgb(95, 187, 176));
            allProperty.setBackgroundResource(R.drawable.bg_border_green);
            allType.setAlpha(1);
            allType.setTextColor(Color.rgb(95, 187, 176));
            allType.setBackgroundResource(R.drawable.bg_border_green);
            position1 = position2 = 0;

        }
    }

    //搜索
    private void doSearch() {
        if (!TextUtils.isEmpty(input.getText())) {
            temp3.clear();
            for (Property property : properties
                    ) {
                if ((property.getName().contains(input.getText().toString()))
                        || (property.getCate().contains(input.getText().toString()))
                        || (property.getBrand().contains(input.getText().toString()))
                        || (property.getProvider().contains(input.getText().toString()))
                        || (property.getIdentifier().contains(input.getText().toString()))) {
                    temp3.add(property);
                }
            }
        }
        if (temp3 != null) {
            if (getActivity() != null) {
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp3);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.top_layout_menu:

                ((MainActivity)getActivity()).showMenu();
            case R.id.top_layout_root:
                input.clearFocus();

                if (popupwindow != null) {
                    down = false;
                    direction.setImageResource(R.drawable.down);
                    popupwindow.dismiss();
                    listView.setAlpha(1);
                }
                break;
            case R.id.back_to_top_in_query:
                listView.smoothScrollByOffset(-getScrollY());
                break;
            case R.id.search:
                doSearch();
                break;
            case R.id.direction:
                if (down) {
                    down = false;
                    direction.setImageResource(R.drawable.down);
                    if (popupwindow != null) {
                        popupwindow.dismiss();
                        listView.setAlpha(1);
                        return;
                    }
                } else {
                    down = true;
                    direction.setImageResource(R.drawable.up);
                    showPopupWindow();
                    listView.setAlpha(0.3f);
                    if (popupwindow != null) {
                        popupwindow.showAsDropDown(v, 0, 30);
                    }
                }
                break;
            //全部资产
            case R.id.all_property_in_drop_down:
                position1 = 0;
                temp1.clear();
                temp2.clear();
                for (Property property : temp3
                        ) {
                    temp1.add(property);
                }

                if (types[position2].equals("全部分类")) {
                    for (Property property : temp1
                            ) {
                        temp2.add(property);
                    }
                } else {
                    for (Property property : temp1) {
                        if (property.getCate().equals(types[position2])) {
                            temp2.add(property);
                        }
                    }
                }
                temp1.clear();
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();
                resetStatusStyle();
                allProperty.setAlpha(1);
                allProperty.setTextColor(Color.rgb(95, 187, 176));
                allProperty.setBackgroundResource(R.drawable.bg_border_green);
                break;
            //待维修资产
            case R.id.to_be_repair_in_drop_down:

                position1 = 1;
                temp1.clear();
                temp2.clear();
                for (Property property : temp3) {
                    if (property.getRepairStatus().equals("待维修")) {
                        temp1.add(property);
                    }
                }


                if (types[position2].equals("全部分类")) {
                    for (Property property : temp1
                            ) {
                        temp2.add(property);
                    }
                } else {
                    for (Property property : temp1) {
                        if (property.getCate().equals(types[position2])) {
                            temp2.add(property);
                        }
                    }
                }

                temp1.clear();
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();

                resetStatusStyle();
                toBeRepair.setAlpha(1);
                toBeRepair.setTextColor(Color.rgb(95, 187, 176));
                toBeRepair.setBackgroundResource(R.drawable.bg_border_green);

                break;
            //借入资产
            case R.id.borrowed_in_drop_down:

                position1 = 2;
                temp1.clear();
                temp2.clear();
                //筛选借入资产
                for (Property property : temp3) {
                    if (property.isBorrowedProperty()) {
                        temp1.add(property);
                    }
                }
                if (types[position2].equals("全部分类")) {
                    for (Property property : temp1
                            ) {
                        temp2.add(property);
                    }
                } else for (Property property : temp1) {
                    if (property.getCate().equals(types[position2])) {
                        temp2.add(property);
                    }
                }

                temp1.clear();
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();

                resetStatusStyle();
                borrowedProperty.setAlpha(1);
                borrowedProperty.setTextColor(Color.rgb(95, 187, 176));
                borrowedProperty.setBackgroundResource(R.drawable.bg_border_green);
                break;

            //所有分类
            case R.id.all_type_in_drop_down:

                position2 = 0;
                temp1.clear();
                temp2.clear();
                for (Property property : temp3
                        ) {
                    temp1.add(property);
                }
                //全部资产+全部分类
                if (position1 == 0) {
                    for (Property property : temp1) {
                        temp2.add(property);
                    }
                }
                //待维修+全部分类
                else if (position1 == 1) {
                    for (Property property : temp1) {
                        if (property.getRepairStatus().equals("待维修")) {
                            temp2.add(property);
                        }
                    }
                } else if (position1 == 2) {
                    for (Property property : temp1) {
                        if (property.isBorrowedProperty()) {
                            temp2.add(property);
                        }
                    }
                }

                temp1.clear();
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();

                resetTypeStyle();
                allType.setAlpha(1);
                allType.setTextColor(Color.rgb(95, 187, 176));
                allType.setBackgroundResource(R.drawable.bg_border_green);
                break;
            //电脑
            case R.id.computer_in_drop_down:

                position2 = 1;
                temp1.clear();
                temp2.clear();
                for (Property property : temp3) {
                    if (property.getCate().equals("电脑")) {
                        temp1.add(property);
                    }
                }
                //全部资产+电脑类
                if (position1 == 0) {
                    for (Property property : temp1) {
                        temp2.add(property);
                    }
                }
                //待维修+电脑类
                else if (position1 == 1) {
                    for (Property property : temp1) {
                        if (property.getRepairStatus().equals("待维修")) {
                            temp2.add(property);
                        }
                    }
                } else if (position1 == 2) {
                    for (Property property : temp1) {
                        if (property.isBorrowedProperty()) {
                            temp2.add(property);
                        }
                    }
                }

                temp1.clear();
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();

                resetTypeStyle();
                computer.setAlpha(1);
                computer.setTextColor(Color.rgb(95, 187, 176));
                computer.setBackgroundResource(R.drawable.bg_border_green);
                break;
            //打印机
            case R.id.printer_in_drop_down:

                position2 = 2;
                temp1.clear();
                temp2.clear();
                for (Property property : temp3) {
                    if (property.getCate().equals("打印机")) {
                        temp1.add(property);
                    }
                }
                //全部资产+电脑类
                if (position1 == 0) {
                    for (Property property : temp1) {
                        temp2.add(property);
                    }
                }
                //待维修+电脑类
                else if (position1 == 1) {
                    for (Property property : temp1) {
                        if (property.getRepairStatus().equals("待维修")) {
                            temp2.add(property);
                        }
                    }
                } else if (position1 == 2) {
                    for (Property property : temp1) {
                        if (property.isBorrowedProperty()) {
                            temp2.add(property);
                        }
                    }
                }

                temp1.clear();
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();

                resetTypeStyle();
                printer.setAlpha(1);
                printer.setTextColor(Color.rgb(95, 187, 176));
                printer.setBackgroundResource(R.drawable.bg_border_green);
                break;
            //其他
            case R.id.other_in_drop_down:

                position2 = 4;
                temp1.clear();
                temp2.clear();
                for (Property property : temp3) {
                    if (property.getCate().equals("其他")) {
                        temp1.add(property);
                    }
                }
                //全部资产+电脑类
                if (position1 == 0) {
                    for (Property property : temp1) {
                        temp2.add(property);
                    }
                }
                //待维修+电脑类
                else if (position1 == 1) {
                    for (Property property : temp1) {
                        if (property.getRepairStatus().equals("待维修")) {
                            temp2.add(property);
                        }
                    }
                } else if (position1 == 2) {
                    for (Property property : temp1) {
                        if (property.isBorrowedProperty()) {
                            temp2.add(property);
                        }
                    }
                }

                temp1.clear();
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();
                resetTypeStyle();
                other.setAlpha(1);
                other.setTextColor(Color.rgb(95, 187, 176));
                other.setBackgroundResource(R.drawable.bg_border_green);
                break;
            //投影仪
            case R.id.presenter_in_drop_down:

                position2 = 3;
                temp1.clear();
                temp2.clear();
                for (Property property : temp3) {
                    if (property.getCate().equals("投影仪")) {
                        temp1.add(property);
                    }
                }
                //全部资产+电脑类
                if (position1 == 0) {
                    for (Property property : temp1) {
                        temp2.add(property);
                    }
                }
                //待维修+电脑类
                else if (position1 == 1) {
                    for (Property property : temp1) {
                        if (property.getRepairStatus().equals("待维修")) {
                            temp2.add(property);
                        }
                    }
                } else if (position1 == 2) {
                    for (Property property : temp1) {
                        if (property.isBorrowedProperty()) {
                            temp2.add(property);
                        }
                    }
                }
                temp1.clear();
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPropertyAdapter);
                allPropertyAdapter.notifyDataSetChanged();
                resetTypeStyle();
                presenter.setAlpha(1);
                presenter.setTextColor(Color.rgb(95, 187, 176));
                presenter.setBackgroundResource(R.drawable.bg_border_green);
                break;

        }
    }

    //获取listview的滚动高度
    private int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    //重置资产状态分类的样式
    public void resetStatusStyle() {
        allProperty.setTextColor(Color.rgb(90, 90, 90));
        toBeRepair.setTextColor(Color.rgb(90, 90, 90));
        borrowedProperty.setTextColor(Color.rgb(90, 90, 90));
        allProperty.setAlpha(0.5f);
        toBeRepair.setAlpha(0.5f);
        borrowedProperty.setAlpha(0.5f);
        allProperty.setBackgroundResource(R.drawable.bg_border_gray);
        toBeRepair.setBackgroundResource(R.drawable.bg_border_gray);
        borrowedProperty.setBackgroundResource(R.drawable.bg_border_gray);
    }

    //重置资产分类的样式
    public void resetTypeStyle() {
        allType.setTextColor(Color.rgb(90, 90, 90));
        computer.setTextColor(Color.rgb(90, 90, 90));
        printer.setTextColor(Color.rgb(90, 90, 90));
        presenter.setTextColor(Color.rgb(90, 90, 90));
        other.setTextColor(Color.rgb(90, 90, 90));
        other.setAlpha(0.5f);
        computer.setAlpha(0.5f);
        allType.setAlpha(0.5f);
        presenter.setAlpha(0.5f);
        printer.setAlpha(0.5f);
        allType.setBackgroundResource(R.drawable.bg_border_gray);
        computer.setBackgroundResource(R.drawable.bg_border_gray);
        printer.setBackgroundResource(R.drawable.bg_border_gray);
        presenter.setBackgroundResource(R.drawable.bg_border_gray);
        other.setBackgroundResource(R.drawable.bg_border_gray);
    }

    private void getPopupWindowView() {
        // 获取自定义布局文件pop.xml的视图
        if (getActivity() != null) {
            customView = getActivity().getLayoutInflater().inflate(R.layout.drop_down_layout,
                    null, false);
            //获取控件
            allProperty = (TextView) customView.findViewById(R.id.all_property_in_drop_down);
            toBeRepair = (TextView) customView.findViewById(R.id.to_be_repair_in_drop_down);
            borrowedProperty = (TextView) customView.findViewById(R.id.borrowed_in_drop_down);
            allType = (TextView) customView.findViewById(R.id.all_type_in_drop_down);
            computer = (TextView) customView.findViewById(R.id.computer_in_drop_down);
            printer = (TextView) customView.findViewById(R.id.printer_in_drop_down);
            presenter = (TextView) customView.findViewById(R.id.presenter_in_drop_down);
            other = (TextView) customView.findViewById(R.id.other_in_drop_down);

            // 自定义view添加触摸事件
            customView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popupwindow != null && popupwindow.isShowing()) {
                        popupwindow.dismiss();
                        listView.setAlpha(1);
                        direction.setImageResource(R.drawable.down);
                        popupwindow = null;
                    }

                    return false;
                }
            });

            //注册事件
            allProperty.setOnClickListener(this);
            toBeRepair.setOnClickListener(this);
            borrowedProperty.setOnClickListener(this);
            allType.setOnClickListener(this);
            computer.setOnClickListener(this);
            printer.setOnClickListener(this);
            presenter.setOnClickListener(this);
            other.setOnClickListener(this);
        }
    }

    private void showPopupWindow() {
        // 创建PopupWindow实例,200,150分别是宽度和高度
        if (popupwindow == null) {
            if (customView != null) {
                popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
    }

    /*class MyQueryAsynTask extends AsyncTask<Void, Void, List<Property>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Property> doInBackground(Void... params) {

            //进行数据库查询
            properties.add(new Property("华硕", "电脑", "2016/7/23", "201412", false, "k620c", "神舟笔记本", "5999", "神舟", "13560321319", "无"));
            return properties;
        }

        @Override
        protected void onPostExecute(List<Property> properties) {
            super.onPostExecute(properties);
            if (getActivity() != null) {
                allPropertyAdapter = new MyAdapterForProperty(getActivity(), R.layout.item, properties);
                doAfterAsyTask();
            }
        }

    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                //数据库发生变化，重新更新数据
                if (resultCode == Activity.RESULT_OK) {
                    refreshData();
                }
                break;
        }
    }

    //从服务器中获取数据，并更新到本地服务器
    private void refreshData() {
        if (getActivity() != null) {
            //访问服务器数据
            volleyUtil.updateSQLiteFromMySql("property", new UpdateListener() {
                @Override
                public void onSucceed(String s) {
                    refreshableView.finishRefreshing("query_property");
                    Log.d("myTag", s);
                    //服务器错误
                    if (s.contains("error-business")) {
                        onError(new VolleyError(s));
                    } else {
                        //从服务器获取数据
                        JsonUtil.parsePropertyJson(getActivity(), s);
                        //从本地数据库获取数据，更新界面
                        readDataFromSQLite();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    refreshableView.finishRefreshing("query_property");
                    Log.d("myTag", error.toString());
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            });
        }
    }

    //从本地数据库获取数据
    private void readDataFromSQLite() {
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = MyDBHandler.getInstance(getActivity()).getDBInstance();
                Cursor cursor = db.query("Property", null, null, null, null, null, null, null);
                properties.clear();
                temp2.clear();
                if (cursor.moveToFirst()) {
                    Cursor repairCursor = null;
                    do {
                        Property property = new Property();
                        property.setName(cursor.getString(cursor.getColumnIndex("name")));
                        property.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                        property.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
                        property.setCate(cursor.getString(cursor.getColumnIndex("cate")));
                        property.setIdentifier(cursor.getString(cursor.getColumnIndex("identifier")));
                        property.setModel(cursor.getString(cursor.getColumnIndex("model")));
                        property.setProvider(cursor.getString(cursor.getColumnIndex("provider")));
                        property.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        property.setProviderTel(cursor.getString(cursor.getColumnIndex("providerTel")));
                        try {
                            repairCursor = db.query("Repair", null, "identifier=?", new String[]{cursor.getString(cursor.getColumnIndex("identifier"))}, null, null, null, null);
                            if (repairCursor.moveToFirst()) {
                                String repairState = repairCursor.getString(repairCursor.getColumnIndex("repairState"));
                                if (!TextUtils.isEmpty(repairState)) {
                                    property.setRepairStatus(repairState);
                                }
                            } else {
                                property.setRepairStatus("无");
                            }
                        } catch (Exception e) {
                            Log.d("tag", "error occurred in query Repair");
                        } finally {
                            repairCursor.close();
                        }
                        property.setBorrowedProperty(cursor.getInt(cursor.getColumnIndex("isBorrowedProperty")) == 0 ? false : true);
                        properties.add(property);
                    } while (cursor.moveToNext());
                    cursor.close();
                }

                //P_PropertyManagementFragment.isInitial = true;
                //读取完成，通知handler更新
                Message msg = handler.obtainMessage();
                msg.obj = properties;
                msg.what = 0;
                handler.sendMessage(msg);
            }
        };
        executor.execute(task);
    }
}
