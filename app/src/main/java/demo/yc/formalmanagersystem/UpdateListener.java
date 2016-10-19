package demo.yc.formalmanagersystem;

import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface UpdateListener {
    void onSucceed(String s);
    void onError(VolleyError error);
}
