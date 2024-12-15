package com.example.labdata_main;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 TabLayout 和 ViewPager2
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // 创建适配器并设置给 ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // 动态调整 ViewPager 的 marginTop，使其紧跟 TabLayout 下面
        tabLayout.post(() -> {
            int tabLayoutHeight = tabLayout.getHeight(); // 获取 TabLayout 的高度（像素值）

            // 修改 ViewPager2 的 marginTop
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
            layoutParams.topMargin = tabLayoutHeight; // 设置 marginTop 为 TabLayout 的高度
            viewPager.setLayoutParams(layoutParams);  // 应用新的布局参数
        });

        // 使用 TabLayoutMediator 来连接 TabLayout 和 ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // 为每个 Tab 设置名称
            switch (position) {
                case 0:
                    tab.setText("概览");
                    break;
                case 1:
                    tab.setText("实验");
                    break;
                case 2:
                    tab.setText("我的");
                    break;
            }
        }).attach();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            moveTaskToBack(true);
            return;
        } else {
            backToast = Toast.makeText(this, "再按一次返回键最小化应用", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}