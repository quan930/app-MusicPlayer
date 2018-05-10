package com.example.daquan.musicplayer;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageButton start;//开始停止键
    private ImageButton down;//下一首
    private ImageButton up;//上一首
    private ImageButton musicMenu;//歌单
    private ImageButton single;//单曲循环
    private SeekBar seekBar;//进度条
    private ImageView coverImage;
    private TextView totalTime;//全部时间
    private TextView playingTime;//当前时间
    private TextView musicName;//歌曲名称
    private Handler handler = new Handler();
    private PopupWindow popupWindow;//菜单浮窗
    private ListView listView;
    private MusicService musicService;
    private Boolean isPlay = false;//音乐开始键 停止为faluse
    private PopupMenu popupMenu;
    private static int musicState = 1;//歌曲状态，1为顺序，2为乱序，3为单曲

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//去掉标题栏
        setContentView(R.layout.activity_main);
        musicService = new MusicService();

        Intent bind = new Intent(MainActivity.this,MusicService.class);
        startService(bind);
        bindServiceConnection();//绑定
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        coverImage = (ImageView) findViewById(R.id.imageView);
        musicService.animator = ObjectAnimator.ofFloat(coverImage, "rotation", 0, 359);

        start = findViewById(R.id.musicstart);
        down = findViewById(R.id.musicbefore);
        up =findViewById(R.id.musicup);
        musicMenu = findViewById(R.id.musicList);
        totalTime = findViewById(R.id.alltime);
        single = findViewById(R.id.single);
        playingTime = findViewById(R.id.currentTime);
        musicName = findViewById(R.id.musicName);
        seekBar = findViewById(R.id.Bar);
        popupMenu = new PopupMenu(MainActivity.this,single);
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());

        if(musicService.getMediaPlayer()==null){
            musicService.initializePlayer();
        }else {
            if(musicService.getMediaPlayer().isPlaying()){
//                start.setText("暂停");
                start.setImageResource(R.drawable.stop);
                musicService.AnimatorAction();
                isPlay = false;
                Log.d("音乐播放", "onCreate: ");
            }else{
                Log.d("没有播放", "onCreate: ");
            }
        }
        if (musicState==1){//顺序播放
            musicService.getMediaPlayer().setLooping(false);
            single.setImageResource(R.drawable.circulation);
        }else{
            if (musicState == 2){//乱序播放
                musicService.getMediaPlayer().setLooping(false);
                single.setImageResource(R.drawable.music_shuffle_button);
            }else {//单曲循环
                musicService.getMediaPlayer().setLooping(true);//循环播放
                single.setImageResource(R.drawable.lopping);
            }
        }
        musicMenu();//菜单浮窗

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay){
                    start.setImageResource(R.drawable.stop);
                    isPlay = false;
                    musicService.getMediaPlayer().start();
                    musicService.AnimatorAction();
                }else{
                    musicService.getMediaPlayer().pause();
                    start.setImageResource(R.drawable.start);
                    musicService.AnimatorAction();
                    isPlay = true;
                }
            }
        });
        //下一首
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.getMediaPlayer().reset();
                musicService.setCurrent(musicService.getCurrent()+1);
                seekBar.setProgress(musicService.getMediaPlayer().getCurrentPosition());
                seekBar.setMax(musicService.getMediaPlayer().getDuration());
                totalTime.setText(new SimpleDateFormat("mm:ss").format(musicService.getMediaPlayer().getDuration()));//曲目时间
                musicName.setText(musicService.getMusicName());
                if(isPlay){
                    musicService.getMediaPlayer().start();
                }
            }
        });
        //上一首
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.getMediaPlayer().reset();
                musicService.setCurrent(musicService.getCurrent()-1);
                seekBar.setProgress(musicService.getMediaPlayer().getCurrentPosition());
                seekBar.setMax(musicService.getMediaPlayer().getDuration());
                totalTime.setText(new SimpleDateFormat("mm:ss").format(musicService.getMediaPlayer().getDuration()));//曲目时间
                musicName.setText(musicService.getMusicName());
                if(isPlay){
                    musicService.getMediaPlayer().start();
                }
            }
        });
        //单曲循环
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.cir:
                                popupWindow.dismiss();
                                musicState = 1;
                                musicService.getMediaPlayer().setLooping(false);
                                single.setImageResource(R.drawable.circulation);
                                break;
                            case R.id.out:
                                popupWindow.dismiss();
                                musicState = 2;
                                single.setImageResource(R.drawable.music_shuffle_button);
                                musicService.getMediaPlayer().setLooping(false);
                                break;
                            case R.id.one:
                                popupWindow.dismiss();
                                musicState = 3;
                                single.setImageResource(R.drawable.lopping);
                                musicService.getMediaPlayer().setLooping(true);//循环播放
                                break;
                        }

                        return true;
                    }
                });
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
                musicService.getMediaPlayer().reset();
                musicService.setCurrent(position);
                seekBar.setProgress(musicService.getMediaPlayer().getCurrentPosition());
                seekBar.setMax(musicService.getMediaPlayer().getDuration());
                totalTime.setText(new SimpleDateFormat("mm:ss").format(musicService.getMediaPlayer().getDuration()));//曲目时间
                musicName.setText(musicService.getMusicName());
                if(!isPlay){
                    musicService.getMediaPlayer().start();
                }
                popupWindow.dismiss();
            }
        });
    }
    @Override
    protected void onResume() {
        seekBar.setProgress(musicService.getMediaPlayer().getCurrentPosition());
        playingTime.setText(new SimpleDateFormat("mm:ss").format(musicService.getMediaPlayer().getCurrentPosition()));
        handler.post(runnable);
        super.onResume();
    }
    public Runnable runnable = new Runnable() {
        @SuppressLint("WrongConstant")
        @Override
        public void run() {
            seekBar.setProgress(musicService.getMediaPlayer().getCurrentPosition());
            seekBar.setMax(musicService.getMediaPlayer().getDuration());
            totalTime.setText(new SimpleDateFormat("mm:ss").format(musicService.getMediaPlayer().getDuration()));//曲目时间
//        musicName.setText(musicPlayer.getMusicName());//曲目名称
            musicName.setText(musicService.getMusicName());
            seekBar.setProgress(musicService.getMediaPlayer().getCurrentPosition());
            playingTime.setText(new SimpleDateFormat("mm:ss").format(musicService.getMediaPlayer().getCurrentPosition()));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        musicService.getMediaPlayer().seekTo(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            musicService.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    musicService.getMediaPlayer().reset();
                    if(musicState==1){
                        musicService.setCurrent(musicService.getCurrent()+1);
                    }else{
                        musicService.setCurrent(new Random().nextInt(musicService.getMun()));
                    }
                    seekBar.setProgress(musicService.getMediaPlayer().getCurrentPosition());
                    seekBar.setMax(musicService.getMediaPlayer().getDuration());
                    totalTime.setText(new SimpleDateFormat("mm:ss").format(musicService.getMediaPlayer().getDuration()));//曲目时间
                    musicName.setText(musicService.getMusicName());
                    musicService.getMediaPlayer().start();
                }
            });
            handler.postDelayed(runnable, 100);
        }
    };


    //菜单栏
    private void musicMenu(){
        listView = new ListView(this);
        musicService.getMusicNameList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,musicService.getMusicNameList());
        listView.setAdapter(adapter);
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDisplayMetrics().heightPixels/2);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(listView);
        Resources res = getResources();
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(res.getColor(R.color.background));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setAnimationStyle(R.style.Popupwindow);
    }

    //绑定activity
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };
    private void bindServiceConnection() {
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("断开连接", "onDestroy: ");
        unbindService(sc);
    }
}

