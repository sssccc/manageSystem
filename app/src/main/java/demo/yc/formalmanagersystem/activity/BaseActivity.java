package demo.yc.formalmanagersystem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.yc.formalmanagersystem.util.ActivityCollector;


/**
 * Created by Administrator on 2016/7/17.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
