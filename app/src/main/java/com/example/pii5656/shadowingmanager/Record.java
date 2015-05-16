package com.example.pii5656.shadowingmanager;

import java.io.File;
import java.lang.String;
import android.media.MediaRecorder;
import android.util.Log;

public class Record extends MediaRecorder {

    public MediaRecorder mediarecorder;

    //録音用のメディアレコーダークラス
    static final String filePath = "/sdcard/sample.mp3"; //録音用のファイルパス

    public void startMediaRecord() {
        try {
            File mediafile = new File(filePath);
            if (mediafile.exists()) {
                //ファイルが存在する場合は削除する
                //mediafile.delete();
                Log.v("record","exists!!");
            }
            mediafile = null;
            mediarecorder = new MediaRecorder();
            //マイクからの音声を録音する
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //音声のエンコーダーも合わせてdefaultにする
            mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //ファイルの保存先を指定
            mediarecorder.setOutputFile(filePath);
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
}