package com.example.tlulostandclaim.in_storage_item_management;

// Các import cần thiết
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.databinding.FragmentInStorageItemManagementBinding;
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.ui.student.main_home.adapter.InStorageLostItemAdapter;

import java.util.ArrayList;
import java.util.List;

// Fragment quản lý danh sách các món đồ bị thất lạc đang lưu trữ
public class InStorageItemManagementFragment extends Fragment implements InStorageLostItemAdapter.InStorageLostItemInteract {

    // Binding để truy cập layout qua ViewBinding
    private FragmentInStorageItemManagementBinding binding;

    // Dùng để điều hướng giữa các màn hình trong Navigation Component
    private NavController navController;

    // Adapter để hiển thị danh sách đồ thất lạc
    private InStorageLostItemAdapter lostItemAdapter;

    // ViewModel chứa logic và dữ liệu cho Fragment
    private InStorageItemManagementViewModel viewModel;

    // Danh sách dữ liệu các món đồ bị thất lạc trong kho
    private List<InStorageLostItemModel> lostItemModels = new ArrayList<>();

    // Handler để delay tìm kiếm (debounce)
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Runnable sẽ thực hiện tìm kiếm sau delay
    private Runnable workRunnable;

    // Tạo View cho Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo binding
        binding = FragmentInStorageItemManagementBinding.inflate(inflater, container, false);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(InStorageItemManagementViewModel.class);

        // Gọi hàm lấy danh sách item ban đầu
        viewModel.getItems();

        // Lấy NavController để điều hướng
        navController = NavHostFragment.findNavController(InStorageItemManagementFragment.this);

        // Khởi tạo adapter với danh sách rỗng và listener là chính fragment này
        lostItemAdapter = new InStorageLostItemAdapter(lostItemModels, this);

        // Trả về view root để hiển thị
        return binding.getRoot();
    }

    // Được gọi sau khi View đã được tạo xong
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gán sự kiện nút quay lại toolbar
        binding.toolbarSearch.imageBack.setOnClickListener(v -> navController.navigateUp());

        // Thiết lập tiêu đề cho toolbar
        binding.toolbarSearch.textToolbarTitle.setText("Quản lý đồ lưu trữ");

        // Cài đặt layout dạng danh sách dọc cho RecyclerView
        binding.listOfItems.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Gắn adapter vào RecyclerView
        binding.listOfItems.setAdapter(lostItemAdapter);

        // Lắng nghe sự thay đổi văn bản trong ô tìm kiếm
        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không xử lý gì trước khi text thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nếu có runnable cũ, hủy bỏ để tránh chạy dư
                if (workRunnable != null) handler.removeCallbacks(workRunnable);

                // Tạo runnable mới để thực hiện tìm kiếm sau 300ms
                workRunnable = () -> performSearch(s.toString());

                // Delay 300ms để tránh gọi API liên tục khi người dùng gõ
                handler.postDelayed(workRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không xử lý gì sau khi text thay đổi
            }
        });

        // Bắt đầu lắng nghe dữ liệu từ ViewModel
        observeData();
    }

    // Quan sát dữ liệu từ ViewModel và cập nhật UI
    @SuppressLint("NotifyDataSetChanged") // Chấp nhận gọi notifyDataSetChanged không tối ưu
    private void observeData() {
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), l -> {
            // Xóa danh sách cũ
            lostItemModels.clear();

            // Thêm dữ liệu mới vào danh sách
            lostItemModels.addAll(l);

            // Cập nhật lại giao diện
            lostItemAdapter.notifyDataSetChanged();
        });
    }

    // Gọi ViewModel thực hiện tìm kiếm theo chuỗi text
    private void performSearch(String text) {
        viewModel.searchItems(text);
    }

    // Callback khi người dùng chọn một item trong danh sách
    @Override
    public void onChosenAction(InStorageLostItemModel model) {
        // Dùng SafeArgs để điều hướng đến màn hình chi tiết với ID tương ứng
        navController.navigate(
                InStorageItemManagementFragmentDirections
                        .actionInStorageItemManagementFragmentToEmployeeDetailInStorageLostItemFragment(model.getId())
        );
    }
}
