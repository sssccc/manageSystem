package demo.yc.formalmanagersystem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import demo.yc.formalmanagersystem.contentvalues.DBContent;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class MyDBHelper extends SQLiteOpenHelper {

//    //所有用户账号
//    private static final String CRETE_TB_ACCOUNT =
//            "create table "+ DBContent.TB_ACCOUNT+" (" +
//                    "_id integer primary key autoincrement," +
//                    "account text" +
//                    "password text" +
//                    "role text)";

    //User建表语句
    public static final String CREATE_USER = "create table User(" +
            "id integer primary key autoincrement," +
            "identifier text," +
            "count text," +
            "name text,"+
            "password text," +
            "position text," +
            "gender text)";

    //Repair建表语句
    public static final String CREATE_REPAIR = "create table Repair(" +
            "id integer primary key autoincrement," +
            "identifier text," +
            "applyTime text," +
            "finishTime text," +
            "checkState text," +
            "repairState text," +
            "createrIdentifier text," +
            "describe text)";

    //Property建表语句
    public static final String CREATE_PROPERTY = "create table Property(" +
            "id integer primary key autoincrement," +
            "name text," +
            "price text," +
            "brand text," +
            "cate text," +
            "date text," +
            "model text," +
            "identifier text," +
            "provider text," +
            "providerTel text," +
            "isBorrowedProperty tinyInt)";

    //Purchase建表语句
    public static final String CREATE_PURCHASE = "create table Purchase(" +
            "id integer primary key autoincrement," +
            "name text," +
            "price text," +
            "purchaseState text," +
            "finishTime text," +
            "createrIdentifier text," +
            "checkState text," +
            "model text," +
            "brand text," +
            "applyTime text," +
            "describe text)";


    //用户详细信息
    private static final String CRETE_TB_PERSON =
            "create table "+ DBContent.TB_PERSON+" (" +
                    "_id integer primary key autoincrement," +
                    "name text," +
                    "sex text," +
                    "age integer," +
                    "college text," +
                    "major text," +
                    "clazz text," +
                    "picture text," +
                    "quartersId text," +
                    "studentId text," +
                    "photoUrl text,"+
                    "userId text)";
   // 用户任务信息
    private static final String CRETE_TB_TASK =
            "create table "+ DBContent.TB_TASK+" (" +
                    "_id integer primary key autoincrement," +
                    "title text," +
                    "avaible integer," +
                    "content text," +
                    "userId text," +
                    "projectId text," +
                    "quartersId text," +
                    "taken integer," +
                    "startData text," +
                    "deadline text," +
                    "administor text," +
                    "enclosure text," +
                    "status integer)";

    //用户时间安排的行程信息
    private static final String CRETE_TB_PLAN =
            "create table "+ DBContent.TB_PLAN+" (" +
                    "_id integer primary key autoincrement," +
                    "id text,"+
                    "userId text,"+
                    "isFixed integer," +
                    "isFree integer," +
                    "type integer," +
                    "weekDay integer," +
                    "dayTime integer," +
                    "title text," +
                    "content text)";


    MyDBHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int vertion)
    {
        super(context,dbName,factory,vertion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CRETE_TB_PERSON);
        sqLiteDatabase.execSQL(CRETE_TB_PLAN);
        sqLiteDatabase.execSQL(CRETE_TB_TASK);
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_PURCHASE);
        sqLiteDatabase.execSQL(CREATE_REPAIR);
        sqLiteDatabase.execSQL(CREATE_PROPERTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
