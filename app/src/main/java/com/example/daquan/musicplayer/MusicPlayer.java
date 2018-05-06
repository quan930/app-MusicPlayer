package com.example.daquan.musicplayer;

import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {
//    private int i;//当前播放的位置
    private MediaPlayer mediaPlayer;
    private int mun;//总音乐数
    private int current = 0;//当前播放数
    private List<File> musicList = new ArrayList<File>();
    public int getSum() {
        return musicList.size();
    }


    public MusicPlayer(String string) throws IOException {
        list(new File(string));
        mun=musicList.size();//返回总数
    }
    //初始播放
    public MediaPlayer getMediaPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource((musicList.get(current)).getPath());//设置音频路径进入初始化状态
            mediaPlayer.prepare();//进入就绪状态
            mediaPlayer.setLooping(true);//循环播放
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }
    //下一首
    public MediaPlayer next(){
        try {
            current ++;
            if(current == musicList.size()){
                current = 0;
            }
            mediaPlayer.setDataSource((musicList.get(current)).getPath());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);//循环播放
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }
    //上一首
    public MediaPlayer before(){
        try {
            current --;
            if(current < 0){
                current = musicList.size()-1;
            }
            mediaPlayer.setDataSource((musicList.get(current)).getPath());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);//循环播放
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }
    //音乐播放列表
    private void list (File file){
        if(file.isDirectory()){
            //文件夹
            File result [] = file.listFiles();
            for(int i = 0;i < result.length;i++) {
                if(result[i].isDirectory()){
                    list(result[i]);
                }else{
//                    文件
                    musicList.add(result[i]);
                }
            }
        }else{
            //文件
            musicList.add(file);
        }
    }
}
