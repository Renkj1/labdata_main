package com.example.labdata_main.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences管理类
 * 用于管理用户登录状态和基本信息的持久化存储
 */
public class SharedPrefsManager {
    // SharedPreferences文件名
    private static final String PREF_NAME = "UserPrefs";
    
    // 存储的键名
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_COMPANY = "userCompany";
    private static final String KEY_USER_PHONE = "userPhone";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    /**
     * 构造函数
     * @param context 应用上下文
     */
    public SharedPrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 保存用户登录状态和基本信息
     * @param id 用户ID
     * @param email 用户邮箱
     * @param name 用户姓名
     * @param company 用户单位
     * @param phone 用户电话
     */
    public void saveUserLoginSession(int id, String email, String name, String company, String phone) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_COMPANY, company);
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    /**
     * 清除用户登录状态和信息
     */
    public void clearUserLoginSession() {
        editor.clear();
        editor.apply();
    }

    /**
     * 检查用户是否已登录
     * @return true表示已登录，false表示未登录
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * 获取用户ID
     * @return 用户ID，如果未登录返回-1
     */
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    /**
     * 获取用户邮箱
     * @return 用户邮箱，如果未登录返回null
     */
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    /**
     * 获取用户姓名
     * @return 用户姓名，如果未登录返回null
     */
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    /**
     * 获取用户单位
     * @return 用户单位，如果未登录返回null
     */
    public String getUserCompany() {
        return sharedPreferences.getString(KEY_USER_COMPANY, null);
    }

    /**
     * 获取用户电话
     * @return 用户电话，如果未登录返回null
     */
    public String getUserPhone() {
        return sharedPreferences.getString(KEY_USER_PHONE, null);
    }
}
