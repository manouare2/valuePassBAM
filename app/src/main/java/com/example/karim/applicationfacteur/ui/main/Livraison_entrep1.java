package com.example.karim.applicationfacteur.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity2;
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


public class Livraison_entrep1 extends myActivity {

    RadioButton rd1,rd2;
    EditText destinaitre;
    RadioGroup rdg1;
    Button btn;
    private SQLiteHandler db;
    ArrayList<Agency3> agencyListt = new ArrayList<>();

    SessionManager session;

    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    String val, val1,designation, des;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.liv_entrep1);
        rd1= (RadioButton) findViewById(R.id.radioButton1);
        rd1.setChecked(true);
        btn = (Button) findViewById(R.id.button2);
        rd2= (RadioButton) findViewById(R.id.radioButton3);
        rdg1= (RadioGroup) findViewById(R.id.rdg1);
        destinaitre= (EditText) findViewById(R.id.des);
        Bundle bundle = getIntent().getExtras();
        final String  client= bundle.getString("client");
        destinaitre.setText(client);
        btn= (Button) findViewById(R.id.button2);
        bundle = getIntent().getExtras();
        agencyListt = bundle.getParcelable("listagc");

        final String code_barre = bundle.getString("code");

        final String  mode_liv = bundle.getString("mode_liv");
        designation = bundle.getString("designation");




       /* if(rd2.isChecked())
        {
            Intent intent = new Intent();
            intent.setClass(this,Livraison_part.class);
            intent.putExtra("val",String.valueOf(nbliv));
            intent.putExtra("val1",String.valueOf(nbnonliv));
            intent.putExtra("siz",String.valueOf(size));
            intent.putExtra("code",String.valueOf(agency.getCode_envoi()));
            intent.putExtra("client",String.valueOf(agency.getNom_client()));

            startActivity(intent);
              finish();
        }

*/


        rdg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton1:

                        final ArrayList<Agency3> myList = (ArrayList<Agency3>) getIntent().getSerializableExtra("listagc");
                        Intent intent = new Intent();
                        intent.putExtra("mode_liv",String.valueOf(mode_liv));
                        intent.putExtra("listagc",myList);

                        intent.putExtra("client",String.valueOf(client));

                        intent.putExtra("designation",String.valueOf(designation));
                        intent.putExtra("code",String.valueOf(code_barre));

                        intent.setClass(Livraison_entrep1.this,Livraison_entrep1.class);



                        startActivity(intent);
                        finish();
                        break;
                    case R.id.radioButton3:
                        intent = new Intent();

                        final ArrayList<Agency3> myList1 = (ArrayList<Agency3>) getIntent().getSerializableExtra("listagc");

                        intent.putExtra("listagc",myList1);

                        intent.setClass(Livraison_entrep1.this,Livraison_part1.class);
                        intent.putExtra("client",String.valueOf(client));
                        intent.putExtra("mode_liv",String.valueOf(mode_liv));
                        intent.putExtra("designation",String.valueOf(designation));
                        intent.putExtra("code",String.valueOf(code_barre));



                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ArrayList<Agency3> myList = (ArrayList<Agency3>) getIntent().getSerializableExtra("listagc");

                if (registerInternetCheckReceiver(context)) {


                    InsertBatch(myList,mode_liv,destinaitre.getText().toString());




                } else {


                    db = new SQLiteHandler(getApplicationContext());

                    for(int i = 0;i<myList.size();i++)

                    {

                        if (myList.get(i).getDesignation().equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || myList.get(i).getDesignation().equalsIgnoreCase("POD")) {
                            des = "ZSD_SPOD";

                        } else {
                            des = "ZSD_STAT";

                        }


                        db.addData3(myList.get(i).getCode_envoi(),session.getUsername(), "Livraison", "liv", des, "E0006", "E0010", "", "", "", mode_liv, "", destinaitre.getText().toString(), "", "", "");
                        db.updateDataglobal(myList.get(i).getCode_envoi(),"livré");
                        Intent intent = new Intent(Livraison_entrep1.this,SignatureActivity.class);
                        intent.putExtra("val", String.valueOf(val));
                        intent.putExtra("val1", String.valueOf(val1));
                        intent.putExtra("client", String.valueOf(destinaitre.getText().toString()));
                        startActivity(intent);
                        finish();

                    }






                }




            }

        });



    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, AgencyListActivity2.class);
            startActivity(intent);
            finish();
        }

        return false;
        // Disable back button..............


    }

    public void InsertBatch(List<Agency3> list,String ModeLiv,String destinataire)

    {
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
           // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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
                newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid", ""));
                newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid", ""));
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
            Intent intent = new Intent(Livraison_entrep1.this,SignatureActivity.class);
            intent.putExtra("val", String.valueOf(val));
            intent.putExtra("val1", String.valueOf(val1));
            intent.putExtra("client", String.valueOf(destinaitre.getText().toString()));
            startActivity(intent);
            finish();


        }


        else
        {


            Toast.makeText(getApplicationContext(),"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();

        }
    }


}



