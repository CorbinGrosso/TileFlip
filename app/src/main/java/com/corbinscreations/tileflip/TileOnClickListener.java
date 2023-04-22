package com.corbinscreations.tileflip;

import android.view.View;

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

            } else {
                tile.toggleMemo(activity.getSelectedMemo());
            }
        }
    }
}
