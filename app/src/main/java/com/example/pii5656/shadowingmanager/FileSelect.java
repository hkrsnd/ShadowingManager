package com.example.pii5656.shadowingmanager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;


public class FileSelect extends Activity implements OnClickListener {

    /** アクティビティ */
    private Activity activity = null;

    /** リスナー */
    private OnFileSelectDialogListener listener = null;

    /** 対象となる拡張子 */
    private String extension = "";

    /** 表示中のファイル情報リスト */
    private List<File> viewFileDataList = null;

    /** 表示パスの履歴 */
    private List<String> viewPathHistory = null;

    /**
     * コントラクト
     *
     * @param activity アクティビティ
     */
    public void FileSelectDialog(Activity activity) {
        this.activity = activity;
        this.viewPathHistory = new ArrayList<String>();
    }

    /**
     * コントラクト
     *
     * @param activity アクティビティ
     * @param extension 対象となる拡張子
     */
    public void FileSelectDialog(Activity activity, String extension) {

        this.activity = activity;
        this.extension = extension;
        this.viewPathHistory = new ArrayList<String>();
    }

    /**
     * 選択イベント
     *
     * @param dialog ダイアログ
     * @param which 選択位置
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

        File file = this.viewFileDataList.get(which);

        // ディレクトリの場合
        if (file.isDirectory()) {

            show(file.getAbsolutePath() + "/");

        } else {

            this.listener.onClickFileSelect(file);
        }
    }

    /**
     * ダイアログを表示
     *
     * @param dirPath ディレクトリのパス
     */
    public void show(final String dirPath) {

        // 変更ありの場合
        if (this.viewPathHistory.size() == 0 || !dirPath.equals(this.viewPathHistory.get(this.viewPathHistory.size() - 1))) {

            // 履歴を追加
            this.viewPathHistory.add(dirPath);
        }

        // ファイルリスト
        File[] fileArray = new File(dirPath).listFiles();

        // 名前リスト
        List<String> nameList = new ArrayList<String>();

        if (fileArray != null) {

            // ファイル情報マップ
            Map<String, File> map = new HashMap<String, File>();

            for (File file : fileArray) {

                // ディレクトリの場合
                if (file.isDirectory()) {

                    nameList.add(file.getName() + "/");
                    map.put(nameList.get(map.size()), file);

                    // 対象となる拡張子の場合
                } else if ("".equals(this.extension) || file.getName().matches("^.*" + this.extension + "$")) {

                    nameList.add(file.getName());
                    map.put(nameList.get(map.size()), file);
                }
            }

            // ソート
            Collections.sort(nameList);

            // ファイル情報リスト
            this.viewFileDataList = new ArrayList<File>();

            for (String name : nameList) {

                this.viewFileDataList.add(map.get(name));
            }
        }

        // ダイアログを生成
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.activity);
        dialog.setTitle(dirPath);
        //dialog.setIcon(R.drawable.file);
        dialog.setItems(nameList.toArray(new String[0]), this);

        dialog.setPositiveButton("上 へ", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int value) {

                if (!"/".equals(dirPath)) {

                    String dirPathNew = dirPath.substring(0, dirPath.length() - 1);
                    dirPathNew = dirPathNew.substring(0, dirPathNew.lastIndexOf("/") + 1);

                    // 履歴を追加
                    FileSelect.this.viewPathHistory.add(dirPathNew);

                    // 1つ上へ
                    show(dirPathNew);

                } else {

                    // 現状維持
                    show(dirPath);
                }
            }
        });

        dialog.setNeutralButton("戻 る", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int value) {

                int index = FileSelect.this.viewPathHistory.size() - 1;

                if (index > 0) {

                    // 履歴を削除
                    FileSelect.this.viewPathHistory.remove(index);

                    // 1つ前に戻る
                    show(FileSelect.this.viewPathHistory.get(index - 1));

                } else {

                    // 現状維持
                    show(dirPath);
                }
            }
        });

        dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int value) {

                FileSelect.this.listener.onClickFileSelect(null);
            }
        });

        dialog.show();
    }

    /**
     * リスナーを設定
     *
     * @param listener 選択イベントリスナー
     */
    public void setOnFileSelectDialogListener(OnFileSelectDialogListener listener) {

        this.listener = listener;
    }

    /**
     * ボタン押下インターフェース
     */
    public interface OnFileSelectDialogListener {

        /**
         * 選択イベント
         *
         * @param file ファイル
         */
        public void onClickFileSelect(File file);
    }

    public static final String ROOT_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/";
    public void SelectDirectoryDialog(final Context context, final String parentPath){
        TextView tv;
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(parentPath);
        ad.setCancelable(false);
        ad.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }

        });
        try{
            //ファイル名のフィルターを作成
            FilenameFilter fFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    boolean f;
                    if(new File(dir + "/" + filename).isDirectory()) {
                        f = true;
                    } else if (filename.matches(".+.jpg")) { //正規表現で検索
                        f = true;
                    } else {
                        f = false;
                    }
                    return f;
                }
            };

            File[] files = new File(parentPath).listFiles(fFilter);

            ArrayList<String> pathList = new ArrayList<String>();
            for (File file : files) {
                pathList.add(file.getName());
            }

            Collections.sort(pathList);//ソート
            pathList.add(0, "このフォルダを選択する");
            pathList.add(1, "一つ前に戻る");

            //配列に変換
            final String[] items = pathList.toArray(new String[0]);

            ad.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case 0:
                            //tv = (TextView)findViewById(R.id.textView1);
                            //tv.setText(parentPath);
                            break;
                        case 1:
                            if(parentPath.equals(ROOT_PATH)){
                                Toast.makeText(context, "これ以上戻れません", Toast.LENGTH_SHORT).show();
                                SelectDirectoryDialog(context, parentPath);
                            }else{
                                //parentの一つ前のパスを取得
                                String grandparentPath = parentPath.substring(0, parentPath.length()-1);
                                grandparentPath = grandparentPath.substring(0, grandparentPath.lastIndexOf("/")+1);
                                SelectDirectoryDialog(context, grandparentPath);
                            }
                            break;
                        default:
                            File f = new File(parentPath + items[which]);
                            if(f.isFile()) {
                                //tv = (TextView)findViewById(R.id.textView1);
                                //tv.setText(parentPath + items[which]);
                            } else {
                                SelectDirectoryDialog(context, parentPath + items[which] + "/");
                            }
                            break;
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        ad.create().show();
    }


}