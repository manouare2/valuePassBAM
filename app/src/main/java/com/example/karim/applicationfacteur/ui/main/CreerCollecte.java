package com.example.karim.applicationfacteur.ui.main;

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
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.CollecteListActivity;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
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
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import android.view.ViewGroup.LayoutParams;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by karim on 04/07/2018.
 */

public class CreerCollecte extends MainCL {
    private SQLiteHandler db;
    Spinner spinner;
    Spinner spinner1;
    Spinner spinner2;

    Button btn;
    String raison;
    String raison2;
    List<String> items = new ArrayList<String>();
    List<String> items2 = new ArrayList<String>();
    List<String> items3 = new ArrayList<String>();
    List<String> items4 = new ArrayList<String>();
    SessionManager session;
    String val, val1, des;
    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> spinnerAdapter2;
    ArrayAdapter<String> spinnerAdapter3;
    ArrayAdapter<String> spinnerAdapter4;
    Context context = this;
    String ID_CL;
    ;
    TextView txt;

    String heure , client,minutes,num_client;
    static ArrayList<Collecte> CollecteList = new ArrayList<>();



    ProgressDialog pDialog;
    List<ODataEntity> entities, entities1, entities2, entities3;
    Context ctx = this;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        session = new SessionManager(getApplicationContext());
        login = session.getUsername();
        pwd = session.getPassword();

        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                FDR_notif();
            }

        };
        timer.schedule (hourlyTask, 0l, 1000*1*60);



        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.creer_collecte);


        db = new SQLiteHandler(this);

        Calendar cal = Calendar.getInstance();
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int minute = cal.get(Calendar.MINUTE);

        context = this;

        btn = (Button) findViewById(R.id.btn);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);


         final SQLiteHandler db = new SQLiteHandler(this);

        Cursor cr = db.getnomclient();

        if (cr.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "la liste des clients ne peut pas etre chargée, une erreur est survenue réessayer plus tard ", Toast.LENGTH_SHORT).show();
        } else {


            for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                        items.add(cr.getString(0));
            }


            spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    client = (String) adapterView.getItemAtPosition(i);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {


                                                      }
                                                  });


