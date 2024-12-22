package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.utils.SharedPrefsManager;

public class ExperimentFragment extends Fragment {
    private Button btnAssignTask;
    private SharedPrefsManager sharedPrefsManager;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiment, container, false);
        
        initViews(view);
        setupClickListeners();
        checkUserType();
        
        return view;
    }

    private void initViews(View view) {
        btnAssignTask = view.findViewById(R.id.btnAssignTask);
        sharedPrefsManager = new SharedPrefsManager(requireContext());
        databaseHelper = new DatabaseHelper(requireContext());
    }

    private void setupClickListeners() {
        btnAssignTask.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AssignTaskActivity.class);
            startActivity(intent);
        });
    }

    private void checkUserType() {
        // 获取用户类型
        int userType = sharedPrefsManager.getUserType();
        
        // 如果不是管理员，隐藏下发任务按钮
        if (userType != 1) { // 1表示管理员
            btnAssignTask.setVisibility(View.GONE);
        } else {
            btnAssignTask.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUserType(); // 每次恢复时检查用户类型
    }
}
