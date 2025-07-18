package com.example.tlulostandclaim.ui.student.add_post;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tlulostandclaim.data.model.CategoryModel;
import com.example.tlulostandclaim.data.model.LostItemModel;
import com.example.tlulostandclaim.utils.GlobalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AddLostPostViewModel extends ViewModel {
    List<Uri> listOfImages = new ArrayList<>();

    private MutableLiveData<List<String>> _listOfCategory = new MutableLiveData<>();

    public LiveData<List<String>> listOfCategory() {
        return _listOfCategory;
    }

    private MutableLiveData<Boolean> _addPostResponse = new MutableLiveData<>();

    public LiveData<Boolean> addPostResponse() {
        return _addPostResponse;
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

    public void addPost(LostItemModel model) {
        new Thread(() -> {
            String id = GlobalData.firebaseDB.collection("lost_items").document().getId();
            model.setId(id);
            List<String> list = uploadMultipleImages();
            model.setImage(list);
            GlobalData.firebaseDB.collection("lost_items").document(id).set(model.convertModelToMap()).addOnCompleteListener(task -> {
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
