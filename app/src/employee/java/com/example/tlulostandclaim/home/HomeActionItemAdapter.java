package com.example.tlulostandclaim.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlulostandclaim.databinding.LayoutImageActionsBinding;

import java.util.List;

public class HomeActionItemAdapter extends RecyclerView.Adapter<HomeActionItemAdapter.ViewHolder> {

    private List<Integer> list;
    private OnActionsChosen onActionsChosen;

    public HomeActionItemAdapter(List<Integer> list, OnActionsChosen onActionsChosen) {
        this.list = list;
        this.onActionsChosen = onActionsChosen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutImageActionsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        private LayoutImageActionsBinding binding;

        public ViewHolder(LayoutImageActionsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(int data) {
            binding.imageStorageManagement.setImageResource(data);
            binding.getRoot().setOnClickListener(v -> onActionsChosen.onAction(data));
        }
    }

    public interface OnActionsChosen {
        void onAction(int data);
    }
}
