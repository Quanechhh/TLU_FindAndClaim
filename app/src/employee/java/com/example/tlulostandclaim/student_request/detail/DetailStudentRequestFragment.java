package com.example.tlulostandclaim.student_request.detail;

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

import com.example.tlulostandclaim.databinding.FragmentDetailStudentRequestBinding;
import com.example.tlulostandclaim.student_request.StudentRequestManagementViewModel;
import com.example.tlulostandclaim.ui.student.add_post.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;
public class DetailStudentRequestFragment extends Fragment {
    private FragmentDetailStudentRequestBinding binding;
    private NavController navController;
    private StudentRequestManagementViewModel viewModel;
    private ImageAdapter imageAdapter;
    private List<Object> list = new ArrayList<>();
    private DetailStudentRequestFragmentArgs navArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailStudentRequestBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(StudentRequestManagementViewModel.class);
        navArgs = DetailStudentRequestFragmentArgs.fromBundle(requireArguments());
        imageAdapter = new ImageAdapter(list);
        viewModel.getItem(navArgs.getPostId());
        navController = NavHostFragment.findNavController(DetailStudentRequestFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarDetailPost.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.toolbarDetailPost.textToolbarTitle.setText("Chi tiết yêu cầu");
        binding.listOfImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfImages.setAdapter(imageAdapter);
        observeData();
    }

    private void observeData() {
        viewModel.getItemResponse().observe(getViewLifecycleOwner(), s -> {
            binding.inputReleaseDate.setText(String.valueOf(s.getCreatedAt())); // dùng tạm, format sẽ ở commit 2
            binding.inputName.setText(s.getItemName());
            binding.inputUserName.setText(s.getUserName());
            binding.inputPhone.setText(s.getUserPhone());
            list.clear();
            list.addAll(s.getItemImage());
            imageAdapter.notifyDataSetChanged();
        });
    }
}
