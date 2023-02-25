package com.myproject.tileflip;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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

public class GameOverScreenActivity extends AppCompatActivity {

    private RelativeLayout titleLayout, buttonLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_screen);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        // Creating the layout
        titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        buttonLayout = (RelativeLayout) findViewById(R.id.button_layout);

        drawTitle();

        try {
            drawScore();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        drawPlayAgainButton();

        drawMainMenuButton();

    }

    private void drawTitle() {
        TextView text = new TextView(getApplicationContext());
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextSize(64);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setText(R.string.game_over);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 64, 0, 0);
        text.setLayoutParams(layoutParams);
        titleLayout.addView(text);
    }

    private void drawScore() throws JSONException, IOException {

        StatisticsDataHandler sdh = new StatisticsDataHandler(getApplicationContext());
        String scoreString = getResources().getString(R.string.score_text) + sdh.getMostRecentScore();

        TextView text = new TextView(getApplicationContext());
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextSize(48);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setText(scoreString);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(960, 256);
        layoutParams.setMargins(0, 512, 0, 0);
        text.setLayoutParams(layoutParams);
        titleLayout.addView(text);
    }

    private void drawPlayAgainButton() {
        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.game_over_button);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(384, 384);
        layoutParams.setMargins(0, 0, 512, 128);
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
        text.setTextSize(32);
        text.setText(R.string.play_again);
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(384, 384);
        layoutParams.setMargins(0, 0, 512, 128);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        text.setLayoutParams(layoutParams);
        buttonLayout.addView(text);
    }

    private void drawMainMenuButton() {
        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.game_over_button);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(384, 384);
        layoutParams.setMargins(512, 0, 0, 128);
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
        text.setTextSize(32);
        text.setText(R.string.main_menu);
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(384, 384);
        layoutParams.setMargins(512, 0, 0, 128);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        text.setLayoutParams(layoutParams);
        buttonLayout.addView(text);
    }

}
