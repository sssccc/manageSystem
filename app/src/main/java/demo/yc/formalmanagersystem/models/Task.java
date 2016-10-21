package demo.yc.formalmanagersystem.models;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Task
{
    private String id;          //用户账号
    private String title;            // 标题
    private String content;        //  描述
    private String enclosure;      //  附件
    private String startData;      //  开始时间    毫秒
    private String deadline;        //  结束时间   毫秒
    private String dead;
    private String start;
    private int status ;            //状态    任务完成情况，0：未完成，1：已完成
    private int avaiable;
    //是否冻结任务，1:是，0:否
    private String administor;      //  发布者
//    private String participator;    //  参与者
    private int taken ;         //任务是否被接收    1接收  0  拒绝
    private String quartersId;    //所属分类
    private String projectId;     //项目组    0表示所有人可以参与




    public Task(){}

    public Task(String title,String deadline,int status,String projectId)
    {
        this.title = title;
        this.deadline = deadline;
        this.status = status;
        this.projectId = projectId;
    }

    public Task(String title,String deadline,int status)
    {
        this.title = title;
        this.deadline = deadline;
        this.status = status;
    }


    public int getAvaiable() {
        return avaiable;
    }

    public void setAvaiable(int avaiable) {
        this.avaiable = this.avaiable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getQuartersId() {
        return quartersId;
    }

    public void setQuartersId(String quartersId) {
        this.quartersId = quartersId;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTaken() {
        return taken;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdministor() {
        return administor;
    }

    public void setAdministor(String administor) {
        this.administor = administor;
    }

    public String getDead() {
        return dead;
    }

    public void setDead(String dead) {
        this.dead = dead;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
