package demo.yc.formalmanagersystem.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.Plan;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class DayBaseFrag extends Fragment {


    public abstract void myRefresh(int type);


}
