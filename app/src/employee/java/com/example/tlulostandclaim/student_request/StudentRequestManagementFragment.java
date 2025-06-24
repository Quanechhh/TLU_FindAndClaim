package com.example.tlulostandclaim.student_request;

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

import com.example.tlulostandclaim.R;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.data.model.RequestLostItemModel;
import com.example.tlulostandclaim.databinding.FragmentSearchPostsBinding;
import com.example.tlulostandclaim.databinding.FragmentStudentRequestManagementBinding;
import com.example.tlulostandclaim.search_post.ClientSearchPostsViewModel;
import com.example.tlulostandclaim.search_post.SearchPostsFragment;
import com.example.tlulostandclaim.search_post.SearchPostsFragmentDirections;
import com.example.tlulostandclaim.ui.student.main_home.adapter.LostItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class StudentRequestManagementFragment extends Fragment implements StudentRequestItemAdapter.StudentRequestInteract {

    private FragmentStudentRequestManagementBinding binding;
    private NavController navController;
    private StudentRequestManagementViewModel viewModel;
    private List<RequestLostItemModel> lostItemModels = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable workRunnable;
    private StudentRequestItemAdapter studentRequestItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentRequestManagementBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(StudentRequestManagementViewModel.class);
        viewModel.getItems();
        studentRequestItemAdapter = new StudentRequestItemAdapter(lostItemModels, this);
        navController = NavHostFragment.findNavController(StudentRequestManagementFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarSearch.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.toolbarSearch.textToolbarTitle.setText("Quản lý yêu cầu nhận đồ");
        binding.listOfItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listOfItems.setAdapter(studentRequestItemAdapter);
        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (workRunnable != null) handler.removeCallbacks(workRunnable);
                workRunnable = () -> performSearch(s.toString());
                handler.postDelayed(workRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        observeData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        viewModel.getItemsResponse().observe(getViewLifecycleOwner(), l -> {
            lostItemModels.clear();
            lostItemModels.addAll(l);
            studentRequestItemAdapter.notifyDataSetChanged();
        });
    }

    private void performSearch(String text) {
        viewModel.searchItems(text);
    }

    @Override
    public void onChosen(RequestLostItemModel item) {
        navController.navigate(StudentRequestManagementFragmentDirections.actionStudentRequestManagementFragmentToDetailStudentRequestFragment(item.getId()));
    }
}