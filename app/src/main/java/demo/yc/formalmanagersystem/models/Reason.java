package demo.yc.formalmanagersystem.models;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Reason
{
    private int id;
    private String title;       // 任务标题
    private String describe;    //  放弃原因
    private String quit_time;  //   放弃日期

    public Reason(){}

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

    public String getQuit_time() {
        return quit_time;
    }

    public void setQuit_time(String quit_time) {
        this.quit_time = quit_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
