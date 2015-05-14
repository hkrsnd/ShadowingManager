package com.example.pii5656.shadowingmanager;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Record extends Activity{
    public final MediaRecorder recorder = new MediaRecorder();
    public final MediaPlayer player = new MediaPlayer();
    public boolean onRecord = false;
    public boolean onPlay = false;
    public String path = "/recordandplay.3gp";

    public void message(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void setAppearance() {
        final Button startRecord = (Button) findViewById(R.id.startRecordButton);
        final Button stopRecord = (Button) findViewById(R.id.stopRecordButton);
        final Button startPlay = (Button) findViewById(R.id.startPlayButton);
        final Button stopPlay = (Button) findViewById(R.id.stopPlayButton);
        startRecord.setEnabled(!onRecord && !onPlay);
        stopRecord.setEnabled(onRecord);
        startPlay.setEnabled(!onRecord && !onPlay);
        stopPlay.setEnabled(onPlay);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAppearance();
    }

    public void clickStartRecorder(View v) {
        if (!onRecord && !onPlay) {
            try {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(path);
                recorder.prepare();
                message("Start Recording");
                recorder.start(); // Recording is now started
                onRecord = true;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setAppearance();
    }

    public void clickStopRecorder(View v) {
        if (onRecord) {
            recorder.stop();
            message("Stop Recording");
            onRecord = false;
        }
        setAppearance();
    }

    public void clickStartPlayer(View v) {
        if (!onRecord && !onPlay) {
            try {
                player.setDataSource(path);
                player.prepare();
                message("Start Play");
                player.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            onPlay = true;
        }
        setAppearance();
    }
    public void clickStopPlayer(View v) {
        if (onPlay) {
            player.stop();
            message("Stop Play");
            player.reset();
            onPlay = false;
        }
        setAppearance();
    }
}