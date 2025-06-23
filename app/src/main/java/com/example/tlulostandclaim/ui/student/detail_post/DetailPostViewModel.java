package com.example.tlulostandclaim.ui.student.detail_post;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.data.model.RequestLostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DetailPostViewModel extends ViewModel {
    private MutableLiveData<LostItemModel> _itemsResponse = new MutableLiveData<>();

    public LiveData<LostItemModel> getItemsResponse() {
        return _itemsResponse;
    }

    private MutableLiveData<Boolean> _requestResponse = new MutableLiveData<>();

    public LiveData<Boolean> getRequestResponse() {
        return _requestResponse;
    }

    private MutableLiveData<Boolean> _addPostResponse = new MutableLiveData<>();

    public LiveData<Boolean> addPostResponse() {
        return _addPostResponse;
    }

    private MutableLiveData<Boolean> _deletePostResponse = new MutableLiveData<>();

    public LiveData<Boolean> deletePostResponse() {
        return _deletePostResponse;
    }

    List<Uri> listOfImages = new ArrayList<>();

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

    void deleteItem(String id) {
        GlobalData.firebaseDB.
                collection("lost_items")
                .document(id)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _deletePostResponse.setValue(true);
                    } else {
                        _deletePostResponse.setValue(false);
                    }
                });
    }

//    void sendRequest() {
//        LostItemModel item = _itemsResponse.getValue();
//        String id = GlobalData.firebaseDB.collection("request_lost_items").document().getId();
//        if (item != null) {
//            RequestLostItemModel model = new RequestLostItemModel(id, GlobalData.user.getFullName(), GlobalData.user.getMobilePhone(), System.currentTimeMillis(), item.getId(), item.getName(), item.getImage(), 0);
//            GlobalData.firebaseDB.
//                    collection("request_lost_items")
//                    .document(id).set(model.toMap())
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            _requestResponse.setValue(true);
//                        } else {
//                            _requestResponse.setValue(false);
//                        }
//                    });
//        } else {
//            _requestResponse.setValue(false);
//        }
//    }

    public void updatePost(LostItemModel model) {
        new Thread(() -> {
            List<String> list = uploadMultipleImages();
            if (!list.isEmpty()) {
                model.setImage(null);
                model.setImage(list);
                listOfImages.clear();
            }
            GlobalData.firebaseDB.collection("lost_items").document(model.getId()).update(model.convertModelToMap()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    _addPostResponse.setValue(true);
                } else {
                    _addPostResponse.setValue(false);
                }
            });
        }).start();
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
