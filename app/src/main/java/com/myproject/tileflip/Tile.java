package com.myproject.tileflip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

public class Tile {

    private final int value, x, y, tileSize, textSize;
    private int tileImgID, tileValueID, blockerID;
    private boolean isFaceDown = true, isExample = false;
    private RelativeLayout parentLayout, blockerLayout;
    private final Context context;
    private GameScreenActivity activity;
    private HowToPlayActivity htpActivity;
    private final boolean[] memos = {false, false, false, false, false, false, false, false};
    private final int[] memoIDs = new int[8];
    private MediaPlayer mediaPlayer;

    public Tile(GameScreenActivity activity, RelativeLayout parentLayout, RelativeLayout blockerLayout, Context context, int x, int y, int tileSize, int value) throws JSONException, IOException {
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.blockerLayout = blockerLayout;
        this.context = context;
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.value = value;
        textSize = (int)(tileSize * 0.25);

        // get options data handler to get the volume
        OptionsDataHandler odh = new OptionsDataHandler(context);
        int volume = odh.getVolume();

        // create media player for sfx when flipping tile
        mediaPlayer = MediaPlayer.create(context, R.raw.points_gained);
        mediaPlayer.setVolume(volume / 100.0f, volume / 100.0f);
    }

    public Tile(HowToPlayActivity activity, RelativeLayout parentLayout, Context context, int x, int y, int tileSize, int value) {
        htpActivity = activity;
        this.parentLayout = parentLayout;
        this.context = context;
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.value = value;
        isExample = true;
        textSize = (int)(tileSize * 0.25);
    }

    public boolean getIsFaceDown() {
        return isFaceDown;
    }

    public void reveal() {
        isFaceDown = false;
        if (value > 1) {
            mediaPlayer.start();
        }
        updateDisplayedValue();
    }

    private void updateDisplayedValue() {
        parentLayout.removeView(activity.findViewById(tileValueID));
        if (value == 0) {
            ImageView img = new ImageView(context);
            img.setId(tileValueID);
            img.setImageResource(R.drawable.bomb_icon);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
            layoutParams.setMargins(x, y, 0, 0);
            img.setLayoutParams(layoutParams);
            parentLayout.addView(img);
        } else {
            TextView text = new TextView(context);
            String valueText = "" + value;
            text.setText(valueText);
            text.setId(tileValueID);
            text.setTextSize(textSize);
            text.setTextColor(context.getResources().getColor(R.color.text_color, null));
            text.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
            layoutParams.setMargins(x, y, 0, 0);
            text.setLayoutParams(layoutParams);
            parentLayout.addView(text);

        }
    }

    public void toggleMemo(int val) {
        memos[val] = !memos[val];
        if (memos[val]) {
            // set text size and margin offsets
            int textSize = tileSize * 3 / 32;
            int vOffset = 0, hOffset = 0;
            if (val < 3) {
                hOffset = tileSize / 3 * val;
            } else if (val == 3) {
                hOffset = tileSize * 2 / 3;
                vOffset = tileSize / 3;
            } else if (val == 4) {
                hOffset = tileSize * 2 / 3;
                vOffset = tileSize * 2 / 3;
            } else if (val == 5) {
                hOffset = tileSize / 3;
                vOffset = tileSize * 2 / 3;
            } else if (val == 6) {
                vOffset = tileSize * 2 / 3;
            } else if (val == 7) {
                vOffset = tileSize / 3;
            }

            if (val > 0) {
                // Place text on the tile
                TextView text = new TextView(context);
                String valString = "" + val;
                text.setText(valString);
                text.setTextSize(textSize);
                memoIDs[val] = View.generateViewId();
                text.setId(memoIDs[val]);
                text.setTextColor(context.getResources().getColor(R.color.memo_color, null));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
                layoutParams.setMargins(x + hOffset + 12, y + vOffset, 0, 0);
                text.setLayoutParams(layoutParams);
                parentLayout.addView(text);
            } else {
                ImageView img = new ImageView(context);
                img.setImageResource(R.drawable.mini_bomb_memo_icon);
                memoIDs[val] = View.generateViewId();
                img.setId(memoIDs[val]);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize / 3, tileSize / 3);
                layoutParams.setMargins(x, y, 0, 0);
                img.setLayoutParams(layoutParams);
                parentLayout.addView(img);
            }
        } else {
            parentLayout.removeView(activity.findViewById(memoIDs[val]));
        }
    }

    private void removeAllMemos() {
        for (int i = 0; i < memos.length; i++) {
            if (memos[i]) {
                toggleMemo(i);
            }
        }
    }

    public void draw() {

        // Tile Image
        ImageView img = new ImageView(context);
        tileImgID = View.generateViewId();
        img.setId(tileImgID);
        img.setImageResource(R.drawable.tile);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
        layoutParams.setMargins(x, y, 0, 0);
        img.setLayoutParams(layoutParams);

        // Make image respond to being tapped
        if (!isExample) {
            img.setOnClickListener(new TileOnClickListener(activity, this));
        }

        parentLayout.addView(img);

        // Value text
        tileValueID = View.generateViewId();
        TextView text = new TextView(context);
        text.setId(tileValueID);
        text.setText(R.string.val_unknown);
        text.setTextSize(textSize);
        text.setTextColor(context.getResources().getColor(R.color.text_color, null));
        text.setGravity(Gravity.CENTER);
        layoutParams = new RelativeLayout.LayoutParams(tileSize, tileSize);
        layoutParams.setMargins(x, y, 0, 0);
        text.setLayoutParams(layoutParams);
        parentLayout.addView(text);
    }

    public void animateFlip1() {
        // create animation object for the tile image, its value, and all of the memos
        ObjectAnimator tileImgAnim = ObjectAnimator.ofFloat(activity.findViewById(tileImgID), "rotationY", 0f, 90f);
        ObjectAnimator tileValueAnim = ObjectAnimator.ofFloat(activity.findViewById(tileValueID), "rotationY", 0f, 90f);

        // Set all animations to play together, and start the animation
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(tileImgAnim, tileValueAnim);
        animation.setDuration(250);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Remove all memos since they don't move with the animation
                removeAllMemos();
                // create a blocker to block the user from being able to tap another tile
                blockerID = View.generateViewId();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                TextView blocker = new TextView(context);
                blocker.setId(blockerID);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(displayMetrics.widthPixels, (int)(displayMetrics.heightPixels * 0.6));
                layoutParams.setMargins(0, (int)(displayMetrics.heightPixels * 0.2), 0, 0);
                blocker.setLayoutParams(layoutParams);
                blocker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // do nothing when tapped
                    }
                });
                blockerLayout.addView(blocker);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                reveal();
                animateFlip2();
            }
        });
        animation.start();
    }

    public void animateFlip2() {
        // create animation object for the tile image, its value, and all of the memos
        ObjectAnimator tileImgAnim = ObjectAnimator.ofFloat(activity.findViewById(tileImgID), "rotationY", -90f, 0f);
        ObjectAnimator tileValueAnim = ObjectAnimator.ofFloat(activity.findViewById(tileValueID), "rotationY", -90f, 0f);

        // Set all animations to play together, and start the animation
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(tileImgAnim, tileValueAnim);
        animation.setDuration(250);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                blockerLayout.removeView(activity.findViewById(blockerID));

                if (value == 0) {
                    try {
                        activity.putScoresInStatistics();
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    activity.gameOver();
                } else {
                    // Update the scoreboard
                    try {
                        activity.updateRoundScore(value);
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        animation.start();
    }

}

