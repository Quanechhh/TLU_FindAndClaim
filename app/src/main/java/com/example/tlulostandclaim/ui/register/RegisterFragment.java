package com.example.tlulostandclaim.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.BuildConfig;
import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.enum_model.EnumUserRole;
import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.databinding.FragmentRegisterBinding;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        navController = NavHostFragment.findNavController(RegisterFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.ROLE == "ADMIN") {
            binding.textSignIn.setVisibility(View.INVISIBLE);
            binding.textStudentId.setVisibility(View.GONE);
            binding.inputStudentId.setVisibility(View.GONE);
        }
        binding.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.textSignIn.setOnClickListener(v -> navController.navigateUp());
        binding.imageShowPassword.setOnClickListener(v -> {
            viewModel.isHidePassword = !viewModel.isHidePassword;

            if (viewModel.isHidePassword) {
                binding.imageShowPassword.setImageResource(R.drawable.ic_show_password);
                binding.inputPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
            } else {
                binding.imageShowPassword.setImageResource(R.drawable.ic_hide_password);
                binding.inputPassword.setInputType(
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
            }
        });
        binding.buttonSignUp.setOnClickListener(v -> {
            String studentId = binding.inputStudentId.getText().toString();
            String name = binding.inputFullName.getText().toString();
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();
            String phone = binding.inputMobilePhone.getText().toString();
            int role = 2;
            if (BuildConfig.ROLE != "STUDENT") {
                role = 1;
            }
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                viewModel.registerUser(new User(null, name, phone, studentId, email, role, password));
            }
        });
        observeData();
    }

    private void observeData() {
        viewModel.getRegisterUserResponse().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s.isEmpty()) navController.navigateUp();
                else
                    GlobalFunction.showToastMessage(requireContext(), s);
            }
        });
    }
}