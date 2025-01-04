package com.example.labdata_main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.labdata_main.model.MoldingMethod;
import com.google.android.material.textfield.TextInputEditText;

public class SelectMoldingMethodFragment extends Fragment {
    private RadioGroup rgMoldingType;
    private LinearLayout layoutCubeDimensions;
    private LinearLayout layoutCylinderDimensions;
    private LinearLayout layoutPrismDimensions;
    
    // 立方体尺寸
    private TextInputEditText etCubeSize;
    
    // 圆柱体尺寸
    private TextInputEditText etCylinderDiameter;
    private TextInputEditText etCylinderHeight;
    
    // 棱柱体尺寸
    private TextInputEditText etPrismLength;
    private TextInputEditText etPrismWidth;
    private TextInputEditText etPrismHeight;
    
    // 试块数量
    private TextInputEditText etSpecimenCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_molding_method, container, false);
        
        initViews(view);
        setupListeners();
        
        return view;
    }

    private void initViews(View view) {
        rgMoldingType = view.findViewById(R.id.rgMoldingType);
        layoutCubeDimensions = view.findViewById(R.id.layoutCubeDimensions);
        layoutCylinderDimensions = view.findViewById(R.id.layoutCylinderDimensions);
        layoutPrismDimensions = view.findViewById(R.id.layoutPrismDimensions);
        
        etCubeSize = view.findViewById(R.id.etCubeSize);
        
        etCylinderDiameter = view.findViewById(R.id.etCylinderDiameter);
        etCylinderHeight = view.findViewById(R.id.etCylinderHeight);
        
        etPrismLength = view.findViewById(R.id.etPrismLength);
        etPrismWidth = view.findViewById(R.id.etPrismWidth);
        etPrismHeight = view.findViewById(R.id.etPrismHeight);
        
        etSpecimenCount = view.findViewById(R.id.etSpecimenCount);
    }

    private void setupListeners() {
        rgMoldingType.setOnCheckedChangeListener((group, checkedId) -> {
            layoutCubeDimensions.setVisibility(View.GONE);
            layoutCylinderDimensions.setVisibility(View.GONE);
            layoutPrismDimensions.setVisibility(View.GONE);
            
            if (checkedId == R.id.rbCube) {
                layoutCubeDimensions.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.rbCylinder) {
                layoutCylinderDimensions.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.rbPrism) {
                layoutPrismDimensions.setVisibility(View.VISIBLE);
            }
            
            checkInputValidity();
        });

        // 为所有输入框添加文本变化监听
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkInputValidity();
            }
        };
        
        etCubeSize.addTextChangedListener(watcher);
        etCylinderDiameter.addTextChangedListener(watcher);
        etCylinderHeight.addTextChangedListener(watcher);
        etPrismLength.addTextChangedListener(watcher);
        etPrismWidth.addTextChangedListener(watcher);
        etPrismHeight.addTextChangedListener(watcher);
        etSpecimenCount.addTextChangedListener(watcher);
    }

    private void checkInputValidity() {
        boolean isValid = false;
        
        // 检查是否选择了制件类型
        int checkedId = rgMoldingType.getCheckedRadioButtonId();
        if (checkedId != -1) {
            // 检查尺寸输入
            if (checkedId == R.id.rbCube) {
                isValid = !TextUtils.isEmpty(etCubeSize.getText());
            } else if (checkedId == R.id.rbCylinder) {
                isValid = !TextUtils.isEmpty(etCylinderDiameter.getText()) &&
                         !TextUtils.isEmpty(etCylinderHeight.getText());
            } else if (checkedId == R.id.rbPrism) {
                isValid = !TextUtils.isEmpty(etPrismLength.getText()) &&
                         !TextUtils.isEmpty(etPrismWidth.getText()) &&
                         !TextUtils.isEmpty(etPrismHeight.getText());
            }
            
            // 检查数量输入
            isValid = isValid && !TextUtils.isEmpty(etSpecimenCount.getText());
        }
        
        // 通知Activity更新下一步按钮状态
        ((ExperimentTaskSetupActivity) requireActivity()).enableNextButton(isValid);
    }

    public MoldingMethod getMoldingMethod() {
        MoldingMethod method = new MoldingMethod();
        
        int checkedId = rgMoldingType.getCheckedRadioButtonId();
        if (checkedId == R.id.rbCube) {
            method.setType(MoldingMethod.TYPE_CUBE);
            method.setSize(Float.parseFloat(etCubeSize.getText().toString()));
        } else if (checkedId == R.id.rbCylinder) {
            method.setType(MoldingMethod.TYPE_CYLINDER);
            method.setDiameter(Float.parseFloat(etCylinderDiameter.getText().toString()));
            method.setHeight(Float.parseFloat(etCylinderHeight.getText().toString()));
        } else if (checkedId == R.id.rbPrism) {
            method.setType(MoldingMethod.TYPE_PRISM);
            method.setLength(Float.parseFloat(etPrismLength.getText().toString()));
            method.setWidth(Float.parseFloat(etPrismWidth.getText().toString()));
            method.setHeight(Float.parseFloat(etPrismHeight.getText().toString()));
        }
        
        method.setCount(Integer.parseInt(etSpecimenCount.getText().toString()));
        
        return method;
    }
}
