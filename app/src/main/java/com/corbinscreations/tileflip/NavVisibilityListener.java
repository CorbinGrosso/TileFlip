package com.corbinscreations.tileflip;

import android.app.Activity;
import android.view.View;

public class NavVisibilityListener implements View.OnSystemUiVisibilityChangeListener {

    private final Activity activity;

    public NavVisibilityListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        // if navigation bar is visible
        if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
            // hide the navigation bar
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
