package com.example.labdata_main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.labdata_main.model.Project;

public class ExperimentTaskSetupActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private MaterialButton btnNext;
    private MaterialButton btnCancel;
    private String taskName;
    private Project selectedProject;

    public static void start(Context context, String taskName) {
        Intent intent = new Intent(context, ExperimentTaskSetupActivity.class);
        intent.putExtra("task_name", taskName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_task_setup);

        taskName = getIntent().getStringExtra("task_name");
        if (taskName == null || taskName.isEmpty()) {
            taskName = "实验任务设置";
        }
        
        initViews();
        setupViewPager();
        setupClickListeners();
        
        // 初始时禁用下一步按钮，直到选择了项目
        enableNextButton(false);
    }

    private void initViews() {
        // 设置工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(taskName);
        }

        // 初始化视图
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        btnNext = findViewById(R.id.btnNext);
        btnCancel = findViewById(R.id.btnCancel);

        // 确保所有必要的视图都已找到
        if (viewPager == null || tabLayout == null || btnNext == null || btnCancel == null) {
            throw new IllegalStateException("Required views not found. Check your layout file.");
        }
    }

    private void setupViewPager() {
        ExperimentTaskPagerAdapter adapter = new ExperimentTaskPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false); // 禁止滑动切换

        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("选择项目");
                        break;
                    case 1:
                        tab.setText("选择配比");
                        break;
                    case 2:
                        tab.setText("制件方式");
                        break;
                    case 3:
                        tab.setText("实验指派");
                        break;
                }
            }
        ).attach();
    }

    private void setupClickListeners() {
        // 设置取消按钮点击事件
        btnCancel.setOnClickListener(v -> finish());
        
        // 设置下一步按钮点击事件
        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < 3) { // 最后一页是3
                viewPager.setCurrentItem(currentItem + 1);
                updateNextButtonState(currentItem + 1);
            } else {
                // TODO: 保存并完成
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNextButtonState(int currentPage) {
        switch (currentPage) {
            case 0: // 选择项目页面
                enableNextButton(selectedProject != null);
                break;
            case 1: // 选择配比页面
                // TODO: 根据配比选择状态更新按钮
                enableNextButton(true);
                break;
            case 2: // 制件方式页面
                // TODO: 根据制件方式选择状态更新按钮
                enableNextButton(true);
                break;
            case 3: // 实验指派页面
                btnNext.setText("完成");
                enableNextButton(true);
                break;
        }
    }

    public void enableNextButton(boolean enable) {
        if (btnNext != null) {
            btnNext.setEnabled(enable);
        }
    }

    public void setSelectedProject(Project project) {
        this.selectedProject = project;
        enableNextButton(project != null);
    }

    public Project getSelectedProject() {
        return selectedProject;
    }
}
