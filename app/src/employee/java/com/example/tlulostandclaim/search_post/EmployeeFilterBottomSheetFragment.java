package com.example.tlulostandclaim.search_post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.databinding.FragmentFilterBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFilterBottomSheetFragment extends BottomSheetDialogFragment {

    private NavController navController;
    private FragmentFilterBottomSheetBinding binding;
    private ClientSearchPostsViewModel viewModel;
    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private EmployeeFilterCategoryAdapter filterCategoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(EmployeeFilterBottomSheetFragment.this);
        viewModel = new ViewModelProvider(requireActivity()).get(ClientSearchPostsViewModel.class);
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false);
        filterCategoryAdapter = new EmployeeFilterCategoryAdapter(categoryModelList, null);
        viewModel.getCategory();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listOfCategory.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfCategory.setAdapter(filterCategoryAdapter);
        viewModel.listOfCategory().observe(getViewLifecycleOwner(), s -> {
            categoryModelList.clear();
            categoryModelList.addAll(s);
            filterCategoryAdapter.notifyDataSetChanged();
        });
    }
}
