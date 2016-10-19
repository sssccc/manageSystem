package demo.yc.formalmanagersystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.adapter.MyAgeGridViewAdapter;
import demo.yc.formalmanagersystem.contentvalues.PersonInfoContent;
import demo.yc.formalmanagersystem.util.AgeUtil;

public class ChangeAgeActivity extends BaseActivity {

    GridView gridView;
    Intent lastInent;
    MyAgeGridViewAdapter adapter;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_age);

        lastInent = getIntent();
        value = Integer.parseInt(lastInent.getStringExtra(PersonInfoContent.CHANGE_AGE_TAG));
        adapter = new MyAgeGridViewAdapter(this, AgeUtil.getAge(),value);
        gridView = (GridView) findViewById(R.id.change_age_gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastInent.putExtra(PersonInfoContent.CHANGE_AGE_TAG,(i+1)+"");
                setResult(RESULT_OK,lastInent);
                finish();
            }
        });
    }






}
