package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

public class ExperimentTaskBottomSheet extends BottomSheetDialogFragment {

    public static ExperimentTaskBottomSheet newInstance() {
        return new ExperimentTaskBottomSheet();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_experiment_task, container, false);

        MaterialCardView addProjectCard = view.findViewById(R.id.add_project_card);
        MaterialCardView addTaskCard = view.findViewById(R.id.add_task_card);

        addProjectCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddProjectActivity.class);
            startActivity(intent);
            dismiss();
        });

        addTaskCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SelectProjectActivity.class);
            startActivity(intent);
            dismiss();
        });

        return view;
    }
}
