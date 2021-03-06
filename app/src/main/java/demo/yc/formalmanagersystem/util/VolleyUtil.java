package demo.yc.formalmanagersystem.util;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.models.Person;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.models.Property;
import demo.yc.formalmanagersystem.models.Purchase;
import demo.yc.formalmanagersystem.models.Repair;
import demo.yc.formalmanagersystem.models.TaskProcess;

/**
 * Created by Administrator on 2016/8/2.
 */
public class VolleyUtil {


    public VolleyUtil() {
    }


    //public static final String ROOT_URL = "";
    public static final String ROOT_URL = "http://192.168.1.124:8888/";
    private static final String BASE_URL = ROOT_URL+"property/";


    /***
     * 从服务器获取数据，更新本地数据库
     *
     * @param table    所需要获取的数据的表名
     * @param listener 操作更新的回调
     */
    public synchronized void updateSQLiteFromMySql(String table, final UpdateListener listener) {
        String url1 = BASE_URL + "show" + table+"?role="+MyApplication.getRole();
        StringRequest request1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                listener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
        request1.addMarker(table);
        MyApplication.getInstance().getMyQueue().add(request1);
    }

    /***
     * 提交Repair记录到服务器(Add)
     *
     * @param repair         待提交的repair 对象
     * @param updateListener 操作更新的回调
     */
    public synchronized void addRepairToMySql(final Repair repair, final UpdateListener updateListener) {
        final String identifier = repair.getIdentifier();
        final String applyTime = repair.getApplyTime();
        final String finishTime = repair.getFinishTime();
        final String createrIdentifier = repair.getCreaterIdentifier();
        final String checkState = repair.getCheckState();
        final String repairState = repair.getRepairState();
        final String describe = repair.getDescribe();


        String url2 = BASE_URL+"addrepair";
        StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("identifier", identifier);
                map.put("repairState", repairState);
                map.put("checkState", checkState);
                map.put("applyTime", applyTime);
                map.put("describe", describe);
                map.put("finishTime", finishTime);
                map.put("createrIdentifier", createrIdentifier);
                map.put("role",MyApplication.getRole());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request2.addMarker("updaterepair");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /***
     * 提交Purchase记录到服务器（Add）
     *
     * @param purchase       待提交的Purchase对象
     * @param updateListener 操作更新的回调
     */
    public synchronized void addPurchaseToMySql(final Purchase purchase, final UpdateListener updateListener) {
        final String name = purchase.getName();
        final String brand = purchase.getBrand();
        final String price = purchase.getPrice();
        final String model = purchase.getModel();
        final String createrIdentifier = purchase.getCreaterIdentifier();
        final String describe = purchase.getDescribe();

        String url2 = BASE_URL+"addpurchase";
        StringRequest request3 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("brand", brand);
                map.put("price", price);
                map.put("model", model);
                map.put("describe", describe);
                map.put("createrIdentifier", createrIdentifier);
                map.put("role",MyApplication.getRole());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request3.addMarker("updaterpurchase");
        MyApplication.getInstance().getMyQueue().add(request3);
    }

    /***
     * 提交Property记录到服务器(Update)
     *
     * @param property       待提交的property 对象
     * @param updateListener 操作更新的回调
     */
    public synchronized void updatePropertyInMySql(final Property property, final UpdateListener updateListener) {
        final String identifier = property.getIdentifier();
        final String name = property.getName();
        final String cate = property.getCate();
        final String brand = property.getBrand();
        final String model = property.getModel();
        final String price = property.getPrice();
        final String date = property.getDate();
        final int isBorrowedProperty = property.isBorrowedProperty() == true ? 1 : 0;
        final String provider = property.getProvider();
        final String providerTel = property.getProviderTel();
        final String repairState = property.getRepairStatus();
        final String id = property.getId();

        String url2 = BASE_URL+"updateproperty";
        StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
        }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("identifier", identifier);
                map.put("repairState", repairState);
                map.put("name", name);
                map.put("cate", cate);
                map.put("model", model);
                map.put("price", price);
                map.put("date", date);
                map.put("isBorrowedProperty", isBorrowedProperty + "");
                map.put("provider", provider);
                map.put("providerTel", providerTel);
                map.put("brand", brand);
                map.put("id", id);
                map.put("role",MyApplication.getRole());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request2.addMarker("updateproperty");
        MyApplication.getInstance().getMyQueue().add(request2);
    }


