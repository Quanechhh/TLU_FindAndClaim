package com.example.tlulostandclaim.detail_post.in_storage;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.enum_model.EnumInStorageItemStatus;
import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.data.model.InStorageLostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeDetailInStorageItemViewModel extends ViewModel {
    private MutableLiveData<InStorageLostItemModel> _itemsResponse = new MutableLiveData<>();
    List<Uri> listOfImages = new ArrayList<>();

    public LiveData<InStorageLostItemModel> getItemsResponse() {
        return _itemsResponse;
    }

    private MutableLiveData<Boolean> _requestResponse = new MutableLiveData<>();

    public LiveData<Boolean> getRequestResponse() {
        return _requestResponse;
    }

    private MutableLiveData<Boolean> _updatePostResponse = new MutableLiveData<>();

    public LiveData<Boolean> updatePostResponse() {
        return _updatePostResponse;
    }

    private MutableLiveData<Boolean> _deletePostResponse = new MutableLiveData<>();

    public LiveData<Boolean> deletePostResponse() {
        return _deletePostResponse;
    }
    private MutableLiveData<List<String>> _listOfCategory = new MutableLiveData<>();

    public LiveData<List<String>> listOfCategory() {
        return _listOfCategory;
    }

    public void getCategories() {
        GlobalData.firebaseDB.collection("category").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> list = new ArrayList<>();
                for (QueryDocumentSnapshot i : task.getResult()) {
                    String model = (i.toObject(CategoryModel.class)).getName();
                    list.add(model);
                }
                _listOfCategory.setValue(list);
            } else {
                // handle error here
            }
        });
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

    public void updateItem(InStorageLostItemModel model, boolean isUpdateStatus) {
        new Thread(() -> {
            if (!isUpdateStatus) {
                List<String> list = uploadMultipleImages();
                if (!list.isEmpty()) {
                    model.setImages(list);
                    listOfImages.clear();
                }
                model.setCreatedDate(System.currentTimeMillis());
                model.setStatus(EnumInStorageItemStatus.WAITING.value);
                GlobalData.firebaseDB.collection("in_storage").document(model.getId()).update(model.convertToMap()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _updatePostResponse.setValue(true);
                    } else {
                        _updatePostResponse.setValue(false);
                    }
                });
            } else {
                model.setStatus(EnumInStorageItemStatus.DONE.value);
                GlobalData.firebaseDB.collection("in_storage").document(model.getId()).update(model.convertToMap()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _updatePostResponse.setValue(true);
                    } else {
                        _updatePostResponse.setValue(false);
                    }
                });
            }
        }).start();
    }

    public void deleteItem(String model) {
        GlobalData.firebaseDB.collection("in_storage").document(model).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                _deletePostResponse.setValue(true);
            } else {
                _deletePostResponse.setValue(false);
            }
        });
    }

    private List<String> uploadMultipleImages() {
        try {
            List<String> uploadTasks = new ArrayList<>();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            for (Uri uri : listOfImages) {
                String fileName = UUID.randomUUID().toString() + ".jpg";
                StorageReference storageRef = storage.getReference().child("images/" + fileName);
                UploadTask uploadTask = storageRef.putFile(uri);
                Tasks.await(uploadTask);
                Uri downloadUri = Tasks.await(storageRef.getDownloadUrl());
                uploadTasks.add(downloadUri.toString());
            }
            return uploadTasks;
        } catch (Exception e) {
        }
        return new ArrayList<String>();
    }
}
