package com.example.karim.applicationfacteur.ui.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.CollecteListActivity;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.sap.maf.tools.logon.core.LogonCoreContext;
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
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Acceuil1 extends MainCL {

    SessionManager session;
    EditText txt1,txt2;
    Button btn,btn2;
    String txtS;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    SQLiteHandler db ;
    int size;
    List<ODataEntity> entities;
    Agency3 agc;
    static ArrayList<Agency3> agencyList = new ArrayList<>();
    static ArrayList<String> agencyList1 = new ArrayList<>();

    String currentDateTimeString;
    String adresse;
    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    private boolean netSpeed;
    String activity = this.getClass().getSimpleName();
    String prevStatus = "";
    static String login;
    static String pwd;
    static LogonCoreContext lgtx;
    Cursor cursor;
    ArrayList<String> list = new ArrayList<String>();
    protected ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    protected ConnectionClassManager mConnectionClassManager;
    protected DeviceBandwidthSampler mDeviceBandwidthSampler;
    protected int mTries;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceuil1);

        this.inflateViews();
        this.initListeners();
        session = new SessionManager(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);



        currentDateTimeString = DateFormat.getDateInstance().format(new Date());

        txt1.setText("Date tournée: "+currentDateTimeString + "(V6)");
        txt1.setFocusable(false);

        final SQLiteHandler db = new SQLiteHandler(this);

        synchro();

/*envois();

        SharedPreferences sp = getSharedPreferences("your_prefs",MODE_PRIVATE);
        final int siz = sp.getInt("your_int_key", 0);
       // sp.edit().remove("text").commit();


        txt2.setText("Envois : " + siz );
        txt2.setFocusable(false);*/

        //  Toast.makeText(getApplicationContext(),session.getUsername(),Toast.LENGTH_LONG).show();


    }

    private void initListeners() {
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                btn.setTextColor(Color.WHITE);

                Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);
                //  intent.putExtra("code",String.valueOf(obj));
                startActivity(intent);
                finish();



              /*  Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);

                startActivity(intent);
                finish();
*/


            /*    db = new SQLiteHandler(getApplicationContext());

                Cursor cursor1 = db.getAll();
   if(cursor1.getCount()==0)
   {
       envois();
   }

   else
   {



       TextView myMsg = new TextView(getApplicationContext());
       myMsg.setText("la dérniere tournée n'est pas cloturer");
       myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
       myMsg.setTextSize(15);
       myMsg.setTextColor(Color.WHITE);

       AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
       builder.setTitle(myMsg.getText().toString());
       builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

               db = new SQLiteHandler(getApplicationContext());

               Cursor cursor2 = db.getAll();


               for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {

                   agc = new Agency3(cursor2.getString(1));
                   agc.setNom_client(cursor2.getString(2));



                   agc.setTelephone_client(cursor2.getString(3));
                   agc.setAdresse_client(cursor2.getString(4));
                   agc.setCrbt(cursor2.getString(5));
                   agc.setPod(cursor2.getString(9));
                   agc.setService(cursor2.getString(10));
                   agc.setDesignation(cursor2.getString(11));
                   agc.setAdr_relais(cursor2.getString(17));
                   agc.setRelais(cursor2.getString(18));
                   agc.setAgc(cursor2.getString(19));
                   agc.setAdr_agc(cursor2.getString(20));
                   agencyList.add(agc);


               }

               Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);
               //  intent.putExtra("code",String.valueOf(obj));
               startActivity(intent);
               finish();


           }




       });
       builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();

           }
       });


       AlertDialog alert1 = builder.create();
       alert1.show();
   }





*/



                ///////////////////////////////// traitm  /////////////////////////////////////
