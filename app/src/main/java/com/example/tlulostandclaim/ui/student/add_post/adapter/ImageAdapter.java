package com.example.tlulostandclaim.ui.student.add_post.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tlulostandclaim.databinding.LayoutMovieImageItemBinding;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Object> imagesList;

    public ImageAdapter(List<Object> imagesList) {
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutMovieImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        private LayoutMovieImageItemBinding binding;

        public ViewHolder(LayoutMovieImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Object data) {
            int width = (int) Math.round((GlobalFunction.getWidthScreen(binding.getRoot().getContext()) * 0.5));
            binding.imageItem.setLayoutParams(new ViewGroup.LayoutParams(width, width));
            Glide.with(binding.getRoot().getContext()).load(data).into(binding.imageItem);
        }
    }
}
