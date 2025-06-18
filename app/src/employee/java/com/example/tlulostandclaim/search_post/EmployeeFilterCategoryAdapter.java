package com.example.tlulostandclaim.search_post;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.databinding.LayoutCategoryFilterItemBinding;

import java.util.List;

public class EmployeeFilterCategoryAdapter extends RecyclerView.Adapter<EmployeeFilterCategoryAdapter.ViewHolder> {
    private List<CategoryModel> list;
    private FilterCategoryInteract interact;

    public EmployeeFilterCategoryAdapter(List<CategoryModel> list, FilterCategoryInteract interact) {
        this.list = list;
        this.interact = interact;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutCategoryFilterItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        private LayoutCategoryFilterItemBinding binding;

        public ViewHolder(LayoutCategoryFilterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(CategoryModel model) {
            binding.textCategory.setText(model.getName());
            binding.getRoot().setOnClickListener(v -> {
                interact.onSelect(model);
            });
        }
    }

    public interface FilterCategoryInteract {
        void onSelect(CategoryModel model);
    }
}
