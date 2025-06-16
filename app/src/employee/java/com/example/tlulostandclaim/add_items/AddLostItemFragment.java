package com.example.tlulostandclaim.add_items;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.enum_model.EnumPostStatus;
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.databinding.FragmentAddLostItemBinding;
import com.example.tlulostandclaim.databinding.FragmentAddLostItemPostBinding;
import com.example.tlulostandclaim.databinding.FragmentClientDetailPostBinding;
import com.example.tlulostandclaim.detail_post.ClientDetailPostFragmentArgs;
import com.example.tlulostandclaim.detail_post.ClientDetailPostViewModel;
import com.example.tlulostandclaim.ui.student.add_post.AddLostItemPostFragment;
import com.example.tlulostandclaim.ui.student.add_post.AddLostPostViewModel;
import com.example.tlulostandclaim.ui.student.add_post.adapter.ImageAdapter;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class AddLostItemFragment extends Fragment {
    private FragmentAddLostItemBinding binding;
    private NavController navController;
    private ImageAdapter imageAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private List<Object> list = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private AddLostItemViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddLostItemBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddLostItemViewModel.class);
        imageAdapter = new ImageAdapter(list);
        navController = NavHostFragment.findNavController(AddLostItemFragment.this);
        viewModel.getCategories();
        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarAddLostPost.textToolbarTitle.setText("Báo cáo mất đồ");
        binding.toolbarAddLostPost.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.cardviewAddImage.setOnClickListener(v -> isPhotoPickerAvailable());
        binding.textEditImages.setOnClickListener(v -> isPhotoPickerAvailable());
        binding.listOfImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.listOfImages.setAdapter(imageAdapter);
        binding.inputType.setAdapter(spinnerAdapter);
        binding.inputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setVisibility(false);
        binding.btnSave.setOnClickListener(v -> {
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
                InStorageLostItemModel lostItemModel = new InStorageLostItemModel(null, title, description, 0L, null, category, 0);
                viewModel.addItem(lostItemModel);
            }
        });
        observeData();
    }

    private void observeData() {
        viewModel.listOfCategory().observe(getViewLifecycleOwner(), s -> {
            categories.clear();
            categories.addAll(s);
            spinnerAdapter.notifyDataSetChanged();
        });
        viewModel.addPostResponse().observe(getViewLifecycleOwner(), s -> {
            binding.progressBar.setVisibility(View.GONE);
            if (s) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.commonSuccess);
                navController.navigateUp();
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
            setVisibility(true);
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

    void setVisibility(boolean isShowListImages) {
        if (isShowListImages) {
            binding.listOfImages.setVisibility(View.VISIBLE);
            binding.textEditImages.setVisibility(View.VISIBLE);
            binding.cardviewAddImage.setVisibility(View.INVISIBLE);
        } else {
            binding.listOfImages.setVisibility(View.INVISIBLE);
            binding.textEditImages.setVisibility(View.INVISIBLE);
            binding.cardviewAddImage.setVisibility(View.VISIBLE);
        }
    }
}