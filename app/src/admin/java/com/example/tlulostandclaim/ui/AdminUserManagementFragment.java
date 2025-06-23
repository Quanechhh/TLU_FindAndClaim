package com.example.tlulostandclaim.ui.user;

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

import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.databinding.FragmentAdminUserManagementBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagementFragment extends Fragment implements AdminUserManagementAdapter.AdminUserManagementInteract {
    private NavController navController;
    private FragmentAdminUserManagementBinding binding;
    private AdminUserManagementViewModel viewModel;
    private List<User> userList = new ArrayList<>();
    private AdminUserManagementAdapter userManagementAdapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable workRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(AdminUserManagementFragment.this);
        viewModel = new ViewModelProvider(this).get(AdminUserManagementViewModel.class);
        binding = FragmentAdminUserManagementBinding.inflate(inflater, container, false);
        userManagementAdapter = new AdminUserManagementAdapter(userList, this);
        viewModel.fetchData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarSearch.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.toolbarSearch.textToolbarTitle.setText("Quản lý người dùng");
        binding.listOfUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfUsers.setAdapter(userManagementAdapter);
        binding.imageAddUser.setOnClickListener(v -> navController.navigate(AdminUserManagementFragmentDirections.actionAdminUserManagementFragmentToRegisterFragment22()));
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
        viewModel.listOfUsers().observe(getViewLifecycleOwner(), l -> {
            userList.clear();
            userList.addAll(l);
            userManagementAdapter.notifyDataSetChanged();
        });
    }

    private void performSearch(String text) {
        viewModel.searchItems(text);
    }

    @Override
    public void onUserChosen(User user) {
        navController.navigate(AdminUserManagementFragmentDirections.actionAdminUserManagementFragmentToAdminDetailUserFragment(user.getId()));
    }
}