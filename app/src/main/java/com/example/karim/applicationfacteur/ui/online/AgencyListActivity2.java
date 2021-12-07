package com.example.karim.applicationfacteur.ui.online;


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
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.myActivity;
import com.sap.maf.tools.logon.core.LogonCoreContext;

import java.util.List;


public class AgencyListActivity2 extends myActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize AgencyListFragment
        Fragment fragment = Fragment.instantiate(this, AgencyListFragment2.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_LIST);
        fragmentTransaction.commit();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {  Intent intent = new Intent(this,AgencyListActivity1.class);
            startActivity(intent);
            finish();

        }

        return false;
        // Disable back button..............
    }


}