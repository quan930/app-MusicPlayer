////package com.example.daquan.musicplayer;
////
////import android.database.Cursor;
////import android.graphics.Bitmap;
////import android.graphics.BitmapFactory;
////import android.graphics.drawable.BitmapDrawable;
////import android.net.Uri;
////import android.widget.ImageView;
////
/////**
//// * Created by daquan on 2018/5/9.
//// */
////
////public class quan {
////
////}
////    private void getImage(int id)
////    {
////        int album_id = id;
////        String albumArt = getAlbumArt(album_id);
////        Bitmap bm = null;
////        if (albumArt == null)
////        {
////            mImageView.setBackgroundResource(R.drawable.noalbum);
////        }
////        else
////        {
////            bm = BitmapFactory.decodeFile(albumArt);
////            BitmapDrawable bmpDraw = new BitmapDrawable(bm);
////            ((ImageView) mImageView).setImageDrawable(bmpDraw);
////        }
////    }
////
////    private String getAlbumArt(int album_id)
////
////    {
////        String mUriAlbums = "content://media/external/audio/albums";
////        String[] projection = new String[] { "album_art" };
////        Cursor cur = this.getContentResolver().query(  Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),  projection, null, null, null);
////        String album_art = null;
////        if (cur.getCount() > 0 && cur.getColumnCount() > 0)
////        {  cur.moveToNext();
////            album_art = cur.getString(0);
////        }
////        cur.close();
////        cur = null;
////        return album_art;
////    }
////}
////
//MediaStore.Audio.Media.TITLE,// 标题
//
//        MediaStore.Audio.Media.DURATION,// 持续时间
//        MediaStore.Audio.Media.ARTIST,// 艺术家
//        MediaStore.Audio.Media._ID,// id
//        MediaStore.Audio.Media.DISPLAY_NAME,// 显示名称
//        MediaStore.Audio.Media.DATA,// 数据
//        MediaStore.Audio.Media.ALBUM_ID,// 专辑名称ID
//        MediaStore.Audio.Media.ALBUM,// 专辑
//
//        MediaStore.Audio.Media.SIZE }, null, null, MediaStore.Audio.Media.ARTIST);// 大小,
//
//        其中我们需要的是MediaStore.Audio.Media.ALBUM_ID(即一下代码中的int id)
//
//private void getImage(int id)
//        {
//        int album_id = id;
//        String albumArt = getAlbumArt(album_id);
//        Bitmap bm = null;
//        if (albumArt == null)
//        {
//        mImageView.setBackgroundResource(R.drawable.noalbum);
//        }
//        else
//        {
//        bm = BitmapFactory.decodeFile(albumArt);
//        BitmapDrawable bmpDraw = new BitmapDrawable(bm);
//        ((ImageView) mImageView).setImageDrawable(bmpDraw);
//        }
//        }
//
//private String getAlbumArt(int album_id)
//
//        {
//        String mUriAlbums = "content://media/external/audio/albums";
//        String[] projection = new String[] { "album_art" };
//        Cursor cur = this.getContentResolver().query(  Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),  projection, null, null, null);
//        String album_art = null;
//        if (cur.getCount() > 0 && cur.getColumnCount() > 0)
//        {  cur.moveToNext();
//        album_art = cur.getString(0);
//        }
//        cur.close();
//        cur = null;
//        return album_art;
//        }
//
//
//
//        long id = cursor.getLong(cursor
//        .getColumnIndex(MediaStore.Audio.Media._ID));	//音乐id
//        String artist = cursor.getString(cursor
//        .getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
//        String album = cursor.getString(cursor
//        .getColumnIndex(MediaStore.Audio.Media.ALBUM));	//专辑
//        long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//        long duration = cursor.getLong(cursor
//        .getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
//        long size = cursor.getLong(cursor
//        .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
//        String url = cursor.getString(cursor
//        .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
//        int isMusic = cursor.getInt(cursor
//        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
//        if (isMusic != 0) { // 只把音乐添加到集合当中
//
//        }