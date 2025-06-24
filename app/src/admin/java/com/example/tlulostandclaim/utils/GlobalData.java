package com.example.tlulostandclaim.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class GlobalData {
    public static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static final FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
    public static final String needToFillAllFields = "Bạn cần nhập đủ các trường";
    public static final String commonError = "Đã có lỗi xảy ra, vui lòng thử lại!";
    public static final String commonSuccess = "Thành công!";

}
