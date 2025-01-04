package com.example.labdata_main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.adapter.MaterialInputAdapter;
import com.example.labdata_main.adapter.MixRatioAdapter;
import com.example.labdata_main.database.DatabaseHelper;
import com.example.labdata_main.model.MaterialItem;
import com.example.labdata_main.model.MixRatio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectMixRatioFragment extends Fragment implements MixRatioAdapter.OnMixRatioSelectedListener {
    private RadioGroup rgMixType;
    private View layoutExistingMix;
    private View layoutNewMix;
    private RecyclerView rvMixRatios;
    private MixRatioAdapter mixRatioAdapter;
    private TextInputEditText etMixName;
    private TextInputEditText etMixDescription;
    private RecyclerView rvMaterials;
    private MaterialInputAdapter materialInputAdapter;
    private FloatingActionButton fabAddMaterial;
    
    private DatabaseHelper databaseHelper;
    private ExecutorService executorService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        executorService = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_mix_ratio, container, false);
        
        initViews(view);
        setupListeners();
        loadMixRatios();
        
        return view;
    }

    private void initViews(View view) {
        rgMixType = view.findViewById(R.id.rgMixType);
        layoutExistingMix = view.findViewById(R.id.layoutExistingMix);
        layoutNewMix = view.findViewById(R.id.layoutNewMix);
        
        // 初始化现有配比列表
        rvMixRatios = view.findViewById(R.id.rvMixRatios);
        rvMixRatios.setLayoutManager(new LinearLayoutManager(requireContext()));
        mixRatioAdapter = new MixRatioAdapter(this);
        rvMixRatios.setAdapter(mixRatioAdapter);
        
        // 初始化新配比输入
        etMixName = view.findViewById(R.id.etMixName);
        etMixDescription = view.findViewById(R.id.etMixDescription);
        rvMaterials = view.findViewById(R.id.rvMaterials);
        rvMaterials.setLayoutManager(new LinearLayoutManager(requireContext()));
        materialInputAdapter = new MaterialInputAdapter();
        rvMaterials.setAdapter(materialInputAdapter);
        fabAddMaterial = view.findViewById(R.id.fabAddMaterial);
    }

    private void setupListeners() {
        rgMixType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbExistingMix) {
                layoutExistingMix.setVisibility(View.VISIBLE);
                layoutNewMix.setVisibility(View.GONE);
            } else {
                layoutExistingMix.setVisibility(View.GONE);
                layoutNewMix.setVisibility(View.VISIBLE);
            }
            checkInputValidity();
        });
        
        fabAddMaterial.setOnClickListener(v -> materialInputAdapter.addMaterial());
    }

    private void loadMixRatios() {
        executorService.execute(() -> {
            List<MixRatio> mixRatios = databaseHelper.mixRatioDao().getAllMixRatios();
            requireActivity().runOnUiThread(() -> {
                mixRatioAdapter.submitList(mixRatios);
                checkInputValidity();
            });
        });
    }

    private void checkInputValidity() {
        boolean isValid = false;
        
        if (rgMixType.getCheckedRadioButtonId() == R.id.rbExistingMix) {
            // 检查是否选择了现有配比
            isValid = mixRatioAdapter.getSelectedPosition() != -1;
        } else {
            // 检查新配比输入
            isValid = !TextUtils.isEmpty(etMixName.getText()) &&
                     materialInputAdapter.getMaterials().size() > 0;
        }
        
        // 通知Activity更新下一步按钮状态
        ((ExperimentTaskSetupActivity) requireActivity()).enableNextButton(isValid);
    }

    @Override
    public void onMixRatioSelected(MixRatio mixRatio) {
        checkInputValidity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    public MixRatio getMixRatio() {
        if (rgMixType.getCheckedRadioButtonId() == R.id.rbExistingMix) {
            return mixRatioAdapter.getSelectedMixRatio();
        } else {
            MixRatio mixRatio = new MixRatio();
            mixRatio.setName(etMixName.getText().toString());
            mixRatio.setDescription(etMixDescription.getText().toString());
            mixRatio.setMaterials(new ArrayList<>(materialInputAdapter.getMaterials()));
            return mixRatio;
        }
    }
}
