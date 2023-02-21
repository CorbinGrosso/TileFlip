package com.myproject.tileflip;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataHandler {

    public JSONObject getDataFromFile(Context context, String filename) throws IOException, JSONException {
        // read in all lines of the file
        File f = new File(context.getFilesDir(),filename);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder stringBuilder = new StringBuilder();
        String line = br.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = br.readLine();
        }
        br.close();

        // Turn file lines into JSON readable string
        String response = stringBuilder.toString();
        return new JSONObject(response);
    }

}
