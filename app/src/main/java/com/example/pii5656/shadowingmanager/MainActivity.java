package com.example.pii5656.shadowingmanager;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Message;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.ImageButton;
import java.io.File;
import android.os.Environment;
import android.widget.TextView;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements View.OnClickListener,OnSeekBarChangeListener, Runnable{

    ImageButton play_button = null, rec_start_button = null, rec_play_start_button = null,
                fast_button = null, back_button = null;
    TextView textview = null;
    MediaPlayer mp = new MediaPlayer();
    MediaPlayer rec_mp = new MediaPlayer();
    Record rec = new Record();//Instantiate RecordClass
    private final static int CHOSE_FILE_CODE = 12345;

    private SeekBar seekBar;
    private boolean running;
    private Thread thread;
    int totalTime = 0;
    int seekTime = 0;
    Boolean isrecorded = false;

    String savePath = "/sdcard/Shadowing/sample.mp3";

    Boolean flag_record = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_button = (ImageButton) findViewById(R.id.PlayButton);
        play_button.setOnClickListener(this);
        rec_start_button = (ImageButton) findViewById(R.id.startRecordButton);
        rec_start_button.setOnClickListener(this);
        rec_play_start_button = (ImageButton) findViewById(R.id.startPlayButton);
        rec_play_start_button.setOnClickListener(this);
        back_button = (ImageButton) findViewById(R.id.BackButton);
        back_button.setOnClickListener(this);
        fast_button = (ImageButton) findViewById(R.id.FastButton);
        fast_button.setOnClickListener(this);
        textview = (TextView) findViewById(R.id.textView);
        textview.setText("Open your audio file.");
        seekBar = (SeekBar)findViewById(R.id.seekBar1);
        seekBar.setProgress(0);

        //録音の保存先のディレクトリの作成
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
                if(mp == null){
                    break;
                }else if (!mp.isPlaying()) {
                    this.findViewById(R.id.PlayButton).setActivated(true);
                    start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        //音源終了後のアクション
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            findViewById(R.id.PlayButton).setActivated(false);

                            mp.seekTo(0);
                        }
                    });
                } else {
                    this.findViewById(R.id.PlayButton).setActivated(false);
                    pause();
                }
                break;

            case R.id.startRecordButton:
                if (flag_record == false) {
                    if (mp != null) {
                        mp.start();
                    }
                    this.findViewById(R.id.startRecordButton).setActivated(true);
                    flag_record = true;
                    rec.startMediaRecord(savePath);
                    break;

                } else {
                    if (mp.isPlaying()) {
                        mp.stop();
                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            //TODO 例外処理
                        }
                    }
                    rec.stopRecord();
                    isrecorded = true;
                    this.findViewById(R.id.startRecordButton).setActivated(false);
                    flag_record = false;
                    try {
                        //rec_mp = new MediaPlayer();//初期化する
                        rec_mp.reset();
                        rec_mp.setDataSource(savePath);
                        rec_mp.prepare();
                    } catch (IOException e) {
                    }
                }
                Log.v("test", "stop recording!");
                break;
            case R.id.startPlayButton:
                if(isrecorded == false) {
                    break;
                }
                else if (!rec_mp.isPlaying()) {
                    Log.v("test","null!!");
                    this.findViewById(R.id.startPlayButton).setActivated(true);
                    rec_mp.start();
                    rec_mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            findViewById(R.id.startPlayButton).setActivated(false);
                            rec_mp.pause();
                            rec_mp.seekTo(0);
                        }
                    });
                    break;
                } else{
                    this.findViewById(R.id.startPlayButton).setActivated(false);
                    rec_mp.stop();
                    prepare(rec_mp);
                    break;
                }
            case R.id.BackButton:
                Log.v("test","back!");
                totalTime = mp.getDuration();
                seekTime = mp.getCurrentPosition();
                seekTime -= 2000;
                if ( seekTime < 0) seekTime = 0; // ０より小さい場合は開始位置に移動
                if ( totalTime < seekTime ) seekTime = totalTime; // サウンド全体の長さより長い場合はサウンドの最後に移動
                mp.seekTo(seekTime); //再生位置に移動
                break;
            case R.id.FastButton:
                Log.v("test","back!");
                totalTime = mp.getDuration();
                seekTime = mp.getCurrentPosition();
                seekTime += 2000;
                if ( seekTime < 0) seekTime = 0; // ０より小さい場合は開始位置に移動
                if ( totalTime < seekTime ) seekTime = totalTime; // サウンド全体の長さより長い場合はサウンドの最後に移動
                mp.seekTo(seekTime); //再生位置に移動
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
    public void stop() {
        mp.pause();
        mp.seekTo(0);
    }

    public void prepare(MediaPlayer mp){
        try{
            mp.prepare();
        }catch(IOException e){
            //TODO いい感じの例外処理
        }
    }

    //メニューボタンが押された際に表示されるアイテムを追加する
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //メニューで要素が選択された時の動作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, CHOSE_FILE_CODE);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOSE_FILE_CODE && resultCode == RESULT_OK) {
            //String filePath = data.getDataString().replace("file://", "");
           if(mp != null){
               if(!mp.isPlaying()) {
                   this.findViewById(R.id.PlayButton).setActivated(false);
                   mp.pause();
               }
               mp.reset();
               Log.v("test","here!");
            } if(flag_record == true){
                rec.stop();
            }
            //選択した音源ファイルのパス取得
            String filePath = data.getDataString();
            //選択したのファイル名を表示
            try{
                String decodedfilePath = URLDecoder.decode(filePath, "utf-8");
                int index = decodedfilePath.lastIndexOf("/");
                String file_name = decodedfilePath.substring(index+1);
                savePath = "/sdcard/Shadowing/rec_"+file_name;
                textview.setText(file_name);
            } catch(UnsupportedEncodingException e) {
                //例外処理
            }
            //選択されたファイルをセットする
            try {
                this.findViewById(R.id.PlayButton).setActivated(false);
                mp.setDataSource(this, Uri.parse(filePath));
                mp.prepare();
                //mp.start();

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

    // SeekBar-control
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mp.seekTo(seekBar.getProgress());
    }
}