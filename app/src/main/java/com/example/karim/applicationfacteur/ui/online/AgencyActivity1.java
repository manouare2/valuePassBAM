package com.example.karim.applicationfacteur.ui.online;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.myActivity;

import java.util.Timer;
import java.util.TimerTask;
/* l'écran qui affiche le détail des envois (client / GSM/ services ....
* Fragment : AgencyFragment1 */

public class AgencyActivity1 extends myActivity {
    private static final String TAG_DETAILS = "agency_details";
    SessionManager session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //Initialize AgencyFragment
        Fragment fragment = Fragment.instantiate(this, AgencyFragment1.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_DETAILS);
        fragmentTransaction.commit();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this,AgencyListActivity1.class);
            startActivity(intent);
            finish();
        }

        return false;
        // Disable back button..............
    }



}
