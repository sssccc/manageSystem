package demo.yc.formalmanagersystem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.util.DateUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class Time_Frag_5_6 extends DayBaseFrag{


    View layout;
    View view;
    TextView title,cate,isFixed;
    Plan p;
    public Time_Frag_5_6() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_time_frag_5_6, container, false);
        setUi();
       // setData();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)(getParentFragment().getActivity())).startIntetnToTimeManager();

            }
        });
        return view;
    }

    private void setUi()
    {
        layout = view.findViewById(R.id.layout_5_6);
        cate = (TextView) view.findViewById(R.id.layout_5_6_type);
        title = (TextView) view.findViewById(R.id.layout_5_6_title);
        isFixed = (TextView) view.findViewById(R.id.layout_5_6_isFixed);

    }


    private void setData()
    {
        p = ((HomePageFrag)getParentFragment()).sendTodayPlan(3);
        Log.w("PATH", p == null ? "yyYY" : "nnNN");

        if(p != null) {
            String cate_s = DateUtil.getPlanCateName(p.getType());
            cate.setText(cate_s);
            title.setText(p.getTitle());
            String isFixed_s = p.getIsFixed() == 0 ? "今天" : "每周";
            isFixed.setText(isFixed_s);
        }
    }

    @Override
    public void myRefresh(int type) {

    }
}
