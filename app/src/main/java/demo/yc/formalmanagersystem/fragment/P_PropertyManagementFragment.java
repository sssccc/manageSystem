package demo.yc.formalmanagersystem.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;


/**
 * Created by Administrator on 2016/7/26.
 */
public class P_PropertyManagementFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout direction;   //弹出选择框按钮
    private TextView hint;      //我的资产中页面提示信息

    //底部导航按钮
    private LinearLayout applyPurchase;
    private LinearLayout queryProperty;
    private LinearLayout allPurchase;
    private LinearLayout allRepair;
    private LinearLayout myProperty;

    //底部导航栏
    private ImageView queryImg;
    private TextView queryText;
    private ImageView purchaseImg;
    private TextView purchaseText;
    private ImageView repairImg;
    private TextView repairText;
    private ImageView myPropertyImg;
    private TextView myPropertyText;
    private ImageView applyImg;

    //指示条
    private View view1;
    private ImageView view2;
    private View view4;
    private View view5;
    private View view3;

    //五个功能Fragment
    private P_QueryPropertyFragment queryPropertyFragment;
    private P_AllRepairFragment allRepairFragment;
    private P_MyPropertyFragment myPropertyFragment;
    private P_AllPurchaseFragment allPurchaseFragment;
    private P_ApplyPurchaseFragment applyPurchaseFragment;

    public static boolean isInitial = true;
    private int page = 0;   //记录当前页面
    FragmentManager fm;
    FragmentTransaction ft;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_manager_fragment, container, false);
        queryProperty = (LinearLayout) view.findViewById(R.id.query_property_layout);
        allPurchase = (LinearLayout) view.findViewById(R.id.all_purchase_layout);
        allRepair = (LinearLayout) view.findViewById(R.id.all_repair_layout);
        myProperty = (LinearLayout) view.findViewById(R.id.my_property_layout);
        applyPurchase = (LinearLayout) view.findViewById(R.id.apply_purchase_layout);

        queryImg = (ImageView) view.findViewById(R.id.query_property_image_view);
        queryText = (TextView) view.findViewById(R.id.query_property_text_view);
        purchaseImg = (ImageView) view.findViewById(R.id.all_purchase_image_view);
        purchaseText = (TextView) view.findViewById(R.id.all_purchase_text_view);
        repairImg = (ImageView) view.findViewById(R.id.all_repair_image_view);
        repairText = (TextView) view.findViewById(R.id.all_repair_text_view);
        myPropertyImg = (ImageView) view.findViewById(R.id.my_property_image_view);
        myPropertyText = (TextView) view.findViewById(R.id.my_property_text_view);
        view1 = view.findViewById(R.id.line1);
        view2 = (ImageView) view.findViewById(R.id.line2);
        view3 = view.findViewById(R.id.line3);
        view4 = view.findViewById(R.id.line4);
        view5 = view.findViewById(R.id.line5);
        applyImg = (ImageView) view.findViewById(R.id.apply_purchase_img);

        resetStyle();//设置默认样式

        queryImg.setImageResource(R.drawable.circle_bg);
        queryText.setTextColor(Color.WHITE);
        view1.setBackgroundColor(Color.rgb(0x5f, 0xbb, 0xb0));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view1.getLayoutParams();
        params.height = 7;
        view1.setLayoutParams(params);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        direction = (RelativeLayout) getActivity().findViewById(R.id.direction_in_top);
        hint = (TextView) getActivity().findViewById(R.id.text_view_in_top);
        queryProperty.setOnClickListener(this);
        allPurchase.setOnClickListener(this);
        allRepair.setOnClickListener(this);
        myProperty.setOnClickListener(this);
        applyPurchase.setOnClickListener(this);
        queryPropertyFragment = new P_QueryPropertyFragment();
        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.frame_layout_in_property_management, queryPropertyFragment).commit();
        ((TextView) (getActivity().findViewById(R.id.top_layout_title))).setText("资产查询");
        direction.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //查询资产页面
            case R.id.query_property_layout:
                //取消其他页面中的网络访问
                MyApplication.getInstance().getMyQueue().cancelAll("repair");
                MyApplication.getInstance().getMyQueue().cancelAll("purchase");
                if (page == 0) {
                    break;
                }
                if (P_AllRepairFragment.executor != null) {
                    if (P_AllRepairFragment.executor.isTerminated()) {
                        P_AllRepairFragment.executor.shutdownNow();
                    }
                }
                ft = fm.beginTransaction();
                queryPropertyFragment = new P_QueryPropertyFragment();
                ft.replace(R.id.frame_layout_in_property_management, queryPropertyFragment).commit();
                page = 0;
                resetStyle();
                queryImg.setImageResource(R.drawable.circle_bg);
                queryText.setTextColor(Color.WHITE);
                direction.setVisibility(View.GONE);
                view1.setBackgroundColor(Color.rgb(0x5f, 0xbb, 0xb0));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view1.getLayoutParams();
                params.height = 7;
                view1.setLayoutParams(params);
                ((TextView) (getActivity().findViewById(R.id.top_layout_title))).setText("资产查询");
                break;
            //全部采购页面
            case R.id.all_purchase_layout:
                MyApplication.getInstance().getMyQueue().cancelAll("repair");
                MyApplication.getInstance().getMyQueue().cancelAll("property");
                if (page == 1) {
                    break;
                }
                ft = fm.beginTransaction();
                allPurchaseFragment = new P_AllPurchaseFragment();
                ft.replace(R.id.frame_layout_in_property_management, allPurchaseFragment).commit();

                page = 1;
                resetStyle();
                purchaseImg.setImageResource(R.drawable.circle_bg);
                purchaseText.setTextColor(Color.WHITE);
                direction.setVisibility(View.VISIBLE);
                hint.setVisibility(View.GONE);
                view2.setBackgroundColor(Color.rgb(0x5f, 0xbb, 0xb0));
                FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view2.getLayoutParams();
                params1.height = 6;
                view2.setLayoutParams(params1);

                ((TextView) (getActivity().findViewById(R.id.top_layout_title))).setText("全部采购");
                break;
            //申请采购页面
            case R.id.apply_purchase_layout:

                MyApplication.getInstance().getMyQueue().cancelAll("repair");
                MyApplication.getInstance().getMyQueue().cancelAll("purchase");
                MyApplication.getInstance().getMyQueue().cancelAll("property");

                if (page == 2) {
                    break;
                }
                ft = fm.beginTransaction();
                applyPurchaseFragment = new P_ApplyPurchaseFragment();
                ft.replace(R.id.frame_layout_in_property_management, applyPurchaseFragment).commit();
                page = 2;
                resetStyle();
                view3.setBackgroundColor(Color.rgb(0x5f, 0xbb, 0xb0));
                LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) view3.getLayoutParams();
                params4.height = 6;
                view3.setLayoutParams(params4);
                applyImg.setImageResource(R.drawable.add_property_bg);
                applyImg.setAlpha(1f);

                ((TextView) (getActivity().findViewById(R.id.top_layout_title))).setText("添加采购");
                direction.setVisibility(View.GONE);
                hint.setVisibility(View.GONE);
                break;

            //全部报修页面
            case R.id.all_repair_layout:

                MyApplication.getInstance().getMyQueue().cancelAll("purchase");
                MyApplication.getInstance().getMyQueue().cancelAll("property");

                if (page == 3) {
                    break;
                }
                ft = fm.beginTransaction();
                allRepairFragment = new P_AllRepairFragment();
                ft.replace(R.id.frame_layout_in_property_management, allRepairFragment).commit();
                page = 3;
                resetStyle();
                repairImg.setImageResource(R.drawable.circle_bg);
                repairText.setTextColor(Color.WHITE);
                direction.setVisibility(View.VISIBLE);
                hint.setVisibility(View.GONE);
                view4.setBackgroundColor(Color.rgb(0x5f, 0xbb, 0xb0));
                FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) view4.getLayoutParams();
                params2.height = 6;
                view4.setLayoutParams(params2);
                ((TextView) (getActivity().findViewById(R.id.top_layout_title))).setText("全部报修");
                break;

            //我的资产页面
            case R.id.my_property_layout:
                MyApplication.getInstance().getMyQueue().cancelAll("property");
                if (page == 4) {
                    break;
                }
                ft = fm.beginTransaction();
                myPropertyFragment = new P_MyPropertyFragment();
                ft.replace(R.id.frame_layout_in_property_management, myPropertyFragment).commit();
                page = 4;
                resetStyle();
                myPropertyImg.setImageResource(R.drawable.circle_bg);
                myPropertyText.setTextColor(Color.WHITE);
                direction.setVisibility(View.VISIBLE);
                hint.setVisibility(View.VISIBLE);
                view5.setBackgroundColor(Color.rgb(0x5f, 0xbb, 0xb0));
                FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) view5.getLayoutParams();
                params3.height = 6;
                view5.setLayoutParams(params3);

                ((TextView) (getActivity().findViewById(R.id.top_layout_title))).setText("我的资产");
                ft = fm.beginTransaction();
                if (!myPropertyFragment.isAdded()) {
                    myPropertyFragment = new P_MyPropertyFragment();
                    ft.add(R.id.frame_layout_in_property_management, myPropertyFragment);
                }
                break;
        }
    }

    //重置底部导航栏样式
    private void resetStyle() {
        queryImg.setImageResource(R.drawable.circle_fg);
        queryText.setTextColor(Color.argb(67, 0xba, 0xba, 0xba));
        purchaseImg.setImageResource(R.drawable.circle_fg);
        purchaseText.setTextColor(Color.argb(67, 0xba, 0xba, 0xba));
        repairImg.setImageResource(R.drawable.circle_fg);
        repairText.setTextColor(Color.argb(67, 0xba, 0xba, 0xba));
        myPropertyImg.setImageResource(R.drawable.circle_fg);
        myPropertyText.setTextColor(Color.argb(67, 0xba, 0xba, 0xba));

        view1.setBackgroundColor(Color.argb(67, 0xba, 0xba, 0xba));
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) view1.getLayoutParams();
        params1.height = 3;
        view1.setLayoutParams(params1);

        view2.setBackgroundColor(Color.argb(67, 0xba, 0xba, 0xba));
        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) view2.getLayoutParams();
        params2.height = 3;
        view2.setLayoutParams(params2);

        view3.setBackgroundColor(Color.argb(67, 0xba, 0xba, 0xba));
        LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) view3.getLayoutParams();
        params5.height = 3;
        view3.setLayoutParams(params5);
        applyImg.setImageResource(R.drawable.add_property_fg);
        applyImg.setAlpha(67f / 255);

        view4.setBackgroundColor(Color.argb(67, 0xba, 0xba, 0xba));
        FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) view4.getLayoutParams();
        params3.height = 3;
        view4.setLayoutParams(params3);

        view5.setBackgroundColor(Color.argb(67, 0xba, 0xba, 0xba));
        FrameLayout.LayoutParams params4 = (FrameLayout.LayoutParams) view5.getLayoutParams();
        params4.height = 3;
        view5.setLayoutParams(params4);
    }

}
