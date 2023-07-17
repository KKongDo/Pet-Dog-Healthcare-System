package com.example.kongbaekdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

//애플리케이션 초기 작동 시 1초동안 앱로고 시각화
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new splashHandler() ,1000);
    }
    private class splashHandler implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}