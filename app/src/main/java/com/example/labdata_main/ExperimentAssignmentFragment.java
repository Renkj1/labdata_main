package com.example.labdata_main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.labdata_main.model.ExperimentAssignment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class ExperimentAssignmentFragment extends Fragment {
    private ChipGroup chipGroupExperiments;
    private ChipGroup chipGroupAges;
    private TextInputEditText etNotes;
    
    private Chip chipCompression;
    private Chip chipFlexural;
    private Chip chipSplitting;
    private Chip chipElastic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiment_assignment, container, false);
        
        initViews(view);
        setupListeners();
        
        return view;
    }

    private void initViews(View view) {
        chipGroupExperiments = view.findViewById(R.id.chipGroupExperiments);
        chipGroupAges = view.findViewById(R.id.chipGroupAges);
        etNotes = view.findViewById(R.id.etNotes);
        
        chipCompression = view.findViewById(R.id.chipCompression);
        chipFlexural = view.findViewById(R.id.chipFlexural);
        chipSplitting = view.findViewById(R.id.chipSplitting);
        chipElastic = view.findViewById(R.id.chipElastic);
    }

    private void setupListeners() {
        chipGroupExperiments.setOnCheckedStateChangeListener((group, checkedIds) -> {
            checkInputValidity();
        });

        chipGroupAges.setOnCheckedStateChangeListener((group, checkedIds) -> {
            checkInputValidity();
        });
    }

    private void checkInputValidity() {
        boolean isValid = chipGroupExperiments.getCheckedChipIds().size() > 0 &&
                         chipGroupAges.getCheckedChipIds().size() > 0;
        
        // 通知Activity更新下一步按钮状态
        ((ExperimentTaskSetupActivity) requireActivity()).enableNextButton(isValid);
    }

    public ExperimentAssignment getExperimentAssignment() {
        ExperimentAssignment assignment = new ExperimentAssignment();
        
        // 获取选中的实验类型
        if (chipCompression.isChecked()) {
            assignment.addExperimentType(ExperimentAssignment.EXPERIMENT_COMPRESSION);
        }
        if (chipFlexural.isChecked()) {
            assignment.addExperimentType(ExperimentAssignment.EXPERIMENT_FLEXURAL);
        }
        if (chipSplitting.isChecked()) {
            assignment.addExperimentType(ExperimentAssignment.EXPERIMENT_SPLITTING);
        }
        if (chipElastic.isChecked()) {
            assignment.addExperimentType(ExperimentAssignment.EXPERIMENT_ELASTIC);
        }
        
        // 获取养护龄期
        Chip selectedAgeChip = chipGroupAges.findViewById(chipGroupAges.getCheckedChipId());
        if (selectedAgeChip != null) {
            String age = selectedAgeChip.getText().toString();
            assignment.setCuringAge(Integer.parseInt(age.replace("d", "")));
        }
        
        // 获取备注说明
        String notes = etNotes.getText().toString();
        if (!TextUtils.isEmpty(notes)) {
            assignment.setNotes(notes);
        }
        
        return assignment;
    }
}
