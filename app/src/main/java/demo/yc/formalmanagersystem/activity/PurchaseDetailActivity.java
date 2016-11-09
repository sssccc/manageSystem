package demo.yc.formalmanagersystem.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.Purchase;
import demo.yc.formalmanagersystem.util.ActivityCollector;


/**
 * Created by Administrator on 2016/7/22.
 */
public class PurchaseDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final int USER = 0;
    public static final int MANAGER = 1;
    public static int currentCountType;

    private RelativeLayout back;
    private LinearLayout btnVisibility;
    private LinearLayout reviewStatusLayout;
    private LinearLayout finishDateLayout;
    private LinearLayout purchaseStatusLayout;
    private TextView brand;
    private TextView model;
    private TextView name;
    private TextView date;
    private TextView reviewStatus;
    private TextView purchaseStatus;
    private TextView finishDate;
    private TextView creater;
    private TextView createrIdentifier;
    private TextView detail;
    private Purchase purchase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_detail);
        Intent intent = getIntent();
        if (intent != null) {
            purchase = (Purchase) intent.getSerializableExtra("data_extra");
        }
        initViews();
        initEvents();
        initValues();
        if(currentCountType == USER){
            btnVisibility.setVisibility(View.GONE);
            String status = reviewStatus.getText().toString();
            if (status.equals("通过")) {
                purchaseStatusLayout.setVisibility(View.VISIBLE);
                if (purchaseStatus.getText().toString().equals("已购买")) {
                    finishDateLayout.setVisibility(View.VISIBLE);
                }
            }
        } else if(currentCountType == MANAGER){
            btnVisibility.setVisibility(View.VISIBLE);
            finishDateLayout.setVisibility(View.GONE);
            reviewStatusLayout.setVisibility(View.GONE);
            purchaseStatusLayout.setVisibility(View.GONE);
        }
    }

    private void initValues() {
        if(purchase!=null){
            name.setText(purchase.getName());
            date.setText(purchase.getApplyTime());
            reviewStatus.setText(purchase.getCheckState());
            purchaseStatus.setText(purchase.getPurchaseState());
            finishDate.setText(purchase.getFinishTime());
            creater.setText(purchase.getCreaterName());
            createrIdentifier.setText(purchase.getCreaterIdentifier());
            detail.setText(purchase.getDescribe());
            model.setText(purchase.getModel());
            brand.setText(purchase.getBrand());
        }

    }

    private void initEvents() {
        back.setOnClickListener(this);
    }

    private void initViews() {

        back = (RelativeLayout) findViewById(R.id.back_in_purchase_detail_page);

        detail = (TextView) findViewById(R.id.detail_in_purchase_detail_page);
        name = (TextView) findViewById(R.id.name_in_purchase_detail_page);
        date = (TextView) findViewById(R.id.apply_date_in_purchase_detail_page);
        reviewStatus = (TextView) findViewById(R.id.review_status_in_purchase_detail_page);
        purchaseStatus = (TextView) findViewById(R.id.purchase_status_in_purchase_detail_page);
        finishDate = (TextView) findViewById(R.id.purchase_finish_time_in_purchase_detail_page);
        creater = (TextView) findViewById(R.id.creater_in_purchase_detail_page);
        createrIdentifier= (TextView) findViewById(R.id.creater_identifier_in_purchase_detail_page);
        btnVisibility = (LinearLayout) findViewById(R.id.btn_visibility);
        reviewStatusLayout = (LinearLayout) findViewById(R.id.review_status_layout);
        finishDateLayout = (LinearLayout) findViewById(R.id.purchase_finish_time_layout);
        purchaseStatusLayout = (LinearLayout) findViewById(R.id.purchase_status_layout);
        model = (TextView) findViewById(R.id.model_in_purchase_detail_page);
        brand = (TextView) findViewById(R.id.brand_in_purchase_detail_page);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_in_purchase_detail_page:
                ActivityCollector.removeActivity(this);
                break;
            case R.id.accept_apply_in_purchase_detail_page:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("确认通过该申请吗？").setTitle("提示").setNegativeButton("取消", null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PurchaseDetailActivity.this,"已通过",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        ActivityCollector.removeActivity(PurchaseDetailActivity.this);
                    }
                }).create().show();
                break;
            case R.id.refuse_apply_in_purchase_detail_page:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("确认拒绝该申请吗？").setTitle("提示").setNegativeButton("取消",null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        purchase.setCheckState("拒绝");
                        Intent intent = new Intent();
                        intent.putExtra("data_result",purchase);
                        setResult(RESULT_CANCELED,intent);
                        ActivityCollector.removeActivity(PurchaseDetailActivity.this);
                    }
                }).create().show();
                break;
        }

    }

    public static void startActivity(Context context, int countType, Purchase
                                      purchase){
        currentCountType = countType;
        Intent intent = new Intent(context,PurchaseDetailActivity.class);
        intent.putExtra("data_extra",purchase);
        context.startActivity(intent);
    }

}
