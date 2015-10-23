package com.pii5656.shadowingmanager;

import java.io.File;
import java.io.IOException;
import java.lang.String;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class Record extends MediaRecorder {

    public MediaRecorder mediarecorder;

    //録音用のメディアレコーダークラス
    static final String filePath = "/sdcard/sample.mp3"; //録音用のファイルパス
    //String saveDir = Environment.getExternalStorageDirectory().getPath() + "/test";

    public void startMediaRecord(String savePath) {
        try {
            mediarecorder = new MediaRecorder();
            //マイクからの音声を録音する
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //音声のエンコーダーも合わせてdefaultにする
            mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //ファイルの保存先を指定
            mediarecorder.setOutputFile(savePath);
            //録音の準備をする
            mediarecorder.prepare();
            //録音開始
            mediarecorder.start();
            Log.v("record", "recording!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //停止
    public void stopRecord() {
        try {
            //録音停止
            mediarecorder.stop();
            mediarecorder.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean mkfile(String path){
        File file = new File(path);
        try{
            return file.createNewFile();
        }catch(IOException e){
            System.out.println(e);
            return false;
        }
    }
}