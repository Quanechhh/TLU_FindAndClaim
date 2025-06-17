package com.example.tlulostandclaim.in_storage_item_management;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel dùng để quản lý dữ liệu danh sách các món đồ thất lạc đang lưu kho.
 * Chứa logic để lấy dữ liệu từ Firestore và tìm kiếm theo tiêu đề.
 */
public class InStorageItemManagementViewModel extends ViewModel {

    // LiveData chứa danh sách các món đồ được hiển thị cho UI
    private MutableLiveData<List<InStorageLostItemModel>> _itemsResponse = new MutableLiveData<>();

    // Public getter cho LiveData (để Fragment hoặc Activity có thể quan sát dữ liệu)
    public LiveData<List<InStorageLostItemModel>> getItemsResponse() {
        return _itemsResponse;
    }

    // Danh sách dùng để lọc hoặc tìm kiếm (giữ dữ liệu gốc từ Firestore)
    private List<InStorageLostItemModel> originalLostItemResponse = new ArrayList<>();

    // Danh sách danh mục để lọc (chưa sử dụng nhưng có thể mở rộng sau)
    private List<String> filterCategory = new ArrayList<>();

    /**
     * Gọi tới Firestore để lấy dữ liệu từ collection "in_storage",
     * sau đó đổ dữ liệu vào LiveData và danh sách gốc.
     */
    void getItems() {
        GlobalData.firebaseDB
                .collection("in_storage")  // Collection trong Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<InStorageLostItemModel> list = new ArrayList<>();
                        // Lặp qua từng document trong kết quả
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            InStorageLostItemModel model = documentSnapshot.toObject(InStorageLostItemModel.class);
                            list.add(model);  // Thêm vào danh sách
                        }
                        // Cập nhật danh sách gốc và LiveData
                        originalLostItemResponse.clear();
                        originalLostItemResponse.addAll(list);
                        _itemsResponse.setValue(list);
                    }
                });
    }

    /**
     * Hàm tìm kiếm đơn giản: lọc danh sách dựa trên tiêu đề chứa từ khóa `name`.
     * Kết quả lọc được cập nhật vào LiveData.
     *
     * @param name từ khóa tìm kiếm
     */
    void searchItems(String name) {
        List<InStorageLostItemModel> list = new ArrayList<>();
        for (InStorageLostItemModel i : originalLostItemResponse) {
            if (i.getTitle().contains(name)) {
                list.add(i);
            }
        }
        _itemsResponse.setValue(list);  // Cập nhật kết quả tìm kiếm
    }
}
