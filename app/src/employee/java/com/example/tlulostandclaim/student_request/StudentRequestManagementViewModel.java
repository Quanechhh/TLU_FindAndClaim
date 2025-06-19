package com.example.tlulostandclaim.student_request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.RequestLostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
public class StudentRequestManagementViewModel extends ViewModel {
    private MutableLiveData<List<RequestLostItemModel>> _itemsResponse = new MutableLiveData<>();

    public LiveData<List<RequestLostItemModel>> getItemsResponse() {
        return _itemsResponse;
    }

    private MutableLiveData<RequestLostItemModel> _itemResponse = new MutableLiveData<>();

    public LiveData<RequestLostItemModel> getItemResponse() {
        return _itemResponse;
    }

    private List<RequestLostItemModel> originalLostItemResponse = new ArrayList<>();

    void getItems() {
        GlobalData.firebaseDB.
                collection("request_lost_items").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<RequestLostItemModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            RequestLostItemModel model = documentSnapshot.toObject(RequestLostItemModel.class);
                            list.add(model);
                        }
                        originalLostItemResponse.clear();
                        originalLostItemResponse.addAll(list);
                        _itemsResponse.setValue(list);
                    }
                });
    }

    public void getItem(String id) {
        GlobalData.firebaseDB.
                collection("request_lost_items").document(id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        RequestLostItemModel model = task.getResult().toObject(RequestLostItemModel.class);
                        if (model != null) _itemResponse.setValue(model);
                    }
                });
    }
}
