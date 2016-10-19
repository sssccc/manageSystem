package demo.yc.formalmanagersystem.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/20.
 */
public class Property implements Serializable {
    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    private String name;
    private String identifier;
    private String cate;
    private String brand;
    private String model;
    private String price;
    private String date;
    private boolean isBorrowedProperty;
    private String repairStatus;
    private String provider;
    private String providerTel;

    public Property() {
        name = "";
        identifier = "";
        cate = "";
        brand = "";
        model = "";
        price = "";
        date = "";
        isBorrowedProperty = false;
        repairStatus = "";
        provider = "";
        providerTel = "";
        id = "";
    }

    //用于显示简略信息
    public Property(String name, String identifier) {
        this.identifier = identifier;
        this.name = name;
    }

    //用于显示详细信息
    public Property(String brand, String cate, String date, String identifier, boolean isBorrowedProperty, String model, String name, String price, String provider, String providerTel, String repairStatus) {
        this.brand = brand;
        this.cate = cate;
        this.date = date;
        this.identifier = identifier;
        this.isBorrowedProperty = isBorrowedProperty;
        this.model = model;
        this.name = name;
        this.price = price;
        this.provider = provider;
        this.providerTel = providerTel;
        this.repairStatus = repairStatus;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getBrand() {
        return brand;
    }

    public String getCate() {
        return cate;
    }

    public boolean isBorrowedProperty() {
        return isBorrowedProperty;
    }

    public String getDate() {
        return date;
    }

    public String getModel() {
        return model;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderTel() {
        return providerTel;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setBorrowedProperty(boolean borrowedProperty) {
        isBorrowedProperty = borrowedProperty;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setProviderTel(String providerTel) {
        this.providerTel = providerTel;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }
}
