package com.example.daquan.musicplayer;

import android.media.MediaPlayer;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;

/**
 * Created by daquan on 2018/5/4.
 */

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    public MusicPlayer(File file) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(file.getPath());
        mediaPlayer.prepare();
        mediaPlayer.setLooping(true);//循环播放
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
