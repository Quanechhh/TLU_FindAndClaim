// Khai báo package của class
package com.example.tlulostandclaim;

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

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.FragmentHomeClientBinding;
import com.example.tlulostandclaim.ui.student.main_home.MainHomeFragmentDirections;
import com.example.tlulostandclaim.ui.student.main_home.adapter.LostItemAdapter;
import com.example.tlulostandclaim.utils.GlobalData;

import java.util.ArrayList;

// Định nghĩa Fragment dành cho nhân viên
public class HomeEmployeeFragment extends Fragment implements LostItemAdapter.LostItemInteract, HomeActionItemAdapter.OnActionsChosen {

    // Khai báo các biến thành phần
    private NavController navController; // Điều hướng giữa các Fragment
    private FragmentHomeClientBinding binding; // View binding cho Fragment
    private HomeEmployeeFragmentViewModel viewModel; // ViewModel để xử lý logic dữ liệu
    private LostItemAdapter lostItemAdapter; // Adapter danh sách các vật bị mất
    private HomeActionItemAdapter homeActionItemAdapter; // Adapter danh sách các hành động
    private ArrayList<LostItemModel> lostItemModelArrayList; // Danh sách các vật bị mất
    private ArrayList<Integer> actionsList = new ArrayList<>(); // Danh sách các hành động (theo icon)

    // Hàm khởi tạo giao diện Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Khởi tạo binding và viewModel
        binding = FragmentHomeClientBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HomeEmployeeFragmentViewModel.class);

        // Khởi tạo danh sách vật bị mất và danh sách hành động
        lostItemModelArrayList = new ArrayList<>();
        actionsList.clear();
        actionsList.add(R.drawable.image_check_in_lost_item); // Hành động check-in
        actionsList.add(R.drawable.image_storage_manage); // Quản lý kho
        actionsList.add(R.drawable.image_request_item); // Quản lý yêu cầu

        // Khởi tạo adapter
        lostItemAdapter = new LostItemAdapter(lostItemModelArrayList, this);
        homeActionItemAdapter = new HomeActionItemAdapter(actionsList, this);

        // Khởi tạo NavController
        navController = NavHostFragment.findNavController(HomeEmployeeFragment.this);

        return binding.getRoot(); // Trả về view đã binding
    }

    // Hàm được gọi sau khi view đã được tạo
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gọi API lấy danh sách vật bị mất
        viewModel.getItems();

        // Hiển thị lời chào người dùng
        if (GlobalData.user != null) {
            binding.textHelloUser.setText("Chào " + GlobalData.user.getFullName() + ",");
        }

        // Set adapter cho danh sách hành động
        binding.listOfActions.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfActions.setAdapter(homeActionItemAdapter);
        homeActionItemAdapter.notifyDataSetChanged();

        // Set sự kiện click cho ô tìm kiếm
        binding.inputSearch.setFocusable(false); // Không cho nhập trực tiếp
        binding.inputSearch.setOnClickListener(v -> navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToSearchPostsFragment()));

        // Set adapter cho danh sách vật bị mất
        binding.listOfItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfItems.setAdapter(lostItemAdapter);

        // Set sự kiện click cho nút thêm bài viết
        binding.iconAddPost.setOnClickListener(v -> navController.navigate(MainHomeFragmentDirections.actionMainHomeFragmentToAddLostItemPostFragment()));

        // Bắt đầu lắng nghe dữ liệu
        observeData();
    }

    // Hàm quan sát dữ liệu từ ViewModel
    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                // Cập nhật danh sách khi có dữ liệu mới
                lostItemModelArrayList.clear();
                lostItemModelArrayList.addAll(list);
                lostItemAdapter.notifyDataSetChanged();
            }
        });
    }

    // Hàm xử lý khi chọn 1 vật bị mất (mở chi tiết bài viết)
    @Override
    public void onChosenAction(LostItemModel model) {
        navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToClientDetailPostFragment(model.getId()));
    }

    // Hàm xử lý khi chọn một hành động
    @Override
    public void onAction(int data) {
        if (data == actionsList.get(0)) {
            // Nếu chọn hành động check-in vật bị mất
            navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToAddLostItemFragment());
        } else if (data == actionsList.get(1)) {
            // Nếu chọn quản lý kho
            navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToInStorageItemManagementFragment());
        } else {
            // Nếu chọn quản lý yêu cầu
            navController.navigate(HomeEmployeeFragmentDirections.actionHomeClientFragment2ToStudentRequestManagementFragment());
        }
    }
}
