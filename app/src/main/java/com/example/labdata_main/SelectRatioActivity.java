package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SelectRatioActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button btnNext;
    private ImageButton btnBack;
    private TextView tvProjectName;
    private TextView tvProgress;
    private String projectName;
    private String selectedRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ratio);

        projectName = getIntent().getStringExtra("project_name");
        initViews();
        setupClickListeners();
        updateUI();
    }

    private void initViews() {
        radioGroup = findViewById(R.id.radioGroup);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        tvProjectName = findViewById(R.id.tvProjectName);
        tvProgress = findViewById(R.id.tvProgress);
    }

    private void updateUI() {
        tvProjectName.setText(projectName);
        // 设置进度文字中"原料配比"为紫色
        String progressText = tvProgress.getText().toString();
        int purpleColor = ContextCompat.getColor(this, android.R.color.holo_purple);
        tvProgress.setTextColor(purpleColor);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            btnNext.setEnabled(true);
            btnNext.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_purple));
            
            if (checkedId == R.id.btnRatio1) {
                selectedRatio = "配比一";
            } else if (checkedId == R.id.btnRatio2) {
                selectedRatio = "配比二";
            } else if (checkedId == R.id.btnAddRatio) {
                selectedRatio = "新配比";
            }
            
            // 保存选择的配比
            SharedPrefsManager.saveString(this, "selected_ratio", selectedRatio);
        });

        btnNext.setOnClickListener(v -> {
            if (selectedRatio != null) {
                // 跳转到制件方式界面
                Intent intent = new Intent(this, ManufacturingMethodActivity.class);
                intent.putExtra("project_name", projectName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "请先选择配比", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
