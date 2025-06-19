package com.example.tlulostandclaim.student_request;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlulostandclaim.data.model.RequestLostItemModel;
import com.example.tlulostandclaim.databinding.LayoutRequestItemBinding;

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
            binding.textLostItemName.setText(model.getItemName());
            binding.textUserName.setText(model.getUserName());
            binding.textUserPhone.setText(model.getUserPhone());
            binding.textLostItemDate.setText(String.valueOf(model.getCreatedAt())); // placeholder nếu chưa có GlobalFunction
        }
    }

    public interface StudentRequestInteract {
        void onChosen(RequestLostItemModel item);
    }
}
