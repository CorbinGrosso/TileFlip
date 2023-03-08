package com.myproject.tileflip;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HowToPlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play_screen1);

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

        // Creating the layout
        RelativeLayout parentLayout = findViewById(R.id.parent_layout);

        // calculating text sizes
        int titleSize = (int)(screenWidth * 0.075);
        int textSize = (int)(screenHeight * 0.02);

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

        // Decorative Tile
//        ImageView img = new ImageView(this);
//        img.setImageResource(R.drawable.tile);
//        layoutParams = new RelativeLayout.LayoutParams(256, 256);
//        layoutParams.setMargins(0, 576, 0, 0);
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        img.setLayoutParams(layoutParams);
//        parentLayout.addView(img);

        // Back Button
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.left_arrow);
        layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth * 0.1), (int)(screenHeight * 0.1));
        layoutParams.setMargins((int)(screenWidth * 0.05), 0, 0, (int)(screenHeight * 0.01));
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