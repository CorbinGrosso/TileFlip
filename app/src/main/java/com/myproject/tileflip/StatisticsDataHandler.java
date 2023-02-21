package com.myproject.tileflip;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StatisticsDataHandler extends DataHandler{

    private int boardsCleared = 0;
    private int[] scores = {0,0,0,0,0,0,0,0,0,0};

    public StatisticsDataHandler(Context context) throws JSONException, IOException {
        loadData(context);
    }

    public int getBoardsCleared() {
        return this.boardsCleared;
    }

    public int[] getScores() {
        return this.scores;
    }

    public void setBoardsCleared(int boardsCleared) {
        this.boardsCleared = boardsCleared;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public void loadData(@NonNull Context context) throws IOException, JSONException {

        JSONObject jsonObject;
        File f = new File(context.getFilesDir(), "statistics");
        if (f.exists()) {
            jsonObject = super.getDataFromFile(context, "statistics");
        } else {
            jsonObject = this.setValues();
        }

        // Read in the values from JSON
        this.boardsCleared = jsonObject.getInt("Boards Cleared");

        for (int i = 0; i < 10; i++) {
            this.scores[i] = jsonObject.getInt("score" + (i+1));
        }
    }

    public void storeData(@NonNull Context context) throws JSONException, IOException {

        // create JSON object with current values
        JSONObject json = this.setValues();

        // Turn JSON object into string to be written to file
        String jsonString = json.toString();

        // Open file and write to it
        File f = new File(context.getFilesDir(),"statistics");
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(jsonString);
        bw.close();
    }

    public JSONObject setValues() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Boards Cleared", this.boardsCleared);
        for (int i = 0; i < 10; i++) {
            json.put("score" + (i+1), this.scores[i]);
        }
        return json;
    }
}
