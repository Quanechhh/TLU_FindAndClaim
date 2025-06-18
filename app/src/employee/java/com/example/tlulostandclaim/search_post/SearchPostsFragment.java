package com.example.tlulostandclaim.search_post;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.FragmentSearchPostsBinding;
import com.example.tlulostandclaim.ui.student.main_home.adapter.LostItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment này hiển thị danh sách các bài đăng thất lạc
 * cho phép người dùng tìm kiếm, lọc và chọn để xem chi tiết.
 */
public class SearchPostsFragment extends Fragment implements LostItemAdapter.LostItemInteract {

    private FragmentSearchPostsBinding binding; // Binding layout
    private NavController navController; // Điều hướng giữa các màn hình
    private LostItemAdapter lostItemAdapter; // Adapter hiển thị danh sách bài đăng
    private ClientSearchPostsViewModel viewModel; // ViewModel chứa logic và dữ liệu
    private List<LostItemModel> lostItemModels = new ArrayList<>(); // Danh sách dữ liệu bài đăng
    private final Handler handler = new Handler(Looper.getMainLooper()); // Handler dùng để delay tìm kiếm
    private Runnable workRunnable; // Biến runnable dùng để debounce khi tìm kiếm

    // Hàm khởi tạo giao diện của Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gắn layout với binding
        binding = FragmentSearchPostsBinding.inflate(inflater, container, false);

        // Khởi tạo ViewModel dùng chung với Activity
        viewModel = new ViewModelProvider(requireActivity()).get(ClientSearchPostsViewModel.class);

        // Gọi API/lấy dữ liệu ban đầu
        viewModel.getItems();

        // Lấy NavController để điều hướng
        navController = NavHostFragment.findNavController(SearchPostsFragment.this);

        // Khởi tạo adapter với danh sách rỗng và gán callback tương tác
        lostItemAdapter = new LostItemAdapter(lostItemModels, this);

        return binding.getRoot(); // Trả về view đã binding
    }

    // Hàm chạy sau khi view đã được tạo xong
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gán sự kiện click cho nút quay lại
        binding.toolbarSearch.imageBack.setOnClickListener(v -> navController.navigateUp());

        // Cập nhật tiêu đề toolbar
        binding.toolbarSearch.textToolbarTitle.setText("Tìm kiếm");

        // Cài đặt RecyclerView hiển thị danh sách các bài đăng
        binding.listOfItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfItems.setAdapter(lostItemAdapter);

        // Mở BottomSheet lọc danh mục khi bấm nút filter
        binding.imageFilter.setOnClickListener(v -> {
            viewModel.clearCategory(); // Xóa bộ lọc trước đó
            navController.navigate(
                    SearchPostsFragmentDirections.actionSearchPostsFragmentToEmployeeFilterBottomSheetFragment()
            );
        });

        // Xử lý sự kiện nhập từ khóa tìm kiếm
        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Hủy bỏ runnable trước nếu người dùng đang gõ tiếp
                if (workRunnable != null) handler.removeCallbacks(workRunnable);

                // Tạo runnable mới để tìm kiếm sau 300ms
                workRunnable = () -> performSearch(s.toString());
                handler.postDelayed(workRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });

        // Bắt đầu quan sát dữ liệu từ ViewModel
        observeData();
    }

    // Quan sát danh sách bài đăng từ ViewModel và cập nhật giao diện
    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), list -> {
            lostItemModels.clear(); // Xóa danh sách cũ
            lostItemModels.addAll(list); // Thêm dữ liệu mới
            lostItemAdapter.notifyDataSetChanged(); // Cập nhật UI
        });
    }

    // Gửi từ khóa tìm kiếm vào ViewModel để lọc dữ liệu
    private void performSearch(String text) {
        viewModel.searchItems(text);
    }

    // Xử lý khi người dùng bấm vào 1 item trong danh sách
    @Override
    public void onChosenAction(LostItemModel model) {
        navController.navigate(
                SearchPostsFragmentDirections.actionSearchPostsFragmentToClientDetailPostFragment(model.getId())
        );
    }
}
