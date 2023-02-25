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

public class GameScreenActivity extends AppCompatActivity {
    private static int totalScore;
    private int roundScore;
    private Board board;
    private RelativeLayout gameBoardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        totalScore = 0;
        roundScore = 0;
        gameBoardLayout = findViewById(R.id.gameboard_layout);
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

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        drawScoreboard();

        try {
            drawGameBoard();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void drawScoreboard() {

        // Creating the layout
        RelativeLayout titleLayout = findViewById(R.id.title_layout);

        // Total Score Text
        TextView text = new TextView(this);
        text.setText(R.string.scoreboard_total);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.START);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 128, 0, 0);
        text.setLayoutParams(layoutParams);
        titleLayout.addView(text);

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
        titleLayout.addView(text);

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
        drawGameBoard();
    }

    private void drawAnnouncement(boolean win) {
        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.title_screen_button);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(768, 192);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        img.setLayoutParams(layoutParams);
        gameBoardLayout.addView(img);

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
        gameBoardLayout.addView(text);

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
        gameBoardLayout.addView(text);
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
        int[] scores = sdh.getScores();
        scores = insert(scores, finalScore);
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

}
