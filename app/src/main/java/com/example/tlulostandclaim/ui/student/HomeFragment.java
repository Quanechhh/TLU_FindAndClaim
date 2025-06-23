package com.example.tlulostandclaim.ui.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.fragment_container_home);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.homeBottomNav, navController);
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.mainHomeFragment) {
                binding.homeBottomNav.setVisibility(View.VISIBLE);
            }
            else if (navDestination.getId() == R.id.myPostManagementFragment) {
                binding.homeBottomNav.setVisibility(View.VISIBLE);
            }
            else if (navDestination.getId() == R.id.profileFragment) {
                binding.homeBottomNav.setVisibility(View.VISIBLE);
            } else {
                binding.homeBottomNav.setVisibility(View.GONE);
            }
        });
    }
}