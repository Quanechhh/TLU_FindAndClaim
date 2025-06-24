package com.example.tlulostandclaim.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginViewModel extends ViewModel {

    public boolean isPasswordVisible = false;

    private final FirebaseAuth auth = GlobalData.firebaseAuth;
    private final FirebaseFirestore db = GlobalData.firebaseDB;

    // Wrapper kết quả: trạng thái (success/fail) + thông điệp
    public static class Result {
        public boolean success;
        public String message;

        public Result(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    // LiveData cho kết quả đăng nhập
    private final MutableLiveData<Result> _loginResult = new MutableLiveData<>();
    public LiveData<Result> getLoginResult() {
        return _loginResult;
    }

    // LiveData cho kết quả đổi mật khẩu
    private final MutableLiveData<Result> _changePasswordResult = new MutableLiveData<>();
    public LiveData<Result> getChangePasswordResult() {
        return _changePasswordResult;
    }

    public void clearLoginResult() {
        _loginResult.setValue(null);
    }

    public void clearChangePasswordResult() {
        _changePasswordResult.setValue(null);
    }

    /**
     * Đăng nhập người dùng bằng email và mật khẩu.
     */
    public void performLogin(User user) {
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() == null) {
                        _loginResult.setValue(new Result(false, "Người dùng không tồn tại!"));
                        return;
                    }

                    String uid = authResult.getUser().getUid();

                    db.collection("users").document(uid).get()
                            .addOnSuccessListener(document -> {
                                User fetchedUser = document.toObject(User.class);
                                if (fetchedUser != null) {
                                    GlobalData.user = fetchedUser;
                                    _loginResult.setValue(new Result(true, "Đăng nhập thành công"));
                                } else {
                                    _loginResult.setValue(new Result(false, "Không tìm thấy thông tin người dùng."));
                                }
                            })
                            .addOnFailureListener(e -> {
                                _loginResult.setValue(new Result(false, "Lỗi khi truy cập dữ liệu: " + e.getMessage()));
                            });

                })
                .addOnFailureListener(e -> {
                    _loginResult.setValue(new Result(false, "Lỗi đăng nhập: " + e.getMessage()));
                });
    }

    /**
     * Đổi mật khẩu cho người dùng hiện tại.
     */
    public void performChangePassword(String newPassword) {
        if (auth.getCurrentUser() == null) {
            _changePasswordResult.setValue(new Result(false, "Người dùng chưa đăng nhập."));
            return;
        }

        auth.getCurrentUser().updatePassword(newPassword)
                .addOnSuccessListener(aVoid -> {
                    _changePasswordResult.setValue(new Result(true, "Đổi mật khẩu thành công"));
                })
                .addOnFailureListener(e -> {
                    _changePasswordResult.setValue(new Result(false, "Lỗi: " + e.getMessage()));
                });
    }
}
