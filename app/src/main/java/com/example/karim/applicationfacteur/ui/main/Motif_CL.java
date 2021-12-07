package com.example.karim.applicationfacteur.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.CollecteListActivity;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.google.gson.Gson;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataContractViolationException;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataNetworkException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.impl.ODataEntityDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataRequestChangeSet;
import com.sap.smp.client.odata.store.ODataRequestParamBatch;
import com.sap.smp.client.odata.store.ODataRequestParamSingle;
import com.sap.smp.client.odata.store.ODataResponse;
import com.sap.smp.client.odata.store.ODataResponseBatch;
import com.sap.smp.client.odata.store.ODataResponseBatchItem;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.sap.smp.client.odata.store.impl.ODataRequestChangeSetDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataRequestParamBatchDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataRequestParamSingleDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataResponseBatchDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataResponseChangeSetDefaultImpl;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.PendingIntent.getActivity;

/**
 * Created by hanane on 11/04/2019.
 */

public class Motif_CL extends MainCL{
    private SQLiteHandler db;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    Button btn;
    String raison;
    String raison2;
    Calendar calandrier=Calendar.getInstance();
    List<String> items = new ArrayList<String>();
    List<String> items2 = new ArrayList<String>();
    List<String> items3 = new ArrayList<String>();
    List<String> items4 = new ArrayList<String>();
    SessionManager session;
    String val, val1,des;
    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> spinnerAdapter2;
    ArrayAdapter<String> spinnerAdapter3;
    ArrayAdapter<String> spinnerAdapter4;
    Context context =this;;
    TextView txt;
    Collecte CL;
     EditText editText;
    LinearLayout linearLayout;

