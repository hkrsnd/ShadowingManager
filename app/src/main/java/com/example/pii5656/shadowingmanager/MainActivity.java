package com.example.pii5656.shadowingmanager;

import com.example.pii5656.shadowingmanager.MediaPlayerSample;
import com.example.pii5656.shadowingmanager.Record;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener {

    Button play_button, stop_button;
    MediaPlayer mp = null;
    Record rec = new Record();//Instantiate RecordClass
    MediaPlayerSample play = new MediaPlayerSample();//Instantiate MediaPlayerSampleClass

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Play oncreate
        //play_button = (Button) findViewById(R.id.PlayButton);
        //play_button.setOnClickListener(this);
        //stop_button = (Button) findViewById(R.id.StopButton);
        //stop_button.setOnClickListener(this);
        //mp = MediaPlayer.create(this, R.raw.jobs4);


        //Recors oncreate
        rec.setAppearance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PlayButton:
                if (!mp.isPlaying()) {
                    // MediaPlayerの再生
                    play_button.setText("Pause");
                    mp.start();
                } else {
                    // MediaPlayerの一時停止
                    play_button.setText("Start");
                    mp.pause();
                }
                break;

            case R.id.StopButton:
                if (mp.isPlaying()) {
                    // MediaPlayerの停止
                    play_button.setText("Start");
                    mp.stop();
                    try {
                        // MediaPlayerの準備
                        mp.prepare();
                    } catch (Exception e) {
                    }
                }
                break;

            case R.id.startRecordButton:
                if (!rec.onRecord && !rec.onPlay) {
                    try {
                        rec.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        rec.recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        rec.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        rec.recorder.setOutputFile(rec.path);
                        rec.recorder.prepare();
                        rec.message("Start Recording");
                        rec.recorder.start(); // Recording is now started
                        rec.onRecord = true;
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                rec.setAppearance();
                break;
            case R.id.stopRecordButton:
                if (rec.onRecord) {
                    rec.recorder.stop();
                    rec.message("Stop Recording");
                    rec.onRecord = false;
                }
                rec.setAppearance();
                break;
        }
        /*
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }


        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
        */
    }
}
