package com.example.tlulostandclaim.ui.category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.databinding.FragmentAddCategoryBinding;
import com.example.tlulostandclaim.databinding.FragmentCategoryManagementBinding;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryFragment extends BottomSheetDialogFragment {
    private NavController navController;
    private FragmentAddCategoryBinding binding;
    private CategoryManagementViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(AddCategoryFragment.this);
        viewModel = new ViewModelProvider(requireActivity()).get(CategoryManagementViewModel.class);
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.buttonAdd.setOnClickListener(v -> {
            String name = binding.inputName.getText().toString();
            if (name.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                viewModel.addCategory(new CategoryModel(name));
            }
        });

        viewModel.addCategoryResponse().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s) {
                    viewModel.fetchData();
                    navController.navigateUp();
                    GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                } else {
                    GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
                }
                viewModel.clearAddCategoryResponse();
            }
        });
    }
}