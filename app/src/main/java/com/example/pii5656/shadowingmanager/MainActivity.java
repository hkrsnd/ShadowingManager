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
import android.util.Log;
import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener {

    Button play_button = null, stop_button = null, rec_start_button = null, rec_stop_button = null;
    MediaPlayer mp = null;


    Record rec = new Record();//Instantiate RecordClass
    MediaPlayerSample play = new MediaPlayerSample();//Instantiate MediaPlayerSampleClass

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this,R.raw.jobs4);
        //Play oncreate
        /*
        play_button = (Button) play.findViewById(R.id.PlayButton);
        play_button.setOnClickListener(this);
        stop_button = (Button) play.findViewById(R.id.StopButton);
        stop_button.setOnClickListener(this);
        mp = MediaPlayer.create(this, R.raw.jobs4);
        */
        play_button = (Button) findViewById(R.id.PlayButton);
        play_button.setOnClickListener(this);
        stop_button = (Button) findViewById(R.id.StopButton);
        stop_button.setOnClickListener(this);
        rec_start_button = (Button) findViewById(R.id.startRecordButton);
        rec_start_button.setOnClickListener(this);
        rec_stop_button = (Button) findViewById(R.id.stopRecordButton);
        rec_stop_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.v("AudioRecord", "click!!");
        switch (v.getId()) {
            case R.id.PlayButton:
                if (!mp.isPlaying()) {
                    Log.v("AudioRecord", "saisei");
                    // MediaPlayerの再生
                    play_button.setText("Pause");
                } else {
                    Log.v("AudioRecord", "ichijiteisi");
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
                rec.startMediaRecord();
                break;
            case R.id.stopRecordButton:
                rec.stopRecord();
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
