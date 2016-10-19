package demo.yc.formalmanagersystem.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/21 0021.
 */

//
//    private int id;
//    public String name;    //姓名
//    public int age;        //年龄
//    public String position; //岗位
//    public int sex;     //性别  0 boy 1 girl
//    public String institute;   //学院
//    public String major;       //专业
//    public int class_num;      //班级
//    public String number;     //学号
//    public String photo_phone;   //照片地址
//    public String photo_url;
//
//    public Person(int age, int class_num, String institute, String major, String name, String number, String photo_phone, String photo_url, String position, int sex) {
//        this.age = age;
//        this.class_num = class_num;
//        this.institute = institute;
//        this.major = major;
//        this.name = name;
//        this.number = number;
//        this.photo_phone = photo_phone;
//        this.photo_url = photo_url;
//        this.position = position;
//        this.sex = sex;
//    }
//
//    public Person() {}
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public int getClass_num() {
//        return class_num;
//    }
//
//    public void setClass_num(int class_num) {
//        this.class_num = class_num;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getInstitute() {
//        return institute;
//    }
//
//    public void setInstitute(String institute) {
//        this.institute = institute;
//    }
//
//    public String getMajor() {
//        return major;
//    }
//
//    public void setMajor(String major) {
//        this.major = major;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
////    public String getPhoto() {
////        return photo;
////    }
////
////    public void setPhoto(String photo) {
////        this.photo = photo;
////    }
//
////    public int getNumber() {
////        return number;
////    }
////
////    public void setNumber(int number) {
////        this.number = number;
////    }
//
////    public String getSex() {
////        return sex;
////    }
////
////    public void setSex(String sex) {
////        this.sex = sex;
////    }
//
//    public String getPosition() {
//        return position;
//    }
//
//    public void setPosition(String position) {
//        this.position = position;
//    }
//}
public class Person  implements Serializable{

    private String picture;     //服务器头像地址
    private String name;           //姓名
    private String studentId;   //学号
    private String sex;         //性别，1：男，0：女
    private int age;               //年龄
    private String college;       //
    private String major;
    private String clazz;
    private String quartersId;
    private String id;
    private String photoUrl;     //本地头像地址

    public Person(int age, String clazz, String college, String major, String name, String studentId, String picture, String photoUrl, String quartersId, String sex,String userId) {
        this.age = age;
        this.clazz = clazz;
        this.college = college;
        this.major = major;
        this.name = name;
        this.studentId = studentId;
        this.picture = picture;
        this.photoUrl = photoUrl;
        this.quartersId = quartersId;
        this.sex = sex;
        this.id = userId;
    }
    public Person(){
    }
    public String getQuartersId() {
        return quartersId;
    }
    public void setQuartersId(String quartersId) {
        this.quartersId = quartersId;
    }
    public String getId() {
        return id;
    }
    public void setId(String userId) {
        this.id = userId;
    }
    public String getCollege() {
        return college;
    }
    public void setCollege(String college) {
        this.college = college;
    }
    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public String getClazz() {
        return clazz;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }




}

