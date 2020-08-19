package com.theapp.naturalisation.utils;

import com.opencsv.CSVWriter;
import com.theapp.naturalisation.models.Item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataExtractor {

    public static void saveDataToFile(List<Object> list) throws IOException {

        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "CitizenshipData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer;

        // File exist
        if (f.exists() && !f.isDirectory()) {
            FileWriter mFileWriter = new FileWriter(filePath, true);
            writer = new CSVWriter(mFileWriter);
        } else {
            writer = new CSVWriter(new FileWriter(filePath));
        }

        Item item;
        int i = 0;
        for (Object object : list) {
            item = (Item) object;
            String[] data = {String.valueOf(i++), item.getQuestion(), item.getResponse(), item.getCategory()};
            writer.writeNext(data);
        }

        writer.close();
    }

}
