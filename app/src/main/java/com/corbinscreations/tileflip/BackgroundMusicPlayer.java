package com.corbinscreations.tileflip;

import android.app.Application;
import android.media.MediaPlayer;

public class BackgroundMusicPlayer extends Application {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.tileflip_background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        mediaPlayer.start();
    }

    public void setVolume(int volume) {
        mediaPlayer.setVolume(volume / 100.0f, volume / 100.0f);
    }

}
