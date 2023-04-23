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

public class GameOverScreenActivity extends AppCompatActivity {

    private RelativeLayout titleLayout, buttonLayout;
    private int screenWidth, screenHeight, titleSize, scoreSize, buttonTextSize;
    private BackgroundMusicPlayer bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_screen);

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
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // Creating the layout
        titleLayout = findViewById(R.id.title_layout);
        buttonLayout = findViewById(R.id.button_layout);

        // calculating text sizes
        titleSize = (int)(screenWidth * 0.075);
        scoreSize = (int)(screenHeight * 0.03);
        buttonTextSize = (int)(screenHeight * 0.02);

        drawTitle();

        try {
            drawScore();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        drawPlayAgainButton();

        drawMainMenuButton();

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

    private void drawTitle() {
        TextView text = new TextView(getApplicationContext());
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextSize(titleSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setText(R.string.game_over);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layoutParams.setMargins(0, (int)(screenHeight * 0.05), 0, 0);
        text.setLayoutParams(layoutParams);
        titleLayout.addView(text);
    }

    private void drawScore() throws JSONException, IOException {

        StatisticsDataHandler sdh = new StatisticsDataHandler(getApplicationContext());
        String scoreString = getResources().getString(R.string.score_text) + sdh.getMostRecentScore();

        TextView text = new TextView(getApplicationContext());
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextSize(scoreSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setText(scoreString);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layoutParams.setMargins(0, (int)(screenHeight * 0.35), 0, 0);
        text.setLayoutParams(layoutParams);
        titleLayout.addView(text);
    }

    private void drawPlayAgainButton() {
        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.game_over_button);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.4), (int)(screenHeight * 0.4));
        layoutParams.setMargins(0, 0, (int)(screenWidth * 0.45), (int)(screenHeight * 0.15));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        img.setLayoutParams(layoutParams);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameScreenActivity.class);
                startActivity(intent);
            }
        });
        buttonLayout.addView(img);

        TextView text = new TextView(getApplicationContext());
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setTextSize(buttonTextSize);
        text.setText(R.string.play_again);
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.4), (int)(screenHeight * 0.4));
        layoutParams.setMargins(0, 0, (int)(screenWidth * 0.45), (int)(screenHeight * 0.15));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        text.setLayoutParams(layoutParams);
        buttonLayout.addView(text);
    }

    private void drawMainMenuButton() {
        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.game_over_button);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.4), (int)(screenHeight * 0.4));
        layoutParams.setMargins((int)(screenWidth * 0.45), 0, 0, (int)(screenHeight * 0.15));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        img.setLayoutParams(layoutParams);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TitleScreenActivity.class);
                startActivity(intent);
            }
        });
        buttonLayout.addView(img);

        TextView text = new TextView(getApplicationContext());
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setTextSize(buttonTextSize);
        text.setText(R.string.main_menu);
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.4), (int)(screenHeight * 0.4));
        layoutParams.setMargins((int)(screenWidth * 0.45), 0, 0, (int)(screenHeight * 0.15));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        text.setLayoutParams(layoutParams);
        buttonLayout.addView(text);
    }

    @Override
    public void onBackPressed() {
        // Do nothing (prevent going back to previous activity in uncontrolled ways)
    }

}
