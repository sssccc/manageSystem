package demo.yc.formalmanagersystem.fragment;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.util.ScreenSizeUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskManageFrag extends Fragment implements View.OnClickListener{


    MainActivity activity;
    View view;
    public ArrayList<TaskBaseFrag> list = new ArrayList<>();
    FragmentManager fm;
    FragmentTransaction ft;
    int currentPager = 0;



    //底部切换布局，图片，text
    View[] layouts ;
    View[] images;
    TextView[] texts;
    View line;
    String[] titles = {"所有任务","参与任务","历史任务","放弃任务"};

    int screenWidth ;
    int perWidth;
    int startPos;



//    //底部图片切换
//    int[] btn_images ={R.drawable.task_ring_green};
//
//    //底部字体颜色
//    String[] btn_text = {"#fff","#aaa"};


    public TaskManageFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_info, container, false);
        fm = getChildFragmentManager();
        setUi();
        setData();
        setListener();
        showFragments(0);
        return view;
    }

    private void setUi() {
        layouts = new View[]
                {
                        view.findViewById(R.id.task_info_all_layout),
                        view.findViewById(R.id.task_info_involve_layout),
                        view.findViewById(R.id.task_info_history_layout),
                        view.findViewById(R.id.task_info_quit_layout)
                };
        images = new View[]
                {
                        view.findViewById(R.id.task_info_all_image),
                        view.findViewById(R.id.task_info_involve_image),
                        view.findViewById(R.id.task_info_history_image),
                        view.findViewById(R.id.task_info_quit_image)
                };
        texts = new TextView[]
                {
                        (TextView) view.findViewById(R.id.task_info_all_text),
                        (TextView) view.findViewById(R.id.task_info_involve_text),
                        (TextView) view.findViewById(R.id.task_info_history_text),
                        (TextView) view.findViewById(R.id.task_info_quit_text)
                };

        line = view.findViewById(R.id.task_line);
        screenWidth = ScreenSizeUtil.getScreenWidth(getActivity());
        perWidth = screenWidth/texts.length;

      //  View params = new FrameLayout.LayoutParams(perWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(perWidth,5);

        FrameLayout.LayoutParams  params = (FrameLayout.LayoutParams) line.getLayoutParams();
        params.width = perWidth;
        line.setLayoutParams(params);
        //LinearLayout.LayoutParams(perWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
     //   line.setLayoutParams(params);
        //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(perWidth, 5);
      //  line.setLayoutParams(params);
        //line.setLayoutParams(new ViewGroup.LayoutParams(perWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
    }



    private void setData() {
        list.add(new Task_All_Frag());
        list.add(new Task_Involve_Frag());
        list.add(new Task_History_Frag());
        list.add(new Task_Quit_Frag());
    }

    private void showFragments(int index)
    {
        ft = fm.beginTransaction();
        changeLineIndex(index);

        for(int i=0;i<list.size();i++)
        {
            if(i==index)
            {
                if(!list.get(i).isAdded())
                    ft.add(R.id.task_frame_layout,list.get(i));
                else
                    ft.show(list.get(i));
            }else
            {
                if(list.get(i).isAdded())
                    ft.hide(list.get(i));
            }
        }
        ft.commit();
        setBtnBackground(index);

    }

    private void setListener() {
       for(int i=0;i<layouts.length;i++)
       {
           layouts[i].setOnClickListener(this);
       }
    }


    private void setBtnBackground(int index)
    {
        for(int i=0;i<layouts.length;++i)
        {
            if(i == index)
            {
                ((MainActivity)getActivity()).setMyTitle(titles[i]);
                images[i].setBackgroundResource(R.drawable.task_ring_green);
                texts[i].setTextColor(Color.WHITE);
            }
            else
            {
                images[i].setBackgroundResource(R.drawable.task_ring_gray);
                texts[i].setTextColor(Color.GRAY);
            }

        }
    }

    public void setMyTitle(String title)
    {
        activity = (MainActivity) getActivity();
        activity.setMyTitle(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.task_info_all_layout:
                currentPager = 0;
                break;
            case R.id.task_info_involve_layout:
                currentPager = 1;
                break;
            case R.id.task_info_history_layout:
                currentPager = 2;
                break;
            case R.id.task_info_quit_layout:
                currentPager = 3;
                break;
        }
        showFragments(currentPager);
    }

    private void changeLineIndex(int index)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(line,View.TRANSLATION_X,startPos*perWidth,index*perWidth);

        animator.start();
        startPos = index;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( data!= null && data.getExtras()!= null)
        {
            Toast.makeText(getContext(),"TaskManager....",Toast.LENGTH_LONG).show();
            int status = data.getExtras().getInt("status");
            list.get(status).myRefresh(requestCode,data);
        }
       // return ;
        //super.onActivityResult(requestCode,resultCode,data);
    }
}