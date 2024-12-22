package com.example.labdata_main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labdata_main.R;
import com.example.labdata_main.model.Project;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    private final List<Project> projects;
    private final OnProjectAccessChangeListener listener;

    public interface OnProjectAccessChangeListener {
        void onProjectAccessChange(Project project, boolean isAccessible);
    }

    public ProjectAdapter(List<Project> projects, OnProjectAccessChangeListener listener) {
        this.projects = projects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.tvProjectName.setText(project.getName());
        holder.switchAccess.setChecked(project.isAccessible());
        
        // 设置开关状态改变监听器
        holder.switchAccess.setOnCheckedChangeListener((buttonView, isChecked) -> {
            project.setAccessible(isChecked);
            listener.onProjectAccessChange(project, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName;
        SwitchCompat switchAccess;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvProjectName);
            switchAccess = itemView.findViewById(R.id.switchAccess);
        }
    }
}
