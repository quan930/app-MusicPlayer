package com.example.daquan.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button start;//开始停止键
    private Button down;//下一首
    private Button up;//上一首
    private SeekBar seekBar;//进度条
    private int sum;//歌曲总数
    private int current = 0;//当前数
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private MusicPlayer musicPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//去掉标题栏
        setContentView(R.layout.activity_main);
        start = (Button)findViewById(R.id.satrt);
        down = (Button)findViewById(R.id.down);
        up = (Button)findViewById(R.id.up);
        seekBar = (SeekBar) findViewById(R.id.Bar);
        try {
            musicPlayer = new MusicPlayer("/sdcard/Music");
            sum = musicPlayer.getSum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer = musicPlayer.getMediaPlayer();
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.setMax(mediaPlayer.getDuration());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start.getText().equals("开始")){
                    start.setText("暂停");
                    mediaPlayer.start();
                }else{
                    mediaPlayer.pause();
                    start.setText("开始");
                }
            }
        });
        //下一首
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer = musicPlayer.next();
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.setMax(mediaPlayer.getDuration());
                if(start.getText().equals("暂停")){
                    mediaPlayer.start();
                }
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer = musicPlayer.before();
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.setMax(mediaPlayer.getDuration());
                if(start.getText().equals("暂停")){
                    mediaPlayer.start();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.setMax(mediaPlayer.getDuration());
        handler.post(runnable);
        super.onResume();
    }
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
//                    musicService.mediaPlayer.seekTo(seekBar.getProgress());
                }
            });
            handler.postDelayed(runnable, 1000);
        }
    };

}

