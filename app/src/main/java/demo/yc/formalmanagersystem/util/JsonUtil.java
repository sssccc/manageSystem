package demo.yc.formalmanagersystem.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import demo.yc.formalmanagersystem.database.MyDBHandler;
import demo.yc.formalmanagersystem.models.Person;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.models.Property;
import demo.yc.formalmanagersystem.models.Purchase;
import demo.yc.formalmanagersystem.models.Repair;
import demo.yc.formalmanagersystem.models.Task;

/**
 * Created by Administrator on 2016/8/3.
 */
public class JsonUtil {

    /***
     * 解析Json数据，转化为Property对象，并存入本地数据库
     *
     * @param context
     * @param jsonData
     */
    public static void parsePropertyJson(Context context, String jsonData) {
        List<Property> properties = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                Property property = new Property();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                property.setBrand(jsonObject.getString("brand"));
                property.setModel(jsonObject.getString("model"));
                property.setName(jsonObject.getString("name"));
                property.setPrice(jsonObject.getString("price"));
                property.setCate(jsonObject.getString("cate"));
                property.setIdentifier(jsonObject.getString("identifier"));
                property.setProvider(jsonObject.getString("provider"));
                property.setProviderTel(jsonObject.getString("providerTel"));
                property.setBorrowedProperty(jsonObject.getInt("isBorrowedProperty") == 0 ? false : true);
                property.setDate(jsonObject.getString("date"));
                properties.add(property);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyDBHandler.getInstance(context).updateProperty(context, properties);
    }


    /***
     * 解析Json数据，转化为Repair对象，并存入本地数据库
     *
     * @param context
     * @param jsonData
     */
    public static void parseRepairJson(Context context, String jsonData) {
        List<Repair> repairList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                Repair repair = new Repair();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                repair.setRepairState(jsonObject.getString("repairState"));
                repair.setIdentifier(jsonObject.getString("identifier"));
                repair.setCheckState(jsonObject.getString("checkState"));
                repair.setApplyTime(jsonObject.getString("applyTime"));
                repair.setFinishTime(jsonObject.getString("finishTime"));
                repair.setCreaterIdentifier(jsonObject.getString("createrIdentifier"));
                repair.setDescribe(jsonObject.getString("describe"));
                repairList.add(repair);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyDBHandler.getInstance(context).updateRepair(context, repairList);
    }

    /***
     * 解析Json数据，转化为Purchase对象，并存入本地数据库
     *
     * @param context
     * @param jsonData
     */
    public static void parsePurchaseJson(Context context, String jsonData) {
        List<Purchase> purchases = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                Purchase purchase = new Purchase();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                purchase.setBrand(jsonObject.getString("brand"));
                purchase.setModel(jsonObject.getString("model"));
                purchase.setName(jsonObject.getString("name"));
                purchase.setPrice(jsonObject.getString("price"));
                purchase.setCheckState(jsonObject.getString("checkState"));
                purchase.setPurchaseState(jsonObject.getString("purchaseState"));
                purchase.setApplyTime(jsonObject.getString("applyTime"));
                purchase.setFinishTime(jsonObject.getString("finishTime"));
                purchase.setCreaterIdentifier(jsonObject.getString("createrIdentifier"));
                purchase.setDescribe(jsonObject.getString("describe"));
                purchases.add(purchase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyDBHandler.getInstance(context).updatePurchase(context, purchases);
    }

    //获取一天或者一个周的任务安排
    public static ArrayList<Plan> parsePlanJson(String result)
    {
        ArrayList<Plan> planList ;
        Gson gson = new Gson();
        planList = gson.fromJson(result, new TypeToken<List<Plan>>() {
        }.getType());
        if(planList == null)
            planList = new ArrayList<>();
        return planList;
    }

    //获取一个人的信息
    public static Person parsePersonJson(String result)
    {
        Person person ;
        Gson gson = new Gson();
        person = gson.fromJson(result,Person.class);
        return person;
    }

    //获取多个task的简易情况
    public static ArrayList<Task> parseTaskJson(String result)
    {
        ArrayList<Task> taskList ;
        Gson gson = new Gson();
        taskList = gson.fromJson(result, new TypeToken<List<Task>>() {
        }.getType());
        if(taskList == null)
            taskList = new ArrayList<>();
        return taskList;
    }

    //获取一个task的详细信息
    public static Task parseSingleJson(String result)
    {
        Task task = new Gson().fromJson(result,Task.class);
        return task;
    }









}
