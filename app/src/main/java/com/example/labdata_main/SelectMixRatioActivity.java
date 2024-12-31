package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.adapter.MixRatioSelectionAdapter;
import com.example.labdata_main.database.AppDatabase;
import com.example.labdata_main.model.MixRatio;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class SelectMixRatioActivity extends AppCompatActivity implements MixRatioSelectionAdapter.OnMixRatioSelectedListener {
    private static final String TAG = "SelectMixRatioActivity";
    private int projectId;
    private String projectName;
    private RecyclerView rvMixRatios;
    private MaterialButton btnNext;
    private MixRatioSelectionAdapter adapter;
    private AppDatabase db;
    private MixRatio selectedMixRatio;
    private FloatingActionButton fabAddMixRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mix_ratio);

        // 获取传递的项目信息
        projectId = getIntent().getIntExtra("project_id", -1);
        projectName = getIntent().getStringExtra("project_name");

        Log.d(TAG, "onCreate: projectId=" + projectId + ", projectName=" + projectName);

        // 初始化数据库
        db = AppDatabase.getInstance(this);

        initViews();
        loadMixRatios();
    }

    private void initViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvTitle);
        rvMixRatios = findViewById(R.id.rvMixRatios);
        btnNext = findViewById(R.id.btnNext);
        fabAddMixRatio = findViewById(R.id.fabAddMixRatio);

        // 设置标题
        if (projectName != null) {
            tvTitle.setText("选择配比 - " + projectName);
        }

        // 设置RecyclerView
        rvMixRatios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MixRatioSelectionAdapter(this, this);
        rvMixRatios.setAdapter(adapter);

        // 设置点击事件
        btnBack.setOnClickListener(v -> finish());
        btnNext.setOnClickListener(v -> {
            if (selectedMixRatio == null) {
                Toast.makeText(this, "请选择一个配比", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, SelectMoldingMethodActivity.class);
            intent.putExtra("project_id", projectId);
            intent.putExtra("project_name", projectName);
            intent.putExtra("mix_ratio_id", selectedMixRatio.getId());
            intent.putExtra("mix_ratio_name", selectedMixRatio.getName());
            startActivity(intent);
        });

        // 添加配比按钮
        fabAddMixRatio.setOnClickListener(v -> {
            Log.d(TAG, "Clicked add mix ratio button");
            Intent intent = new Intent(this, MixRatioEditActivity.class);
            startActivity(intent);
        });
    }

    private void loadMixRatios() {
        Log.d(TAG, "Starting to load mix ratios");
        new Thread(() -> {
            try {
                List<MixRatio> mixRatios = db.mixRatioDao().getAllMixRatios();
                Log.d(TAG, "Loaded " + (mixRatios != null ? mixRatios.size() : 0) + " mix ratios");
                
                runOnUiThread(() -> {
                    if (mixRatios == null || mixRatios.isEmpty()) {
                        Log.d(TAG, "No mix ratios found");
                        Toast.makeText(this, "暂无配比方案，请先添加配比", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Setting mix ratios to adapter");
                        adapter.setMixRatios(mixRatios);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading mix ratios", e);
                runOnUiThread(() -> {
                    Toast.makeText(this, "加载配比失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        loadMixRatios(); // 每次返回页面时重新加载数据
    }

    @Override
    public void onMixRatioSelected(MixRatio mixRatio) {
        Log.d(TAG, "Selected mix ratio: " + mixRatio.getName());
        this.selectedMixRatio = mixRatio;
        btnNext.setEnabled(true);
    }
}
