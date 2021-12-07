package com.example.karim.applicationfacteur.ui.online;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.ui.main.Acceuil1;
import com.example.karim.applicationfacteur.ui.main.MainCL;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.myActivity;
import com.sap.maf.tools.logon.core.LogonCoreContext;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hanane on 12/02/2019.
 */

public class CollecteListActivity extends MainCL {
    private static final String TAG_LIST = "agency_list";
    SessionManager session;
    private View myView;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    String text = "";
    static LogonCoreContext lgtx;
    static List<Agency3> agencies;
    String login, pwd;
    Agency3 agency;
    String code_barre;

    CollecteListFragment frg;

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


        Fragment fragment = Fragment.instantiate(this, CollecteListFragment.class.getName());


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_LIST);
        fragmentTransaction.commit();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {  Intent intent = new Intent(this,Acceuil1.class);
            startActivity(intent);
            finish();

        }

        return false;
        // Disable back button..............
    }

}
