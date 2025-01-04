package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.adapter.ProjectSelectionAdapter;
import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.model.Project;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class SelectProjectActivity extends AppCompatActivity 
        implements ProjectSelectionAdapter.OnProjectSelectedListener, 
                   AddProjectBottomSheet.OnProjectAddedListener {
                   
    private RecyclerView rvProjects;
    private MaterialButton btnNext;
    private ProjectSelectionAdapter adapter;
    private DatabaseHelper databaseHelper;
    private Project selectedProject;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_project);

        databaseHelper = new DatabaseHelper(this);
        initViews();
        setupClickListeners();
        loadProjects();
    }

    private void initViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        rvProjects = findViewById(R.id.rvProjects);
        btnNext = findViewById(R.id.btnNext);
        FloatingActionButton fabAddProject = findViewById(R.id.fabAddProject);

        // 设置RecyclerView
        rvProjects.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProjectSelectionAdapter(this);
        rvProjects.setAdapter(adapter);

        // 返回按钮点击事件
        btnBack.setOnClickListener(v -> finish());

        // 添加项目按钮点击事件
        fabAddProject.setOnClickListener(v -> {
            AddProjectBottomSheet bottomSheet = AddProjectBottomSheet.newInstance();
            bottomSheet.setOnProjectAddedListener(this);
            bottomSheet.show(getSupportFragmentManager(), "AddProject");
        });
    }

    private void setupClickListeners() {
        btnNext.setOnClickListener(v -> {
            if (selectedProject == null) {
                Toast.makeText(this, "请选择一个项目", Toast.LENGTH_SHORT).show();
                return;
            }
            // 跳转到选择配比界面
            Intent intent = new Intent(this, SelectMixRatioActivity.class);
            intent.putExtra("project_id", selectedProject.getId());
            intent.putExtra("project_name", selectedProject.getName());
            startActivity(intent);
        });
    }

    private void loadProjects() {
        new Thread(() -> {
            List<Project> projects = databaseHelper.getAccessibleProjects();
            mainHandler.post(() -> {
                if (projects.isEmpty()) {
                    Toast.makeText(this, "暂无可选项目，请先添加项目", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.submitList(new ArrayList<>(projects));
            });
        }).start();
    }

    @Override
    public void onProjectSelected(Project project) {
        this.selectedProject = project;
        btnNext.setEnabled(true);
    }

    @Override
    public void onProjectAdded(Project project) {
        // 刷新项目列表
        loadProjects();
    }
}
