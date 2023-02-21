package com.myproject.tileflip;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;

public class GameScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        int totalScore = 0;
        int roundScore = 0;

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        OptionsDataHandler odh = null;
        try {
            odh = new OptionsDataHandler(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        Board board = new Board(odh.getBoardSize(), odh.getHighestValueMultiplier());

        // Creating the layout
        RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.title_layout);

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
        text = new TextView(this);
        String totalScoreText = "" + totalScore;
        text.setText(totalScoreText);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.END);
        layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 128, 0, 0);
        text.setLayoutParams(layoutParams);
        titleLayout.addView(text);

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
        text = new TextView(this);
        String roundScoreText = "" + roundScore;
        text.setText(roundScoreText);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.END);
        layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 256, 0, 0);
        text.setLayoutParams(layoutParams);
        titleLayout.addView(text);

        RelativeLayout gameBoardLayout = (RelativeLayout) findViewById(R.id.gameboard_layout);
        layoutParams = new RelativeLayout.LayoutParams(960, 1440);
        layoutParams.setMargins(0, 256, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        board.draw(gameBoardLayout, getApplicationContext());
    }
}
