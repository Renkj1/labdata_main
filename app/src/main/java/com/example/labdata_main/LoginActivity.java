package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.model.User;
import com.example.labdata_main.utils.SharedPrefsManager;

/**
 * 登录界面Activity
 * 处理用户登录和注册跳转功能
 */
public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private DatabaseHelper databaseHelper;
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);

        // 初始化工具类
        databaseHelper = new DatabaseHelper(this);
        sharedPrefsManager = new SharedPrefsManager(this);

        // 检查是否已登录
        if (sharedPrefsManager.isLoggedIn()) {
            startMainActivity();
            finish();
            return;
        }

        // 初始化视图
        initViews();
        // 设置点击事件
        setClickListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void setClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 验证输入
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("请输入邮箱");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return;
        }

        // 验证用户登录
        User user = databaseHelper.checkUser(email, password);
        if (user != null) {
            // 添加日志
            Log.d("LoginActivity", "Login successful. User type: " + user.getUserType());
            
            // 保存登录状态和用户信息
            sharedPrefsManager.saveUserLoginSession(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCompany(),
                user.getPhone(),
                user.getUserType()
            );
            
            // 添加日志验证保存后的用户类型
            Log.d("LoginActivity", "Saved user type. Verifying: " + sharedPrefsManager.getUserType());
            
            // 登录成功，跳转到主界面
            startMainActivity();
            finish();
        } else {
            Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
