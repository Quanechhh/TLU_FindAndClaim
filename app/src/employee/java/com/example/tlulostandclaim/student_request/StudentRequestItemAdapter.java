package com.example.tlulostandclaim.student_request;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.model.RequestLostItemModel;
import com.example.tlulostandclaim.databinding.LayoutRequestItemBinding;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.List;

public class StudentRequestItemAdapter extends RecyclerView.Adapter<StudentRequestItemAdapter.ViewHolder> {

    private List<RequestLostItemModel> list;
    private StudentRequestInteract requestInteract;

    public StudentRequestItemAdapter(List<RequestLostItemModel> list, StudentRequestInteract requestInteract) {
        this.list = list;
        this.requestInteract = requestInteract;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutRequestItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutRequestItemBinding binding;

        public ViewHolder(LayoutRequestItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(RequestLostItemModel model) {
            Glide.with(binding.getRoot()).load(model.getItemImage().get(0)).into(binding.imageLostItem);
            binding.textLostItemName.setText(model.getItemName());
            binding.textUserName.setText(model.getUserName());
            binding.textUserPhone.setText(model.getUserPhone());
            binding.textLostItemDate.setText(GlobalFunction.formatDateTimeFromMillisecond(model.getCreatedAt()));
            if (model.getStatus() == 2) {
                binding.textLostItemStatus.setText("Đã từ chối");
                binding.textLostItemStatus.setTextColor(binding.getRoot().getResources().getColor(R.color.color_red));
            } else if (model.getStatus() == 1) {
                binding.textLostItemStatus.setTextColor(binding.getRoot().getResources().getColor(R.color.color_green));
                binding.textLostItemStatus.setText("Đã xử lý");
            } else {
                binding.textLostItemStatus.setText(" Đang chờ xử lý");
                binding.textLostItemStatus.setTextColor(binding.getRoot().getResources().getColor(R.color.color_orange));
            }
            binding.getRoot().setOnClickListener(v -> requestInteract.onChosen(model));
        }
    }

    public interface StudentRequestInteract {
        void onChosen(RequestLostItemModel item);
    }
}
