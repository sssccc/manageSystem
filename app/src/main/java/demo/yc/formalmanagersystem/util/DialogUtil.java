package demo.yc.formalmanagersystem.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import demo.yc.formalmanagersystem.view.DialogCancelListener;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class DialogUtil {

    private static ProgressDialog pd;
    public static ProgressDialog showDialog(Context context, String content)
    {
        if(pd == null) {
            pd = new ProgressDialog(context);
        }
        else
        {
            if (pd.getContext() != context)
                pd = new ProgressDialog(context);
        }
        if(content != null)
            pd.setMessage(content);
        return pd;
    }

    public static void dissmiss()
    {
        if(pd != null)
            pd.dismiss();
    }

    public static void onCancelListener(final DialogCancelListener listener)
    {
        if(pd == null)
            return;
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                listener.onCancel();
            }
        });
    }



}