/////////////******recupérer l'heure systeme ***********///////



                    for (int j = 1; j < 24; j++) {

                        if (j >= hour) {
                            if (String.valueOf(j).length() == 1) {


                            } else {

                                items2.add(String.valueOf(j));
                            }

                        }

                    }

                    items2.add("00");

                    // Toast.makeText(getApplicationContext(),"cc"+cc.getCount(),Toast.LENGTH_SHORT).show();


                    spinnerAdapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items2);
                    spinner1.setAdapter(spinnerAdapter2);

                }

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                heure = (String) adapterView.getItemAtPosition(i);

                    if (heure.equalsIgnoreCase(String.valueOf(hour))) {
                        Log.e("kim","1");
                     items3.clear();

                        for (int j = 0; j < 60; j++) {

                            if (j >= minute) {
                                if (String.valueOf(j).length() == 1) {

                                    items3.add("0" + String.valueOf(j));

                                } else {

                                    items3.add(String.valueOf(j));
                                }

                            }

                        }


                    }



                else
                {

                    Log.e("kim","2");

                      items3.clear();

                    for (int j = 0 ; j < 60; j++) {


                        if (String.valueOf(j).length() == 1) {

                            items3.add("0"+String.valueOf(j));

                        } else {

                            items3.add(String.valueOf(j));
                        }



                    }

                }



                // Toast.makeText(getApplicationContext(),"cc"+cc.getCount(),Toast.LENGTH_SHORT).show();


                spinnerAdapter3 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items3);
                spinner2.setAdapter(spinnerAdapter3);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });







        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minutes = (String) adapterView.getItemAtPosition(i);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });






        btn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {

               if (registerInternetCheckReceiver(context)) {

                    Cursor cr = db.getnumclient(client);

                    for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                        num_client = cr.getString(0);
                    }

                                Cursor cursor = db.getobject_cl(session.getUsername());




                        if (cursor.getCount()== 0) {

                                Collecte(num_client, heure + minutes, "", view, ctx, client);

                        } else {


                            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                                ID_CL = cursor.getString(0);

                            }


                            ID_CL = ID_CL + "";


                            ID_CL = ID_CL.replace(" ", "");


                            Log.e("han", "test1");


                            cr = db.getFDRClient(session.getUsername(),num_client,ID_CL);
                            if(cr.getCount()==0) {

                               Collecte(num_client, heure + minutes, ID_CL, view, ctx, client);
                            }
                            else {
                                Toast.makeText(CreerCollecte.this, "ce client existe déja", Toast.LENGTH_SHORT).show();

                            }




                        }
                    }



                 else {



                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("ERROR");
                    builder.setMessage("Attention if faut être connecté à internet pour utiliser la fonctionalité");
                    builder.setCancelable(false);
                    builder.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }

                    });


                    AlertDialog alert1 = builder.create();


                    alert1.show();






                }



            }















        });

        }















    public static List<Collecte> getFDR(Context context) {

        return CollecteList;

    }



    private void Collecte(final String num_client, final String heure, final String ID_collecte, final View v, final Context context, final String nom_client ) {
      /*  mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mTries = 0;

        final OkHttpClient client = new OkHttpClient();
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
                            Toast.makeText(getApplicationContext(),"Vérifiez votre connexion internet ",Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();

*/

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
                            //url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                          //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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
                        }



                        if (store != null) {

                            ODataProperty property;
                            ODataPropMap properties;

                            ODataResponseSingle resp = null;
                            try {

                                resp = store.executeReadEntity("ZSD_FDR_COLLECTESet(Kunnr='" + num_client + "',ZheureEch='" + heure+ "',ZfdrId='" + ID_collecte+ "')", null);

                            } catch (ODataNetworkException e3) {
                                e3.printStackTrace();
                            } catch (ODataParserException e2) {
                                e2.printStackTrace();

                            } catch (ODataContractViolationException e3) {
                                   e3.printStackTrace();

                            }


                            if(resp==null)
                            {

                                Toast.makeText(getApplicationContext(),"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                ODataEntity feed = (ODataEntity) resp.getPayload();
                                //Get the list of ODataEntity

                                //Loop to retrieve the information from the response

                                properties = feed.getProperties();
                                Log.e("test", String.valueOf(properties.size()));

                                property = properties.get("Statut_CL");

                                if (property.getValue().equals("0"))


                                {
                                    Toast.makeText(getApplicationContext(),"Collecte inexistente", Toast.LENGTH_SHORT).show();


                                             String client = (String) properties.get("Kunnr").getValue();

                                             db.addData_FDR((String)properties.get("ZfdrId").getValue(),
                                            (String) properties.get("Zposnr").getValue(),
                                             client.replaceAll("^0+(?=.)", ""),(String) properties.get("Name").getValue(),
                                            (String) properties.get("ZnameCt").getValue(),
                                            (String) properties.get("AdrCl").getValue(),
                                            (String) properties.get("ZtelCt").getValue(),
                                            (String) properties.get("ZdateEch").getValue(),
                                            (String)(properties.get("ZheureEch").getValue()), session.getUsername(), "En cours", (String) properties.get("Agent").getValue(), (String) properties.get("Agence").getValue(), (String) properties.get("ZtypeCl").getValue(),"",(String) properties.get("Zcode2d").getValue(),"0","","");




                                    Cursor cr = db.getFDR(session.getUsername());

                             Log.e("size", String.valueOf(cr.getCount()));

                                    for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                        cl = new Collecte();
                                        cl.setNum_client(cr.getString(3));
                                        cl.setNum_fdr(cr.getString(1));
                                        cl.setNum_poste(cr.getString(2));
                                        cl.setNom_client(cr.getString(4));
                                        cl.setInterlocuteur(cr.getString(5));
                                        cl.setTelephone_client(cr.getString(6));
                                        cl.setAdresse_client(cr.getString(7));
                                        cl.setDate(cr.getString(8));
                                        cl.setStat(cr.getString(11));
                                        cl.setAgent(cr.getString(12));
                                        cl.setAgence(cr.getString(13));

                                        cl.setType_cl(cr.getString(14));


                                        CollecteList.add(cl);

                                      /*  SharedPreferences prefs = getSharedPreferences("ID_CL", Context.MODE_PRIVATE);
                                        prefs.edit().putString("ID",String.valueOf(cr.getString(1))).commit();*/

                                                 db.add_object_table(cr.getString(1),session.getUsername());

                                    }



                                    Intent intent = new Intent(getApplicationContext(), CollecteListActivity.class);
                                    //  intent.putExtra("code",String.valueOf(obj));
                                    startActivity(intent);
                                    finish();





                                }

                                    else {

                                    Toast.makeText(getApplicationContext(), "Collecte existe", Toast.LENGTH_SHORT).show();


                                    final AlertDialog.Builder inputAlert = new AlertDialog.Builder(context);

                                     inputAlert.setTitle("collecte existe déja");
                                    LinearLayout layout = new LinearLayout(ctx);
                                    layout.setOrientation(LinearLayout.VERTICAL);

                                    final EditText clientt = new EditText(ctx);
                                    clientt.setHint("client");
                                    clientt.setText((String)properties.get("Name").getValue());
                                    clientt.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_cl, 0, 0, 0);
                                    layout.addView(clientt);

                                    final EditText Interlocuteur  = new EditText(ctx);
                                    Interlocuteur.setHint("Interlocuteur");
                                    Interlocuteur.setText((String)properties.get("ZnameCt").getValue());
                                    Interlocuteur.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_action_per, 0, 0, 0);
                                    layout.addView(Interlocuteur);


                                    final EditText Telephone  = new EditText(ctx);
                                    Telephone.setHint("Telephone");
                                    Telephone.setText((String)properties.get("ZtelCt").getValue());
                                    Telephone.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_cal, 0, 0, 0);
                                    layout.addView(Telephone);




                                    final EditText adresse  = new EditText(ctx);
                                    adresse.setHint("Adresse");
                                    adresse.setText((String)properties.get("AdrCl").getValue());
                                    adresse.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_map, 0, 0, 0);
                                    layout.addView(adresse);






                                    inputAlert.setView(layout);

                                  /*  inputAlert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String userInputValue = userInput.getText().toString();
                                            new Livraison_entrep.SendPostRequest(userInputValue,envoi).execute();
                                        }
                                    });
                                    inputAlert.setNegativeButton("Anuuler", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });*/
                                    AlertDialog alertDialog = inputAlert.create();
                                    alertDialog.show();






                                }

                            }
                            //Get the response payload


                        }
                        else
                        {

                            Toast.makeText(getApplicationContext(),"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();
                        }

    }




    }





