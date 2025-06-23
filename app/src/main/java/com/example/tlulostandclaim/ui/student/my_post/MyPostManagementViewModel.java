package com.example.tlulostandclaim.ui.student.my_post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyPostManagementViewModel extends ViewModel {

    private MutableLiveData<List<LostItemModel>> _listOfPosts = new MutableLiveData<>();

    public LiveData<List<LostItemModel>> listOfPosts() {
        return _listOfPosts;
    }

    public void fetchData() {
        GlobalData.firebaseDB.
                collection("lost_items")
                .whereEqualTo("userId", GlobalData.user.getId().trim())
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
}
