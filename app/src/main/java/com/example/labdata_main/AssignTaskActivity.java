package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labdata_main.adapter.ExperimenterAdapter;
import com.example.labdata_main.adapter.ProjectAdapter;
import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.model.Experimenter;
import com.example.labdata_main.model.Project;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignTaskActivity extends AppCompatActivity {
    private RecyclerView rvExperimenters;
    private Button btnSave;
    private ImageButton btnBack;
    private ExperimenterAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Experimenter> selectedExperimenters = new ArrayList<>();
    private Map<Integer, List<Project>> projectPermissionsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);

        databaseHelper = new DatabaseHelper(this);
        initViews();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initViews() {
        rvExperimenters = findViewById(R.id.rvExperimenters);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupRecyclerView() {
        // 添加测试数据
        List<Experimenter> experimenters = new ArrayList<>();
        experimenters.add(new Experimenter(1, "小李"));
        experimenters.add(new Experimenter(2, "小王"));
        experimenters.add(new Experimenter(3, "小张"));

        adapter = new ExperimenterAdapter(
            experimenters,
            this::showPermissionDialog,
            (experimenter, isSelected) -> {
                if (isSelected) {
                    selectedExperimenters.add(experimenter);
                } else {
                    selectedExperimenters.remove(experimenter);
                }
            }
        );
        
        rvExperimenters.setLayoutManager(new LinearLayoutManager(this));
        rvExperimenters.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> {
            if (selectedExperimenters.isEmpty()) {
                Toast.makeText(this, "请选择至少一名实验员", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, AddDeviceActivity.class));
        });
    }

    private void showPermissionDialog(Experimenter experimenter) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_permissions, null);
        dialog.setContentView(view);

        Button btnSavePermissions = view.findViewById(R.id.btnSavePermissions);
        Button btnViewDetails = view.findViewById(R.id.btnViewDetails);
        
        androidx.appcompat.widget.SwitchCompat switchTaskAssignment = view.findViewById(R.id.switchTaskAssignment);
        androidx.appcompat.widget.SwitchCompat switchDeviceManagement = view.findViewById(R.id.switchDeviceManagement);

        // 设置当前权限状态
        switchTaskAssignment.setChecked(experimenter.isCanAssignTasks());
        switchDeviceManagement.setChecked(experimenter.isCanManageDevices());

        btnSavePermissions.setOnClickListener(v -> {
            experimenter.setCanAssignTasks(switchTaskAssignment.isChecked());
            experimenter.setCanManageDevices(switchDeviceManagement.isChecked());
            databaseHelper.updateExperimenterPermissions(
                experimenter.getId(),
                switchTaskAssignment.isChecked(),
                switchDeviceManagement.isChecked()
            );
            Toast.makeText(this, "权限已保存", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnViewDetails.setOnClickListener(v -> {
            showProjectPermissionsDialog(experimenter);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showProjectPermissionsDialog(Experimenter experimenter) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_project_permissions, null);
        dialog.setContentView(view);

        RecyclerView rvProjects = view.findViewById(R.id.rvProjects);
        Button btnSaveProjectPermissions = view.findViewById(R.id.btnSaveProjectPermissions);
        
        // 获取已保存的项目权限或创建新的列表
        List<Project> projects = projectPermissionsMap.getOrDefault(experimenter.getId(), new ArrayList<>());
        if (projects.isEmpty()) {
            // 添加测试数据
            projects.add(new Project(1, "东四环项目一", false));
            projects.add(new Project(2, "东四环项目二", false));
            projects.add(new Project(3, "东四环项目三", false));
        }

        ProjectAdapter projectAdapter = new ProjectAdapter(projects, (project, isAccessible) -> {
            project.setAccessible(isAccessible);
        });

        rvProjects.setLayoutManager(new LinearLayoutManager(this));
        rvProjects.setAdapter(projectAdapter);

        btnSaveProjectPermissions.setOnClickListener(v -> {
            // 保存所有项目权限
            for (Project project : projects) {
                databaseHelper.updateProjectAccess(
                    experimenter.getId(),
                    project.getId(),
                    project.isAccessible()
                );
            }
            // 缓存项目权限
            projectPermissionsMap.put(experimenter.getId(), projects);
            Toast.makeText(this, "项目权限已保存", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
