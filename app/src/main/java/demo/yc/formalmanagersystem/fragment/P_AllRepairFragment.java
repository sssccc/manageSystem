package demo.yc.formalmanagersystem.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.activity.RepairDetailActivity;
import demo.yc.formalmanagersystem.adapter.MyAdapterForRepair;
import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.models.Repair;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.ToastUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.RefreshableView;

/**
 * Created by Administrator on 2016/7/26.
 */
public class P_AllRepairFragment extends Fragment implements View.OnClickListener {

    private ImageView top_layout_menu;  //切换菜单按钮
    private boolean once = true;    //确保本地数据库无数据时，只自动进行一次服务器访问
    private List<Repair> repairs = new ArrayList<>();//全部报修
    private ListView listView;
    private MyAdapterForRepair myAdapterForRepair;

    //线程池执行从本地数据库读取数据的任务
    public static ExecutorService executor;
    //用于筛选
    private List<Repair> temp2 = new ArrayList<>();

    //用于分类
    private PopupWindow popupwindow;
    private TextView allPurchase;
    private TextView pass;
    private TextView refuse;
    private View customView;
    private TextView toBeHandle;

    private LinearLayout backToTop;     //返回顶部
    private RelativeLayout direction;  //弹出选择框按钮
    private ImageView directionImg; //指示选择框的状态
    private boolean down;   //标志当前选择框的状态
    private VolleyUtil volleyUtil = new VolleyUtil();
    private RefreshableView refreshableView;    //下拉刷新

