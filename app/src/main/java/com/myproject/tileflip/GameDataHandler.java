package com.myproject.tileflip;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GameDataHandler extends DataHandler {

    private int totalScore = 0, roundScore = 0, boardSize, highestValueMultiplier;
    private int[][] tileVals;
    private boolean continueBool = false, gameOver = false;
    private final String filename = "gameData",
            totalScoreString = "Total Score",
            roundScoreString = "Round Score",
    continueString = "Continue",
    gameOverString = "Game Over",
    boardSizeString = "Board Size",
    highestValueMultiplierString = "Highest Value Multiplier",
    tileValString = "Tile Val";

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

    public boolean getContinue() {
        return continueBool;
    }

    public void setContinue(boolean continueBool) {
        this.continueBool = continueBool;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
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
        continueBool = jsonObject.getBoolean(continueString);
        gameOver = jsonObject.getBoolean(gameOverString);
        boardSize = jsonObject.getInt(boardSizeString);
        highestValueMultiplier = jsonObject.getInt(highestValueMultiplierString);
        tileVals = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                tileVals[i][j] = jsonObject.getInt(tileValString + "[" + i + "][" + j + "]");
            }
        }
    }

    public void storeData(@NonNull Context context) throws JSONException, IOException {
        super.storeData(context, setValues(), filename);
    }

    public JSONObject setValues() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(totalScoreString, totalScore);
        json.put(roundScoreString, roundScore);
        json.put(continueString, continueBool);
        json.put(gameOverString, gameOver);
        json.put(boardSizeString, boardSize);
        json.put(highestValueMultiplierString, highestValueMultiplier);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                json.put(tileValString + "[" + i + "][" + j + "]", tileVals[i][j]);
            }
        }
        return json;
    }

    public void resetValues() {
        totalScore = 0;
        roundScore = 0;
        gameOver = false;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getHighestValueMultiplier() {
        return highestValueMultiplier;
    }

    public int[][] getTileVals() {
        return tileVals;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setHighestValueMultiplier(int highestValueMultiplier) {
        this.highestValueMultiplier = highestValueMultiplier;
    }

    public void setTileVals(int[][] tileVals) {
        this.tileVals = tileVals;
    }
}
