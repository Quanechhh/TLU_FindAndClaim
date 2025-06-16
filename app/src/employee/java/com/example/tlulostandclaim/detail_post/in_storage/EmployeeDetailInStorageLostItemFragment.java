package com.example.tlulostandclaim.detail_post.in_storage;

// Import các thư viện cần thiết
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tlulostandclaim.data.enum_model.EnumInStorageItemStatus;
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.databinding.FragmentEmployeeDetailInStorageBinding;
import com.example.tlulostandclaim.ui.student.add_post.adapter.ImageAdapter;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDetailInStorageLostItemFragment extends Fragment {

    // Binding để kết nối với layout XML
    private FragmentEmployeeDetailInStorageBinding binding;

    // Controller để điều hướng giữa các màn hình
    private NavController navController;

    // Adapter hiển thị danh sách hình ảnh
    private ImageAdapter imageAdapter;

    // Danh sách hình ảnh
    private List<Object> list = new ArrayList<>();

    // ViewModel quản lý logic và dữ liệu
    private EmployeeDetailInStorageItemViewModel viewModel;

    // Adapter cho Spinner chọn danh mục
    private ArrayAdapter<String> spinnerAdapter;

    // Nhận tham số từ màn hình trước (id bài đăng)
    private EmployeeDetailInStorageLostItemFragmentArgs navArgs;

    // Danh sách danh mục
    private List<String> categories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Lấy các tham số truyền vào từ màn trước
        navArgs = EmployeeDetailInStorageLostItemFragmentArgs.fromBundle(requireArguments());

        // Ánh xạ layout vào fragment
        binding = FragmentEmployeeDetailInStorageBinding.inflate(inflater, container, false);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(EmployeeDetailInStorageItemViewModel.class);

        // Khởi tạo adapter hình ảnh
        imageAdapter = new ImageAdapter(list);

        // Lấy danh sách danh mục từ Firestore
        viewModel.getCategories();

        // Lấy chi tiết bài đăng
        viewModel.getItem(navArgs.getPostId());

        // Tạo adapter cho Spinner danh mục
        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Khởi tạo NavController
        navController = NavHostFragment.findNavController(EmployeeDetailInStorageLostItemFragment.this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Nút quay lại
        binding.toolbarDetailPost.imageBack.setOnClickListener(v -> navController.navigateUp());

        // Set tiêu đề toolbar
        binding.toolbarDetailPost.textToolbarTitle.setText("Chi tiết");

        // Set layout cho danh sách hình ảnh theo chiều ngang
        binding.listOfImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfImages.setAdapter(imageAdapter);

        // Set adapter cho Spinner danh mục
        binding.inputType.setAdapter(spinnerAdapter);

        // Bắt sự kiện chọn ảnh mới
        binding.textEditImages.setOnClickListener(v -> isPhotoPickerAvailable());

        // Bắt sự kiện lưu chỉnh sửa bài đăng
        binding.textSaveChange.setOnClickListener(v -> {
            String title = binding.inputName.getText().toString();
            String description = binding.inputDescription.getText().toString();
            String category = categories.get(binding.inputType.getSelectedItemPosition());

            // Nếu chưa chọn danh mục thì để mặc định "Khác"
            if (category.isEmpty()) {
                category = "Khác";
            }

            // Kiểm tra dữ liệu đã nhập
            if (title.isEmpty() || description.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
                // Gửi dữ liệu cập nhật lên Firestore
                viewModel.updateItem(new InStorageLostItemModel(
                                navArgs.getPostId(),
                                title,
                                description,
                                System.currentTimeMillis(),
                                viewModel.getItemsResponse().getValue().getImages(),
                                category,
                                viewModel.getItemsResponse().getValue().getStatus())
                        , false);
            }
        });

        // Xử lý nút cập nhật trạng thái (hoàn thành)
        binding.btnUpdate.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.updateItem(viewModel.getItemsResponse().getValue(), true);
        });

        // Xử lý nút xóa bài đăng
        binding.btnDelete.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.deleteItem(navArgs.getPostId());
        });

        // Theo dõi dữ liệu thay đổi
        observeData();
    }

    // Theo dõi và cập nhật dữ liệu lên giao diện
    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {

        // Quan sát danh sách danh mục
        viewModel.listOfCategory().observe(getViewLifecycleOwner(), s -> {
            categories.clear();
            categories.addAll(s);
            spinnerAdapter.notifyDataSetChanged();
        });

        // Quan sát kết quả xóa bài đăng
        viewModel.deletePostResponse().observe(getViewLifecycleOwner(), s -> {
            binding.progressBar.setVisibility(View.GONE);
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                navController.navigateUp();
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });

        // Quan sát kết quả cập nhật bài đăng
        viewModel.updatePostResponse().observe(getViewLifecycleOwner(), s -> {
            binding.progressBar.setVisibility(View.GONE);
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                // Load lại dữ liệu mới
                viewModel.getItem(navArgs.getPostId());
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });

        // Quan sát chi tiết bài đăng
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), s -> {
            binding.inputReleaseDate.setText(GlobalFunction.formatDateTimeFromMillisecond(s.getCreatedDate()));
            binding.inputDescription.setText(s.getDescription());
            binding.inputName.setText(s.getTitle());

            // Nếu đã hoàn thành thì ẩn nút cập nhật trạng thái
            if (s.getStatus() == EnumInStorageItemStatus.DONE.value) {
                binding.btnUpdate.setVisibility(View.GONE);
            } else {
                binding.btnUpdate.setVisibility(View.VISIBLE);
            }

            // Hiển thị danh sách ảnh
            list.clear();
            list.addAll(s.getImages());
            imageAdapter.notifyDataSetChanged();
        });
    }

    // Launcher để chọn nhiều ảnh từ thiết bị
    @SuppressLint("NotifyDataSetChanged")
    private ActivityResultLauncher<String> pickImageGetContent = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uri -> {
        if (!uri.isEmpty()) {
            viewModel.listOfImages.clear();
            viewModel.listOfImages.addAll(uri);
            list.clear();
            list.addAll(uri);
            imageAdapter.notifyDataSetChanged();
        } else {
            GlobalFunction.showToastMessage(requireContext(), "Chưa chọn ảnh nào!");
        }
    });

    // Mở trình chọn ảnh
    private void isPhotoPickerAvailable() {
        pickImageGetContent.launch("image/*");
    }
}
