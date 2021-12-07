package com.example.karim.applicationfacteur.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.zxing.Result;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;


public class Scann extends myActivity implements ZXingScannerView.ResultHandler {


    static ArrayList<Agency3> agencyList = new ArrayList<>();

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    Button btn ;
   private  static String code;
   String login,pwd;
    SQLiteHandler db;
    String code1;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    Agency3 agc;
    LogonCoreContext lgtx;
    String myResult;
    String code_resultat;
    String code_retour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


                scannerView = new ZXingScannerView(getApplicationContext());
                setContentView(scannerView);
                int currentApiVersion = Build.VERSION.SDK_INT;

               // if (currentApiVersion >= Build.VERSION_CODES.M) {
                    if (checkPermission()) {
                        Toast.makeText(getApplicationContext(), "Vous pouvez commencer le scann", Toast.LENGTH_LONG).show();
                    } else {
                        requestPermission();
                    }
                }


           // }









    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = Build.VERSION.SDK_INT;
        //if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }



    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                       // Toast.makeText(getApplicationContext(), "permission accordée, Maintenant, vous pouvez accéder à la caméra", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"permission refusée, vous ne pouvez pas accéder et caméra", Toast.LENGTH_LONG).show();
                      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("Vous devez autoriser l'accès aux permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }

                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Scann.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
         myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        code=result.getText();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Code Envoi :"+code);
        builder.setCancelable(false);

        builder.setPositiveButton("Affecté", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(registerInternetCheckReceiver(context)) {
                    scanCode(result);
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Connectez vous à internet puis résseayer", Toast.LENGTH_SHORT).show();
                    scannerView.resumeCameraPreview(Scann.this);
                }




            }


        });

        builder.setNeutralButton("Annuler" +
                "", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Scann.this,Scann.class);
                startActivity(intent);

            }
        });




        AlertDialog alert1 = builder.create();


        alert1.show();
    }

    public static List<Agency3> getAgencies1(Context context)  {

        return agencyList;

    }


    public  String  InsertBatch(Context ctx,String code_envoi)

    {

        Agency1 agen = null;
        ODataResponse oDataResponse = null;

        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx,"HHAROUY","123456789");

        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                ctx).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(ctx));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx, requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
            url = new URL(url_g);
          //  url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
        //    url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        OnlineODataStore store = null;


        try {
            store = OnlineODataStore.open(ctx, url, manager, null);
        } catch (ODataException e) {
            e.printStackTrace();

        }

        ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM_SRV_01.Affectation");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("AffectationSet");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");



            //Toast.makeText(ctx,"veuillez scanner et traiter les commandes avant de les rafraichir",Toast.LENGTH_SHORT).show();

                newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre",code_envoi));
                newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur","2"));
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30","AFFECTE FACTEUR"));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04","aff"));

                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma","ZSD_STAT"));
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec"," "));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat","E0006"));
                newEntity.getProperties().put("Raison", new ODataPropertyDefaultImpl("Raison"," "));


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

                }

// Add batch item to batch request

                try {

                    requestParamBatch.add(changeSetItem);

                } catch (ODataException e) {
                    e.printStackTrace();

                }

                try {
                    oDataResponse = store.executeRequest(requestParamBatch);
                } catch (ODataNetworkException e) {
                    e.printStackTrace();


                } catch (ODataParserException e) {
                    e.printStackTrace();

                } catch (ODataContractViolationException e) {
                    e.printStackTrace();

                }


            Map<ODataResponse.Headers, String> headerMap = oDataResponse.getHeaders();


            if (headerMap != null) {

                String code = headerMap.get(ODataResponse.Headers.Code);

            }

