package demo.yc.formalmanagersystem.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Purchase implements Serializable{

    private int id;
    private String describe;    // 描述
    private String applyTime;  //  申请时间
    private String checkState; //  审核状态
    private String name ;       //  资产名称
    private String createrName;     //关联表查询
    private String createrIdentifier;
    private String brand;
    private String model;
    private String purchaseState;
    private String finishTime;
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Purchase(){
        this.describe = "";
        this.applyTime = "";
        this.checkState = "";
        this.name = "";
        this.createrIdentifier = "";
        this.createrName = "";
        this.brand = "";
        this.model = "";
        this.purchaseState = "";
        this.finishTime = "";
    }

    public Purchase(String name, String applyTime, String finishTime, String checkState, String purchaseState
                    , String describe, String createrIdentifier,
                    String createrName, String brand, String model){
        this.describe = describe;
        this.applyTime = applyTime;
        this.checkState = checkState;
        this.name = name;
        this.createrIdentifier = createrIdentifier;
        this.createrName = createrName;
        this.brand = brand;
        this.model = model;
        this.purchaseState = purchaseState;
        this.finishTime = finishTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(String purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCreaterIdentifier() {
        return createrIdentifier;
    }

    public void setCreaterIdentifier(String createrIdentifier) {
        this.createrIdentifier = createrIdentifier;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
