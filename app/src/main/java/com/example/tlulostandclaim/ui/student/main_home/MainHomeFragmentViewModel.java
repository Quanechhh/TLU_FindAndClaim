package com.example.tlulostandclaim.ui.student.main_home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainHomeFragmentViewModel extends ViewModel {

    private MutableLiveData<List<InStorageLostItemModel>> _itemsResponse = new MutableLiveData<>();

    public LiveData<List<InStorageLostItemModel>> getItemsResponse() {
        return _itemsResponse;
    }

    void getItems() {
        GlobalData.firebaseDB.
                collection("in_storage")
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .limit(5).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<InStorageLostItemModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            InStorageLostItemModel model = documentSnapshot.toObject(InStorageLostItemModel.class);
                            list.add(model);
                        }
                        _itemsResponse.setValue(list);
                    }
                });
    }
}
