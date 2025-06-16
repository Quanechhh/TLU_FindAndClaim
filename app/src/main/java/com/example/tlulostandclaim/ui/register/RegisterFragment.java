package com.example.tlulostandclaim.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.BuildConfig;
import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.databinding.FragmentRegisterBinding;
import com.example.tlulostandclaim.utils.GlobalFunction;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding; // Sử dụng ViewBinding để ánh xạ layout
    private RegisterViewModel viewModel; // ViewModel xử lý logic đăng ký
    private NavController navController; // Điều hướng giữa các Fragment

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Gán layout cho Fragment bằng ViewBinding
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        // Tạo instance ViewModel
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Lấy NavController để điều hướng
        navController = NavHostFragment.findNavController(RegisterFragment.this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Nếu đang đăng ký tài khoản với quyền ADMIN thì ẩn ô nhập mã sinh viên và các nút không cần thiết
        if (BuildConfig.ROLE == "ADMIN") {
            binding.textSignIn.setVisibility(View.INVISIBLE); // Ẩn nút chuyển về màn đăng nhập
            binding.textStudentId.setVisibility(View.GONE); // Ẩn label mã sinh viên
            binding.inputStudentId.setVisibility(View.GONE); // Ẩn ô nhập mã sinh viên
        }

        // Sự kiện quay lại màn hình trước
        binding.imageBack.setOnClickListener(v -> navController.navigateUp());

        // Sự kiện quay lại đăng nhập (nếu không phải admin)
        binding.textSignIn.setOnClickListener(v -> navController.navigateUp());

        // Sự kiện hiển thị/ẩn mật khẩu
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

        // Sự kiện khi người dùng nhấn nút Đăng ký
        binding.buttonSignUp.setOnClickListener(v -> {
            // Lấy dữ liệu người dùng nhập
            String studentId = binding.inputStudentId.getText().toString();
            String name = binding.inputFullName.getText().toString();
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();
            String phone = binding.inputMobilePhone.getText().toString();

            // Mặc định role là STUDENT (2), nếu không phải student thì là EMPLOYEE (1)
            int role = 2;
            if (BuildConfig.ROLE != "STUDENT") {
                role = 1;
            }

            // Kiểm tra ràng buộc nhập đủ các trường
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                // Gửi dữ liệu người dùng sang ViewModel để xử lý đăng ký
                viewModel.registerUser(new User(
                        null, // userId (Firebase sẽ tự sinh)
                        name,
                        phone,
                        studentId,
                        email,
                        role,
                        password
                ));
            }
        });

        // Theo dõi phản hồi đăng ký từ ViewModel
        observeData();
    }

    /**
     * Hàm lắng nghe dữ liệu phản hồi sau khi gọi đăng ký người dùng.
     */
    private void observeData() {
        viewModel.getRegisterUserResponse().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s.isEmpty()) {
                    // Đăng ký thành công -> quay về màn hình trước
                    navController.navigateUp();
                } else {
                    // Đăng ký thất bại -> hiển thị lỗi
                    GlobalFunction.showToastMessage(requireContext(), s);
                }
            }
        });
    }
}
