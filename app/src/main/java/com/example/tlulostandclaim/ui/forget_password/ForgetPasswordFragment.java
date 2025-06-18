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
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

/**
 * Fragment này dùng để xử lý logic và giao diện của chức năng "Quên mật khẩu".
 */
public class ForgetPasswordFragment extends Fragment {
    private FragmentForgetPasswordBinding binding; // ViewBinding giúp truy cập trực tiếp đến các View trong layout
    private ForgetPasswordViewModel viewModel;     // ViewModel để tách biệt logic và UI
    private NavController navController;           // Dùng để điều hướng giữa các màn hình (Fragment)

    /**
     * Hàm tạo View cho Fragment bằng cách sử dụng ViewBinding.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class); // Khởi tạo ViewModel
        navController = NavHostFragment.findNavController(ForgetPasswordFragment.this); // Lấy NavController để điều hướng
        return binding.getRoot(); // Trả về root view
    }

    /**
     * Hàm được gọi sau khi View đã được tạo xong.
     * Ở đây xử lý các sự kiện click và observer dữ liệu.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý khi người dùng bấm nút "Gửi"
        binding.buttonSignIn.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString(); // Lấy email người dùng nhập

            if (email.isEmpty()) {
                // Nếu email rỗng, hiển thị thông báo yêu cầu điền đầy đủ
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                // Gửi yêu cầu quên mật khẩu lên ViewModel
                viewModel.forgetPassword(email);
            }
        });

        // Xử lý khi người dùng bấm nút quay lại
        binding.imageBack.setOnClickListener(v -> {
            navController.navigateUp(); // Điều hướng quay lại Fragment trước đó
        });

        // Theo dõi phản hồi từ ViewModel
        observeData();
    }

    /**
     * Quan sát LiveData từ ViewModel để nhận phản hồi quên mật khẩu.
     */
    private void observeData() {
        viewModel.forgetPasswordResponse().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s.isEmpty()) {
                    // Nếu phản hồi rỗng, nghĩa là gửi email thành công
                    GlobalFunction.showToastMessage(requireContext(), "Thành công!");
                    navController.navigateUp(); // Quay lại màn hình trước
                } else {
                    // Hiển thị thông báo lỗi nếu có
                    GlobalFunction.showToastMessage(requireContext(), s);
                }
            }
        });
    }
}
