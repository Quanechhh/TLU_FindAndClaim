package com.example.tlulostandclaim.ui.student.detail_post;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tlulostandclaim.data.enum_model.EnumPostStatus;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.FragmentDetailPostBinding;
import com.example.tlulostandclaim.ui.student.add_post.adapter.ImageAdapter;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class DetailPostFragment extends Fragment {
    private FragmentDetailPostBinding binding;
    private NavController navController;
    private ImageAdapter imageAdapter;
    private List<Object> list = new ArrayList<>();
    private DetailPostViewModel viewModel;
    private DetailPostFragmentArgs navArgs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navArgs = DetailPostFragmentArgs.fromBundle(requireArguments());
        binding = FragmentDetailPostBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DetailPostViewModel.class);
        imageAdapter = new ImageAdapter(list);
        viewModel.getItem(navArgs.getPostId());
        navController = NavHostFragment.findNavController(DetailPostFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarDetailPost.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.toolbarDetailPost.textToolbarTitle.setText("Chi tiết");
        binding.listOfImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfImages.setAdapter(imageAdapter);
        binding.btnDelete.setOnClickListener(v -> viewModel.deleteItem(navArgs.getPostId()));
        binding.textEditImages.setOnClickListener(v -> isPhotoPickerAvailable());
        observeData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), s -> {
            if (s.getUserId().equals(GlobalData.user.getId())) {
                if (s.getStatus() == EnumPostStatus.DONE.value) {
                    binding.inputReleaseDate.setEnabled(false);
                    binding.inputReleaseDate.setFocusable(false);
                    binding.inputDescription.setEnabled(false);
                    binding.inputDescription.setFocusable(false);
                    binding.inputName.setEnabled(false);
                    binding.inputName.setFocusable(false);
                    binding.inputLostLocation.setEnabled(false);
                    binding.inputLostLocation.setFocusable(false);
                    binding.inputContactInfo.setEnabled(false);
                    binding.inputContactInfo.setFocusable(false);
                    binding.btnSave.setVisibility(View.GONE);
                } else {
                    binding.btnSave.setVisibility(View.VISIBLE);
                    binding.btnSave.setOnClickListener(v -> {
                        String title = binding.inputName.getText().toString();
                        String description = binding.inputDescription.getText().toString();
                        String date = binding.inputReleaseDate.getText().toString();
                        String place = binding.inputLostLocation.getText().toString();
                        String contact = binding.inputContactInfo.getText().toString();
                        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || place.isEmpty() || contact.isEmpty()) {
                            GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
                        } else {
                            LostItemModel itemModel = new LostItemModel(s.getId(), title, s.getCategory(), description, s.getImage(), s.getCreatedAt(), date, place, contact, s.getUserId(), s.getStatus());
                            viewModel.updatePost(itemModel);
                        }
                    });
                }
            } else {
                binding.inputReleaseDate.setEnabled(false);
                binding.inputReleaseDate.setFocusable(false);
                binding.inputDescription.setEnabled(false);
                binding.inputDescription.setFocusable(false);
                binding.inputName.setEnabled(false);
                binding.inputName.setFocusable(false);
                binding.inputLostLocation.setEnabled(false);
                binding.inputLostLocation.setFocusable(false);
                binding.inputContactInfo.setEnabled(false);
                binding.inputContactInfo.setFocusable(false);
                binding.btnSave.setVisibility(View.GONE);
            }
            binding.inputReleaseDate.setText(s.getLostDate());
            binding.inputDescription.setText(s.getDescription());
            binding.inputName.setText(s.getName());
            binding.inputLostLocation.setText(s.getLostLocation());
            binding.inputContactInfo.setText(s.getContactInfo());
            list.clear();
            list.addAll(s.getImage());
            imageAdapter.notifyDataSetChanged();
        });

        viewModel.addPostResponse().observe(getViewLifecycleOwner(), s -> {
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                viewModel.getItem(navArgs.getPostId());
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });

        viewModel.deletePostResponse().observe(getViewLifecycleOwner(), s -> {
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                navController.navigateUp();
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });

        viewModel.getRequestResponse().observe(getViewLifecycleOwner(), s -> {
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
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