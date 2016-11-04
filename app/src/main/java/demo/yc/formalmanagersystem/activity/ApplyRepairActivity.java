package demo.yc.formalmanagersystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.models.Property;
import demo.yc.formalmanagersystem.models.Repair;
import demo.yc.formalmanagersystem.util.ActivityCollector;
import demo.yc.formalmanagersystem.util.VolleyUtil;

/**
 * Created by Administrator on 2016/7/22.
 */
public class ApplyRepairActivity extends BaseActivity implements View.OnClickListener {

    private TextView name;
    private TextView identifier;
    private EditText detailOfRepair;
    private Button confirmApplyRepair;
    private RelativeLayout back;
    private Property property;
    private VolleyUtil volleyUtil = new VolleyUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.apply_repair_layout);
        intiViews();
        initEvents();
        initValues();
    }

    private void initValues() {
        property = (Property) getIntent().getSerializableExtra("data_extra");
        if (property != null) {
            name.setText(property.getName());
            identifier.setText(property.getIdentifier());
        }
    }

    private void initEvents() {
        back.setOnClickListener(this);
        confirmApplyRepair.setOnClickListener(this);
    }

    private void intiViews() {
        back = (RelativeLayout) findViewById(R.id.back_in_apply_repair_page);
        detailOfRepair = (EditText) findViewById(R.id.detail_of_repair_in_repair_page);
        confirmApplyRepair = (Button) findViewById(R.id.confirm_to_apply_repair);
        name = (TextView) findViewById(R.id.name_in_apply_repair_page);
        identifier = (TextView) findViewById(R.id.identifier_in_apply_repair_page);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_in_apply_repair_page:
                ActivityCollector.removeActivity(this);
                break;
            case R.id.confirm_to_apply_repair:

                if (TextUtils.isEmpty(detailOfRepair.getText().toString())) {
                    SweetAlertDialog eDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    eDialog.setTitleText("请在备注处注明报修原因！");
                    eDialog.setConfirmText("OK");
                    eDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    eDialog.show();
                } else {
                    final Repair repair = new Repair();
                    repair.setCreaterIdentifier(MyApplication.getUser().getUsername());
                    repair.setIdentifier(property.getIdentifier());
                    repair.setDescribe(detailOfRepair.getText().toString());
                    repair.setDescribe(detailOfRepair.getText().toString());

                    final SweetAlertDialog alarmDialog = new SweetAlertDialog(ApplyRepairActivity.this, SweetAlertDialog.WARNING_TYPE);
                    alarmDialog.setTitleText("确认申请？");
                    alarmDialog.setCancelText("取消");
                    alarmDialog.setCancelable(false);
                    alarmDialog.setCanceledOnTouchOutside(false);
                    alarmDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    alarmDialog.setConfirmText("确认");
                    alarmDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(final SweetAlertDialog alarmDialog) {
                            alarmDialog.dismissWithAnimation();
                            final SweetAlertDialog progressDialog = new SweetAlertDialog(ApplyRepairActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setTitleText("");
                            progressDialog.setContentText("提交申请中，请稍等...");
                            progressDialog.show();


                            volleyUtil.updateRepairInMySql(repair, new UpdateListener() {
                                @Override
                                public void onSucceed(String s) {
                                    //提交成功，设置维修状态为 待维修
                                    property.setRepairStatus("待审核");
                                    //更新Property表
                                    volleyUtil.updatePropertyInMySql(property, new UpdateListener() {
                                        @Override
                                        public void onSucceed(String s) {
                                            progressDialog.dismiss();
                                            SweetAlertDialog successDialog = new SweetAlertDialog(ApplyRepairActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                            successDialog.setTitleText("申请成功!");
                                            successDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    Intent intent = new Intent();
                                                    setResult(RESULT_OK,intent);
                                                    ActivityCollector.removeActivity(ApplyRepairActivity.this);
                                                }
                                            });
                                            successDialog.show();
                                        }

                                        //更新Property失败
                                        @Override
                                        public void onError(VolleyError error) {
                                            progressDialog.dismiss();
                                            SweetAlertDialog errorDialog = new SweetAlertDialog(ApplyRepairActivity.this, SweetAlertDialog.ERROR_TYPE);
                                            errorDialog.setTitleText("资产数据异常，请回首页刷新！");
                                            errorDialog.setConfirmText("确认");
                                            errorDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                }
                                            });
                                            errorDialog.show();
                                        }
                                    });
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    progressDialog.dismiss();
                                    SweetAlertDialog errorDialog = new SweetAlertDialog(ApplyRepairActivity.this, SweetAlertDialog.ERROR_TYPE);
                                    errorDialog.setTitleText("提交失败，请重试！");
                                    errorDialog.setConfirmText("确认");
                                    errorDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                                    errorDialog.show();
                                }
                            });
                        }
                    });
                    alarmDialog.show();
                }
                break;
        }
    }
}
