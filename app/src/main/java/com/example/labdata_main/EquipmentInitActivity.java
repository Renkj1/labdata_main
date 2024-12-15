package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labdata_main.constants.EquipmentConstants;
import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.model.Equipment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EquipmentInitActivity extends AppCompatActivity {
    private LinearLayout mixingContainer;
    private LinearLayout formingContainer;
    private LinearLayout testingContainer;
    private Button btnFinish;
    private DatabaseHelper databaseHelper;
    private String companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_equipment_init);

        // 初始化视图
        initViews();
        // 设置点击事件
        setupClickListeners();
        
        // 获取公司ID
        companyId = getIntent().getStringExtra("company_id");
        if (companyId == null) {
            Toast.makeText(this, "公司信息获取失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseHelper = new DatabaseHelper(this);
        Log.d("EquipmentInit", "Received companyId: " + companyId);
        databaseHelper.logAllUsers(); // 记录所有用户信息以便调试
    }

    private void initViews() {
        mixingContainer = findViewById(R.id.mixingEquipmentContainer);
        formingContainer = findViewById(R.id.formingEquipmentContainer);
        testingContainer = findViewById(R.id.testingEquipmentContainer);
        btnFinish = findViewById(R.id.btnFinish);
    }

    private void setupClickListeners() {
        findViewById(R.id.btnAddMixing).setOnClickListener(v -> addEquipmentView(mixingContainer, "MIXING"));
        findViewById(R.id.btnAddForming).setOnClickListener(v -> addEquipmentView(formingContainer, "FORMING"));
        findViewById(R.id.btnAddTesting).setOnClickListener(v -> addEquipmentView(testingContainer, "TESTING"));
        
        btnFinish.setOnClickListener(v -> validateAndSaveEquipment());
    }

    private void addEquipmentView(LinearLayout container, String type) {
        View equipmentView = LayoutInflater.from(this).inflate(R.layout.item_equipment, container, false);
        
        Spinner spinnerManufacturer = equipmentView.findViewById(R.id.spinnerManufacturer);
        Spinner spinnerModel = equipmentView.findViewById(R.id.spinnerModel);
        EditText etPurchaseYear = equipmentView.findViewById(R.id.etPurchaseYear);
        Button btnDelete = equipmentView.findViewById(R.id.btnDelete);

        // 设置厂家下拉菜单
        List<String> manufacturers = EquipmentConstants.getManufacturers(type);
        ArrayAdapter<String> manufacturerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, manufacturers);
        manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerManufacturer.setAdapter(manufacturerAdapter);

        // 设置型号下拉菜单
        spinnerManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedManufacturer = manufacturers.get(position);
                List<String> models = EquipmentConstants.getModels(type, selectedManufacturer);
                ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(EquipmentInitActivity.this,
                        android.R.layout.simple_spinner_item, models);
                modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerModel.setAdapter(modelAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnDelete.setOnClickListener(v -> container.removeView(equipmentView));
        container.addView(equipmentView);
    }

    private void validateAndSaveEquipment() {
        // 验证每种类型是否至少有一个设备
        if (mixingContainer.getChildCount() == 0 || 
            formingContainer.getChildCount() == 0 || 
            testingContainer.getChildCount() == 0) {
            Toast.makeText(this, "每种类型至少需要添加一个设备", Toast.LENGTH_SHORT).show();
            return;
        }

        // 收集并保存所有设备信息
        List<Equipment> equipmentList = new ArrayList<>();
        
        // 收集拌合设备
        equipmentList.addAll(collectEquipment(mixingContainer, "MIXING"));
        // 收集制件设备
        equipmentList.addAll(collectEquipment(formingContainer, "FORMING"));
        // 收集实验设备
        equipmentList.addAll(collectEquipment(testingContainer, "TESTING"));

        if (validateEquipmentList(equipmentList)) {
            boolean success = true;
            String errorMessage = "保存失败：";
            
            for (Equipment equipment : equipmentList) {
                equipment.setCompanyId(companyId);
                Log.d("EquipmentInit", "Saving equipment: " + 
                    "CompanyId=" + equipment.getCompanyId() + 
                    ", Type=" + equipment.getType() + 
                    ", Model=" + equipment.getModel() + 
                    ", Manufacturer=" + equipment.getManufacturer() + 
                    ", Year=" + equipment.getPurchaseYear());
                
                long result = databaseHelper.addEquipment(equipment);
                if (result == -1) {
                    success = false;
                    errorMessage += "\n" + equipment.getType() + " 类型设备保存失败";
                    Log.e("EquipmentInit", "Failed to save " + equipment.getType());
                }
            }

            if (success) {
                Toast.makeText(this, "设备信息保存成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<Equipment> collectEquipment(LinearLayout container, String type) {
        List<Equipment> equipmentList = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            Spinner spinnerManufacturer = view.findViewById(R.id.spinnerManufacturer);
            Spinner spinnerModel = view.findViewById(R.id.spinnerModel);
            EditText etPurchaseYear = view.findViewById(R.id.etPurchaseYear);

            String manufacturer = spinnerManufacturer.getSelectedItem().toString();
            String model = spinnerModel.getSelectedItem().toString();
            String purchaseYear = etPurchaseYear.getText().toString().trim();

            Equipment equipment = new Equipment();
            equipment.setType(type);
            equipment.setManufacturer(manufacturer);
            equipment.setModel(model);
            equipment.setPurchaseYear(purchaseYear);

            equipmentList.add(equipment);
        }
        return equipmentList;
    }

    private boolean validateEquipmentList(List<Equipment> equipmentList) {
        if (equipmentList.isEmpty()) {
            Toast.makeText(this, "请至少添加一个设备", Toast.LENGTH_SHORT).show();
            return false;
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        
        for (Equipment equipment : equipmentList) {
            if (equipment.getManufacturer().isEmpty() || 
                equipment.getModel().isEmpty() || 
                equipment.getPurchaseYear().isEmpty()) {
                Toast.makeText(this, "请填写完整的设备信息", Toast.LENGTH_SHORT).show();
                return false;
            }

            try {
                int year = Integer.parseInt(equipment.getPurchaseYear());
                if (year <= 0 || year > currentYear) {
                    Toast.makeText(this, "请输入有效的购买年限", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "请输入有效的购买年限", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
