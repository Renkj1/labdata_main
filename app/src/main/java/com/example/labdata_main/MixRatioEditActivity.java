package com.example.labdata_main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MixRatioEditActivity extends AppCompatActivity {
    private PieChart pieChart;
    private RecyclerView materialsList;
    private MaterialAdapter adapter;
    private List<MaterialItem> materials;
    private String mixRatioName;

    private static final int REQUEST_ADD_MATERIAL = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_ratio_edit);

        // 初始化数据
        materials = new ArrayList<>();
        mixRatioName = getIntent().getStringExtra("mix_ratio_name");
        
        // 初始化视图和设置
        initViews();
        setupMaterialsList();
        setupPieChart();
        setupListeners();
    }

    private void initViews() {
        TextView titleText = findViewById(R.id.mix_ratio_name);
        titleText.setText(mixRatioName);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        pieChart = findViewById(R.id.pie_chart);
        materialsList = findViewById(R.id.materials_list);

        FloatingActionButton addButton = findViewById(R.id.add_material_button);
        addButton.setOnClickListener(v -> {
            // 计算剩余可用百分比
            float usedPercentage = 0;
            for (MaterialItem material : materials) {
                usedPercentage += material.getPercentage();
            }
            float availablePercentage = 100 - usedPercentage;

            if (availablePercentage > 0) {
                Intent intent = new Intent(this, MaterialSelectionActivity.class);
                intent.putExtra("available_percentage", availablePercentage);
                startActivityForResult(intent, REQUEST_ADD_MATERIAL);
            }
        });
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setHoleRadius(40f);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        
        // 设置中心文字
        pieChart.setCenterText(mixRatioName);
        pieChart.setCenterTextSize(16f);
        pieChart.setCenterTextColor(Color.BLACK);
        
        // 设置图例
        pieChart.getLegend().setEnabled(false);
        
        // 初始状态显示空状态
        updatePieChart();
        
        pieChart.animateY(1400);
    }

    private void setupMaterialsList() {
        adapter = new MaterialAdapter(materials, this::updatePieChart);
        materialsList.setLayoutManager(new LinearLayoutManager(this));
        materialsList.setAdapter(adapter);
    }

    private void setupListeners() {
        findViewById(R.id.next_step_button).setOnClickListener(v -> {
            if (validateMaterials()) {
                saveMixRatio();
            }
        });

        // 点击添加按钮添加新原料
        FloatingActionButton addButton = findViewById(R.id.add_material_button);
        addButton.setOnClickListener(v -> {
            // 计算剩余可用百分比
            float usedPercentage = 0;
            for (MaterialItem material : materials) {
                usedPercentage += material.getPercentage();
            }
            float availablePercentage = 100 - usedPercentage;

            if (availablePercentage > 0) {
                Intent intent = new Intent(this, MaterialSelectionActivity.class);
                intent.putExtra("maxPercentage", availablePercentage);
                startActivityForResult(intent, REQUEST_ADD_MATERIAL);
            } else {
                android.widget.Toast.makeText(this, "配比总和已达到100%，无法添加新原料", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateMaterials() {
        if (materials.isEmpty()) {
            showToast("请添加至少一种原料");
            return false;
        }

        // 检查是否有沥青、沙子和石子
        boolean hasAsphalt = false;
        boolean hasSand = false;
        boolean hasStone = false;
        
        // 检查总百分比
        float totalPercentage = 0f;

        for (MaterialItem material : materials) {
            String type = material.getType();
            if ("asphalt".equals(type)) {
                hasAsphalt = true;
            } else if ("sand".equals(type)) {
                hasSand = true;
            } else if ("stone".equals(type)) {
                hasStone = true;
            }
            totalPercentage += material.getPercentage();
        }

        // 检查必需的原料类型
        StringBuilder missingMaterials = new StringBuilder();
        if (!hasAsphalt) {
            missingMaterials.append("沥青、");
        }
        if (!hasSand) {
            missingMaterials.append("沙子、");
        }
        if (!hasStone) {
            missingMaterials.append("石子、");
        }

        if (missingMaterials.length() > 0) {
            missingMaterials.setLength(missingMaterials.length() - 1); // 移除最后的顿号
            showToast("缺少必需的原料：" + missingMaterials.toString());
            return false;
        }

        // 检查总百分比是否为100%
        if (Math.abs(totalPercentage - 100) > 0.01f) {
            showToast("原料配比总和必须为100%，当前为" + String.format("%.1f%%", totalPercentage));
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_MATERIAL && resultCode == RESULT_OK && data != null) {
            // 获取选择的原料信息
            String materialType = data.getStringExtra("materialType");
            String materialName = data.getStringExtra("materialName");
            String materialCode = data.getStringExtra("materialCode");
            String gradation = data.getStringExtra("gradation");
            float percentage = data.getFloatExtra("percentage", 0f);

            // 创建原料显示名称
            String displayName;
            if (materialType.equals("sand")) {
                displayName = String.format("%s\n编号：%s\n级配：%s", 
                    materialName, materialCode, gradation);
            } else {
                String typeDisplay = materialType.equals("asphalt") ? "沥青" : "石子";
                displayName = String.format("%s·%s\n编号：%s\n级配：%s", 
                    typeDisplay, materialName, materialCode, gradation);
            }

            // 创建新的原料项
            MaterialItem newMaterial = new MaterialItem(displayName, percentage, materialType);
            materials.add(newMaterial);
            adapter.notifyItemInserted(materials.size() - 1);
            updatePieChart();
        }
    }

    private void updatePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        float totalPercentage = 0;

        // 添加已有材料
        for (MaterialItem material : materials) {
            if (material.getPercentage() > 0) {
                entries.add(new PieEntry(material.getPercentage(), material.getName()));
                totalPercentage += material.getPercentage();
            }
        }

        // 如果总百分比小于100，添加剩余部分
        if (totalPercentage < 100) {
            entries.add(new PieEntry(100 - totalPercentage, "未分配"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(getChartColors());
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.rgb(64, 64, 64)); // 改为深灰色
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        dataSet.setValueLineColor(Color.rgb(64, 64, 64)); // 连接线也改为深灰色
        dataSet.setValueLinePart1Length(0.4f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineWidth(2f);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());

        pieChart.setEntryLabelColor(Color.rgb(64, 64, 64)); // 设置标签文字颜色为深灰色
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private int[] getChartColors() {
        return new int[]{
            Color.rgb(33, 150, 243),    // 明亮的蓝色
            Color.rgb(255, 152, 0),     // 明亮的橙色
            Color.rgb(76, 175, 80),     // 鲜艳的绿色
            Color.rgb(244, 67, 54),     // 鲜艳的红色
            Color.rgb(156, 39, 176),    // 深紫色
            Color.rgb(255, 193, 7),     // 明黄色
            Color.rgb(0, 150, 136),     // 青绿色
            Color.rgb(233, 30, 99),     // 粉红色
            Color.rgb(96, 125, 139),    // 蓝灰色
            Color.rgb(158, 158, 158)    // 用于未分配部分的灰色
        };
    }

    private void saveMixRatio() {
        // 创建新的配比对象
        MixRatio mixRatio = new MixRatio();
        mixRatio.setName(((TextView) findViewById(R.id.mix_ratio_name)).getText().toString());
        mixRatio.setCreatedTime(System.currentTimeMillis());
        
        // 将材料列表转换为JSON字符串
        Gson gson = new Gson();
        String materialsJson = gson.toJson(materials);
        mixRatio.setMaterials(materialsJson);

        // 在后台线程中保存数据
        new Thread(() -> {
            // 获取数据库实例
            AppDatabase db = AppDatabase.getInstance(this);
            // 插入新的配比记录
            long id = db.mixRatioDao().insert(mixRatio);
            
            // 在主线程中处理保存后的操作
            runOnUiThread(() -> {
                if (id > 0) {
                    // 保存成功
                    android.widget.Toast.makeText(this, "配比保存成功", android.widget.Toast.LENGTH_SHORT).show();
                    
                    // 返回主页面
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    // 保存失败
                    android.widget.Toast.makeText(this, "保存失败，请重试", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
