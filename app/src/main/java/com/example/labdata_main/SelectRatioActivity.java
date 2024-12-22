package com.example.labdata_main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectRatioActivity extends AppCompatActivity {
    private Button btnRatio1;
    private Button btnRatio2;
    private Button btnAddRatio;
    private ImageButton btnBack;
    private String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ratio);

        deviceName = getIntent().getStringExtra("device_name");
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnRatio1 = findViewById(R.id.btnRatio1);
        btnRatio2 = findViewById(R.id.btnRatio2);
        btnAddRatio = findViewById(R.id.btnAddRatio);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnRatio1.setOnClickListener(v -> {
            // TODO: 处理配比一的选择
            Toast.makeText(this, "选择了配比一", Toast.LENGTH_SHORT).show();
        });

        btnRatio2.setOnClickListener(v -> {
            // TODO: 处理配比二的选择
            Toast.makeText(this, "选择了配比二", Toast.LENGTH_SHORT).show();
        });

        btnAddRatio.setOnClickListener(v -> {
            // TODO: 处理添加新配比
            Toast.makeText(this, "添加新配比", Toast.LENGTH_SHORT).show();
        });
    }
}
