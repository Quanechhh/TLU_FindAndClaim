package com.example.tlulostandclaim.ui.home;

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
import com.example.tlulostandclaim.databinding.FragmentMyPostManagementBinding;
import com.example.tlulostandclaim.ui.student.add_post.adapter.ImageAdapter;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeAdminFragment extends Fragment implements FeatureImageAdapter.AdminFeatureInteract {

    private NavController navController;
    private FragmentHomeAdminBinding binding;
    private HomeAdminViewModel viewModel;

    private List<Integer> featuresList = new ArrayList<>();
    private FeatureImageAdapter imageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(HomeAdminFragment.this);
        viewModel = new ViewModelProvider(this).get(HomeAdminViewModel.class);
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false);
        featuresList.clear();
        featuresList.add(R.drawable.image_admin_user);
        featuresList.add(R.drawable.image_admin_category);
        featuresList.add(R.drawable.image_admin_stastic);
        imageAdapter = new FeatureImageAdapter(featuresList, this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarAdmin.imageBack.setVisibility(View.GONE);
        binding.toolbarAdmin.textToolbarTitle.setText("Admin");
        binding.listOfFeatures.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.listOfFeatures.setAdapter(imageAdapter);
    }

    @Override
    public void onChosen(int data) {
        if (data == featuresList.get(0)) {
            navController.navigate(HomeAdminFragmentDirections.actionHomeAdminFragment2ToAdminUserManagementFragment());
        } else if (data == featuresList.get(1)) {
            navController.navigate(HomeAdminFragmentDirections.actionHomeAdminFragment2ToCategoryManagementFragment());
        } else {
            navController.navigate(HomeAdminFragmentDirections.actionHomeAdminFragment2ToAdminStasticFragment());
        }
    }
}