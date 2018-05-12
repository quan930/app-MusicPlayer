package com.example.daquan.musicplayer;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {
    public static ObjectAnimator animator;
    public final IBinder binder = new MyBinder();
    static private MediaPlayer mediaPlayer;
    private int mun;//总音乐数
    private int current = 0;//当前播放数
    private List<File> musicList = new ArrayList<File>();
    private List<String> nameList = new ArrayList<String>();
    private String musicName;//当前播放的歌名

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
    //当前播放数
    public int getCurrent() {
        return current;
    }
    //构造器
    public MusicService(String string) {
        list(new File(string));
        mun=musicList.size();//返回总数
        Log.d("音乐数",String.valueOf(mun));
    }
    //构造器
    public MusicService() {
        list(new File(Environment.getExternalStorageDirectory()+"/Music"));
        mun=musicList.size();//返回总数
        Log.d("音乐数",String.valueOf(mun));
    }
    //返回歌单
    public List<String> getMusicNameList() {
        return nameList;
    }
    //返回总数
    public int getMun() {
        return mun;
    }
    //换曲
    public void setCurrent(int currentt) {
        if(currentt>=musicList.size()){
            current = 0;
        }else{
            if(currentt<=0){
                Log.d("asdf歌曲数",String.valueOf(currentt));
                current = musicList.size()-2;
            }
        }
        this.current = currentt;
        Log.d("asdf歌曲数",String.valueOf(current));
        try {
            mediaPlayer.setDataSource((musicList.get(current)).getPath());
            mediaPlayer.prepare();
            musicName = musicList.get(current).getName().substring(0,musicList.get(current).getName().indexOf("."));//曲目名称
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获取歌曲名字
    public String getMusicName() {
        return musicName;
    }
    //MediaPlayer
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    //初始播放
    public void initializePlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource((musicList.get(current)).getPath());//设置音频路径进入初始化状态
            mediaPlayer.prepare();//进入就绪状态
//            mediaPlayer.setLooping(true);//循环播放
            musicName = musicList.get(current).getName().substring(0,musicList.get(current).getName().indexOf("."));//曲目名称
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    //音乐播放列表
    private void list (File file){
        if(file.isDirectory()){
            //文件夹
            File result [] = file.listFiles();
            for(int i = 0;i < result.length;i++) {
                if(result[i].isDirectory()){
                    list(result[i]);
                    continue;
                }else{
//                    文件
                    if(result[i].getName().endsWith("mp3")){
                        musicList.add(result[i]);
                        nameList.add(result[i].getName().substring(0,result[i].getName().indexOf(".")));
                        continue;
                    }else{
                        continue;
                    }
                }
            }
        }else{
            if(file.getName().endsWith("mp3")){
                musicList.add(file);
                nameList.add(file.getName().substring(0,file.getName().indexOf(".")));
                return;
            }else{
                return;
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("启动", "onCreate: ");
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource((musicList.get(current)).getPath());//设置音频路径进入初始化状态
            mediaPlayer.prepare();//进入就绪状态
//            mediaPlayer.setLooping(true);//循环播放
            musicName = musicList.get(current).getName().substring(0,musicList.get(current).getName().indexOf("."));//曲目名称
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public  void AnimatorAction(boolean isplay) {
        if (isplay) {
            animator.setDuration(5000);
            animator.setInterpolator(new LinearInterpolator()); // 均速旋转
            animator.setRepeatCount(ValueAnimator.INFINITE); // 无限循环
            animator.setRepeatMode(ValueAnimator.INFINITE);
            animator.start();
        }else{
            animator.end();
        }
    }
}
