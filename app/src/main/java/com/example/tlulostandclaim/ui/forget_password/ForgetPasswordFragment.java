package com.example.tlulostandclaim.ui.forget_password;

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
import com.example.tlulostandclaim.databinding.FragmentForgetPasswordBinding;
import com.example.tlulostandclaim.ui.login.LoginFragment;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

public class ForgetPasswordFragment extends Fragment {
    private FragmentForgetPasswordBinding binding;
    private ForgetPasswordViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);
        navController = NavHostFragment.findNavController(ForgetPasswordFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSignIn.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString();
            if (email.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                viewModel.forgetPassword(email);
            }
        });
        binding.imageBack.setOnClickListener(v -> {
            navController.navigateUp();
        });
        observeData();
    }

    private void observeData() {
        viewModel.forgetPasswordResponse().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s.isEmpty()) {
                    GlobalFunction.showToastMessage(requireContext(), "Thành công!");
                    navController.navigateUp();
                } else {
                    GlobalFunction.showToastMessage(requireContext(), s);
                }
            }
        });
    }
}