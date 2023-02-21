package com.myproject.tileflip;

import android.graphics.Bitmap;

public class GameObject {
    protected Bitmap image;

    protected int x, y;

    public GameObject(Bitmap image, int x, int y) {
        this.image = image;

        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}

