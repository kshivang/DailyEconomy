package com.shivang.dailyeconomy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.shivang.dailyeconomy.R;

/**
 * Created by kshivang on 07/12/16.
 * This is splash screen
 */

public class SplashActivity extends AppCompatActivity{

    boolean isAnimationFinished = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Animation bounceAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        findViewById(R.id.game_name).setAnimation(bounceAnim);

        bounceAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationFinished = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                }, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAnimationFinished) {
            finish();
        }
    }
}
