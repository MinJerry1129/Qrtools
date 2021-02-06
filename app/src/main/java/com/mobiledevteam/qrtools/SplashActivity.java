package com.mobiledevteam.qrtools;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    ImageView icon;
    ImageView letter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        icon = findViewById(R.id.icon);

        Glide.with(SplashActivity.this)
                .load(R.drawable.sicon)
                .into(icon);
        letter = findViewById(R.id.letter);

        Glide.with(SplashActivity.this)
                .load(R.drawable.letter)
                .into(letter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, StartActivity.class));
                finish();
            }
        }, 4000);
    }
}
