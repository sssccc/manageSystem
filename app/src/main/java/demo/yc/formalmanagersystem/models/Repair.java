package demo.yc.formalmanagersystem.models;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Repair extends BmobObject implements Serializable {
    private String describe;        // 描述
    private String applyTime;      // 申请时间
    private String finishTime;     // 完成时间
    private String checkState;     // 审核状态
    private String repairState;    // 修理状态
    private String createrName;     //申请人名称
    private String createrIdentifier;   //申请人学号
    private String identifier;       //资产编号
    private String name;            //资产名称


    public Repair(String identifier, String name, String applyTime, String finishTime, String checkState, String repairState, String describe, String createrIdentifier, String createrName) {
        this.applyTime = applyTime;
        this.checkState = checkState;
        this.createrIdentifier = createrIdentifier;
        this.createrName = createrName;
        this.describe = describe;
        this.identifier = identifier;
        this.name = name;
        this.repairState = repairState;
        this.finishTime = finishTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }


    public String getCreaterIdentifier() {
        return createrIdentifier;
    }

    public void setCreaterIdentifier(String createrIdentifier) {
        this.createrIdentifier = createrIdentifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRepairState() {
        return repairState;
    }

    public void setRepairState(String repairState) {
        this.repairState = repairState;
    }

    public Repair() {
        this.applyTime = "";
        this.checkState = "";
        this.createrIdentifier = "";
        this.createrName = "";
        this.describe = "";
        this.identifier = "";
        this.name = "";
        this.repairState = "";
        this.finishTime = "";
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

}
