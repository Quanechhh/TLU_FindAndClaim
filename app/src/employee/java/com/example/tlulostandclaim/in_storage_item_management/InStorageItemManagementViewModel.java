package com.example.tlulostandclaim.in_storage_item_management;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.InStorageLostItemModel;

import java.util.ArrayList;
import java.util.List;

public class InStorageItemManagementViewModel extends ViewModel {
    private MutableLiveData<List<InStorageLostItemModel>> _itemsResponse = new MutableLiveData<>();
    public LiveData<List<InStorageLostItemModel>> getItemsResponse() {
        return _itemsResponse;
    }

    private List<String> filterCategory = new ArrayList<>();
    private List<InStorageLostItemModel> originalLostItemResponse = new ArrayList<>();
}
