package demo.yc.formalmanagersystem.util;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.models.Person;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.models.Task;

/**
 * Created by Administrator on 2016/8/2 0002.
 */
public class ThreadUtil
{

    Context context;
    public ThreadUtil(Context context){
        this.context = context;
    }
    Handler handler = new Handler();

    public void getPersonInfo(final String account)
    {
        new Thread()
        {
            @Override
            public void run() {
                MyDBHandler db = MyDBHandler.getInstance(context);
                final Person p = db.getPersonInfoFromTable(account);
                if(p == null)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            error(1);
                        }
                    });
                else
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            success(1, p);
                        }
                    });
                }
            }
        }.start();
    }

    public void getTaskInfo(final String account,final int status)
    {
        new Thread()
        {
            @Override
            public void run() {
                MyDBHandler db = MyDBHandler.getInstance(context);
                final ArrayList<Task> list = db.getTaskInfoFromTable(account,status);
                if(list == null )
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            error(2);
                        }
                    });
                else
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            success(2,list);
                        }
                    });
            }
        }.start();
    }

    public void getPlanInfo(final String account)
    {
        new Thread()
        {
            @Override
            public void run() {
                MyDBHandler db = MyDBHandler.getInstance(context);
                final ArrayList<Plan> list = db.getPlanInfoFromTable(account);
                if(list.size() == 0)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            error(3);
                        }
                    });
                else
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            success(3,list);
                        }
                    });
            }
        }.start();
    }

    public void getSubPlanInfo(final String account,final int today)
    {
        new Thread()
        {
            @Override
            public void run() {
                MyDBHandler db = MyDBHandler.getInstance(context);
                final ArrayList<Plan> list = db.getSubPlanInfoFromTable(account,today);
                if(list.size() == 0)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            error(4);
                        }
                    });
                else
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            success(4,list);
                        }
                    });
            }
        }.start();
    }

    public void error(int code)
    {

    }

    public void success(int code,Object obj)
    {

    }
}
