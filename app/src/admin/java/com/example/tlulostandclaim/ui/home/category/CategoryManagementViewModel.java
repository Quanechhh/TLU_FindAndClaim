package com.example.tlulostandclaim.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagementViewModel extends ViewModel {
    private MutableLiveData<List<CategoryModel>> _listOfCategory = new MutableLiveData<>();

    public LiveData<List<CategoryModel>> listOfCategory() {
        return _listOfCategory;
    }

    private List<CategoryModel> originalLostItemResponse = new ArrayList<>();
    private MutableLiveData<Boolean> _deleteCategoryResponse = new MutableLiveData<>();

    public LiveData<Boolean> deleteCategoryResponse() {
        return _deleteCategoryResponse;
    }

    private MutableLiveData<Boolean> _addCategoryResponse = new MutableLiveData<>();

    public LiveData<Boolean> addCategoryResponse() {
        return _addCategoryResponse;
    }

    public void clearAddCategoryResponse() {
        _addCategoryResponse.setValue(null);
    }

    public void fetchData() {
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
                        originalLostItemResponse.clear();
                        originalLostItemResponse.addAll(list);
                    }
                });
    }

    public void deleteCategory(String id) {
        GlobalData.firebaseDB.
                collection("category").document(id)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _deleteCategoryResponse.setValue(true);
                    } else {
                        _deleteCategoryResponse.setValue(false);
                    }
                });
    }

    public void addCategory(CategoryModel model) {
        GlobalData.firebaseDB.
                collection("category").document(model.getName())
                .set(model.convertToMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _addCategoryResponse.setValue(true);
                    } else {
                        _addCategoryResponse.setValue(false);
                    }
                });
    }

    void searchItems(String name) {
        List<CategoryModel> list = new ArrayList<>();
        for (CategoryModel i : originalLostItemResponse) {
            if (i.getName().contains(name)) {
                list.add(i);
            }
        }
        _listOfCategory.setValue(list);
    }
}
