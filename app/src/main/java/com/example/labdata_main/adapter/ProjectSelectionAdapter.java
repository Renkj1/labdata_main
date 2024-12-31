package com.example.labdata_main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.R;
import com.example.labdata_main.model.Project;
import java.util.ArrayList;
import java.util.List;

public class ProjectSelectionAdapter extends RecyclerView.Adapter<ProjectSelectionAdapter.ProjectViewHolder> {
    private List<Project> projects;
    private int selectedPosition = -1;
    private final OnProjectSelectedListener listener;

    public ProjectSelectionAdapter(OnProjectSelectedListener listener) {
        this.projects = new ArrayList<>();
        this.listener = listener;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project_selection, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.tvProjectName.setText(project.getName());
        holder.tvDeadline.setText("截止日期：" + project.getDeadline());
        
        // 设置单选按钮状态
        holder.rbSelect.setChecked(position == selectedPosition);
        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onProjectSelected(project);
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName;
        TextView tvDeadline;
        RadioButton rbSelect;

        ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvProjectName);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            rbSelect = itemView.findViewById(R.id.rbSelect);
        }
    }

    public interface OnProjectSelectedListener {
        void onProjectSelected(Project project);
    }
}
