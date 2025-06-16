package com.example.tlulostandclaim.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class GlobalData {
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
}
