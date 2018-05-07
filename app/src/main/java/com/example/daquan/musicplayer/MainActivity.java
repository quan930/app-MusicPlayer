package com.example.daquan.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button start;//开始停止键
    private Button down;//下一首
    private Button up;//上一首
    private Button musicMenu;//歌单
    private SeekBar seekBar;//进度条
    private int sum;//歌曲总数
    private int current = 0;//当前数
    private TextView totalTime;//全部时间
    private TextView playingTime;//当前时间
    private TextView musicName;//歌曲名称
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private MusicPlayer musicPlayer;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private PopupWindow popupWindow;//菜单浮窗
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//去掉标题栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.satrt);
        down = (Button) findViewById(R.id.down);
        up = (Button) findViewById(R.id.up);
        musicMenu = (Button) findViewById(R.id.musicList);
        totalTime = findViewById(R.id.alltime);
        playingTime = findViewById(R.id.currentTime);
        musicName = findViewById(R.id.musicName);
        seekBar = findViewById(R.id.Bar);
        try {
            musicPlayer = new MusicPlayer(Environment.getExternalStorageDirectory()+"/Music");
            sum = musicPlayer.getSum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer = musicPlayer.getMediaPlayer();
        musicMenu();//菜单浮窗
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.setMax(mediaPlayer.getDuration());
        totalTime.setText(time.format(mediaPlayer.getDuration()));//曲目时间
        musicName.setText(musicPlayer.getMusicName());//曲目名称

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
                totalTime.setText(time.format(mediaPlayer.getDuration()));//曲目时间
                musicName.setText(musicPlayer.getMusicName());//曲目名称
                if(start.getText().equals("暂停")){
                    mediaPlayer.start();
                }
            }
        });
        //上一首
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer = musicPlayer.before();
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.setMax(mediaPlayer.getDuration());
                totalTime.setText(time.format(mediaPlayer.getDuration()));//曲目时间
                musicName.setText(musicPlayer.getMusicName());//曲目名称
                if(start.getText().equals("暂停")){
                    mediaPlayer.start();
                }
            }
        });
        //歌单
        musicMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
        });
        //歌单监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("current",String.valueOf(position));
                mediaPlayer.reset();
                musicPlayer.setCurrent(position);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.setMax(mediaPlayer.getDuration());
                totalTime.setText(time.format(mediaPlayer.getDuration()));//曲目时间
                musicName.setText(musicPlayer.getMusicName());//曲目名称
                if(start.getText().equals("暂停")){
                    mediaPlayer.start();
                }
                popupWindow.dismiss();
            }
        });
    }
    @Override
    protected void onResume() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        playingTime.setText(time.format(mediaPlayer.getCurrentPosition()));
        handler.post(runnable);
        super.onResume();
    }
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            playingTime.setText(time.format(mediaPlayer.getCurrentPosition()));
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
            if(start.getText().equals("暂停")&&!mediaPlayer.isPlaying()){
                mediaPlayer.reset();
                mediaPlayer = musicPlayer.next();
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.setMax(mediaPlayer.getDuration());
                totalTime.setText(time.format(mediaPlayer.getDuration()));//曲目时间
                musicName.setText(musicPlayer.getMusicName());//曲目名称
                mediaPlayer.start();
            }
            handler.postDelayed(runnable, 1000);
        }
    };
    private void musicMenu(){
        listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,musicPlayer.getMusicNameList());
        listView.setAdapter(adapter);
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDisplayMetrics().heightPixels/2);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(listView);
        Resources res = getResources();
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(res.getColor(R.color.background));
        popupWindow.setBackgroundDrawable(colorDrawable);
    }

}

