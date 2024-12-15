package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.labdata_main.utils.SharedPrefsManager;

/**
 * "我的"界面Fragment
 * 显示用户个人信息和提供退出登录功能
 */
public class MyFragment extends Fragment {
    private TextView tvCompany;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvEmail;
    private Button btnLogout;

    private SharedPrefsManager sharedPrefsManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        // 初始化工具类
        sharedPrefsManager = new SharedPrefsManager(getContext());

        // 初始化界面控件
        initViews(view);
        // 设置点击事件监听
        setClickListeners();
        // 显示用户信息
        displayUserInfo();

        return view;
    }

    /**
     * 初始化界面控件
     */
    private void initViews(View view) {
        tvCompany = view.findViewById(R.id.tvCompany);
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    /**
     * 设置点击事件监听
     */
    private void setClickListeners() {
        // 退出登录按钮点击事件
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    /**
     * 显示用户信息
     */
    private void displayUserInfo() {
        tvCompany.setText("单位：" + sharedPrefsManager.getUserCompany());
        tvName.setText("姓名：" + sharedPrefsManager.getUserName());
        tvPhone.setText("电话：" + sharedPrefsManager.getUserPhone());
        tvEmail.setText("邮箱：" + sharedPrefsManager.getUserEmail());
    }

    /**
     * 退出登录
     */
    private void logout() {
        // 清除登录状态
        sharedPrefsManager.clearUserLoginSession();
        // 跳转到登录界面
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新用户信息显示
        displayUserInfo();
    }
}
