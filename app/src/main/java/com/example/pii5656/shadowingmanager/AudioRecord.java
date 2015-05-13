package com.example.pii5656.shadowingmanager;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.view.View;
import android.media.AudioRecord;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class AudioRecord extends Activity implements View.OnClickListener {
    //録音用のオーディオレコードクラス
    final static int SAMPLING_RATE = 44100; //オーディオレコード用サンプリング周波数
    private int bufSize;//オーディオレコード用バッファのサイズ
    private short[] shortData; //オーディオレコード用バッファ
    private MyWaveFile wav1 = new MyWaveFile();
    Button audiostartButton, audiostopButton;

    //AudioRecordの初期化
    private void initAudioRecord() {
        wav1.createFile(SoundDefine.filePath);
        // AudioRecordオブジェクトを作成
        bufSize = android.media.AudioRecord.getMinBufferSize(SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize);

        shortData = new short[bufSize / 2];

        // コールバックを指定
        audioRecord.setRecordPositionUpdateListener(new AudioRecord.OnRecordPositionUpdateListener() {
            // フレームごとの処理
            @Override
            public void onPeriodicNotification(AudioRecord recorder) {
                // TODO Auto-generated method stub
                audioRecord.read(shortData, 0, bufSize / 2); // 読み込む
                wav1.addBigEndianData(shortData); // ファイルに書き出す
            }

            @Override
            public void onMarkerReached(AudioRecord recorder) {
                // TODO Auto-generated method stub

            }
        });
        // コールバックが呼ばれる間隔を指定
        audioRecord.setPositionNotificationPeriod(bufSize / 2);
    }

    /**
     * オーディオレコード用のボタン初期化
     */
    private void initAudioRecordButton() {
        audiostartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAudioRecord();
            }
        });

        audiostopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAudioRecord();
            }
        });
    }
        /*
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playRecord();
                }
            });
    }
    */

    private void startAudioRecord() {
        audioRecord.startRecording();
        audioRecord.read(shortData, 0, bufSize / 2);
    }

    //オーディオレコードを停止する
    private void stopAudioRecord() {
        audioRecord.stop();
    }
}