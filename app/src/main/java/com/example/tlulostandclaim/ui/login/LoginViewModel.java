package com.example.tlulostandclaim.ui.login;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.User;


import com.example.tlulostandclaim.utils.GlobalData;
public class LoginViewModel extends ViewModel {
    boolean isHidePassword = true;
    private MutableLiveData<String> _loginUserResponse = new MutableLiveData<>();
    public LiveData<String> loginUserResponse() {
        return _loginUserResponse;
    }

    public void clearLoginResponse() {
        _loginUserResponse.setValue(null);
    }
    private MutableLiveData<String> _changePasswordResponse = new MutableLiveData<>();
    public LiveData<String> changePasswordResponse() {
        return _changePasswordResponse;
    }
    void loginEmail(User user) {
        GlobalData.firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() != null) {
                        GlobalData.firebaseDB.collection("users")
                                .document(authResult.getUser().getUid())
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    User model = documentSnapshot.toObject(User.class);
                                    if (model != null) {
                                        GlobalData.user = model;
                                        _loginUserResponse.setValue("");
                                    } else {
                                        _loginUserResponse.setValue("Đã có lỗi xảy ra, vui lòng thử lại!");
                                    }
                                });
                    } else {
                        _loginUserResponse.setValue("Đã có lỗi xảy ra, vui lòng thử lại!");
                    }
                })
                .addOnFailureListener(e -> _loginUserResponse.setValue(e.toString()));
    }
    void changePassword(User user) {
        GlobalData.firebaseAuth.getCurrentUser()
                .updatePassword(user.getPassword())
                .addOnSuccessListener(unused -> _changePasswordResponse.setValue(""))
                .addOnFailureListener(e -> _changePasswordResponse.setValue(e.getMessage().toString()));
    }
}
