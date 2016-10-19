package demo.yc.formalmanagersystem.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.activity.TimeManageActivity;
import demo.yc.formalmanagersystem.adapter.MyExpandableListViewAdapter;
import demo.yc.formalmanagersystem.models.Plan;

/**
 * A simple {@link Fragment} subclass.
 */
public class Day_Frag_FriDay extends DayBaseFrag {

    ArrayList<Plan> planList = new ArrayList<>();
    public Day_Frag_FriDay() {
        // Required empty public constructor
    }

    ExpandableListView exListView;
    MyExpandableListViewAdapter adapter;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setData();
        adapter = new MyExpandableListViewAdapter(getContext(),planList);
        view = inflater.inflate(R.layout.fragment_day_frag_plan, container, false);
        exListView = (ExpandableListView) view.findViewById(R.id.expand_listView);
        exListView.setAdapter(adapter);
        setListener();
        return view;
    }
    private void setCloseGroup(int index)
    {
        for(int i=0;i<adapter.getGroupCount();i++)
        {
            if(i !=index)
            {
                exListView.collapseGroup(i);
            }
        }
    }

    private void setListener()
    {
        exListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                setCloseGroup(groupPosition);
            }
        });
    }

    private void setData()
    {
        planList = ((TimeManageActivity)(getActivity())).sendPlanInfo(5);
//        planList.add(new Plan("课程设计1","java磕碜设计1",1,0,1,1,1));
//        planList.add(new Plan("课程设计2","java磕碜设计2",1,1,1,1,1));
//        planList.add(new Plan("课程设计3","java磕碜设计3",2,2,1,0,1));
//        planList.add(new Plan("课程设计4","java磕碜设计4",2,3,1,1,0));
//        planList.add(new Plan("课程设计5","java磕碜设计5",3,4,1,0,0));
    }


    @Override
    public void myRefresh(int type) {
        ArrayList<Plan> plan = new ArrayList<>();
        switch (type)
        {
            case 0:
                //显示全部
                plan = planList;
                break;
            case 1:
                for(Plan p:planList)
                {
                    if (p.getIsFree() == 0)
                        plan.add(p);
                }
                //显示空闲
                break;
            case 2:
                for(Plan p:planList)
                {
                    if (p.getIsFree() == 1)
                        plan.add(p);
                }
                //显示繁忙
                break;
        }
        adapter = new MyExpandableListViewAdapter(getContext(),plan);
        exListView.setAdapter(adapter);

    }


    @Override
    public void onResume() {

        if(adapter != null) {
            setData();
            adapter.notifyDataSetChanged();
            setCloseGroup(-1);
        }
        super.onResume();

    }

}
