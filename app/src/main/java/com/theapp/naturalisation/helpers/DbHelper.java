package com.theapp.naturalisation.helpers;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DbHelper {

    private static final String QUESTIONS_COLLECTION_NAME = "items";
    private static final String FAQS_COLLECTION_NAME = "faqs";

    @NonNull
    public static CollectionReference getItemsCollection() {
        return FirebaseFirestore.getInstance().collection(QUESTIONS_COLLECTION_NAME);
    }

    @NonNull
    public static CollectionReference getFaqsCollection() {
        return FirebaseFirestore.getInstance().collection(FAQS_COLLECTION_NAME);
    }

}
