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

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding; // Binding cho layout fragment_login.xml
    private LoginViewModel viewModel; // ViewModel xử lý logic đăng nhập
    private NavController navController; // Điều hướng giữa các Fragment

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout thông qua ViewBinding
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Khởi tạo ViewModel để quản lý dữ liệu UI
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Khởi tạo NavController để điều hướng giữa các Fragment
        navController = NavHostFragment.findNavController(LoginFragment.this);

        return binding.getRoot(); // Trả về root view
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý sự kiện khi người dùng nhấn nút "Đăng nhập"
        binding.buttonSignIn.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();

            // Kiểm tra các trường bắt buộc
            if (email.isEmpty() || password.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                // Gửi thông tin đăng nhập tới ViewModel
                viewModel.loginEmail(new User(email, password));
            }
        });

        // Nếu đang ở chế độ ADMIN (xác định qua biến cấu hình), ẩn chức năng "Quên mật khẩu" và "Đăng ký"
        if (BuildConfig.ROLE == "ADMIN") {
            binding.textForgetPassword.setVisibility(View.INVISIBLE);
            binding.textSignUp.setVisibility(View.INVISIBLE);
        }

        // Gán sẵn email và mật khẩu mẫu để test nhanh
        binding.inputEmail.setText("tuanprokt44@gmail.com");
        binding.inputPassword.setText("123456");

        // Xử lý nút hiện/ẩn mật khẩu
        binding.imageShowPassword.setOnClickListener(v -> {
            viewModel.isHidePassword = !viewModel.isHidePassword;

            if (viewModel.isHidePassword) {
                // Hiển thị icon "mật khẩu bị ẩn"
                binding.imageShowPassword.setImageResource(R.drawable.ic_show_password);
                binding.inputPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
            } else {
                // Hiển thị icon "mật khẩu đang hiển thị"
                binding.imageShowPassword.setImageResource(R.drawable.ic_hide_password);
                binding.inputPassword.setInputType(
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
            }
        });

        // Chuyển đến màn hình "Đăng ký"
        binding.textSignUp.setOnClickListener(v -> {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
            viewModel.clearLoginResponse(); // Xoá kết quả đăng nhập trước đó
        });

        // Chuyển đến màn hình "Quên mật khẩu"
        binding.textForgetPassword.setOnClickListener(v -> {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment());
        });

        observeData(); // Bắt đầu lắng nghe dữ liệu từ ViewModel
    }

    /**
     * Hàm lắng nghe LiveData từ ViewModel để xử lý kết quả đăng nhập.
     */
    private void observeData() {
        viewModel.loginUserResponse().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s.isEmpty()) {
                    // Nếu đăng nhập thành công (s là chuỗi rỗng), chuyển hướng theo vai trò người dùng
                    if (GlobalData.user.getRole() == EnumUserRole.STUDENT.value && BuildConfig.ROLE.equals("STUDENT")) {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment());
                    } else if (GlobalData.user.getRole() == EnumUserRole.EMPLOYEE.value && BuildConfig.ROLE == "EMPLOYEE") {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToClientMainNav());
                    } else if (GlobalData.user.getRole() == EnumUserRole.ADMIN.value && BuildConfig.ROLE == "ADMIN") {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToAdminMainNav());
                    } else {
                        GlobalFunction.showToastMessage(requireContext(), "Không tìm thấy tài khoản!");
                    }
                } else {
                    // Nếu có lỗi đăng nhập, hiển thị thông báo lỗi
                    GlobalFunction.showToastMessage(requireContext(), s);
                }

                // Xoá kết quả đăng nhập sau khi xử lý
                viewModel.clearLoginResponse();
            }
        });
    }
}
