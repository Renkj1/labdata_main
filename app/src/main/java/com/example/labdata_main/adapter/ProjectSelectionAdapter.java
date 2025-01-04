package com.example.labdata_main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.R;
import com.example.labdata_main.model.Project;

public class ProjectSelectionAdapter extends ListAdapter<Project, ProjectSelectionAdapter.ProjectViewHolder> {
    private int selectedPosition = -1;
    private final OnProjectSelectedListener listener;

    public ProjectSelectionAdapter(OnProjectSelectedListener listener) {
        super(new ProjectDiffCallback());
        this.listener = listener;
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
        Project project = getItem(position);
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

    private static class ProjectDiffCallback extends DiffUtil.ItemCallback<Project> {
        @Override
        public boolean areItemsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
            return oldItem.getName().equals(newItem.getName()) 
                && oldItem.getDeadline().equals(newItem.getDeadline());
        }
    }
}
