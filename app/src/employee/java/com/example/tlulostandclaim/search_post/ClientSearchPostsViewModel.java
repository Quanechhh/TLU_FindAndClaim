package com.example.tlulostandclaim.search_post;

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

public class ClientSearchPostsViewModel extends ViewModel {
    private MutableLiveData<List<LostItemModel>> _itemsResponse = new MutableLiveData<>();

    public LiveData<List<LostItemModel>> getItemsResponse() {
        return _itemsResponse;
    }

    private List<String> filterCategory = new ArrayList<>();
    private List<LostItemModel> originalLostItemResponse = new ArrayList<>();
    private MutableLiveData<List<CategoryModel>> _listOfCategory = new MutableLiveData<>();

    public LiveData<List<CategoryModel>> listOfCategory() {
        return _listOfCategory;
    }

    public CategoryModel chosenCategoryFilter;

    public void clearCategory() {
        chosenCategoryFilter = null;
    }

    void getItems() {
        GlobalData.firebaseDB.
                collection("lost_items").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<LostItemModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            LostItemModel model = documentSnapshot.toObject(LostItemModel.class);
                            list.add(model);
                        }
                        originalLostItemResponse.clear();
                        originalLostItemResponse.addAll(list);
                        _itemsResponse.setValue(list);
                    }
                });
    }

    void searchItems(String name) {
        List<LostItemModel> list = new ArrayList<>();
        for (LostItemModel i : originalLostItemResponse) {
            if (chosenCategoryFilter == null) {
                if (i.getName().contains(name)) {
                    list.add(i);
                }
            } else {
                if (i.getName().contains(name) && i.getCategory().equals(chosenCategoryFilter.getName())) {
                    list.add(i);
                }
            }
        }
        _itemsResponse.setValue(list);
    }

    public void chooseCategoryFilter(CategoryModel model) {
        chosenCategoryFilter = model;
        List<LostItemModel> list = new ArrayList<>();
        for (LostItemModel i : originalLostItemResponse) {
            if (chosenCategoryFilter != null) {
                if (i.getCategory().equals(chosenCategoryFilter.getName())) {
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
