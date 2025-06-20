package com.example.tlulostandclaim.ui;

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
import com.example.tlulostandclaim.databinding.FragmentAdminDetailUserBinding;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class AdminDetailUserFragment extends Fragment {

    private NavController navController;
    private FragmentAdminDetailUserBinding binding;
    private AdminUserManagementViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(AdminDetailUserFragment.this);
        viewModel = new ViewModelProvider(this).get(AdminUserManagementViewModel.class);
        binding = FragmentAdminDetailUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageBack.setOnClickListener(v -> navController.navigateUp());
    }
}