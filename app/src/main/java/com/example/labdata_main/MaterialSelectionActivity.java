package com.example.labdata_main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.tabs.TabLayout;

public class MaterialSelectionActivity extends AppCompatActivity {
    private TabLayout materialTypeTabs;
    private TextView materialName;
    private TextView materialBatch;
    private ChipGroup gradationChips;
    private Slider percentageSlider;
    private TextView percentageText;
    private float maxAvailablePercentage = 100f; // 从Intent中获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_selection);

        // 获取可用百分比
        maxAvailablePercentage = getIntent().getFloatExtra("available_percentage", 100f);

        initViews();
        setupTabs();
        setupGradationChips();
        setupPercentageSlider();
        setupListeners();
    }

    private void initViews() {
        materialTypeTabs = findViewById(R.id.material_type_tabs);
        materialName = findViewById(R.id.material_name);
        materialBatch = findViewById(R.id.material_batch);
        gradationChips = findViewById(R.id.gradation_chips);
        percentageSlider = findViewById(R.id.percentage_slider);
        percentageText = findViewById(R.id.percentage_text);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // 设置最大可用百分比
        percentageSlider.setValueTo(maxAvailablePercentage);
        updatePercentageText(0f);
    }

    private void setupTabs() {
        materialTypeTabs.addTab(materialTypeTabs.newTab().setText("沥青"));
        materialTypeTabs.addTab(materialTypeTabs.newTab().setText("沙子"));
        materialTypeTabs.addTab(materialTypeTabs.newTab().setText("石子"));

        materialTypeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateMaterialInfo(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupGradationChips() {
        String[] gradations = {"3.2", "9.5", "1.18", "2.36", "4.75"};
        for (String gradation : gradations) {
            Chip chip = new Chip(this);
            chip.setText(gradation);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            chip.setTextColor(getResources().getColor(R.color.gray));
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setCheckedIconTint(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            chip.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.blue_light)));
            gradationChips.addView(chip);
        }
    }

    private void setupPercentageSlider() {
        percentageSlider.addOnChangeListener((slider, value, fromUser) -> {
            updatePercentageText(value);
        });
        
        // 设置滑块颜色
        percentageSlider.setTrackActiveTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        percentageSlider.setTrackInactiveTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light)));
        percentageSlider.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
    }

    private void setupListeners() {
        findViewById(R.id.confirm_button).setOnClickListener(v -> {
            // 获取选中的级配值
            int checkedChipId = gradationChips.getCheckedChipId();
            String selectedGradation = "";
            if (checkedChipId != -1) {
                Chip selectedChip = findViewById(checkedChipId);
                selectedGradation = selectedChip.getText().toString();
            }

            // 返回选择结果
            Intent resultIntent = new Intent();
            resultIntent.putExtra("material_type", materialTypeTabs.getSelectedTabPosition());
            resultIntent.putExtra("material_name", materialName.getText().toString());
            resultIntent.putExtra("material_batch", materialBatch.getText().toString());
            resultIntent.putExtra("gradation", selectedGradation);
            resultIntent.putExtra("percentage", percentageSlider.getValue());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void updateMaterialInfo(int position) {
        switch (position) {
            case 0: // 沥青
                materialName.setText("沥青·中国石化");
                materialBatch.setText("编号：A01");
                break;
            case 1: // 沙子
                materialName.setText("沙子·标准砂");
                materialBatch.setText("编号：S01");
                break;
            case 2: // 石子
                materialName.setText("石子·碎石");
                materialBatch.setText("编号：R01");
                break;
        }
    }

    private void updatePercentageText(float value) {
        percentageText.setText(String.format("%.1f%%", value));
        percentageText.setTextColor(getResources().getColor(R.color.gray));
    }
}
