package com.example.tlulostandclaim.ui.student.search;

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

import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.databinding.FragmentLostItemSearchBinding;
import com.example.tlulostandclaim.ui.student.main_home.MainHomeFragmentDirections;
import com.example.tlulostandclaim.ui.student.main_home.adapter.InStorageLostItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class LostItemSearchFragment extends Fragment implements InStorageLostItemAdapter.InStorageLostItemInteract {
    private FragmentLostItemSearchBinding binding;
    private NavController navController;
    private InStorageLostItemAdapter lostItemAdapter;
    private LostItemSearchViewModel viewModel;
    private List<InStorageLostItemModel> lostItemModels = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable workRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLostItemSearchBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(LostItemSearchViewModel.class);
        viewModel.getItems();
        navController = NavHostFragment.findNavController(LostItemSearchFragment.this);
        lostItemAdapter = new InStorageLostItemAdapter(lostItemModels, this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarSearch.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.toolbarSearch.textToolbarTitle.setText("Tìm kiếm");
        binding.listOfItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfItems.setAdapter(lostItemAdapter);
        binding.imageFilter.setOnClickListener(v -> {
            viewModel.clearCategory();
            navController.navigate(LostItemSearchFragmentDirections.actionLostItemSearchFragmentToFilterBottomSheetFragment2());
        });
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
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), l -> {
            lostItemModels.clear();
            lostItemModels.addAll(l);
            lostItemAdapter.notifyDataSetChanged();
        });
    }

    private void performSearch(String text) {
        viewModel.searchItems(text);
    }

    @Override
    public void onChosenAction(InStorageLostItemModel model) {
        navController.navigate(LostItemSearchFragmentDirections.actionLostItemSearchFragmentToDetailInStorageLostItemFragment(model.getId()));
    }
}