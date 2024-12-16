package com.example.labdata_main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.labdata_main.utils.SharedPrefsManager;

public class EditInfoActivity extends AppCompatActivity {
    private TextInputEditText etName;
    private TextInputEditText etPhone;
    private TextInputEditText etCompany;
    private MaterialButton btnSave;
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        // 初始化 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // 初始化控件
        initViews();
        // 设置点击事件
        setClickListeners();
        // 加载现有信息
        loadExistingInfo();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCompany = findViewById(R.id.etCompany);
        btnSave = findViewById(R.id.btnSave);
        sharedPrefsManager = new SharedPrefsManager(this);
    }

    private void setClickListeners() {
        btnSave.setOnClickListener(v -> saveUserInfo());
    }

    private void loadExistingInfo() {
        String name = sharedPrefsManager.getUserName();
        String phone = sharedPrefsManager.getUserPhone();
        String company = sharedPrefsManager.getUserCompany();

        if (!TextUtils.isEmpty(name)) {
            etName.setText(name);
        }
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
        }
        if (!TextUtils.isEmpty(company)) {
            etCompany.setText(company);
        }
    }

    private void saveUserInfo() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String company = etCompany.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
