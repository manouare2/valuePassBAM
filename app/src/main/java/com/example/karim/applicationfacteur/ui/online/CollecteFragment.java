package com.example.karim.applicationfacteur.ui.online;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.Operation;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.ui.main.Livraison_entrep;
import com.example.karim.applicationfacteur.ui.main.MapsActivity;
import com.example.karim.applicationfacteur.ui.main.Motif_CL;
import com.example.karim.applicationfacteur.ui.main.Raison;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.SignatureActivity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.karim.applicationfacteur.ui.main.MainCL.url_g;


public class CollecteFragment extends Fragment implements UIListener {


    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    LogonCoreContext lgtx;


    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }

    private View myView;
    private EditText telephone, adresse,client,interl;
    private Button btn1,btn2;
    private LayoutInflater inflater;
    private Collecte cl;
    Button liv,nonliv;
    static  int nbliv=0 ;
    int  nbnonliv = 0;
    String login,pwd;
    String size;
    SQLiteHandler db;
    RadioButton cb,cb2;
    RadioGroup rd;
    Cursor cr;
    SessionManager session;
    String des, pere;




    private boolean isNew = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        if (myView == null) {
            myView = this.inflater.inflate(R.layout.collectefragment, null);


            adresse = (EditText) myView.findViewById(R.id.adr);
            client = (EditText) myView.findViewById(R.id.client);
            interl = (EditText) myView.findViewById(R.id.inter);
            telephone = (EditText) myView.findViewById(R.id.teleph);
            rd= (RadioGroup) myView.findViewById(R.id.rdg);
            cb = (RadioButton) myView.findViewById(R.id.radioButton);
            cb2= (RadioButton) myView.findViewById(R.id.radioButton2);
           /* liv = (Button) myView.findViewById(R.id.liv);
            nonliv = (Button) myView.findViewById(R.id.nonliv);*/


        }
            //When user select an agency, the agency will be passed as an intent
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            cl = bundle.getParcelable("CollecteSelected");
            initializeViews();
        }

        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton:


                                 SQLiteHandler  db = new SQLiteHandler(getActivity());
                                 Cursor cr = db.getFDRStatut(cl.getNum_client(),cl.getNum_fdr());
                        if(cr.getCount()==0)
                        {
                            Intent intent = new Intent(getActivity(),  CollecteTraitmActivity.class);
                            intent.putExtra("CollecteSelected",cl);
                            startActivity(intent);
                            getActivity().finish();

                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Attention");
                            builder.setMessage("Collecte déja traité");
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
                        }


                        break;


                    case R.id.radioButton2:


                          db = new SQLiteHandler(getActivity());
                          cr = db.getFDRStatut(cl.getNum_client(),cl.getNum_fdr());
                        if(cr.getCount()==0)
                        {
                            Intent intent = new Intent(getActivity(),Motif_CL.class);
                            intent.putExtra("CollecteSelected",cl);
                            startActivity(intent);
                            getActivity().finish();

                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Attention");
                            builder.setMessage("Collecte déja traité");
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
                        }


                        break;




                }
            }
        });



        return myView;
    }


    private void initializeViews(){

        client.setText(cl.getNom_client());
        client.setFocusable(false);


        interl.setText(cl.getInterlocuteur());
        interl.setFocusable(false);

        telephone.setText(cl.getTelephone_client());
        telephone.setFocusable(false);

        adresse.setText(cl.getAdresse_client());
        adresse.setFocusable(false);

        adresse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent();
                intent.putExtra("CollecteSelected",cl);
                intent.setClass(getActivity(), MapsActivity.class);
                startActivity(intent);
                getActivity().finish();


            }
        });


        telephone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String phoneNo = telephone.getText().toString();

                String dial = "tel:" + phoneNo;

                Intent phoneCall=new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phoneNo,null));

                Intent sms = new Intent(Intent.ACTION_VIEW);
                sms.setType("vnd.android-dir/mms-sms");
                sms.putExtra("address",phoneNo);
                sms.putExtra("sms_body", "Bonjour! nous vous avertirons du passage du facteur");
                Intent chooser=Intent.createChooser(sms,"");//default action
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{phoneCall});//additional actions
                startActivity(chooser);
            }
        });






        isNew = false;
    }



    public void onSaveRequested() {
    }


    @Override
    public void onRequestError(int operation, Exception e) {

    }


    @Override
    public void onRequestSuccess(int operation, String key) {
        String message= "";
        if (operation == Operation.CreateAgency.getValue()){
            message = getString(R.string.msg_success_create_agency_param, key);
        } else if (operation == Operation.UpdateAgency.getValue()){
            if (TextUtils.isEmpty(key))
                message = getString(R.string.msg_success_update_agency);
            else
                message = getString(R.string.msg_success_update_agency_param, key);
        }
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }




    private boolean registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(broadcastReceiver, internetFilter);
        String status = getConnectivityStatusString(getActivity());
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

        db = new SQLiteHandler(getActivity());
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


    public void InsertBatch(String code_envoi,String motif,String mesure ,String modepaiement,String ModeLiv,String TypePid,String Destinataire ,String Pid,String designation)

    {

        session = new SessionManager(getActivity());
        login = session.getUsername();
        pwd = session.getPassword();
        Agency1 agen = null;
        ODataResponse oDataResponse = null;

        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx, login, pwd);

        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                getActivity()).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(getActivity()));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getActivity(), requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
            url = new URL(url_g);
       //     url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
         //   url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        OnlineODataStore store = null;


        try {
            store = OnlineODataStore.open(getActivity(), url, manager, null);
        } catch (ODataException e) {
            e.printStackTrace();
            Log.e("e0", e.toString());
        }



        if(store != null) {
            ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
            ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.Affectation");

            ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

            batchItem.setResourcePath("AffectationSet");


            batchItem.setMode(ODataRequestParamSingle.Mode.Create);

            batchItem.setCustomTag("something to identify the request");

            db = new SQLiteHandler(getActivity());

            Cursor cursor = db.getAll3(session.getUsername());

            if (cursor.getCount() == 0) {


                newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", code_envoi));
                newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "statut"));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "stat"));


                if (designation.equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD")) {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
                } else {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

                }
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", "E000X"));
                newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif", motif));
                newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure", mesure));
                newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement", modepaiement));
                newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid", TypePid));
                newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid", Pid));
                newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire", Destinataire));
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
                                    Toast.makeText(getActivity(), "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();
                                    db.updateDataglobal(code_envoi, "livré");

                                    db.deleteData3();

                                } else {
                                    Toast.makeText(getActivity(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();


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


                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                    //Toast.makeText(getApplicationContext(), "Code Envoi :" + cursor.getString(1), Toast.LENGTH_SHORT).show();


                    newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", cursor.getString(1)));
                    newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
                    newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", cursor.getString(3)));
                    newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", cursor.getString(4)));


                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", cursor.getString(5)));
                    newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", cursor.getString(6)));

                    newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", cursor.getString(7)));

                    newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif", cursor.getString(8)));
                    newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure", cursor.getString(9)));
                    newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement", cursor.getString(10)));
                    newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid", cursor.getString(11)));
                    newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid", cursor.getString(12)));
                    newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire", cursor.getString(13)));
                    newEntity.getProperties().put("ModeLiv", new ODataPropertyDefaultImpl("ModeLiv", cursor.getString(14)));




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
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "statut"));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "stat"));


                if (designation.equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD")) {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
                } else {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

                }
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", "E000X"));
                newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif", motif));
                newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure", mesure));
                newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement", modepaiement));
                newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid", TypePid));
                newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid", Pid));
                newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire", Destinataire));
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
                                    Toast.makeText(getActivity(), "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();
                                    db.updateDataglobal(code_envoi, "livré");
                                    cursor = db.getAll3(session.getUsername());
                                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                                        db.updateDataglobal(cursor.getString(1), "livré");
                                    }
                                    db.deleteData3();


                                } else {

                                    Toast.makeText(getActivity(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

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

            Intent intent = new Intent(getActivity(), SignatureActivity.class);

            startActivity(intent);
            getActivity().finish();


        }

        else
        {
            Toast.makeText(getActivity(),"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();

        }
    }








}
