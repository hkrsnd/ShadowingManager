package com.example.pii5656.shadowingmanager;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class MediaPlayerSample extends Activity implements View.OnClickListener {
    MediaPlayer mp = null;
    Button play_button, stop_button, record_button;
    final static int SAMPLING_RATE = 11025;
    AudioRecord audioRec = null;
    boolean bIsRecording = false;
    int bufSize;
    //private WaveFile wav1 = new WaveFile();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Play
        play_button = (Button) findViewById(R.id.PlayButton);
        play_button.setOnClickListener(this);
        stop_button = (Button) findViewById(R.id.StopButton);
        stop_button.setOnClickListener(this);
        record_button = (Button) findViewById(R.id.RecordButton);
        record_button.setOnClickListener(this);
        // メディアプレイヤーの作成
        mp = MediaPlayer.create(this, R.raw.jobs4);

        // ループ再生の設定
        // mediaPlayer.setLooping(true);

        //Record
        // バッファサイズの計算
        bufSize = AudioRecord.getMinBufferSize(
                SAMPLING_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT) * 2;
        // AudioRecordの作成
        audioRec = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLING_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize);
    }

    @Override
    public void onClick(View v) {
        if (v == play_button) {
            // 再生してなかったら
            if (!mp.isPlaying()) {
                // MediaPlayerの再生
                play_button.setText("Pause");
                mp.start();
            } else {
                // MediaPlayerの一時停止
                play_button.setText("Start");
                mp.pause();
            }
        }
        else if (v == stop_button) {
            // 再生してたら
            if (mp.isPlaying()) {
                // MediaPlayerの停止
                play_button.setText("Start");
                mp.stop();
                try {
                    // MediaPlayerの準備
                    mp.prepare();
                } catch (Exception e) {}
            }
        }else if (v == record_button) {                     //Recordについて
            if (bIsRecording) {
                record_button.setText("RecordStart");
                bIsRecording = false;
            } else {
                // 録音開始
                Log.v("AudioRecord", "startRecording");
                audioRec.startRecording();
                bIsRecording = true;
                // 録音スレッド
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte buf[] = new byte[bufSize];
                        // TODO Auto-generated method stub
                        while (bIsRecording) {
                            // 録音データ読み込み
                            audioRec.read(buf, 0, buf.length);
                            Log.v("AudioRecord", "read " + buf.length + " bytes");

                        }
                        // 録音停止
                        Log.v("AudioRecord", "stop");
                        audioRec.stop();
                    }
                }).start();
                record_button.setText("RecordStop");
            }
        }
    }


}