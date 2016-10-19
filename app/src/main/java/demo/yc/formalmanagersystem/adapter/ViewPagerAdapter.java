package demo.yc.formalmanagersystem.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.fragment.DayBaseFrag;

/**
 * Created by user on 2016/7/23.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    private ArrayList<DayBaseFrag> list = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm,ArrayList<DayBaseFrag> list) {
        super(fm);
        this.list = list;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}