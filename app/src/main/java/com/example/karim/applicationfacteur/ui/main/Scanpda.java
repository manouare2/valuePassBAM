package com.example.karim.applicationfacteur.ui.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.odata.ODataEntity;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Scanpda  extends  myActivity{
    static ArrayList<Agency3> agencyList = new ArrayList<>();
        private SQLiteHandler db;
        Spinner spinner;
        Spinner spinner2;
        Button btn;
        String raison;
        String raison2;
        List<String> items = new ArrayList<String>();
        List<String> items2 = new ArrayList<String>();
        SessionManager session;
        String val, val1,des;
        private boolean internetConnected = true;
        public static int TYPE_WIFI = 1;
        public static int TYPE_MOBILE = 2;
        public static int TYPE_NOT_CONNECTED = 0;
        private boolean a;
        ArrayAdapter<String> spinnerAdapter;
        ArrayAdapter<String> spinnerAdapter2;
        Context context;
        List<ODataEntity> entities ,entities1;
        String motif,designation,motiff,mesure,stat;
        EditText code;
        String code_barre;
        String str;
        String num_obj, adresse;
        Agency3 agc;
        String obj;
        LogonCoreContext lgtx = null;
        Agency1 agen = null;
        ODataResponse oDataResponse = null;



    @Override
        protected void onCreate(final Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);

            setContentView(R.layout.scanpda);

            context = this;

            btn = (Button) findViewById(R.id.btn);

            code = (EditText) findViewById(R.id.code);

            session = new SessionManager(this);
             db = new SQLiteHandler(this);



      /*      SharedPreferences sp = getSharedPreferences("ob", Context.MODE_PRIVATE);
            obj  = sp.getString("ob1", null);
            if(obj==null)
            {
               obj="";
                Log.e("han","test");
                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();

            }
            else {

                obj  = String.valueOf(sp.getString("ob1", null));

                String val=obj+"";
                 str = val;

                str= str.replace(" " , "");

                Log.e("han","test1");
                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();

            }
*/

            btn.setOnClickListener(new View.OnClickListener() {



                public void onClick(View view) {

                    String var;

                 var=code.getText().toString().replace(" ", "");
                    if (var.isEmpty())
                    {

                        Toast.makeText(getApplicationContext(), "veuillez saisir le code d'envoi", Toast.LENGTH_LONG).show();




                    } else {


                        if (registerInternetCheckReceiver(context)) {


                          /*  SharedPreferences sp = getSharedPreferences("num_obj", Context.MODE_PRIVATE);
                            obj = sp.getString("obj", null);*/
                            Cursor cr= db.getobject_dnl(session.getUsername());
                            // Toast.makeText(getApplicationContext(),"objj"+null,Toast.LENGTH_LONG).show();
                            if (cr.getCount() == 0) {
                               // Toast.makeText(getApplicationContext(), "objj" + null, Toast.LENGTH_LONG).show();

                                obj = "";
                                Log.e("han", "test");

                                scanCode(code.getText().toString().toUpperCase(), obj, view);


                            } else {



                                for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
                                    obj = cr.getString(0);
                                }

                                String val = obj + "";
                                str = val;

                                str = str.replace(" ", "");


                                Log.e("han", "test1");

                                scanCode(code.getText().toString().toUpperCase(), str, view);


                            }


                            //Toast.makeText(getApplicationContext(), "obj"+obj, Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getApplicationContext(), "Connectez vous à internet puis résseayer", Toast.LENGTH_SHORT).show();

                        }


                    }

                }
            });





        }




    public static List<Agency3> getAgencies1(Context context)  {

        return agencyList;

    }


    private void scanCode(final String code1, final String obj, final View v) {
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
                         //   url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                         //   url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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
                              /*  resp = store.executeReadEntity(
                                        "VerificationSet('" + code1 + "')", null);

*/



//code envoi est verifié au niveau de la table zsd_statut_user

                                String str = code1;

                                str= str.replace(" " , "");
                                resp = store.executeReadEntity("VerificationSet(code_envoi='" + str + "',Num_obj='" + obj+ "')", null);

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

                                property = properties.get("Statut_veri");

                                if (property.getValue().equals("OK")) {
                                    String code_envoi_pere= (String) properties.get("CODE_ENVOI_PERE").getValue();

                                    if(code_envoi_pere.isEmpty())
                                    {


                                    db = new SQLiteHandler(getApplicationContext());
                                    String oobj = (String) properties.get("Num_obj_dnl").getValue();
                                    String oobj1= oobj.replace(" " , "");

                                        String designation = (String) properties.get("Designation").getValue();
                                        if(designation.equalsIgnoreCase("POD"))
                                        {
                                            adresse=(String) properties.get("ADRESSE").getValue();
                                        }
                                        else
                                        {
                                            adresse= (String) properties.get("Quartier").getValue()+", "+(String) properties.get("Rue").getValue()+", "+(String) properties.get("point_remise").getValue()+", "+(String) properties.get("point_geo").getValue();


                                        }

                                    db.addData(code1.replace(" " , ""),
                                            (String) properties.get("Client").getValue(),
                                            (String) properties.get("Telephone_dest").getValue(),
                                            adresse,
                                            (String) properties.get("Montant").getValue(),
                                            "En cours","DNL",session.getUsername(),(String) properties.get("POD").getValue(),(String) properties.get("Service").getValue(),(String) properties.get("Designation").getValue(), (String) properties.get("Mode_liv").getValue(),(String) properties.get("Rue").getValue(),(String) properties.get("Code_postal").getValue(),"1", DateFormat.getDateInstance().format(new Date()), (String) properties.get("RUE_RELAIS").getValue()+", "+(String) properties.get("NUM_RUE_RELAIS").getValue()+", "+(String) properties.get("LOCALITE_RELAIS").getValue()+", "+(String) properties.get("CODE_POSTAL_RELAIS").getValue(),(String) properties.get("NOM_RELAIS").getValue(),(String) properties.get("AGENCE").getValue(),   (String) properties.get("RUE_AGENCE").getValue()+", "+(String) properties.get("CODE_POSTAL_AGENCE").getValue()+", "+(String) properties.get("LOCALITE_AGENCE").getValue(),oobj1,(String) properties.get("TELEPHONE_EXP").getValue(),(String) properties.get("CODE_ENVOI_PERE").getValue());

                                    Log.e("ns","ns");


                                      /*  SharedPreferences prefs = getSharedPreferences("num_obj", Context.MODE_PRIVATE);
                                        prefs.edit().putString("obj",String.valueOf(oobj1)).commit();*/


                                                     db.add_object_table_DNL(String.valueOf(oobj1),session.getUsername());

                                        Cursor cursor1 = db.getAllDNL(DateFormat.getDateInstance().format(new Date()));

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
                                            agc.setObj(cursor1.getString(21));
                                            agc.setTelephone_exp(cursor1.getString(22));
                                            //  Toast.makeText(getApplicationContext(),cursor1.getString(21)+"obbj",Toast.LENGTH_SHORT).show();

                                            agencyList.add(agc);


                                            Intent intent = new Intent(Scanpda.this, AgencyListActivity1.class);
                                            //  intent.putExtra("code",String.valueOf(obj));
                                            startActivity(intent);
                                            finish();


                                        }


                                    }
                                    else {

                                        db = new SQLiteHandler(getApplicationContext());

                                        String oobj = (String) properties.get("Num_obj_dnl").getValue();
                                        String oobj1 = oobj.replace(" ", "");

                                         if(designation!=null) {


                                             if (designation.equalsIgnoreCase("POD")) {
                                                 adresse = (String) properties.get("ADRESSE").getValue();
                                             } else {
                                                 adresse = (String) properties.get("Quartier").getValue() + ", " + (String) properties.get("Rue").getValue() + ", " + (String) properties.get("point_remise").getValue() + ", " + (String) properties.get("point_geo").getValue();

                                             }
                                         }

                                        db.addData_pere(code1.replace(" ", ""),
                                                (String) properties.get("Client").getValue(),
                                                (String) properties.get("Telephone_dest").getValue(),
                                                adresse,
                                                (String) properties.get("Montant").getValue(),
                                                "En cours", "DNL", session.getUsername(), (String) properties.get("POD").getValue(), (String) properties.get("Service").getValue(), (String) properties.get("Designation").getValue(), (String) properties.get("Mode_liv").getValue(), (String) properties.get("Rue").getValue(), (String) properties.get("Code_postal").getValue(), "1", DateFormat.getDateInstance().format(new Date()), (String) properties.get("RUE_RELAIS").getValue() + ", " + (String) properties.get("NUM_RUE_RELAIS").getValue() + ", " + (String) properties.get("LOCALITE_RELAIS").getValue() + ", " + (String) properties.get("CODE_POSTAL_RELAIS").getValue(), (String) properties.get("NOM_RELAIS").getValue(), (String) properties.get("AGENCE").getValue(), (String) properties.get("RUE_AGENCE").getValue() + ", " + (String) properties.get("CODE_POSTAL_AGENCE").getValue() + ", " + (String) properties.get("LOCALITE_AGENCE").getValue(), oobj1, (String) properties.get("TELEPHONE_EXP").getValue(), (String) properties.get("CODE_ENVOI_PERE").getValue());


                                        String tail = (String) properties.get("ENVOIS_PERE").getValue();
                                        String taill = tail+"";
                                       String  size =  tail.replace(" " , "");

                                        int taille = Integer.valueOf(size);


                                        Cursor cursorr = db.getenvoipere1(code_envoi_pere);



                                        if (cursorr.getCount() == taille)


                                        {

                                            ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
                                            ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ENVOIPERE1");

                                            ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

                                            batchItem.setResourcePath("ENVOIPERE1Set");


                                            batchItem.setMode(ODataRequestParamSingle.Mode.Create);

                                            batchItem.setCustomTag("something to identify the request");





                                         /*   SharedPreferences prefs = getSharedPreferences("num_obj", Context.MODE_PRIVATE);
                                            prefs.edit().putString("obj", String.valueOf(oobj1)).commit();*/



                                            db.add_object_table_DNL(String.valueOf(oobj1),session.getUsername());


                                            for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {


                                              /*  db.addData(cursorr.getString(1),
                                                        (String) properties.get("Client").getValue(),
                                                        (String) properties.get("Telephone_dest").getValue(),
                                                        (String) properties.get("Ville").getValue() + " " + (String) properties.get("Quartier").getValue(),
                                                        (String) properties.get("Montant").getValue(),
                                                        "En cours", "DNL", session.getUsername(), (String) properties.get("POD").getValue(), (String) properties.get("Service").getValue(), (String) properties.get("Designation").getValue(), (String) properties.get("Mode_liv").getValue(), (String) properties.get("Rue").getValue(), (String) properties.get("Code_postal").getValue(), "1", DateFormat.getDateInstance().format(new Date()), (String) properties.get("RUE_RELAIS").getValue() + ", " + (String) properties.get("NUM_RUE_RELAIS").getValue() + ", " + (String) properties.get("LOCALITE_RELAIS").getValue() + ", " + (String) properties.get("CODE_POSTAL_RELAIS").getValue(), (String) properties.get("NOM_RELAIS").getValue(), (String) properties.get("AGENCE").getValue(), (String) properties.get("RUE_AGENCE").getValue() + ", " + (String) properties.get("CODE_POSTAL_AGENCE").getValue() + ", " + (String) properties.get("LOCALITE_AGENCE").getValue(), oobj1, (String) properties.get("TELEPHONE_EXP").getValue(), (String) properties.get("CODE_ENVOI_PERE").getValue());

                                               InsertBatch(cursorr.getString(1),obj);*/


                                            ///////////////
                                                db.addData(cursorr.getString(1),
                                                        cursorr.getString(2),
                                                        cursorr.getString(3),
                                                        cursorr.getString(4),
                                                        cursorr.getString(5),
                                                        cursorr.getString(6),  cursorr.getString(7),  cursorr.getString(8),cursorr.getString(9),cursorr.getString(10),  cursorr.getString(11),  cursorr.getString(12),  cursorr.getString(13),  cursorr.getString(14),  cursorr.getString(15),  cursorr.getString(16),cursorr.getString(17),cursorr.getString(18), cursorr.getString(19), cursorr.getString(20), cursorr.getString(21), cursorr.getString(22), cursorr.getString(23));



                                                newEntity.getProperties().put("NumEnvoi", new ODataPropertyDefaultImpl("NumEnvoi", cursorr.getString(1)));
                                                newEntity.getProperties().put("ZnumObj", new ODataPropertyDefaultImpl("ZnumObj",obj));

                                                batchItem.setPayload(newEntity);


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


// Add batch item to
// batch request

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

                                            //}

                                            Map<ODataResponse.Headers, String> headerMap = oDataResponse.getHeaders();
                                            Log.e("1", "16");


                                            if (headerMap != null) {
                                                Log.e("1", "16");
                                                String code = headerMap.get(ODataResponse.Headers.Code);
                                                Log.e("1", "17");
                                            }


// Get batch response
                                            //han


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

                                                                db = new SQLiteHandler(getApplicationContext());
                                                               db.deletepere(code_envoi_pere);

                                                                Cursor cursor1 = db.getAllDNL(DateFormat.getDateInstance().format(new Date()));


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
                                                                    agc.setObj(cursor1.getString(21));
                                                                    agc.setTelephone_exp(cursor1.getString(22));
                                                                    //  Toast.makeText(getApplicationContext(),cursor1.getString(21)+"obbj",Toast.LENGTH_SHORT).show();

                                                                    agencyList.add(agc);

                                                                }


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



                                            Intent intent = new Intent(Scanpda.this, AgencyListActivity1.class);
                                            //  intent.putExtra("code",String.valueOf(obj));
                                            startActivity(intent);
                                            finish();





                                        ;


                                    }

                                        else {


                                          /*  db.addData_pere(code1.replace(" " , ""),
                                                    (String) properties.get("Client").getValue(),
                                                    (String) properties.get("Telephone_dest").getValue(),
                                                    (String) properties.get("Ville").getValue()+" "+(String) properties.get("Quartier").getValue(),
                                                    (String) properties.get("Montant").getValue(),
                                                    "En cours","DNL",session.getUsername(),(String) properties.get("POD").getValue(),(String) properties.get("Service").getValue(),(String) properties.get("Designation").getValue(), (String) properties.get("Mode_liv").getValue(),(String) properties.get("Rue").getValue(),(String) properties.get("Code_postal").getValue(),"1", DateFormat.getDateInstance().format(new Date()), (String) properties.get("RUE_RELAIS").getValue()+", "+(String) properties.get("NUM_RUE_RELAIS").getValue()+", "+(String) properties.get("LOCALITE_RELAIS").getValue()+", "+(String) properties.get("CODE_POSTAL_RELAIS").getValue(),(String) properties.get("NOM_RELAIS").getValue(),(String) properties.get("AGENCE").getValue(),   (String) properties.get("RUE_AGENCE").getValue()+", "+(String) properties.get("CODE_POSTAL_AGENCE").getValue()+", "+(String) properties.get("LOCALITE_AGENCE").getValue(),oobj1,(String) properties.get("TELEPHONE_EXP").getValue(),(String) properties.get("CODE_ENVOI_PERE").getValue());
*/



                                            db.add_object_table_DNL(String.valueOf(oobj1),session.getUsername());


                                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                                            builder.setTitle("Attention");
                                            builder.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                                            builder.setMessage("l'envoi "+code.getText().toString()+" ne peut être traité ,Les envois groupés associés sont manquants");


                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                    code.setText("");


                                                }
                                            });
                                            builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    code.setText("");

                                                }
                                            });


                                            AlertDialog alert1 = builder.create();

                                            alert1.show();







                                        }





                                    }





                                } else {
                                    Toast.makeText(getApplicationContext(), "Envoi non valide", Toast.LENGTH_SHORT).show();
                                    code.setText("");
                                }

                            }
                            //Get the response payload


                        }
                        else
                        {

                            Toast.makeText(getApplicationContext(),"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }









    public void InsertBatch(String code_envoi,String obj)

    {

        LogonCoreContext lgtx = null;
        SessionManager session = new SessionManager(this);
        String login = session.getUsername();
        String  pwd = session.getPassword();
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
           // url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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


        ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ENVOIPERE");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("ENVOIPERESet");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");









        newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", code_envoi));
        newEntity.getProperties().put("NUMERO_OBJ", new ODataPropertyDefaultImpl("NUMERO_OBJ", obj));




        batchItem.setPayload(newEntity);


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


// Add batch item to
// batch request

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



        //}

        Map<ODataResponse.Headers, String> headerMap = oDataResponse.getHeaders();
        Log.e("1", "16");


        if (headerMap != null) {
            Log.e("1", "16");
            String code = headerMap.get(ODataResponse.Headers.Code);
            Log.e("1", "17");
        }


// Get batch response
        //han


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

                            db = new SQLiteHandler(getApplicationContext());

                            Cursor cursor1 = db.getAllDNL(DateFormat.getDateInstance().format(new Date()));


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
                                agc.setObj(cursor1.getString(21));
                                agc.setTelephone_exp(cursor1.getString(22));
                                //  Toast.makeText(getApplicationContext(),cursor1.getString(21)+"obbj",Toast.LENGTH_SHORT).show();

                                agencyList.add(agc);


                            }

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




    }




}
