package demo.yc.formalmanagersystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.contentvalues.PersonInfoContent;

public class ChangeNameActivity extends BaseActivity {


    EditText nameEdt;
    Button saveBtn;

    Intent lastIntent;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        setUi();
        getMyIntent();
        setListener();
    }

    private void getMyIntent()
    {
        lastIntent = getIntent();
        name = lastIntent.getStringExtra(PersonInfoContent.CHANGE_NAME_TAG);
        nameEdt.setText(name);

        Editable editable = nameEdt.getText();
        Selection.setSelection(editable,editable.length());
    }
    private void  setUi()
    {
        nameEdt = (EditText) findViewById(R.id.change_name_edit);
        saveBtn = (Button) findViewById(R.id.change_name_saveBtn);
    }

    private void setListener()
    {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastIntent.putExtra(PersonInfoContent.CHANGE_NAME_TAG,nameEdt.getText().toString());
                setResult(RESULT_OK,lastIntent);
                finish();
            }
        });
    }
}
