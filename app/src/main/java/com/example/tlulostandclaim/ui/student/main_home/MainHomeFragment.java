package com.example.tlulostandclaim.ui.student.main_home;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.FragmentMainHomeBinding;
import com.example.tlulostandclaim.ui.student.main_home.adapter.InStorageLostItemAdapter;
import com.example.tlulostandclaim.ui.student.main_home.adapter.LostItemAdapter;
import com.example.tlulostandclaim.utils.GlobalData;

import java.util.ArrayList;

public class MainHomeFragment extends Fragment implements InStorageLostItemAdapter.InStorageLostItemInteract {
    private NavController navController;
    private FragmentMainHomeBinding binding;
    private MainHomeFragmentViewModel viewModel;
    private InStorageLostItemAdapter lostItemAdapter;
    private ArrayList<InStorageLostItemModel> lostItemModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MainHomeFragmentViewModel.class);
        lostItemModelArrayList = new ArrayList<>();
        lostItemAdapter = new InStorageLostItemAdapter(lostItemModelArrayList, this::onChosenAction);
        navController = NavHostFragment.findNavController(MainHomeFragment.this);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getItems();
        if (GlobalData.user != null) {
            binding.textHelloUser.setText("Chào " + GlobalData.user.getFullName() + ",");
        }
        binding.inputSearch.setFocusable(false);
        binding.inputSearch.setOnClickListener(v -> navController.navigate(MainHomeFragmentDirections.actionMainHomeFragmentToLostItemSearchFragment()));
        binding.listOfItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfItems.setAdapter(lostItemAdapter);
        binding.imageAddPost.setOnClickListener(v -> navController.navigate(MainHomeFragmentDirections.actionMainHomeFragmentToAddLostItemPostFragment()));
        binding.iconAddPost.setOnClickListener(v -> navController.navigate(MainHomeFragmentDirections.actionMainHomeFragmentToAddLostItemPostFragment()));
        observeData();
    }

    private void observeData() {
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                lostItemModelArrayList.clear();
                lostItemModelArrayList.addAll(list);
                lostItemAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onChosenAction(InStorageLostItemModel model) {
        navController.navigate(MainHomeFragmentDirections.actionMainHomeFragmentToDetailInStorageLostItemFragment(model.getId()));
    }
}