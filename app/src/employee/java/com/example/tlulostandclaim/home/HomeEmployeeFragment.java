package com.example.tlulostandclaim.home;

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

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.FragmentHomeClientBinding;
import com.example.tlulostandclaim.ui.student.main_home.MainHomeFragmentDirections;
import com.example.tlulostandclaim.ui.student.main_home.adapter.LostItemAdapter;
import com.example.tlulostandclaim.utils.GlobalData;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;


public class HomeEmployeeFragment extends Fragment implements LostItemAdapter.LostItemInteract, HomeActionItemAdapter.OnActionsChosen {
    private NavController navController;
    private FragmentHomeClientBinding binding;
    private HomeEmployeeFragmentViewModel viewModel;
    private LostItemAdapter lostItemAdapter;
    private HomeActionItemAdapter homeActionItemAdapter;
    private ArrayList<LostItemModel> lostItemModelArrayList;
    private ArrayList<Integer> actionsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeClientBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HomeEmployeeFragmentViewModel.class);
        lostItemModelArrayList = new ArrayList<>();
        actionsList.clear();
        actionsList.add(R.drawable.image_check_in_lost_item);
        actionsList.add(R.drawable.image_storage_manage);
        actionsList.add(R.drawable.image_request_item);
        lostItemAdapter = new LostItemAdapter(lostItemModelArrayList, this);
        homeActionItemAdapter = new HomeActionItemAdapter(actionsList, this);
        navController = NavHostFragment.findNavController(HomeEmployeeFragment.this);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getItems();
        if (GlobalData.user != null) {
            binding.textHelloUser.setText("Chào " + GlobalData.user.getFullName() + ",");
        }
        binding.listOfActions.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfActions.setAdapter(homeActionItemAdapter);
        homeActionItemAdapter.notifyDataSetChanged();
        binding.inputSearch.setFocusable(false);
        binding.inputSearch.setOnClickListener(v -> navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToSearchPostsFragment()));
        binding.listOfItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfItems.setAdapter(lostItemAdapter);
        binding.iconAddPost.setOnClickListener(v -> navController.navigate(MainHomeFragmentDirections.actionMainHomeFragmentToAddLostItemPostFragment()));
        observeData();
    }

    @SuppressLint("NotifyDataSetChanged")
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
    public void onChosenAction(LostItemModel model) {
        navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToClientDetailPostFragment(model.getId()));
    }

    @Override
    public void onAction(int data) {
        if (data == actionsList.get(0)) {
            navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToAddLostItemFragment());
        } else if (data == actionsList.get(1)) {
            navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToInStorageItemManagementFragment());
        } else {
            navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToStudentRequestManagementFragment());
        }
    }
}