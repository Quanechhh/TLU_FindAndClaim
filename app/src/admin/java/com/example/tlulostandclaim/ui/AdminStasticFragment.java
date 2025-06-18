package com.example.tlulostandclaim.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.databinding.FragmentAddCategoryBinding;
import com.example.tlulostandclaim.databinding.FragmentAdminStasticBinding;
import com.example.tlulostandclaim.ui.AddCategoryFragment;
import com.example.tlulostandclaim.ui.CategoryManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminStasticFragment extends Fragment {

    private NavController navController;
    private FragmentAdminStasticBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(AdminStasticFragment.this);
        binding = FragmentAdminStasticBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarStatistic.textToolbarTitle.setText("Báo cáo thống kê");
        binding.toolbarStatistic.imageBack.setOnClickListener(v -> {
            navController.navigateUp();
        });
    }
}