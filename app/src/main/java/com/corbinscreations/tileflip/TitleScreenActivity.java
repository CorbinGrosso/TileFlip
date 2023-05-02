package com.corbinscreations.tileflip;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;

import java.io.IOException;

public class TitleScreenActivity extends AppCompatActivity {

    public BackgroundMusicPlayer bmp;

    private int screenWidth, screenHeight, textSize;
    private RelativeLayout parentLayout, startOptionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_screen);

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

        // get option data handler to get volume for background music
        OptionsDataHandler odh;
        try {
            odh = new OptionsDataHandler(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        int volume = odh.getVolume();

        // Load Background Music and set the volume
        bmp = (BackgroundMusicPlayer) getApplication();
        bmp.setVolume(volume);

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
        parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

        // calculate text sizes
        int titleSize = (int)(screenWidth * 0.075);
        textSize = (int)(screenWidth * 3 / 64);

        // Title
        TextView text = new TextView(this);
        text.setText(R.string.app_name);
        text.setTextSize(titleSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layoutParams.setMargins(0, (int)(screenHeight * 0.1), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Decorative Tile
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.tile);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.2), (int)(screenHeight * 0.2));
        layoutParams.setMargins(0, (int)(screenHeight * 0.2), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        img.setLayoutParams(layoutParams);
        parentLayout.addView(img);

        // Decorative Tile Text
        text = new TextView(this);
        text.setText(R.string.val_unknown);
        text.setTextSize(textSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.2), (int)(screenHeight * 0.2));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0, (int)(screenHeight * 0.2), 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Make popup menu for new/continue game option and hide it for use with the start button
        makeStartOptions();

        // Make the start button
        makeStartButton();

        // Create all of the buttons on the screen except the start button
        makeButtons();
    }

    public void onPause() {
        super.onPause();
        bmp.pause();
    }

    public void onResume() {
        super.onResume();
        bmp.resume();
    }

    @Override
    public void onBackPressed() {
        // Do nothing (prevent going back to previous activity in uncontrolled ways)
    }

    private void makeButtons() {

        // set up arrays with information for making the buttons
        int[] strings = {R.string.statistics_button, R.string.options_button, R.string.how_to_play_button};
        Class[] activities = {StatisticsActivity.class, OptionsActivity.class, HowToPlayActivity.class};
        ImageView img;
        TextView text;
        RelativeLayout.LayoutParams layoutParams;

        for (int i = 0; i < 3; i++) {
            // Button Image
            img = new ImageView(this);
            img.setImageResource(R.drawable.title_screen_button);
            layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.8), (int)(screenHeight * 0.15));
            layoutParams.setMargins(0, (int)(screenHeight * 0.15 * (i + 1) + screenHeight * 0.375), 0, 0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            img.setLayoutParams(layoutParams);

            // Make image respond to being tapped
            Class activity = activities[i];
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), activity);
                    startActivity(intent);
                }
            });

            parentLayout.addView(img);

            // Button Text
            text = new TextView(this);
            text.setText(strings[i]);
            text.setTextSize(textSize);
            text.setTextColor(getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.CENTER);
            layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.8), (int)(screenHeight * 0.15));
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.setMargins(0, (int)(screenHeight * 0.15 * (i + 1) + screenHeight * 0.375), 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);
        }
    }

    private void makeStartOptions() {
        startOptionsLayout = findViewById(R.id.start_options_layout);

        startOptionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOptionsLayout.setVisibility(View.GONE);
            }
        });

        // pop-up box
        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.start_options);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.95), (int)(screenHeight * 0.95));
        layoutParams.setMargins(0, (int)(screenHeight * 0.075), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        img.setLayoutParams(layoutParams);
        startOptionsLayout.addView(img);

        // continue button image
        img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.title_screen_button);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.8), (int)(screenHeight * 0.15));
        layoutParams.setMargins(0, (int)(screenHeight * 0.55), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        img.setLayoutParams(layoutParams);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GameDataHandler gdh;
                try {
                    gdh = new GameDataHandler(getApplicationContext());
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
                gdh.setContinue(true);
                try {
                    gdh.storeData(getApplicationContext());
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(getApplicationContext(), GameScreenActivity.class);
                startActivity(intent);
            }
        });
        startOptionsLayout.addView(img);

        // continue button text
        TextView text = new TextView(getApplicationContext());
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setTextSize(textSize);
        text.setText(R.string.continue_game);
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.8), (int)(screenHeight * 0.15));
        layoutParams.setMargins(0, (int)(screenHeight * 0.55), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        startOptionsLayout.addView(text);

        // new game button image
        img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.title_screen_button);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.8), (int)(screenHeight * 0.15));
        layoutParams.setMargins(0, (int)(screenHeight * 0.4), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        img.setLayoutParams(layoutParams);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GameDataHandler gdh;
                try {
                    gdh = new GameDataHandler(getApplicationContext());
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
                gdh.setContinue(false);
                try {
                    gdh.storeData(getApplicationContext());
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(getApplicationContext(), GameScreenActivity.class);
                startActivity(intent);
            }
        });
        startOptionsLayout.addView(img);

        // new game button text
        text = new TextView(getApplicationContext());
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setTextSize(textSize);
        text.setText(R.string.new_game);
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.8), (int)(screenHeight * 0.15));
        layoutParams.setMargins(0, (int)(screenHeight * 0.4), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        startOptionsLayout.addView(text);

        startOptionsLayout.setVisibility(View.GONE);
    }

    private void makeStartButton() {

        ImageView img;
        TextView text;
        RelativeLayout.LayoutParams layoutParams;

        // Button Image
        img = new ImageView(this);
        img.setImageResource(R.drawable.title_screen_button);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.8), (int)(screenHeight * 0.15));
        layoutParams.setMargins(0, (int)(screenHeight * 0.375), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        img.setLayoutParams(layoutParams);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOptionsLayout.setVisibility(View.VISIBLE);
            }
        });

        parentLayout.addView(img);

        // Button Text
        text = new TextView(this);
        text.setText(R.string.start_button);
        text.setTextSize(textSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.8), (int)(screenHeight * 0.15));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0, (int)(screenHeight * 0.375), 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);
    }

}