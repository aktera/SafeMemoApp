package io.github.aktera.safememoapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // レイアウトリソースを関連付け、部品をアクティビティに配置する
        setContentView(R.layout.activity_memo);

        // ツールバーをアクションバーとして扱う
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ツールバーに戻るボタンを追加する
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // メモを読み込む
        loadMemo();

    }

    // ウィンドウが非表示にされた
    @Override
    protected void onPause() {
        super.onPause();

        // メモを保存する
        saveMemo();
    }

    // メニュー表示時に呼ばれる
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // メニューリソースを関連付ける
        getMenuInflater().inflate(R.menu.menu_memo, menu);
        return true;
    }

    // メニューアイテム選択時に呼ばれる
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            // エクスポートメニュー
            case R.id.menu_export:
                finish();
                return true;

            // インポートメニュー
            case R.id.menu_import:
                finish();
                return true;

            // 戻るメニュー
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    // メモを読み込む
    private void loadMemo() {
        // メモファイルパスを取得する
        String path = getMemoFilePath("01");

        // テキストビュー
        TextView viewMemo = (TextView)findViewById(R.id.editMemo);
        String strMemo = "";

        try {
            // プライベートファイルを読み込むにはopenFileInputを使用する
            FileInputStream fis = getApplicationContext().openFileInput(path);
            InputStreamReader isr = new InputStreamReader(fis, "utf8");
            BufferedReader br = new BufferedReader(isr);

            // テキスト読み込み
            // 最後の行が改行で終わってない場合でも改行を付加してしまうのが気に入らないが
            while (true) {
                String line  = br.readLine();
                if (line == null) {
                    break;
                }
                strMemo += line + "\n";
            }

            // 読み込んだテキストファイルをビューに反映する
            viewMemo.setText(strMemo);
            br.close();
        }
        catch (FileNotFoundException e) {
            // 初回起動時にはファイルがない
        }
        catch (IOException e) {
            Toast.makeText( getApplicationContext(), e.toString(), Toast.LENGTH_SHORT ).show();
        }
    }

    // メモを保存する
    private void saveMemo() {
        // メモファイルパスを取得する
        String path = getMemoFilePath("01");

        // テキストビューから文字列を取得する
        TextView viewMemo = (TextView) findViewById(R.id.editMemo);
        String strMemo = viewMemo.getText().toString();

        try {
            // プライベートファイルに書き込むにはopenFileOutputを使用する
            FileOutputStream fos = getApplicationContext().openFileOutput(path, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf8");
            BufferedWriter bw = new BufferedWriter(osw);

            // ビューのテキストをテキストファイルに書き込む
            bw.write(strMemo);
            bw.flush();
            bw.close();
        }
        catch (IOException e) {
            Toast.makeText( getApplicationContext(), e.toString(), Toast.LENGTH_SHORT ).show();
        }
    }

    // メモファイルパスを取得する
    private String getMemoFilePath(String key) {
        return "SafeMemoApp_" + key + ".memo";
    }

}
