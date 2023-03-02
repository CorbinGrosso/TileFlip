package com.myproject.tileflip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;

public class OptionsActivity extends AppCompatActivity {
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);

        OptionsDataHandler odh;
        try {
            odh = new OptionsDataHandler(getApplicationContext());
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        // Creating the layout
        RelativeLayout parentLayout = findViewById(R.id.parent_layout);

        // Title
        TextView text = new TextView(this);
        text.setText(R.string.options_button);
        text.setTextSize(64);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(640, 256);
        layoutParams.setMargins(0, 256, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Display Board Size Option
        text = new TextView(this);
        text.setText(R.string.boardSizeOption);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(1024, 1024);
        layoutParams.setMargins(32, 640, 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Allow for user to alter the value of the option
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER); // This sets the input type to be numeric
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1), new OptionsInputFilter(3, 5)});
        String boardSizeValue = "" + odh.getBoardSize();
        editText.setText(boardSizeValue);
        editText.setTextSize(32);
        editText.setTextColor(getResources().getColor(R.color.text_color, null));
        editText.addTextChangedListener(new OptionsWatcher(getApplicationContext(), "boardSize"));
        editText.setBackgroundColor(getResources().getColor(R.color.enter_value_background_color, null));
        editText.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(64, 144);
        layoutParams.setMargins(0, 640, 32, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        editText.setLayoutParams(layoutParams);
        parentLayout.addView(editText);

        // Display Highest Value Multiplier Option
        text = new TextView(this);
        text.setText(R.string.highestValueMultiplierOption);
        text.setTextSize(32);
        text.setTextColor(getResources().getColor(R.color.text_color, null));
        layoutParams = new RelativeLayout.LayoutParams(1024, 1024);
        layoutParams.setMargins(32, 864, 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // Allow for user to alter the value of the option
        editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER); // This sets the input type to be numeric
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1), new OptionsInputFilter(3, 7)});
        String highestValueMultiplierValue = "" + odh.getHighestValueMultiplier();
        editText.setText(highestValueMultiplierValue);
        editText.setTextSize(32);
        editText.setTextColor(getResources().getColor(R.color.text_color, null));
        editText.addTextChangedListener(new OptionsWatcher(getApplicationContext(), "highestValueMultiplier"));
        editText.setBackgroundColor(getResources().getColor(R.color.enter_value_background_color, null));
        editText.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(64, 144);
        layoutParams.setMargins(0, 864, 32, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        editText.setLayoutParams(layoutParams);
        parentLayout.addView(editText);

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
