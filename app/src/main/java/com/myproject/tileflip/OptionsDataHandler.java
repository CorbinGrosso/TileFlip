package com.myproject.tileflip;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class OptionsDataHandler extends DataHandler{
    private int boardSize = 3, highestValueMultiplier = 3, volume = 100;
    private final String filename = "options", boardSizeString = "Board Size", highestValueMultiplierString = "Highest Value Multiplier", volumeString = "Volume";

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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
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
        this.boardSize = jsonObject.getInt(boardSizeString);

        this.highestValueMultiplier = jsonObject.getInt(highestValueMultiplierString);

        this.volume = jsonObject.getInt(volumeString);
    }

    public void storeData(@NonNull Context context) throws JSONException, IOException {
        super.storeData(context, this.setValues(), filename);
    }

    public JSONObject setValues() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(boardSizeString, boardSize);
        json.put(highestValueMultiplierString, highestValueMultiplier);
        json.put(volumeString, volume);
        return json;
    }
}
