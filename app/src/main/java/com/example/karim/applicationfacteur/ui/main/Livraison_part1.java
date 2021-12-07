package com.example.karim.applicationfacteur.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Livraison_part1 extends myActivity {

    RadioButton rd1;
    RadioGroup rdg1;
    EditText destinataire ,pid, liv;
    Button btn;
    private SQLiteHandler db;
    Spinner spinner;

    SessionManager session;

    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    String val, val1,designation,code_barre;
    List<String> items = new ArrayList<String>();
    List<String> items2 = new ArrayList<String>();
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> spinnerAdapter2;
    Context context;
    String PID;
    String des;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.liv_part1);
        spinner = (Spinner) findViewById(R.id.spinner);
        destinataire= (EditText) findViewById(R.id.des);
        pid = (EditText) findViewById(R.id.pid);
        liv = (EditText) findViewById(R.id.liv);
        rd1= (RadioButton) findViewById(R.id.radioButton3);
        rd1.setChecked(true);
        rdg1= (RadioGroup) findViewById(R.id.rdg1);
        destinataire= (EditText) findViewById(R.id.des);
        Bundle bundle = getIntent().getExtras();
        final String  client= bundle.getString("client");
        destinataire.setText(client);
        final String  mode_liv= bundle.getString("mode_liv");
        designation= bundle.getString("designation");
        destinataire.setText(client);
        code_barre = bundle.getString("code");
        btn= (Button) findViewById(R.id.button2);
        final  Context ctx = this;


//Rue quartier ville





        liv.setText(mode_liv);
        liv.setFocusable(false);




        if(mode_liv.equalsIgnoreCase("Point de relais"))
        // Point de relais

        {
            liv.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.pt,0);

        }
        btn= (Button) findViewById(R.id.button2);

        items .add("CIN");
        items .add("Passeport");

        context = this;


        spinnerAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,items);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {



            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PID = (String)adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        rdg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton1:
                        final ArrayList<Agency3> myList1 = (ArrayList<Agency3>) getIntent().getSerializableExtra("listagc");

                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(),Livraison_entrep1.class);
                        intent.putExtra("mode_liv",String.valueOf(mode_liv));
                        intent.putExtra("listagc",myList1);

                        intent.putExtra("client",String.valueOf(client));

                        intent.putExtra("designation",String.valueOf(designation));
                        intent.putExtra("code",String.valueOf(code_barre));


                        startActivity(intent);
                        // finish();
                        break;
                    case R.id.radioButton3:
                        final ArrayList<Agency3> myList = (ArrayList<Agency3>) getIntent().getSerializableExtra("listagc");
                        intent = new Intent();
                        intent.setClass(getApplicationContext(),Livraison_part1.class);
                        intent.putExtra("mode_liv",String.valueOf(mode_liv));
                        intent.putExtra("listagc",myList);

                        intent.putExtra("client",String.valueOf(client));

                        intent.putExtra("designation",String.valueOf(designation));
                        intent.putExtra("code",String.valueOf(code_barre));



                        startActivity(intent);
                        // finish();
                        break;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Pattern pattern = Pattern.compile(".*[a-zA-Z].*");
                Pattern pattern2 = Pattern.compile(".*[0-9].*");

                Matcher matcher1 = pattern.matcher(pid.getText().toString());
                Matcher matcher = pattern2.matcher(pid.getText().toString());
                if (matcher.matches() && matcher1.matches()) {

                    final ArrayList<Agency3> myList = (ArrayList<Agency3>) getIntent().getSerializableExtra("listagc");

                    if (registerInternetCheckReceiver(context)) {

                        // InsertBatch(code_barre, "AFFECTE GUICHET", "affg", "ZSD_STAT", "E0006", "E0005", raison);
                        InsertBatch(myList, mode_liv, destinataire.getText().toString(),PID,pid.getText().toString());
                        Intent intent = new Intent(Livraison_part1.this,SignatureActivity.class);
                        intent.putExtra("val", String.valueOf(val));
                        intent.putExtra("val1", String.valueOf(val1));
                        intent.putExtra("client", String.valueOf(destinataire.getText().toString()));
                        startActivity(intent);
                        finish();


                    } else {


                        db = new SQLiteHandler(getApplicationContext());

                        for(int i = 0;i<myList.size();i++)

                        {

                            if (myList.get(i).getDesignation().equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD")) {
                                des = "ZSD_SPOD";

                            } else {
                                des = "ZSD_STAT";

                            }



                            db.addData3(myList.get(i).getCode_envoi(),session.getUsername(), "Livraison", "liv", des, "E0006", "E0010", "", "", "", mode_liv, PID, destinataire.getText().toString(), pid.getText().toString(), "", "");
                            db.updateDataglobal(myList.get(i).getCode_envoi(),"livré");
                            Intent intent = new Intent(Livraison_part1.this,SignatureActivity.class);
                            intent.putExtra("val", String.valueOf(val));
                            intent.putExtra("val1", String.valueOf(val1));
                            intent.putExtra("client", String.valueOf(destinataire.getText().toString()));
                            startActivity(intent);
                            finish();

                        }


                    }


                } else {
                  /*  pid.setText("Le PID doit contenir au moins un caractère et chiffres");
                    pid.getBackground().setColorFilter(getResources().getColor(R.color.sap_uex_dark_red),
                            PorterDuff.Mode.SRC_ATOP);
                    pid.setTextColor(context.getResources().getColor(R.color.sap_uex_dark_red));
*/



                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Attention");
                    builder.setMessage("Le PID doit contenir au moins un caractère et chiffres");
                    builder.setCancelable(false);
                    builder.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();



                        }


                    });

                    builder.setNeutralButton("Annuler" +
                            "", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();


                        }
                    });


                    AlertDialog alert1 = builder.create();


                    alert1.show();

                    pid.setText("");
                    pid.getBackground().setColorFilter(getResources().getColor(R.color.sap_uex_dark_red),
                            PorterDuff.Mode.SRC_ATOP);



                }

            }


        });




        }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, AgencyListActivity1.class);
            startActivity(intent);
            finish();
        }

        return false;
        // Disable back button..............


    }

    public void InsertBatch(List<Agency3> list,String ModeLiv,String destinataire,String type,String pid)

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
        //    url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
           // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
            url = new URL(url_g);
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

        if(store!=null) {


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
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Livraison"));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "liv"));


                if (list.get(i).getDesignation().equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || list.get(i).getDesignation().equalsIgnoreCase("POD")) {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
                } else {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

                }
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", "E0010"));
                newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif", ""));
                newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure", ""));
                newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement", ""));
                newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid", type));
                newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid", pid));
                newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire", destinataire));
                newEntity.getProperties().put("ModeLiv", new ODataPropertyDefaultImpl("ModeLiv", ModeLiv));

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


                                    db.updateDataglobal(list.get(i).getCode_envoi(), "livré");
                                    db.deleteper(list.get(i).getCode_envoi());
                                }
                              //  db.deleteData3();
                                db.deleteenvoi(session.getUsername());


                            } else {

                                Toast.makeText(getApplicationContext(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();


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


            Intent intent = new Intent(Livraison_part1.this, SignatureActivity.class);
            intent.putExtra("val", String.valueOf(val));
            intent.putExtra("val1", String.valueOf(val1));
            intent.putExtra("client", String.valueOf(destinataire.toString()));
            startActivity(intent);
            finish();

        }

else
        {
            Toast.makeText(getApplicationContext(),"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();


        }

    }



}
