package demo.yc.formalmanagersystem.database;

import java.util.ArrayList;

import demo.yc.formalmanagersystem.models.Person;
import demo.yc.formalmanagersystem.models.Plan;
import demo.yc.formalmanagersystem.models.Task;

/**
 * Created by Administrator on 2016/7/17.
 */
public interface DataBaseInterface {
//    int checkCount(String count);
//    int checkCountAndPassword(String count, String password);
//    void storeCountAndPasswordInDatabase(String count, String password);
//    void storeProductInfo(String pname, String price, String brand, String cate, String date, boolean isUsing, boolean isBeingRepaired, boolean products_to_be_repaired, String product_identifier, String product_model, String product_maker, String product_provider, String product_provider_tel, boolean is_borrowed_product);
//    void storeUserInfo(String name, String user_count, String user_password, int user_sex, String user_position, String age, String user_identifier, String user_institute, String user_major, String user_class, String user_portrait);
//    void storeRepairedProductInfo(String name, String creater, String creater_identifier, String recordDate, String product_identifier);
//    void storeToBeRepairedProductInfo(String name, String creater, String creater_identifier, String recordDate, String product_identifier);
//    void saveToTable(String tableName,Object object);


    Person getPersonInfoFromTable(String account);
    void updatePersonInfo(Person p);
    void addPersonInfo(Person p);



    ArrayList<Plan> getPlanInfoFromTable(String account);
    ArrayList<Plan> getSubPlanInfoFromTable(String account,int today);
    void updatePlanInfo(Plan p);
    boolean addPlanInfo(Plan p);


    ArrayList<Task> getTaskInfoFromTable(String account,int status);
    void updateTaskInfo(String account,Task t);
    void addTaskInfo(Task t);
    void deleteTaskInfo(int id);






}
