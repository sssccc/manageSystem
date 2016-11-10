package demo.yc.formalmanagersystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.contentvalues.PersonInfoContent;

public class ChangePositionActivity extends BaseActivity {

    ListView listView ;
    String[] items;
    Intent lastIntent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_position);
        lastIntent = getIntent();
        listView = (ListView) findViewById(R.id.change_position_listview);
        items = getResources().getStringArray(R.array.position);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastIntent.putExtra(PersonInfoContent.CHANGE_POSITION_TAG,items[i]);
                setResult(RESULT_OK,lastIntent);
                finish();
            }
        });
    }
}
