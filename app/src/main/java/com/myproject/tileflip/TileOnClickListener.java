package com.myproject.tileflip;

import android.view.View;

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
            // if flip mode is selected
            if (activity.getFlipButtonIsSelected()) {

                // perform the first half of the flipping animation
                tile.animateFlip1();

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

            } else {
                tile.toggleMemo(activity.getSelectedMemo());
            }
        }
    }
}
