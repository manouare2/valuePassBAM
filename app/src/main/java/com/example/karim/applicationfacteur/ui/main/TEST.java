package com.example.karim.applicationfacteur.ui.main;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.sap.maf.tools.logon.core.LogonCoreContext;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TEST extends myActivity {



    String obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);







            InsertBatch();


    }



    public void InsertBatch()

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
            url = new URL(url_g);
           // url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
          //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ENVOIPERE1");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("ENVOIPERE1Set");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");






        SQLiteHandler db = new SQLiteHandler(getApplicationContext());



        Cursor cursorr = db.getenvoipere1("AN001682MA_D1");


        Toast.makeText(getApplicationContext(),cursorr.getCount()+"taille",Toast.LENGTH_LONG).show();

        for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {
            Toast.makeText(getApplicationContext(),"envoi"+cursorr.getString(1),Toast.LENGTH_LONG).show();


            newEntity.getProperties().put("NumEnvoi", new ODataPropertyDefaultImpl("NumEnvoi", cursorr.getString(1)));
            newEntity.getProperties().put("ZnumObj", new ODataPropertyDefaultImpl("ZnumObj","1154"));

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