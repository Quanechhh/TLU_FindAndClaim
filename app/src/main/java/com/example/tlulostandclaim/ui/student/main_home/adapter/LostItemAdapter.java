package com.example.tlulostandclaim.ui.student.main_home.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.enum_model.EnumPostStatus;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.LayoutLostItemBinding;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.List;

public class LostItemAdapter extends RecyclerView.Adapter<LostItemAdapter.LostItemViewHolder> {
    private List<LostItemModel> lostItemModels;

    private LostItemInteract lostItemInteract;

    public LostItemAdapter(List<LostItemModel> lostItemModels, LostItemInteract lostItemInteract) {
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
        void bind(LostItemModel data) {
            String image = "";
            if (!data.getImage().isEmpty()) {
                image = data.getImage().get(0);
            }
            Glide.with(binding.getRoot().getContext()).load(image).into(binding.imageLostItem);
            binding.textLostItemCategory.setText(data.getCategory());
            if (data.getStatus() == EnumPostStatus.DONE.value) {
                binding.textLostItemStatus.setText("Đã xử lý");
                binding.textLostItemStatus.setTextColor(binding.getRoot().getResources().getColor(R.color.color_red));

            } else if (data.getStatus() == EnumPostStatus.IN_STORAGE.value) {
                binding.textLostItemStatus.setText("Đang có sẵn ở kho");
                binding.textLostItemStatus.setTextColor(binding.getRoot().getResources().getColor(R.color.color_green));
            } else {
                binding.textLostItemStatus.setText("Đang tìm");
                binding.textLostItemStatus.setTextColor(binding.getRoot().getResources().getColor(R.color.color_orange));
            }
            binding.textLostItemName.setText(data.getName());
            binding.textLostItemDate.setText(String.valueOf(GlobalFunction.formatDateTimeFromMillisecond(data.getCreatedAt())));
            binding.getRoot().setOnClickListener(v -> lostItemInteract.onChosenAction(data));
        }
    }

    public interface LostItemInteract {
        void onChosenAction(LostItemModel model);
    }
}
