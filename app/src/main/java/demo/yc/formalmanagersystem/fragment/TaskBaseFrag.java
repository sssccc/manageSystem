package demo.yc.formalmanagersystem.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TaskBaseFrag extends Fragment {

    public abstract void myDelete(int pos,int flag);

    public abstract void myFinish(int pos,int flag);

    public abstract void myRefresh(int requestCode,Intent intent);



}
