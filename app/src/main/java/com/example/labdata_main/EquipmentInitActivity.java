package com.example.labdata_main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.model.Equipment;
import com.example.labdata_main.utils.SharedPrefsManager;

/**
 * 设备初始化活动
 * 用于收集用户单位的设备信息，包括拌合、制件和实验三类设备
 * 每类设备至少需要添加一个才能完成初始化
 */
public class EquipmentInitActivity extends AppCompatActivity {
    /** 拌合设备容器 */
    private LinearLayout mixingContainer;
    /** 制件设备容器 */
    private LinearLayout moldingContainer;
    /** 实验设备容器 */
    private LinearLayout testingContainer;
    /** 添加拌合设备按钮 */
    private Button btnAddMixing;
    /** 添加制件设备按钮 */
    private Button btnAddMolding;
    /** 添加实验设备按钮 */
    private Button btnAddTesting;
    /** 完成初始化按钮 */
    private Button btnFinish;
    /** 数据库帮助类 */
    private DatabaseHelper databaseHelper;
    /** 当前用户单位名称 */
    private String companyName;
    /** SharedPreferences管理类 */
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_equipment_init);

        // 初始化数据
        databaseHelper = new DatabaseHelper(this);
        sharedPrefsManager = new SharedPrefsManager(this);
        companyName = sharedPrefsManager.getLoggedInUserCompany();

        // 初始化视图和监听器
        initViews();
        setupListeners();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        mixingContainer = findViewById(R.id.mixingEquipmentContainer);
        moldingContainer = findViewById(R.id.moldingEquipmentContainer);
        testingContainer = findViewById(R.id.testingEquipmentContainer);
        btnAddMixing = findViewById(R.id.btnAddMixing);
        btnAddMolding = findViewById(R.id.btnAddMolding);
        btnAddTesting = findViewById(R.id.btnAddTesting);
        btnFinish = findViewById(R.id.btnFinish);
    }

    /**
     * 设置按钮点击监听器
     */
    private void setupListeners() {
        btnAddMixing.setOnClickListener(v -> showAddEquipmentDialog("拌合"));
        btnAddMolding.setOnClickListener(v -> showAddEquipmentDialog("制件"));
        btnAddTesting.setOnClickListener(v -> showAddEquipmentDialog("实验"));
        btnFinish.setOnClickListener(v -> validateAndFinish());
    }

    /**
     * 显示添加设备对话框
     * @param type 设备类型（拌合、制件、实验）
     */
    private void showAddEquipmentDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_equipment, null);
        
        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        EditText etModel = dialogView.findViewById(R.id.etModel);
        EditText etManufacturer = dialogView.findViewById(R.id.etManufacturer);
        EditText etPurchaseYear = dialogView.findViewById(R.id.etPurchaseYear);

        dialogTitle.setText("添加" + type + "设备");

        builder.setView(dialogView)
               .setPositiveButton("添加", (dialog, which) -> {
                   // 获取输入的设备信息
                   String model = etModel.getText().toString().trim();
                   String manufacturer = etManufacturer.getText().toString().trim();
                   String yearStr = etPurchaseYear.getText().toString().trim();

                   // 验证输入是否完整
                   if (model.isEmpty() || manufacturer.isEmpty() || yearStr.isEmpty()) {
                       Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                       return;
                   }

                   // 添加设备到数据库
                   int year = Integer.parseInt(yearStr);
                   Equipment equipment = new Equipment(companyName, type, model, manufacturer, year);
                   long id = databaseHelper.addEquipment(equipment);

                   // 添加成功后更新UI
                   if (id != -1) {
                       addEquipmentView(type, model, manufacturer, year);
                       updateFinishButtonState();
                   }
               })
               .setNegativeButton("取消", null)
               .create()
               .show();
    }

    /**
     * 添加设备视图到对应的容器
     * @param type 设备类型
     * @param model 设备型号
     * @param manufacturer 生产厂家
     * @param year 购买年限
     */
    private void addEquipmentView(String type, String model, String manufacturer, int year) {
        LinearLayout container = null;
        // 根据设备类型选择对应的容器
        switch (type) {
            case "拌合":
                container = mixingContainer;
                break;
            case "制件":
                container = moldingContainer;
                break;
            case "实验":
                container = testingContainer;
                break;
        }

        if (container != null) {
            // 创建并添加设备信息视图
            View equipmentView = LayoutInflater.from(this).inflate(R.layout.item_equipment, container, false);
            TextView tvInfo = equipmentView.findViewById(R.id.tvEquipmentInfo);
            tvInfo.setText(String.format("型号：%s\n厂家：%s\n购买年限：%d", model, manufacturer, year));
            container.addView(equipmentView);
        }
    }

    /**
     * 验证设备是否已全部添加并完成初始化
     */
    private void validateAndFinish() {
        // 检查是否每类设备都至少添加了一个
        if (mixingContainer.getChildCount() == 0 ||
            moldingContainer.getChildCount() == 0 ||
            testingContainer.getChildCount() == 0) {
            Toast.makeText(this, "请确保每类设备至少添加一个", Toast.LENGTH_SHORT).show();
            return;
        }

        // 初始化完成，跳转到主页面
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 更新完成按钮的状态
     * 只有当每类设备都至少添加了一个时，完成按钮才可用
     */
    private void updateFinishButtonState() {
        boolean canFinish = mixingContainer.getChildCount() > 0 &&
                          moldingContainer.getChildCount() > 0 &&
                          testingContainer.getChildCount() > 0;
        btnFinish.setEnabled(canFinish);
    }
}
