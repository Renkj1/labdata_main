package com.example.labdata_main;

import android.os.Parcel;
import android.os.Parcelable;

public class Equipment implements Parcelable {
    private String type;
    private String manufacturer;
    private String model;
    private String purchaseYear;
    private String number;
    private String name;

    public Equipment() {
    }

    protected Equipment(Parcel in) {
        type = in.readString();
        manufacturer = in.readString();
        model = in.readString();
        purchaseYear = in.readString();
        number = in.readString();
        name = in.readString();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPurchaseYear() {
        return purchaseYear;
    }

    public void setPurchaseYear(String purchaseYear) {
        this.purchaseYear = purchaseYear;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(manufacturer);
        dest.writeString(model);
        dest.writeString(purchaseYear);
        dest.writeString(number);
        dest.writeString(name);
    }

    // 生成设备名称
    public void generateName() {
        StringBuilder sb = new StringBuilder();
        if (type != null) {
            switch (type) {
                case "MIXING":
                    sb.append("搅拌设备-");
                    break;
                case "FORMING":
                    sb.append("成型设备-");
                    break;
                case "TESTING":
                    sb.append("检测设备-");
                    break;
            }
        }
        if (manufacturer != null) {
            sb.append(manufacturer).append("-");
        }
        if (model != null) {
            sb.append(model);
        }
        this.name = sb.toString();
    }

    // 生成设备编号
    public void generateNumber() {
        StringBuilder sb = new StringBuilder();
        if (type != null) {
            switch (type) {
                case "MIXING":
                    sb.append("MIX");
                    break;
                case "FORMING":
                    sb.append("FORM");
                    break;
                case "TESTING":
                    sb.append("TEST");
                    break;
            }
        }
        if (purchaseYear != null) {
            sb.append(purchaseYear);
        }
        // 添加随机数
        sb.append(String.format("%04d", (int) (Math.random() * 10000)));
        this.number = sb.toString();
    }
}
