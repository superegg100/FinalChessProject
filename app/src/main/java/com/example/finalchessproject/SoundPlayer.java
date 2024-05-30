package com.example.finalchessproject;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
    private static MediaPlayer mediaPlayer;

    public static void playSound(Context context, int soundResId) {
        releaseMediaPlayer();

        mediaPlayer = MediaPlayer.create(context, soundResId);

        if (mediaPlayer != null) {
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                }
            });
        }
    }

    private static void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