/*
                db = new SQLiteHandler(getApplicationContext());

                Cursor cursor2 = db.getAllDNL(currentDateTimeString);
                Cursor cr = db.getAll();




               if (cursor2.getCount() == 0 && cr.getCount() != 0) {

                    TextView myMsg = new TextView(view.getRootView().getContext());
                    myMsg.setText("Attention");
                    myMsg.setGravity(Gravity.NO_GRAVITY);

                    myMsg.setTextColor(Color.WHITE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle(myMsg.getText().toString());
                    builder.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                    builder.setMessage("la dérniere DNL n'est pas cloturé");


                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            Cursor cursor1 = db.getAllCondition();




                            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                agc = new Agency3(cursor1.getString(1));
                                agc.setNom_client(cursor1.getString(2));


                                agc.setTelephone_client(cursor1.getString(3));
                                agc.setAdresse_client(cursor1.getString(4));
                                agc.setCrbt(cursor1.getString(5));
                                agc.setPod(cursor1.getString(9));
                                agc.setService(cursor1.getString(10));
                                agc.setDesignation(cursor1.getString(11));
                                agc.setAdr_relais(cursor1.getString(17));
                                agc.setRelais(cursor1.getString(18));
                                agc.setAgc(cursor1.getString(19));
                                agc.setAdr_agc(cursor1.getString(20));
                                agc.setObj1(cursor1.getString(21));

                                if (agencyList.size() == 0) {


                                    Log.e("han", "1");
                                    agc.setObj(cursor1.getString(21));


                                    agencyList.add(agc);
                                    agencyList1.add(agc.getObj());


                                } else {


                                    if (agencyList1.contains(cursor1.getString(21))) {

                                        agc.setObj("");

                                    } else {

                                        agc.setObj(cursor1.getString(21));

                                    }


                                    agencyList.add(agc);
                                    agencyList1.add(agc.getObj());


                                }


                            }
                            Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);
                            //  intent.putExtra("code",String.valueOf(obj));
                            startActivity(intent);
                            finish();


                        }
                    });
                    builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });


                    AlertDialog alert1 = builder.create();

                    alert1.show();





                }




              else{


                       envois();




                }
*/





            }


        });



        btn2.setOnClickListener(new View.OnClickListener() {



            public void onClick(View view) {



                btn2.setTextColor(Color.WHITE);



                Cursor cr = db.getnomclient();


                if(cr.getCount()==0)

                {

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Client_collecte();
                        }
                    });

                }




                Intent intent = new Intent(Acceuil1.this, CollecteListActivity.class);
                //  intent.putExtra("code",String.valueOf(obj));
                startActivity(intent);
                finish();





              /*  btn2.setTextColor(Color.WHITE);

                    Intent intent = new Intent(Acceuil1.this,AgencyListActivity2.class);

                    startActivity(intent);
                    finish();*/

            }


        });

    }


    private void inflateViews() {
        btn= (Button) findViewById(R.id.btn);
        btn2= (Button) findViewById(R.id.btn2);
        txt1= (EditText) findViewById(R.id.txt1);
        txt2= (EditText) findViewById(R.id.txt2);
    }

    /*@Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

            new AlertDialog.Builder(context)
                    .setTitle("Déconnexion")
                    .setMessage("Voulez vous vraiment quitter l'application ?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getApplicationContext(), Deconnexion.class);
                                startActivity(intent);
                                session = new SessionManager(getApplicationContext());
                                session.clearSession();
                                db = new SQLiteHandler(getApplicationContext());
                                db.deletediv();
                                restart(getApplicationContext(), 0);}
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            // finishAffinity();
        }

        return true;
        // Disable back button..............
    }

    private void DNLIntent() {
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
                            Toast.makeText(getApplicationContext(),"connectez vous à internet puis réssayer",Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);

                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
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
                            Toast.makeText(getApplicationContext(),"Vérifiez votre connexion internet",Toast.LENGTH_SHORT).show();

                            db = new SQLiteHandler(getApplicationContext());


                            Cursor cr = db.getAll();



                            if(cr.getCount()!=0)

                             {


                                for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                    agc = new Agency3(cr.getString(1));
                                    agc.setNom_client(cr.getString(2));
                                    agc.setTelephone_client(cr.getString(3));
                                    agc.setAdresse_client(cr.getString(4));
                                    agc.setCrbt(cr.getString(5));
                                    agc.setPod(cr.getString(9));
                                    agc.setService(cr.getString(10));
                                    agc.setDesignation(cr.getString(11));
                                    agc.setAdr_relais(cr.getString(17));
                                    agc.setRelais(cr.getString(18));
                                    agc.setAgc(cr.getString(19));
                                    agc.setAdr_agc(cr.getString(20));
                                    agc.setObj1(cr.getString(21));

                                    if (agencyList.size() == 0) {


                                        Log.e("han", "1");
                                        agc.setObj(cr.getString(21));


                                        agencyList.add(agc);
                                        agencyList1.add(agc.getObj());


                                    } else {


                                        if (agencyList1.contains(cr.getString(21))) {

                                            agc.setObj("");

                                        } else {

                                            agc.setObj(cr.getString(21));

                                        }


                                        agencyList.add(agc);
                                        agencyList1.add(agc.getObj());


                                    }


                                }

                            }
                            Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);
                            //  intent.putExtra("code",String.valueOf(obj));
                            startActivity(intent);
                            finish();



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
                           //  url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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

                        // ArrayList<Agency> agencyList = new ArrayList<Agency>();

	/*	AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();*/

                        if (store != null) {

                            ODataProperty property;
                            ODataPropMap properties;





                            ODataResponseSingle resp = null;
                            try {
                                resp = store.executeReadEntitySet(
                                        "DNLENVOISet", null);
                            } catch (ODataNetworkException e) {
                                e.printStackTrace();
                            } catch (ODataParserException e) {
                                e.printStackTrace();


                            } catch (ODataContractViolationException e) {
                                e.printStackTrace();

                            }
                            //Get the response payload


                            //Get the response payload


                            if(resp==null)
                            {
                                Toast.makeText(getApplicationContext()," Vous ne pouvez pas charger DNL ,une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);
                                //  intent.putExtra("code",String.valueOf(obj));
                                startActivity(intent);
                                finish();




                            }

                            else
                            {



                                ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                                //Get the list of ODataEntity

                                entities = feed.getEntities();

                                if(entities.size()==0)
                                {
                                    Toast.makeText(getApplicationContext(),"aucune DNL à charger",Toast.LENGTH_SHORT).show();


                                    Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);
                                    //  intent.putExtra("code",String.valueOf(obj));
                                    startActivity(intent);
                                    finish();
                                }

                                for (ODataEntity entity : entities) {

                                    properties = entity.getProperties();

                                    db = new SQLiteHandler(getApplicationContext());


                                    Log.e("kim","1");



                                    String designation = (String) properties.get("Designation").getValue();
                                    if(designation.equalsIgnoreCase("POD"))
                                    {
                                        adresse=(String) properties.get("ADRESSE").getValue();
                                    }
                                    else
                                    {
                                        adresse= (String) properties.get("Quartier").getValue()+", "+(String) properties.get("Rue").getValue()+", "+(String) properties.get("point_remise").getValue()+", "+(String) properties.get("point_geo").getValue();

                                    }

                                    db.addData((String) properties.get("code_envoi").getValue(),
                                            (String) properties.get("Client").getValue(),
                                            (String) properties.get("Telephone").getValue(),
                                           adresse,
                                            (String) properties.get("Montant").getValue(),
                                            "En cours","DNL",session.getUsername(),(String) properties.get("POD").getValue(),(String) properties.get("Service").getValue(),(String) properties.get("Designation").getValue(), (String) properties.get("Mode_liv").getValue(),(String) properties.get("Rue").getValue(),(String) properties.get("Code_postal").getValue(),"0",currentDateTimeString, (String) properties.get("RUE_RELAIS").getValue()+", "+(String) properties.get("NUM_RUE_RELAIS").getValue()+", "+(String) properties.get("LOCALITE_RELAIS").getValue()+", "+(String) properties.get("CODE_POSTAL_RELAIS").getValue(),(String) properties.get("NOM_RELAIS").getValue(),(String) properties.get("AGENCE").getValue(),   (String) properties.get("RUE_AGENCE").getValue()+", "+(String) properties.get("CODE_POSTAL_AGENCE").getValue()+", "+(String) properties.get("LOCALITE_AGENCE").getValue(),(String) properties.get("Num_obj_dnl").getValue(),(String) properties.get("TELEPHONE_EXP").getValue(),(String) properties.get("CODE_ENVOI_PERE").getValue());


                                    Log.e("kim","2");

                                    Cursor cursor1 = db.getAllCondition();




                                    for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                        agc = new Agency3(cursor1.getString(1));
                                        agc.setNom_client(cursor1.getString(2));



                                        agc.setTelephone_client(cursor1.getString(3));
                                        agc.setAdresse_client(cursor1.getString(4));
                                        agc.setCrbt(cursor1.getString(5));
                                        agc.setPod(cursor1.getString(9));
                                        agc.setService(cursor1.getString(10));
                                        agc.setDesignation(cursor1.getString(11));
                                        agc.setAdr_relais(cursor1.getString(17));
                                        agc.setRelais(cursor1.getString(18));
                                        agc.setAgc(cursor1.getString(19));
                                        agc.setAdr_agc(cursor1.getString(20));
                                        agc.setObj1(cursor1.getString(21));
                                        agc.setTelephone_exp(cursor1.getString(22));


                                        if(agencyList.size()==0)
                                        {



                                            Log.e("han","1");
                                            agc.setObj(cursor1.getString(21));


                                            agencyList.add(agc);
                                            agencyList1.add(agc.getObj());





                                        }
                                        else {


                                            if(agencyList1.contains(cursor1.getString(21)))
                                            {

                                                agc.setObj("");

                                            }
                                            else{

                                                agc.setObj(cursor1.getString(21));

                                            }


                                            agencyList.add(agc);
                                            agencyList1.add(agc.getObj());


                                        }



                                    }

                              /*  String obj = (String) properties.get("Num_obj_dnl").getValue();
                                SharedPreferences prefs = getSharedPreferences("objj", Context.MODE_PRIVATE);
                                prefs.edit().putString("obj",String.valueOf(obj)).commit();*/


                                    Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);
                                    //  intent.putExtra("code",String.valueOf(obj));
                                    startActivity(intent);
                                    finish();


                             /*   size =    entities.size()
                                ;
                                SharedPreferences sp = getSharedPreferences("your_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("your_int_key", size);
                                editor.commit();/*/



                                }






                            }





                        }

                      else{
                            Toast.makeText(getApplicationContext()," Vous ne pouvez pas charger DNL ,une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Acceuil1.this, AgencyListActivity1.class);
                            //  intent.putExtra("code",String.valueOf(obj));
                            startActivity(intent);
                            finish();
                        }

                    }

                });

            }



        });

    }

    public static List<Agency3> getAgencies1(Context context)  {

        return agencyList;

    }
