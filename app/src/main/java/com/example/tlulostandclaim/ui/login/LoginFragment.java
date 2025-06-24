package com.example.tlulostandclaim.ui.login;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tlulostandclaim.BuildConfig;
import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.enum_model.EnumUserRole;
import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.databinding.FragmentLoginBinding;
import com.example.tlulostandclaim.utils.GlobalFunction;
import com.example.tlulostandclaim.utils.GlobalData;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        navController = NavHostFragment.findNavController(LoginFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSignIn.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                viewModel.loginEmail(new User(email, password));
            }
        });

        if (BuildConfig.ROLE.equals("ADMIN")) {
            binding.textForgetPassword.setVisibility(View.INVISIBLE);
            binding.textSignUp.setVisibility(View.INVISIBLE);
        }

        // Dữ liệu test sẵn (tùy chọn giữ lại hoặc xóa)
        binding.inputEmail.setText("vanrap13062003@gmail.com");
        binding.inputPassword.setText("123456");

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

        binding.textSignUp.setOnClickListener(v -> {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
            viewModel.clearLoginResponse();
        });

        binding.textForgetPassword.setOnClickListener(v -> {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment());
        });

        observeData();
    }

    private void observeData() {
        viewModel.loginUserResponse().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s.isEmpty()) {
                    if (GlobalData.user.getRole() == EnumUserRole.STUDENT.value && BuildConfig.ROLE.equals("STUDENT")) {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment());
                    } else if (GlobalData.user.getRole() == EnumUserRole.EMPLOYEE.value && BuildConfig.ROLE.equals("EMPLOYEE")) {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToClientMainNav());
