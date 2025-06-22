package com.example.tlulostandclaim.ui.student.change_password;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tlulostandclaim.databinding.FragmentChangePasswordBinding;
import com.example.tlulostandclaim.utils.GlobalData;
import com.example.tlulostandclaim.utils.GlobalFunction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class ChangePasswordFragment extends Fragment {

    private NavController navController;
    private FragmentChangePasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        navController = NavHostFragment.findNavController(ChangePasswordFragment.this);
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageBack.setOnClickListener(v -> navController.navigateUp());
        binding.btnSavePassword.setOnClickListener(v -> {
            String oldPassword = binding.inputPassword.getText().toString();
            String newPassword = binding.inputNewPassword.getText().toString();
            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                GlobalFunction.showToastMessage(requireContext(), GlobalData.needToFillAllFields);
            } else {
                checkIfPasswordIsCorrect(GlobalData.user.getEmail(), oldPassword, newPassword);
            }
        });
    }

    void checkIfPasswordIsCorrect(String email, String password, String newPassword) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).reauthenticate(credential).addOnCompleteListener((OnCompleteListener<Void>) task -> {
            if (task.isSuccessful()) {
                changePassword(newPassword);
            } else {
                GlobalFunction.showToastMessage(requireContext(), "Sai mật khẩu cũ");
            }
        });
    }


    void changePassword(String password) {
        try {
            FirebaseAuth.getInstance().getCurrentUser().updatePassword(password);
            GlobalFunction.showToastMessage(requireContext(), "Đổi mật khẩu thành công");
        } catch (Exception ex) {
            GlobalFunction.showToastMessage(requireContext(), "Đổi mật khẩu thất bại, vui lòng thử lại!");
        }
    }

}