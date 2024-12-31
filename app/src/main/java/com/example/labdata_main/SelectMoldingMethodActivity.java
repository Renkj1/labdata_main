package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.adapter.SpecimenAdapter;
import com.example.labdata_main.database.AppDatabase;
import com.example.labdata_main.model.Specimen;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class SelectMoldingMethodActivity extends AppCompatActivity implements SpecimenAdapter.OnSpecimenClickListener {
    private static final String TAG = "SelectMoldingMethod";
    private static final int REQUEST_CUT_PROPERTIES = 1;
    
    private int projectId;
    private String projectName;
    private long mixRatioId;
    private String mixRatioName;
    
    private RecyclerView rvSpecimens;
    private SpecimenAdapter adapter;
    private View bottomSheet;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextInputEditText etMixingTemperature;
    private TextInputEditText etMixingSpeed;
    private AutoCompleteTextView spinnerCompactionMethod;
    private MaterialButton btnNext;
    private AppDatabase db;

    private static final String[] COMPACTION_METHODS = {
        "马歇尔击实", "轮碾击实", "静压法"
    };

    private final ActivityResultLauncher<Intent> cutPropertiesLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                saveCutProperties(data);
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_molding_method);

        // 获取传递的信息
        projectId = getIntent().getIntExtra("project_id", -1);
        projectName = getIntent().getStringExtra("project_name");
        mixRatioId = getIntent().getLongExtra("mix_ratio_id", -1);
        mixRatioName = getIntent().getStringExtra("mix_ratio_name");

        Log.d(TAG, String.format("Project: %d-%s, MixRatio: %d-%s",
            projectId, projectName, mixRatioId, mixRatioName));

        // 初始化数据库
        db = AppDatabase.getInstance(this);

        initViews();
        setupBottomSheet();
        loadSpecimens();
    }

    private void initViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvTitle);
        rvSpecimens = findViewById(R.id.rvSpecimens);
        FloatingActionButton fabAddSpecimen = findViewById(R.id.fabAddSpecimen);
        btnNext = findViewById(R.id.btnNext);

        // 设置标题
        tvTitle.setText("选择制件方式");

        // 设置RecyclerView
        rvSpecimens.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SpecimenAdapter(this);
        rvSpecimens.setAdapter(adapter);

        // 设置点击事件
        btnBack.setOnClickListener(v -> finish());
        fabAddSpecimen.setOnClickListener(v -> showBottomSheet());
        btnNext.setOnClickListener(v -> {
            // TODO: 跳转到实验方法选择页面
            Toast.makeText(this, "即将实现", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBottomSheet() {
        bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // 初始化输入框
        etMixingTemperature = bottomSheet.findViewById(R.id.etMixingTemperature);
        etMixingSpeed = bottomSheet.findViewById(R.id.etMixingSpeed);
        spinnerCompactionMethod = bottomSheet.findViewById(R.id.spinnerCompactionMethod);
        MaterialButton btnCancel = bottomSheet.findViewById(R.id.btnCancel);
        MaterialButton btnConfirm = bottomSheet.findViewById(R.id.btnConfirm);

        // 设置压实方式下拉选项
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_dropdown_item_1line, COMPACTION_METHODS);
        spinnerCompactionMethod.setAdapter(methodAdapter);

        // 设置按钮点击事件
        btnCancel.setOnClickListener(v -> hideBottomSheet());
        btnConfirm.setOnClickListener(v -> validateAndProceed());
    }

    private void showBottomSheet() {
        // 清空输入框
        etMixingTemperature.setText("");
        etMixingSpeed.setText("");
        spinnerCompactionMethod.setText("");

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void hideBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void loadSpecimens() {
        new Thread(() -> {
            List<Specimen> specimens = db.specimenDao().getSpecimensForMixRatio(mixRatioId);
            runOnUiThread(() -> {
                adapter.setSpecimens(specimens);
                btnNext.setEnabled(!specimens.isEmpty());
            });
        }).start();
    }

    private void validateAndProceed() {
        // 获取输入值
        String tempStr = etMixingTemperature.getText().toString();
        String speedStr = etMixingSpeed.getText().toString();
        String method = spinnerCompactionMethod.getText().toString();

        // 验证输入
        if (tempStr.isEmpty() || speedStr.isEmpty() || method.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float temperature = Float.parseFloat(tempStr);
            float speed = Float.parseFloat(speedStr);

            // 启动切割属性页面
            Intent intent = new Intent(this, CutPropertiesActivity.class);
            intent.putExtra("mixing_temperature", temperature);
            intent.putExtra("mixing_speed", speed);
            intent.putExtra("compaction_method", method);
            cutPropertiesLauncher.launch(intent);
            hideBottomSheet();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数值", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCutProperties(Intent data) {
        float temperature = data.getFloatExtra("mixing_temperature", 0);
        float speed = data.getFloatExtra("mixing_speed", 0);
        String method = data.getStringExtra("compaction_method");
        String cutShape = data.getStringExtra("cut_shape");
        int cutCount = data.getIntExtra("cut_count", 1);

        // 创建试件对象
        Specimen specimen = new Specimen();
        specimen.setMixRatioId(mixRatioId);
        specimen.setMixingTemperature(temperature);
        specimen.setMixingSpeed(speed);
        specimen.setCompactionMethod(method);
        specimen.setCreationTime(System.currentTimeMillis());
        specimen.setCutShape(cutShape);
        specimen.setCutCount(cutCount);

        // 根据切割形状设置尺寸
        switch (cutShape) {
            case "CUBOID":
                specimen.setLength(data.getFloatExtra("length", 0));
                specimen.setWidth(data.getFloatExtra("width", 0));
                specimen.setHeight(data.getFloatExtra("height", 0));
                break;

            case "CYLINDER":
            case "HALF_CYLINDER":
                specimen.setRadius(data.getFloatExtra("radius", 0));
                specimen.setHeight(data.getFloatExtra("height", 0));
                break;
        }

        // 保存到数据库
        new Thread(() -> {
            long id = db.specimenDao().insert(specimen);
            Log.d(TAG, "Saved specimen with id: " + id);
            runOnUiThread(() -> {
                Toast.makeText(this, "试件添加成功", Toast.LENGTH_SHORT).show();
                loadSpecimens(); // 重新加载列表
            });
        }).start();
    }

    @Override
    public void onSpecimenClick(Specimen specimen) {
        // TODO: 实现试件点击事件
        Toast.makeText(this, "点击了试件 #" + specimen.getId(), Toast.LENGTH_SHORT).show();
    }
}
