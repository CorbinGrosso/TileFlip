package com.myproject.tileflip;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OptionsDataHandler extends DataHandler{
    private int boardSize = 3;
    private int highestValueMultiplier = 3;

    public OptionsDataHandler(Context context) throws JSONException, IOException {
        loadData(context);
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    public int getHighestValueMultiplier() {
        return this.highestValueMultiplier;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setHighestValueMultiplier(int highestValueMultiplier) {
        this.highestValueMultiplier = highestValueMultiplier;
    }

    public void loadData(@NonNull Context context) throws IOException, JSONException {

        JSONObject jsonObject;
        File f = new File(context.getFilesDir(), "options");
        if (f.exists()) {
            jsonObject = super.getDataFromFile(context, "options");
        } else {
            jsonObject = this.setValues();
        }

        // Read in the values from JSON
        this.boardSize = jsonObject.getInt("Board Size");

        this.highestValueMultiplier = jsonObject.getInt("Highest Value Multiplier");
    }

    public void storeData(@NonNull Context context) throws JSONException, IOException {

        // create JSON object with current values
        JSONObject json = this.setValues();

        // Turn JSON object into string to be written to file
        String jsonString = json.toString();

        // Open file and write to it
        File f = new File(context.getFilesDir(),"options");
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(jsonString);
        bw.close();
    }

    public JSONObject setValues() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Board Size", this.boardSize);
        json.put("Highest Value Multiplier", this.highestValueMultiplier);
        return json;
    }
}
