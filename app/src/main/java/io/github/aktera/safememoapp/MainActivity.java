package io.github.aktera.safememoapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 指紋認証の権限チェック
        if (checkSelfPermission(Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText( getApplicationContext(), getResources().getString(R.string.fingerprint_permission_fail), Toast.LENGTH_SHORT ).show();
            return;
        }

        // 指紋認証マネージャ
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);

        // ハードウェアが指紋認証をサポートしているかチェックする
        if ((fingerprintManager.isHardwareDetected() == false) || (fingerprintManager.hasEnrolledFingerprints() == false)) {
            Toast.makeText( getApplicationContext(), getResources().getString(R.string.fingerprint_hardware_fail), Toast.LENGTH_SHORT ).show();
            return;
        }

        // 指紋認証を行う
        fingerprintManager.authenticate(null, null, 0, new FingerprintManager.AuthenticationCallback() {

            // 認証成功
            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                startActivity(intent);
            }

            // 認証失敗
            @Override
            public void onAuthenticationFailed() {
                Toast.makeText( getApplicationContext(), getResources().getString(R.string.fingerprint_authentication_fail), Toast.LENGTH_SHORT ).show();
            }

            // 認証エラー（読み取り不能など）
            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                Toast.makeText( getApplicationContext(), helpString.toString(), Toast.LENGTH_SHORT ).show();
            }

            // 致命的な認証エラー（コールバックがキャンセルされたり、認証制限オーバーだったり）
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                // 認証操作がキャンセルされた場合は特に何も表示しない
                if (errorCode == FingerprintManager.FINGERPRINT_ERROR_CANCELED) {
                    return;
                }

                Toast.makeText( getApplicationContext(), errString.toString(), Toast.LENGTH_SHORT ).show();
                return;
            }

        }, new Handler());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }}
