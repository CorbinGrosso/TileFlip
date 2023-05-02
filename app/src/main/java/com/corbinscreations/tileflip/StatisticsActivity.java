package com.corbinscreations.tileflip;

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

import org.json.JSONException;

import java.io.IOException;

public class StatisticsActivity extends AppCompatActivity {
    private BackgroundMusicPlayer bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_screen);

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
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        StatisticsDataHandler sdh;
        try {
            sdh = new StatisticsDataHandler(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        // Creating the layout
        RelativeLayout parentLayout = findViewById(R.id.parent_layout);

        // calculating text sizes
        int titleSize = (int)(screenWidth * 0.075);
        int textSize = (int)(screenHeight * 0.02);

        // Title
        TextView text = new TextView(this);
        text.setText(R.string.statistics_button);
        text.setTextSize(titleSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layoutParams.setMargins(0, (int)(screenHeight * 0.05), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Display Boards Cleared
        text = new TextView(this);
        String boardsClearedStr = "Boards Cleared: " + sdh.getBoardsCleared();
        text.setText(boardsClearedStr);
        text.setTextSize(textSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layoutParams.setMargins((int)(screenWidth * 0.05), (int)(screenHeight * 0.175), 0, 0);
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
        text.setTextSize(textSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layoutParams.setMargins((int)(screenWidth * 0.05), (int)(screenHeight * 0.25), 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Back Button
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.left_arrow);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.1), (int)(screenHeight * 0.1));
        layoutParams.setMargins((int)(screenWidth * 0.05), 0, 0, (int)(screenHeight * 0.05));
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

        bmp = (BackgroundMusicPlayer) getApplication();
    }

    public void onPause() {
        super.onPause();
        bmp.pause();
    }

    public void onResume() {
        super.onResume();
        bmp.resume();
    }
}

