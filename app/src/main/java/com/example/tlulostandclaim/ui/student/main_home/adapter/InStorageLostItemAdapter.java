package com.example.tlulostandclaim.ui.student.main_home.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.enum_model.EnumInStorageItemStatus;
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.databinding.LayoutLostItemBinding;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.List;

public class InStorageLostItemAdapter extends RecyclerView.Adapter<InStorageLostItemAdapter.LostItemViewHolder> {
    private List<InStorageLostItemModel> lostItemModels;

    private InStorageLostItemInteract lostItemInteract;

    public InStorageLostItemAdapter(List<InStorageLostItemModel> lostItemModels, InStorageLostItemInteract lostItemInteract) {
        this.lostItemModels = lostItemModels;
        this.lostItemInteract = lostItemInteract;
    }

    @NonNull
    @Override
    public LostItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LostItemViewHolder(LayoutLostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LostItemViewHolder holder, int position) {
        holder.bind(lostItemModels.get(position));
    }

    @Override
    public int getItemCount() {
        return lostItemModels.size();
    }

    class LostItemViewHolder extends RecyclerView.ViewHolder {
        private final LayoutLostItemBinding binding;

        public LostItemViewHolder(LayoutLostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bind(InStorageLostItemModel data) {
            String image = "";
            if (!data.getImages().isEmpty()) {
                image = data.getImages().get(0);
            }
            Glide.with(binding.getRoot().getContext()).load(image).into(binding.imageLostItem);
            binding.textLostItemCategory.setText(data.getType());
            if (data.getStatus() == EnumInStorageItemStatus.DONE.value) {
                binding.textLostItemStatus.setText("Đã nhận lại");
                binding.textLostItemStatus.setTextColor(binding.getRoot().getResources().getColor(R.color.color_green));

            } else {
                binding.textLostItemStatus.setText("Đang tìm");
                binding.textLostItemStatus.setTextColor(binding.getRoot().getResources().getColor(R.color.color_orange));
            }
            binding.textLostItemName.setText(data.getTitle());
            binding.textLostItemDate.setText(String.valueOf(GlobalFunction.formatDateTimeFromMillisecond(data.getCreatedDate())));
            binding.getRoot().setOnClickListener(v -> lostItemInteract.onChosenAction(data));
        }
    }

    public interface InStorageLostItemInteract {
        void onChosenAction(InStorageLostItemModel model);
    }
}
