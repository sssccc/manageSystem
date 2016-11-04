package demo.yc.formalmanagersystem.models;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public class TaskProcess {

    private String username;
    private String createAt;
    private String content;

    public TaskProcess()
    {}


    public TaskProcess(String content, String username, String time) {
        this.content = content;
        this.username = username;
        this.createAt = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
