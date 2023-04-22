package com.corbinscreations.tileflip;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

    public void storeData(@NonNull Context context, JSONObject json, String filename) throws IOException {

        // Turn JSON object into string to be written to file
        String jsonString = json.toString();

        // Open file and write to it
        File f = new File(context.getFilesDir(), filename);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(jsonString);
        bw.close();
    }

}
