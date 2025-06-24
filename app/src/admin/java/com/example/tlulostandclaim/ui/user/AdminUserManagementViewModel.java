package com.example.tlulostandclaim.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagementViewModel extends ViewModel {

    private MutableLiveData<List<User>> _listOfUsers = new MutableLiveData<>();

    public LiveData<List<User>> listOfUsers() {
        return _listOfUsers;
    }

    private MutableLiveData<User> _userResponse = new MutableLiveData<>();

    public LiveData<User> userResponse() {
        return _userResponse;
    }

    private MutableLiveData<Boolean> _deleteUserResponse = new MutableLiveData<>();

    public LiveData<Boolean> deleteUserResponse() {
        return _deleteUserResponse;
    }

    private List<User> originalLostItemResponse = new ArrayList<>();

    public void fetchData() {
        GlobalData.firebaseDB.
                collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            User model = documentSnapshot.toObject(User.class);
                            list.add(model);
                        }
                        _listOfUsers.setValue(list);
                        originalLostItemResponse.clear();
                        originalLostItemResponse.addAll(list);
                    }
                });
    }

    public void getUser(String id) {
        GlobalData.firebaseDB.
                collection("users").document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User model = task.getResult().toObject(User.class);
                        if (model != null) _userResponse.setValue(model);
                    }
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

    void searchItems(String name) {
        List<User> list = new ArrayList<>();
        for (User i : originalLostItemResponse) {
            if (i.getFullName().contains(name) || i.getMobilePhone().contains(name) || i.getEmail().contains(name)) {
                list.add(i);
            }
        }
        _listOfUsers.setValue(list);
    }
}
