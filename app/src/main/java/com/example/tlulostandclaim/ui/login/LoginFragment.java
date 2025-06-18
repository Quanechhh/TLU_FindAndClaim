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

    private FragmentLoginBinding binding;         // Binding với file layout fragment_login.xml
    private LoginViewModel viewModel;             // ViewModel để xử lý logic đăng nhập
    private NavController navController;          // Điều hướng giữa các màn hình (fragment)

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class); // Khởi tạo ViewModel
        navController = NavHostFragment.findNavController(LoginFragment.this); // Lấy NavController
        return binding.getRoot(); // Trả về root view của layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sự kiện khi ấn nút "Đăng nhập"
        binding.buttonSignIn.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();

            // Kiểm tra nếu email hoặc password trống
            if (email.isEmpty() || password.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                // Gọi ViewModel thực hiện đăng nhập với email và password
                viewModel.loginEmail(new User(email, password));
            }
        });

        // Ẩn chức năng "Quên mật khẩu" và "Đăng ký" nếu đang đăng nhập bằng quyền ADMIN
        if (BuildConfig.ROLE == "ADMIN") {
            binding.textForgetPassword.setVisibility(View.INVISIBLE);
            binding.textSignUp.setVisibility(View.INVISIBLE);
        }

        // Mặc định gán sẵn email và password vào input (chỉ dùng trong dev/test)
        binding.inputEmail.setText("vanrap13062003@gmail.com");
        binding.inputPassword.setText("123456");

        // Sự kiện ẩn/hiện mật khẩu khi click vào icon mắt
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

        // Sự kiện chuyển sang màn hình "Đăng ký"
        binding.textSignUp.setOnClickListener(v -> {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
            viewModel.clearLoginResponse(); // Xóa kết quả đăng nhập trước đó
        });

        // Sự kiện chuyển sang màn hình "Quên mật khẩu"
        binding.textForgetPassword.setOnClickListener(v -> {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment());
        });

        observeData(); // Lắng nghe dữ liệu phản hồi từ ViewModel
    }

    // Phương thức quan sát kết quả đăng nhập từ ViewModel
    private void observeData() {
        viewModel.loginUserResponse().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s.isEmpty()) { // Thành công (Firebase trả về chuỗi rỗng)
                    if (GlobalData.user.getRole() == EnumUserRole.STUDENT.value && BuildConfig.ROLE.equals("STUDENT")) {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment());
                    } else if (GlobalData.user.getRole() == EnumUserRole.EMPLOYEE.value && BuildConfig.ROLE.equals("EMPLOYEE")) {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToClientMainNav());
                    } else if (GlobalData.user.getRole() == EnumUserRole.ADMIN.value && BuildConfig.ROLE.equals("ADMIN")) {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToAdminMainNav());
                    } else {
                        GlobalFunction.showToastMessage(requireContext(), "Không tìm thấy tài khoản!");
                    }
                } else {
                    // Nếu có lỗi, hiện thông báo lỗi
                    GlobalFunction.showToastMessage(requireContext(), s);
                }
                viewModel.clearLoginResponse(); // Reset kết quả sau khi xử lý
            }
        });
    }
}
