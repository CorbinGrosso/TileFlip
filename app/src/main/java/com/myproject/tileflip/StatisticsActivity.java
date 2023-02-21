package com.myproject.tileflip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;

public class StatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_screen);

        StatisticsDataHandler sdh = null;
        try {
            sdh = new StatisticsDataHandler(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        // Creating the layout
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

        // Title
        TextView text = new TextView(this);
        text.setText(R.string.statistics_button);
        text.setTextSize(64);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(768, 256);
        layoutParams.setMargins(0, 256, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Display Boards Cleared
        text = new TextView(this);
        String boardsClearedStr = "Boards Cleared: " + sdh.getBoardsCleared();
        text.setText(boardsClearedStr);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(1024, 1024);
        layoutParams.setMargins(32, 640, 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Display Top Scores
        int[] scoresList = sdh.getScores();
        String scoresString = "";
        for (int i = 0; i < 10; i++) {
            scoresString += (i+1);
            scoresString += ". ";
            scoresString += scoresList[i];
            scoresString += "\n";
        }
        scoresString = scoresString.substring(0, scoresString.length() - 1);
        text = new TextView(this);
        text.setText(scoresString);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(1024, 1024);
        layoutParams.setMargins(32, 800, 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Back Button
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.left_arrow);
        layoutParams = new RelativeLayout.LayoutParams(128, 128);
        layoutParams.setMargins(16, 0, 0, 16);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        img.setLayoutParams(layoutParams);

        // Make image respond to being tapped
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TitleScreenActivity.class);
                startActivity(intent);
            }
        });

        parentLayout.addView(img);
    }
}

