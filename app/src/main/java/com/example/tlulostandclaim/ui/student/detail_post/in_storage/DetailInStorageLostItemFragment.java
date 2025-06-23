package com.example.tlulostandclaim.ui.student.detail_post.in_storage;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.data.enum_model.EnumPostStatus;
import com.example.tlulostandclaim.databinding.FragmentDetailInStorageLostItemBinding;
import com.example.tlulostandclaim.ui.student.add_post.adapter.ImageAdapter;
import com.example.tlulostandclaim.ui.student.detail_post.DetailPostViewModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class DetailInStorageLostItemFragment extends Fragment {

    private FragmentDetailInStorageLostItemBinding binding;
    private NavController navController;
    private ImageAdapter imageAdapter;
    private List<Object> list = new ArrayList<>();
    private DetailInStorageItemViewModel viewModel;
    private DetailInStorageLostItemFragmentArgs navArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navArgs = DetailInStorageLostItemFragmentArgs.fromBundle(requireArguments());
        binding = FragmentDetailInStorageLostItemBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DetailInStorageItemViewModel.class);
        imageAdapter = new ImageAdapter(list);
        viewModel.getItem(navArgs.getPostId());
        navController = NavHostFragment.findNavController(DetailInStorageLostItemFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarDetailPost.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.toolbarDetailPost.textToolbarTitle.setText("Chi tiết");
        binding.listOfImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfImages.setAdapter(imageAdapter);
        binding.btnSave.setOnClickListener(v -> viewModel.sendRequest());
        observeData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), s -> {
            binding.inputReleaseDate.setText(GlobalFunction.formatDateTimeFromMillisecond(s.getCreatedDate()));
            binding.inputDescription.setText(s.getDescription());
            binding.inputName.setText(s.getTitle());
            list.clear();
            list.addAll(s.getImages());
            imageAdapter.notifyDataSetChanged();
        });
        viewModel.getRequestResponse().observe(getViewLifecycleOwner(), s -> {
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
//                navController.navigateUp();
            } else {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonError);
            }
        });
    }
}