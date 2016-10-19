package demo.yc.formalmanagersystem.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Plan implements Serializable
{

    private int iid;

    private  String id;
    private String title;
    private int type;
    private int isFixed;
    private  int dayTime;
    private int weekDay;
    private String content;


    private String userId;
    private int isFree;

    public Plan(){};
    public Plan(String title,String describe,int type,int day_time,int week_day,int  isFixed,int isFreeed,String account){
        this.title = title;
        this.content = describe;
        this.dayTime = day_time;
        this.weekDay = week_day;
        this.isFixed = isFixed;
        this.type = type;
        this.isFree = isFreeed;
        this.userId = account;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public int getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(int isFixed) {
        this.isFixed = isFixed;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }
}