    //当数据更新后，更新UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            P_PropertyManagementFragment.isInitial = true;
            executor.shutdownNow();
            //更新出错时
            if (msg.what == 1) {
                if (getActivity() != null) {
                    ToastUtil.createToast(getActivity(),"更新失败，请重试！");
                }
            }
            //更新成功时
            else {
                repairs = (List<Repair>) msg.obj;
                //本地有数据
                if (repairs.size() != 0) {
                    for (Repair repair : repairs
                            ) {
                        temp2.add(repair);
                    }
                    if (temp2.size() != 0) {
                        if (getActivity() != null) {    //快速切换时候，新开的线程中Fragment的Activity还没Created
                            myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, temp2);
                        }
                        if (myAdapterForRepair != null) {
                            listView.setAdapter(myAdapterForRepair);
                            myAdapterForRepair.notifyDataSetChanged();
                        }
                    }
                    doAfterAsyTask();
                }
                //本地无数据，首次会自动进行一次网络加载
                else {
                    if (once) {
                        once = false;
                        refreshData();
                    }
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_repair_in_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_in_all_repair_fragment);
        refreshableView = (RefreshableView) view.findViewById(R.id.refresh_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            executor = Executors.newFixedThreadPool(2);
            readDataFromSQLite();
            //设置下拉刷新监听
            refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshData();
                }
            }, 0);
            top_layout_menu =(ImageView) getActivity().findViewById(R.id.top_layout_menu);
            top_layout_menu.setOnClickListener(this);
            direction = (RelativeLayout) getActivity().findViewById(R.id.direction_in_top);
            direction.setOnClickListener(this);
            directionImg = (ImageView) getActivity().findViewById(R.id.direction_img);
            myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, repairs);
        }
    }

    //从本地数据库读取数据
    private void readDataFromSQLite() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = MyDBHandler.getInstance(getActivity()).getDBInstance();
                Cursor cursor = db.query("Repair", null, null, null, null, null, null, null);
                repairs.clear();
                temp2.clear();
                if (cursor.moveToFirst()) {
                    Cursor propertyCursor = null;
                    do {
                        Repair repair = new Repair();
                        repair.setIdentifier(cursor.getString(cursor.getColumnIndex("identifier")));
                        repair.setApplyTime(cursor.getString(cursor.getColumnIndex("applyTime")));
                        repair.setFinishTime(cursor.getString(cursor.getColumnIndex("finishTime")));
                        try {
                            propertyCursor = db.query("Property", null, "identifier=?", new String[]{cursor.getString(cursor.getColumnIndex("identifier"))}, null, null, null, null);
                            propertyCursor.moveToFirst();
                            if (propertyCursor.moveToFirst()) {
                                repair.setName(propertyCursor.getString(propertyCursor.getColumnIndex("name")));
                            }
                        } catch (Exception e) {
                            repair.setName("未知");
                        } finally {
                            propertyCursor.close();
                        }
                        repair.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
                        repair.setCheckState(cursor.getString(cursor.getColumnIndex("checkState")));
                        repair.setRepairState(cursor.getString(cursor.getColumnIndex("repairState")));
                        repair.setCreaterIdentifier(cursor.getString(cursor.getColumnIndex("createrIdentifier")));
                        repairs.add(repair);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                //更新成功，发送通知更新UI
                Message msg = handler.obtainMessage();
                msg.obj = repairs;
                msg.what = 0;
                handler.sendMessage(msg);
            }
        };
        executor.execute(task);
    }

    //初始化ListView
    private void initListView() {
        if (getActivity() != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RepairDetailActivity.startActivity(getActivity(), RepairDetailActivity.USER, temp2.get(position));
                }
            });

            //设置滚动监听，当item数大于20时，显示返回顶部指示器
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
        }
    }

    //事件注册
    private void initEvents() {
        if (getActivity() != null) {
            allPurchase.setOnClickListener(this);
            pass.setOnClickListener(this);
            refuse.setOnClickListener(this);
            toBeHandle.setOnClickListener(this);
            backToTop.setOnClickListener(this);
        }
    }

    //获取listView拖动高度
    public int getScrollY() {
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
        if (getActivity() != null) {
            //设置字体默认颜色
            toBeHandle.setTextColor(Color.rgb(90, 90, 90));
            allPurchase.setTextColor(Color.rgb(90, 90, 90));
            pass.setTextColor(Color.rgb(90, 90, 90));
            refuse.setTextColor(Color.rgb(90, 90, 90));

            //设置默认透明度
            toBeHandle.setAlpha(0.5f);
            allPurchase.setAlpha(0.5f);
            pass.setAlpha(0.5f);
            refuse.setAlpha(0.5f);

            //设置默认边框颜色
            toBeHandle.setBackgroundResource(R.drawable.bg_border_gray);
            allPurchase.setBackgroundResource(R.drawable.bg_border_gray);
            pass.setBackgroundResource(R.drawable.bg_border_gray);
            refuse.setBackgroundResource(R.drawable.bg_border_gray);
        }
    }

    //获取下拉分类框
    private void getPopupWindowView() {
        // 获取自定义布局文件pop.xml的视图
        if (getActivity() != null) {
            customView = getActivity().getLayoutInflater().inflate(R.layout.drop_down_layout_for_all_repair,
                    null, false);
            //获取控件
            allPurchase = (TextView) customView.findViewById(R.id.all_repair_in_drop_down_for_all_repair);
            pass = (TextView) customView.findViewById(R.id.pass_in_drop_down_for_all_repair);
            refuse = (TextView) customView.findViewById(R.id.refuse_in_drop_down_for_all_repair);
            toBeHandle = (TextView) customView.findViewById(R.id.to_be_review_in_drop_down_for_all_repair);
            backToTop = (LinearLayout) getActivity().findViewById(R.id.back_to_top_in_all_repair);
            // 自定义view添加触摸事件
            customView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    directionImg.setImageResource(R.drawable.down);
                    if (popupwindow != null && popupwindow.isShowing()) {
                        popupwindow.dismiss();
                        listView.setAlpha(1);
                        popupwindow = null;
                    }
                    return false;
                }
            });
            initEvents();
        }

    }

    private void showPopupWindow() {
        // 创建PopupWindow实例
        if (popupwindow == null) {
            if (customView != null) {
                popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //切换侧滑菜单按钮
            case R.id.top_layout_menu:
                ((MainActivity)getActivity()).showMenu();
                break;
            //下拉选择框
            case R.id.direction_in_top:
                //选择框当前为隐藏状态
                if (down == false) {
                    directionImg.setImageResource(R.drawable.up);
                    showPopupWindow();
                    listView.setAlpha(0.3f);
                    if (popupwindow != null) {
                        popupwindow.showAsDropDown(getActivity().findViewById(R.id.direction_img), 0, 0);
                    }
                    down = true;
                }
                //选择框当前为弹出状态
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
            case R.id.back_to_top_in_all_repair:
                listView.smoothScrollByOffset(-getScrollY());
                backToTop.setVisibility(View.INVISIBLE);
                break;

            //全部采购
            case R.id.all_repair_in_drop_down_for_all_repair:
                temp2.clear();
                for (Repair repair : repairs
                        ) {
                    temp2.add(repair);
                }
                myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, temp2);
                listView.setAdapter(myAdapterForRepair);
                myAdapterForRepair.notifyDataSetChanged();
                resetStatusStyle();
                allPurchase.setAlpha(1);
                allPurchase.setTextColor(Color.rgb(95, 187, 176));
                allPurchase.setBackgroundResource(R.drawable.bg_border_green);
                break;

            //通过审核
            case R.id.pass_in_drop_down_for_all_repair:
                temp2.clear();
                for (Repair repair : repairs) {
                    if (repair.getCheckState().equals("通过")) {
                        temp2.add(repair);
                    }
                }
                myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, temp2);
                listView.setAdapter(myAdapterForRepair);
                myAdapterForRepair.notifyDataSetChanged();
                resetStatusStyle();
                pass.setAlpha(1);
                pass.setTextColor(Color.rgb(95, 187, 176));
                pass.setBackgroundResource(R.drawable.bg_border_green);
                break;
            //拒绝申请
            case R.id.refuse_in_drop_down_for_all_repair:
                temp2.clear();
                for (Repair repair : repairs
                        ) {
                    if (repair.getCheckState().equals("拒绝")) {
                        temp2.add(repair);
                    }
                }
                myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, temp2);
                listView.setAdapter(myAdapterForRepair);
                myAdapterForRepair.notifyDataSetChanged();

                resetStatusStyle();
                refuse.setAlpha(1);
                refuse.setTextColor(Color.rgb(95, 187, 176));
                refuse.setBackgroundResource(R.drawable.bg_border_green);
                break;

            //待审核
            case R.id.to_be_review_in_drop_down_for_all_repair:
                temp2.clear();
                for (Repair repair : repairs
                        ) {
                    if (repair.getCheckState().equals("待审核")) {
                        temp2.add(repair);
                    }
                }
                myAdapterForRepair = new MyAdapterForRepair(getActivity(), R.layout.item, temp2);
                listView.setAdapter(myAdapterForRepair);
                myAdapterForRepair.notifyDataSetChanged();

                resetStatusStyle();
                toBeHandle.setAlpha(1);
                toBeHandle.setTextColor(Color.rgb(95, 187, 176));
                toBeHandle.setBackgroundResource(R.drawable.bg_border_green);
                break;


        }
    }

    //数据读取完成后的操作
    private void doAfterAsyTask() {
        if (getActivity() != null) {
            getPopupWindowView();
            resetStatusStyle();
            //默认选中全部资产与全部分类
            allPurchase.setAlpha(1);
            allPurchase.setTextColor(Color.rgb(95, 187, 176));
            allPurchase.setBackgroundResource(R.drawable.bg_border_green);
            initListView();
        }
    }

    //获取服务器数据
    public void refreshData() {
        if (getActivity() != null) {
            //访问服务器数据
            volleyUtil.updateSQLiteFromMySql("repair", new UpdateListener() {
                @Override
                public void onSucceed(String s) {
                    refreshableView.finishRefreshing("all_repair");
                    if (s.contains("error-business")) {
                        onError(new VolleyError("error-business"));
                    } else {
                        //从服务器获取数据
                        List<Repair> lists = JsonUtil.parseRepairJson(s);
                        //更新本地数据库
                        MyDBHandler.getInstance(getActivity()).updateRepair(getActivity(), lists);
                        //从本地数据库获取数据，更新界面
                        readDataFromSQLite();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    refreshableView.finishRefreshing("all_repair");
                    Log.d("myTag", error.toString());
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            });
        }
    }
}

