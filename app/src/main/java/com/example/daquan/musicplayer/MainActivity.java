package com.example.daquan.musicplayer;

import android.Manifest;
import android.content.Context;
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
    private SeekBar seekBar;//进度条
    private MediaPlayer mediaPlayer;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        start = (Button)findViewById(R.id.satrt);
        seekBar = (SeekBar) findViewById(R.id.Bar);
        mediaPlayer = new MediaPlayer();
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        seekBar.setProgress(msg.getData().getInt("123"));
                        break;
                }
            }
        };
//        final Handler handler = new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg) {
//
//            }
//        };
        try {
            File file = new File("/sdcard/Music/沼野跃鱼丶 - 赵鑫 - 许多年以后.mp3");
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
//            mediaPlayer.setLooping(true);//循环播放
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            seekBar.setMax(mediaPlayer.getDuration());


        } catch (IOException e) {
            e.printStackTrace();
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start.getText().equals("开始")){
                    start.setText("暂停");
                    mediaPlayer.start();
//                    handler.post(updatesb);
                    new Thread(updatesb).start();
//                    thread.start();
                }else{
                    mediaPlayer.pause();
                    start.setText("开始");
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

//    Runnable musicStart=new Runnable(){
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            mediaPlayer.start();
//            handler.post(updatesb);
//            //用一个handler更新SeekBar
//        }
//    };
    Runnable updatesb =new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
//            Message message = new Message();
            while(true){
                try {
                    Message message=handler.obtainMessage();
                    message.what = 1;
                    Bundle bundle = new Bundle();
//                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
//                    int currentPosition = mediaPlayer.getCurrentPosition();
                    bundle.putInt("123",mediaPlayer.getCurrentPosition());
                    message.setData(bundle);
                    handler.sendMessage(message);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            seekBar.setProgress(mediaPlayer.getCurrentPosition());
//            handler.postDelayed(updatesb, 1000);
            //每秒钟更新一次
        }

    };
}
