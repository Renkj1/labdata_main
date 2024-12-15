package com.example.labdata_main.model;

/**
 * 设备实体类
 * 用于存储设备相关信息，包括设备类型、型号、生产厂家等
 */
public class Equipment {
    /** 设备ID，数据库主键 */
    private int id;
    /** 设备所属单位名称 */
    private String companyName;
    /** 设备类型：拌合、制件、实验 */
    private String type;
    /** 设备型号 */
    private String model;
    /** 生产厂家 */
    private String manufacturer;
    /** 购买年限 */
    private int purchaseYear;

    /**
     * 默认构造函数
     */
    public Equipment() {
    }

    /**
     * 带参数的构造函数
     * @param companyName 归属单位名称
     * @param type 设备类型
     * @param model 设备型号
     * @param manufacturer 生产厂家
     * @param purchaseYear 购买年限
     */
    public Equipment(String companyName, String type, String model, String manufacturer, int purchaseYear) {
        this.companyName = companyName;
        this.type = type;
        this.model = model;
        this.manufacturer = manufacturer;
        this.purchaseYear = purchaseYear;
    }

    // Getters and Setters
    /**
     * 获取设备ID
     * @return 设备ID
     */
    public int getId() {
        return id;
    }

    /**
     * 设置设备ID
     * @param id 设备ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取归属单位名称
     * @return 归属单位名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置归属单位名称
     * @param companyName 归属单位名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取设备类型
     * @return 设备类型（拌合、制件、实验）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置设备类型
     * @param type 设备类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取设备型号
     * @return 设备型号
     */
    public String getModel() {
        return model;
    }

    /**
     * 设置设备型号
     * @param model 设备型号
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 获取生产厂家
     * @return 生产厂家名称
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * 设置生产厂家
     * @param manufacturer 生产厂家名称
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * 获取购买年限
     * @return 购买年限
     */
    public int getPurchaseYear() {
        return purchaseYear;
    }

    /**
     * 设置购买年限
     * @param purchaseYear 购买年限
     */
    public void setPurchaseYear(int purchaseYear) {
        this.purchaseYear = purchaseYear;
    }
}
