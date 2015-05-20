package com.example.pii5656.shadowingmanager;

import com.example.pii5656.shadowingmanager.MediaPlayerSample;
import com.example.pii5656.shadowingmanager.Record;
import android.app.Activity;
import com.example.pii5656.shadowingmanager.FileSelect.OnFileSelectDialogListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Message;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
//import java.util.logging.Handler;
import android.os.Handler;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements View.OnClickListener, OnFileSelectDialogListener,OnSeekBarChangeListener, Runnable{

    Button play_button = null, stop_button = null, rec_start_button = null, rec_stop_button = null;
    TextView textview = null;
    MediaPlayer mp = new MediaPlayer();
    Record rec = new Record();//Instantiate RecordClass
    MediaPlayerSample play = new MediaPlayerSample();//Instantiate MediaPlayerSampleClass
    private Menu mainMenu;
    private static final int MENU_ID_MENU1 = (Menu.FIRST + 1);
    private static final int MENU_ID_MENU2 = (Menu.FIRST + 2);
    FileSelect fileselect = new FileSelect();
    private final static int CHOSE_FILE_CODE = 12345;

    private SeekBar seekBar;
    private boolean running;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mp = MediaPlayer.create(this, R.raw.jobs4);

        play_button = (Button) findViewById(R.id.PlayButton);
        play_button.setOnClickListener(this);
        stop_button = (Button) findViewById(R.id.StopButton);
        stop_button.setOnClickListener(this);
        rec_start_button = (Button) findViewById(R.id.startRecordButton);
        rec_start_button.setOnClickListener(this);
        rec_stop_button = (Button) findViewById(R.id.stopRecordButton);
        rec_stop_button.setOnClickListener(this);
        textview = (TextView) findViewById(R.id.textView);

        //録音の保存先のディレクトリの作成
        //String saveDir = Environment.getExternalStorageDirectory().getPath() + "/test";

        String saveDir = Environment.getExternalStorageDirectory().getPath() + "/Shadowing/";
        File folder = new File(saveDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        seekBar = (SeekBar)findViewById(R.id.seekBar1);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(this);
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PlayButton:
                if (!mp.isPlaying()) {
                    Log.v("AudioRecord", "saisei");
                    // MediaPlayerの再生
                    play_button.setText("Pause");
                    //mp.start();
                    start();
                } else {
                    Log.v("AudioRecord", "ichijiteisi");
                    // MediaPlayerの一時停止
                    play_button.setText("Start");
                    //mp.pause();
                    pause();
                }
                break;

            case R.id.StopButton:
                if (mp.isPlaying()) {
                    // MediaPlayerの停止
                    play_button.setText("Start");
                    //mp.stop();
                    stop();
                    try {
                        // MediaPlayerの準備
                        mp.prepare();
                    } catch (Exception e) {
                    }
                }
                break;

            case R.id.startRecordButton:
                if(mp != null){
                    mp.start();
                }
                rec.startMediaRecord();
                break;
            case R.id.stopRecordButton:
                if(mp.isPlaying()){
                    mp.stop();
                    try{
                        mp.prepare();
                    } catch (IOException e){
                        //TODO 例外処理
                    }
                }
                rec.stopRecord();
                break;
        }

    }

    // Start mediaPlayer
    public void start() {
        if (!mp.isPlaying()) {
            mp.seekTo(mp.getCurrentPosition());
            mp.start();
        }
    }

     // Pause mediaPlayer
    public void pause(){
        mp.pause();
    }

    // Stop mediaPlayer
    public void stop(){
        mp.pause();
        mp.seekTo(0);
    }


    //メニューボタンが押された際に表示されるアイテムを追加する
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mainMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    //メニューで要素が選択された時の動作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        //Log.v("a","menu selected");
                //Intent i = new Intent(getApplicationContext(), FileSelect.class);
                //startActivity(i);
                Log.v("a","selected");
                //onClickFileSelect(this);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, CHOSE_FILE_CODE);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOSE_FILE_CODE && resultCode == RESULT_OK) {
            //String filePath = data.getDataString().replace("file://", "");
            if(mp != null){
                mp.stop();
                mp.reset();
            }


            //選択した音源ファイルのパス取得
            String filePath = data.getDataString();
            //選択したのファイル名を表示
            try{
                String decodedfilePath = URLDecoder.decode(filePath, "utf-8");
                int index = decodedfilePath.lastIndexOf("/");
                String file_name = decodedfilePath.substring(index+1);
                textview.setText(file_name);
            } catch(UnsupportedEncodingException e) {
                //例外処理
            }
            //選択されたファイルをセットする
            try {
                mp.setDataSource(this,Uri.parse(filePath));
                mp.prepare();
                //mp.start();
                seekBar = (SeekBar)findViewById(R.id.seekBar1);
                seekBar.setProgress(0);
                seekBar.setMax(mp.getDuration());
                seekBar.setOnSeekBarChangeListener(this);
            } catch (IOException e) {
                //TODO　いい感じに例外処理
            }
        }
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


    // Run the thread using for seekBar
    public void run(){
        while(running){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendMessage(Message.obtain(handler, mp.getCurrentPosition()));
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            seekBar.setProgress(msg.what);
        }
    };


    public void stopRunning(){
        running = false;
    }

    // SeekBar-control
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        //mp.seekTo(seekBar.getProgress());
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //int msec = seekBar.getProgress() * mp.getDuration() /  100;
        mp.seekTo(seekBar.getProgress());
        //mp.seekTo(11100);
        Log.e("test",String.valueOf(mp.getDuration()));
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