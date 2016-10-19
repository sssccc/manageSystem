package demo.yc.formalmanagersystem.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class NetWorkUtil {

    private static NetWorkUtil util;
    private static ConnectivityManager manager;
    private static NetworkInfo info;

    public static NetWorkUtil getInstance(Context context)
    {
        if(util == null)
        {
            util = new NetWorkUtil();
            manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = manager.getActiveNetworkInfo();
        }

        return util;

    }

    public  static boolean isWork()
    {
        if(info != null && info.isAvailable())
            return true;
        return false;
    }
    public  static boolean isWifi()
    {
        if(isWork()) {
            NetworkInfo.State state = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if(state == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }
    public  static boolean isPhone()
    {
        if(isWork()) {
            NetworkInfo.State state = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if(state == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }

    public static void refreshNet(Context context)
    {
        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = manager.getActiveNetworkInfo();
    }







}
