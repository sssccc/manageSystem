package demo.yc.formalmanagersystem.models;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Time
{
    private int id;
    private String week_day;        //  星期几
    private String class_1_2;      //  1,2 节课
    private String class_3_4;       // 3,4 节课
    private String class_7_8;       // 7,8 节课
    private String class_9_10;      // 9,10 节课
    private String class_11_12_13;  // 11,12，13 节课

    public Time()
    {}

    public String getClass_11_12_13() {
        return class_11_12_13;
    }

    public void setClass_11_12_13(String class_11_12_13) {
        this.class_11_12_13 = class_11_12_13;
    }

    public String getClass_1_2() {
        return class_1_2;
    }

    public void setClass_1_2(String class_1_2) {
        this.class_1_2 = class_1_2;
    }

    public String getClass_3_4() {
        return class_3_4;
    }

    public void setClass_3_4(String class_3_4) {
        this.class_3_4 = class_3_4;
    }

    public String getClass_7_8() {
        return class_7_8;
    }

    public void setClass_7_8(String class_7_8) {
        this.class_7_8 = class_7_8;
    }

    public String getClass_9_10() {
        return class_9_10;
    }

    public void setClass_9_10(String class_9_10) {
        this.class_9_10 = class_9_10;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeek_day() {
        return week_day;
    }

    public void setWeek_day(String week_day) {
        this.week_day = week_day;
    }
}
