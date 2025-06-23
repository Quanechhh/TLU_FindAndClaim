package com.example.tlulostandclaim.ui.category;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.databinding.LayoutAdminCategoryItemBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> list;
    private CategoryInteract interact;

    public CategoryAdapter(List<CategoryModel> list, CategoryInteract interact) {
        this.list = list;
        this.interact = interact;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutAdminCategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        private LayoutAdminCategoryItemBinding binding;

        public ViewHolder(LayoutAdminCategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(CategoryModel model) {
            binding.textName.setText(model.getName());
            binding.buttonDelete.setOnClickListener(v -> {
                interact.onDelete(model);
            });
        }
    }

    public interface CategoryInteract {
        void onDelete(CategoryModel model);
    }
}
