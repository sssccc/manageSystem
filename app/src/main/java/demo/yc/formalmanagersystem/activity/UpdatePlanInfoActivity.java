package demo.yc.formalmanagersystem.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.contentvalues.TimePlanContent;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.util.DialogUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.DialogCancelListener;

public class UpdatePlanInfoActivity extends BaseActivity {

    Intent lastIntent;
    EditText title,describe;
    RadioGroup cate,isFixed;
    Button saveBtn;
    View backLayout,editLayout;
    TextView time;
    ToggleButton isFreedBtn;
    Plan p;

    RadioButton classBtn,taskBtn,personBtn;
    RadioButton todayBtn,alwaysBtn;

    String week_day,day_time;

    ProgressDialog pd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setMessage("正在保存...");
        setContentView(R.layout.activity_update_plan_info);
        setUi();
        getMyIntent();
        setListener();
    }
    private void setUi()
    {
        title = (EditText) findViewById(R.id.update_plan_title);
        describe = (EditText) findViewById(R.id.update_plan_describe);
        cate = (RadioGroup) findViewById(R.id.update_plan_group_cate);
        isFixed = (RadioGroup) findViewById(R.id.update_plan_group_isFixed);
        saveBtn = (Button) findViewById(R.id.update_plan_save_btn);
        backLayout = findViewById(R.id.update_plan_back_layout);
        editLayout = findViewById(R.id.update_plan_edit_layout);
        time = (TextView) findViewById(R.id.update_plan_time);
        isFreedBtn = (ToggleButton) findViewById(R.id.update_plan_isFreed_btn);

        classBtn = (RadioButton) findViewById(R.id.update_plan_btn_class);
        taskBtn = (RadioButton) findViewById(R.id.update_plan_btn_task);
        personBtn = (RadioButton) findViewById(R.id.update_plan_btn_person);
        todayBtn = (RadioButton) findViewById(R.id.update_plan_btn_today);
        alwaysBtn = (RadioButton) findViewById(R.id.update_plan_btn_always);
    }

    private void getMyIntent()
    {
        lastIntent = getIntent();
        p = (Plan)lastIntent.getSerializableExtra(TimePlanContent.UPDATE_PLAN_INFO_TAG);
        title.setText(p.getTitle());
        describe.setText(p.getContent());

        if(p.getIsFixed() ==0)
            todayBtn.setChecked(true);
        else if(p.getIsFixed() ==1)
          alwaysBtn.setChecked(true);
        switch (p.getDayTime())
        {
            case 1:
                day_time = "1,2节";
                break;
            case 2:
                day_time = ("3,4节");
                break;
            case 3:
                day_time = ("5,6节");
                break;
            case 4:
                day_time = ("7,8节");
                break;
            case 5:
                day_time = ("9,10节");
                break;
            case 6:
                day_time = ("11,12,13节");
                break;
        }

        switch (p.getWeekDay())
        {
            case 1:
                week_day = "周一";
                break;
            case 2:
                week_day = "周二";
                break;
            case 3:
                week_day = "周三";
                break;
            case 4:
                week_day = "周四";
                break;
            case 5:
                week_day = "周五";
                break;
            case 6:
                week_day = "周六";
                break;
            case 7:
                week_day = "周日";
                break;
        }

        time.setText(week_day+" "+day_time);
        if(p.getType() == 0)
            classBtn.setChecked(true);
        else if(p.getType() ==1)
           taskBtn.setChecked(true);
        else if(p.getType() ==2)
           personBtn.setChecked(true);

        if(p.getIsFree() == 0)
        {
            isFreedBtn.setChecked(true);
            editLayout.setVisibility(View.GONE);
        }else
        {
            isFreedBtn.setChecked(false);
            editLayout.setVisibility(View.VISIBLE);
        }


    }

    private void setListener()
    {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showDialog(UpdatePlanInfoActivity.this,"正在保存...").show();
                DialogUtil.onCancelListener(new DialogCancelListener() {
                    @Override
                    public void onCancel() {
                        DialogUtil.dissmiss();
                        Log.w("plan","取消事件.....手动取消");
                        MyApplication.getInstance().getMyQueue().cancelAll("updatePlanInfo");
                    }
                });

                if(isFreedBtn.isChecked())
                {
                    p.setTitle("没事做");
                    p.setContent("无");
                    p.setType(2);
                    p.setIsFixed(1);
                    p.setIsFree(0);
                }else
                {
                    p.setIsFree(1);
                    p.setTitle(title.getText().toString());
                    p.setContent(describe.getText().toString());

                    if(classBtn.isChecked())
                       p.setType(0);
                    else if(taskBtn.isChecked())
                        p.setType(1);
                    else if(personBtn.isChecked())
                        p.setType(2);
                    if(todayBtn.isChecked())
                       p.setIsFixed(0);
                    else
                        p.setIsFixed(1);
                }

                new VolleyUtil().updatePlanInfo(p, new UpdateListener() {
                    @Override
                    public void onSucceed(String s) {
                        DialogUtil.dissmiss();
                        Log.w("plan","plan保存成功后的返回："+s.toString());
                        Toast.makeText(UpdatePlanInfoActivity.this,"保存成功",Toast.LENGTH_LONG).show();
                        lastIntent.putExtra(TimePlanContent.UPDATE_PLAN_INFO_TAG,p);
                        setResult(RESULT_OK,lastIntent);
                        finish();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.w("plan","plan保存失败后的返回"+error.toString());
                        DialogUtil.dissmiss();
                        Toast.makeText(UpdatePlanInfoActivity.this,"保存失败",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        cate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.update_plan_btn_person:
                        p.setType(2);
                        break;
                    case R.id.update_plan_btn_task:
                        p.setType(1);
                        break;
                    case R.id.update_plan_btn_class:
                        p.setType(0);
                        break;
                }
            }
        });

        isFixed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.update_plan_btn_today:
                        p.setIsFixed(0);
                        break;
                    case R.id.update_plan_btn_always:
                        p.setIsFixed(1);
                        break;
                }
            }
        });

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        isFreedBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isFreedBtn.setChecked(b);
                if(b)
                    editLayout.setVisibility(View.GONE);
                else
                    editLayout.setVisibility(View.VISIBLE);
            }
        });
    }

}
