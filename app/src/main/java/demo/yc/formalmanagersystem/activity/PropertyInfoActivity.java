package demo.yc.formalmanagersystem.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.models.Property;
import demo.yc.formalmanagersystem.util.ActivityCollector;


/**
 * Created by Administrator on 2016/7/21.
 */
public class PropertyInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tel;
    private RelativeLayout modifyPropertyInfo;
    private RelativeLayout back;
    private Button applyRepair;

    private TextView name;
    private TextView cate;
    private TextView brand;
    private TextView model;
    private TextView identifier;
    private TextView price;
    private TextView date;
    private TextView provider;
    private TextView providerTel;
    private TextView isBorrowedProperty;
    private TextView repairStatus;
    private TextView user;
    private TextView factoryId;
    private TextView location;
    private Property property;
    private boolean change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.property_info_layout);
        initViews();
        initEvents();
        initValues();
    }

    private void initValues() {
        property = (Property) getIntent().getSerializableExtra("data_extra");
        location.setText(property.getLocation());
        factoryId.setText(property.getFactoryId());
        user.setText(property.getConsumer());
        name.setText(property.getName());
        cate.setText(property.getCate());
        brand.setText(property.getBrand());
        model.setText(property.getModel());
        identifier.setText(property.getIdentifier());
        price.setText(property.getPrice());
        date.setText(property.getDate());
        provider.setText(property.getProvider());
        providerTel.setText(property.getProviderTel());
        isBorrowedProperty.setText(property.isBorrowedProperty() == true ? "是" : "否");
        repairStatus.setText(property.getRepairStatus());
        if (property.getRepairStatus().equals("待维修")) {
            applyRepair.setVisibility(View.GONE);
        } else if (property.getRepairStatus().equals("无")) {
            SQLiteDatabase db = MyDBHandler.getInstance(this).getDBInstance();
            Cursor cursor = db.query("Repair", null, "identifier=?", new String[]{property.getIdentifier()}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String checkState = cursor.getColumnName(cursor.getColumnIndex("checkState"));
                    if (checkState.equals("待审核")) {
                        applyRepair.setVisibility(View.GONE);
                    }
                }
            }
        }

    }

    private void initEvents() {
        tel.setOnClickListener(this);
        modifyPropertyInfo.setOnClickListener(this);
        applyRepair.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initViews() {
        modifyPropertyInfo = (RelativeLayout) findViewById(R.id.modify_property_info);
        applyRepair = (Button) findViewById(R.id.apply_to_repair_in_info_page);
        back = (RelativeLayout) findViewById(R.id.back_in_property_info);
        tel = (TextView) findViewById(R.id.provider_tel_in_info_page);
        name = (TextView) findViewById(R.id.name_in_info_page);
        cate = (TextView) findViewById(R.id.cate_in_info_page);
        brand = (TextView) findViewById(R.id.brand_in_info_page);
        model = (TextView) findViewById(R.id.model_in_info_page);
        identifier = (TextView) findViewById(R.id.identifier_in_info_page);
        price = (TextView) findViewById(R.id.price_in_info_page);
        date = (TextView) findViewById(R.id.date_in_info_page);
        provider = (TextView) findViewById(R.id.provider_in_info_page);
        providerTel = (TextView) findViewById(R.id.provider_tel_in_info_page);
        isBorrowedProperty = (TextView) findViewById(R.id.isBorrowedProperty);
        repairStatus = (TextView) findViewById(R.id.repair_status);
        user = (TextView) findViewById(R.id.user_in_property_info);
        factoryId = (TextView) findViewById(R.id.factory_identifier_in_property_info);
        location = (TextView) findViewById(R.id.location_in_property_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_property_info:
                Intent intent1 = new Intent(this, ModifyPropertyInfoActivity.class);
                intent1.putExtra("property_to_be_modified", property);
                startActivityForResult(intent1, 0);
                break;
            case R.id.provider_tel_in_info_page:
                Intent intent2 = new Intent(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:" + tel.getText().toString()));
                startActivity(intent2);
                break;
            case R.id.back_in_property_info:
                if (change) {
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                ActivityCollector.removeActivity(this);
                break;
            case R.id.apply_to_repair_in_info_page:
                Intent intent3 = new Intent(this, ApplyRepairActivity.class);
                intent3.putExtra("data_extra", property);
                startActivityForResult(intent3, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    change = true;
                    Property property = (Property) data.getSerializableExtra("data_return");
                    if (property != null) {
                        name.setText(property.getName());
                        cate.setText(property.getCate());
                        brand.setText(property.getBrand());
                        model.setText(property.getModel());
                        identifier.setText(property.getIdentifier());
                        price.setText(property.getPrice());
                        date.setText(property.getDate());
                        provider.setText(property.getProvider());
                        providerTel.setText(property.getProviderTel());
                        user.setText(property.getConsumer());
                        location.setText(property.getLocation());
                        factoryId.setText(property.getFactoryId());
                        if (property.isBorrowedProperty()) {
                            isBorrowedProperty.setText("是");
                        } else {
                            isBorrowedProperty.setText("否");
                        }
                        if (property.getRepairStatus().equals("已维修")) {
                            repairStatus.setText("已维修");
                            applyRepair.setVisibility(View.VISIBLE);
                        } else if (property.getRepairStatus().equals("待维修")) {
                            repairStatus.setText("待维修");
                            applyRepair.setVisibility(View.GONE);
                        } else if (property.getRepairStatus().equals("无")) {
                            repairStatus.setText("无");
                            applyRepair.setVisibility(View.VISIBLE);
                        } else if (property.getRepairStatus().equals("待处理")) {
                            repairStatus.setText("待处理");
                            applyRepair.setVisibility(View.GONE);
                        }
                    }
                } else {
                    change = false;
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    change = true;
                    repairStatus.setText("待处理");
                    applyRepair.setVisibility(View.GONE);
                } else {
                    change = false;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (change) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        ActivityCollector.removeActivity(this);
    }
}
