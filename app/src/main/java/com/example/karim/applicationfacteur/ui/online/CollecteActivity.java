package com.example.karim.applicationfacteur.ui.online;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.ui.main.MainCL;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.myActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hanane on 21/02/2019.
 */

public class CollecteActivity  extends MainCL {
    private static final String TAG_DETAILS = "collecte_details";
    SessionManager session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                FDR_notif();
            }

        };
        timer.schedule (hourlyTask, 0l, 1000*1*60);


        Fragment fragment = Fragment.instantiate(this, CollecteFragment.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_DETAILS);
        fragmentTransaction.commit();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this,CollecteListActivity.class);
            startActivity(intent);
            finish();
        }

        return false;
        // Disable back button..............
    }



}

