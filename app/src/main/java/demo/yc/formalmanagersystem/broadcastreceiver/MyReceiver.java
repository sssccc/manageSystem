package demo.yc.formalmanagersystem.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2016/7/29.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String json = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            JSONTokener jsonTokener  = new JSONTokener(json);
            String content = null;
            try {
                JSONObject object = (JSONObject) jsonTokener.nextValue();
                content = object.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (content){
                case "com.managementsystem.apply_repair":

                    break;
            }
        }
    }
}
