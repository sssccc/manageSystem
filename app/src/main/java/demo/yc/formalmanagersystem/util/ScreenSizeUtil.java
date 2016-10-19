package demo.yc.formalmanagersystem.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/7/14 0014.
 */
public class ScreenSizeUtil {

    private static final DisplayMetrics dm = new DisplayMetrics();

    //获得的是手机像素密度  px
    public static int getScreenWidth(Activity activity)
    {
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    public static int getScreenHeight(Activity activity)
    {
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

}