/********************************************synchonisation BTR-32******************************************/

private void synchro() {

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
                        Toast.makeText(getApplicationContext(), "Vérifiez votre connexion internet", Toast.LENGTH_SHORT).show();

                        db = new SQLiteHandler(getApplicationContext());


                        Cursor cr = db.getAll();


                        if (cr.getCount() != 0)

                        {


                            for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                agc = new Agency3(cr.getString(1));
                                agc.setNom_client(cr.getString(2));
                                agc.setTelephone_client(cr.getString(3));
                                agc.setAdresse_client(cr.getString(4));
                                agc.setCrbt(cr.getString(5));
                                agc.setPod(cr.getString(9));
                                agc.setService(cr.getString(10));
                                agc.setDesignation(cr.getString(11));
                                agc.setAdr_relais(cr.getString(17));
                                agc.setRelais(cr.getString(18));
                                agc.setAgc(cr.getString(19));
                                agc.setAdr_agc(cr.getString(20));
                                agc.setObj1(cr.getString(21));

                                if (agencyList.size() == 0) {


                                    Log.e("han", "1");
                                    agc.setObj(cr.getString(21));

                                    if (!agencyList.contains(agc)) {
                                        agencyList.add(agc);

                                    }



                                } else {


                                    if (agencyList1.contains(cr.getString(21))) {

                                        agc.setObj("");

                                    } else {

                                        agc.setObj(cr.getString(21));

                                    }

                                    if (!agencyList.contains(agc)) {
                                        agencyList.add(agc);
                                        agencyList1.add(agc.getObj());
                                    }




                                }


                            }

                        }


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
                    Toast.makeText(getApplicationContext(), "Connecté", Toast.LENGTH_SHORT).show();

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
                        //url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();

                    }
                    //Method to open a new online store asynchronously


                    //Check if OnlineODataStore opened successfully
                    OnlineODataStore store = null;

                    try {
                        store = OnlineODataStore.open(getApplication(), url, manager, null);
                    } catch (ODataException e) {
                        e.printStackTrace();


                    }
                    db = new SQLiteHandler(getApplicationContext());
                    db.deleteData();
                    // ArrayList<Agency> agencyList = new ArrayList<Agency>();

	/*	AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();*/

                    if (store != null) {

                        ODataProperty property;
                        ODataPropMap properties;


                        ODataResponseSingle resp = null;
                        try {
                            resp = store.executeReadEntitySet(
                                    "DNLSYNSet", null);
                        } catch (ODataNetworkException e) {
                            e.printStackTrace();
                        } catch (ODataParserException e) {
                            e.printStackTrace();


                        } catch (ODataContractViolationException e) {
                            e.printStackTrace();

                        }
                        //Get the response payload


                        //Get the response payload


                        if (resp == null) {
                           /* Toast.makeText(getApplicationContext(), " Vous ne pouvez pas charger DNL ,une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplication(), AgencyListActivity1.class);
                            //  intent.putExtra("code",String.valueOf(obj));
                            startActivity(intent);
                            finish();*/


                        } else {
                            ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                            //Get the list of ODataEntity

                            entities = feed.getEntities();

                            if (entities.size() == 0) {
                                //Toast.makeText(getApplicationContext(), "aucune DNL à charger", Toast.LENGTH_SHORT).show();


                               /*     Intent intent = new Intent(getApplication(), AgencyListActivity1.class);
                                    //  intent.putExtra("code",String.valueOf(obj));
                                    startActivity(intent);
                                    finish();*/
                                db = new SQLiteHandler(getApplicationContext());

                                db.deleteData();
                            }
else{
                            for (ODataEntity entity : entities) {

                                properties = entity.getProperties();

                                db = new SQLiteHandler(getApplicationContext());


                                Log.e("kim", "1");


                                String designation = (String) properties.get("Designation").getValue();
                                if (designation.equalsIgnoreCase("POD")) {
                                    adresse = (String) properties.get("ADRESSE").getValue();
                                } else {
                                    adresse = (String) properties.get("Quartier").getValue() + ", " + (String) properties.get("Rue").getValue() + ", " + (String) properties.get("point_remise").getValue() + ", " + (String) properties.get("point_geo").getValue();

                                }
                                db.addData((String) properties.get("code_envoi").getValue(),
                                        (String) properties.get("Client").getValue(),
                                        (String) properties.get("Telephone").getValue(),
                                        adresse,
                                        (String) properties.get("Montant").getValue(),
                                        (String) properties.get("STATUT").getValue(), "DNL",
                                        session.getUsername(), (String) properties.get("POD").getValue(),
                                        (String) properties.get("Service").getValue(),
                                        (String) properties.get("Designation").getValue(),
                                        (String) properties.get("Mode_liv").getValue(), (String) properties.get("Rue").getValue(), (String) properties.get("Code_postal").getValue(), "0",
                                        currentDateTimeString,
                                        (String) properties.get("RUE_RELAIS").getValue() + ", " + (String) properties.get("NUM_RUE_RELAIS").getValue() + ", " + (String) properties.get("LOCALITE_RELAIS").getValue() + ", " + (String) properties.get("CODE_POSTAL_RELAIS").getValue(), (String) properties.get("NOM_RELAIS").getValue(), (String) properties.get("AGENCE").getValue(), (String) properties.get("RUE_AGENCE").getValue() + ", " + (String) properties.get("CODE_POSTAL_AGENCE").getValue() + ", " + (String) properties.get("LOCALITE_AGENCE").getValue(), (String) properties.get("Num_obj_dnl").getValue(), (String) properties.get("TELEPHONE_EXP").getValue(), (String) properties.get("CODE_ENVOI_PERE").getValue());

                                db.updateDataglobal((String) properties.get("code_envoi").getValue(), (String) properties.get("STATUT").getValue());
                                Log.e("kim", "2");



                                Cursor cursor1 = db.getAllCondition();


                                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                    agc = new Agency3(cursor1.getString(1));
                                    agc.setNom_client(cursor1.getString(2));


                                    agc.setTelephone_client(cursor1.getString(3));
                                    agc.setAdresse_client(cursor1.getString(4));
                                    agc.setCrbt(cursor1.getString(5));
                                    agc.setPod(cursor1.getString(9));
                                    agc.setService(cursor1.getString(10));
                                    agc.setDesignation(cursor1.getString(11));
                                    agc.setAdr_relais(cursor1.getString(17));
                                    agc.setRelais(cursor1.getString(18));
                                    agc.setAgc(cursor1.getString(19));
                                    agc.setAdr_agc(cursor1.getString(20));
                                    agc.setObj1(cursor1.getString(21));
                                    agc.setTelephone_exp(cursor1.getString(22));


                                    if (agencyList.size() == 0) {


                                        Log.e("han", "1");
                                        agc.setObj(cursor1.getString(21));

                                        if (!agencyList.contains(agc)) {
                                            agencyList.add(agc);
                                            agencyList1.add(agc.getObj());
                                        }


                                    } else {


                                        if (agencyList1.contains(cursor1.getString(21))) {

                                            agc.setObj("");

                                        } else {

                                            agc.setObj(cursor1.getString(21));

                                        }

                                        if (!agencyList.contains(agc)) {
                                            agencyList.add(agc);
                                            agencyList1.add(agc.getObj());
                                        }


                                    }


                                }

                              /*  String obj = (String) properties.get("Num_obj_dnl").getValue();
                                SharedPreferences prefs = getSharedPreferences("objj", Context.MODE_PRIVATE);
                                prefs.edit().putString("obj",String.valueOf(obj)).commit();*/


                              /*  Intent intent = new Intent(getApplication(), AgencyListActivity1.class);
                                //  intent.putExtra("code",String.valueOf(obj));
                                startActivity(intent);
                                finish();*/


                             /*   size =    entities.size()
                                ;
                                SharedPreferences sp = getSharedPreferences("your_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("your_int_key", size);
                                editor.commit();/*/


                            }


                        }
                    }

                        } else{
                      /*  Toast.makeText(getApplicationContext(), " Vous ne pouvez pas charger DNL ,une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplication(), AgencyListActivity1.class);
                        //  intent.putExtra("code",String.valueOf(obj));
                        startActivity(intent);
                        finish();*/
                        }


                }

            });

        }


    });

}










}

