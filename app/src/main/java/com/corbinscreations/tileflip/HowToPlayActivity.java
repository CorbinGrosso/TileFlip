package com.corbinscreations.tileflip;

import static java.lang.Math.min;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;

public class HowToPlayActivity extends AppCompatActivity {
    private int screenWidth, screenHeight, textSize, tileSize;
    private RelativeLayout[] pages;
    private RelativeLayout boardLayout, toolboxLayout;
    private BackgroundMusicPlayer bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play_screen);

        // Initialize Google Admobs
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Load ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Load background music
        bmp = (BackgroundMusicPlayer) getApplication();

        // Hide navigation bar
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new NavVisibilityListener(this));
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // get screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // calculating text sizes and tile size
        int titleSize = (int)(screenWidth * 0.06);
        textSize = (int)(screenHeight * 0.015);
        tileSize = min((int)(screenWidth / 8.0), (int)(screenHeight * 0.1));

        // Creating the parent layout (layout common to all pages)
        RelativeLayout parentLayout = findViewById(R.id.parent_layout);

        // Creating the board layout  and toolbox layout (used so that these things are centered)
        boardLayout = findViewById(R.id.htp_board_layout);
        toolboxLayout = findViewById(R.id.htp_toolbox_layout);

        // Create list of each page layout
        pages = new RelativeLayout[]{findViewById(R.id.page1), findViewById(R.id.page2), findViewById(R.id.page3), findViewById(R.id.page4)};

        /* *************************
         Content common to all pages
         ***************************/

        // Title
        TextView text = new TextView(this);
        text.setText(R.string.how_to_play_button);
        text.setTextSize(titleSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layoutParams.setMargins(0, (int)(screenHeight * 0.05), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        /* ************
         Page 1 Content
         **************/

        // draw an example board
        Board board;
        try {
            board = new Board(this, boardLayout, getApplicationContext(), (int) (screenHeight * 0.25));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        board.draw();

        createText("This is the game board", 0, (int)(screenHeight * 0.7), 0, 0, 0);

        // Back Button (return to main menu)
        createButton(true, 0);

        // Page Number
        createPageNumber(0);

        // Next Page Button
        createButton(false, 0);

        /* ************
         Page 2 Content
         **************/

        createText("This is a tile", 0, (int)(screenHeight * 0.25), 0, 0, 1);

        Tile tile = new Tile(pages[1], getApplicationContext(), (int)(screenWidth * 0.5) - tileSize / 2, (int)(screenHeight * 0.325), tileSize, 0);
        tile.draw();

        createText("Hidden underneath it is a points multiplier", 0, (int)(screenHeight * 0.4), 0, 0, 1);
        createText("If that multiplier is 0 (a bomb), it's game over", 0, (int)(screenHeight * 0.55), 0, 0, 1);
        createText("Flip over every value greater than 1 to clear the board!", 0, (int)(screenHeight * 0.7), 0, 0, 1);


        // Previous Page Button
        createButton(true, 1);

        // Page Number
        createPageNumber(1);

        // Next Page Button
        createButton(false, 1);

        /* ************
         Page 3 Content
         **************/

        createText("This is a\ndescription tile", 0, (int)(screenHeight * 0.25), 0, 0, 2);

        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.description_tile);
        layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
        layoutParams.setMargins(0, (int)(screenHeight * 0.4), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        img.setLayoutParams(layoutParams);
        pages[2].addView(img);

        createText("The top slot tells you the sum of all tile values in its row or column", 0, (int)(screenHeight * 0.5), 0, 0, 2);
        createText("The bottom slot tells you the number of bombs in its row or column", 0, (int)(screenHeight * 0.7), 0, 0, 2);

        // Previous Page Button
        createButton(true, 2);

        // Page Number
        createPageNumber(2);

        // Next Page Button
        createButton(false, 2);

        /* ************
         Page 4 Content
         **************/

        try {
            drawToolbox(toolboxLayout, (int)(screenHeight * 0.25));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        createText("This is the toolbox. Use it to mark tiles with what you think they might be!", 0, (int)(screenHeight * 0.5), 0, 0, 3);
        createText("Just tap what mark you want to give and then any tiles you want to mark", 0, (int)(screenHeight * 0.7), 0, 0, 3);

        // Previous Page Button
        createButton(true, 3);

        // Page Number
        createPageNumber(3);

        /* ********************************************
         Set all content to invisible except for page 1
         **********************************************/
        for (int i = 1; i < pages.length; i++) {
            pages[i].setVisibility(View.GONE);
        }
        toolboxLayout.setVisibility(View.GONE);
    }

    private void createButton(boolean isPrevDirection, int pageIndex) {
        ImageView img = new ImageView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.1), (int)(screenHeight * 0.1));
        if (isPrevDirection) {
            img.setImageResource(R.drawable.left_arrow);
            if (pageIndex == 3) {
                layoutParams.setMargins((int)(screenWidth * 0.05), 0, (int)(screenWidth * 0.85), (int) (screenHeight * 0.05));
            } else {
                layoutParams.setMargins((int) (screenWidth * 0.05), 0, 0, (int) (screenHeight * 0.05));
            }
        } else {
            img.setImageResource(R.drawable.right_arrow);
            layoutParams.setMargins((int)(screenWidth * 0.85), 0, (int)(screenWidth * 0.05), (int)(screenHeight * 0.05));
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        img.setLayoutParams(layoutParams);

        // Make image respond to being tapped
        // if this is a left arrow
        if (isPrevDirection) {
            // if we're on page 1, then tapping should return to the main menu
            if (pageIndex == 0) {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), TitleScreenActivity.class);
                        startActivity(intent);
                    }
                });
            // if we're not on page 1, tapping should bring us to the previous page
            } else {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pages[pageIndex].setVisibility(View.GONE);
                        pages[pageIndex - 1].setVisibility(View.VISIBLE);
                        if (pageIndex == 1) {
                            boardLayout.setVisibility(View.VISIBLE);
                        } else if (pageIndex == 3) {
                            toolboxLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        // if it's a right arrow, bring us to the next page
        } else {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pages[pageIndex].setVisibility(View.GONE);
                    pages[pageIndex + 1].setVisibility(View.VISIBLE);
                    if (pageIndex == 0) {
                        boardLayout.setVisibility(View.GONE);
                    } else if (pageIndex == 2) {
                        toolboxLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        pages[pageIndex].addView(img);
    }

    private void createText(String msg, int left, int top, int right, int bottom, int pageIndex) {

        TextView text = new TextView(getApplicationContext());
        text.setText(msg);
        text.setTextColor(getColor(R.color.text_color));
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextSize(textSize);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, (int)(screenHeight * 0.2));
        layoutParams.setMargins(left, top, right, bottom);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        pages[pageIndex].addView(text);
    }

    private void drawToolbox(RelativeLayout layout, int height) throws IOException {

        // Label the section of the screen as the Toolbox
        TextView text = new TextView(this);
        text.setText(R.string.toolbox);
        text.setTextColor(getColor(R.color.text_color));
        text.setTextSize(textSize);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, (int)(screenHeight * 0.1));
        layoutParams.setMargins((int)(screenWidth * 0.025), height, 0, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        text.setLayoutParams(layoutParams);
        layout.addView(text);

        for (int i = 0; i < 4; i++) {
            drawMemoButton((int) (tileSize * i * 1.25 + tileSize * 0.25), height + tileSize, i, tileSize, layout);
        }

        drawFlipButton((int)(tileSize * 1.25 * 4 + tileSize * 0.25), height + tileSize, tileSize, layout);
    }

    private void drawMemoButton(int x, int y, int val, int tileSize, RelativeLayout layout) {

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.tile);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
        layoutParams.setMargins(x, y, 0, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        img.setLayoutParams(layoutParams);
        layout.addView(img);

        // put value on button
        if (val == 0) {
            img = new ImageView(this);
            img.setImageResource(R.drawable.bomb_memo_icon);
            img.setLayoutParams(layoutParams);
            layout.addView(img);
        } else {
            TextView text = new TextView(this);
            String textString = "" + val;
            text.setText(textString);
            text.setTextSize(24);
            text.setGravity(Gravity.CENTER);
            text.setTextColor(getColor(R.color.text_color));
            text.setLayoutParams(layoutParams);
            layout.addView(text);
        }
    }

    private void drawFlipButton(int x, int y, int tileSize, RelativeLayout layout) {
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.tile_flip_button_pressed);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(tileSize * 2.25), (int)(tileSize * 2.25));
        layoutParams.setMargins(x, y, (int)(tileSize * 0.5), 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        img.setLayoutParams(layoutParams);
        layout.addView(img);

        TextView text = new TextView(this);
        text.setText(R.string.flip_button);
        text.setTextColor(getColor(R.color.memo_selected_color));
        text.setTextSize(24);
        text.setGravity(Gravity.CENTER);
        text.setLayoutParams(layoutParams);
        layout.addView(text);
    }

    public void onPause() {
        super.onPause();
        bmp.pause();
    }

    public void onResume() {
        super.onResume();
        bmp.resume();
    }

    private void createPageNumber(int pageIndex) {
        String str = "" + (pageIndex + 1) + " / 4";
        createText(str, 0, (int)(screenHeight * 0.9), 0, 0, pageIndex);
    }

}