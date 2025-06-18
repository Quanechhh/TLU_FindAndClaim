package com.example.tlulostandclaim.search_post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClientSearchPostsViewModel extends ViewModel {
    // LiveData chứa danh sách bài đăng thất lạc đã lọc hoặc tất cả
    private MutableLiveData<List<LostItemModel>> _itemsResponse = new MutableLiveData<>();

    // Hàm trả về LiveData để UI quan sát dữ liệu thay đổi
    public LiveData<List<LostItemModel>> getItemsResponse() {
        return _itemsResponse;
    }

    // Danh sách các bộ lọc theo category (chưa dùng trong code hiện tại)
    private List<String> filterCategory = new ArrayList<>();

    // Lưu dữ liệu gốc ban đầu để tìm kiếm hoặc lọc không làm mất dữ liệu gốc
    private List<LostItemModel> originalLostItemResponse = new ArrayList<>();

    // LiveData chứa danh sách các category lấy từ Firebase
    private MutableLiveData<List<CategoryModel>> _listOfCategory = new MutableLiveData<>();

    // Trả về LiveData danh sách category để UI quan sát
    public LiveData<List<CategoryModel>> listOfCategory() {
        return _listOfCategory;
    }

    // Biến lưu category đã chọn để lọc bài đăng
    public CategoryModel chosenCategoryFilter;

    // Xóa bộ lọc category đã chọn (reset về null)
    public void clearCategory() {
        chosenCategoryFilter = null;
    }

    // Lấy tất cả bài đăng thất lạc từ Firestore
    void getItems() {
        GlobalData.firebaseDB
                .collection("lost_items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<LostItemModel> list = new ArrayList<>();
                        // Duyệt qua từng tài liệu trả về từ Firestore
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            // Chuyển dữ liệu thành đối tượng LostItemModel
                            LostItemModel model = documentSnapshot.toObject(LostItemModel.class);
                            list.add(model);
                        }
                        // Lưu danh sách gốc
                        originalLostItemResponse.clear();
                        originalLostItemResponse.addAll(list);
                        // Cập nhật dữ liệu LiveData để UI hiển thị
                        _itemsResponse.setValue(list);
                    }
                });
    }

    // Tìm kiếm bài đăng theo tên (chuỗi name)
    void searchItems(String name) {
        List<LostItemModel> list = new ArrayList<>();
        for (LostItemModel i : originalLostItemResponse) {
            if (chosenCategoryFilter == null) {
                // Nếu chưa chọn category thì chỉ kiểm tra tên chứa chuỗi tìm kiếm
                if (i.getName().contains(name)) {
                    list.add(i);
                }
            } else {
                // Nếu đã chọn category thì kiểm tra cả tên và category
                if (i.getName().contains(name) && i.getCategory().equals(chosenCategoryFilter.getName())) {
                    list.add(i);
                }
            }
        }
        // Cập nhật danh sách sau khi tìm kiếm
        _itemsResponse.setValue(list);
    }

    // Chọn category để lọc bài đăng
    public void chooseCategoryFilter(CategoryModel model) {
        chosenCategoryFilter = model;
        List<LostItemModel> list = new ArrayList<>();
        for (LostItemModel i : originalLostItemResponse) {
            if (chosenCategoryFilter != null) {
                // Lọc bài đăng theo category được chọn
                if (i.getCategory().equals(chosenCategoryFilter.getName())) {
                    list.add(i);
                }
            }
        }
        // Nếu có bài đăng phù hợp thì cập nhật LiveData
        if (!list.isEmpty()) _itemsResponse.setValue(list);
    }

    // Lấy danh sách category từ Firestore
    void getCategory() {
        GlobalData.firebaseDB
                .collection("category")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<CategoryModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            // Chuyển từng document thành CategoryModel
                            CategoryModel model = documentSnapshot.toObject(CategoryModel.class);
                            list.add(model);
                        }
                        // Cập nhật danh sách category cho UI
                        _listOfCategory.setValue(list);
                    }
                });
    }
}
