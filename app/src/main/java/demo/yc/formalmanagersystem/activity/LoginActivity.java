package demo.yc.formalmanagersystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.User;
import demo.yc.formalmanagersystem.util.ToastUtil;

/**
 * Created by Administrator on 2016/7/17.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button loginButton;
    private EditText countEditText;
    private EditText passwordEditText;
    private TextView registerTextView;
    private ImageView portraitImageView;
    private CheckBox rememberCheckBox;
    private SharedPreferences pre;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initViews();
        loginButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        pre = getPreferences(MODE_PRIVATE);
        editor = pre.edit();
        initCount();
        countEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initCount() {
        //初始化控件，查询是否有保存的账号密码
        String count = pre.getString("count", "");
        if (!TextUtils.isEmpty(count)) {
            String password = pre.getString("password", "");
            countEditText.setText(count);
            passwordEditText.setText(password);
            rememberCheckBox.setChecked(true);
        } else {
            rememberCheckBox.setChecked(false);
        }
        countEditText.setSelection(countEditText.getText().length());
    }

    private void initViews() {
        loginButton = (Button) findViewById(R.id.login_btn);
        countEditText = (EditText) findViewById(R.id.count_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        registerTextView = (TextView) findViewById(R.id.register_text_view);
        rememberCheckBox = (CheckBox) findViewById(R.id.remember_check_box);
        portraitImageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                final String count = countEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                //信息未完善
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(count)) {
                    ToastUtil.createToast(LoginActivity.this,"请填写完整信息！");
                    break;
                }

                final SweetAlertDialog dialog = new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                dialog.setTitleText("正在登录...");
                dialog.show();

//
                Intent i1 = new Intent(LoginActivity.this, MainActivity.class);
                MyApplication.getUser().setId("0615bb2424f84ebca0758f387b8daf0c");
                MyApplication.getUser().setUsername(count);
                startActivity(i1);

//                new VolleyUtil().login(count, password, new UpdateListener() {
//                    @Override
//                    public void onSucceed(String s) {
//                        //账号不存在
//                        //密码错误
//                        //成功登陆
//
//                        Log.w("login",s);
//                        if(s == null) {
//                            dialog.dismiss();
//                            Toast.makeText(LoginActivity.this,"登录异常",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        if(s.equals("1"))
//                        {
//                            dialog.dismiss();
//                            Toast.makeText(LoginActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
//
//                            Log.w("login","用户名不存在");
//                        }
//                        //密码错误
//                        else if(s.equals("2"))
//                        {
//                            dialog.dismiss();
//                            Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
//                            Log.w("login","密码错误");
//                        }
//                        //登录成功
//                        else if(s.length() == 32)
//                        {
//                            Log.w("login","登陆成功");
//                            if (rememberCheckBox.isChecked())
//                            {
//                                editor.putString("count", count);
//                                editor.putString("password", password);
//                            }else
//                            {
//                                //不保存账号密码
//                                editor.clear();
//                            }
//                            editor.commit();
//                            Intent i1 = new Intent(LoginActivity.this, MainActivity.class);
//                            MyApplication.getUser().setId(s);
//                            MyApplication.getUser().setUsername(count);
//                            startActivity(i1);
//                        }
//                        else
//                        {
//                            dialog.dismiss();
//                            Toast.makeText(LoginActivity.this,"登录异常",Toast.LENGTH_SHORT).show();
//                            Log.w("login","登录异常");
//                        }
//                    }
//
//                    @Override
//                    public void onError(VolleyError error) {
//                        dialog.dismiss();
//                        Toast.makeText(LoginActivity.this,"登录超时",Toast.LENGTH_SHORT).show();
//                        Log.w("login","error login" + error.toString());
//                    }
//                });

                break;
            //注册新用户
            case R.id.register_text_view:
                Intent i2 = new Intent(this, RegisterActivity.class);
                startActivityForResult(i2, 0);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Bundle bundle = data.getExtras();
                    User user = (User) bundle.getSerializable("user");
                    countEditText.setText(user.getUsername());
                    passwordEditText.setText(user.getPassword());
                    break;
            }
        }
    }
}
