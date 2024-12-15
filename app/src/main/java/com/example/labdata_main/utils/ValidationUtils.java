package com.example.labdata_main.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * 输入验证工具类
 */
public class ValidationUtils {
    
    // 密码正则表达式：必须包含数字、小写字母和大写字母
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{6,20}$";

    /**
     * 验证邮箱格式
     * @param email 待验证的邮箱
     * @return 验证结果
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * 验证密码格式
     * 要求：
     * 1. 必须包含数字
     * 2. 必须包含小写字母
     * 3. 必须包含大写字母
     * 4. 长度在6-20位之间
     * @param password 待验证的密码
     * @return 验证结果
     */
    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

    /**
     * 获取密码强度提示信息
     * @param password 密码
     * @return 提示信息
     */
    public static String getPasswordStrengthMessage(String password) {
        if (TextUtils.isEmpty(password)) {
            return "密码不能为空";
        }
        
        if (password.length() < 6) {
            return "密码长度不能小于6位";
        }
        
        if (password.length() > 20) {
            return "密码长度不能大于20位";
        }
        
        if (!Pattern.compile(".*[0-9].*").matcher(password).matches()) {
            return "密码必须包含数字";
        }
        
        if (!Pattern.compile(".*[a-z].*").matcher(password).matches()) {
            return "密码必须包含小写字母";
        }
        
        if (!Pattern.compile(".*[A-Z].*").matcher(password).matches()) {
            return "密码必须包含大写字母";
        }
        
        return null;
    }
}
