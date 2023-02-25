package com.myproject.tileflip;

import java.io.IOException;
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

import org.json.JSONException;

public class Board {

    private final Tile[][] tiles;
    private final int[] colSums, rowSums, colBombs, rowBombs;
    private final int boardSize, spaceBetweenTiles, hPipeOffsetLeft, height;
    private int tileSize, maxScore = 1;
    private RelativeLayout parentLayout;

    public Board(GameScreenActivity activity, RelativeLayout parentLayout, Context context, int boardSize, int maxValue, int height) throws JSONException, IOException {

        tiles = new Tile[boardSize][boardSize];
        Random rand = new Random();
        colSums = new int[boardSize];
        rowSums = new int[boardSize];
        colBombs = new int[boardSize];
        rowBombs = new int[boardSize];
        this.boardSize = boardSize;
        this.height = height;
        this.parentLayout = parentLayout;

        if (boardSize == 3) {
            tileSize = 192;
        } else if (boardSize >= 4) {
            tileSize = 128;
        }
        spaceBetweenTiles = (int)(tileSize * 1.25);
        hPipeOffsetLeft = (int)(tileSize * 0.5);

        // initialize arrays to all 0s
        for (int i = 0; i < boardSize; i++) {
            colSums[i] = 0;
            rowSums[i] = 0;
            colBombs[i] = 0;
            rowBombs[i] = 0;
        }

        // set up arraylist of values to use for the tiles
        ArrayList<Integer> tileVals = new ArrayList<Integer>();
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (i < boardSize * boardSize / 3) {
                tileVals.add(0);
            } else {
                tileVals.add(rand.nextInt(maxValue + 1));
            }
        }

        // Create tiles to populate board
        // Also calculate totals like bomb counts and max score
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int randVal = rand.nextInt(tileVals.size());
                int tileVal = tileVals.get(randVal);
                tileVals.remove(randVal);
                tiles[i][j] = new Tile(activity, parentLayout, context.getApplicationContext(), spaceBetweenTiles * j, spaceBetweenTiles * i + height, tileSize, tileVal);
                rowSums[i] += tileVal;
                colSums[j] += tileVal;
                if (tileVal == 0) {
                    rowBombs[i] += 1;
                    colBombs[j] += 1;
                } else {
                    maxScore *= tileVal;
                }
            }
        }

        GameDataHandler gdh = null;
        gdh = new GameDataHandler(context);
        System.out.println("The max score is " + maxScore);
        gdh.storeData(context);
    }

    public int getRowSum(int rowNum) {
        return rowSums[rowNum];
    }

    public int getColSums(int colNum) {
        return colSums[colNum];
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void draw(Context context) {

        ImageView img;
        RelativeLayout.LayoutParams layoutParams;

        // Draw all of the Tile Connectors
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                // Tile Connector Horizontal Image
                img = new ImageView(context);
                img.setImageResource(R.drawable.tile_connector_horizontal);
                layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
                layoutParams.setMargins(spaceBetweenTiles * j + hPipeOffsetLeft, spaceBetweenTiles * i + height, 0, 0);
                img.setLayoutParams(layoutParams);
                parentLayout.addView(img);

                // Tile Connector Vertical Image
                img = new ImageView(context);
                img.setImageResource(R.drawable.tile_connector_vertical);
                layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
                layoutParams.setMargins(spaceBetweenTiles * j, spaceBetweenTiles * i + hPipeOffsetLeft + height, 0, 0);
                img.setLayoutParams(layoutParams);
                parentLayout.addView(img);
            }
        }

        // Draw all of the Tiles and Description Tiles
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                // Tile Image and Text
                this.tiles[i][j].draw();
            }

            // Draw Row Description Tile
            drawDescriptionTile(context, spaceBetweenTiles * boardSize, spaceBetweenTiles * i + height, tileSize, rowSums[i], rowBombs[i]);

            // Draw Column Description Tile
            drawDescriptionTile(context, spaceBetweenTiles * i, spaceBetweenTiles * boardSize + height, tileSize, colSums[i], colBombs[i]);

        }
    }

    private void drawDescriptionTile(Context context, int x, int y, int tileSize, int sum, int bombs) {

        int textSize = (int) (tileSize * 0.125);
        // Description Box Image
        ImageView img = new ImageView(context);
        img.setImageResource(R.drawable.description_tile);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
        layoutParams.setMargins(x, y, 0, 0);
        img.setLayoutParams(layoutParams);
        parentLayout.addView(img);

        // Sums Text
        TextView text = new TextView(context);
        text.setTextSize(textSize);
        text.setTextColor(context.getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.END);
        String colSum = "" + sum;
        text.setText(colSum);
        layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
        layoutParams.setMargins(x - 32, y, 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Bombs Text
        text = new TextView(context);
        text.setTextSize(textSize);
        text.setTextColor(context.getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.END);
        String colBomb = "" + bombs;
        text.setText(colBomb);
        layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
        layoutParams.setMargins(x - 32, y + (int)(tileSize * 0.5), 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);
    }

    public void destroy(RelativeLayout parentLayout) {
        parentLayout.removeAllViews();
    }
}

