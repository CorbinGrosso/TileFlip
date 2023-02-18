package com.myproject.tileflip;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.myproject.tileflip.databinding.ActivityMainBinding;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleScreenActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_screen);

        // Creating the layout
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

        // Title
        TextView text = new TextView(this);
        text.setText(R.string.app_name);
        text.setTextSize(64);
        text.setTextColor(Color.rgb(100, 150, 250));
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
        text.setTextColor(Color.rgb(100, 150, 250));
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(64, 256);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0, 576, 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);

        // set up arrays with information for making the buttons
        String[] strings = {"Start Game", "Statistics", "Options", "How to Play"};

        for (int i = 0; i < 4; i++) {
            // Button Image
            img = new ImageView(this);
            img.setImageResource(R.drawable.title_screen_button);
            layoutParams = new RelativeLayout.LayoutParams(256*3, 256);
            layoutParams.setMargins(0, 512 + 256 * (i + 2), 0, 0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            img.setLayoutParams(layoutParams);

            // Make image respond to being tapped
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), GameScreenActivity.class);
                    startActivity(intent);
                }
            });

            parentLayout.addView(img);

            // Button Text
            text = new TextView(this);
            text.setText(strings[i]);
            text.setTextSize(48);
            text.setTextColor(Color.rgb(100, 150, 250));
            text.setGravity(Gravity.CENTER);
            layoutParams = new RelativeLayout.LayoutParams(256*3, 256);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.setMargins(128, 512 + 256 * (i + 2), 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);
        }

//        ImageView imgview = new ImageView(this);


//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        Board board = new Board(3, 3);
//        board.draw();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}