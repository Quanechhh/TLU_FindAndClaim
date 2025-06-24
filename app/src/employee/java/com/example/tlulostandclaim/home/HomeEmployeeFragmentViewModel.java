package com.example.tlulostandclaim.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeEmployeeFragmentViewModel extends ViewModel {

    private MutableLiveData<List<LostItemModel>> _itemsResponse = new MutableLiveData<>();

    public LiveData<List<LostItemModel>> getItemsResponse() {
        return _itemsResponse;
    }

    void getItems() {
        GlobalData.firebaseDB.
                collection("lost_items")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(5).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<LostItemModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            LostItemModel model = documentSnapshot.toObject(LostItemModel.class);
                            list.add(model);
                        }
                        _itemsResponse.setValue(list);
                    }
                });
    }
}
