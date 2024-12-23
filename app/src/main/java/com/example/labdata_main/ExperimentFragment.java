package com.example.labdata_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.labdata_main.db.DatabaseHelper;
import com.example.labdata_main.utils.SharedPrefsManager;

public class ExperimentFragment extends Fragment {
    private SharedPrefsManager sharedPrefsManager;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiment, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        sharedPrefsManager = new SharedPrefsManager(requireContext());
        databaseHelper = new DatabaseHelper(requireContext());
    }
}