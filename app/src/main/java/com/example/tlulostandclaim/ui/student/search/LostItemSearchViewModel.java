package com.example.tlulostandclaim.ui.student.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LostItemSearchViewModel extends ViewModel {

    private MutableLiveData<List<CategoryModel>> _listOfCategory = new MutableLiveData<>();

    public LiveData<List<CategoryModel>> listOfCategory() {
        return _listOfCategory;
    }

    public CategoryModel chosenCategoryFilter;

    public void clearCategory() {
        chosenCategoryFilter = null;
    }

    private MutableLiveData<List<InStorageLostItemModel>> _itemsResponse = new MutableLiveData<>();

    public LiveData<List<InStorageLostItemModel>> getItemsResponse() {
        return _itemsResponse;
    }

    private List<String> filterCategory = new ArrayList<>();
    private List<InStorageLostItemModel> originalLostItemResponse = new ArrayList<>();

    void getItems() {
        GlobalData.firebaseDB.
                collection("in_storage").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<InStorageLostItemModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            InStorageLostItemModel model = documentSnapshot.toObject(InStorageLostItemModel.class);
                            list.add(model);
                        }
                        originalLostItemResponse.clear();
                        originalLostItemResponse.addAll(list);
                        _itemsResponse.setValue(list);
                    }
                });
    }

    void searchItems(String name) {
        List<InStorageLostItemModel> list = new ArrayList<>();
        for (InStorageLostItemModel i : originalLostItemResponse) {
            if (chosenCategoryFilter == null) {
                if (i.getTitle().contains(name)) {
                    list.add(i);
                }
            } else {
                if (i.getTitle().contains(name) && i.getType().equals(chosenCategoryFilter.getName())) {
                    list.add(i);
                }
            }
        }
        _itemsResponse.setValue(list);
    }

    public void chooseCategoryFilter(CategoryModel model) {
        chosenCategoryFilter = model;
        List<InStorageLostItemModel> list = new ArrayList<>();
        for (InStorageLostItemModel i : originalLostItemResponse) {
            if (chosenCategoryFilter != null) {
                if (i.getType().equals(chosenCategoryFilter.getName())) {
                    list.add(i);
                }
            }
        }
        if (!list.isEmpty()) _itemsResponse.setValue(list);
    }

    void getCategory() {
        GlobalData.firebaseDB.
                collection("category")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<CategoryModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            CategoryModel model = documentSnapshot.toObject(CategoryModel.class);
                            list.add(model);
                        }
                        _listOfCategory.setValue(list);
                    }
                });
    }
}
