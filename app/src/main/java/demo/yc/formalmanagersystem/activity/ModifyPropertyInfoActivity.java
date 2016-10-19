package demo.yc.formalmanagersystem.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.models.Property;
import demo.yc.formalmanagersystem.util.ActivityCollector;
import demo.yc.formalmanagersystem.util.VolleyUtil;

/**
 * Created by Administrator on 2016/7/21.
 */
public class ModifyPropertyInfoActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back;
    private EditText name;
    private EditText cate;
    private EditText brand;
    private EditText model;
    private EditText identifier;
    private EditText price;
    private EditText date;
    private EditText provider;
    private EditText providerTel;
    private Spinner isBorrowedProperty;
    private TextView repairStatus;
    private Button confirmToModify;

    private ImageView datePicker;

    private Property propertyToBeModified;

    private VolleyUtil volleyUtil = new VolleyUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modify_property_info_layout);
        initViews();
        initEvents();
        initValues();
    }

    private void initValues() {
        Intent i = getIntent();
        propertyToBeModified = (Property) i.getSerializableExtra("property_to_be_modified");
        if (propertyToBeModified != null) {
            name.setText(propertyToBeModified.getName());
            name.setSelection(propertyToBeModified.getName().length());
            cate.setText(propertyToBeModified.getCate());
            brand.setText(propertyToBeModified.getBrand());
            model.setText(propertyToBeModified.getModel());
            identifier.setText(propertyToBeModified.getIdentifier());
            price.setText(propertyToBeModified.getPrice());
            date.setText(propertyToBeModified.getDate());
            provider.setText(propertyToBeModified.getProvider());
            providerTel.setText(propertyToBeModified.getProviderTel());
            repairStatus.setText(propertyToBeModified.getRepairStatus());
            if (propertyToBeModified.isBorrowedProperty()) {
                isBorrowedProperty.setSelection(0);
            } else {
                isBorrowedProperty.setSelection(1);
            }

        }

    }

    private void initEvents() {
        back.setOnClickListener(this);
        confirmToModify.setOnClickListener(this);
        datePicker.setOnClickListener(this);
    }

    private void initViews() {
        back = (RelativeLayout) findViewById(R.id.back_in_modify_page);
        name = (EditText) findViewById(R.id.name_edit_in_modify_page);
        cate = (EditText) findViewById(R.id.cate_edit_in_modify_page);
        brand = (EditText) findViewById(R.id.brand_edit_in_modify_page);
        model = (EditText) findViewById(R.id.model_edit_in_modify_page);
        identifier = (EditText) findViewById(R.id.identifier_edit_in_modify_page);
        price = (EditText) findViewById(R.id.price_edit_in_modify_page);
        date = (EditText) findViewById(R.id.date_edit_in_modify_page);
        provider = (EditText) findViewById(R.id.provider_in_modify_page);
        providerTel = (EditText) findViewById(R.id.provider_tel_in_modify_page);
        isBorrowedProperty = (Spinner) findViewById(R.id.isBorrowedProperty_in_modify_page);
        repairStatus = (TextView) findViewById(R.id.repair_status_in_modify_page);
        confirmToModify = (Button) findViewById(R.id.confirm_to_modify_in_modify_page);
        datePicker = (ImageView) findViewById(R.id.pick_date_in_modify_page);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_in_modify_page:
                ActivityCollector.removeActivity(this);
                setResult(RESULT_CANCELED);
                break;
            case R.id.confirm_to_modify_in_modify_page:

                final SweetAlertDialog alarmDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                alarmDialog.setTitleText("确认修改该资产信息？");
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
                        final SweetAlertDialog progressDialog = new SweetAlertDialog(ModifyPropertyInfoActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.setTitleText("");
                        progressDialog.setContentText("修改中，请稍等...");
                        progressDialog.show();

                        propertyToBeModified.setId(propertyToBeModified.getIdentifier());
                        propertyToBeModified.setName(name.getText().toString());
                        propertyToBeModified.setCate(cate.getText().toString());
                        propertyToBeModified.setBrand(brand.getText().toString());
                        propertyToBeModified.setModel(model.getText().toString());
                        propertyToBeModified.setIdentifier(identifier.getText().toString());
                        propertyToBeModified.setPrice(price.getText().toString());
                        propertyToBeModified.setDate(date.getText().toString());
                        if (isBorrowedProperty.getSelectedItemPosition() == 0) {
                            propertyToBeModified.setBorrowedProperty(true);
                        } else {
                            propertyToBeModified.setBorrowedProperty(false);
                        }
                        propertyToBeModified.setProvider(provider.getText().toString());
                        propertyToBeModified.setProviderTel(providerTel.getText().toString());

                        volleyUtil.updatePropertyInMySql(propertyToBeModified, new UpdateListener() {
                            @Override
                            public void onSucceed(String s) {
                                progressDialog.dismiss();
                                SweetAlertDialog successDialog = new SweetAlertDialog(ModifyPropertyInfoActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                                successDialog.setTitleText("修改成功!");
                                successDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        Intent intent = new Intent();
                                        intent.putExtra("data_return", propertyToBeModified);
                                        setResult(RESULT_OK, intent);
                                        ActivityCollector.removeActivity(ModifyPropertyInfoActivity.this);
                                    }
                                });
                                successDialog.show();

                            }

                            @Override
                            public void onError(VolleyError error) {
                                progressDialog.dismiss();
                                SweetAlertDialog errorDialog = new SweetAlertDialog(ModifyPropertyInfoActivity.this, SweetAlertDialog.ERROR_TYPE);
                                errorDialog.setTitleText("修改失败，请重试！");
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



                /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("是否确定修改资产信息？").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        propertyToBeModified.setId(propertyToBeModified.getIdentifier());
                        propertyToBeModified.setName(name.getText().toString());
                        propertyToBeModified.setCate(cate.getText().toString());
                        propertyToBeModified.setBrand(brand.getText().toString());
                        propertyToBeModified.setModel(model.getText().toString());
                        propertyToBeModified.setIdentifier(identifier.getText().toString());
                        propertyToBeModified.setPrice(price.getText().toString());
                        propertyToBeModified.setDate(date.getText().toString());
                        if (isBorrowedProperty.getSelectedItemPosition() == 0) {
                            propertyToBeModified.setBorrowedProperty(true);
                        } else {
                            propertyToBeModified.setBorrowedProperty(false);
                        }
                        propertyToBeModified.setProvider(provider.getText().toString());
                        propertyToBeModified.setProviderTel(providerTel.getText().toString());

                        volleyUtil.updatePropertyInMySql(propertyToBeModified, new UpdateListener() {
                            @Override
                            public void onSucceed(String s) {
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ModifyPropertyInfoActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent();
                                        intent.putExtra("data_return", propertyToBeModified);
                                        setResult(RESULT_OK, intent);
                                        ActivityCollector.removeActivity(ModifyPropertyInfoActivity.this);
                                    }
                                });

                            }

                            @Override
                            public void onError(VolleyError error) {
                                Toast.makeText(ModifyPropertyInfoActivity.this, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).show();*/
                break;
            case R.id.pick_date_in_modify_page:
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        ActivityCollector.removeActivity(this);
        setResult(RESULT_CANCELED);
    }
}
