package com.myproject.tileflip;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;

public class GameScreenActivity extends AppCompatActivity {
    private int totalScore = 0, roundScore = 0;
    private Board board;
    private RelativeLayout gameBoardLayout;
    private RelativeLayout announcementLayout;
    private RelativeLayout memoLayout;
    private int[] memoIDs;
    private int flipButtonID;
    private boolean[] memoIsSelected;
    private boolean flipButtonIsSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        // Hide navigation bar
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new NavVisibilityListener(this));
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // set up layouts
        gameBoardLayout = findViewById(R.id.gameboard_layout);
        announcementLayout = findViewById(R.id.announcement_layout);
        memoLayout = findViewById(R.id.memo_layout);

        // reset values in the game data handler to clear any leftover data from a previous game
        GameDataHandler gdh;
        try {
            gdh = new GameDataHandler(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        gdh.resetValues();
        try {
            gdh.storeData(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        // Create IDs for all of the buttons in the toolbox
        OptionsDataHandler odh;
        try {
            odh = new OptionsDataHandler(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        int highestValueMultiplier = odh.getHighestValueMultiplier();
        memoIDs = new int[highestValueMultiplier + 1];
        for (int i = 0; i < highestValueMultiplier; i++) {
            memoIDs[i] = View.generateViewId();
        }
        flipButtonID = View.generateViewId();

        // set all memo buttons as unselected
        memoIsSelected = new boolean[highestValueMultiplier + 1];
        for (int i = 0; i < highestValueMultiplier; i++) {
            memoIsSelected[i] = false;
        }

        // draw the scoreboard, game board, and toolbox
        drawScoreboard();

        try {
            drawGameBoard();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            drawToolbox();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void drawScoreboard() {

        // Creating the layout
        RelativeLayout scoreboardLayout = findViewById(R.id.scoreboard_layout);

        // Total Score Text
        TextView text = new TextView(this);
        text.setText(R.string.scoreboard_total);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.START);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 128, 0, 0);
        text.setLayoutParams(layoutParams);
        scoreboardLayout.addView(text);

        // Total Score Value
        text = findViewById(R.id.total_score_value);
        String totalScoreText = "" + totalScore;
        text.setText(totalScoreText);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.END);
        layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 128, 0, 0);
        text.setLayoutParams(layoutParams);

        // Round Score Text
        text = new TextView(this);
        text.setText(R.string.scoreboard_round);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.START);
        layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 256, 0, 0);
        text.setLayoutParams(layoutParams);
        scoreboardLayout.addView(text);

        // Round Score Value
        text = findViewById(R.id.round_score_value);
        String roundScoreText = "" + roundScore;
        text.setText(roundScoreText);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.END);
        layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 256, 0, 0);
        text.setLayoutParams(layoutParams);
    }

    public void updateRoundScore(int value) throws JSONException, IOException {
        GameDataHandler gdh = new GameDataHandler(getApplicationContext());
        gdh.updateRoundScore(value);
        gdh.storeData(getApplicationContext());
        roundScore = gdh.getRoundScore();
        TextView text = findViewById(R.id.round_score_value);
        String roundValue = "" + roundScore;
        text.setText(roundValue);
        if (roundScore == board.getMaxScore()) {
            incrementBoardsClearedStatistic();
            drawAnnouncement(true);
        }
    }

    private void updateTotalScore() throws JSONException, IOException {
        GameDataHandler gdh = new GameDataHandler(getApplicationContext());
        gdh.updateTotalScore();
        gdh.storeData(getApplicationContext());
        roundScore = gdh.getRoundScore();
        TextView text = findViewById(R.id.round_score_value);
        String roundValue = "" + roundScore;
        text.setText(roundValue);
        totalScore = gdh.getTotalScore();
        text = findViewById(R.id.total_score_value);
        String totalValue = "" + totalScore;
        text.setText(totalValue);

    }

    private void drawGameBoard() throws JSONException, IOException {
        OptionsDataHandler odh;
        try {
            odh = new OptionsDataHandler(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        board = new Board(this, gameBoardLayout, getApplicationContext(), odh.getBoardSize(), odh.getHighestValueMultiplier(), 512);

        board.draw(getApplicationContext());
    }

    public void newBoard() throws JSONException, IOException {
        board.destroy(gameBoardLayout);
        announcementLayout.removeAllViews();
        drawGameBoard();
    }

    private void drawAnnouncement(boolean win) {
        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.title_screen_button);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(768, 192);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        img.setLayoutParams(layoutParams);
        announcementLayout.addView(img);

        TextView text = new TextView(getApplicationContext());
        if (win) {
            text.setText(R.string.board_cleared);
        } else {
            text.setText(R.string.game_over);
        }
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setTextSize(24);
        layoutParams = new RelativeLayout.LayoutParams(768, 192);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        text.setLayoutParams(layoutParams);
        announcementLayout.addView(text);

        text = new TextView(getApplicationContext());
        layoutParams = new RelativeLayout.LayoutParams(960, 1984);
        text.setLayoutParams(layoutParams);
        if (win) {
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        updateTotalScore();
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        newBoard();
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), GameOverScreenActivity.class);
                    startActivity(intent);
                }
            });
        }
        announcementLayout.addView(text);
    }

    private void incrementBoardsClearedStatistic() throws JSONException, IOException {
        StatisticsDataHandler sdh = new StatisticsDataHandler(getApplicationContext());
        sdh.setBoardsCleared(sdh.getBoardsCleared() + 1);
        sdh.storeData(getApplicationContext());
    }

    public void putScoresInStatistics() throws JSONException, IOException {
        StatisticsDataHandler sdh = new StatisticsDataHandler(getApplicationContext());
        int finalScore = totalScore + roundScore;
        sdh.setMostRecentScore(finalScore);
        int[] scores = insert(sdh.getScores(), finalScore);
        sdh.setScores(scores);
        sdh.storeData(getApplicationContext());
    }

    private int[] insert(int[] vals, int val) {
        for (int i = 0; i < vals.length; i++) {
            if (val > vals[i]) {
                int temp = vals[i];
                vals[i] = val;
                val = temp;
            }
        }
        return vals;
    }

    public void gameOver() {
        drawAnnouncement(false);
    }

    private void drawToolbox() throws JSONException, IOException {

        // get highest value multiplier to know what memo buttons need to be made
        OptionsDataHandler odh = new OptionsDataHandler(getApplicationContext());
        int highestValueMultiplier = odh.getHighestValueMultiplier();

        // Label the section of the screen as the Toolbox
        TextView text = new TextView(this);
        text.setText(R.string.toolbox);
        text.setTextColor(getColor(R.color.text_color));
        text.setTextSize(32);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(386,128);
        layoutParams.setMargins(0, 0, 0, 386);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        text.setLayoutParams(layoutParams);
        memoLayout.addView(text);

        for (int i = 0; i < 4; i++) {
            drawMemoButton(160 * i, 224, i);
        }
        for (int i = 4; i <= highestValueMultiplier; i++) {
            drawMemoButton(160 * (i - 4), 64, i);
        }

        drawFlipButton(640, 64);
    }

    private void drawMemoButton(int x, int y, int val) {

        ImageView img = new ImageView(this);
        img.setId(memoIDs[val]);
        img.setImageResource(R.drawable.tile);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(128, 128);
        layoutParams.setMargins(x, 0, 0, y);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        img.setLayoutParams(layoutParams);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!memoIsSelected[val]) {
                    ImageView img;
                    // turn off all memo buttons
                    for (int i = 0; i < memoIDs.length; i++) {
                        img = findViewById(memoIDs[i]);
                        img.setImageResource(R.drawable.tile);
                        memoIsSelected[i] = false;
                    }
                    // turn off flip button
                    img = findViewById(flipButtonID);
                    img.setImageResource(R.drawable.tile_flip_button);
                    flipButtonIsSelected = false;
                    // turn on this memo
                    img = findViewById(memoIDs[val]);
                    img.setImageResource(R.drawable.tile_pressed);
                    memoIsSelected[val] = true;
                }
            }
        });
        memoLayout.addView(img);

        TextView text = new TextView(this);
        String textString = "" + val;
        text.setText(textString);
        text.setTextSize(24);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(getColor(R.color.text_color));
        text.setLayoutParams(layoutParams);
        memoLayout.addView(text);
    }

    private void drawFlipButton(int x, int y) {
        ImageView img = new ImageView(this);
        img.setId(flipButtonID);
        img.setImageResource(R.drawable.tile_flip_button_pressed);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(288, 288);
        layoutParams.setMargins(x, 0, 64, y);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        img.setLayoutParams(layoutParams);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flipButtonIsSelected) {
                    ImageView img;
                    // turn off all memos
                    for (int i = 0; i < memoIDs.length; i++) {
                        img = findViewById(memoIDs[i]);
                        img.setImageResource(R.drawable.tile);
                        memoIsSelected[i] = false;
                    }
                    // turn on the flip button
                    img = findViewById(flipButtonID);
                    img.setImageResource(R.drawable.tile_flip_button_pressed);
                    flipButtonIsSelected = true;
                }
            }
        });
        memoLayout.addView(img);

        TextView text = new TextView(this);
        text.setText(R.string.flip_button);
        text.setTextColor(getColor(R.color.text_color));
        text.setTextSize(24);
        text.setGravity(Gravity.CENTER);
        text.setLayoutParams(layoutParams);
        memoLayout.addView(text);
    }

    public boolean getFlipButtonIsSelected() {
        return flipButtonIsSelected;
    }

    public int getSelectedMemo() {
        for (int i = 0; i < memoIsSelected.length; i++) {
            if (memoIsSelected[i]) {
                return i;
            }
        }
        return -1;
    }

}
