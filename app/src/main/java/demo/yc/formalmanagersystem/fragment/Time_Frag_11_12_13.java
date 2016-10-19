package demo.yc.formalmanagersystem.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.Plan;

/**
 * A simple {@link Fragment} subclass.
 */
public class Time_Frag_11_12_13  extends DayBaseFrag {

    View layout;
    TextView title,cate,isFixed;
    View view;
    public Time_Frag_11_12_13() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_time_frag_11_12_13, container, false);
        setUi();
      //  setData();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)(getParentFragment().getActivity())).startIntetnToTimeManager();

            }
        });
        return view;
    }

    private void setData()
    {
        Plan p = ((HomePageFrag)getParentFragment()).sendTodayPlan(6);
        String cate_s = p.getType() == 0 ?  "课程" :((p.getType() == 1) ? "项目" : "个人") ;
        cate.setText(cate_s);
        title.setText(p.getTitle());
        String isFixed_s = p.getIsFixed() == 0 ? "今天" : "每周" ;
        isFixed.setText(isFixed_s);
    }
    private void setUi()
    {
        layout = view.findViewById(R.id.layout_11_12_13);
        cate = (TextView) view.findViewById(R.id.layout_11_12_13_type);
        title = (TextView) view.findViewById(R.id.layout_11_12_13_title);
        isFixed = (TextView) view.findViewById(R.id.layout_11_12_13_isFixed);
    }


    @Override
    public void myRefresh(int type) {

    }
}
