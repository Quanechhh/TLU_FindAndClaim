// Package định danh
package com.example.tlulostandclaim.detail_post;

// Import các thư viện cần thiết
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.enum_model.EnumPostStatus;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;

import java.util.HashMap;
import java.util.Map;

// ViewModel quản lý dữ liệu cho màn hình chi tiết bài đăng của khách hàng
public class ClientDetailPostViewModel extends ViewModel {

    // LiveData chứa dữ liệu chi tiết bài đăng
    private MutableLiveData<LostItemModel> _itemsResponse = new MutableLiveData<>();

    // Phương thức public cho phép Fragment quan sát dữ liệu bài đăng
    public LiveData<LostItemModel> getItemsResponse() {
        return _itemsResponse;
    }

    // LiveData chứa kết quả gửi yêu cầu nhận đồ (thành công/thất bại)
    private MutableLiveData<Boolean> _requestResponse = new MutableLiveData<>();

    // Phương thức public cho phép Fragment quan sát kết quả gửi yêu cầu
    public LiveData<Boolean> getRequestResponse() {
        return _requestResponse;
    }

    // Phương thức lấy chi tiết bài đăng từ Firestore theo id
    void getItem(String id) {
        GlobalData.firebaseDB.
                collection("lost_items") // Truy cập collection 'lost_items'
                .document(id) // Truy cập document có id được truyền vào
                .get()
                .addOnCompleteListener(task -> { // Lắng nghe kết quả truy vấn
                    if (task.isSuccessful()) { // Nếu truy vấn thành công
                        LostItemModel model = task.getResult().toObject(LostItemModel.class); // Chuyển document thành đối tượng LostItemModel
                        if (model != null) {
                            _itemsResponse.setValue(model); // Cập nhật dữ liệu cho LiveData để Fragment nhận và hiển thị
                        }
                    }
                });
    }

    // Phương thức gửi yêu cầu nhận lại đồ, cập nhật trạng thái bài đăng
    void sendRequest() {
        LostItemModel item = _itemsResponse.getValue(); // Lấy dữ liệu hiện tại đang được lưu trong LiveData
        Map<String, Object> hm = new HashMap<>();
        hm.put("status", EnumPostStatus.DONE.value); // Tạo dữ liệu mới để cập nhật trạng thái là 'DONE'

        if (item != null) { // Nếu bài đăng tồn tại
            GlobalData.firebaseDB.
                    collection("lost_items") // Truy cập collection 'lost_items'
                    .document(item.getId()).update(hm) // Cập nhật document với dữ liệu mới
                    .addOnCompleteListener(task -> { // Lắng nghe kết quả cập nhật
                        if (task.isSuccessful()) { // Nếu cập nhật thành công
                            _requestResponse.setValue(true); // Trả về kết quả thành công cho Fragment
                        } else { // Nếu cập nhật thất bại
                            _requestResponse.setValue(false); // Trả về kết quả thất bại cho Fragment
                        }
                    });
        } else { // Nếu dữ liệu bài đăng không tồn tại
            _requestResponse.setValue(false); // Trả về kết quả thất bại
        }
    }
}
