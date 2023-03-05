package com.myproject.tileflip;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_screen);

        // Hide navigation bar
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new NavVisibilityListener(this));
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Creating the layout
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

        // Title
        TextView text = new TextView(this);
        text.setText(R.string.app_name);
        text.setTextSize(64);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(640, 256);
        layoutParams.setMargins(0, 256, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Decorative Tile
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.tile);
        layoutParams = new RelativeLayout.LayoutParams(256, 256);
        layoutParams.setMargins(0, 576, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        img.setLayoutParams(layoutParams);
        parentLayout.addView(img);

        // Decorative Tile Text
        text = new TextView(this);
        text.setText(R.string.val_unknown);
        text.setTextSize(48);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(64, 256);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0, 576, 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // set up arrays with information for making the buttons
        int[] strings = {R.string.start_button, R.string.statistics_button, R.string.options_button, R.string.how_to_play_button};
        Class[] activities = {GameScreenActivity.class, StatisticsActivity.class, OptionsActivity.class, HowToPlayActivity.class};

        for (int i = 0; i < 4; i++) {
            // Button Image
            img = new ImageView(this);
            img.setImageResource(R.drawable.title_screen_button);
            layoutParams = new RelativeLayout.LayoutParams(256*3, 256);
            layoutParams.setMargins(0, 512 + 256 * (i + 2), 0, 0);
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
            text.setTextSize(48);
            text.setTextColor(getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.CENTER);
            layoutParams = new RelativeLayout.LayoutParams(256*3, 256);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.setMargins(128, 512 + 256 * (i + 2), 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);
        }

    }

}