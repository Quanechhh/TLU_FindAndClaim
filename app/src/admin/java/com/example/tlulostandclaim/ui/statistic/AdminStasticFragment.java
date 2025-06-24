package com.example.tlulostandclaim.ui.statistic;

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
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.databinding.FragmentAddCategoryBinding;
import com.example.tlulostandclaim.databinding.FragmentAdminStasticBinding;
import com.example.tlulostandclaim.ui.category.AddCategoryFragment;
import com.example.tlulostandclaim.ui.category.CategoryManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminStasticFragment extends Fragment {

    private NavController navController;
    private FragmentAdminStasticBinding binding;
    private AdminStatisticViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(AdminStasticFragment.this);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminStatisticViewModel.class);
        binding = FragmentAdminStasticBinding.inflate(inflater, container, false);
        viewModel.getPosts();
        viewModel.getUsers();
        viewModel.getItems();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarStatistic.textToolbarTitle.setText("Báo cáo thống kê");
        binding.toolbarStatistic.imageBack.setOnClickListener(v -> {
            navController.navigateUp();
        });
        observeData();
    }

    private void observeData() {
        viewModel.listOfUsers().observe(getViewLifecycleOwner(), s -> {
            List<User> employees = new ArrayList<>();
            List<User> students = new ArrayList<>();
            for (int i = 0; i < s.size(); i++) {
                if (s.get(i).getRole() == 1) {
                    employees.add(s.get(i));
                } else if (s.get(i).getRole() == 2) {
                    students.add(s.get(i));
                }
            }
            binding.textTotalEmployees.setText("Số lượng nhân viên: " + employees.size());
            binding.textTotalStudents.setText("Số lượng học sinh: " + students.size());
        });

        viewModel.listOfPosts().observe(getViewLifecycleOwner(), s -> {
            List<LostItemModel> done = new ArrayList<>();
            List<LostItemModel> undone = new ArrayList<>();
            for (LostItemModel i : s) {
                if (i.getStatus() == 0) {
                    undone.add(i);
                } else if (i.getStatus() == 2) {
                    done.add(i);
                }
            }
            binding.textTotalUndone.setText("Số lượng bài đăng đang tìm: " + undone.size());
            binding.textTotalDone.setText("Số lượng bài đăng đã hoàn thành: " + done.size());
        });

        viewModel.listOfItems().observe(getViewLifecycleOwner(), s -> {
            List<InStorageLostItemModel> done = new ArrayList<>();
            List<InStorageLostItemModel> undone = new ArrayList<>();
            for (InStorageLostItemModel i : s) {
                if (i.getStatus() == 0) {
                    undone.add(i);
                } else {
                    done.add(i);
                }
            }
            binding.textTotalItemsUndone.setText("Số lượng vật phẩm đang tìm: " + undone.size());
            binding.textTotalItemsDone.setText("Số lượng vật phẩm đã tìm thấy: " + done.size());
        });
    }
}