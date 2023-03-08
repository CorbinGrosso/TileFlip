package com.myproject.tileflip;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tile {

    private final int value, x, y, tileSize;
    private boolean isFaceDown = true;
    private final RelativeLayout parentLayout;
    private final Context context;
    private final GameScreenActivity activity;
    private final boolean[] memos = {false, false, false, false, false, false, false, false};
    private final int[] memoIDs = {View.generateViewId(), View.generateViewId(), View.generateViewId(), View.generateViewId(),
            View.generateViewId(), View.generateViewId(), View.generateViewId(), View.generateViewId()};

    public Tile(GameScreenActivity activity, RelativeLayout parentLayout, Context context, int x, int y, int tileSize, int value) {
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.context = context;
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean getIsFaceDown() {
        return isFaceDown;
    }

    public void reveal() {
        isFaceDown = false;
        removeAllMemos();
        draw();
    }

    public void toggleMemo(int val) {
        memos[val] = !memos[val];
        if (memos[val]) {
            // set text size and margin offsets
            int textSize = tileSize * 3 / 32;
            int vOffset = 0, hOffset = 0;
            if (val < 3) {
                hOffset = tileSize / 3 * val;
            } else if (val == 3) {
                hOffset = tileSize * 2 / 3;
                vOffset = tileSize / 3;
            } else if (val == 4) {
                hOffset = tileSize * 2 / 3;
                vOffset = tileSize * 2 / 3;
            } else if (val == 5) {
                hOffset = tileSize / 3;
                vOffset = tileSize * 2 / 3;
            } else if (val == 6) {
                vOffset = tileSize * 2 / 3;
            } else if (val == 7) {
                vOffset = tileSize / 3;
            }

            if (val > 0) {
                // Place text on the tile
                TextView text = new TextView(context);
                String valString = "" + val;
                text.setText(valString);
                text.setTextSize(textSize);
                text.setId(memoIDs[val]);
                text.setTextColor(context.getResources().getColor(R.color.memo_color, null));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
                layoutParams.setMargins(x + hOffset + 12, y + vOffset, 0, 0);
                text.setLayoutParams(layoutParams);
                parentLayout.addView(text);
            } else {
                ImageView img = new ImageView(context);
                img.setImageResource(R.drawable.mini_bomb_memo_icon);
                img.setId(memoIDs[val]);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize / 3, tileSize / 3);
                layoutParams.setMargins(x, y, 0, 0);
                img.setLayoutParams(layoutParams);
                parentLayout.addView(img);
            }
        } else {
            parentLayout.removeView(activity.findViewById(memoIDs[val]));
        }
    }

    private void removeAllMemos() {
        for (int i = 0; i < memos.length; i++) {
            if (memos[i]) {
                toggleMemo(i);
            }
        }
    }

    public void draw() {

        int textSize = (int)(tileSize * 0.25);

        // Tile Image
        ImageView img = new ImageView(context);
        img.setImageResource(R.drawable.tile);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
        layoutParams.setMargins(x, y, 0, 0);
        img.setLayoutParams(layoutParams);

        // Make image respond to being tapped
        img.setOnClickListener(new TileOnClickListener(activity, this));

        parentLayout.addView(img);

        if (isFaceDown || value != 0) {
            // Value text
            TextView text = new TextView(context);
            if (isFaceDown) {
                text.setText(R.string.val_unknown);
            } else {
                String valueStr = "" + value;
                text.setText(valueStr);
            }
            text.setTextSize(textSize);
            text.setTextColor(context.getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.CENTER);
            layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
            layoutParams.setMargins(x, y, 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);
        } else {
            // Bomb image
            img = new ImageView(context);
            img.setImageResource(R.drawable.bomb_icon);
            layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
            layoutParams.setMargins(x, y, 0, 0);
            img.setLayoutParams(layoutParams);
            parentLayout.addView(img);
        }
    }

}

