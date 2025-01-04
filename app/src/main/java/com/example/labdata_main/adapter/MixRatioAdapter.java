package com.example.labdata_main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labdata_main.R;
import com.example.labdata_main.model.MixRatio;
import com.google.android.material.card.MaterialCardView;

public class MixRatioAdapter extends ListAdapter<MixRatio, MixRatioAdapter.MixRatioViewHolder> {

    private final OnMixRatioSelectedListener listener;
    private int selectedPosition = -1;

    public MixRatioAdapter(OnMixRatioSelectedListener listener) {
        super(new DiffUtil.ItemCallback<MixRatio>() {
            @Override
            public boolean areItemsTheSame(@NonNull MixRatio oldItem, @NonNull MixRatio newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull MixRatio oldItem, @NonNull MixRatio newItem) {
                return oldItem.getName().equals(newItem.getName()) &&
                       oldItem.getDescription().equals(newItem.getDescription());
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public MixRatioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mix_ratio, parent, false);
        return new MixRatioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MixRatioViewHolder holder, int position) {
        MixRatio mixRatio = getItem(position);
        holder.bind(mixRatio, position == selectedPosition);
        
        holder.cardView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onMixRatioSelected(mixRatio);
        });
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public MixRatio getSelectedMixRatio() {
        return selectedPosition != -1 ? getItem(selectedPosition) : null;
    }

    static class MixRatioViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardView;
        private final TextView tvName;
        private final TextView tvDescription;

        MixRatioViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            tvName = itemView.findViewById(R.id.tvMixName);
            tvDescription = itemView.findViewById(R.id.tvMixDescription);
        }

        void bind(MixRatio mixRatio, boolean isSelected) {
            cardView.setChecked(isSelected);
            tvName.setText(mixRatio.getName());
            tvDescription.setText(mixRatio.getDescription());
        }
    }

    public interface OnMixRatioSelectedListener {
        void onMixRatioSelected(MixRatio mixRatio);
    }
}
