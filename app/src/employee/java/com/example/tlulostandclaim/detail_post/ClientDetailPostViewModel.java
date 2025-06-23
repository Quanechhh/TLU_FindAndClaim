package com.example.tlulostandclaim.detail_post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.enum_model.EnumPostStatus;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.data.model.RequestLostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;

import java.util.HashMap;
import java.util.Map;

public class ClientDetailPostViewModel extends ViewModel {
    private MutableLiveData<LostItemModel> _itemsResponse = new MutableLiveData<>();

    public LiveData<LostItemModel> getItemsResponse() {
        return _itemsResponse;
    }

    private MutableLiveData<Boolean> _requestResponse = new MutableLiveData<>();

    public LiveData<Boolean> getRequestResponse() {
        return _requestResponse;
    }

    void getItem(String id) {
        GlobalData.firebaseDB.
                collection("lost_items")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        LostItemModel model = task.getResult().toObject(LostItemModel.class);
                        if (model != null) {
                            _itemsResponse.setValue(model);
                        }
                    }
                });
    }

    void sendRequest() {
        LostItemModel item = _itemsResponse.getValue();
        Map<String, Object> hm = new HashMap<>();
        hm.put("status", EnumPostStatus.DONE.value);
        if (item != null) {
            GlobalData.firebaseDB.
                    collection("lost_items")
                    .document(item.getId()).update(hm)
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
