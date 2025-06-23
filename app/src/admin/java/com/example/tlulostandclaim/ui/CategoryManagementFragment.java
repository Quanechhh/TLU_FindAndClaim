package com.example.tlulostandclaim.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.databinding.FragmentAdminUserManagementBinding;
import com.example.tlulostandclaim.databinding.FragmentCategoryManagementBinding;
import com.example.tlulostandclaim.ui.user.AdminUserManagementAdapter;
import com.example.tlulostandclaim.ui.user.AdminUserManagementFragment;
import com.example.tlulostandclaim.ui.user.AdminUserManagementFragmentDirections;
import com.example.tlulostandclaim.ui.user.AdminUserManagementViewModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagementFragment extends Fragment implements CategoryAdapter.CategoryInteract {

    private NavController navController;
    private FragmentCategoryManagementBinding binding;
    private CategoryManagementViewModel viewModel;
    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable workRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(CategoryManagementFragment.this);
        viewModel = new ViewModelProvider(requireActivity()).get(CategoryManagementViewModel.class);
        binding = FragmentCategoryManagementBinding.inflate(inflater, container, false);
        categoryAdapter = new CategoryAdapter(categoryModelList, this);
        viewModel.fetchData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarSearch.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.toolbarSearch.textToolbarTitle.setText("Quản lý danh mục");
        binding.listOfCategory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfCategory.setAdapter(categoryAdapter);
        binding.imageAddCategory.setOnClickListener(v -> navController.navigate(CategoryManagementFragmentDirections.actionCategoryManagementFragmentToAddCategoryFragment()));
        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (workRunnable != null) handler.removeCallbacks(workRunnable);
                workRunnable = () -> performSearch(s.toString());
                handler.postDelayed(workRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        observeData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        viewModel.listOfCategory().observe(getViewLifecycleOwner(), l -> {
            categoryModelList.clear();
            categoryModelList.addAll(l);
            categoryAdapter.notifyDataSetChanged();
        });
        viewModel.deleteCategoryResponse().observe(getViewLifecycleOwner(), s -> {
            if (s) {
                viewModel.fetchData();
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });

    }

    private void performSearch(String text) {
        viewModel.searchItems(text);
    }

    @Override
    public void onDelete(CategoryModel model) {
        viewModel.deleteCategory(model.getName());
    }
}