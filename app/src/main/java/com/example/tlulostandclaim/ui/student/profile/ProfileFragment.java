package com.example.tlulostandclaim.ui.student.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.FragmentMyPostManagementBinding;
import com.example.tlulostandclaim.databinding.FragmentProfileBinding;
import com.example.tlulostandclaim.ui.student.main_home.adapter.LostItemAdapter;
import com.example.tlulostandclaim.ui.student.my_post.MyPostManagementFragment;
import com.example.tlulostandclaim.ui.student.my_post.MyPostManagementViewModel;
import com.example.tlulostandclaim.utils.GlobalData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProfileFragment extends Fragment {
    private NavController navController;
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private List<LostItemModel> lostItemModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        navController = NavHostFragment.findNavController(ProfileFragment.this);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.textProfileChangePassword.setOnClickListener(v -> {
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment());
        });
        binding.textProfileLogout.setOnClickListener(v -> {
            GlobalData.user = null;
            NavOptions options = new NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, true) // true nghĩa là inclusive
                    .setLaunchSingleTop(true)
                    .build();

            NavController navController = NavHostFragment.findNavController(Objects.requireNonNull(requireActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container_all)));
            navController.navigate(R.id.loginFragment, null, options);
        });
    }
}