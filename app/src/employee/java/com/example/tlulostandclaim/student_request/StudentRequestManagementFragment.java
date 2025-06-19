package com.example.tlulostandclaim.student_request;

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

import com.example.tlulostandclaim.databinding.FragmentStudentRequestManagementBinding;
import com.example.tlulostandclaim.data.model.RequestLostItemModel;

import java.util.ArrayList;
import java.util.List;
public class StudentRequestManagementFragment extends Fragment implements StudentRequestItemAdapter.StudentRequestInteract {

    private FragmentStudentRequestManagementBinding binding;
    private NavController navController;
    private StudentRequestManagementViewModel viewModel;
    private List<RequestLostItemModel> lostItemModels = new ArrayList<>();
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

    @Override
    public void onChosen(RequestLostItemModel item) {
        navController.navigate(StudentRequestManagementFragmentDirections
                .actionStudentRequestManagementFragmentToDetailStudentRequestFragment(item.getId()));
    }
}
