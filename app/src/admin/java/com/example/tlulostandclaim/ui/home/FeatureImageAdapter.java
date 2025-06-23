package com.example.tlulostandclaim.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tlulostandclaim.databinding.LayoutAdminImageItemBinding;
import com.example.tlulostandclaim.databinding.LayoutMovieImageItemBinding;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.List;

public class FeatureImageAdapter extends RecyclerView.Adapter<FeatureImageAdapter.ViewHolder> {

    private List<Integer> imagesList;
    private AdminFeatureInteract interact;

    public FeatureImageAdapter(List<Integer> imagesList, AdminFeatureInteract interact) {
        this.imagesList = imagesList;
        this.interact = interact;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutAdminImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(imagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutAdminImageItemBinding binding;

        public ViewHolder(LayoutAdminImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int data) {
            Glide.with(binding.getRoot().getContext()).load(data).into(binding.imageItem);
            binding.getRoot().setOnClickListener(v -> interact.onChosen(data));
        }
    }

    public interface AdminFeatureInteract {
        void onChosen(int data);
    }
}
