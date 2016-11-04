package demo.yc.formalmanagersystem.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.activity.PurchaseDetailActivity;
import demo.yc.formalmanagersystem.activity.RepairDetailActivity;
import demo.yc.formalmanagersystem.adapter.MyAdapterForPurchase;
import demo.yc.formalmanagersystem.adapter.MyAdapterForRepair;
import demo.yc.formalmanagersystem.contentvalues.DBContent;
import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.models.Purchase;
import demo.yc.formalmanagersystem.models.Repair;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.ToastUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.RefreshableView;

/**
 * Created by Administrator on 2016/7/26.
 */
public class P_MyPropertyFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout direction;       //弹出选择框按钮
    private ImageView directionImg;     //选择框状态指示器
    private TextView hint;  //提示当前页面
    private boolean down;   //标志当前选择框的状态

    private LinearLayout backToTop; //返回顶部指示器
    private int position1 = 0;//记录第一行筛选位置

    private ListView listView;  //数据展示
    private MyAdapterForRepair myAdapterForRepair;
    private MyAdapterForPurchase myAdapterForPurchase;
    //所有资产
    private List<Purchase> purchases = new ArrayList<>();
    private List<Repair> repairs = new ArrayList<>();

    //根据第一二部分筛选
    private List<Purchase> pTemp2 = new ArrayList<>();

    //根据第一二部分筛选
    private List<Repair> rTemp2 = new ArrayList<>();

    //用于分类
    private PopupWindow popupwindow;
    private View customView;
    private TextView myPurchase;
    private TextView myRepair;
    private TextView allProperties;
    private TextView pass;
    private TextView refuse;
    private TextView toBeHandle;

    public ExecutorService executor;
    private VolleyUtil volleyUtil = new VolleyUtil();
    private int count = 0;  //用于标志初始化只进行一次

    private RefreshableView refreshableView;    //下拉刷新
    private ImageView top_layout_menu;      //侧滑菜单切换按钮

    //用于标志是否第一次在本地数据库无数据时自动进行首次网络加载
    private boolean pOnce = true;
    private boolean rOnce = true;

    //获取数据后，更新UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            P_PropertyManagementFragment.isInitial = true;
            //更新失败
            if (msg.what == 1) {
                if (getActivity() != null) {
                    ToastUtil.createToast(getActivity(), "更新失败，请重试！");
                }
            }
            //更新成功时(我的采购页面)
            else if (position1 == 0) {
                count++;
                doAfterAsyTask();
                purchases = (List<Purchase>) msg.obj;
                if (purchases.size() != 0) {
                    pTemp2.clear();
                    for (Purchase purchase : purchases
                            ) {
                        pTemp2.add(purchase);
                    }
                    if (pTemp2.size() != 0) {
                        if (getActivity() != null) {
                            myAdapterForPurchase = new MyAdapterForPurchase(getActivity(), R.layout.item, pTemp2);
                        }
                        if (myAdapterForPurchase != null) {
                            listView.setAdapter(myAdapterForPurchase);
                            myAdapterForPurchase.notifyDataSetChanged();
                        }
                    }

                }
                //本地数据库无数据，自动进行一次网络加载
                else {
                    if (pOnce) {
                        pOnce = false;
                        refreshData();
                    }
                }
            }
            //更新成功时(我的报修)
            else if (position1 == 1) {
                {
                    repairs = (List<Repair>) msg.obj;
                    rTemp2.clear();
                    for (Repair repair : repairs
                            ) {
                        rTemp2.add(repair);
                    }

                    if (rTemp2.size() > 0) {
                        if (getActivity() != null) {
                            myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, rTemp2);
                        }
                        if (myAdapterForRepair != null) {
                            listView.setAdapter(myAdapterForRepair);
                            myAdapterForRepair.notifyDataSetChanged();
                        }
                    }

                    //本地数据库无数据，自动进行一次网络加载
                    else {
                        if (rOnce) {
                            refreshData();
                            rOnce = false;
                        }
                    }
                }
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_property_in_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_in_my_property_fragment);
        backToTop = (LinearLayout) view.findViewById(R.id.back_to_top_in_my_property);
        refreshableView = (RefreshableView) view.findViewById(R.id.refresh_view_in_my_property);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        if (getActivity() != null) {
            //设置下拉刷新监听
            refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshData();
                }
            }, 2);
            direction = (RelativeLayout) getActivity().findViewById(R.id.direction_in_top);
            direction.setVisibility(View.VISIBLE);
            direction.setOnClickListener(this);
            top_layout_menu = (ImageView) getActivity().findViewById(R.id.top_layout_menu);
            top_layout_menu.setOnClickListener(this);
            directionImg = (ImageView) getActivity().findViewById(R.id.direction_img);
            hint = (TextView) getActivity().findViewById(R.id.text_view_in_top);
            hint.setVisibility(View.VISIBLE);
            hint.setText("采购");
            executor = Executors.newCachedThreadPool();
            readDataFromSQLite();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            initEvents();
        }
    }

    //从本地数据库读取数据
    private void readDataFromSQLite() {
        //更新采购表
        if (position1 == 0) {
            Runnable task1 = new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        SQLiteDatabase db = MyDBHandler.getInstance(getActivity()).getDBInstance();
                        Cursor cursor = db.query("Purchase", null, "createrIdentifier=?", new String[]{MyApplication.getUser().getUsername()}, null, null, "applyTime"+" desc", null);
                        purchases.clear();
                        if (cursor.moveToFirst()) {
                            Cursor personCursor = null;
                            do {
                                Purchase purchase = new Purchase();
                                purchase.setName(cursor.getString(cursor.getColumnIndex("name")));
                                purchase.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
                                purchase.setApplyTime(cursor.getString(cursor.getColumnIndex("applyTime")));
                                purchase.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
                                purchase.setModel(cursor.getString(cursor.getColumnIndex("model")));
                                purchase.setCheckState(cursor.getString(cursor.getColumnIndex("checkState")));
                                purchase.setCreaterIdentifier(cursor.getString(cursor.getColumnIndex("createrIdentifier")));
                                purchase.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                                purchase.setPurchaseState(cursor.getString(cursor.getColumnIndex("purchaseState")));
                                purchase.setFinishTime(cursor.getString(cursor.getColumnIndex("finishTime")));
                                try {
                                    personCursor = db.query(DBContent.TB_PERSON,null,"userId=?",new String[]{MyApplication.getPersonId()},null,null,null);
                                    if(personCursor.moveToFirst()){
                                        purchase.setCreaterName(personCursor.getString(personCursor.getColumnIndex("name")));
                                    }
                                } catch (Exception e) {
                                    Log.d("tag", "error occurred in query User");
                                } finally {
                                    if(personCursor != null){
                                        personCursor.close();
                                    }
                                }
                                purchases.add(purchase);
                            } while (cursor.moveToNext());
                            cursor.close();
                        }
                        Message msg = handler.obtainMessage();
                        msg.obj = purchases;
                        msg.what = 0;       //处理成功
                        handler.sendMessage(msg);
                    }

                }
            };
            executor.execute(task1);
        }
        //更新报修表
        else if (position1 == 1) {
            Runnable task2 = new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db = MyDBHandler.getInstance(getActivity()).getDBInstance();
                    Cursor cursor = db.query("Repair", null, "createrIdentifier=?", new String[]{MyApplication.getUser().getUsername()}, null, null,  "applyTime"+" desc", null);
                    repairs.clear();
                    if (cursor.moveToFirst()) {
                        Cursor propertyCursor;
                        Cursor personCursor;
                        do {
                            Repair repair = new Repair();
                            repair.setIdentifier(cursor.getString(cursor.getColumnIndex("identifier")));
                            repair.setApplyTime(cursor.getString(cursor.getColumnIndex("applyTime")));
                            repair.setFinishTime(cursor.getString(cursor.getColumnIndex("finishTime")));
                            repair.setDescribe("");
                            propertyCursor = db.query("Property", null, "identifier=?", new String[]{cursor.getString(cursor.getColumnIndex("identifier"))}, null, null, null, null);
                            propertyCursor.moveToFirst();
                            if (propertyCursor.moveToFirst()) {
                                repair.setName(propertyCursor.getString(propertyCursor.getColumnIndex("name")));
                            }
                            personCursor = db.query(DBContent.TB_PERSON,null,"userId=?",new String[]{MyApplication.getPersonId()},null,null,null);
                            if(personCursor.moveToFirst()){
                                repair.setCreaterName(personCursor.getString(personCursor.getColumnIndex("name")));
                            }
                            repair.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
                            repair.setCheckState(cursor.getString(cursor.getColumnIndex("checkState")));
                            repair.setRepairState(cursor.getString(cursor.getColumnIndex("repairState")));
                            if (cursor.getString(cursor.getColumnIndex("createrIdentifier")) != null) {
                                repair.setCreaterIdentifier(cursor.getString(cursor.getColumnIndex("createrIdentifier")));
                            } else repair.setCreaterIdentifier("");
                            repairs.add(repair);
                        } while (cursor.moveToNext());
                        cursor.close();
                        propertyCursor.close();
                        personCursor.close();
                        P_PropertyManagementFragment.isInitial = true;
                    }
                    Message msg = handler.obtainMessage();
                    msg.obj = repairs;
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            };
            executor.execute(task2);
        }

    }


    //注册事件
    private void initEvents() {
        backToTop.setOnClickListener(this);
    }

    private void doAfterAsyTask() {
        if (getActivity() != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position1 == 0) {
                        Purchase purchase = pTemp2.get(position);
                        PurchaseDetailActivity.startActivity(getActivity(), PurchaseDetailActivity.USER, purchase);
                    } else if (position1 == 1) {
                        Repair repair = rTemp2.get(position);
                        RepairDetailActivity.startActivity(getActivity(), RepairDetailActivity.USER, repair);
                    }
                }
            });
        }

        //设置滚动监听，当item数大于20时显示返回顶部指示器
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

        if (count == 1) {
            getPopupWindowView();
            resetTypeStyle();
            resetStatusStyle();
            //默认选中全部资产
            if (this.isAdded()) {
                if (position1 == 0) {
                    myPurchase.setAlpha(1);
                    myPurchase.setTextColor(Color.rgb(95, 187, 176));
                    myPurchase.setBackgroundResource(R.drawable.bg_border_green);
                } else if (position1 == 1) {
                    myRepair.setAlpha(1);
                    myRepair.setTextColor(Color.rgb(95, 187, 176));
                    myRepair.setBackgroundResource(R.drawable.bg_border_green);
                }
                //默认选中全部分类
                allProperties.setAlpha(1);
                allProperties.setTextColor(Color.rgb(95, 187, 176));
                allProperties.setBackgroundResource(R.drawable.bg_border_green);
            }
        }

    }


    //获取ListView拖动高度
    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //切换侧滑菜单
            case R.id.top_layout_menu:
                if (popupwindow != null) {
                    down = false;
                    directionImg.setImageResource(R.drawable.down);
                    popupwindow.dismiss();
                    listView.setAlpha(1);
                }
                ((MainActivity) getActivity()).showMenu();
                break;
            //选择框按钮
            case R.id.direction_in_top:
                //未弹出状态
                if (down == false) {
                    directionImg.setImageResource(R.drawable.up);
                    showPopupWindow();
                    listView.setAlpha(0.3f);
                    if (popupwindow != null) {
                        if (getActivity() != null) {
                            popupwindow.showAsDropDown(getActivity().findViewById(R.id.direction_img), 0, 0);
                        }
                    }
                    down = true;
                }
                //弹出状态
                else {
                    down = false;
                    directionImg.setImageResource(R.drawable.down);
                    if (popupwindow != null) {
                        directionImg.setImageResource(R.drawable.down);
                        popupwindow.dismiss();
                        listView.setAlpha(1);
                    }
                }
                break;

            //返回顶部
            case R.id.back_to_top_in_my_property:
                listView.smoothScrollByOffset(-getScrollY());
                break;

            //我的采购
            case R.id.my_purchase_in_drop_down_for_my_property:
                if (position1 == 0) {
                    break;
                }
                position1 = 0;
                pTemp2.clear();
                hint.setText("采购");

                pTemp2.addAll(purchases);
                myAdapterForPurchase = new MyAdapterForPurchase(getActivity(), R.layout.item, pTemp2);
                listView.setAdapter(myAdapterForPurchase);
                myAdapterForPurchase.notifyDataSetChanged();
                resetStatusStyle();
                resetTypeStyle();
                allProperties.setAlpha(1);
                allProperties.setTextColor(Color.rgb(95, 187, 176));
                allProperties.setBackgroundResource(R.drawable.bg_border_green);
                myPurchase.setAlpha(1);
                myPurchase.setTextColor(Color.rgb(95, 187, 176));
                myPurchase.setBackgroundResource(R.drawable.bg_border_green);
                break;

            //我的报修
            case R.id.my_repair_in_drop_down_for_my_property:
                if (position1 == 1) {
                    break;
                }
                position1 = 1;
                hint.setText("报修");
                readDataFromSQLite();

                resetTypeStyle();
                resetStatusStyle();
                allProperties.setAlpha(1);
                allProperties.setTextColor(Color.rgb(95, 187, 176));
                allProperties.setBackgroundResource(R.drawable.bg_border_green);
                myRepair.setAlpha(1);
                myRepair.setTextColor(Color.rgb(95, 187, 176));
                myRepair.setBackgroundResource(R.drawable.bg_border_green);
                break;

            //全部
            case R.id.all_status_in_drop_down_for_my_property:
                if (position1 == 0) {
                    pTemp2.clear();
                    for (Purchase purchase : purchases) {
                        pTemp2.add(purchase);
                    }
                    myAdapterForPurchase = new MyAdapterForPurchase(getActivity(), R.layout.item, pTemp2);
                    listView.setAdapter(myAdapterForPurchase);
                    myAdapterForPurchase.notifyDataSetChanged();
                } else if (position1 == 1) {
                    rTemp2.clear();
                    for (Repair repair : repairs) {
                        rTemp2.add(repair);
                    }
                    myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, rTemp2);
                    listView.setAdapter(myAdapterForRepair);
                    myAdapterForRepair.notifyDataSetChanged();
                }
                resetTypeStyle();
                allProperties.setAlpha(1);
                allProperties.setTextColor(Color.rgb(95, 187, 176));
                allProperties.setBackgroundResource(R.drawable.bg_border_green);
                break;

            //审核通过
            case R.id.pass_in_drop_down_for_my_property:
                if (position1 == 0) {
                    pTemp2.clear();
                    for (Purchase purchase : purchases) {
                        if (purchase.getCheckState().equals("通过"))
                            pTemp2.add(purchase);
                    }
                    myAdapterForPurchase = new MyAdapterForPurchase(getActivity(), R.layout.item, pTemp2);
                    listView.setAdapter(myAdapterForPurchase);
                    myAdapterForPurchase.notifyDataSetChanged();

                } else if (position1 == 1) {
                    rTemp2.clear();
                    for (Repair repair : repairs) {
                        if (repair.getCheckState().equals("通过"))
                            rTemp2.add(repair);
                    }
                    myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, rTemp2);
                    listView.setAdapter(myAdapterForRepair);
                    myAdapterForRepair.notifyDataSetChanged();
                }
                resetTypeStyle();
                pass.setAlpha(1);
                pass.setTextColor(Color.rgb(95, 187, 176));
                pass.setBackgroundResource(R.drawable.bg_border_green);
                break;

            //审核未通过
            case R.id.refuse_in_drop_down_for_my_property:
                pTemp2.clear();
                rTemp2.clear();

                if (position1 == 0) {
                    for (Purchase purchase : purchases) {
                        if (purchase.getCheckState().equals("拒绝"))
                            pTemp2.add(purchase);
                    }
                    myAdapterForPurchase = new MyAdapterForPurchase(getActivity(), R.layout.item, pTemp2);
                    listView.setAdapter(myAdapterForPurchase);
                    myAdapterForPurchase.notifyDataSetChanged();

                } else if (position1 == 1) {
                    for (Repair repair : repairs) {
                        if (repair.getCheckState().equals("拒绝"))
                            rTemp2.add(repair);
                    }
                    myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, rTemp2);
                    listView.setAdapter(myAdapterForRepair);
                    myAdapterForRepair.notifyDataSetChanged();
                }
                resetTypeStyle();
                refuse.setAlpha(1);
                refuse.setTextColor(Color.rgb(95, 187, 176));
                refuse.setBackgroundResource(R.drawable.bg_border_green);
                break;

            //待审核
            case R.id.to_be_review_in_drop_down_for_my_property:
                pTemp2.clear();
                rTemp2.clear();

                if (position1 == 0) {
                    for (Purchase purchase : purchases) {
                        if (purchase.getCheckState().equals("待审核"))
                            pTemp2.add(purchase);
                    }
                    myAdapterForPurchase = new MyAdapterForPurchase(getActivity(), R.layout.item, pTemp2);
                    listView.setAdapter(myAdapterForPurchase);
                    myAdapterForPurchase.notifyDataSetChanged();

                } else if (position1 == 1) {
                    for (Repair repair : repairs) {
                        if (repair.getCheckState().equals("待审核"))
                            rTemp2.add(repair);
                    }
                    myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, rTemp2);
                    listView.setAdapter(myAdapterForRepair);
                    myAdapterForRepair.notifyDataSetChanged();
                }
                resetTypeStyle();
                toBeHandle.setAlpha(1);
                toBeHandle.setTextColor(Color.rgb(95, 187, 176));
                toBeHandle.setBackgroundResource(R.drawable.bg_border_green);
                break;
        }
    }

    //重置资产状态分类的样式
    public void resetStatusStyle() {
        if (this.isAdded()) {
            myRepair.setTextColor(Color.rgb(90, 90, 90));
            myPurchase.setTextColor(Color.rgb(90, 90, 90));
            myRepair.setAlpha(0.5f);
            myPurchase.setAlpha(0.5f);
            myRepair.setBackgroundResource(R.drawable.bg_border_gray);
            myPurchase.setBackgroundResource(R.drawable.bg_border_gray);
        }

    }

    //重置资产分类的样式
    public void resetTypeStyle() {
        if (this.isAdded()) {
            //设置默认文字颜色
            allProperties.setTextColor(Color.rgb(90, 90, 90));
            pass.setTextColor(Color.rgb(90, 90, 90));
            refuse.setTextColor(Color.rgb(90, 90, 90));
            toBeHandle.setTextColor(Color.rgb(90, 90, 90));
            //设置默认透明度
            allProperties.setAlpha(0.5f);
            pass.setAlpha(0.5f);
            refuse.setAlpha(0.5f);
            toBeHandle.setAlpha(0.5f);
            //设置默认边框颜色
            allProperties.setBackgroundResource(R.drawable.bg_border_gray);
            pass.setBackgroundResource(R.drawable.bg_border_gray);
            refuse.setBackgroundResource(R.drawable.bg_border_gray);
            toBeHandle.setBackgroundResource(R.drawable.bg_border_gray);
        }

    }

    private void getPopupWindowView() {
        // 获取自定义布局文件pop.xml的视图
        if (getActivity() != null) {
            customView = getActivity().getLayoutInflater().inflate(R.layout.drop_down_layout_for_my_property,
                    null, false);
            //获取控件
            myPurchase = (TextView) customView.findViewById(R.id.my_purchase_in_drop_down_for_my_property);
            myRepair = (TextView) customView.findViewById(R.id.my_repair_in_drop_down_for_my_property);
            pass = (TextView) customView.findViewById(R.id.pass_in_drop_down_for_my_property);
            refuse = (TextView) customView.findViewById(R.id.refuse_in_drop_down_for_my_property);
            toBeHandle = (TextView) customView.findViewById(R.id.to_be_review_in_drop_down_for_my_property);
            allProperties = (TextView) customView.findViewById(R.id.all_status_in_drop_down_for_my_property);

            // 自定义view添加触摸事件
            customView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popupwindow != null && popupwindow.isShowing()) {
                        directionImg.setImageResource(R.drawable.down);
                        popupwindow.dismiss();
                        listView.setAlpha(1);
                        popupwindow = null;
                    }

                    return false;
                }
            });
            //注册事件
            myPurchase.setOnClickListener(this);
            myRepair.setOnClickListener(this);
            pass.setOnClickListener(this);
            refuse.setOnClickListener(this);
            toBeHandle.setOnClickListener(this);
            allProperties.setOnClickListener(this);
        }

    }

    private void showPopupWindow() {
        // 创建PopupWindow实例
        if (popupwindow == null) {
            popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    /***
     * 从服务器更新数据
     */
    public void refreshData() {
        if (getActivity() != null) {
            //更新purchase数据
            if (position1 == 0) {
                volleyUtil.updateSQLiteFromMySql("purchase", new UpdateListener() {
                    @Override
                    public void onSucceed(String s) {
                        refreshableView.finishRefreshing("my_property");
                        if (s.contains("error-business")) {
                            onError(new VolleyError("error-business"));
                        } else {
                            ToastUtil.createToast(getActivity(), "更新成功！");
                            //解析Json格式的数据
                            List<Purchase> lists = JsonUtil.parsePurchaseJson(s);
                            //更新本地数据库
                            MyDBHandler.getInstance(getActivity()).updatePurchase(getActivity(), lists);
                            //从本地数据库获取数据，更新界面
                            readDataFromSQLite();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        refreshableView.finishRefreshing("my_property");
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                });
            }
            //更新repair数据
            else if (position1 == 1) {
                volleyUtil.updateSQLiteFromMySql("repair", new UpdateListener() {
                    @Override
                    public void onSucceed(String s) {
                        ToastUtil.createToast(getActivity(), "更新成功！");
                        refreshableView.finishRefreshing("my_property");
                        if (s.contains("error-business")) {
                            onError(new VolleyError("error-business"));
                        } else {
                            ToastUtil.createToast(getActivity(), "更新成功！");
                            //解析Json格式数据
                            List<Repair> lists = JsonUtil.parseRepairJson(s);
                            //更新本地数据库
                            MyDBHandler.getInstance(getActivity()).updateRepair(getActivity(), lists);
                            //从本地数据库获取数据，更新界面
                            readDataFromSQLite();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        refreshableView.finishRefreshing("my_property");
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });
            }
        }
    }
}
