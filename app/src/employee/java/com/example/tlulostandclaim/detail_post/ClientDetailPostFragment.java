package com.example.tlulostandclaim.detail_post;

// Import các thư viện cần thiết
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tlulostandclaim.data.enum_model.EnumPostStatus;
import com.example.tlulostandclaim.databinding.FragmentClientDetailPostBinding;
import com.example.tlulostandclaim.ui.student.add_post.adapter.ImageAdapter;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class ClientDetailPostFragment extends Fragment {

    // Binding để truy cập layout
    private FragmentClientDetailPostBinding binding;

    // NavController để điều hướng màn hình
    private NavController navController;

    // Adapter hiển thị danh sách hình ảnh
    private ImageAdapter imageAdapter;

    // Danh sách chứa các hình ảnh
    private List<Object> list = new ArrayList<>();

    // ViewModel để quản lý dữ liệu
    private ClientDetailPostViewModel viewModel;

    // Đối tượng nhận tham số truyền vào Fragment
    private ClientDetailPostFragmentArgs navArgs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Lấy dữ liệu được truyền từ màn hình trước qua Navigation
        navArgs = ClientDetailPostFragmentArgs.fromBundle(requireArguments());

        // Liên kết layout với Fragment
        binding = FragmentClientDetailPostBinding.inflate(inflater, container, false);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(ClientDetailPostViewModel.class);

        // Khởi tạo adapter cho danh sách hình ảnh
        imageAdapter = new ImageAdapter(list);

        // Gọi hàm lấy chi tiết bài đăng theo postId được truyền vào
        viewModel.getItem(navArgs.getPostId());

        // Lấy NavController để điều hướng
        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý nút quay lại
        binding.toolbarDetailPost.imageBack.setOnClickListener(v -> navController.navigateUp());

        // Đặt tiêu đề toolbar
        binding.toolbarDetailPost.textToolbarTitle.setText("Chi tiết");

        // Cài đặt layout cho danh sách hình ảnh theo chiều ngang
        binding.listOfImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfImages.setAdapter(imageAdapter);

        // Xử lý nút gửi yêu cầu
        binding.btnSave.setOnClickListener(v -> viewModel.sendRequest());

        // Theo dõi dữ liệu thay đổi
        observeData();
    }

    // Theo dõi dữ liệu từ ViewModel và cập nhật giao diện
    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {

        // Theo dõi chi tiết bài đăng trả về
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), s -> {

            // Gán dữ liệu bài đăng vào các trường thông tin
            binding.inputReleaseDate.setText(s.getLostDate());
            binding.inputDescription.setText(s.getDescription());
            binding.inputName.setText(s.getName());
            binding.inputLostLocation.setText(s.getLostLocation());
            binding.inputContactInfo.setText(s.getContactInfo());

            // Cập nhật danh sách hình ảnh
            list.clear();
            list.addAll(s.getImage());
            imageAdapter.notifyDataSetChanged();
        });

        // Theo dõi kết quả khi gửi yêu cầu
        viewModel.getRequestResponse().observe(getViewLifecycleOwner(), s -> {
            if (s) { // Nếu gửi yêu cầu thành công
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                // navController.navigateUp(); // Có thể quay lại màn trước nếu muốn
            } else { // Nếu gửi yêu cầu thất bại
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });
    }
}
