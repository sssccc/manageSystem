package demo.yc.formalmanagersystem.util;

import android.app.Activity;
import android.content.Intent;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import demo.yc.formalmanagersystem.MainActivity;

/**
 * Created by Administrator on 2016/7/31.
 */
public class QR_Util {

    public static void startQRScan(Activity activity){
        activity.startActivityForResult(new Intent(activity, CaptureActivity.class), MainActivity.QR_SCAN);
    }

}
