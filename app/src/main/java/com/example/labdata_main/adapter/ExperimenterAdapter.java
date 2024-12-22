package com.example.labdata_main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labdata_main.R;
import com.example.labdata_main.model.Experimenter;

import java.util.List;

public class ExperimenterAdapter extends RecyclerView.Adapter<ExperimenterAdapter.ViewHolder> {
    private final List<Experimenter> experimenters;
    private final OnPermissionClickListener permissionListener;
    private final OnExperimenterSelectListener selectListener;

    public interface OnPermissionClickListener {
        void onPermissionClick(Experimenter experimenter);
    }

    public interface OnExperimenterSelectListener {
        void onExperimenterSelect(Experimenter experimenter, boolean isSelected);
    }

    public ExperimenterAdapter(List<Experimenter> experimenters, 
                             OnPermissionClickListener permissionListener,
                             OnExperimenterSelectListener selectListener) {
        this.experimenters = experimenters;
        this.permissionListener = permissionListener;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experimenter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Experimenter experimenter = experimenters.get(position);
        holder.tvExperimenterName.setText(experimenter.getName());
        holder.switchSelect.setChecked(experimenter.isSelected());
        
        holder.switchSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            experimenter.setSelected(isChecked);
            selectListener.onExperimenterSelect(experimenter, isChecked);
        });
        
        holder.btnManagePermissions.setOnClickListener(v -> 
            permissionListener.onPermissionClick(experimenter));
    }

    @Override
    public int getItemCount() {
        return experimenters.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExperimenterName;
        Button btnManagePermissions;
        SwitchCompat switchSelect;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExperimenterName = itemView.findViewById(R.id.tvExperimenterName);
            btnManagePermissions = itemView.findViewById(R.id.btnManagePermissions);
            switchSelect = itemView.findViewById(R.id.switchSelect);
        }
    }
}
