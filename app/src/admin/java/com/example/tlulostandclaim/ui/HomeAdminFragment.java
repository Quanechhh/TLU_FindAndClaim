package com.example.tlulostandclaim.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.databinding.FragmentHomeAdminBinding;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeAdminFragment extends Fragment {

    private NavController navController;
    private FragmentHomeAdminBinding binding;
    private HomeAdminViewModel viewModel;

    private List<Integer> featuresList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(HomeAdminFragment.this);
        viewModel = new ViewModelProvider(this).get(HomeAdminViewModel.class);
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false);
        featuresList.clear();
        featuresList.add(R.drawable.image_admin_user);
        featuresList.add(R.drawable.image_admin_category);
        featuresList.add(R.drawable.image_admin_stastic);
        return binding.getRoot();
    }
}