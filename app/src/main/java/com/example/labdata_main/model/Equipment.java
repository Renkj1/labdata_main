package com.example.labdata_main.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Equipment implements Parcelable {
    private int id;
    private String companyId;  // 归属单位ID
    private String type;       // 设备类型：MIXING(拌合), FORMING(制件), TESTING(实验)
    private String model;      // 设备型号
    private String manufacturer; // 生产厂家
    private String purchaseYear;   // 购买年限

    public Equipment() {
    }

    public Equipment(String companyId, String type, String model, String manufacturer, String purchaseYear) {
        this.companyId = companyId;
        this.type = type;
        this.model = model;
        this.manufacturer = manufacturer;
        this.purchaseYear = purchaseYear;
    }

    protected Equipment(Parcel in) {
        id = in.readInt();
        companyId = in.readString();
        type = in.readString();
        model = in.readString();
        manufacturer = in.readString();
        purchaseYear = in.readString();
    }

    public static final Creator<Equipment> CREATOR = new Creator<Equipment>() {
        @Override
        public Equipment createFromParcel(Parcel in) {
            return new Equipment(in);
        }

        @Override
        public Equipment[] newArray(int size) {
            return new Equipment[size];
        }
    };

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPurchaseYear() {
        return purchaseYear;
    }

    public void setPurchaseYear(String purchaseYear) {
        this.purchaseYear = purchaseYear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(companyId);
        dest.writeString(type);
        dest.writeString(model);
        dest.writeString(manufacturer);
        dest.writeString(purchaseYear);
    }
}
