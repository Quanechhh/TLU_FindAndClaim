package com.example.tlulostandclaim.ui.user;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.databinding.LayoutAdminUserItemBinding;

import java.util.List;

public class AdminUserManagementAdapter extends RecyclerView.Adapter<AdminUserManagementAdapter.ViewHolder> {

    private List<User> userList;
    private AdminUserManagementInteract interact;

    public AdminUserManagementAdapter(List<User> userList, AdminUserManagementInteract interact) {
        this.userList = userList;
        this.interact = interact;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutAdminUserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutAdminUserItemBinding binding;

        public ViewHolder(LayoutAdminUserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(User user) {
            binding.textUserName.setText(user.getFullName());
            binding.textUserPhone.setText(user.getMobilePhone());
            binding.textUserEmail.setText(user.getEmail());
            if (user.getRole() == 1) {
                binding.textUserRole.setText("Nhân viên");
            } else if (user.getRole() == 2) {
                binding.textUserRole.setText("Sinh viên");
            } else {
                binding.textUserRole.setText("Admin");
            }
            binding.getRoot().setOnClickListener(v -> interact.onUserChosen(user));
        }
    }

    public interface AdminUserManagementInteract {
        void onUserChosen(User user);
    }
}
