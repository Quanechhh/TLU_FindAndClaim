package com.example.tlulostandclaim.ui.forget_password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.utils.GlobalData;

public class ForgetPasswordViewModel extends ViewModel {
    private MutableLiveData<String> _forgetPasswordResponse = new MutableLiveData<>();

    public LiveData<String> forgetPasswordResponse() {
        return _forgetPasswordResponse;
    }

    void forgetPassword(String email) {
        GlobalData.firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(s -> _forgetPasswordResponse.setValue(""))
                .addOnFailureListener(e -> _forgetPasswordResponse.setValue(e.getMessage()));
    }
}
