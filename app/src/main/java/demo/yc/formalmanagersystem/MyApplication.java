package demo.yc.formalmanagersystem;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import demo.yc.formalmanagersystem.models.User;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class MyApplication extends Application
{
    private RequestQueue queue;
    private static MyApplication instance;
    private static User user;
    private static Context context;
    private static String role = "";
    private static String personName;

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        MyApplication.role = role;
    }

    private static String personId = "";

    private static String personHeadPath = "";
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        user = new User();
        context = getApplicationContext();
    }

    public static synchronized MyApplication getInstance(){
        return instance;
    }

    public  RequestQueue getMyQueue(){
        if(queue==null){
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        return queue;
    }

    public static User getUser(){
        return user;
    }


    public static Context getContext(){return context;}

    public static String getPersonId() {
        return personId;
    }

    public static void setPersonId(String personId) {
        MyApplication.personId = personId;
    }


    public static String getPersonHeadPath(){
        return personHeadPath;
    }
    public static void setPersonHeadPath(String mPersonHeadPath)
    {
        personHeadPath = mPersonHeadPath;
    }

    public static String getPersonName() {
        return personName;
    }

    public static void setPersonName(String personName) {
        MyApplication.personName = personName;
    }
}

