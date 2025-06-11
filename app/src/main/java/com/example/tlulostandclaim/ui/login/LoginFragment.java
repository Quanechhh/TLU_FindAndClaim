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
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private NavController navController;

    // Khởi tạo View Fragment bằng ViewBinding
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // Cài đặt logic khi view được tạo xong
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        navController = NavHostFragment.findNavController(this);

        setupUI();
        setupClickEvents();
        observeLoginResult();
    }

    // Thiết lập dữ liệu mặc định và UI ban đầu
    private void setupUI() {
        // Gán dữ liệu mẫu để test nhanh
        binding.inputEmail.setText("vanrap136@gmail.com");
        binding.inputPassword.setText("123456");

        // Nếu là ADMIN, ẩn các lựa chọn không cần thiết
        if (BuildConfig.ROLE.equals("ADMIN")) {
            binding.textForgetPassword.setVisibility(View.GONE);
            binding.textSignUp.setVisibility(View.GONE);
        }
    }

    // Thiết lập các sự kiện click
    private void setupClickEvents() {
        // Sự kiện click vào nút đăng nhập
        binding.buttonSignIn.setOnClickListener(v -> handleLogin());

        // Sự kiện click vào biểu tượng hiện/ẩn mật khẩu
        binding.imageShowPassword.setOnClickListener(v -> togglePasswordVisibility());

        // Điều hướng đến trang Đăng ký
        binding.textSignUp.setOnClickListener(v -> {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
            loginViewModel.clearLoginResponse();
        });

        // Điều hướng đến trang Quên mật khẩu
        binding.textForgetPassword.setOnClickListener(v -> {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment());
        });
    }

    // Xử lý sự kiện đăng nhập
    private void handleLogin() {
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            return;
        }

        // Gửi yêu cầu đăng nhập đến ViewModel
        loginViewModel.loginEmail(new User(email, password));
    }

    // Chuyển đổi hiển thị mật khẩu (ẩn/hiện)
    private void togglePasswordVisibility() {
        loginViewModel.isHidePassword = !loginViewModel.isHidePassword;

        if (loginViewModel.isHidePassword) {
            binding.imageShowPassword.setImageResource(R.drawable.ic_show_password);
            binding.inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            binding.imageShowPassword.setImageResource(R.drawable.ic_hide_password);
            binding.inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

        // Di chuyển con trỏ về cuối văn bản
        binding.inputPassword.setSelection(binding.inputPassword.getText().length());
    }

    // Quan sát kết quả đăng nhập từ ViewModel
    private void observeLoginResult() {
        loginViewModel.loginUserResponse().observe(getViewLifecycleOwner(), response -> {
            if (response == null) return;

            if (response.isEmpty()) {
                // Kiểm tra role và điều hướng tương ứng
                int userRole = GlobalData.user.getRole();

                if (userRole == EnumUserRole.STUDENT.value && BuildConfig.ROLE.equals("STUDENT")) {
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment());
                } else if (userRole == EnumUserRole.EMPLOYEE.value && BuildConfig.ROLE.equals("EMPLOYEE")) {
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToClientMainNav());
                } else if (userRole == EnumUserRole.ADMIN.value && BuildConfig.ROLE.equals("ADMIN")) {
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToAdminMainNav());
                } else {
                    GlobalFunction.showToastMessage(requireContext(), "Không tìm thấy tài khoản phù hợp!");
                }
            } else {
                // Hiển thị lỗi đăng nhập nếu có
                GlobalFunction.showToastMessage(requireContext(), response);
            }

            loginViewModel.clearLoginResponse();
        });
    }
}
