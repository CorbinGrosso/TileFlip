package com.myproject.tileflip;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Board {

    private Tile[][] tiles;
    private int[] colSums, rowSums, colBombs, rowBombs;
    private int size;

    public Board(int size, int maxValue) {

        tiles = new Tile[size][size];
        Random rand = new Random();
        colSums = new int[size];
        rowSums = new int[size];
        colBombs = new int[size];
        rowBombs = new int[size];
        this.size = size;

        // initialize arrays to all 0s
        for (int i = 0; i < size; i++) {
            colSums[i] = 0;
            rowSums[i] = 0;
            colBombs[i] = 0;
            rowBombs[i] = 0;
        }

        // set up arraylist of values to use for the tiles
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
                tiles[i][j] = new Tile(tileVal);
                rowSums[i] += tileVal;
                colSums[j] += tileVal;
                if (tileVal == 0) {
                    rowBombs[i] += 1;
                    colBombs[j] += 1;
                }
            }
        }
    }

    public int getRowSum(int rowNum) {
        return rowSums[rowNum];
    }

    public int getColSums(int colNum) {
        return colSums[colNum];
    }

    public void draw(RelativeLayout parentLayout, Context context) {
        ImageView img;
        RelativeLayout.LayoutParams layoutParams;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                // Tile Connector Horizontal Image
                img = new ImageView(context);
                img.setImageResource(R.drawable.tile_connector_horizontal);
                layoutParams = new RelativeLayout.LayoutParams(128, 128);
                layoutParams.setMargins(160 * j + 80 + 32, 160 * i + 384, 0, 0);
                img.setLayoutParams(layoutParams);
                parentLayout.addView(img);

                // Tile Connector Vertical Image
                img = new ImageView(context);
                img.setImageResource(R.drawable.tile_connector_vertical);
                layoutParams = new RelativeLayout.LayoutParams(128, 128);
                layoutParams.setMargins(160 * j + 32, 160 * i + 80 + 384, 0, 0);
                img.setLayoutParams(layoutParams);
                parentLayout.addView(img);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                // Tile Image and Text
                this.tiles[i][j].draw(parentLayout, context.getApplicationContext(), 32 + 160 * j, 160 * i + 384);
            }

            // Row Description Boxes
            img = new ImageView(context);
            img.setImageResource(R.drawable.description_tile);
            layoutParams = new RelativeLayout.LayoutParams(128, 128);
            layoutParams.setMargins(160 * size + 32, 160 * i + 384, 0, 0);
            img.setLayoutParams(layoutParams);
            parentLayout.addView(img);

            // Row Sums
            TextView text = new TextView(context);
            String rowSum = "" + rowSums[i];
            text.setText(rowSum);
            text.setTextSize(16);
            text.setTextColor(context.getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.END);
            layoutParams = new RelativeLayout.LayoutParams(128, 128);
            layoutParams.setMargins(160 * size + 16, 160 * i + 384, 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);

            // Row Bombs
            text = new TextView(context);
            String rowBomb = "" + rowBombs[i];
            text.setText(rowBomb);
            text.setTextSize(16);
            text.setTextColor(context.getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.END);
            layoutParams = new RelativeLayout.LayoutParams(128, 128);
            layoutParams.setMargins(160 * size + 16, 160 * i + 384 + 56, 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);

            // Column Description Boxes
            img = new ImageView(context);
            img.setImageResource(R.drawable.description_tile);
            layoutParams = new RelativeLayout.LayoutParams(128, 128);
            layoutParams.setMargins(160 * i + 32, 160 * size + 384, 0, 0);
            img.setLayoutParams(layoutParams);
            parentLayout.addView(img);

            // Column Sums
            text = new TextView(context);
            String colSum = "" + colSums[i];
            text.setText(colSum);
            text.setTextSize(16);
            text.setTextColor(context.getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.END);
            layoutParams = new RelativeLayout.LayoutParams(128, 128);
            layoutParams.setMargins(160 * i + 16, 160 * size + 384, 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);

            // Column Bombs
            text = new TextView(context);
            String colBomb = "" + colBombs[i];
            text.setText(colBomb);
            text.setTextSize(16);
            text.setTextColor(context.getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.END);
            layoutParams = new RelativeLayout.LayoutParams(128, 128);
            layoutParams.setMargins(160 * i + 16, 160 * size + 384 + 56, 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);
        }
    }
}

