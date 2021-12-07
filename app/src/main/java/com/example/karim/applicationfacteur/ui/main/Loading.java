package com.example.karim.applicationfacteur.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataContractViolationException;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataNetworkException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Loading extends myActivity {

    protected boolean active = true;
    protected int splashTime = 5000;
    SessionManager session;
    EditText txt1,txt2;
    Button btn,btn2;
    String txtS;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    SQLiteHandler db ;
    int size;
    ProgressDialog pDialog;

    String  clientt , mode_liv,designation,code_barre;


    List<ODataEntity> entities ,entities1,entities2,entities3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);



        Bundle bundle = getIntent().getExtras();
          clientt= bundle.getString("client");
          mode_liv = bundle.getString("mode_liv");
           designation = bundle.getString("designation");
          code_barre = bundle.getString("code");


        // thread for displaying the SplashScreen
      /*  Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {

                    motif();
                    Log.e("han","han");

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
                    Intent intent = new Intent(getApplicationContext(),Raison.class);
                    intent.putExtra("mode_liv",String.valueOf(mode_liv));
                    intent.putExtra("designation",String.valueOf(designation));
                    intent.putExtra("code",String.valueOf(code_barre));
                    intent.putExtra("client",String.valueOf(client));

                    startActivity(intent);

                    finish();
                }
            }
        };

        splashThread.start();*/



        pDialog = new ProgressDialog(this);


        if(!registerInternetCheckReceiver(context))
        {
            Toast.makeText(getApplicationContext(),"Aucune connexion internet",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), Raison.class);
            intent.putExtra("mode_liv", String.valueOf(mode_liv));
            intent.putExtra("designation", String.valueOf(designation));
            intent.putExtra("code", String.valueOf(code_barre));
            intent.putExtra("client", String.valueOf(clientt));

            startActivity(intent);

            finish();

        }


        else
        {
            motif();
        }






    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }



    private  void  envois() {

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

                mDeviceBandwidthSampler.stopSampling();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Connecté",Toast.LENGTH_SHORT).show();

                        session = new SessionManager(getApplicationContext());
                        login = session.getUsername();
                        pwd = session.getPassword();

                        CredentialsProvider1 credProvider = CredentialsProvider1
                                .getInstance(lgtx, login, pwd);

                        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                                getApplicationContext()).supportBasicAuthUsing(credProvider).configure(
                                new HttpConversationManager(getApplicationContext()));


                        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
                        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getApplicationContext(),
                                requestFilter);
                        manager.addFilter(requestFilter);
                        manager.addFilter(responseFilter);
                        URL url = null;


                        try {
                            url = new URL(url_g);
                          //  url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        //Method to open a new online store asynchronously


                        //Check if OnlineODataStore opened successfully
                        OnlineODataStore store = null;

                        try {
                            store = OnlineODataStore.open(getApplicationContext(), url, manager, null);
                        } catch (ODataException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"une erreur est survenue, réessayer plus tard",Toast.LENGTH_SHORT).show();
                        }

                        // ArrayList<Agency> agencyList = new ArrayList<Agency>();

	/*	AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();*/

                        if (store != null) {

                            ODataProperty property;
                            ODataPropMap properties;




                            ODataResponseSingle resp = null;
                            try {
                                resp = store.executeReadEntitySet(
                                        "DNLLSet", null);
                            } catch (ODataNetworkException e) {
                                e.printStackTrace();
                            } catch (ODataParserException e) {
                                e.printStackTrace();
                            } catch (ODataContractViolationException e) {
                                e.printStackTrace();
                            }
                            //Get the response payload


                            //Get the response payload
                            ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                            //Get the list of ODataEntity

                            entities = feed.getEntities();

                            for (ODataEntity entity : entities) {
                                properties = entity.getProperties();

                                db = new SQLiteHandler(getApplicationContext());

/*
                                db.addData((String) properties.get("NumEnvoi").getValue(),
                                        (String) properties.get("Name1").getValue(),
                                        (String) properties.get("ZnumGsm").getValue(),
                                        (String) properties.get("Zcity").getValue() + " " + (String) properties.get("Landx").getValue(),
                                        String.valueOf(properties.get("Crbt").getValue()),
                                        "En cours","DNL", session.getUsername());*/

                                size = entities.size();
                                SharedPreferences sp = getSharedPreferences("your_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("your_int_key", size);
                                editor.commit();





                            }




                        }


                    }

                });

            }



        });

    }


    public  void motif() {





        Log.e("han1", "han1");

        db = new SQLiteHandler(getApplicationContext());
        Cursor cu = db.getAllmotif();



        if (cu.getCount() == 0) {
            // Toast.makeText(getApplicationContext()," 1",Toast.LENGTH_SHORT).show();

           pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();


            mConnectionClassManager = ConnectionClassManager.getInstance();
            mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
            mTries = 0;


            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://www.youtube.com/")
                    .build();
//        Toast.makeText(getApplicationContext(), "une erreur est survenue réssayer plus tard", Toast.LENGTH_SHORT).show();


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
                                //Toast.makeText(getApplicationContext(), "Timed out", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    mDeviceBandwidthSampler.stopSampling();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), "Connecté", Toast.LENGTH_SHORT).show();

                            session = new SessionManager(getApplicationContext());
                            login = session.getUsername();
                            pwd = session.getPassword();

                            CredentialsProvider1 credProvider = CredentialsProvider1
                                    .getInstance(lgtx, login, pwd);

                            HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                                    getApplicationContext()).supportBasicAuthUsing(credProvider).configure(
                                    new HttpConversationManager(getApplicationContext()));


                            XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
                            XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getApplicationContext(),
                                    requestFilter);
                            manager.addFilter(requestFilter);
                            manager.addFilter(responseFilter);
                            URL url = null;


                            try {
                                url = new URL(url_g);
                              //  url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                               // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            //Method to open a new online store asynchronously


                            //Check if OnlineODataStore opened successfully
                            OnlineODataStore store = null;

                            try {
                                store = OnlineODataStore.open(getApplicationContext(), url, manager, null);
                            } catch (ODataException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), " une erreur est survenue réssayer plus tard", Toast.LENGTH_SHORT).show();
                            }

                            // ArrayList<Agency> agencyList = new ArrayList<Agency>();

	/*	AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();*/

                            if (store != null) {

                                ODataProperty property;
                                ODataPropMap properties;


                                ODataResponseSingle resp = null;
                                try {
                                    resp = store.executeReadEntitySet(
                                            "MOTIFSet", null);
                                } catch (ODataNetworkException e) {
                                    e.printStackTrace();
                                } catch (ODataParserException e) {
                                    e.printStackTrace();
                                } catch (ODataContractViolationException e) {
                                    e.printStackTrace();
                                }
                                //Get the response payload


                                //Get the response payload
                                ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                                //Get the list of ODataEntity

                                entities = feed.getEntities();

                                for (ODataEntity entity : entities) {
                                    properties = entity.getProperties();


                               /* motif = (String) properties.get("ZsdMotif").getValue();
                                designation = (String) properties.get("ZsdDesignation").getValue();*/
                                    db.addmotif((String) properties.get("ZsdMotif").getValue(), (String) properties.get("ZsdDesignation").getValue());

                                    //  Toast.makeText(getApplicationContext(), "hanane", Toast.LENGTH_SHORT).show();


                                    ////////////////////////////////////////////////////////


                                }


                                ODataProperty property1;
                                ODataPropMap properties1;


                                ODataResponseSingle resp1 = null;
                                try {
                                    resp1 = store.executeReadEntitySet(
                                            "MESURESet", null);
                                } catch (ODataNetworkException e) {
                                    e.printStackTrace();
                                } catch (ODataParserException e) {
                                    e.printStackTrace();
                                } catch (ODataContractViolationException e) {
                                    e.printStackTrace();
                                }
                                //Get the response payload


                                //Get the response payload
                                ODataEntitySet feed1 = (ODataEntitySet) resp1.getPayload();
                                //Get the list of ODataEntity

                                entities1 = feed1.getEntities();

                                for (ODataEntity entity1 : entities1) {
                                    properties1 = entity1.getProperties();


                                /*motiff = (String) properties1.get("ZsdMotif").getValue();
                                mesure = (String) properties1.get("ZsdDesignation").getValue();*/
                                    db.addmesure((String) properties1.get("ZsdMotif").getValue(), (String) properties1.get("ZsdDesignation").getValue(), (String) properties1.get("Stat").getValue());

                                    //Toast.makeText(getApplicationContext(), mesure + "mesure", Toast.LENGTH_SHORT).show();


                                    ////////////////////////////////////////////////////////


                                }


                                ODataProperty property2;
                                ODataPropMap properties2;

                                Log.e("han3", "han3");

                                ODataResponseSingle resp2 = null;
                                try {
                                    resp2 = store.executeReadEntitySet(
                                            "DivisonSet", null);
                                } catch (ODataNetworkException e) {
                                    e.printStackTrace();
                                    Log.e("han4", e.toString());
                                } catch (ODataParserException e) {
                                    e.printStackTrace();

                                    Log.e("han5", e.toString());
                                } catch (ODataContractViolationException e) {
                                    e.printStackTrace();
                                    Log.e("han6", e.toString());
                                }
                                //Get the response payload


                                //Get the response payload
                                ODataEntitySet feed2 = (ODataEntitySet) resp2.getPayload();
                                //Get the list of ODataEntity

                                entities2 = feed2.getEntities();

                                for (ODataEntity entity2 : entities2) {
                                    properties2 = entity2.getProperties();


                                /*motiff = (String) properties1.get("ZsdMotif").getValue();
                                mesure = (String) properties1.get("ZsdDesignation").getValue();*/
                                    db.adddivision((String) properties2.get("Werks").getValue() + "  " + (String) properties2.get("Name1").getValue());


                                }


//////////////////////////////////


                                ODataProperty property3;
                                ODataPropMap properties3;

                                Log.e("han3", "han3");

                                ODataResponseSingle resp3 = null;
                                try {
                                    resp3 = store.executeReadEntitySet(
                                            "RelaisSet", null);
                                } catch (ODataNetworkException e) {
                                    e.printStackTrace();
                                    Log.e("han4", e.toString());
                                } catch (ODataParserException e) {
                                    e.printStackTrace();

                                    Log.e("han5", e.toString());
                                } catch (ODataContractViolationException e) {
                                    e.printStackTrace();
                                    Log.e("han6", e.toString());
                                }
                                //Get the response payload


                                //Get the response payload
                                ODataEntitySet feed3 = (ODataEntitySet) resp3.getPayload();
                                //Get the list of ODataEntity

                                entities3 = feed3.getEntities();

                                for (ODataEntity entity3 : entities3) {
                                    properties3 = entity3.getProperties();


                                /*motiff = (String) properties1.get("ZsdMotif").getValue();
                                mesure = (String) properties1.get("ZsdDesignation").getValue();*/
                                    db.addrelais((String) properties3.get("Pudoid").getValue() + "  " + (String) properties3.get("Designation").getValue());


                                }
                                if (entities.size() != 0 && entities1.size() != 0 && entities2.size() != 0 && entities3.size() != 0) {
                                    // pDialog.dismiss();

                                    Intent intent = new Intent(getApplicationContext(), Raison.class);
                                    intent.putExtra("mode_liv", String.valueOf(mode_liv));
                                    intent.putExtra("designation", String.valueOf(designation));
                                    intent.putExtra("code", String.valueOf(code_barre));
                                    intent.putExtra("client", String.valueOf(clientt));

                                    startActivity(intent);

                                    finish();


                                }
                            }


                            else
                            {
                                Toast.makeText(getApplicationContext(),"une erreur est survenue, réessayer plus tard",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), Raison.class);
                                intent.putExtra("mode_liv", String.valueOf(mode_liv));
                                intent.putExtra("designation", String.valueOf(designation));
                                intent.putExtra("code", String.valueOf(code_barre));
                                intent.putExtra("client", String.valueOf(clientt));

                                startActivity(intent);

                                finish();


                            }




                        }

                    });

                }


            });



        }
        else{

            Intent intent = new Intent(getApplicationContext(),Raison.class);
            intent.putExtra("mode_liv",String.valueOf(mode_liv));
            intent.putExtra("designation",String.valueOf(designation));
            intent.putExtra("code",String.valueOf(code_barre));
            intent.putExtra("client",String.valueOf(clientt));

            startActivity(intent);

            finish();


       }


    }











}