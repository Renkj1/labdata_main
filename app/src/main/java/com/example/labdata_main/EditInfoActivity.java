package com.example.labdata_main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labdata_main.utils.SharedPrefsManager;
import com.google.android.material.textfield.TextInputEditText;

public class EditInfoActivity extends AppCompatActivity {
    private TextInputEditText etName;
    private TextInputEditText etPhone;
    private TextInputEditText etCompany;
    private Button btnSave;
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        // 初始化 SharedPrefsManager
        sharedPrefsManager = new SharedPrefsManager(this);

        // 初始化视图
        initViews();
        
        // 加载当前用户信息
        loadUserInfo();

        // 设置保存按钮点击事件
        btnSave.setOnClickListener(v -> saveUserInfo());
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCompany = findViewById(R.id.etCompany);
        btnSave = findViewById(R.id.btnSave);
    }

    private void loadUserInfo() {
        etName.setText(sharedPrefsManager.getUserName());
        etPhone.setText(sharedPrefsManager.getUserPhone());
        etCompany.setText(sharedPrefsManager.getUserCompany());
    }

    private void saveUserInfo() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String company = etCompany.getText().toString().trim();

        // 验证输入
        if (name.isEmpty()) {
            etName.setError("请输入姓名");
            return;
        }

        // 保存用户信息
        sharedPrefsManager.saveUserName(name);
        sharedPrefsManager.saveUserPhone(phone);
        sharedPrefsManager.saveUserCompany(company);

        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
