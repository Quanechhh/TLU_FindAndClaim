package com.example.tlulostandclaim.add_items;

// Import các thư viện cần thiết
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.enum_model.EnumInStorageItemStatus;
import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddLostItemViewModel extends ViewModel {

    // Danh sách URI của các ảnh được chọn
    List<Uri> listOfImages = new ArrayList<>();

    // LiveData lưu danh sách các loại đồ vật
    private MutableLiveData<List<String>> _listOfCategory = new MutableLiveData<>();
    public LiveData<List<String>> listOfCategory() {
        return _listOfCategory;
    }

    // LiveData theo dõi kết quả thêm đồ vật
    private MutableLiveData<Boolean> _addPostResponse = new MutableLiveData<>();
    public LiveData<Boolean> addPostResponse() {
        return _addPostResponse;
    }

    // Lấy danh sách loại đồ vật từ Firebase
    public void getCategories() {
        GlobalData.firebaseDB.collection("category").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> list = new ArrayList<>();
                for (QueryDocumentSnapshot i : task.getResult()) {
                    String model = (i.toObject(CategoryModel.class)).getName(); // Lấy tên loại đồ vật
                    list.add(model);
                }
                _listOfCategory.setValue(list); // Gửi danh sách cho UI
            } else {
                // Xử lý lỗi nếu cần
            }
        });
    }

    // Thêm đồ vật vào kho
    public void addItem(InStorageLostItemModel model) {
        new Thread(() -> { // Thực hiện trên thread nền
            String id = GlobalData.firebaseDB.collection("in_storage").document().getId(); // Tạo ID ngẫu nhiên
            model.setId(id); // Gán ID cho model

            List<String> list = uploadMultipleImages(); // Upload ảnh và lấy link trả về
            model.setImages(list); // Gán danh sách ảnh cho model
            model.setCreatedDate(System.currentTimeMillis()); // Lưu thời gian tạo
            model.setStatus(EnumInStorageItemStatus.WAITING.value); // Trạng thái chờ duyệt

            // Lưu dữ liệu lên Firebase
            GlobalData.firebaseDB.collection("in_storage").document(id).set(model.convertToMap()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    _addPostResponse.setValue(true); // Thêm thành công
                } else {
                    _addPostResponse.setValue(false); // Thêm thất bại
                }
            });
        }).start(); // Bắt đầu thread
    }

    // Hàm upload nhiều ảnh lên Firebase Storage
    private List<String> uploadMultipleImages() {
        try {
            List<String> uploadTasks = new ArrayList<>();
            FirebaseStorage storage = FirebaseStorage.getInstance(); // Khởi tạo Firebase Storage

            for (Uri uri : listOfImages) { // Lặp qua từng ảnh
                String fileName = UUID.randomUUID().toString() + ".jpg"; // Tạo tên file ngẫu nhiên
                StorageReference storageRef = storage.getReference().child("images/" + fileName); // Tạo đường dẫn lưu ảnh
                UploadTask uploadTask = storageRef.putFile(uri); // Upload ảnh lên Firebase Storage
                Tasks.await(uploadTask); // Chờ upload xong
                Uri downloadUri = Tasks.await(storageRef.getDownloadUrl()); // Lấy link download của ảnh
                uploadTasks.add(downloadUri.toString()); // Thêm link vào danh sách
            }
            return uploadTasks; // Trả về danh sách link ảnh
        } catch (Exception e) {
            // Có thể thêm log lỗi nếu cần
        }
        return new ArrayList<String>(); // Trả về danh sách rỗng nếu thất bại
    }
}
