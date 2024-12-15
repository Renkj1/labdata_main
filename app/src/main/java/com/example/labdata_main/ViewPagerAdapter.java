package com.example.labdata_main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.labdata_main.ExperimentFragment;
import com.example.labdata_main.MyFragment;
import com.example.labdata_main.OverviewFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    // 构造函数，传入 FragmentActivity
    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // 创建Fragment实例，根据position来区分返回不同的Fragment
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OverviewFragment();  // 概览
            case 1:
                return new ExperimentFragment();  // 实验
            case 2:
                return new MyFragment();  // 我的
            default:
                return new OverviewFragment();  // 默认返回概览Fragment
        }
    }

    // 返回ViewPager的页数（Tab数目）
    @Override
    public int getItemCount() {
        return 3;  // 你有三个Tab
    }
}
