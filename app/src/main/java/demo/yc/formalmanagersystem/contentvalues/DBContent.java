package demo.yc.formalmanagersystem.contentvalues;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class DBContent {

    public static final String DB_NAME = "manager_system.db";  //数据库名称
    public static final int DB_VERTION = 1;  //数据库版本

    public static final String TB_ACCOUNT = "account"; //所有的账号表名
    public static final String TB_PERSON = "person";    //用户的详细信息表名
    public static final String TB_TASK = "task";      //用户的任务信息表名
    public static final String TB_TIME = "time";      //用户的时间安排表名
    public static final String TB_PLAN = "plan";       //用户的时间安排对应的行程信息表名
    public static final String TB_PROGRESS = "progress"; // 用户参与任务的完成进度信息表名
    public static final String TB_REASON  = "reason" ; //用户放弃任务的原因信息表名
    public static final String TB_PROPERTY = "property" ;  //所有的资产信息表名
    public static final String TB_REPAIR = "repair" ; //所有修理的资产记录表名
    public static final String TB_PURCHASE = "purchase" ; //所有的采购资产记录表名

}
