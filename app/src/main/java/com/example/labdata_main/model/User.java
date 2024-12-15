package com.example.labdata_main.model;

/**
 * 用户数据模型类
 * 用于存储用户的基本信息，包括单位、姓名、电话、邮箱等
 */
public class User {
    // 用户ID，数据库主键
    private int id;
    // 用户所属单位
    private String company;
    // 用户姓名
    private String name;
    // 用户电话
    private String phone;
    // 用户邮箱，用作登录账号
    private String email;
    // 用户密码
    private String password;

    // 默认构造函数
    public User() {
    }

    /**
     * 带参数的构造函数
     * @param company 单位名称
     * @param name 用户姓名
     * @param phone 电话号码
     * @param email 邮箱地址
     * @param password 登录密码
     */
    public User(String company, String name, String phone, String email, String password) {
        this.company = company;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
