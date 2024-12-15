package com.example.labdata_main.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.labdata_main.model.User;
import com.example.labdata_main.model.Equipment;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮助类
 * 用于管理用户数据的SQLite数据库操作
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    
    // 数据库名称和版本
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 2; // 增加版本号以触发数据库升级

    // 用户表
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COMPANY = "company";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // 设备表
    private static final String TABLE_EQUIPMENT = "equipment";
    private static final String COLUMN_COMPANY_ID = "company_id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_MANUFACTURER = "manufacturer";
    private static final String COLUMN_PURCHASE_YEAR = "purchase_year";

    // 创建用户表的 SQL
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_COMPANY + " TEXT UNIQUE,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_PHONE + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT"
            + ")";

    // 创建设备表的 SQL
    private static final String CREATE_EQUIPMENT_TABLE = "CREATE TABLE " + TABLE_EQUIPMENT + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_COMPANY_ID + " TEXT NOT NULL, "
            + COLUMN_TYPE + " TEXT NOT NULL, "
            + COLUMN_MODEL + " TEXT NOT NULL, "
            + COLUMN_MANUFACTURER + " TEXT NOT NULL, "
            + COLUMN_PURCHASE_YEAR + " TEXT NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_COMPANY_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_COMPANY + ") "
            + "ON DELETE CASCADE ON UPDATE CASCADE)";

    /**
     * 构造函数
     * @param context 应用上下文
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database tables...");
        try {
            db.execSQL("PRAGMA foreign_keys=ON");
            // 创建用户表
            db.execSQL(CREATE_USERS_TABLE);
            Log.d(TAG, "Users table created successfully");
            
            // 创建设备表
            db.execSQL(CREATE_EQUIPMENT_TABLE);
            Log.d(TAG, "Equipment table created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        try {
            db.execSQL("PRAGMA foreign_keys=ON");
            // 删除旧表
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            Log.d(TAG, "Old tables dropped successfully");
            
            // 创建新表
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * 添加新用户
     * @param user 用户对象
     * @return 插入成功返回新记录的ID，失败返回-1
     */
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // 准备要插入的数据
        values.put(COLUMN_COMPANY, user.getCompany());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        // 插入数据并获取返回值
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    /**
     * 验证用户登录
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 验证成功返回用户对象，失败返回null
     */
    public User checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        // 查询匹配的用户记录
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_COMPANY, COLUMN_NAME, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PASSWORD},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            // 找到匹配的用户，创建用户对象
            user = new User();
            user.setId(cursor.getInt(0));
            user.setCompany(cursor.getString(1));
            user.setName(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setEmail(cursor.getString(4));
            user.setPassword(cursor.getString(5));
            cursor.close();
        }
        db.close();
        return user;
    }

    /**
     * 检查邮箱是否已被注册
     * @param email 要检查的邮箱
     * @return true表示邮箱已存在，false表示邮箱可用
     */
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // 查询是否存在使用该邮箱的用户
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

    // 添加设备
    public long addEquipment(Equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        
        try {
            // 先检查公司ID是否存在
            String query = "SELECT " + COLUMN_COMPANY + " FROM " + TABLE_USERS + 
                         " WHERE " + COLUMN_COMPANY + "=?";
            Cursor cursor = db.rawQuery(query, new String[]{equipment.getCompanyId()});
            
            Log.d(TAG, "Checking company ID: " + equipment.getCompanyId());
            Log.d(TAG, "Company exists: " + (cursor != null && cursor.getCount() > 0));
            
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_COMPANY_ID, equipment.getCompanyId());
                values.put(COLUMN_TYPE, equipment.getType());
                values.put(COLUMN_MODEL, equipment.getModel());
                values.put(COLUMN_MANUFACTURER, equipment.getManufacturer());
                values.put(COLUMN_PURCHASE_YEAR, equipment.getPurchaseYear());
                
                Log.d(TAG, "Inserting equipment: " + 
                    "Type=" + equipment.getType() + 
                    ", Model=" + equipment.getModel() + 
                    ", Manufacturer=" + equipment.getManufacturer() + 
                    ", Year=" + equipment.getPurchaseYear());
                
                result = db.insert(TABLE_EQUIPMENT, null, values);
                Log.d(TAG, "Insert result: " + result);
            }
            
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding equipment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        
        return result;
    }

    // 用于调试：获取所有用户
    public void logAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);
        
        Log.d(TAG, "All users in database:");
        if (cursor.moveToFirst()) {
            do {
                String company = cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY));
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                Log.d(TAG, "Company: " + company + ", Email: " + email);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    // 检查公司是否已初始化设备
    public boolean hasInitializedEquipment(String companyId) {
        if (companyId == null || companyId.isEmpty()) {
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String[] types = {"MIXING", "FORMING", "TESTING"};
        
        try {
            for (String type : types) {
                String query = "SELECT COUNT(*) FROM " + TABLE_EQUIPMENT + 
                              " WHERE company_id = ? AND type = ?";
                Cursor cursor = db.rawQuery(query, new String[]{companyId, type});
                if (cursor.moveToFirst()) {
                    int count = cursor.getInt(0);
                    cursor.close();
                    if (count == 0) {
                        return false;
                    }
                } else {
                    cursor.close();
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取公司的所有设备
    public List<Equipment> getCompanyEquipment(String companyId) {
        List<Equipment> equipmentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT * FROM " + TABLE_EQUIPMENT + " WHERE company_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{companyId});
        
        if (cursor.moveToFirst()) {
            do {
                Equipment equipment = new Equipment();
                equipment.setId(cursor.getInt(cursor.getColumnIndex("id")));
                equipment.setCompanyId(cursor.getString(cursor.getColumnIndex("company_id")));
                equipment.setType(cursor.getString(cursor.getColumnIndex("type")));
                equipment.setModel(cursor.getString(cursor.getColumnIndex("model")));
                equipment.setManufacturer(cursor.getString(cursor.getColumnIndex("manufacturer")));
                equipment.setPurchaseYear(cursor.getString(cursor.getColumnIndex("purchase_year")));
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return equipmentList;
    }
}
