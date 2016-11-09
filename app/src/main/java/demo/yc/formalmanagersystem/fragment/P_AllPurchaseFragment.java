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
import demo.yc.formalmanagersystem.activity.PurchaseDetailActivity;
import demo.yc.formalmanagersystem.adapter.MyAdapterForPurchase;
import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.models.Purchase;
import demo.yc.formalmanagersystem.util.JsonUtil;
import demo.yc.formalmanagersystem.util.ToastUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.RefreshableView;

/**
 * Created by Administrator on 2016/7/26.
 */
public class P_AllPurchaseFragment extends Fragment implements View.OnClickListener {

    private ImageView top_layout_menu;  //切换菜单按钮
    private boolean once = true;    //用于标志本地无数据时，是否第一次进行网络加载
    private RelativeLayout direction;    //弹出选项框按钮
    private ImageView directionImg;     //弹出框指示图标
    private boolean down;   //标志当前是否弹出选项框
    private List<Purchase> purchases = new ArrayList<>();//全部采购

    //用于对purchases进行筛选
    private List<Purchase> temp2 = new ArrayList<>();
    private ListView listView;
    private MyAdapterForPurchase allPurchaseAdapter;

    //用于分类显示
    private PopupWindow popupwindow;
    private TextView allPurchase;
    private TextView pass;
    private TextView refuse;
    private View customView;
    private TextView toBeHandle;

    private LinearLayout backToTop;     //返回顶部
    private VolleyUtil volleyUtil = new VolleyUtil();
    private ExecutorService executor;
    private RefreshableView refreshableView;    //下拉刷新

