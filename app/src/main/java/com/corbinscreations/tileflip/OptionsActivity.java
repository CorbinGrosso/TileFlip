package com.corbinscreations.tileflip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;

import java.io.IOException;

public class OptionsActivity extends AppCompatActivity {
    private BackgroundMusicPlayer bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);

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

        // Load Background Music
        bmp = (BackgroundMusicPlayer) getApplication();

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

        OptionsDataHandler odh;
        try {
            odh = new OptionsDataHandler(getApplicationContext());
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
        text.setText(R.string.options_button);
        text.setTextSize(titleSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layoutParams.setMargins(0, (int)(screenHeight * 0.05), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Display Board Size Option
        text = new TextView(this);
        text.setText(R.string.boardSizeOption);
        text.setTextSize(textSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(screenWidth, (int)(screenHeight * 0.8));
        layoutParams.setMargins((int)(screenWidth * 0.05), (int)(screenHeight * 0.2), 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Allow for user to alter the value of the option
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER); // This sets the input type to be numeric
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1), new OptionsInputFilter(3, 5)});
        String boardSizeValue = "" + odh.getBoardSize();
        editText.setText(boardSizeValue);
        editText.setTextSize(textSize);
        editText.setTextColor(getResources().getColor(R.color.text_color, null));
        editText.addTextChangedListener(new OptionsWatcher(getApplicationContext(), "boardSize"));
        editText.setBackgroundColor(getResources().getColor(R.color.enter_value_background_color, null));
        editText.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(textSize * 3, textSize * 5);
        layoutParams.setMargins(0, (int)(screenHeight * 0.2), (int)(screenWidth * 0.05), 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        editText.setLayoutParams(layoutParams);
        parentLayout.addView(editText);

        // Display Highest Value Multiplier Option
        text = new TextView(this);
        text.setText(R.string.highestValueMultiplierOption);
        text.setTextSize(textSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(screenWidth, (int)(screenHeight * 0.8));
        layoutParams.setMargins((int)(screenWidth * 0.05), (int)(screenHeight * 0.35), 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Allow for user to alter the value of the option
        editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER); // This sets the input type to be numeric
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1), new OptionsInputFilter(3, 7)});
        String highestValueMultiplierValue = "" + odh.getHighestValueMultiplier();
        editText.setText(highestValueMultiplierValue);
        editText.setTextSize(textSize);
        editText.setTextColor(getResources().getColor(R.color.text_color, null));
        editText.addTextChangedListener(new OptionsWatcher(getApplicationContext(), "highestValueMultiplier"));
        editText.setBackgroundColor(getResources().getColor(R.color.enter_value_background_color, null));
        editText.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(textSize * 3, textSize * 5);
        layoutParams.setMargins(0, (int)(screenHeight * 0.35), (int)(screenWidth * 0.05), 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        editText.setLayoutParams(layoutParams);
        parentLayout.addView(editText);

        // Display Volume
        text = new TextView(this);
        text.setText(R.string.volumeOption);
        text.setTextSize(textSize);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(screenWidth, (int)(screenHeight * 0.8));
        layoutParams.setMargins((int)(screenWidth * 0.05), (int)(screenHeight * 0.5), 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Set up seekbar
        SeekBar volumeSlider = findViewById(R.id.volume_slider);
        System.out.println(odh.getVolume());
        volumeSlider.setProgress(odh.getVolume());
        layoutParams = new RelativeLayout.LayoutParams(textSize * 3, textSize * 5);
        layoutParams.setMargins(0, (int)(screenHeight * 0.5), (int)(screenWidth * 0.05), 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.width = (int) (screenWidth * 0.5);
        volumeSlider.setLayoutParams(layoutParams);
        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the volume based on the progress of the slider
                odh.setVolume(progress);
                bmp.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
                try {
                    odh.storeData(getApplicationContext());
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

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
