package com.myproject.tileflip;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class GameDataHandler extends DataHandler {

    private int totalScore, roundScore;
    private final String filename = "gameData",
            totalScoreString = "Total Score",
            roundScoreString = "Round Score";

    public GameDataHandler(Context context) throws JSONException, IOException {
        loadData(context);
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void updateTotalScore() {
        totalScore += roundScore;
        roundScore = 0;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void updateRoundScore(int val) {
        if (roundScore == 0) {
            roundScore += val;
        } else {
            roundScore *= val;
        }
    }

    public void loadData(@NonNull Context context) throws IOException, JSONException {

        JSONObject jsonObject;
        File f = new File(context.getFilesDir(), filename);
        if (f.exists()) {
            jsonObject = super.getDataFromFile(context, filename);
        } else {
            jsonObject = setValues();
        }

        // Read in the values from JSON
        totalScore = jsonObject.getInt(totalScoreString);

        roundScore = jsonObject.getInt(roundScoreString);
    }

    public void storeData(@NonNull Context context) throws JSONException, IOException {
        super.storeData(context, setValues(),filename);
    }

    public JSONObject setValues() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(totalScoreString, totalScore);
        json.put(roundScoreString, roundScore);
        return json;
    }

    public void resetValues() {
        totalScore = 0;
        roundScore = 0;
    }

}
