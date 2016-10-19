package demo.yc.formalmanagersystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import demo.yc.formalmanagersystem.R;

public class PlanDetailActivity extends BaseActivity {



    TextView title,time,describe,isFixed;

    Button updateBtn,cancelBtn;

    Intent lastIntent;

    View blank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
        setUi();
        getMyIntent();
        setListener();
    }

    private void setUi()
    {
        title = (TextView) findViewById(R.id.plan_detail_title);
        time = (TextView) findViewById(R.id.plan_detail_time);
        describe = (TextView) findViewById(R.id.plan_detail_describe);
        isFixed = (TextView) findViewById(R.id.plan_detail_isFixed);
        cancelBtn = (Button) findViewById(R.id.plan_detail_cancel_btn);
        updateBtn = (Button) findViewById(R.id.plan_detail_update_btn);

        blank = findViewById(R.id.plan_detail_blank_view);
    }

    private void getMyIntent()
    {
        lastIntent = getIntent();
    }

    private void setListener()
    {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0,R.anim.set_out);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0,R.anim.set_out);
            }
        });

        blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0,R.anim.set_out);
            }
        });
    }
}
