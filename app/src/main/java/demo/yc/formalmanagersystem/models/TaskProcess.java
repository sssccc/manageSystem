package demo.yc.formalmanagersystem.models;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public class TaskProcess {

    private String name;
    private String time;
    private String content;

    public TaskProcess()
    {}


    public TaskProcess(String content, String name, String time) {
        this.content = content;
        this.name = name;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
