package com.example.labdata_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CompactionProcessBottomSheet extends BottomSheetDialogFragment {
    private RadioGroup radioGroup;
    private Button btnSave;
    private ImageButton btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_compaction_process, container, false);
        initViews(view);
        setupClickListeners();
        return view;
    }

    private void initViews(View view) {
        radioGroup = view.findViewById(R.id.radioGroup);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> dismiss());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            btnSave.setEnabled(true);
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String method = "";
            if (selectedId == R.id.rbMarshall) {
                method = "马歇尔击实法";
            } else if (selectedId == R.id.rbRotary) {
                method = "旋转压实法";
            } else if (selectedId == R.id.rbShear) {
                method = "剪切压实法";
            } else if (selectedId == R.id.rbStatic) {
                method = "静压法";
            }

            SharedPrefsManager.saveString(requireContext(), "compaction_method", method);
            dismiss();
        });
    }
}
