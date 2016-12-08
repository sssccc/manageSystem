package demo.yc.formalmanagersystem.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import java.io.File;

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.contentvalues.PersonInfoContent;
import demo.yc.formalmanagersystem.contentvalues.SelectPhotoContent;
import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.models.Person;
import demo.yc.formalmanagersystem.util.DialogUtil;
import demo.yc.formalmanagersystem.util.FileUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.CircleImageView;

public class UpdatePersonInfoActivity extends BaseActivity implements View.OnClickListener {

    ImageView sex;
    TextView name, age;
    Button saveBtn, cancelBtn;
    EditText number, institute, major, class_num, position;
    String sexType;  // 0 boy   1  girl
    CircleImageView headPhoto;

    Intent lastIntent;
    Person p;

    MyDBHandler db;

    boolean isChange = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_person_info);
        db = MyDBHandler.getInstance(this);

        setUi();
        setListener();
        getMyIntent();
    }

    private void setUi() {
        name = (TextView) findViewById(R.id.update_name);
        age = (TextView) findViewById(R.id.update_age);
        saveBtn = (Button) findViewById(R.id.update_save_btn);
        cancelBtn = (Button) findViewById(R.id.update_cancel_btn);
        number = (EditText) findViewById(R.id.update_number);
        institute = (EditText) findViewById(R.id.update_institute);
        major = (EditText) findViewById(R.id.update_major);
        class_num = (EditText) findViewById(R.id.update_class);
        position = (EditText) findViewById(R.id.update_position);
        headPhoto = (CircleImageView) findViewById(R.id.update_head);
        sex = (ImageView) findViewById(R.id.update_sex);
    }

    private void setListener() {
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        headPhoto.setOnClickListener(this);
        name.setOnClickListener(this);
        sex.setOnClickListener(this);
        age.setOnClickListener(this);
        position.setOnClickListener(this);
    }


    private void getMyIntent() {
        lastIntent = getIntent();//.getSerializableExtra(PersonInfoContent.UPTADE_PERSON_INFO_TAG);
        if (lastIntent == null) {
            Toast.makeText(this, "null", Toast.LENGTH_LONG).show();
            return;
        }
        p = (Person) lastIntent.getSerializableExtra(PersonInfoContent.UPTADE_PERSON_INFO_TAG);
        name.setText(p.getName());
        age.setText(p.getAge() + "岁");
        institute.setText(p.getCollege());
        class_num.setText(p.getClazz());
        major.setText(p.getMajor());
        number.setText(p.getStudentId());
        position.setText(p.getQuartersId());
       // position.setText(PersonUtil.getPositonName(p.getQuartersId()));
        if (p.getSex().contains("1"))
            sex.setImageResource(R.drawable.boy);
        else
            sex.setImageResource(R.drawable.girl);

        if(!MyApplication.getPersonHeadPath().equals("")) {
            Glide.with(this).load(MyApplication.getPersonHeadPath()).into(headPhoto);
            Log.w("head","updateActivity-->application-->"+MyApplication.getPersonHeadPath());
        }else {
//            String tempPath = FileUtil.getUserImagePath(MyApplication.getUser().getId());
//            File file = new File(tempPath);
//            if (file.exists())
//                Glide.with(this).load(tempPath).into(headPhoto);
//            else
            Log.w("head","updateActivity-->p.getPicture-->"+p.getPicture());
            Glide.with(this).load(VolleyUtil.ROOT_URL+p.getPicture()).into(headPhoto);
        }
    }

    private void changePhoto() {
        Intent i = new Intent(UpdatePersonInfoActivity.this, SelectPhotoActivity.class);
        String path = "";
        if(!MyApplication.getPersonHeadPath().equals(""))
          path = MyApplication.getPersonHeadPath();
        else {
            String tempPath = FileUtil.getUserImagePath(MyApplication.getUser().getId());
            File file = new File(tempPath);
            if (file.exists())
                path = tempPath;
            else
                path = VolleyUtil.ROOT_URL+p.getPicture();
        }
        i.putExtra(PersonInfoContent.CHANGE_PHOTO_TAG, path);
        startActivityForResult(i, SelectPhotoContent.CHANGE_PHOTO);
    }

    private void changeAge() {
        Intent ageIntent = new Intent(this, ChangeAgeActivity.class);
        String myAge = age.getText().toString();
        String value = myAge.substring(0, myAge.length() - 1);
        ageIntent.putExtra(PersonInfoContent.CHANGE_AGE_TAG, value);
        startActivityForResult(ageIntent, PersonInfoContent.CHANGE_AGE);
    }

    private void changeName() {
        Intent nameIntent = new Intent(this, ChangeNameActivity.class);
        nameIntent.putExtra(PersonInfoContent.CHANGE_NAME_TAG, name.getText().toString());
        startActivityForResult(nameIntent, PersonInfoContent.CHANGE_NAME);
    }

    private void changeSex() {
        Intent sexIntent = new Intent(this, ChangeSexActivity.class);
        startActivityForResult(sexIntent, PersonInfoContent.CHANGE_SEX);
    }
    private void changePosition() {
        Intent sexIntent = new Intent(this, ChangePositionActivity.class);
        startActivityForResult(sexIntent, PersonInfoContent.CHANGE_POSITION);
    }


    private void saveInfo() {
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(UpdatePersonInfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        ProgressDialog pd = DialogUtil.showDialog(UpdatePersonInfoActivity.this,"正在保存...");
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(UpdatePersonInfoActivity.this,"操作取消",Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().getMyQueue().cancelAll("updatePersonInfo");
            }
        });
        pd.show();

        p.setCollege(institute.getText().toString());
        p.setMajor(major.getText().toString());
        p.setStudentId(number.getText().toString());
        p.setClazz(class_num.getText().toString());


        new VolleyUtil().updatePersonInfo(p, new UpdateListener() {
            @Override
            public void onSucceed(String s) {
                DialogUtil.dissmiss();
                if(s.equals("1"))
                {
                    Toast.makeText(UpdatePersonInfoActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    db.updatePersonInfo(p);
                    MyApplication.setPersonName(p.getName());
                    lastIntent.putExtra(PersonInfoContent.UPTADE_PERSON_INFO_TAG, p);
                    if(isChange)
                        lastIntent.putExtra("isChange",true);
                    setResult(RESULT_OK, lastIntent);
                    finish();
                }else
                {
                    Toast.makeText(UpdatePersonInfoActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(VolleyError error) {
                DialogUtil.dissmiss();
                Log.w("person","save"+error.toString());
                Toast.makeText(UpdatePersonInfoActivity.this,"保存失败",Toast.LENGTH_SHORT).show();

            }
        });
        //上传到服务器，修改数据库内容

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_age:
                changeAge();
                break;
            case R.id.update_sex:
                changeSex();
                break;
            case R.id.update_name:
                changeName();
                break;
            case R.id.update_head:
                changePhoto();
                break;
            case R.id.update_save_btn:
                saveInfo();
                break;
            case R.id.update_position:
                changePosition();
                break;
            case R.id.update_cancel_btn:
                if(isChange)
                {
                    setResult(RESULT_OK, lastIntent);
                }else
                {
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PersonInfoContent.CHANGE_AGE:
                    String age = data.getStringExtra(PersonInfoContent.CHANGE_AGE_TAG);
                    this.age.setText(age + "岁");
                    p.setAge(Integer.parseInt(age));
                    break;
                case PersonInfoContent.CHANGE_NAME:
                    String name = data.getStringExtra(PersonInfoContent.CHANGE_NAME_TAG);
                    this.name.setText(name);
                    p.setName(name);
                    break;
                case PersonInfoContent.CHANGE_SEX:
                    String sexType = data.getStringExtra(PersonInfoContent.CHANGE_SEX_TAG);
                    this.sexType = sexType;
                    p.setSex(sexType);
                    if (sexType.contains("1"))
                        sex.setImageResource(R.drawable.boy);
                    else
                        sex.setImageResource(R.drawable.girl);
                    break;
                case PersonInfoContent.CHANGE_PHOTO:
                    Glide.with(this).load(MyApplication.getPersonHeadPath()).into(headPhoto);
                    isChange = true;
                    break;
                case PersonInfoContent.CHANGE_POSITION:
                    String positionStr = data.getStringExtra(PersonInfoContent.CHANGE_POSITION_TAG);
                    this.position.setText(positionStr);
                    p.setQuartersId(positionStr);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
