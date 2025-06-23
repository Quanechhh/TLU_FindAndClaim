package com.example.tlulostandclaim.ui.statistic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.data.model.User;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminStatisticViewModel extends ViewModel {
    private MutableLiveData<List<User>> _listOfUsers = new MutableLiveData<>();

    public LiveData<List<User>> listOfUsers() {
        return _listOfUsers;
    }

    private MutableLiveData<List<LostItemModel>> _listOfPosts = new MutableLiveData<>();

    public LiveData<List<LostItemModel>> listOfPosts() {
        return _listOfPosts;
    }

    private MutableLiveData<List<InStorageLostItemModel>> _listOfItems = new MutableLiveData<>();

    public LiveData<List<InStorageLostItemModel>> listOfItems() {
        return _listOfItems;
    }

    public void getUsers() {
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
                    }
                });
    }

    public void getPosts() {
        GlobalData.firebaseDB.
                collection("lost_items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<LostItemModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            LostItemModel model = documentSnapshot.toObject(LostItemModel.class);
                            list.add(model);
                        }
                        _listOfPosts.setValue(list);
                    }
                });
    }

    public void getItems() {
        GlobalData.firebaseDB.
                collection("in_storage")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<InStorageLostItemModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            InStorageLostItemModel model = documentSnapshot.toObject(InStorageLostItemModel.class);
                            list.add(model);
                        }
                        _listOfItems.setValue(list);
                    }
                });
    }
}
