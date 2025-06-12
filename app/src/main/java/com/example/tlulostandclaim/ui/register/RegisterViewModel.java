package com.example.tlulostandclaim.ui.register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class RegisterViewModel extends ViewModel {
    // Biến xác định có ẩn mật khẩu hay không (mặc định là ẩn)
    boolean isHidePassword = true;

    // Biến LiveData dùng để thông báo kết quả đăng ký (thành công hay lỗi)
    private MutableLiveData<String> _registerUserResponse = new MutableLiveData<>();

    // Getter để fragment có thể quan sát dữ liệu phản hồi từ ViewModel
    public LiveData<String> getRegisterUserResponse() {
        return _registerUserResponse;
    }

    /**
     * Hàm xử lý đăng ký tài khoản Firebase và lưu thông tin người dùng vào Firestore.
     * @param user Đối tượng User chứa thông tin đăng ký
     */
    public void registerUser(User user) {
        // Gọi Firebase Auth để tạo tài khoản bằng email và mật khẩu
        GlobalData.firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Lấy đối tượng người dùng Firebase sau khi tạo thành công
                FirebaseUser firebaseUser = task.getResult().getUser();
                if (firebaseUser != null) {
                    // Tạo user mới có chứa ID Firebase (UID)
                    User userWithId = new User(
                            firebaseUser.getUid(),
                            user.getFullName(),
                            user.getMobilePhone(),
                            user.getStudentId(),
                            user.getEmail(),
                            user.getRole()
                    );
                    // Lưu user vào Firestore
                    saveUserToDB(userWithId);

                    // Gửi phản hồi thành công (chuỗi rỗng)
                    _registerUserResponse.setValue("");
                } else {
                    // Gặp lỗi: không có thông tin người dùng Firebase
                    _registerUserResponse.setValue("Đã có lỗi xảy ra, vui lòng thử lại!");
                }
            } else {
                // Gặp lỗi khi tạo tài khoản Firebase
                _registerUserResponse.setValue("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        }).addOnFailureListener(e ->
                // Nếu lỗi xảy ra ở mức hệ thống Firebase
                _registerUserResponse.setValue(e.getMessage())
        );
    }

    /**
     * Hàm lưu thông tin người dùng đã đăng ký vào Firestore (dưới collection 'users').
     * @param user Đối tượng người dùng đã có ID Firebase
     */
    private void saveUserToDB(@NonNull User user) {
        // Tạo HashMap chứa thông tin người dùng để lưu vào Firestore
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("studentId", user.getStudentId());
        userMap.put("email", user.getEmail());
        userMap.put("fullName", user.getFullName());
        userMap.put("mobilePhone", user.getMobilePhone());
        userMap.put("role", user.getRole());

        // Lưu vào collection "users" với document ID là UID của Firebase
        GlobalData.firebaseDB
                .collection("users")
                .document(user.getId())
                .set(userMap)
                .addOnSuccessListener(aVoid ->
                        // Log khi lưu thành công
                        Log.d("Register", "User info saved to Firestore")
                )
                .addOnFailureListener(e ->
                        // Log lỗi khi lưu thất bại
                        Log.w("Register", "Error saving user info", e)
                );
    }
}
