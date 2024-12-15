package com.example.labdata_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class OverviewFragment extends Fragment {
    private TextView mixText;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.overview, container, false);

        // 获取TextView和Spinner
        mixText = view.findViewById(R.id.mix_text);
        spinner = view.findViewById(R.id.experiment_spinner);

        // 创建下拉菜单选项
        String[] items = new String[]{"请选择实验类型", "混合料实验", "沥青试验"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                items
        );
        spinner.setAdapter(adapter);

        // 设置选项选择监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // 显示默认文本
                    mixText.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    return;
                }
                
                if (position == 1) {
                    // 显示混合料实验文本，并清除沥青试验Fragment
                    mixText.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                } else if (position == 2) {
                    // 隐藏文本并显示沥青试验Fragment
                    mixText.setVisibility(View.GONE);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new AsphaltFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment恢复时，确保TextView可见并重置Spinner
        if (mixText != null) {
            mixText.setVisibility(View.VISIBLE);
        }
        if (spinner != null) {
            spinner.setSelection(1); // 设置为"混合料实验"
        }
    }
}
