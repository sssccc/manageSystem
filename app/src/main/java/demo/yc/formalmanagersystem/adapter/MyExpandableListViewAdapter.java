package demo.yc.formalmanagersystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.activity.TimeManageActivity;
import demo.yc.formalmanagersystem.activity.UpdatePlanInfoActivity;
import demo.yc.formalmanagersystem.contentvalues.ColorContent;
import demo.yc.formalmanagersystem.contentvalues.TimePlanContent;
import demo.yc.formalmanagersystem.models.Plan;

/**
 * Created by user on 2016/7/25.
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    LayoutInflater inflater;
    ArrayList<Plan> planList;
    Context context;
    View item;
    TextView time;
    TextView title;
    TextView type;
    View line;


    TextView describe;
    TextView isFixed;
    Button updateBtn;


    public MyExpandableListViewAdapter(Context context,ArrayList<Plan> planList)

    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.planList = planList;
    }
    @Override
    public int getGroupCount()
    {
        if(planList == null)
            return 0;
        return planList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return planList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return item;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//         int duration =100;
//         duration = duration*(groupPosition+1);

            Plan pp = planList.get(groupPosition);
            convertView = inflater.inflate(R.layout.expandable_group,parent,false) ;
            time = (TextView) convertView.findViewById(R.id.ex_group_time);
            title = (TextView) convertView.findViewById(R.id.ex_group_title);
            type = (TextView) convertView.findViewById(R.id.ex_group_type);
            line = convertView.findViewById(R.id.ex_group_line);
          setGroupDate(pp,groupPosition);
//        Animation animation = AnimationUtils.loadAnimation(context,R.anim.group_expandable_anim);
//        animation.setStartOffset(duration);
//        convertView.setAnimation(animation);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            Plan p = planList.get(groupPosition);
            convertView = inflater.inflate(R.layout.expandable_item, parent, false);
            describe = (TextView) convertView.findViewById(R.id.ex_item_describe);
            isFixed = (TextView) convertView.findViewById(R.id.ex_item_isFixed);
            updateBtn = (Button) convertView.findViewById(R.id.ex_item_updateBtn);
            item = convertView;

        setItemDate(p);

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.expandable_anim);
        convertView.setAnimation(animation);
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    private void setGroupDate(final Plan p,int groupPosition)
    {
        //设置第几节课
        switch (p.getDayTime())
        {
            case 1:
                time.setText("1,2");
                break;
            case 2:
                time.setText("3,4");
                break;
            case 3:
                time.setText("5,6");
                break;
            case 4:
                time.setText("7,8");
                break;
            case 5:
                time.setText("9,10");
                break;
            case 6:
                time.setText("11,12,13");
                break;
        }
        //设置行程标题
        title.setText(p.getTitle());
        //设置间隔线颜色
        line.setBackgroundColor(Color.parseColor(ColorContent.colors[groupPosition]));
        //设置行程性质
        if(p.getIsFree() == 0)
        {
            type.setText("空闲");
            type.setBackgroundColor(Color.parseColor("#33ff0000"));
        }else {
            if (p.getType() == 0)
                type.setText("课程");
            else if (p.getType() == 1)
                type.setText("项目");
            else if (p.getType() == 2)
                type.setText("个人");
            type.setBackgroundColor(Color.parseColor(ColorContent.colors[p.getType()]));
        }
    }

    private void  setItemDate(final Plan p)
    {
        describe.setText(p.getContent());
        if(p.getIsFixed() ==0)
            isFixed.setText("单天");
        else if(p.getIsFixed() ==1)
            isFixed.setText("每周");

        //将新的数据上传到服务器
        //根据p的id将数据库的内容更新
        //然后通知fragment 将显示的内容更新
        updateBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, UpdatePlanInfoActivity.class);
            i.putExtra(TimePlanContent.UPDATE_PLAN_INFO_TAG,p);
            ((TimeManageActivity)context).startActivityForResult(i, TimePlanContent.UPDATE_PLAN_INFO_CODE);
            }
        });

    }

}
