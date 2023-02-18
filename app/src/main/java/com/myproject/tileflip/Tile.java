package com.myproject.tileflip;

import android.graphics.Bitmap;

public class Tile extends GameObject {

    private int value;
    private boolean isFaceDown = true;

    public Tile(Bitmap image, int x, int y, int value) {
        super(image, x, y);
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void reveal() {
        this.isFaceDown = false;
    }
}

