package demo.yc.formalmanagersystem.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.activity.UpdatePersonInfoActivity;
import demo.yc.formalmanagersystem.contentvalues.PersonInfoContent;
import demo.yc.formalmanagersystem.models.Person;
import demo.yc.formalmanagersystem.util.FileUtil;
import demo.yc.formalmanagersystem.util.PersonUtil;
import demo.yc.formalmanagersystem.view.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonInfoFrag extends Fragment {

    View view;
    PersonListener listener ;
    Button updateBtn;
    ImageView sex;
    TextView name,age,number,institute,major,class_num,position;
    CircleImageView headPhoto;
    Person p;

    public PersonInfoFrag(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_person_info, container, false);
        setUi();
        setData();
        setListener();
        return view;
    }

    //寻找id
    private void setUi()
    {
        updateBtn = (Button) view.findViewById(R.id.info_update_Btn);
        name = (TextView) view.findViewById(R.id.info_name);
        age = (TextView) view.findViewById(R.id.info_age);
        institute = (TextView) view.findViewById(R.id.info_institute);
        major = (TextView) view.findViewById(R.id.info_major);
        class_num = (TextView) view.findViewById(R.id.info_class);
        position = (TextView) view.findViewById(R.id.info_position);
        number = (TextView) view.findViewById(R.id.info_number);
        sex = (ImageView) view.findViewById(R.id.info_sex);
        headPhoto = (CircleImageView) view.findViewById(R.id.info_head);
    }
    private void setListener()
    {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UpdatePersonInfoActivity.class);
                i.putExtra(PersonInfoContent.UPTADE_PERSON_INFO_TAG,p);
                startActivityForResult(i,PersonInfoContent.UPDATE_PERSON_INFO);
            }
        });
    }

    //获取主界面传递过来的参数
    private void setData()
    {
        p = ((MainActivity)getActivity()).SendPersonInfo();
        setUiData();
    }

    //设置该界面的信息
    private void setUiData()
    {
        name.setText(p.getName());
        age.setText(p.getAge()+"岁");
        institute.setText(p.getCollege());
        class_num.setText(p.getClazz()+"班");
        major.setText(p.getMajor());
        number.setText(p.getStudentId());
        position.setText(PersonUtil.getPositonName(p.getQuartersId()));
        if(p.getSex().contains("1"))
            sex.setImageResource(R.drawable.boy);
        else
            sex.setImageResource(R.drawable.girl);
        if(!MyApplication.getPersonHeadPath().equals(""))
            Glide.with(getContext()).load(MyApplication.getPersonHeadPath()).into(headPhoto);
        else {
            String tempPath = FileUtil.getUserImagePath(MyApplication.getUser().getId());
            File file = new File(tempPath);
            if (file.exists())
                Glide.with(this).load(tempPath).into(headPhoto);
            else
                Glide.with(this).load(p.getPicture()).into(headPhoto);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       // Toast.makeText(getContext(),requestCode+"..."+PersonInfoContent.UPDATE_PERSON_INFO,Toast.LENGTH_LONG).show();
        if(requestCode == PersonInfoContent.UPDATE_PERSON_INFO) {
            if(data != null) {
                Person pp = (Person) data.getSerializableExtra(PersonInfoContent.UPTADE_PERSON_INFO_TAG);
                if(pp == null)
                {
                    Glide.with(getContext()).load(MyApplication.getPersonHeadPath()).into(headPhoto);
                    if (listener != null)
                        listener.updatePersonInfo();
                }
                else {
                    p = pp;
                    setUiData();
                    if(data.getBooleanExtra("isChange",false)) {
                        Glide.with(getContext()).load(MyApplication.getPersonHeadPath()).into(headPhoto);
                        if (listener != null)
                            listener.updatePersonInfo();
                    }
                    if (listener != null)
                        listener.updatePersonInfo(p);
                }
            }
        }
    }

    public interface PersonListener
    {
        void updatePersonInfo(Person p);
        void updatePersonInfo();
    }
}
