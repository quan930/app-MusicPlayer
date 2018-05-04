//package com.example.daquan.musicplayer;
//
///**
// * Created by daquan on 2018/5/4.
// */
//
//public class SeekBarThread implements Runnable {
//    @Override
//    public void run() {
//        while (!isChanging && m.isPlaying()) {
//            // 将SeekBar位置设置到当前播放位置
//            audio_seekBar.setProgress(m.getCurrentPosition());
//            try {
//                // 每100毫秒更新一次位置
//                Thread.sleep(100);
//                //播放进度
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
