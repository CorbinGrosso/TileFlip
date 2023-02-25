package com.myproject.tileflip;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

public class TileOnClickListener implements View.OnClickListener {

    private GameScreenActivity activity;
    private Tile tile;

    public TileOnClickListener(GameScreenActivity activity, Tile tile) {
        this.activity = activity;
        this.tile = tile;
    }

    @Override
    public void onClick(View v) {
        // If the tile is face down
        if (tile.getIsFaceDown()) {
            // flip it over, reveal its value
            tile.reveal();

            if (tile.getValue() == 0) {
                try {
                    activity.putScoresInStatistics();
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
                activity.gameOver();
            } else {
                // Update the scoreboard
                try {
                    activity.updateRoundScore(tile.getValue());
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}


//                if (isFaceDown) {
//
//        // Reveal the value of the tile
//        reveal();
//
//        // Prep the data handler
//        GameDataHandler gdh = null;
//        try {
//            gdh = new GameDataHandler(context);
//        } catch (JSONException | IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Update the scoreboard
//        gdh.updateRoundScore(value);
//        // Write the new scoreboard values to the JSON file
//        try {
//            gdh.storeData(context);
//        } catch (JSONException | IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Update the scoreboard on the screen
//        int totalScore = gdh.getTotalScore();
//        int roundScore = gdh.getRoundScore();
//        TextView totalScoreValue = v.getRootView().findViewById(R.id.total_score_value);
//        String totalScoreString = "" + totalScore;
//        totalScoreValue.setText(totalScoreString);
//        TextView roundScoreValue = v.getRootView().findViewById(R.id.round_score_value);
//        String roundScoreString = "" + roundScore;
//        roundScoreValue.setText(roundScoreString);
//
//        // Check if the board has been cleared
//        if (roundScore == gdh.getMaxScore()) {
//            // Create a box to announce that the board was cleared
//            ImageView announcementBox = new ImageView(context);
//            announcementBox.setImageResource(R.drawable.title_screen_button);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(768, 192);
//            layoutParams.setMargins(0, 0, 0, 0);
//            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            announcementBox.setLayoutParams(layoutParams);
//            parentLayout.addView(announcementBox);
//
//            // Add text to announce that the board was cleared
//            TextView announcementText = new TextView(context);
//            announcementText.setText(R.string.board_cleared);
//            announcementText.setTextSize(24);
//            announcementText.setTextColor(context.getResources().getColor(R.color.text_color));
//            announcementText.setGravity(Gravity.CENTER);
//            layoutParams = new RelativeLayout.LayoutParams(768, 192);
//            layoutParams.setMargins(0, 0, 0, 0);
//            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            announcementText.setLayoutParams(layoutParams);
//            parentLayout.addView(announcementText);
//
//        }
//    }

