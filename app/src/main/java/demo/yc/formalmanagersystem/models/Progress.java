package demo.yc.formalmanagersystem.models;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Progress
{

    private int id;
    private String record_time;    //    记录时间
    private String describe;       // 任务记录描述
    private String title;       //  任务标题
    private double value;       // 进度值


    public Progress(){}

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecord_time() {
        return record_time;
    }

    public void setRecord_time(String record_time) {
        this.record_time = record_time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
