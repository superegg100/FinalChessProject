package com.example.finalchessproject;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
    private static MediaPlayer mediaPlayer;

    public static void playSound(Context context, int soundResId) {
        // Release any existing MediaPlayer instance
        releaseMediaPlayer();

        // Initialize MediaPlayer with the provided sound resource ID
        mediaPlayer = MediaPlayer.create(context, soundResId);

        // Start playing the sound
        if (mediaPlayer != null) {
            mediaPlayer.start();

            // Optional: Add a listener to handle completion of the sound
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                }
            });
        }
    }

    private static void releaseMediaPlayer() {
        // Release the MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
