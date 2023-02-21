package com.myproject.tileflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tile {

    private int value;
    private boolean isFaceDown = false;

    public Tile(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void reveal() {
        this.isFaceDown = false;
    }

    public void draw(RelativeLayout parentLayout, Context context, int x, int y) {

        // Tile Image
        ImageView img = new ImageView(context);
        img.setImageResource(R.drawable.tile);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(128, 128);
        layoutParams.setMargins(x, y, 0, 0);
        img.setLayoutParams(layoutParams);

        // Make image respond to being tapped
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reveal();
            }
        });

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
            text.setTextSize(32);
            text.setTextColor(context.getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.CENTER);
            layoutParams = new RelativeLayout.LayoutParams(128, 128);
            layoutParams.setMargins(x, y, 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);
        } else {
            // Bomb image
            img = new ImageView(context);
            img.setImageResource(R.drawable.bomb_icon);
            layoutParams = new RelativeLayout.LayoutParams(128, 128);
            layoutParams.setMargins(x, y, 0, 0);
            img.setLayoutParams(layoutParams);
            parentLayout.addView(img);
        }
    }
}

