package com.theapp.naturalisation.helpers;

import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;

public class RetrieveFileTask extends AsyncTask<String, Void, PdfReader> {
    @Override
    protected PdfReader doInBackground(String... strings) {
        try {
            return new PdfReader(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
