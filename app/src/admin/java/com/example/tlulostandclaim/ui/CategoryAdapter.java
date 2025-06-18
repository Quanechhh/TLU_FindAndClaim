package com.example.tlulostandclaim.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlulostandclaim.data.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView {

    private List<CategoryModel> list;
    private CategoryInteract interact;

    public CategoryAdapter(@NonNull Context context) {
        super(context);
    }


    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }


    public int getItemCount() {
        return list.size();
    }



    public interface CategoryInteract {
        void onDelete(CategoryModel model);
    }

    public interface ViewHolder {
    }
}
