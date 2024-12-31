package com.example.labdata_main.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.R;
import com.example.labdata_main.model.MixRatio;
import com.example.labdata_main.model.MaterialItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class MixRatioSelectionAdapter extends RecyclerView.Adapter<MixRatioSelectionAdapter.MixRatioViewHolder> {
    private static final String TAG = "MixRatioSelectionAdapter";
    private List<MixRatio> mixRatios;
    private int selectedPosition = -1;
    private final OnMixRatioSelectedListener listener;
    private final Context context;

    public MixRatioSelectionAdapter(Context context, OnMixRatioSelectedListener listener) {
        this.context = context;
        this.mixRatios = new ArrayList<>();
        this.listener = listener;
    }

    public void setMixRatios(List<MixRatio> mixRatios) {
        Log.d(TAG, "Setting mix ratios: " + (mixRatios != null ? mixRatios.size() : 0));
        this.mixRatios = mixRatios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MixRatioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mix_ratio_selection, parent, false);
        return new MixRatioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MixRatioViewHolder holder, int position) {
        MixRatio mixRatio = mixRatios.get(position);
        Log.d(TAG, "Binding mix ratio at position " + position + ": " + mixRatio.getName());
        
        holder.tvMixRatioName.setText(mixRatio.getName());
        
        // 设置配比详情
        List<MaterialItem> materials = mixRatio.getMaterials();
        if (materials != null && !materials.isEmpty()) {
            Log.d(TAG, "Mix ratio has " + materials.size() + " materials");
            StringBuilder details = new StringBuilder("材料配比：");
            for (MaterialItem material : materials) {
                if (details.length() > 7) {  // "材料配比：" 的长度
                    details.append(" | ");
                }
                details.append(material.getName())
                      .append(" ")
                      .append(material.getPercentage())
                      .append("%");
            }
            holder.tvMixRatioDetails.setText(details.toString());
        } else {
            Log.d(TAG, "Mix ratio has no materials");
            holder.tvMixRatioDetails.setText("暂无材料配比信息");
        }

        // 设置单选按钮状态
        holder.rbSelect.setChecked(position == selectedPosition);

        // 添加材料标签
        holder.chipGroup.removeAllViews();
        addMaterialChips(holder.chipGroup, materials);

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "Mix ratio clicked at position: " + position);
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onMixRatioSelected(mixRatio);
        });
    }

    @Override
    public int getItemCount() {
        return mixRatios.size();
    }

    private void addMaterialChips(ChipGroup chipGroup, List<MaterialItem> materials) {
        if (materials != null) {
            for (MaterialItem material : materials) {
                String chipText = String.format("%s %.1f%%", 
                    material.getName(), 
                    material.getPercentage());
                
                // 如果有材料类型，添加到显示中
                if (material.getType() != null && !material.getType().isEmpty()) {
                    chipText = String.format("%s (%s)", chipText, material.getType());
                }
                
                addChip(chipGroup, chipText);
            }
        }
    }

    private void addChip(ChipGroup chipGroup, String text) {
        Chip chip = new Chip(context);
        chip.setText(text);
        chip.setClickable(false);
        chip.setCheckable(false);
        chipGroup.addView(chip);
    }

    static class MixRatioViewHolder extends RecyclerView.ViewHolder {
        TextView tvMixRatioName;
        TextView tvMixRatioDetails;
        RadioButton rbSelect;
        ChipGroup chipGroup;

        MixRatioViewHolder(View itemView) {
            super(itemView);
            tvMixRatioName = itemView.findViewById(R.id.tvMixRatioName);
            tvMixRatioDetails = itemView.findViewById(R.id.tvMixRatioDetails);
            rbSelect = itemView.findViewById(R.id.rbSelect);
            chipGroup = itemView.findViewById(R.id.chipGroup);
        }
    }

    public interface OnMixRatioSelectedListener {
        void onMixRatioSelected(MixRatio mixRatio);
    }
}
