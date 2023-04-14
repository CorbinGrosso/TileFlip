package com.myproject.tileflip;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.min;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import android.content.Context;
import java.util.ArrayList;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

public class Board {

    private final Tile[][] tiles;
    private final int[] colSums, rowSums, colBombs, rowBombs;
    private final int boardSize, spaceBetweenTiles, pipeOffset, height;
    private final int tileSize;
    private int maxScore = 1;
    private final RelativeLayout parentLayout;
    private final Context context;

    public Board(GameScreenActivity activity, RelativeLayout parentLayout, RelativeLayout blockerLayout, Context context, int boardSize, int maxValue, int height, int[][] tileVals) throws JSONException, IOException {

        tiles = new Tile[boardSize][boardSize];
        Random rand = new Random();
        colSums = new int[boardSize];
        rowSums = new int[boardSize];
        colBombs = new int[boardSize];
        rowBombs = new int[boardSize];
        this.boardSize = boardSize;
        this.height = height;
        this.parentLayout = parentLayout;
        this.context = context;

        // calculate sizes of drawables
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int boardScreenSize = min(displayMetrics.widthPixels, (int) (displayMetrics.heightPixels * 0.8));
        tileSize = boardScreenSize / (boardSize + 3);
        spaceBetweenTiles = (int)(tileSize * 1.25);
        pipeOffset = (int)(tileSize * 0.625);

        // initialize arrays to all 0s
        for (int i = 0; i < boardSize; i++) {
            colSums[i] = 0;
            rowSums[i] = 0;
            colBombs[i] = 0;
            rowBombs[i] = 0;
        }

        if (tileVals != null) {
            // Create tiles to populate board
            // Also calculate totals like bomb counts and max score
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    int tileVal = tileVals[i][j];
                    tiles[i][j] = new Tile(activity, parentLayout, blockerLayout, context, spaceBetweenTiles * j, spaceBetweenTiles * i + height, tileSize, tileVal);
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
        } else {
            // set up arraylist of values to use for the tiles
            ArrayList<Integer> potentialTileVals = new ArrayList<>();
            for (int i = 0; i < boardSize * boardSize; i++) {
                // 30% of the board is guaranteed to be bombs
                if (i < ceil(boardSize * boardSize * 0.3)) {
                    potentialTileVals.add(0);
                // 30% of the board is guaranteed to be a multiplier greater than 1
                } else if (i < ceil(boardSize * boardSize * 7 / 25.0) + ceil(boardSize * boardSize * 0.3)) {
                    int randVal = rand.nextInt(maxValue - 1) + 2;
                    potentialTileVals.add(randVal);
                // 40% of the board is 1s
                } else {
                    potentialTileVals.add(1);
                }
            }

            // Create tiles to populate board
            // Also calculate totals like bomb counts and max score
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    int randVal = rand.nextInt(potentialTileVals.size());
                    int tileVal = potentialTileVals.get(randVal);
                    potentialTileVals.remove(randVal);
                    tiles[i][j] = new Tile(activity, parentLayout, blockerLayout, context, spaceBetweenTiles * j, spaceBetweenTiles * i + height, tileSize, tileVal);
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
        }

        GameDataHandler gdh;
        gdh = new GameDataHandler(context);
        gdh.storeData(context);
    }

    public Board(HowToPlayActivity activity, RelativeLayout parentLayout, Context context, int height) throws JSONException, IOException {

        boardSize = 3;
        tiles = new Tile[boardSize][boardSize];
        colSums = new int[boardSize];
        rowSums = new int[boardSize];
        colBombs = new int[boardSize];
        rowBombs = new int[boardSize];
        this.height = height;
        this.parentLayout = parentLayout;
        this.context = context;

        // Calculate sizes of drawables
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int boardScreenSize = min(displayMetrics.widthPixels, (int) (displayMetrics.heightPixels * 0.8));
        tileSize = boardScreenSize / (boardSize + 3);
        spaceBetweenTiles = (int)(tileSize * 1.25);
        pipeOffset = (int)(tileSize * 0.625);

        // initialize arrays to all 0s
        for (int i = 0; i < boardSize; i++) {
            colSums[i] = 0;
            rowSums[i] = 0;
            colBombs[i] = 0;
            rowBombs[i] = 0;
        }

        // Create tiles to populate board
        // Also set all tiles to a value of 0 for the sake of demonstration
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                tiles[i][j] = new Tile(parentLayout, context.getApplicationContext(), spaceBetweenTiles * j, spaceBetweenTiles * i + height, tileSize, 0);
                rowBombs[i] += 1;
                colBombs[j] += 1;
            }
        }
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void draw() {

        ImageView img;
        RelativeLayout.LayoutParams layoutParams;

        // Draw all of the Tile Connectors
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                // Tile Connector Horizontal Image
                img = new ImageView(context);
                img.setImageResource(R.drawable.tile_connector_horizontal);
                layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
                layoutParams.setMargins(spaceBetweenTiles * j + pipeOffset, spaceBetweenTiles * i + height, 0, 0);
                img.setLayoutParams(layoutParams);
                parentLayout.addView(img);

                // Tile Connector Vertical Image
                img = new ImageView(context);
                img.setImageResource(R.drawable.tile_connector_vertical);
                layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
                layoutParams.setMargins(spaceBetweenTiles * j, spaceBetweenTiles * i + pipeOffset + height, 0, 0);
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
        layoutParams.setMargins(x - (int)(tileSize * 0.1), y + (int)(tileSize * 0.15 - textSize), 0, 0);
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
        layoutParams.setMargins(x - (int)(tileSize * 0.1), y + (int)(tileSize * 0.65 - textSize), 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);
    }

    public void destroy() {
        parentLayout.removeAllViews();
    }

    public int[][] getTileVals() {
        int[][] tileVals = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j =0; j < tiles[i].length; j++) {
                tileVals[i][j] = tiles[i][j].getValue();
            }
        }
        return tileVals;
    }
}

