package com.example.rift.tifr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private ImageView splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        splashView = (ImageView) findViewById(R.id.splash_logo);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.transition);
        splashView.startAnimation(anim);

        final Intent intent = new Intent(this,MainActivity.class);
        Thread timer= new Thread(){
            public void run(){
                try
                {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally{
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }

}



