package com.example.karim.applicationfacteur.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;


public class Acceuil extends AppCompatActivity {
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceuil);

          handleResult();



    }


    public void handleResult() {

        TextView myMsg = new TextView(this);
        myMsg.setText("voulez vous passer à:");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        myMsg.setTextSize(15);
        myMsg.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle(myMsg.getText().toString());
        builder.setPositiveButton("Scann", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Acceuil.this,Scann.class);
                startActivity(intent);


            }
        });
        builder.setNeutralButton("liste commandes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Acceuil.this,AgencyListActivity1.class);
                startActivity(intent);


            }
        });


        AlertDialog alert1 = builder.create();
        alert1.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuacceuil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_liste) {
            Intent intent = new Intent(Acceuil.this,AgencyListActivity1.class);
            startActivity(intent);

            return true;
        }
       else if (item.getItemId() == R.id.menu_item_scann) {
            Intent intent = new Intent(Acceuil.this,Scann.class);
            startActivity(intent);

            return true;
        }

        else if (item.getItemId() == R.id.menu_item_dec) {



            TextView myMsg = new TextView(this);
            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

            myMsg.setTextColor(Color.WHITE);
            myMsg.setText("les données seront pérdues lors de la déconnexion ");
            myMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,4);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(myMsg.getText().toString());


            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    deleteAppData();
                    session = new SessionManager(getApplicationContext());
                    session.setLogin(false);

                    restart(getApplicationContext(), 0);


                }
            });
            builder.setNeutralButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Acceuil.this,Acceuil.class);
                    startActivity(intent);


                }
            });


            AlertDialog alert1 = builder.create();
            alert1.show();


            return true;
        }






        return super.onOptionsItemSelected(item);
    }


    private void deleteAppData() {
        try {
            // clearing app data

            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            //restart(getApplicationContext(),0);


            runtime.exec("pm clear "+packageName);
            Intent intent = new Intent(Acceuil.this,LogonActivity.class);

            startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        } }

    public void restart(Context context, int delay) {
        if (delay == 0) {
            delay = 1;
        }
        Log.e("", "restarting app");
        Intent restartIntent = new Intent(Acceuil.this,LogonActivity.class);


        PendingIntent intent = PendingIntent.getActivity(
                context, 0,
                restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(2);
    }

}