    //根据数据更新的情况更新UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            P_PropertyManagementFragment.isInitial = true;
            //更新失败时
            if (msg.what == 1) {
                Log.d("myTag", "failure");
                if (getActivity() != null) {
                    ToastUtil.createToast(getActivity(), "更新失败，请重试！");
                }
            }
            //更新成功时(我的采购)
            else {
                purchases = (List<Purchase>) msg.obj;
                //本地数据库中存在数据
                if (purchases.size() != 0) {
                    for (Purchase purchase : purchases
                            ) {
                        temp2.add(purchase);
                    }
                    if (temp2.size() != 0) {
                        if (getActivity() != null) {
                            allPurchaseAdapter = new MyAdapterForPurchase(getActivity(), R.layout.item, temp2);
                        }
                        if (allPurchaseAdapter != null) {
                            listView.setAdapter(allPurchaseAdapter);
                            allPurchaseAdapter.notifyDataSetChanged();
                        }
                    }
                    doAfterAsyTask();
                }
                //本地无数据，自动进行一次网络加载
                else {
                    if (once) {
                        once = false;
                        refreshData();
                    }
                }
            }

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        refreshableView.finishRefreshing("pause");
        P_PropertyManagementFragment.isInitial = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_purchase_in_fragment_layout, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_in_all_purchase_fragment);
        refreshableView = (RefreshableView) view.findViewById(R.id.refresh_view_in_all_purchase);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            //设置下拉刷新监听
            refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshData();
                }
            }, 1);
            top_layout_menu = (ImageView) getActivity().findViewById(R.id.top_layout_menu);
            direction = (RelativeLayout) getActivity().findViewById(R.id.direction_in_top);
            direction.setOnClickListener(this);
            directionImg = (ImageView) getActivity().findViewById(R.id.direction_img);
            executor = Executors.newFixedThreadPool(2);
            //从本地数据库读取数据
            readDataFromSQLite();
        }
    }


    private void initListView() {
        if (getActivity() != null) {
            listView.setAdapter(new MyAdapterForPurchase(getActivity(), R.layout.item, temp2));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PurchaseDetailActivity.startActivity(getActivity(), temp2.get(position));
                }
            });

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

    //注册事件
    private void initEvents() {
        allPurchase.setOnClickListener(this);
        pass.setOnClickListener(this);
        refuse.setOnClickListener(this);
        toBeHandle.setOnClickListener(this);
        backToTop.setOnClickListener(this);
        top_layout_menu.setOnClickListener(this);
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

    //重置资产状态分类的样式
    public void resetStatusStyle() {
        if (this.isAdded()) {
            //设置默认文字颜色
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

    private void getPopupWindowView() {
        // 获取自定义布局文件pop.xml的视图
        if (getActivity() != null) {
            customView = getActivity().getLayoutInflater().inflate(R.layout.drop_down_layout_for_all_purchase,
                    null, false);
            //获取控件
            allPurchase = (TextView) customView.findViewById(R.id.all_purchase_in_drop_down_for_all_purchase);
            pass = (TextView) customView.findViewById(R.id.pass_in_drop_down_for_all_purchase);
            refuse = (TextView) customView.findViewById(R.id.refuse_in_drop_down_for_all_purchase);
            toBeHandle = (TextView) customView.findViewById(R.id.to_be_review_in_drop_down_for_all_purchase);
            backToTop = (LinearLayout) getActivity().findViewById(R.id.back_to_top);
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
            initEvents();
        }
    }

    private void showPopupWindow() {
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void doAfterAsyTask() {
        temp2 = new ArrayList<>(purchases);
        getPopupWindowView();
        resetStatusStyle();
        //默认选中全部资产与全部分类
        if (this.isAdded()) {
            allPurchase.setAlpha(1);
            allPurchase.setTextColor(Color.rgb(95, 187, 176));
            allPurchase.setBackgroundResource(R.drawable.bg_border_green);
            initListView();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //弹出侧滑菜单按钮
            case R.id.top_layout_menu:
                if (popupwindow != null) {
                    down = false;
                    directionImg.setImageResource(R.drawable.down);
                    popupwindow.dismiss();
                    listView.setAlpha(1);
                }
                ((MainActivity) getActivity()).showMenu();
                break;
            //弹出选择框按钮
            case R.id.direction_in_top:
                if (down == false) {
                    directionImg.setImageResource(R.drawable.up);
                    showPopupWindow();
                    listView.setAlpha(0.3f);
                    if (popupwindow != null) {
                        popupwindow.showAsDropDown(getActivity().findViewById(R.id.direction_img), 0, 0);
                    }
                    down = true;
                } else {
                    down = false;
                    if (popupwindow != null) {
                        directionImg.setImageResource(R.drawable.down);
                        popupwindow.dismiss();
                        listView.setAlpha(1);
                    }
                }
                break;
            //返回顶部
            case R.id.back_to_top:
                listView.smoothScrollByOffset(-getScrollY());
                backToTop.setVisibility(View.INVISIBLE);
                break;

            //全部采购
            case R.id.all_purchase_in_drop_down_for_all_purchase:
                temp2.clear();
                for (Purchase purchase : purchases
                        ) {
                    temp2.add(purchase);
                }
                allPurchaseAdapter = new MyAdapterForPurchase(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPurchaseAdapter);
                allPurchaseAdapter.notifyDataSetChanged();
                resetStatusStyle();
                allPurchase.setAlpha(1);
                allPurchase.setTextColor(Color.rgb(95, 187, 176));
                allPurchase.setBackgroundResource(R.drawable.bg_border_green);
                break;
            //通过审核
            case R.id.pass_in_drop_down_for_all_purchase:
                temp2.clear();
                for (Purchase purchase : purchases) {
                    if (purchase.getCheckState().equals("通过")) {
                        temp2.add(purchase);
                    }
                }
                allPurchaseAdapter = new MyAdapterForPurchase(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPurchaseAdapter);
                allPurchaseAdapter.notifyDataSetChanged();
                resetStatusStyle();
                pass.setAlpha(1);
                pass.setTextColor(Color.rgb(95, 187, 176));
                pass.setBackgroundResource(R.drawable.bg_border_green);
                break;
            //拒绝申请
            case R.id.refuse_in_drop_down_for_all_purchase:
                temp2.clear();
                for (Purchase purchase : purchases
                        ) {
                    if (purchase.getCheckState().equals("拒绝")) {
                        temp2.add(purchase);
                    }
                }
                allPurchaseAdapter = new MyAdapterForPurchase(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPurchaseAdapter);
                allPurchaseAdapter.notifyDataSetChanged();

                resetStatusStyle();
                refuse.setAlpha(1);
                refuse.setTextColor(Color.rgb(95, 187, 176));
                refuse.setBackgroundResource(R.drawable.bg_border_green);
                break;

            case R.id.to_be_review_in_drop_down_for_all_purchase:
                temp2.clear();
                for (Purchase purchase : purchases
                        ) {
                    if (purchase.getCheckState().equals("待审核")) {
                        temp2.add(purchase);
                    }
                }
                allPurchaseAdapter = new MyAdapterForPurchase(getActivity(), R.layout.item, temp2);
                listView.setAdapter(allPurchaseAdapter);
                allPurchaseAdapter.notifyDataSetChanged();

                resetStatusStyle();
                toBeHandle.setAlpha(1);
                toBeHandle.setTextColor(Color.rgb(95, 187, 176));
                toBeHandle.setBackgroundResource(R.drawable.bg_border_green);
                break;
        }
    }

    public void refreshData() {
        if (getActivity() != null) {
            //访问服务器数据
            volleyUtil.updateSQLiteFromMySql("purchase", new UpdateListener() {
                @Override
                public void onSucceed(String s) {
                    refreshableView.finishRefreshing("all_purchase");
                    Log.d("myTag", s);
                    //从服务器获取数据
                    if (s.contains("error-business")) {
                        onError(new VolleyError("error-business"));
                    } else {
                        ToastUtil.createToast(getActivity(), "更新成功！");
                        //解析Json格式
                        List<Purchase> lists = JsonUtil.parsePurchaseJson(s);
                        //更新本地数据库
                        MyDBHandler.getInstance(getActivity()).updatePurchase(getActivity(), lists);
                        //从本地数据库获取数据，更新界面
                        readDataFromSQLite();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    refreshableView.finishRefreshing("all_purchase");
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    private void readDataFromSQLite() {
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    SQLiteDatabase db = MyDBHandler.getInstance(getActivity()).getDBInstance();
                    Cursor cursor = db.query("Purchase", null, null, null, null, null, "applyTime"+" desc", null);
                    purchases.clear();
                    temp2.clear();
                    if (cursor.moveToFirst()) {
                        do {
                            final Purchase purchase = new Purchase();
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
                            volleyUtil.getCreaterName(purchase.getCreaterIdentifier(), new UpdateListener() {
                                @Override
                                public void onSucceed(String s) {
                                    purchase.setCreaterName(s);
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Log.d("error",error.toString());
                                    purchase.setCreaterName("未知");
                                }
                            });
                            purchases.add(purchase);
                        } while (cursor.moveToNext());
                        cursor.close();
                    }
                }
                //本地数据库读取完成，发送消息更新UI
                Message msg = handler.obtainMessage();
                msg.obj = purchases;
                msg.what = 0;       //处理成功
                handler.sendMessage(msg);
            }
        };
        executor.execute(task1);
    }
}
