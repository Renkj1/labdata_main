package com.example.labdata_main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.adapter.ProjectSelectionAdapter;
import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.model.Project;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class SelectProjectFragment extends Fragment implements 
        ProjectSelectionAdapter.OnProjectSelectedListener,
        AddProjectBottomSheet.OnProjectAddedListener {
    
    private RecyclerView rvProjects;
    private FloatingActionButton fabAddProject;
    private ProjectSelectionAdapter adapter;
    private DatabaseHelper databaseHelper;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_project, container, false);

        rvProjects = view.findViewById(R.id.rvProjects);
        fabAddProject = view.findViewById(R.id.fabAddProject);

        setupRecyclerView();
        setupClickListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProjects();
    }

    private void setupRecyclerView() {
        adapter = new ProjectSelectionAdapter(this);
        rvProjects.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvProjects.setAdapter(adapter);
    }

    private void setupClickListeners() {
        fabAddProject.setOnClickListener(v -> {
            AddProjectBottomSheet bottomSheet = AddProjectBottomSheet.newInstance();
            bottomSheet.setOnProjectAddedListener(this);
            bottomSheet.show(getChildFragmentManager(), "AddProjectBottomSheet");
        });
    }

    private void loadProjects() {
        new Thread(() -> {
            List<Project> projects = databaseHelper.getAllProjects();
            if (isAdded()) {
                mainHandler.post(() -> adapter.submitList(new ArrayList<>(projects)));
            }
        }).start();
    }

    @Override
    public void onProjectSelected(Project project) {
        if (getActivity() instanceof ExperimentTaskSetupActivity) {
            ExperimentTaskSetupActivity activity = (ExperimentTaskSetupActivity) getActivity();
            activity.setSelectedProject(project);
            activity.enableNextButton(true);
        }
    }

    @Override
    public void onProjectAdded(Project project) {
        loadProjects();
    }
}
