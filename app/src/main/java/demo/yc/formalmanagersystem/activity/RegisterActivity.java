package demo.yc.formalmanagersystem.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.models.User;
import demo.yc.formalmanagersystem.util.VolleyUtil;

/**
 * Created by Administrator on 2016/7/17.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private Button registerButton;
    private EditText countEditText;
    private EditText passwordEditText;
    private EditText confirmPassword;
    private LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initViews();
        registerButton.setOnClickListener(this);
    }

    private void initViews() {
        parent = (LinearLayout) findViewById(R.id.parent);
        float translationX = parent.getTranslationX();
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(parent,"translationX",-300,translationX);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(parent,"scaleX",0f,1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(parent,"scaleY",0f,1f);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(parent,"alpha",0f,1f);
        AnimatorSet set = new AnimatorSet();
        set.play(translationAnim).with(scaleXAnim).with(scaleYAnim).with(alphaAnim);
        set.setDuration(1500);
        set.start();
        registerButton = (Button) findViewById(R.id.register_btn);
        countEditText = (EditText) findViewById(R.id.register_count_edit_text);
        passwordEditText = (EditText) findViewById(R.id.register_password_edit_text);
        confirmPassword = (EditText) findViewById(R.id.register_confirm_password_edit_text);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn:
                final String count = countEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String confirmPsw = confirmPassword.getText().toString();
                if(TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmPsw)||TextUtils.isEmpty(count)){
                    Toast.makeText(this,"请填写完整信息！",Toast.LENGTH_SHORT).show();
                    break;
                }else
                {
                    new VolleyUtil().login(count, password, new UpdateListener() {
                        @Override
                        public void onSucceed(String s) {
                            if(s.equals(""))
                            {
                                Log.w("login","login error");
                            }
                            else
                            {
                                Intent lastIntent = getIntent();
                                lastIntent.putExtra("username",count);
                                lastIntent.putExtra("password",password);

                              //  Intent i1 = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle b = new Bundle();
                                User user = new User();
                                user.setPassword(password);
                                user.setUsername(count);
                                user.setId(s);
                                b.putSerializable("user", user);
//                                i1.putExtras(b);
//                                startActivity(i1);
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.w("login","error login" + error.toString());
                        }
                    });
                }

//                              BmobQuery<User> query = new BmobQuery<>();
//                query.addWhereEqualTo("count",count);
//                if(TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmPsw)||TextUtils.isEmpty(count)){
//                    Toast.makeText(this,"请填写完整信息！",Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                query.findObjects(this, new FindListener<User>() {
//                    @Override
//                    public void onSuccess(List<User> list) {
//                        if(list!=null && list.size()!=0){
//                            SweetAlertDialog errorDialog = new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.ERROR_TYPE);
//                            errorDialog.setTitleText("账号已存在！请重试");
//                            errorDialog.show();
//                           // Toast.makeText(RegisterActivity.this, "账号已存在！请重试！", Toast.LENGTH_SHORT).show();
//                            countEditText.setText("");
//                            passwordEditText.setText("");
//                            confirmPassword.setText("");
//                        }else{
//                            if(!password.equals(confirmPsw)){
//                                Toast.makeText(RegisterActivity.this,"两次密码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
//                                passwordEditText.setText("");
//                                confirmPassword.setText("");
//                                return;
//                            }
//                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
//                            User user = new User();
//                            user.setUsername(count);
//                            user.setPassword(password);
//                            user.save(RegisterActivity.this);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("user",user);
//                            Intent intent = new Intent();
//                            intent.putExtras(bundle);
//                            setResult(RESULT_OK,intent);
//                            ActivityCollector.removeActivity(RegisterActivity.this);
//                        }
//                    }
//
//                    @Override
//                    public void onError(int i, String s) {
//
//                    }
//                });




        }
    }
}
