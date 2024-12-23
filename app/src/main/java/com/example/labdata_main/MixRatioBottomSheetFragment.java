package com.example.labdata_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class MixRatioBottomSheetFragment extends BottomSheetDialogFragment {
    private TextInputEditText mixRatioNameInput;
    private Button nextStepButton;

    public static MixRatioBottomSheetFragment newInstance() {
        return new MixRatioBottomSheetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_mix_ratio, container, false);
        
        mixRatioNameInput = view.findViewById(R.id.mix_ratio_name_input);
        nextStepButton = view.findViewById(R.id.next_step_button);

        nextStepButton.setOnClickListener(v -> {
            String mixRatioName = mixRatioNameInput.getText().toString();
            if (!mixRatioName.isEmpty()) {
                Intent intent = new Intent(getContext(), MixRatioEditActivity.class);
                intent.putExtra("mix_ratio_name", mixRatioName);
                startActivity(intent);
                dismiss();
            }
        });

        return view;
    }
}
