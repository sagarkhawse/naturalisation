package com.theapp.naturalisation.helpers;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FindNameThread extends AsyncTask<String, String, Boolean> {
    private static final String TAG = "FindNameThread";

    private static boolean foundName;
    private static String joNumberTemp;
    private static String joNumber;

    public FindNameThread() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return findNameInJO(strings[0], strings[1], strings[2], strings[3]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    private boolean findNameInJO(String nameToFind, String jo1, String jo2, String jo3) {
        foundName = false;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        List<StorageReference> list = new ArrayList<>();

        StorageReference reference1 = storageRef.child(jo1);
        StorageReference reference2 = storageRef.child(jo2);
        StorageReference reference3 = storageRef.child(jo3);

        list.add(reference1);
        list.add(reference2);
        list.add(reference3);

        for (StorageReference reference : list) {
            reference.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri uri = task.getResult();
                    try {
                        PdfReader reader = new RetrieveFileTask().execute(Objects.requireNonNull(uri).toString()).get();
                        int n = reader.getNumberOfPages();
                        joNumberTemp = null;
                        for (int i = 0; i < n; i++) {
                            //Extracting the content from the different pages
                            String text = PdfTextExtractor.getTextFromPage(reader, i + 1).trim();
                            if (i == 0) {
                                joNumberTemp = text.substring(0, text.indexOf("\n"));
                            }
                            if (joNumberTemp.trim().equals("er")) {
                                if (i == 1) {
                                    joNumberTemp = text.substring(4, text.indexOf("J"));
                                }
                            }
                            if (text.contains(nameToFind)) {
                                foundName = true;
                                joNumber = joNumberTemp;
                                Log.i(TAG, "name found in " + joNumberTemp);
                                break;
                            }
                        }
                        reader.close();
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                } else {
                    Log.e(TAG, "error in findNameInJO %s", task.getException());
                }
            });
        }

        return foundName;
    }

    static boolean isFound() {
        return foundName;
    }

    static String getJoNumber() {
        return joNumber;
    }
}
