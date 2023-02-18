package com.myproject.tileflip;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Board {

    private Tile[][] tiles;
    private int[] colSums, rowSums;

    public Board(int size, int maxValue) {

        tiles = new Tile[size][size];
        Random rand = new Random();
//        Bitmap tileImage = BitmapFactory.decodeFile("res\\drawable\\tile.png");
        colSums = new int[size];
        rowSums = new int[size];

        ArrayList<Integer> tileVals = new ArrayList<Integer>();

        for (int i = 0; i < size * size; i++) {
            if (i < size * size / 3) {
                tileVals.add(0);
            } else {
                tileVals.add(rand.nextInt(maxValue + 1));
            }
        }

        // Create tiles to populate board
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int randVal = rand.nextInt(tileVals.size());
                int tileVal = tileVals.get(randVal);
                tileVals.remove(randVal);
                int x = i * 64 + 8;
                int y = j * 64 + 8;
//                tiles[i][j] = new Tile(tileImage, x, y, tileVal);
                rowSums[i] += tiles[i][j].getValue();
                colSums[j] += tiles[i][j].getValue();
            }
        }
    }

    public int getRowSum(int rowNum) {
        return rowSums[rowNum];
    }

    public int getColSums(int colNum) {
        return colSums[colNum];
    }

    public void draw() {
        RectF rect = new RectF(10, 10, 32, 32);
        Canvas screen = new Canvas();
//        screen.drawBitmap(this.hPipe, null, rect, null);
    }
}

