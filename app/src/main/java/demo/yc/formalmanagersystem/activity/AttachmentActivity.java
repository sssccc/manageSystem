package demo.yc.formalmanagersystem.activity;

import android.os.Bundle;
import android.widget.ListView;

import demo.yc.formalmanagersystem.R;


public class AttachmentActivity extends BaseActivity {

    ListView listView;
    String[] address = new String[]{
            "http://tsmusic24.tc.qq.com/107693524.mp3",
            "http://tsmusic24.tc.qq.com/107602121.mp3",
            "http://tsmusic24.tc.qq.com/107192538.mp3"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);
        listView = (ListView) findViewById(R.id.attachmentList);

    }
}