    /***
     * 根据createrIdentifier获取名字
     * @param createrIdentifier
     * @param updateListener
     */
    public synchronized void getCreaterName(String createrIdentifier, final UpdateListener updateListener) {

        String url2 = BASE_URL+"get/name?studentId="+createrIdentifier+"&role="+MyApplication.getRole();
        StringRequest request3 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) ;

        request3.addMarker("getName");
        MyApplication.getInstance().getMyQueue().add(request3);
    }

    /***
     * 根据createrIdentifier获取名字
     * @param identifier
     * @param updateListener
     */
    public synchronized void getRepairStatus(String identifier, final UpdateListener updateListener) {

        String url2 = BASE_URL+"get/repairstate?identifier="+identifier+"&role="+MyApplication.getRole();
        StringRequest request3 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) ;

        request3.addMarker("getName");
        MyApplication.getInstance().getMyQueue().add(request3);
    }

    /***
     * 根据createrIdentifier获取名字
     * @param identifier
     * @param updateListener
     */
    public synchronized void getPropertyName(String identifier, final UpdateListener updateListener) {

        String url2 = BASE_URL+"get/name?studentId="+identifier+"&role="+MyApplication.getRole();
        StringRequest request3 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) ;

        request3.addMarker("getName");
        MyApplication.getInstance().getMyQueue().add(request3);
    }

    //以上是伟钊的
    //-----------------------------------------------------------------------------
    //以下是我的

    /**
     *传递一个plan对象过来
     * @param plan
     * @param updateListener
     */
    public synchronized void updatePlanInfo(final Plan plan, final UpdateListener updateListener) {
        String url2 = ROOT_URL+"plan/update";
        StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("title", plan.getTitle());
                map.put("content", plan.getContent());
                map.put("isFixed", plan.getIsFixed()+"");
                map.put("type", plan.getType()+"");
                map.put("isFree", plan.getIsFree()+"");
                map.put("id",plan.getId());
                map.put("weekDay", plan.getWeekDay()+"");
                map.put("dayTime",plan.getDayTime()+"");
                map.put("role",MyApplication.getRole());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request2.setTag("updatePlanInfo");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     *
     *传递一个person对象过来
     * @param person
     * @param updateListener
     */
    public synchronized void updatePersonInfo(final Person person, final UpdateListener updateListener) {
        String url2 = ROOT_URL+"information/update";
        StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.w("person","save"+s);
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.w("person","save error"+volleyError.toString());
                updateListener.onError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", person.getName());
                map.put("studentId", person.getStudentId());
                map.put("sex", person.getSex());
                map.put("age", person.getAge()+"");
                map.put("college", person.getCollege());
                map.put("major", person.getMajor());
                map.put("clazz", person.getClazz());
                map.put("quartersId", person.getQuartersId());
                map.put("id", person.getId());
                map.put("role",MyApplication.getRole());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request2.setTag("updatePersonInfo");
        MyApplication.getInstance().getMyQueue().add(request2);
    }



    //更新任务的操作
    //放弃未参与的的任务，放弃正在参与的任务， task/quit
    //接收未参与的任务，完成正在参与的任务      task/finish
    //删除已完成任务信息，删除已放弃任务信息     task/delete
    /**
     *
     * @param taskId
     * @param updateListener
     */
    public synchronized void quitTask(final String taskId,final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/quit";
        StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId",MyApplication.getUser().getId());
                map.put("taskId",taskId);
                map.put("role",MyApplication.getRole());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request2.setTag("quitTask");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    //参与taken = 1
    //完成 status = 1
    public synchronized void finishTask(final String taskId, final  int flag,final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/finish";
        StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                //参与
                if(flag == 0)
                {
                    map.put("taken",1+"");
                    map.put("userId",MyApplication.getUser().getId());
                }else {
                    map.put("status",1+"");
                }

                map.put("taskId",taskId);
                map.put("role",MyApplication.getRole());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request2.setTag("finishTask");
        MyApplication.getInstance().getMyQueue().add(request2);
    }


    /**
     * 根据账号id 获取用户信息
     * @param userId
     * @param updateListener
     */
    public synchronized void getPersonInfo(final String userId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"information/show?userId="+userId+"&role="+MyApplication.getRole();
       // Toast.makeText(MyApplication.getContext(),userId,Toast.LENGTH_SHORT).show();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
               // Toast.makeText(MyApplication.getContext(),s,Toast.LENGTH_SHORT).show();
                Log.w("person",s);
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.addMarker("getPersonInfo");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     * 根据用户的id 以及 星期几，获取该用户星期几的行程安排
     * @param userId
     * @param weekDay
     * @param updateListener
     */
    public synchronized void getSingleDayPlan(final String userId, int weekDay, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"plan/getday?userId="+userId+"&weekDay="+weekDay+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.addMarker("getSingleDayPlan");
        MyApplication.getInstance().getMyQueue().add(request2);
    }


    /**
     * 根据用户的id  获取用户一个星期的行程安排
     * @param userId
     * @param updateListener
     */
    public synchronized void getAllDayPlan(final String userId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"plan/get?userId="+userId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.setTag("getAllDayPlan");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     * 获取所有人可以参与的任务
     * @param updateListener
     */
    public synchronized void getAllAcceptableTaskList(final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/untakenteamtask?userId="+MyApplication.getUser().getId()+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.setTag("getAllAcceptableTaskList");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     * 根据用户id 获取 用户可参与的任务
     * @param userId
     * @param updateListener
     */
    public synchronized void getMyAcceptableTaskList(final String userId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/untakenprojecttask?userId="+userId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.setTag("getMyAcceptableTaskList");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     * 根据用户Id 获取所有人已参与的任务
     * @param userId
     * @param updateListener
     */
    public synchronized void getAllInvolveTaskList(final String userId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/acceptedteamtask?userId="+userId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.setTag("getAllInvolveTaskList");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     * 根据用户Id 获取用户已参与的项目组任务
     * @param userId
     * @param updateListener
     */
    public synchronized void getMyInvolveTaskList(final String userId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/acceptedprojecttask?userId="+userId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.setTag("getMyInvolveTaskList");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     * 在homepage 获取
     * 根据用户Id 获取用户已参与的所有人组和项目组任务
     * @param userId
     * @param updateListener
     */
    public synchronized void getAllMyInvolveTaskList(final String userId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/accepted/get?userId="+userId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.setTag("getAllMyInvolveTaskList");
        MyApplication.getInstance().getMyQueue().add(request2);
    }


    /**
     * 根据用户的id获取已经完成的任务
     * @param userId
     * @param updateListener
     */
    public synchronized void getHistoryTaskList(final String userId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/history?userId="+userId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }

        });
        request2.setTag("getHistoryTaskList");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     * 根据用户的id查找用户已经放弃的任务
     * @param userId
     * @param updateListener
     */
    public synchronized void getQuitTaskList(final String userId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"/task/quitpage?userId="+userId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        });
        request2.setTag("getQuitTaskList");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    /**
     * 根据task的id获取任务的详细信息
     * @param taskId
     * @param updateListener
     */
    public synchronized void getTaskDetail(final String taskId, final UpdateListener updateListener) {

        String url2 = ROOT_URL+"task/taskdetail?taskId="+taskId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        });
        request2.setTag("GetTaskDetail");
        MyApplication.getInstance().getMyQueue().add(request2);
    }

    public synchronized void getTaskProcessRecord(final String taskId,final UpdateListener updateListener)
    {

        String url2 = ROOT_URL+"task/scheduledetail?taskId="+taskId+"&role="+MyApplication.getRole();
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        });
        request2.setTag("getTaskPrecess");
        MyApplication.getInstance().getMyQueue().add(request2);
    }
    public synchronized void updateTaskProcessRecord(final String taskId, final TaskProcess process, final UpdateListener updateListener)
    {
        String url = ROOT_URL + "task/add/schedule";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                    updateListener.onSucceed(s);
            }
        },
        new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("content",process.getContent());
                map.put("username",process.getusername());
                map.put("userId",MyApplication.getUser().getId());
                map.put("taskId",taskId);
                map.put("role",MyApplication.getRole());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request.setTag("updateTaskProcessRecord");
        MyApplication.getInstance().getMyQueue().add(request);
    }



    /**
     * 登录
     * @param username
     * @param password
     * @param updateListener
     */
    public static void login(final String username,final String password,final UpdateListener updateListener)
    {

       Log.w("login",username+"......"+password);
        String url2 = ROOT_URL+"phone/login";
        StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateListener.onSucceed(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateListener.onError(volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", username);
                map.put("password", password);
                return map;
            }};
        request2.setTag("login");
        MyApplication.getInstance().getMyQueue().add(request2);
    }
}
