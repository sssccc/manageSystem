package demo.yc.formalmanagersystem.activity;

import android.os.Bundle;
import android.widget.ListView;

import demo.yc.formalmanagersystem.R;

public class ChangePositionActivity extends BaseActivity {

    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_position);
    }
}
