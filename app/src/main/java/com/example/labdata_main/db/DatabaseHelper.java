package com.example.labdata_main.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.labdata_main.model.Equipment;
import com.example.labdata_main.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮助类
 * 用于管理用户数据和设备数据的SQLite数据库操作
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /** 数据库名称 */
    private static final String DATABASE_NAME = "labdata.db";
    /** 数据库版本号 */
    private static final int DATABASE_VERSION = 2;

    // 用户表相关常量
    /** 用户表名 */
    private static final String TABLE_USERS = "users";
    /** ID列名 */
    private static final String COLUMN_ID = "id";
    /** 公司列名 */
    private static final String COLUMN_COMPANY = "company";
    /** 姓名列名 */
    private static final String COLUMN_NAME = "name";
    /** 电话列名 */
    private static final String COLUMN_PHONE = "phone";
    /** 邮箱列名 */
    private static final String COLUMN_EMAIL = "email";
    /** 密码列名 */
    private static final String COLUMN_PASSWORD = "password";

    // 设备表相关常量
    /** 设备表名 */
    private static final String TABLE_EQUIPMENT = "equipment";
    /** 设备类型列名 */
    private static final String COLUMN_TYPE = "type";
    /** 设备型号列名 */
    private static final String COLUMN_MODEL = "model";
    /** 生产厂家列名 */
    private static final String COLUMN_MANUFACTURER = "manufacturer";
    /** 购买年限列名 */
    private static final String COLUMN_PURCHASE_YEAR = "purchase_year";

    /**
     * 构造函数
     * @param context 应用上下文
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户表
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COMPANY + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // 创建设备表
        String CREATE_EQUIPMENT_TABLE = "CREATE TABLE " + TABLE_EQUIPMENT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COMPANY + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_MODEL + " TEXT,"
                + COLUMN_MANUFACTURER + " TEXT,"
                + COLUMN_PURCHASE_YEAR + " INTEGER"
                + ")";
        db.execSQL(CREATE_EQUIPMENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库升级时删除旧表并创建新表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
        onCreate(db);
    }

    // 用户相关方法

    /**
     * 添加新用户
     * @param user 用户对象
     * @return 插入成功返回true，失败返回false
     */
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPANY, user.getCompany());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    /**
     * 验证用户登录
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 验证成功返回用户对象，失败返回null
     */
    public User checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_COMPANY, COLUMN_NAME, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PASSWORD},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setCompany(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            cursor.close();
        }
        db.close();
        return user;
    }

    /**
     * 检查邮箱是否已存在
     * @param email 待检查的邮箱
     * @return true表示已存在，false表示不存在
     */
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return exists;
    }

    // 设备相关方法

    /**
     * 添加新设备
     * @param equipment 设备对象
     * @return 插入成功返回新记录的ID，失败返回-1
     */
    public long addEquipment(Equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPANY, equipment.getCompanyName());
        values.put(COLUMN_TYPE, equipment.getType());
        values.put(COLUMN_MODEL, equipment.getModel());
        values.put(COLUMN_MANUFACTURER, equipment.getManufacturer());
        values.put(COLUMN_PURCHASE_YEAR, equipment.getPurchaseYear());
        return db.insert(TABLE_EQUIPMENT, null, values);
    }

    /**
     * 获取指定公司的所有设备
     * @param companyName 公司名称
     * @return 设备列表
     */
    public List<Equipment> getEquipmentByCompany(String companyName) {
        List<Equipment> equipmentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EQUIPMENT + 
                           " WHERE " + COLUMN_COMPANY + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{companyName});

        if (cursor.moveToFirst()) {
            do {
                Equipment equipment = new Equipment();
                equipment.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                equipment.setCompanyName(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY)));
                equipment.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                equipment.setModel(cursor.getString(cursor.getColumnIndex(COLUMN_MODEL)));
                equipment.setManufacturer(cursor.getString(cursor.getColumnIndex(COLUMN_MANUFACTURER)));
                equipment.setPurchaseYear(cursor.getInt(cursor.getColumnIndex(COLUMN_PURCHASE_YEAR)));
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return equipmentList;
    }

    /**
     * 检查公司是否已完成设备初始化
     * 要求每类设备（拌合、制件、实验）至少有一个
     * @param companyName 公司名称
     * @return true表示已完成初始化，false表示未完成
     */
    public boolean hasInitializedEquipment(String companyName) {
        boolean hasMixing = false;
        boolean hasMolding = false;
        boolean hasTesting = false;

        List<Equipment> equipmentList = getEquipmentByCompany(companyName);
        for (Equipment equipment : equipmentList) {
            switch (equipment.getType()) {
                case "拌合":
                    hasMixing = true;
                    break;
                case "制件":
                    hasMolding = true;
                    break;
                case "实验":
                    hasTesting = true;
                    break;
            }
        }

        return hasMixing && hasMolding && hasTesting;
    }
}
