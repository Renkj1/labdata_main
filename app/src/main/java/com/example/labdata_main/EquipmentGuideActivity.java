package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EquipmentGuideActivity extends AppCompatActivity {
    private String companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_equipment_guide);

        companyId = getIntent().getStringExtra("company_id");
        if (companyId == null) {
            finish();
            return;
        }

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> startInitialization());
    }

    private void startInitialization() {
        Intent intent = new Intent(this, EquipmentInitActivity.class);
        intent.putExtra("company_id", companyId);
        startActivity(intent);
        finish();
    }
}
