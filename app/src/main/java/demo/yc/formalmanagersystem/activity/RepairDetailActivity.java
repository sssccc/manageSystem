package demo.yc.formalmanagersystem.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.Repair;
import demo.yc.formalmanagersystem.util.ActivityCollector;


/**
 * Created by Administrator on 2016/7/22.
 */
public class RepairDetailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back;
    private LinearLayout reviewStatusLayout;
    private LinearLayout finishDateLayout;
    private LinearLayout repairStatusLayout;
    private TextView name;
    private TextView identifier;
    private TextView date;
    private TextView reviewStatus;
    private TextView repairStatus;
    private TextView finishDate;
    private TextView creater;
    private TextView createrIdentifier;
    private TextView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.repair_detail);

        initViews();
        initEvents();
        initValues();
        reviewStatusLayout.setVisibility(View.VISIBLE);
        String status = reviewStatus.getText().toString();
        if (status.equals("通过")) {
            repairStatusLayout.setVisibility(View.VISIBLE);
            if (repairStatus.getText().toString().equals("已维修")) {
                finishDateLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initValues() {
        Repair repair = (Repair) getIntent().getSerializableExtra("data_extra");

        if (repair != null) {
            name.setText(repair.getName());
            identifier.setText(repair.getIdentifier());
            date.setText(repair.getApplyTime());
            reviewStatus.setText(repair.getCheckState());
            repairStatus.setText(repair.getRepairState());
            finishDate.setText(repair.getFinishTime());
            creater.setText(repair.getCreaterName());
            createrIdentifier.setText(repair.getCreaterIdentifier());
            detail.setText(repair.getDescribe());
        }

    }

    private void initEvents() {
        back.setOnClickListener(this);
    }

    private void initViews() {
        back = (RelativeLayout) findViewById(R.id.back_in_repair_detail_page);
        detail = (TextView) findViewById(R.id.detail_in_repair_detail_page);
        name = (TextView) findViewById(R.id.name_in_repair_detail_page);
        identifier = (TextView) findViewById(R.id.identifier_in_repair_detail_page);
        date = (TextView) findViewById(R.id.apply_date_in_repair_detail_page);
        reviewStatus = (TextView) findViewById(R.id.review_status_in_repair_detail_page);
        repairStatus = (TextView) findViewById(R.id.repair_status_in_repair_detail_page);
        finishDate = (TextView) findViewById(R.id.repair_finish_time_in_repair_detail_page);
        creater = (TextView) findViewById(R.id.creater_in_repair_detail_page);
        createrIdentifier = (TextView) findViewById(R.id.creater_identifier_in_repair_detail_page);
        reviewStatusLayout = (LinearLayout) findViewById(R.id.review_status_layout);
        finishDateLayout = (LinearLayout) findViewById(R.id.purchase_finish_time_layout);
        repairStatusLayout = (LinearLayout) findViewById(R.id.purchase_status_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_in_repair_detail_page:
                setResult(-10);
                ActivityCollector.removeActivity(this);
                break;
        }
    }

    public static void startActivity(Context context,Repair
            repair) {
        Intent intent = new Intent(context, RepairDetailActivity.class);
        intent.putExtra("data_extra", repair);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        setResult(-10);
        ActivityCollector.removeActivity(this);
    }
}
