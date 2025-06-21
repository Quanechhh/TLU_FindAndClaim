package com.example.tlulostandclaim.ui.register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class RegisterViewModel extends ViewModel {
    boolean isHidePassword = true;
    private MutableLiveData<String> _registerUserResponse = new MutableLiveData<>();

    public LiveData<String> getRegisterUserResponse() {
        return _registerUserResponse;
    }

    public void registerUser(User user) {
        GlobalData.firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                FirebaseUser firebaseUser = task.getResult().getUser();
                if (firebaseUser != null) {
                    User userWithId = new User(
                            firebaseUser.getUid(),
                            user.getFullName(),
                            user.getMobilePhone(),
                            user.getStudentId(),
                            user.getEmail(),
                            user.getRole()
                    );
                    saveUserToDB(userWithId);
                    _registerUserResponse.setValue("");
                } else {
                    _registerUserResponse.setValue("Đã có lỗi xảy ra, vui lòng thử lại!");
                }
            } else {
                _registerUserResponse.setValue("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        }).addOnFailureListener(e -> _registerUserResponse.setValue(e.getMessage()));
    }

    private void saveUserToDB(@NonNull User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("studentId", user.getStudentId());
        userMap.put("email", user.getEmail());
        userMap.put("fullName", user.getFullName());
        userMap.put("mobilePhone", user.getMobilePhone());
        userMap.put("role", user.getRole());

        GlobalData.firebaseDB
                .collection("users")
                .document(user.getId())
                .set(userMap)
                .addOnSuccessListener(aVoid -> Log.d("Register", "User info saved to Firestore"))
                .addOnFailureListener(e -> Log.w("Register", "Error saving user info", e));
    }
}
