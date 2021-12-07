package com.example.karim.applicationfacteur.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.types.Agency;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class Journee extends myActivity {

    EditText txt1,txt2;
    Button btn;
    String txtS;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    EditText txt3;
    EditText txt4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityjournee);

        btn= (Button) findViewById(R.id.jrne);
        txt1= (EditText) findViewById(R.id.date1);
        txt3= (EditText) findViewById(R.id.liv);
        txt4= (EditText) findViewById(R.id.nonliv);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);




        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());

        Cursor cursor= (new SQLiteHandler(this)).getUserCmd(session.getUsername());
        int val = 0;
        int val1 = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(6).equalsIgnoreCase("livré"))
                val++;
            else if (cursor.getString(6).equalsIgnoreCase("Non livré"))
                val1++;

        }


        txt1.setText("Date : "+currentDateTimeString);
        txt1.setFocusable(false);


        txt3.setText("Envoi Livré : "+val );
        txt3.setFocusable(false);
        txt4.setText("Envoi Non livré :"+val1);
        txt4.setFocusable(false);





        btn.setOnClickListener(new View.OnClickListener() {



            public void onClick(View view) {

                if(registerInternetCheckReceiver(context))

                {


               cloturer();





                }
                else
                {
                    Toast.makeText(getApplicationContext(),"connectez vous à internet puis réessayez",Toast.LENGTH_SHORT).show();
                }









            }


        });


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

    private void cloturer() {
        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mTries = 0;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.youtube.com/")
                .build();

        mDeviceBandwidthSampler.startSampling();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                mDeviceBandwidthSampler.stopSampling();
                // Retry for up to 10 times until we find a ConnectionClass.
                if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                    mTries++;
                    //checkNetworkQuality();

                }
                if (!mDeviceBandwidthSampler.isSampling()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Timed out",Toast.LENGTH_SHORT).show();
                        }
                    });

                    //
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InsertBatch("Clic");
                    }
                });

            }
        });
    }




}

