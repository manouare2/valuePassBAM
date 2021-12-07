package com.example.karim.applicationfacteur.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
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
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by karim on 04/07/2018.
 */

public class Raison1 extends myActivity {
    private SQLiteHandler db;
    Spinner spinner;
    Spinner spinner2;
    Button btn;
    String raison;
    String raison2;
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
    Context context;
    List<ODataEntity> entities ,entities1;
    String motif,designation,motiff,mesure,stat;
    EditText code;
    String code_barre;
    String div,relais;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.raison1);

        context = this;

        btn = (Button) findViewById(R.id.btn);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        spinner2.setBackgroundColor(0x000);
        db= new SQLiteHandler(this);

        Bundle bundle = getIntent().getExtras();
//        final String  client= bundle.getString("client");
        final String  mode_liv = bundle.getString("mode_liv");
        designation = bundle.getString("designation");
        code_barre = bundle.getString("code");
        final String  client= bundle.getString("client");


        Cursor cr =  db.getAllmotif();

        if(cr.getCount()==0)
        {
            Toast.makeText(getApplicationContext(),"une erreur est survenue réessayer plus tard ",Toast.LENGTH_SHORT).show();
        }



        else {

            for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                items.add(cr.getString(2));
            }


            spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    raison = (String) adapterView.getItemAtPosition(i);


                    items2.clear();

                    Cursor cc = db.getmotif(raison);


                    for (cc.moveToFirst(); !cc.isAfterLast(); cc.moveToNext()) {

                        Cursor cc1 = db.getmesure(cc.getString(1));
                        for (cc1.moveToFirst(); !cc1.isAfterLast(); cc1.moveToNext()) {


                            //  Toast.makeText(getApplicationContext(),cc1.getString(2)+"mesure",Toast.LENGTH_SHORT).show();


                            items2.add(cc1.getString(2));


                        }


                    }


                    // Toast.makeText(getApplicationContext(),"cc"+cc.getCount(),Toast.LENGTH_SHORT).show();


                    spinnerAdapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items2);
                    spinner2.setAdapter(spinnerAdapter2);


                    // Toast.makeText(getApplicationContext(), raison, Toast.LENGTH_SHORT).show();
                    // items2 = new String[]{raison + " 1",raison + " 2",raison + " 3"};

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    raison2 = (String) adapterView.getItemAtPosition(i);


                    if (raison2 == null) {


                    } else {

                        final Spinner spinner3 = new Spinner(context);
                        final TextView tv = new TextView(context);
                        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout1);
                        final LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.layout2);
                        final Spinner spinner4 = new Spinner(context);
                        final TextView tv1 = new TextView(context);


                        if (raison != null) {
                            if (raison2.equalsIgnoreCase("OBJET RETENU,LE DESTINATAIRE AVISE") && raison.equalsIgnoreCase("ADRESSE INCORRECTE")) {

                               /* final RadioGroup rg = new RadioGroup(getApplicationContext());
                                rg.setOrientation(RadioGroup.HORIZONTAL);
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                final RadioButton rb_coldfusion = new RadioButton(getApplicationContext());
                                rb_coldfusion.setText("Agence Réception");
                                rb_coldfusion.setTextColor(Color.parseColor("#303F9F"));
                                rb_coldfusion.setTypeface(null, Typeface.BOLD);
                                rb_coldfusion.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#ffdd00")));
                                rg.addView(rb_coldfusion);

                                // Create another Radio Button for RadioGroup
                                RadioButton rb_flex = new RadioButton(getApplicationContext());
                                rb_flex.setText("Point Relais");;
                                rb_flex.setTextColor(Color.parseColor("#303F9F"));
                                rb_flex.setTypeface(null, Typeface.BOLD);
                                rb_flex.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#ffdd00")));
                                rg.addView(rb_flex);

                                // Create another Radio Button for RadioGroup

                                // Finally, add the RadioGroup to main layout
                                linearLayout.addView(rg);




                                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                                        View radioButton = rg.findViewById(checkedId);
                                        int index = rg.indexOfChild(radioButton);

                                        // Add logic here

                                        switch (index) {
                                            case 0: // first button

                                                for (int m = 0; m < linearLayout.getChildCount(); m++) {
                                                    if (linearLayout1.getChildAt(m) instanceof Spinner) {
                                                        linearLayout1.removeView(linearLayout1.getChildAt(m));
                                                    }


                                                }

                                                relais="";


                                              //  tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                spinner3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                                //linearLayout.addView(tv);





                                                linearLayout1.addView(spinner3);

                                                Cursor ccd = db.getAlldiv();


                                                for (ccd.moveToFirst(); !ccd.isAfterLast(); ccd.moveToNext()) {


                                                    items3.add(ccd.getString(1));


                                                }


                                                spinnerAdapter3 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items3);
                                                spinner3.setAdapter(spinnerAdapter3);


                                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        div = (String) adapterView.getItemAtPosition(i);


                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                    }


                                                });



                                                break;
                                            case 1: // secondbutton

                                                for (int m = 0; m < linearLayout.getChildCount(); m++) {
                                                    if (linearLayout1.getChildAt(m) instanceof Spinner) {
                                                        linearLayout1.removeView(linearLayout1.getChildAt(m));
                                                    }


                                                }



                                                div ="";
                                                spinner4.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                                linearLayout1.addView(spinner4);



/////////////////////////////////////////////////////////////////////////////////////////


                                                Cursor ccr = db.getAllrelais();


                                                for (ccr.moveToFirst(); !ccr.isAfterLast(); ccr.moveToNext()) {


                                                    items4.add(ccr.getString(1));


                                                    Log.e("relais",ccr.getString(1));


                                                }


                                                spinnerAdapter4 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items4);
                                                spinner4.setAdapter(spinnerAdapter4);


                                                spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        relais = (String) adapterView.getItemAtPosition(i);

                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                    }


                                                });
                                                break;
                                        }
                                    }
                                });










*/

                            } else {
                                for (int x = 0; x < linearLayout.getChildCount(); x++) {
                                    if (linearLayout.getChildAt(x) instanceof TextView) {
                                        linearLayout.removeView(linearLayout.getChildAt(x));
                                    }
                                }

                                for (int m = 0; m < linearLayout.getChildCount(); m++) {
                                    if (linearLayout.getChildAt(m) instanceof Spinner) {
                                        linearLayout.removeView(linearLayout.getChildAt(m));
                                    }


                                }


                               /* for (int a = 0; a < linearLayout1.getChildCount(); a++) {
                                    if (linearLayout1.getChildAt(a) instanceof TextView) {
                                        linearLayout1.removeView(linearLayout1.getChildAt(a));
                                    }
                                }

                                for (int y = 0; y < linearLayout1.getChildCount(); y++) {
                                    if (linearLayout1.getChildAt(y) instanceof Spinner) {
                                        linearLayout1.removeView(linearLayout1.getChildAt(y));
                                    }


                                }*/


                            }
                        }
                    }


                }


                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }


            });

        }
       /* Bundle bundle = getIntent().getExtras();

        //Extract the data…
        val = bundle.getString("val");

        val1 = bundle.getString("val1");*/

        // SQLite database handler

        btn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                if(raison2!=null && raison!=null)


                {


                    if (raison2.equalsIgnoreCase("OBJET RETENU,LE DESTINATAIRE AVISE") && raison.equalsIgnoreCase("ADRESSE INCORRECTE"))

                    {
                      /*  final ArrayList<Agency3> myList1 = (ArrayList<Agency3>) getIntent().getSerializableExtra("listagc");


                        if (registerInternetCheckReceiver(context)) {


                            Cursor cc2 = db.getSatmesure(raison2);
                            for (cc2.moveToFirst(); !cc2.isAfterLast(); cc2.moveToNext()) {


                                stat = cc2.getString(3);
                            }


                            if (myList1.size() == 0) {


                            } else {

                                InsertBatch(myList1, stat, raison, raison2, client, "", div, relais);
                                Intent intent = new Intent(Raison1.this, AgencyListActivity1.class);
                                intent.putExtra("val", String.valueOf(val));
                                intent.putExtra("val1", String.valueOf(val1));
                                startActivity(intent);
                                finish();


                            }


                            // InsertBatch(code_barre, "AFFECTE GUICHET", "affg", "ZSD_STAT", "E0006", "E0005", raison);
                            //  InsertBatch(code_barre,raison,raison2,"",mode_liv,"","","");
                        } else {
                            db = new SQLiteHandler(getApplicationContext());


                            for(int i = 0 ; i <myList1.size();i++)
                            {
                                if (myList1.get(i).getDesignation().equalsIgnoreCase("DOC POD")) {
                                    des = "ZSD_SPOD";

                                } else {
                                    des = "ZSD_STAT";

                                }

                                Cursor cc2 = db.getSatmesure(raison2);
                                for (cc2.moveToFirst(); !cc2.isAfterLast(); cc2.moveToNext()) {

                                    stat = cc2.getString(3);


                                }


                                db.addData3(myList1.get(i).getCode_envoi(),session.getUsername(), "Non livré", "non liv", des, "E0006", stat, raison, raison2, "", mode_liv, "", "", "", "", "");

                                db.updateDataglobal(myList1.get(i).getCode_envoi(), "Non livré");
                                Intent intent = new Intent(Raison1.this, AgencyListActivity1.class);
                                intent.putExtra("val", String.valueOf(val));
                                intent.putExtra("val1", String.valueOf(val1));
                                startActivity(intent);
                                finish();

                            }







                        }
                        // Toast.makeText(getApplicationContext(), "Raison est ajoutée", Toast.LENGTH_SHORT).show();


                    */} else {

                        final ArrayList<Agency3> myList1 = (ArrayList<Agency3>) getIntent().getSerializableExtra("listagc");


                        if (registerInternetCheckReceiver(context)) {


                            Cursor cc2 = db.getSatmesure(raison2);
                            for (cc2.moveToFirst(); !cc2.isAfterLast(); cc2.moveToNext()) {


                                stat = cc2.getString(3);
                            }

                            if (myList1.size() == 0) {


                            } else {

                                InsertBatch(myList1, stat, raison, raison2, client, "", div, relais);
                                Intent intent = new Intent(Raison1.this, AgencyListActivity1.class);
                                intent.putExtra("val", String.valueOf(val));
                                intent.putExtra("val1", String.valueOf(val1));
                                startActivity(intent);
                                finish();


                            }


                            // InsertBatch(code_barre, "AFFECTE GUICHET", "affg", "ZSD_STAT", "E0006", "E0005", raison);
                            //  InsertBatch(code_barre,raison,raison2,"",mode_liv,"","","");
                        } else {
                            for(int i = 0 ; i <myList1.size();i++)
                            {
                                if (myList1.get(i).getDesignation().equalsIgnoreCase("DOC POD")) {
                                    des = "ZSD_SPOD";

                                } else {
                                    des = "ZSD_STAT";

                                }

                                Cursor cc2 = db.getSatmesure(raison2);
                                for (cc2.moveToFirst(); !cc2.isAfterLast(); cc2.moveToNext()) {

                                    stat = cc2.getString(3);


                                }


                                db.addData3(myList1.get(i).getCode_envoi(),session.getUsername(), "Non livré", "non liv", des, "E0006", stat, raison, raison2, "", mode_liv, "", "", "", "", "");

                                db.updateDataglobal(myList1.get(i).getCode_envoi(), "Non livré");
                                Intent intent = new Intent(Raison1.this, AgencyListActivity1.class);
                                intent.putExtra("val", String.valueOf(val));
                                intent.putExtra("val1", String.valueOf(val1));
                                startActivity(intent);
                                finish();

                            }



                        }
                        // Toast.makeText(getApplicationContext(), "Raison est ajoutée", Toast.LENGTH_SHORT).show();



                    }

                }


                else
                {

                    Toast.makeText(getApplicationContext(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

                }




            }


        });


        // Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String code_barre = bundle.getString("val");


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Raison1.this, AgencyListActivity1.class);
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

    public void InsertBatch(List<Agency3> list,String stat,String motif,String mesure,String destinataire,String mode_liv,String div,String relais)

    {

        /*db = new SQLiteHandler(getApplicationContext());

        Cursor cursor = db.getAll2();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            login = cursor.getString(1);
            pwd = cursor.getString(2);

        }
        */
        session = new SessionManager(this);
        login = session.getUsername();
        pwd = session.getPassword();
        Agency1 agen = null;
        ODataResponse oDataResponse = null;

        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx, login, pwd);

        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                getApplication()).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(getApplication()));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getApplicationContext(), requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
            url = new URL(url_g);
          //  url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
         //   url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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


        if (store != null) {


            ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
            ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.Affectation");

            ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

            batchItem.setResourcePath("AffectationSet");


            batchItem.setMode(ODataRequestParamSingle.Mode.Create);

            batchItem.setCustomTag("something to identify the request");

      /*  db = new SQLiteHandler(this);

        cursor = db.getAll3();*/

       /* if ( == 0) {

            newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", code_envoi));
            newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
            newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Livraison"));
            newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "liv"));


            newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));
            newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

            newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat","E0010"));
            newEntity.getProperties().put("Raison", new ODataPropertyDefaultImpl("Raison",raison));


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
                                Toast.makeText(getApplicationContext(), "Les envois sont envoyés avec succée", Toast.LENGTH_SHORT).show();
                                db.updateDataglobal(code_envoi,"livré");

                                db.deleteData3();

                            } else {
                                Toast.makeText(getApplicationContext(), "une Erreur est survenue ", Toast.LENGTH_SHORT).show();


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
*/
            for (int i = 0; i < list.size(); i++) {


                //Toast.makeText(getApplicationContext(), "Code Envoi :" + cursor.getString(1), Toast.LENGTH_SHORT).show();

                newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", list.get(i).getCode_envoi()));
                newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Non Livraison"));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "non liv"));


                if (list.get(i).getDesignation().equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || list.get(i).getDesignation().equalsIgnoreCase("POD")) {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
                } else {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

                }
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", stat));
                newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif", motif));
                newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure", mesure));
                newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement", ""));
                newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid", ""));
                newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid", ""));
                newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire", destinataire));
                newEntity.getProperties().put("ModeLiv", new ODataPropertyDefaultImpl("ModeLiv", mode_liv));
                newEntity.getProperties().put("PointRelais", new ODataPropertyDefaultImpl("PointRelais", relais));
                newEntity.getProperties().put("Division", new ODataPropertyDefaultImpl("Division", div));



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





        /*    newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", code_envoi));
            newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
            newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Livraison"));
            newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "liv"));


            newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));
            newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

            newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat","E0010"));
            newEntity.getProperties().put("Raison", new ODataPropertyDefaultImpl("Raison",raison));*/


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

                                db = new SQLiteHandler(this);

                                for (int i = 0; i < list.size(); i++) {


                                    db.updateDataglobal(list.get(i).getCode_envoi(), "Non livré");
                                    db.deleteper(list.get(i).getCode_envoi());
                                }
                             //   db.deleteData3();
                               db.deleteenvoi(session.getUsername());

                            } else {
                                Toast.makeText(this, "une Erreur est survenue ", Toast.LENGTH_SHORT).show();


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

            Intent intent = new Intent(Raison1.this, AgencyListActivity1.class);
            intent.putExtra("val", String.valueOf(val));
            intent.putExtra("val1", String.valueOf(val1));
            startActivity(intent);
            finish();



        }
        else
        {


            Toast.makeText(getApplicationContext(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

        }
    }




}