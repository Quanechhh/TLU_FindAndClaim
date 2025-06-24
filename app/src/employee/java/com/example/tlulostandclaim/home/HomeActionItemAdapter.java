// Khai báo package
package com.example.tlulostandclaim.home;

// Import các thư viện cần thiết
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlulostandclaim.databinding.LayoutImageActionsBinding;

import java.util.List;

// Adapter cho RecyclerView hiển thị danh sách các hành động trong giao diện nhân viên
public class HomeActionItemAdapter extends RecyclerView.Adapter<HomeActionItemAdapter.ViewHolder> {

    private List<Integer> list; // Danh sách resource ID của các hình ảnh hành động
    private OnActionsChosen onActionsChosen; // Interface callback để xử lý khi hành động được chọn

    // Constructor khởi tạo adapter với dữ liệu và sự kiện click
    public HomeActionItemAdapter(List<Integer> list, OnActionsChosen onActionsChosen) {
        this.list = list;
        this.onActionsChosen = onActionsChosen;
    }

    // Hàm tạo ViewHolder cho mỗi item trong danh sách
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item (layout chứa ảnh hành động)
        return new ViewHolder(LayoutImageActionsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    // Hàm gán dữ liệu cho ViewHolder tại vị trí position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list.get(position)); // Truyền resource ID ảnh vào để hiển thị
    }

    // Trả về số lượng phần tử trong danh sách
    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder quản lý mỗi item trong RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutImageActionsBinding binding; // Binding layout item

        public ViewHolder(LayoutImageActionsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Gán dữ liệu (ảnh) và xử lý sự kiện click cho item
        void bindData(int data) {
            binding.imageStorageManagement.setImageResource(data); // Hiển thị ảnh theo resource ID
            binding.getRoot().setOnClickListener(v -> onActionsChosen.onAction(data)); // Xử lý khi item được click, gọi callback
        }
    }

    // Interface để xử lý sự kiện khi chọn một hành động
    public interface OnActionsChosen {
        void onAction(int data); // Truyền resource ID của hành động được chọn
    }
}
