package com.myproject.tileflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

public class Tile {

    private final int value, x, y, tileSize;
    private boolean isFaceDown = true;
    private final RelativeLayout parentLayout;
    private final Context context;
    private final GameScreenActivity activity;

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
        this.isFaceDown = false;
        this.draw();
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

