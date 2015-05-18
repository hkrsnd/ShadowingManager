package com.example.pii5656.shadowingmanager;

import com.example.pii5656.shadowingmanager.MediaPlayerSample;
import com.example.pii5656.shadowingmanager.Record;
import android.app.Activity;
import com.example.pii5656.shadowingmanager.FileSelect.OnFileSelectDialogListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.IOException;
import java.net.URLDecoder;

public class MainActivity extends Activity implements View.OnClickListener, OnFileSelectDialogListener{

    Button play_button = null, stop_button = null, rec_start_button = null, rec_stop_button = null;
    MediaPlayer mp = null;
    Record rec = new Record();//Instantiate RecordClass
    MediaPlayerSample play = new MediaPlayerSample();//Instantiate MediaPlayerSampleClass
    private Menu mainMenu;
    private static final int MENU_ID_MENU1 = (Menu.FIRST + 1);
    private static final int MENU_ID_MENU2 = (Menu.FIRST + 2);
    FileSelect fileselect = new FileSelect();
    private final static int CHOSE_FILE_CODE = 12345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this, R.raw.jobs4);

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
                    mp.start();
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
                    } catch (Exception e) { }
                }
                break;

            case R.id.startRecordButton:
                rec.startMediaRecord();
                break;
            case R.id.stopRecordButton:
                rec.stopRecord();
                break;
        }
    }

    //メニューボタンが押された際に表示されるアイテムを追加する
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        mainMenu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        //Log.v("a","menu selected");
                //Intent i = new Intent(getApplicationContext(), FileSelect.class);
                //startActivity(i);
                Log.v("a","selected");
                //onClickFileSelect(this);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, CHOSE_FILE_CODE);


        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final int action = event.getAction();
        final int keyCode = event.getKeyCode();
        if (action == KeyEvent.ACTION_UP) {
            // メニュー表示
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                if (mainMenu != null) {
                    mainMenu.performIdentifierAction(R.id.overflow_options, 0);
                }
                return true;
            }
        } else {
            return true;
        }
        return true;
    }
  
    /**
     * ファイル選択イベント
     */
    public void onClickFileSelect(View view) {
 
        // ファイル選択ダイアログを表示
        FileSelect dialog = new FileSelect();
        dialog.setOnFileSelectDialogListener(this);
 
        // 表示
        dialog.show(Environment.getExternalStorageDirectory().getPath());
    }
 
    /**
     * ファイル選択完了イベント
     */
    @Override
    public void onClickFileSelect(File file) {
 
        if (file != null) {
            // 選択ファイルを設定
            //((TextView)findViewById(R.id.text)).setText(file.getPath() + file.getName());
        }
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CHOSE_FILE_CODE && resultCode == RESULT_OK) {
                String filePath = data.getDataString().replace("file://", "");
                String decodedfilePath = URLDecoder.decode(filePath, "utf-8");
                imageText.setText(decodedfilePath);
            } catch (UnsupportedEncodingException e) {
                // いい感じに例外処理
            }
        }
    }*/
}