package demo.yc.formalmanagersystem.models;

/**
 * Created by Administrator on 2016/7/21 0021.
 */

public class Account
{
    private int id;          //
    private String account;  // 账号
    private String password; // 密码
    private int role;         // 角色

    public Account(){}

    public Account(String account, String password, int role) {
        this.account = account;
        this.role = role;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
