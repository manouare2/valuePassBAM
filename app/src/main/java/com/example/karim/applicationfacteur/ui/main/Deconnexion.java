package com.example.karim.applicationfacteur.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.example.karim.applicationfacteur.R;

public class Deconnexion extends Activity {

    protected boolean active = true;
    protected int splashTime = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deconnexion);


        // thread for displaying the SplashScreen
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    sleep(100);
                    long waited = System.currentTimeMillis() - startTime;
                    while (active && (waited < splashTime)) {
                        sleep(100);
                        if (active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    finishAffinity();
                }
            }
        };

        splashThread.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
