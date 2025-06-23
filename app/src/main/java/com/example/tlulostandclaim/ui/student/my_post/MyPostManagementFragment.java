package com.example.tlulostandclaim.ui.student.my_post;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.FragmentMyPostManagementBinding;
import com.example.tlulostandclaim.ui.student.main_home.MainHomeFragmentDirections;
import com.example.tlulostandclaim.ui.student.main_home.adapter.LostItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class MyPostManagementFragment extends Fragment implements LostItemAdapter.LostItemInteract {
    private NavController navController;
    private FragmentMyPostManagementBinding binding;
    private MyPostManagementViewModel viewModel;

    private LostItemAdapter lostItemAdapter;

    private List<LostItemModel> lostItemModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        navController = NavHostFragment.findNavController(MyPostManagementFragment.this);
        viewModel = new ViewModelProvider(this).get(MyPostManagementViewModel.class);
        binding = FragmentMyPostManagementBinding.inflate(inflater, container, false);
        lostItemAdapter = new LostItemAdapter(lostItemModels, this);
        viewModel.fetchData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarPostManagement.textToolbarTitle.setText("Bài đăng của tôi");
        binding.toolbarPostManagement.imageBack.setVisibility(View.INVISIBLE);
        binding.listOfPosts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfPosts.setAdapter(lostItemAdapter);
        binding.swipeRefreshMyPosts.setOnRefreshListener(this::onRefreshLoad);

        observeData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        viewModel.listOfPosts().observe(getViewLifecycleOwner(), s -> {
            lostItemModels.clear();
            lostItemModels.addAll(s);
            lostItemAdapter.notifyDataSetChanged();
        });
    }

    private void onRefreshLoad() {
        viewModel.fetchData();
        new Handler(Looper.getMainLooper()).postDelayed(() -> binding.swipeRefreshMyPosts.setRefreshing(false), 1500);
    }

    @Override
    public void onChosenAction(LostItemModel model) {
        navController.navigate(MyPostManagementFragmentDirections.actionMyPostManagementFragmentToDetailPostFragment(model.getId()));
    }
}