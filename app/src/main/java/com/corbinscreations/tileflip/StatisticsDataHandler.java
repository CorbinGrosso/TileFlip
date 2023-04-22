package com.corbinscreations.tileflip;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class StatisticsDataHandler extends DataHandler{

    private int boardsCleared = 0, mostRecentScore = 0;
    private int[] scores = {0,0,0,0,0,0,0,0,0,0};
    private final String filename = "statistics",
            scoresString = "Score",
            boardsClearedString = "Boards Cleared",
            mostRecentScoreString = "Most Recent Score";

    public StatisticsDataHandler(Context context) throws JSONException, IOException {
        loadData(context);
    }

    public int getBoardsCleared() {
        return boardsCleared;
    }

    public int[] getScores() {
        return scores;
    }

    public int getMostRecentScore() {
        return mostRecentScore;
    }

    public void setBoardsCleared(int boardsCleared) {
        this.boardsCleared = boardsCleared;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public void setMostRecentScore(int val) {
        mostRecentScore = val;
    }

    public void loadData(@NonNull Context context) throws IOException, JSONException {

        JSONObject jsonObject;
        File f = new File(context.getFilesDir(), filename);
        if (f.exists()) {
            jsonObject = super.getDataFromFile(context, filename);
        } else {
            jsonObject = this.setValues();
        }

        // Read in the values from JSON
        boardsCleared = jsonObject.getInt(boardsClearedString);

        for (int i = 0; i < 10; i++) {
            scores[i] = jsonObject.getInt(scoresString + (i+1));
        }

        mostRecentScore = jsonObject.getInt(mostRecentScoreString);
    }

    public void storeData(@NonNull Context context) throws JSONException, IOException {
        super.storeData(context, this.setValues(), filename);
    }

    public JSONObject setValues() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(boardsClearedString, boardsCleared);
        for (int i = 0; i < 10; i++) {
            json.put(scoresString + (i+1), scores[i]);
        }
        json.put(mostRecentScoreString, mostRecentScore);
        return json;
    }
}
