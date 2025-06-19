// Gói khai báo chứa lớp LoginViewModel trong module UI đăng nhập
package com.example.tlulostandclaim.ui.login;

// Import các class liên quan đến LiveData và ViewModel trong kiến trúc MVVM
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Import model người dùng để chứa thông tin đăng nhập
import com.example.tlulostandclaim.data.model.User;

// Import dữ liệu toàn cục (FirebaseAuth, Firestore, người dùng hiện tại, v.v.)
import com.example.tlulostandclaim.utils.GlobalData;

// Lớp ViewModel cho màn hình đăng nhập, tách biệt logic khỏi giao diện
public class LoginViewModel extends ViewModel {

    // Biến boolean để theo dõi trạng thái ẩn/hiện mật khẩu trên giao diện
    boolean isHidePassword = true;

    // LiveData dùng để phản hồi khi người dùng đăng nhập (có thể là chuỗi lỗi hoặc rỗng nếu thành công)
    private MutableLiveData<String> _loginUserResponse = new MutableLiveData<>();

    // Getter cho LiveData để UI (Fragment) quan sát phản hồi đăng nhập
    public LiveData<String> loginUserResponse() {
        return _loginUserResponse;
    }

    // Phương thức xóa phản hồi đăng nhập cũ (reset trạng thái LiveData)
    public void clearLoginResponse() {
        _loginUserResponse.setValue(null);
    }

    // LiveData để phản hồi sau khi người dùng đổi mật khẩu (tương tự như đăng nhập)
    private MutableLiveData<String> _changePasswordResponse = new MutableLiveData<>();

    // Getter cho LiveData phản hồi đổi mật khẩu
    public LiveData<String> changePasswordResponse() {
        return _changePasswordResponse;
    }

    // Hàm xử lý đăng nhập bằng email và mật khẩu
    void loginEmail(User user) {
        // Gọi Firebase để xác thực người dùng
        GlobalData.firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())

                // Nếu đăng nhập thành công
                .addOnSuccessListener(authResult -> {
                    // Kiểm tra có người dùng hay không
                    if (authResult.getUser() != null) {
                        // Lấy dữ liệu người dùng từ Firestore theo UID
                        GlobalData.firebaseDB.collection("users")
                                .document(authResult.getUser().getUid())
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    // Convert dữ liệu từ Firestore thành đối tượng User
                                    User model = documentSnapshot.toObject(User.class);

                                    // Nếu dữ liệu người dùng hợp lệ
                                    if (model != null) {
                                        // Gán người dùng toàn cục và thông báo thành công (rỗng)
                                        GlobalData.user = model;
                                        _loginUserResponse.setValue("");
                                    } else {
                                        // Nếu không lấy được dữ liệu => báo lỗi
                                        _loginUserResponse.setValue("Đã có lỗi xảy ra, vui lòng thử lại!");
                                    }
                                });
                    } else {
                        // Nếu người dùng không tồn tại sau khi đăng nhập
                        _loginUserResponse.setValue("Đã có lỗi xảy ra, vui lòng thử lại!");
                    }
                })

                // Nếu đăng nhập thất bại (sai tài khoản, mạng lỗi, v.v.)
                .addOnFailureListener(e -> _loginUserResponse.setValue(e.toString()));
    }

    // Hàm xử lý thay đổi mật khẩu cho người dùng hiện tại
    void changePassword(User user) {
        GlobalData.firebaseAuth.getCurrentUser()
                .updatePassword(user.getPassword())

                // Nếu đổi mật khẩu thành công
                .addOnSuccessListener(unused -> _changePasswordResponse.setValue(""))

                // Nếu thất bại (ví dụ: chưa đăng nhập hoặc mật khẩu không đủ mạnh)
                .addOnFailureListener(e -> _changePasswordResponse.setValue(e.getMessage().toString()));
    }
}
