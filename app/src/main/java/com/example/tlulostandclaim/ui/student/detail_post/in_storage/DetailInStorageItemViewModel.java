package com.example.tlulostandclaim.ui.student.detail_post.in_storage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.data.model.RequestLostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;

public class DetailInStorageItemViewModel extends ViewModel {
    private MutableLiveData<InStorageLostItemModel> _itemsResponse = new MutableLiveData<>();

    public LiveData<InStorageLostItemModel> getItemsResponse() {
        return _itemsResponse;
    }

    private MutableLiveData<Boolean> _requestResponse = new MutableLiveData<>();

    public LiveData<Boolean> getRequestResponse() {
        return _requestResponse;
    }

    void getItem(String id) {
        GlobalData.firebaseDB.
                collection("in_storage")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        InStorageLostItemModel model = task.getResult().toObject(InStorageLostItemModel.class);
                        if (model != null) {
                            _itemsResponse.setValue(model);
                        }
                    }
                });
    }

    void sendRequest() {
        InStorageLostItemModel item = _itemsResponse.getValue();
        String id = GlobalData.firebaseDB.collection("request_lost_items").document().getId();
        if (item != null) {
            RequestLostItemModel model = new RequestLostItemModel(id, GlobalData.user.getFullName(), GlobalData.user.getMobilePhone(), System.currentTimeMillis(), item.getId(), item.getTitle(), item.getImages(), 0);
            GlobalData.firebaseDB.
                    collection("request_lost_items")
                    .document(id).set(model.toMap())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            _requestResponse.setValue(true);
                        } else {
                            _requestResponse.setValue(false);
                        }
                    });
        } else {
            _requestResponse.setValue(false);
        }
    }
}
