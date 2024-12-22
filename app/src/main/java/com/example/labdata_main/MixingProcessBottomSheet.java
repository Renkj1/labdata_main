package com.example.labdata_main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class MixingProcessBottomSheet extends BottomSheetDialogFragment {
    private TextInputEditText etRatio, etTemperature, etSpeed;
    private Button btnSave;
    private ImageButton btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_mixing_process, container, false);
        initViews(view);
        setupClickListeners();
        return view;
    }

    private void initViews(View view) {
        etRatio = view.findViewById(R.id.etRatio);
        etTemperature = view.findViewById(R.id.etTemperature);
        etSpeed = view.findViewById(R.id.etSpeed);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                // 保存数据
                String ratio = etRatio.getText().toString();
                String temperature = etTemperature.getText().toString();
                String speed = etSpeed.getText().toString();

                SharedPrefsManager.saveString(requireContext(), "mixing_ratio", ratio);
                SharedPrefsManager.saveString(requireContext(), "mixing_temperature", temperature);
                SharedPrefsManager.saveString(requireContext(), "mixing_speed", speed);

                dismiss();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etRatio.getText())) {
            etRatio.setError("请输入配比");
            isValid = false;
        }

        if (TextUtils.isEmpty(etTemperature.getText())) {
            etTemperature.setError("请输入拌合温度");
            isValid = false;
        }

        if (TextUtils.isEmpty(etSpeed.getText())) {
            etSpeed.setError("请输入拌合速度");
            isValid = false;
        }

        return isValid;
    }
}
