package com.example.tlulostandclaim.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.utils.GlobalData;
public class AdminUserManagementViewModel extends ViewModel {

    private MutableLiveData<Boolean> _deleteUserResponse = new MutableLiveData<>();

    public LiveData<Boolean> deleteUserResponse() {
        return _deleteUserResponse;
    }

    public void fetchData() {
        GlobalData.firebaseDB.
                collection("users")
                .get()
                .addOnCompleteListener(task -> {

                });
    }

    public void deleteUser(String id) {
        GlobalData.firebaseDB.
                collection("users").document(id)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _deleteUserResponse.setValue(true);
                    } else {
                        _deleteUserResponse.setValue(false);
                    }
                });
    }

    void searchItems() {

    }
}