// Get batch response

            if (oDataResponse instanceof ODataResponseBatchDefaultImpl) {

                ODataResponseBatch batchResponse = (ODataResponseBatch) oDataResponse;

                List<ODataResponseBatchItem> responses = batchResponse.getResponses();

                for (ODataResponseBatchItem response : responses) {

                    // Check if batch item is a change set

                    if (response instanceof ODataResponseChangeSetDefaultImpl) {

                        ODataResponseChangeSetDefaultImpl changesetResponse = (ODataResponseChangeSetDefaultImpl) response;

                        List<ODataResponseSingle> singles = changesetResponse.getResponses();

                        for (ODataResponseSingle singleResponse : singles) {

                            // Get Custom tag

                            String customTag = singleResponse.getCustomTag();

                            // Get http status code for individual responses

                            headerMap = singleResponse.getHeaders();

                            code_retour = headerMap.get(ODataResponse.Headers.Code);
                          //  Toast.makeText(this,code_retour,Toast.LENGTH_SHORT).show();

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

            }


        db.deleteData3();

        code_resultat=code_retour;
        return code_resultat;

        }

   /* public  void InsertBatch(Context ctx)

    {
        db = new SQLiteHandler(ctx);

        Cursor cursor = db.getAll2();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            login = cursor.getString(1);
            pwd = cursor.getString(2);

        }
        Agency1 agen = null;
        ODataResponse oDataResponse = null;

        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx, login, pwd);

        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                ctx).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(ctx));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx, requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
            url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        OnlineODataStore store = null;


        try {
            store = OnlineODataStore.open(ctx, url, manager, null);
        } catch (ODataException e) {
            e.printStackTrace();
            Log.e("e0", e.toString());
        }

        ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM_SRV_01.Affectation");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("AffectationSet");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");

        cursor = db.getAll3();

        if (cursor.getCount() == 0) {
            Toast.makeText(ctx,"veuillez scanner et traiter les commandes avant de les rafraichir",Toast.LENGTH_SHORT).show();

        } else {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {


                newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", cursor.getString(1)));
                newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", cursor.getString(2)));
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", cursor.getString(3)));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", cursor.getString(4)));

                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", cursor.getString(5)));
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", cursor.getString(6)));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", cursor.getString(8)));
                newEntity.getProperties().put("Raison", new ODataPropertyDefaultImpl("Raison", cursor.getString(7)));


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
                            Log.e("1", "26");
                            // Get individual response

                            ODataPayload payload = singleResponse.getPayload();

                            if (payload != null) {

                                if (payload instanceof ODataError) {

                                    ODataError oError = (ODataError) payload;

                                    String uiMessage = oError.getMessage d();

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


           db.deleteData3();


        }


    }*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Scann.this, AgencyListActivity1.class);
            startActivity(intent);
            finish();



        }

        return false;
        // Disable back button..............
    }

    private void scanCode(final Result result) {
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
                           // Toast.makeText(getApplicationContext(),"Timed out",Toast.LENGTH_SHORT).show();
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
                       // Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                        scannerView.resumeCameraPreview(Scann.this);

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

                            String code1 = result.getText();


                            ODataResponseSingle resp = null;
                            try {
                              /*  resp = store.executeReadEntity(
                                        "VerificationSet('" + code1 + "')", null);
*/

                                String str = code1;
                                str= str.replace(" " , "");
                                resp = store.executeReadEntity(
                                        "VerificationSet('" + str + "')", null);
                            } catch (ODataNetworkException e3) {
                                e3.printStackTrace();
                            } catch (ODataParserException e2) {
                                e2.printStackTrace();
                                Toast.makeText(getApplicationContext(),e2.toString()+"e2",Toast.LENGTH_SHORT).show();
                            } catch (ODataContractViolationException e3) {
                                e3.printStackTrace();
                                Toast.makeText(getApplicationContext(),e3.toString()+"e3",Toast.LENGTH_SHORT).show();
                            }
                            //Get the response payload
                            ODataEntity feed = (ODataEntity) resp.getPayload();
                            //Get the list of ODataEntity

                            //Loop to retrieve the information from the response

                            properties = feed.getProperties();
                            property = properties.get("Statut_veri");

                            if (property.getValue().equals("OK")) {


                                db = new SQLiteHandler(getApplicationContext());


                                db.addData(result.getText(),
                                        (String) properties.get("Client").getValue(),
                                        (String) properties.get("Telephone").getValue(),
                                        (String) properties.get("Ville").getValue()+" "+(String) properties.get("Quartier").getValue(),
                                        (String) properties.get("Montant").getValue(),
                                        "En cours","DNL",session.getUsername(),(String) properties.get("POD").getValue(),(String) properties.get("Service").getValue(),(String) properties.get("Designation").getValue(), (String) properties.get("Mode_liv").getValue(),(String) properties.get("Rue").getValue(),(String) properties.get("Code_postal").getValue(),"1", DateFormat.getDateInstance().format(new Date()), (String) properties.get("RUE_RELAIS").getValue()+", "+(String) properties.get("NUM_RUE_RELAIS").getValue()+", "+(String) properties.get("LOCALITE_RELAIS").getValue()+", "+(String) properties.get("CODE_POSTAL_RELAIS").getValue(),(String) properties.get("NOM_RELAIS").getValue(),(String) properties.get("AGENCE").getValue(),   (String) properties.get("RUE_AGENCE").getValue()+", "+(String) properties.get("CODE_POSTAL_AGENCE").getValue()+", "+(String) properties.get("LOCALITE_AGENCE").getValue(),(String) properties.get("Num_obj_dnl").getValue(),(String) properties.get("TELEPHONE_EXP").getValue(),(String) properties.get("CODE_ENVOI_PERE").getValue());



                              /*  int num_obj = (int) properties.get("Num_obj_dnl").getValue();
                                int a  = 664;


                                SharedPreferences sharedpreferences = getSharedPreferences("numob", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putInt("num", a);
                                editor.commit();*/

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
                                    agc.setObj(cursor1.getString(20));
                                    agc.setTelephone_exp(cursor1.getString(22));



                                    agencyList.add(agc);
                                }

                                String obj = (String) properties.get("Num_obj_dnl").getValue();
                                SharedPreferences prefs = getSharedPreferences("objj", Context.MODE_PRIVATE);
                                prefs.edit().putString("obj",String.valueOf(obj)).commit();
                                Toast.makeText(getApplicationContext(),obj+"obj",Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(Scann.this, AgencyListActivity1.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), "Envoi non valide", Toast.LENGTH_SHORT).show();
                            }


                        }

                    }
                });
            }
        });
    }




}