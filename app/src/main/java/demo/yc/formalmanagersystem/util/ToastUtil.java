package demo.yc.formalmanagersystem.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ToastUtil {
    private static Toast toast;

    public static void createToast(Context context,String content) {
        if(toast == null){
            toast = Toast.makeText(context,content,Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }
}