    String motif,designation,motiff,mesure,stat;
    EditText client;
    String code_barre;
    String div,relais,obj;
    Motif_CL cl_activity;
String minutes;
    ProgressDialog pDialog ;
    List<ODataEntity> entities ,entities1,entities2,entities3;





    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.motif_activity_cl);

        context = this;

        btn = (Button) findViewById(R.id.btn);
        spinner = (Spinner) findViewById(R.id.spinner);;
        client = (EditText) findViewById(R.id.client);
      //  spinner2.setBackgroundColor(0x000);
        db = new SQLiteHandler(this);
        cl_activity = this;

        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        Bundle bundle =  getIntent().getExtras();
        if (bundle != null) {
            CL = bundle.getParcelable("CollecteSelected");

            String json = gson.toJson(CL); // myObject - instance of MyObject
            prefsEditor.putString("Collecte", json);
            prefsEditor.commit();

        }

        else{

            String json = mPrefs.getString("Collecte", "");
            CL = gson.fromJson(json, Collecte.class);

        }
        client.setText(CL.getNom_client());
        client.setFocusable(false);
      /*  client.getBackground().setColorFilter(getResources().getColor(R.color.sap_uex_dark_red),
                PorterDuff.Mode.SRC_ATOP);*/


        Cursor cr = db.getAllmotif_cl();

        if (cr.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "une erreur est survenue réessayer plus tard ", Toast.LENGTH_SHORT).show();
        } else {


            for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {


                if (!items.contains(cr.getString(2)))

                {
                    items.add(cr.getString(2));
            }
            }


            spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    raison = (String) adapterView.getItemAtPosition(i);



                    editText= new EditText(getApplication());
                    if(raison.equalsIgnoreCase("AUTRES"))
                    {


                        linearLayout = (LinearLayout) findViewById(R.id.layout2);
                        editText.setHint("Saisir autre motif");
                        editText.setHintTextColor(R.color.sap_uex_black);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        editText.setPadding(20, 20, 20, 20);
                        editText.setTextColor(R.color.sap_uex_black);

                        // Add EditText to LinearLayout
                        if (linearLayout != null) {
                            linearLayout.addView(editText);
                        }



                    }
                    else {

                        if (linearLayout!=null) {


                            for (int x = 0; x < linearLayout.getChildCount(); x++) {
                                if (linearLayout.getChildAt(x) instanceof TextView) {
                                    linearLayout.removeView(linearLayout.getChildAt(x));
                                }
                            }
                        }
                    }



                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



        }


        btn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {


                final String hour = String.valueOf(calandrier.get(Calendar.HOUR_OF_DAY));
                final String minute = String.valueOf(calandrier.get(Calendar.MINUTE));
                session = new SessionManager(getApplicationContext());


                if(minute.length()==1)
                {
                    minutes ="0"+minute;



                    if(raison.equalsIgnoreCase("AUTRES"))
                    {
                        if(registerInternetCheckReceiver(getApplicationContext())) {


                            InsertENVOI(getApplicationContext(), CL, null, hour + minutes, "", editText.getText().toString(), Motif_CL.this,"N");
                        }

                        else
                        {
                            db.addData_CL_offline(CL.getNum_fdr(),CL.getNum_poste(), CL.getNum_client(), CL.getNom_client(), CL.getInterlocuteur(), CL.getTelephone_client(), CL.getAdresse_client(), CL.getDate(), CL.getHeure(),session.getUsername(), CL.getStat(),CL.getAgent(),CL.getAgence(), CL.getType_cl(),hour+minutes, CL.getCode_2D(),editText.getText().toString(),"","");

                            db.updateStatut(CL.getNum_client(),"Non Traité",hour + minute,CL.getNum_fdr());



                            Intent intent = new Intent(getApplicationContext(), CollecteListActivity.class);
                            //  intent.putExtra("code",String.valueOf(obj));
                            startActivity(intent);
                            finish();




                        }
                    }
                    else

                    {

                          if(registerInternetCheckReceiver(getApplicationContext())) {
                              InsertENVOI(getApplicationContext(), CL, null, hour + minutes, "", raison, Motif_CL.this,"N");

                          }
                          else
                          {
                              db.addData_CL_offline(CL.getNum_fdr(),CL.getNum_poste(), CL.getNum_client(), CL.getNom_client(), CL.getInterlocuteur(), CL.getTelephone_client(), CL.getAdresse_client(), CL.getDate(), CL.getHeure(),session.getUsername(), CL.getStat(),CL.getAgent(),CL.getAgence(), CL.getType_cl(),hour+minutes, CL.getCode_2D(),raison,"","");
                              db.updateStatut(CL.getNum_client(),"Non Traité",hour + minute,CL.getNum_fdr());


                              Intent intent = new Intent(getApplicationContext(), CollecteListActivity.class);
                              //  intent.putExtra("code",String.valueOf(obj));
                              startActivity(intent);
                              finish();
                          }
                    }


                }

                else
                {



                    if(raison.equalsIgnoreCase("AUTRES"))
                    {
                        if(registerInternetCheckReceiver(getApplicationContext())) {
                            InsertENVOI(getApplicationContext(), CL, null, hour + minute, "", editText.getText().toString(), Motif_CL.this,"N");

                        }
                        else
                        {
                            db.addData_CL_offline(CL.getNum_fdr(),CL.getNum_poste(), CL.getNum_client(), CL.getNom_client(), CL.getInterlocuteur(), CL.getTelephone_client(), CL.getAdresse_client(), CL.getDate(), CL.getHeure(),session.getUsername(), CL.getStat(),CL.getAgent(),CL.getAgence(), CL.getType_cl(),hour+minute, CL.getCode_2D(),editText.getText().toString(),"","");

                            db.updateStatut(CL.getNum_client(),"Non Traité",hour + minute,CL.getNum_fdr());



                            Intent intent = new Intent(getApplicationContext(), CollecteListActivity.class);
                            //  intent.putExtra("code",String.valueOf(obj));
                            startActivity(intent);
                            finish();


                        }
                    }
                    else

                    {
                        if(registerInternetCheckReceiver(getApplicationContext())) {
                            InsertENVOI(getApplicationContext(), CL, null, hour + minute, "", raison, Motif_CL.this,"N");
                        }
                        else
                        {
                            db.addData_CL_offline(CL.getNum_fdr(),CL.getNum_poste(), CL.getNum_client(), CL.getNom_client(), CL.getInterlocuteur(), CL.getTelephone_client(), CL.getAdresse_client(), CL.getDate(), CL.getHeure(),session.getUsername(), CL.getStat(),CL.getAgent(),CL.getAgence(), CL.getType_cl(),hour+minute, CL.getCode_2D(),raison,"","");

                            db.updateStatut(CL.getNum_client(),"Non Traité",hour + minute,CL.getNum_fdr());




                            Intent intent = new Intent(getApplicationContext(), CollecteListActivity.class);
                            //  intent.putExtra("code",String.valueOf(obj));
                            startActivity(intent);
                            finish();

                        }
                    }

                }




            }


        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Motif_CL.this, CollecteListActivity.class);
            startActivity(intent);
        }

        return false;
        // Disable back button..............
    }


    private boolean registerInternetCheckReceiver1() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
        String status = getConnectivityStatusString(getApplicationContext());
        a = setSnackbarMessage(status, false);
        return a;

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status, false);

        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    private boolean setSnackbarMessage(String status, boolean showBar) {

        db = new SQLiteHandler(getApplicationContext());
        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
        } else {
            internetStatus = "Lost Internet Connection";
        }
        if (internetStatus.equalsIgnoreCase("Lost Internet Connection")) {
            if (internetConnected) {

                internetConnected = false;


            }
        } else {

            if (!internetConnected) {
                internetConnected = true;


            }
        }

        return internetConnected;


    }

   /* public void InsertBatch(String code_envoi,String stat,String motif,String mesure ,String modepaiement,String ModeLiv,String TypePid,String Destinataire ,String Pid,String div,String relais)

    {


        session = new SessionManager(this);
        login = session.getUsername();
        pwd = session.getPassword();
        Agency1 agen = null;
        ODataResponse oDataResponse = null;

        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx, login, pwd);

        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                getApplicationContext()).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(getApplicationContext()));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getApplicationContext(), requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
            url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        OnlineODataStore store = null;


        try {
            store = OnlineODataStore.open(getApplicationContext(), url, manager, null);
        } catch (ODataException e) {
            e.printStackTrace();
            Log.e("e0", e.toString());
        }

        ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.Affectation");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("AffectationSet");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");

        db = new SQLiteHandler(this);

        cursor = db.getAll3(session.getUsername());

        if (cursor.getCount() == 0) {


            Log.e("hann1","hann1");
            newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", code_envoi));
            newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
            newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Non livré"));
            newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "non liv"));

            Log.e("hann2","hann2");
            if(designation.equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD"))
            {
                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
            }

            else
            {
                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

            }
            newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));
            Log.e("hann3","hann3");
            newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat",stat));
            newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif",motif));
            newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure",mesure));
            newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement",modepaiement));
            newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid",TypePid));
            newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid",Pid));
            newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire",Destinataire));
            newEntity.getProperties().put("ModeLiv", new ODataPropertyDefaultImpl("ModeLiv",ModeLiv));
            newEntity.getProperties().put("PointRelais", new ODataPropertyDefaultImpl("PointRelais",relais));
            newEntity.getProperties().put("Division", new ODataPropertyDefaultImpl("Division",div));



            Log.e("hann4","hann4");
            batchItem.setPayload(newEntity);

            Log.e("hann5","hann5");
            //batchItem.setPayload(newEntity2);


            Map<String, String> createHeaders = new HashMap<String, String>();

            Log.e("hann6","hann6");

            createHeaders.put("accept", "application/atom+xml");

            Log.e("hann7","hann7");

            createHeaders.put("content-type", "application/atom+xml");

            Log.e("hann8","hann8");
            batchItem.setOptions(createHeaders);


            Log.e("hann9","hann9");
            ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();
            Log.e("hann10","hann10");

            try {
                Log.e("hann11i","hann11i");
                changeSetItem.add(batchItem);

                Log.e("hann11","hann11");
            } catch (ODataException e) {
                e.printStackTrace();
                Log.e("e1", e.toString());
            }

// Add batch item to batch request

            try {
                Log.e("hann12","hann12");

                requestParamBatch.add(changeSetItem);
                Log.e("hann13","hann13");

            } catch (ODataException e) {
                e.printStackTrace();
                Log.e("e2", e.toString());
            }

            try {
                Log.e("hann14","hann14");

                oDataResponse = store.executeRequest(requestParamBatch);
                Log.e("hann15","hann15");

            } catch (ODataNetworkException e) {
                e.printStackTrace();
                Log.e("hann16", e.toString());


            } catch (ODataParserException e) {
                e.printStackTrace();
                Log.e("e4", e.toString());
            } catch (ODataContractViolationException e) {
                e.printStackTrace();
                Log.e("e5", e.toString());
            }


            Map<ODataResponse.Headers, String> headerMap = oDataResponse.getHeaders();
            Log.e("1", "16");

            if (headerMap != null) {
                Log.e("1", "16");
                String code = headerMap.get(ODataResponse.Headers.Code);
                Log.e("1", "17");
            }

// Get batch response

            if (oDataResponse instanceof ODataResponseBatchDefaultImpl) {
                Log.e("1", "18");
                ODataResponseBatch batchResponse = (ODataResponseBatch) oDataResponse;
                Log.e("1", "19");
                List<ODataResponseBatchItem> responses = batchResponse.getResponses();
                Log.e("1", "20");
                for (ODataResponseBatchItem response : responses) {
                    Log.e("1", "21");
                    // Check if batch item is a change set

                    if (response instanceof ODataResponseChangeSetDefaultImpl) {
                        Log.e("1", "22");
                        ODataResponseChangeSetDefaultImpl changesetResponse = (ODataResponseChangeSetDefaultImpl) response;
                        Log.e("1", "23");
                        List<ODataResponseSingle> singles = changesetResponse.getResponses();
                        Log.e("1", "24");
                        for (ODataResponseSingle singleResponse : singles) {
                            Log.e("1", "25");
                            // Get Custom tag

                            String customTag = singleResponse.getCustomTag();

                            // Get http status code for individual responses

                            headerMap = singleResponse.getHeaders();

                            String code = headerMap.get(ODataResponse.Headers.Code);


                            if (code.equalsIgnoreCase("201")) {
                                Toast.makeText(getApplicationContext(), "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();
                                db.updateDataglobal(code_envoi,"Non livré");

                                //    db.deleteData3();

                                db.deleteenvoi(session.getUsername());


                            } else {
                                Toast.makeText(getApplicationContext(), "une erreur est survenue, résssayer plus tard ", Toast.LENGTH_SHORT).show();


                            }

                            Log.e("1", "26");
                            // Get individual response

                            ODataPayload payload = singleResponse.getPayload();

                            if (payload != null) {

                                if (payload instanceof ODataError) {

                                    ODataError oError = (ODataError) payload;

                                    String uiMessage = oError.getMessage();

                                } else {

                                    // TODO do something with payload

                                }

                            }

                        }

                    } else {

                        // TODO Check if batch item is a single READ request

                    }

                }
                Log.e("1", "27");
            }


        } else {

            Log.e("han2","han2");

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                //Toast.makeText(getApplicationContext(), "Code Envoi :" + cursor.getString(1), Toast.LENGTH_SHORT).show();


                newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", cursor.getString(1)));
                newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", cursor.getString(3)));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", cursor.getString(4)));


                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", cursor.getString(5)));
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", cursor.getString(6)));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", cursor.getString(7)));

                newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif",cursor.getString(8)));
                newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure",cursor.getString(9)));
                newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement",cursor.getString(10)));
                newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid",cursor.getString(11)));
                newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid",cursor.getString(12)));
                newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire",cursor.getString(13)));
                newEntity.getProperties().put("ModeLiv", new ODataPropertyDefaultImpl("ModeLiv",cursor.getString(14)));
                newEntity.getProperties().put("PointRelais", new ODataPropertyDefaultImpl("PointRelais",relais));
                newEntity.getProperties().put("Division", new ODataPropertyDefaultImpl("Division",div));









                batchItem.setPayload(newEntity);
                //batchItem.setPayload(newEntity2);


                Map<String, String> createHeaders = new HashMap<String, String>();

                createHeaders.put("accept", "application/atom+xml");

                createHeaders.put("content-type", "application/atom+xml");

                batchItem.setOptions(createHeaders);


                ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();


                try {
                    changeSetItem.add(batchItem);

                } catch (ODataException e) {
                    e.printStackTrace();
                    Log.e("e1", e.toString());
                }

// Add batch item to batch request

                try {

                    requestParamBatch.add(changeSetItem);

                } catch (ODataException e) {
                    e.printStackTrace();
                    Log.e("e2", e.toString());
                }

                try {
                    oDataResponse = store.executeRequest(requestParamBatch);
                    Log.e("1", "15");
                } catch (ODataNetworkException e) {
                    e.printStackTrace();


                } catch (ODataParserException e) {
                    e.printStackTrace();
                    Log.e("e4", e.toString());
                } catch (ODataContractViolationException e) {
                    e.printStackTrace();
                    Log.e("e5", e.toString());
                }
            }

            newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", code_envoi));
            newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
            newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Livraison"));
            newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "liv"));


            if( designation.equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD"))
            {
                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
            }

            else
            {
                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

            }
            newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

            newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat","E0010"));
            newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif",motif));
            newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure",mesure));
            newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement",modepaiement));
            newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid",TypePid));
            newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid",Pid));
            newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire",Destinataire));
            newEntity.getProperties().put("ModeLiv", new ODataPropertyDefaultImpl("ModeLiv",ModeLiv));
            newEntity.getProperties().put("PointRelais", new ODataPropertyDefaultImpl("PointRelais",relais));
            newEntity.getProperties().put("Division", new ODataPropertyDefaultImpl("Division",div));




            batchItem.setPayload(newEntity);
            //batchItem.setPayload(newEntity2);


            Map<String, String> createHeaders = new HashMap<String, String>();

            createHeaders.put("accept", "application/atom+xml");

            createHeaders.put("content-type", "application/atom+xml");

            batchItem.setOptions(createHeaders);


            ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();


            try {
                changeSetItem.add(batchItem);

            } catch (ODataException e) {
                e.printStackTrace();
                Log.e("e1", e.toString());
            }

// Add batch item to batch request

            try {

                requestParamBatch.add(changeSetItem);

            } catch (ODataException e) {
                e.printStackTrace();
                Log.e("e2", e.toString());
            }

            try {
                oDataResponse = store.executeRequest(requestParamBatch);
                Log.e("1", "15");
            } catch (ODataNetworkException e) {
                e.printStackTrace();


            } catch (ODataParserException e) {
                e.printStackTrace();
                Log.e("e4", e.toString());
            } catch (ODataContractViolationException e) {
                e.printStackTrace();
                Log.e("e5", e.toString());
            }


            Map<ODataResponse.Headers, String> headerMap = oDataResponse.getHeaders();
            Log.e("1", "16");

            if (headerMap != null) {
                Log.e("1", "16");
                String code = headerMap.get(ODataResponse.Headers.Code);
                Log.e("1", "17");
            }

// Get batch response

            if (oDataResponse instanceof ODataResponseBatchDefaultImpl) {
                Log.e("1", "18");
                ODataResponseBatch batchResponse = (ODataResponseBatch) oDataResponse;
                Log.e("1", "19");
                List<ODataResponseBatchItem> responses = batchResponse.getResponses();
                Log.e("1", "20");
                for (ODataResponseBatchItem response : responses) {
                    Log.e("1", "21");
                    // Check if batch item is a change set

                    if (response instanceof ODataResponseChangeSetDefaultImpl) {
                        Log.e("1", "22");
                        ODataResponseChangeSetDefaultImpl changesetResponse = (ODataResponseChangeSetDefaultImpl) response;
                        Log.e("1", "23");
                        List<ODataResponseSingle> singles = changesetResponse.getResponses();
                        Log.e("1", "24");
                        for (ODataResponseSingle singleResponse : singles) {
                            Log.e("1", "25");
                            // Get Custom tag

                            String customTag = singleResponse.getCustomTag();

                            // Get http status code for individual responses

                            headerMap = singleResponse.getHeaders();

                            String code = headerMap.get(ODataResponse.Headers.Code);


                            if (code.equalsIgnoreCase("201")) {
                                Toast.makeText(getApplicationContext(), "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();
                                db.updateDataglobal(code_envoi,"Non livré");
                                cursor = db.getAll3(session.getUsername());
                                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                                    db.updateDataglobal(cursor.getString(1),"Non livré");
                                }
                                //    db.deleteData3();
                                db.deleteenvoi(session.getUsername());





                            } else {
                                Toast.makeText(getApplicationContext(), "une erreur est survenue,réssayer plus tard ", Toast.LENGTH_SHORT).show();


                            }

                            Log.e("1", "26");
                            // Get individual response

                            ODataPayload payload = singleResponse.getPayload();

                            if (payload != null) {

                                if (payload instanceof ODataError) {

                                    ODataError oError = (ODataError) payload;

                                    String uiMessage = oError.getMessage();

                                } else {

                                    // TODO do something with payload

                                }

                            }

                        }

                    } else {

                        // TODO Check if batch item is a single READ request

                    }

                }
                Log.e("1", "27");
            }


        }


    }


*/




   /* public  void motif() {


        db = new SQLiteHandler(getApplicationContext());
        Cursor cu = db.getAllmotif();


        if (cu.getCount() == 0) {


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
                                url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            //Method to open a new online store asynchronously



                            OnlineODataStore store = null;

                            try {
                                store = OnlineODataStore.open(getApplicationContext(), url, manager, null);
                            } catch (ODataException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), " une erreur est survenue réssayer plus tard", Toast.LENGTH_SHORT).show();
                            }


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

                                ODataEntitySet feed = (ODataEntitySet) resp.getPayload();

                                entities = feed.getEntities();

                                for (ODataEntity entity : entities) {
                                    properties = entity.getProperties();



                                    db.addmotif((String) properties.get("ZsdMotif").getValue(), (String) properties.get("ZsdDesignation").getValue());




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


                                    db.addmesure((String) properties1.get("ZsdMotif").getValue(), (String) properties1.get("ZsdDesignation").getValue(), (String) properties1.get("Stat").getValue());



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



                                    db.adddivision((String) properties2.get("Werks").getValue() + "  " + (String) properties2.get("Name1").getValue());


                                }




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



                                    db.addrelais((String) properties3.get("Pudoid").getValue() + "  " + (String) properties3.get("Designation").getValue());


                                }


                                if (entities.size() != 0 && entities1.size() != 0 && entities2.size() != 0 && entities3.size() != 0) {
                                    pDialog.dismiss();


                                }


                                Intent intent = new Intent(Motif_CL.this, AgencyListActivity1.class);
                                intent.putExtra("val", String.valueOf(val));
                                intent.putExtra("val1", String.valueOf(val1));
                                startActivity(intent);
                                finish();


                            } else {

                                Toast.makeText(getApplicationContext(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

                            }


                        }

                    });


                }


            });


        }




    }*/


}
