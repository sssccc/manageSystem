package demo.yc.formalmanagersystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import demo.yc.formalmanagersystem.contentvalues.DBContent;
import demo.yc.formalmanagersystem.models.Person;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.models.Property;
import demo.yc.formalmanagersystem.models.Purchase;
import demo.yc.formalmanagersystem.models.Repair;
import demo.yc.formalmanagersystem.models.Task;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class MyDBHandler implements DataBaseInterface
{

    private static SQLiteDatabase db;
    private static MyDBHandler dbHandler;
    private MyDBHandler(Context context)
    {
        MyDBHelper dbHelper = new MyDBHelper(context, DBContent.DB_NAME,null,DBContent.DB_VERTION);
        db = dbHelper.getWritableDatabase();
    }
    public synchronized static MyDBHandler getInstance(Context context)
    {
        if(dbHandler == null)
        {
            dbHandler = new MyDBHandler(context);
        }
        return dbHandler;
    }

    public synchronized  SQLiteDatabase getDBInstance()
    {
        return db;
    }


    //个人信息
    @Override
    public Person getPersonInfoFromTable(String account) {
        Cursor cursor = db.query(DBContent.TB_PERSON,null,"userId=?",new String[]{account},null,null,null);
        Person p = null;
        if(cursor == null)
            Log.w("PATH","dabase...person---cursor-->"+null+"");
        if(cursor.moveToFirst())
        {
            p = new Person();
            p.setId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
            p.setAge( cursor.getInt(cursor.getColumnIndexOrThrow("age")));
            p.setName( cursor.getString(cursor.getColumnIndexOrThrow("name")));
            p.setSex( cursor.getString(cursor.getColumnIndexOrThrow("sex")));
            p.setStudentId( cursor.getString(cursor.getColumnIndexOrThrow("studentId")));
            p.setPicture( cursor.getString(cursor.getColumnIndexOrThrow("picture")));
            p.setPhotoUrl( cursor.getString(cursor.getColumnIndexOrThrow("photoUrl")));
            p.setClazz( cursor.getString(cursor.getColumnIndexOrThrow("clazz")));
            p.setMajor( cursor.getString(cursor.getColumnIndexOrThrow("major")));
            p.setCollege( cursor.getString(cursor.getColumnIndexOrThrow("college")));
            p.setQuartersId( cursor.getString(cursor.getColumnIndexOrThrow("quartersId")));
        }else
            Log.w("PATH","dabase...person---cursor-->"+"null2"+"");
       // Log.w("PATH","dabase...person---getpeson-->"+p.getId()+"");
        return p;
    }
    @Override
    public void updatePersonInfo(Person p) {
        ContentValues values = new ContentValues();
        values.put("picture",p.getPicture());
        values.put("age",p.getAge());
        values.put("name",p.getName());
        values.put("sex",p.getSex());
        values.put("college",p.getCollege());
        values.put("major",p.getMajor());
        values.put("clazz",p.getClazz());
        values.put("studentId",p.getStudentId());
        values.put("photoUrl",p.getPhotoUrl());
        values.put("quartersId",p.getQuartersId());
        values.put("userId",p.getId());

        db.update(DBContent.TB_PERSON,values,"userId=?",new String[]{p.getId()});
    }
    @Override
    public void addPersonInfo(Person p) {
        if(p != null)
        {
            ContentValues values = new ContentValues();
            values.put("picture",p.getPicture());
            values.put("age",p.getAge());
            values.put("name",p.getName());
            values.put("sex",p.getSex());
            values.put("college",p.getCollege());
            values.put("major",p.getMajor());
            values.put("clazz",p.getClazz());
            values.put("studentId",p.getStudentId());
            values.put("photoUrl",p.getPhotoUrl());
            values.put("quartersId",p.getQuartersId());
            values.put("userId",p.getId());

            int line = (int)db.insert(DBContent.TB_PERSON,null,values);
            Log.w("PATH","dabase...person---add-->"+line+"");
            Log.w("PATH","dabase...person---add-->"+p.getId()+"");
        }
    }



    //行程信息
    @Override
    public ArrayList<Plan> getPlanInfoFromTable(String account)
    {

        Cursor cursor = db.query(DBContent.TB_PLAN,null,"userId=?",new String[]{account},null,null,null);
        ArrayList<Plan> planList = new ArrayList<>();
        while(cursor.moveToNext()) {
            Plan p = new Plan();
            p.setIid(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            p.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            p.setDayTime(cursor.getInt(cursor.getColumnIndexOrThrow("dayTime")));
            p.setWeekDay(cursor.getInt(cursor.getColumnIndexOrThrow("weekDay")));
            p.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));
            p.setUserId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
            p.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            p.setIsFixed(cursor.getInt(cursor.getColumnIndexOrThrow("isFixed")));
            p.setIsFree(cursor.getInt(cursor.getColumnIndexOrThrow("isFree")));

            planList.add(p);
        }
        return planList;
    }

    @Override
    public ArrayList<Plan> getSubPlanInfoFromTable(String account, int today) {
        Cursor cursor = db.query(DBContent.TB_PLAN,null,"userId=? and weekDay=?",new String[]{account,today+""},null,null,null);
        ArrayList<Plan> planList = new ArrayList<>();
        while(cursor.moveToNext()) {
            Plan p = new Plan();
            p.setIid(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            p.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            p.setDayTime(cursor.getInt(cursor.getColumnIndexOrThrow("dayTime")));
            p.setWeekDay(cursor.getInt(cursor.getColumnIndexOrThrow("weekDay")));
            p.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));
            p.setUserId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
            p.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            p.setIsFixed(cursor.getInt(cursor.getColumnIndexOrThrow("isFixed")));
            p.setIsFree(cursor.getInt(cursor.getColumnIndexOrThrow("isFree")));
            planList.add(p);
        }
        return planList;
    }

    @Override
    public void updatePlanInfo(Plan p) {
        ContentValues values = new ContentValues();
        values.put("title",p.getTitle());
        values.put("content",p.getContent());
        values.put("dayTime",p.getDayTime());
        values.put("userId",p.getUserId());
        values.put("isFixed",p.getIsFixed());
        values.put("isFreeed",p.getIsFixed());
        values.put("weekDay",p.getWeekDay());
        values.put("type",p.getType());

       int line =  db.update(DBContent.TB_PLAN,values,"_id=?",new String[]{p.getId()+""});
        if(line>=0)
            Log.w("plan","plan---->plan 保存成功  "+ line+"。。。。"+p.getId());
        else
            Log.w("plan","plan---->plan 保存失败  "+  line+"。。。。"+p.getId());
    }
    @Override
    public boolean addPlanInfo(Plan p) {
        ContentValues values = new ContentValues();
        values.put("title",p.getTitle());
        values.put("content",p.getContent());
        values.put("dayTime",p.getDayTime());
        values.put("userId",p.getUserId());
        values.put("isFixed",p.getIsFixed());
        values.put("isFreeed",p.getIsFixed());
        values.put("weekDay",p.getWeekDay());
        values.put("type",p.getType());

        boolean flag =  db.insert(DBContent.TB_PLAN,null,values)>=0;

        Log.w("PATH","dabase...plan---add-->"+flag+"");

        return flag;

    }



    //任务信息
    @Override
    public ArrayList<Task> getTaskInfoFromTable(String account,int status) {
        Cursor cursor = db.query(DBContent.TB_TASK,null,"userId =? and status=?",new String[]{account,status+""},null,null,null);
        ArrayList<Task> tastList = new ArrayList<>();
        while(cursor.moveToNext()) {
            Task task = new Task();
            task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
           // task.setAvaiable(cursor.getInt(cursor.getColumnIndexOrThrow("avaible")));
            task.setId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
           // task.setProjectId(cursor.getString(cursor.getColumnIndexOrThrow("projectId")));
           // task.setQuartersId(cursor.getString(cursor.getColumnIndexOrThrow("quartersId")));
           // task.setTaken(cursor.getInt(cursor.getColumnIndexOrThrow("taken")));
          //  task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));
            task.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow("startData")));
            task.setDeadline(cursor.getString(cursor.getColumnIndexOrThrow("deadline")));
            task.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            task.setEnclosure(cursor.getString(cursor.getColumnIndexOrThrow("enclosure")));
            tastList.add(task);
        }
        return tastList;
    }
    @Override
    public void updateTaskInfo(String account, Task t) {
        ContentValues values = new ContentValues();
        values.put("title",t.getTitle());
      //  values.put("avaible",t.getAvaiable());
        values.put("userId",t.getId());
      //  values.put("projectId",t.getProjectId());
      //  values.put("quartersId",t.getQuartersId());
      //  values.put("taken",t.getTaken());
       // values.put("status",t.getStatus());
        values.put("startData",t.getStartDate());
        values.put("deadline",t.getDeadline());
        values.put("content",t.getContent());
        values.put("enclosure",t.getEnclosure());

        int line =  db.update(DBContent.TB_TASK,values,"userId = ? and title = ?",new String[]{t.getId(),t.getTitle()});
        if(line>=0)
            Log.w("plan","plan---->plan 保存成功  "+ line+"。。。。"+t.getId());
        else
            Log.w("plan","plan---->plan 保存失败  "+  line+"。。。。"+t.getId());
    }
    @Override
    public void addTaskInfo(Task t) {
        ContentValues values = new ContentValues();
        values.put("title",t.getTitle());
      //  values.put("avaible",t.getAvaiable());
        values.put("userId",t.getId());
       // values.put("projectId",t.getProjectId());
       // values.put("quartersId",t.getQuartersId());
       // values.put("taken",t.getTaken());
       // values.put("status",t.getStatus());
        values.put("startData",t.getStartDate());
        values.put("deadline",t.getDeadline());
        values.put("content",t.getContent());
        values.put("enclosure",t.getEnclosure());

        db.insert(DBContent.TB_TASK,null,values);
    }

    @Override
    public void deleteTaskInfo(int id) {
        db.delete(DBContent.TB_TASK,"_id=?",new String[]{id+""});
    }

    //从服务器数据更新本地数据库中的资产表
    public synchronized void updateProperty(Context context, List<Property> properties) {
        db.beginTransaction();
        try {
            db.delete("Property", null, null);
            final ContentValues values = new ContentValues();
            for (Property property : properties) {
                values.put("name", property.getName());
                values.put("price", (property.getPrice()));
                values.put("brand", (property.getBrand()));
                values.put("cate", property.getCate());
                values.put("date", property.getDate());
                values.put("model", property.getModel());
                values.put("identifier", property.getIdentifier());
                values.put("provider", property.getProvider());
                values.put("providerTel", property.getProviderTel());
                values.put("isBorrowedProperty", property.isBorrowedProperty() == true ? 1 : 0);
                db.insert("Property", null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Toast.makeText(context, "操作失败，请重试！", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }
    }

    /***
     * 更新本地数据库中的维修表
     * @param context
     * @param repairs
     */
    public synchronized void updateRepair( Context context, List<Repair> repairs) {
        db.beginTransaction();
        try {
            db.delete("Repair", null, null);
            final ContentValues values = new ContentValues();
            for (Repair repair : repairs) {
                values.put("identifier", repair.getIdentifier());
                values.put("applyTime", (repair.getApplyTime()));
                values.put("finishTime", (repair.getFinishTime()));
                values.put("checkState", repair.getCheckState());
                values.put("repairState", repair.getRepairState());
                values.put("createrIdentifier", repair.getCreaterIdentifier());
                values.put("describe", repair.getDescribe());
                db.insert("Repair", null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Toast.makeText(context, "操作失败，请重试！", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

    }

    /***
     * 更新本地数据库中的采购表
     * @param context
     * @param purchases
     */
    public synchronized void updatePurchase( Context context, List<Purchase> purchases) {
        db.beginTransaction();
        try {
            db.delete("Purchase", null, null);
            final ContentValues values = new ContentValues();
            for (Purchase purchase : purchases) {
                values.put("name", purchase.getName());
                values.put("brand", purchase.getBrand());
                values.put("model", purchase.getModel());
                values.put("describe", purchase.getDescribe());
                values.put("price", purchase.getPrice());
                values.put("createrIdentifier", purchase.getCreaterIdentifier());
                values.put("checkState", purchase.getCheckState());
                values.put("purchaseState", purchase.getPurchaseState());
                values.put("applyTime", purchase.getApplyTime());
                values.put("finishTime", purchase.getFinishTime());
                db.insert("Purchase", null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Toast.makeText(context, "操作失败，请重试！", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

    }



}
