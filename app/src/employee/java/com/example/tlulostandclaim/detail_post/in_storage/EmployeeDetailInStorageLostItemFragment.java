package com.example.tlulostandclaim.detail_post.in_storage;

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
import com.example.tlulostandclaim.databinding.FragmentDetailInStorageLostItemBinding;
import com.example.tlulostandclaim.databinding.FragmentEmployeeDetailInStorageBinding;
import com.example.tlulostandclaim.ui.student.add_post.adapter.ImageAdapter;
import com.example.tlulostandclaim.ui.student.detail_post.in_storage.DetailInStorageLostItemFragmentArgs;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDetailInStorageLostItemFragment extends Fragment {

    private FragmentEmployeeDetailInStorageBinding binding;
    private NavController navController;
    private ImageAdapter imageAdapter;
    private List<Object> list = new ArrayList<>();
    private EmployeeDetailInStorageItemViewModel viewModel;
    private ArrayAdapter<String> spinnerAdapter;
    private EmployeeDetailInStorageLostItemFragmentArgs navArgs;
    private List<String> categories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navArgs = EmployeeDetailInStorageLostItemFragmentArgs.fromBundle(requireArguments());
        binding = FragmentEmployeeDetailInStorageBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(EmployeeDetailInStorageItemViewModel.class);
        imageAdapter = new ImageAdapter(list);
        viewModel.getCategories();
        viewModel.getItem(navArgs.getPostId());
        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        navController = NavHostFragment.findNavController(EmployeeDetailInStorageLostItemFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarDetailPost.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.toolbarDetailPost.textToolbarTitle.setText("Chi tiết");
        binding.listOfImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfImages.setAdapter(imageAdapter);
        binding.inputType.setAdapter(spinnerAdapter);
        binding.textEditImages.setOnClickListener(v -> isPhotoPickerAvailable());
        binding.textSaveChange.setOnClickListener(v -> {
            String title = binding.inputName.getText().toString();
            String description = binding.inputDescription.getText().toString();
            String category = categories.get(binding.inputType.getSelectedItemPosition());
            if (category.isEmpty()) {
                category = "Khác";
            }
            if (title.isEmpty() || description.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
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
        binding.btnUpdate.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.updateItem(viewModel.getItemsResponse().getValue(), true);
        });
        binding.btnDelete.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.deleteItem(navArgs.getPostId());
        });
        observeData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        viewModel.listOfCategory().observe(getViewLifecycleOwner(), s -> {
            categories.clear();
            categories.addAll(s);
            spinnerAdapter.notifyDataSetChanged();
        });
        viewModel.deletePostResponse().observe(getViewLifecycleOwner(), s -> {
            binding.progressBar.setVisibility(View.GONE);
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                navController.navigateUp();
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });
        viewModel.updatePostResponse().observe(getViewLifecycleOwner(), s -> {
            binding.progressBar.setVisibility(View.GONE);
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                viewModel.getItem(navArgs.getPostId());
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), s -> {
            binding.inputReleaseDate.setText(GlobalFunction.formatDateTimeFromMillisecond(s.getCreatedDate()));
            binding.inputDescription.setText(s.getDescription());
            binding.inputName.setText(s.getTitle());
            if (s.getStatus() == EnumInStorageItemStatus.DONE.value) {
                binding.btnUpdate.setVisibility(View.GONE);
            } else {
                binding.btnUpdate.setVisibility(View.VISIBLE);
            }
            list.clear();
            list.addAll(s.getImages());
            imageAdapter.notifyDataSetChanged();
        });
    }

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

    private void isPhotoPickerAvailable() {
        pickImageGetContent.launch("image/*");
    }
}