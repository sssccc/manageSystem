package demo.yc.formalmanagersystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.contentvalues.PersonInfoContent;

public class ChangeSexActivity extends BaseActivity implements View.OnClickListener{

    View boyLayout,girlLayout;

    Intent lastIntent;

    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_sex);

        lastIntent = getIntent();
        setUi();

    }

    private void setUi()
    {
        boyLayout = findViewById(R.id.change_sex_boy_layout);
        girlLayout = findViewById(R.id.change_sex_girl_layout);

        boyLayout.setOnClickListener(this);
        girlLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.change_sex_boy_layout)
            type = "1";
        else
            type = "0";
        lastIntent.putExtra(PersonInfoContent.CHANGE_SEX_TAG,type);
        setResult(RESULT_OK,lastIntent);
        finish();
    }
}
