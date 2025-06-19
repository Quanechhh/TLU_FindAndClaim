// Khai báo package chứa lớp ViewModel cho màn hình Quên mật khẩu
package com.example.tlulostandclaim.ui.forget_password;

// Import các class hỗ trợ từ Android Architecture Components
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Import lớp GlobalData có chứa firebaseAuth dùng để gửi email khôi phục
import com.example.tlulostandclaim.utils.GlobalData;

// Lớp ForgetPasswordViewModel kế thừa từ ViewModel, dùng để quản lý logic và dữ liệu cho màn hình Quên mật khẩu
public class ForgetPasswordViewModel extends ViewModel {

    // Biến private kiểu MutableLiveData để lưu kết quả phản hồi sau khi gửi email khôi phục mật khẩu
    // MutableLiveData cho phép ViewModel thay đổi giá trị và cập nhật UI khi có thay đổi
    private MutableLiveData<String> _forgetPasswordResponse = new MutableLiveData<>();

    // Phương thức public để expose MutableLiveData ra ngoài dưới dạng LiveData (chỉ đọc từ phía View)
    public LiveData<String> forgetPasswordResponse() {
        return _forgetPasswordResponse;
    }

    // Phương thức xử lý logic gửi email khôi phục mật khẩu thông qua Firebase Authentication
    void forgetPassword(String email) {
        // Gọi phương thức gửi email reset mật khẩu từ FirebaseAuth với email người dùng nhập
        GlobalData.firebaseAuth.sendPasswordResetEmail(email)

                // Nếu gửi thành công, set giá trị rỗng ("") để thông báo thành công (có thể xử lý tùy theo UI)
                .addOnSuccessListener(s -> _forgetPasswordResponse.setValue(""))

                // Nếu gửi thất bại, set giá trị là thông báo lỗi từ exception để hiển thị lên UI
                .addOnFailureListener(e -> _forgetPasswordResponse.setValue(e.getMessage()));
    }
}
