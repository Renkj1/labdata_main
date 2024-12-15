package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.model.User;
import com.example.labdata_main.utils.SharedPrefsManager;
import com.example.labdata_main.utils.ValidationUtils;

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

        // 初始化界面控件
        initViews();
        // 设置输入监听
        setupInputValidation();
        // 设置点击事件监听
        setClickListeners();
    }

    /**
     * 初始化界面控件
     */
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    /**
     * 设置输入验证
     */
    private void setupInputValidation() {
        // 邮箱输入验证
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (!ValidationUtils.isValidEmail(email)) {
                    etEmail.setError("请输入有效的邮箱地址");
                } else {
                    etEmail.setError(null);
                }
                updateLoginButtonState();
            }
        });

        // 密码输入验证
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString().trim();
                String errorMessage = ValidationUtils.getPasswordStrengthMessage(password);
                if (errorMessage != null) {
                    etPassword.setError(errorMessage);
                } else {
                    etPassword.setError(null);
                }
                updateLoginButtonState();
            }
        });
    }

    /**
     * 更新登录按钮状态
     */
    private void updateLoginButtonState() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        boolean isValid = ValidationUtils.isValidEmail(email) && 
                         ValidationUtils.isValidPassword(password);
        
        btnLogin.setEnabled(isValid);
        btnLogin.setAlpha(isValid ? 1.0f : 0.5f);
    }

    /**
     * 设置点击事件监听
     */
    private void setClickListeners() {
        // 登录按钮点击事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // 注册链接点击事件
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    /**
     * 尝试登录
     */
    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 验证邮箱和密码
        if (!ValidationUtils.isValidEmail(email)) {
            etEmail.setError("请输入有效的邮箱地址");
            etEmail.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            String errorMessage = ValidationUtils.getPasswordStrengthMessage(password);
            etPassword.setError(errorMessage);
            etPassword.requestFocus();
            return;
        }

        // 验证登录
        User user = databaseHelper.checkUser(email, password);
        if (user != null) {
            handleLoginSuccess(user);
        } else {
            Toast.makeText(this, "邮箱或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理登录成功
     */
    private void handleLoginSuccess(User user) {
        try {
            // 保存登录状态
            sharedPrefsManager.saveUserLoginSession(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCompany(),
                user.getPhone()
            );
            
            // 检查是否已初始化设备
            if (databaseHelper.hasInitializedEquipment(user.getCompany())) {
                // 已初始化，直接进入主页
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // 未初始化，进入设备初始化引导页面
                Intent intent = new Intent(LoginActivity.this, EquipmentGuideActivity.class);
                intent.putExtra("company_id", user.getCompany());
                startActivity(intent);
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "系统错误，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 启动主界面
     */
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // 禁用返回键
        moveTaskToBack(true);
    }
}
