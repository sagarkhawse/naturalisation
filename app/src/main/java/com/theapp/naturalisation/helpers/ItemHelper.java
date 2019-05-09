package com.theapp.naturalisation.helpers;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItemHelper {

    private static final String COLLECTION_NAME = "items";

    @NonNull
    public static CollectionReference getItemsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

}
