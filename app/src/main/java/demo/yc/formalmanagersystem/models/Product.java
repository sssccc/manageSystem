package demo.yc.formalmanagersystem.models;

import android.app.ProgressDialog;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Product
{
    private int id;
    private String name;            //  资产名称
    private String number;          //  资产编号
    private double price;           //  资产价格
    private String brand;           //  资产商标
    private String cate;            //  资产类型
    private String model;           //  资产型号
    private String entry_time;       // 资产录入时间
    private String seller;          //  资产销售者
    private int seller_tel;         //  售后电话
    private String isBorrowed;      //  是否为借用资产

    public Product(){}

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsBorrowed() {
        return isBorrowed;
    }

    public void setIsBorrowed(String isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getSeller_tel() {
        return seller_tel;
    }

    public void setSeller_tel(int seller_tel) {
        this.seller_tel = seller_tel;
    }
}
