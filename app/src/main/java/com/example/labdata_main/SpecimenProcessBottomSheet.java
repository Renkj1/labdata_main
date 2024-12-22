package com.example.labdata_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SpecimenProcessBottomSheet extends BottomSheetDialogFragment {
    private TextView tvProcessTitle1, tvProcessTitle2;
    private LinearLayout layoutProcessContent1, layoutProcessContent2;
    private TextView btnMixing, btnCompaction;
    private TextView btnRectangle, btnCylinder, btnSemiCircle;
    private Button btnNext;
    private ImageButton btnClose;
    private boolean isFirstProcess = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_specimen_process, container, false);
        initViews(view);
        setupClickListeners();
        return view;
    }

    private void initViews(View view) {
        tvProcessTitle1 = view.findViewById(R.id.tvProcessTitle1);
        tvProcessTitle2 = view.findViewById(R.id.tvProcessTitle2);
        layoutProcessContent1 = view.findViewById(R.id.layoutProcessContent1);
        layoutProcessContent2 = view.findViewById(R.id.layoutProcessContent2);
        btnMixing = view.findViewById(R.id.btnMixing);
        btnCompaction = view.findViewById(R.id.btnCompaction);
        btnRectangle = view.findViewById(R.id.btnRectangle);
        btnCylinder = view.findViewById(R.id.btnCylinder);
        btnSemiCircle = view.findViewById(R.id.btnSemiCircle);
        btnNext = view.findViewById(R.id.btnNext);
        btnClose = view.findViewById(R.id.btnClose);
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> dismiss());

        btnMixing.setOnClickListener(v -> {
            btnMixing.setSelected(true);
            btnCompaction.setSelected(false);
            // 显示拌合流程对话框
            MixingProcessBottomSheet mixingSheet = new MixingProcessBottomSheet();
            mixingSheet.show(getParentFragmentManager(), "MixingProcess");
            btnNext.setEnabled(true);
        });

        btnCompaction.setOnClickListener(v -> {
            btnMixing.setSelected(false);
            btnCompaction.setSelected(true);
            // 显示击实流程对话框
            CompactionProcessBottomSheet compactionSheet = new CompactionProcessBottomSheet();
            compactionSheet.show(getParentFragmentManager(), "CompactionProcess");
            btnNext.setEnabled(true);
        });

        btnNext.setOnClickListener(v -> {
            // 直接跳转到切割属性界面
            isFirstProcess = false;
            tvProcessTitle1.setTextColor(getResources().getColor(R.color.gray));
            tvProcessTitle2.setTextColor(getResources().getColor(R.color.blue));
            layoutProcessContent1.setVisibility(View.GONE);
            layoutProcessContent2.setVisibility(View.VISIBLE);
            btnNext.setEnabled(false);
        });

        // 切割属性按钮点击事件
        View.OnClickListener shapeClickListener = v -> {
            btnRectangle.setSelected(v == btnRectangle);
            btnCylinder.setSelected(v == btnCylinder);
            btnSemiCircle.setSelected(v == btnSemiCircle);
            btnNext.setEnabled(true);
            
            if (getActivity() instanceof ManufacturingMethodActivity) {
                ((ManufacturingMethodActivity) getActivity()).enableNextButton();
                dismiss();
            }
        };

        btnRectangle.setOnClickListener(shapeClickListener);
        btnCylinder.setOnClickListener(shapeClickListener);
        btnSemiCircle.setOnClickListener(shapeClickListener);
    }
}
