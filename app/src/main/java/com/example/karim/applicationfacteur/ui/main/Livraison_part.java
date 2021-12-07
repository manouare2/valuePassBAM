package com.example.karim.applicationfacteur.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static java.nio.charset.StandardCharsets.UTF_8;

// Ecran pour la livraison particulier
public class Livraison_part extends myActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner,spinner2;
    RadioButton rd1,rd3,rd4;
    String mode_paiement ;//= "Espéce";
    TextView crbt;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    RadioGroup rdg1,rdg;
    EditText destinataire ,pid, liv;
    String des;
    Button btn;
    String PID,PID2;
    private SQLiteHandler db;
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> spinnerAdapter2;
    SessionManager session;
    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    String val, val1;
    List<String> items = new ArrayList<String>();
    List<String> items2 = new ArrayList<String>();
    Context context;
    String designation,code_barre,obj;
    Agency3 envoi;
    String date;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date = formatter.format(now);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setContentView(R.layout.liv_part);
        rd1= (RadioButton) findViewById(R.id.radioButton3);
        rd1.setChecked(true);
        rdg1= (RadioGroup) findViewById(R.id.rdg1);
        spinner = (Spinner) findViewById(R.id.spinner);
        rd3= (RadioButton) findViewById(R.id.radioButton);
        rd3.setChecked(true);
        rd4= (RadioButton) findViewById(R.id.radioButton2);
        rdg= (RadioGroup) findViewById(R.id.rdg);
        destinataire= (EditText) findViewById(R.id.des);
        pid = (EditText) findViewById(R.id.pid);
        crbt = (TextView) findViewById(R.id.crbt);
        liv = (EditText) findViewById(R.id.liv);
        Bundle bundle = getIntent().getExtras();
        final String  client= bundle.getString("client");
        final String  mode_liv= bundle.getString("mode_liv");
        final String  service= bundle.getString("service");
        envoi = bundle.getParcelable("AgencySelected");
        designation= bundle.getString("designation");
        destinataire.setText(client);
        code_barre = bundle.getString("code");
        obj = bundle.getString("obj");
        final  Context ctx = this;
       rdg.clearCheck();
        liv.setText(mode_liv);
        liv.setFocusable(false);
        liv.setFocusable(false);

            if(mode_liv.equalsIgnoreCase("Point de relais"))
               // Point de relais
        {
            liv.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.pt,0);
        }

        btn= (Button) findViewById(R.id.button2);
        items .add("CIN");
        items .add("Passeport");
        items .add("Carte de séjour");
        items .add("Permis de conduire");
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

                        Intent intent = new Intent();
                        intent.putExtra("mode_liv",String.valueOf(mode_liv));
                        intent.setClass(getApplicationContext(),Livraison_entrep.class);
                        intent.putExtra("AgencySelected",envoi);
                        intent.putExtra("client",String.valueOf(client));
                        intent.putExtra("code",String.valueOf(code_barre));
                        intent.putExtra("service",String.valueOf(service));
                        intent.putExtra("designation",String.valueOf(designation));


                        startActivity(intent);
                       // finish();
                        break;
                    case R.id.radioButton3:

                        intent = new Intent();
                        intent.putExtra("mode_liv",String.valueOf(mode_liv));
                        intent.putExtra("client",String.valueOf(client));
                        intent.putExtra("code",String.valueOf(code_barre));
                        intent.putExtra("AgencySelected",envoi);
                        intent.putExtra("service",String.valueOf(service));
                        intent.putExtra("designation",String.valueOf(designation));
                        intent.setClass(getApplicationContext(),Livraison_part.class);



                        startActivity(intent);
                       // finish();
                        break;
                }
            }
        });





        if(service.equalsIgnoreCase("crbt ccp") == false || designation.equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD"))
        {
            rdg.clearCheck();
            rdg.removeAllViews();
            crbt.setVisibility(View.GONE);
        }
else {

            rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.radioButton:
                            mode_paiement = "Espéce ";


                            break;
                        case R.id.radioButton2:
                            mode_paiement = "Electronique";



                            final AlertDialog.Builder inputAlert = new AlertDialog.Builder(ctx);
                            inputAlert.setTitle("Veuillez saisir le numéro de téléphone du client");
                            final EditText userInput = new EditText(ctx);
                            userInput.setText(envoi.getTelephone_client());
                            inputAlert.setView(userInput);
                            inputAlert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String userInputValue = userInput.getText().toString();
                                    new SendPostRequest(userInputValue,envoi).execute();
                                }
                            });
                            inputAlert.setNegativeButton("Anuuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = inputAlert.create();
                            alertDialog.show();






                            break;

                    }

                }
            });


        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new SQLiteHandler(getApplicationContext());

                Cursor cursor1 = db.getLIV1(code_barre);
                if (cursor1.getCount() != 0) {



                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Attention");
                    builder.setMessage("cet envoi est déja traité");
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



                else {



                    Pattern pattern = Pattern.compile(".*[a-zA-Z].*");
                    Pattern pattern2 = Pattern.compile(".*[0-9].*");

                    Matcher matcher1 = pattern.matcher(pid.getText().toString());
                    Matcher matcher = pattern2.matcher(pid.getText().toString());
                   if (matcher.matches() && matcher1.matches()) {

                        if (registerInternetCheckReceiver(context)) {

                            // InsertBatch(code_barre, "AFFECTE GUICHET", "affg", "ZSD_STAT", "E0006", "E0005", raison);
                            InsertBatch(code_barre, "", "", mode_paiement, mode_liv, PID, destinataire.getText().toString(), pid.getText().toString());
                        } else {
                            db = new SQLiteHandler(getApplicationContext());

                            if (designation.equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD")) {
                                des = "ZSD_SPOD";

                            } else {
                                des = "ZSD_STAT";

                            }
                            db.addData3(code_barre,session.getUsername(), "Livraison", "liv", des, "E0006", "E0010", "", "", mode_paiement, mode_liv, PID, destinataire.getText().toString(), pid.getText().toString(), "", "");
                            db.updateDataglobal(code_barre, "livré");
                            Intent intent = new Intent(Livraison_part.this, SignatureActivity.class);
                            intent.putExtra("val", String.valueOf(val));
                            intent.putExtra("val1", String.valueOf(val1));
                            intent.putExtra("client", String.valueOf(destinataire.getText().toString()));
                            startActivity(intent);
                            finish();


                        }



                    } else {



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

                //  Toast.makeText(getApplicationContext(),"cc",Toast.LENGTH_SHORT).show();




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

    public void InsertBatch(String code_envoi,String motif,String mesure ,String modepaiement,String ModeLiv,String TypePid,String Destinataire ,String Pid)

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
                getApplicationContext()).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(getApplicationContext()));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getApplicationContext(), requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
           // url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
          //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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

            db = new SQLiteHandler(this);

            cursor = db.getAll3(session.getUsername());

            if (cursor.getCount() == 0) {

                newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre", code_envoi));
                newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Livraison"));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "liv"));


                if (designation.equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD")) {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
                } else {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

                }
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", "E0010"));
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
                                    Toast.makeText(getApplicationContext(), "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();

                                    db.updateDataglobal(code_envoi, "livré");

                                //    db.deleteData3();

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
                newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Livraison"));
                newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "liv"));


                if (designation.equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || designation.equalsIgnoreCase("POD")) {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
                } else {
                    newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

                }
                newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

                newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", "E0010"));
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
                                    Toast.makeText(getApplicationContext(), "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();

                                    db.updateDataglobal(code_envoi, "livré");
                                    cursor = db.getAll3(session.getUsername());
                                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                                        db.updateDataglobal(cursor.getString(1), "livré");
                                    }
                                //    db.deleteData3();
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


            }


            Intent intent = new Intent(Livraison_part.this, SignatureActivity.class);
            intent.putExtra("val", String.valueOf(val));
            intent.putExtra("val1", String.valueOf(val1));
            intent.putExtra("client", String.valueOf(destinataire.getText().toString()));
            startActivity(intent);
            finish();
        }



else
    {
        Toast.makeText(getApplicationContext(),"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();


    }



}



    private static boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }



    /////////********************* mobile bancking**************************////////////////////////



    public class SendPostRequest extends AsyncTask<String, Void, String> {

        String tel;
        Agency3 env;

        SendPostRequest(String telephone,Agency3 envoi)
        {
            tel=telephone;
            env=envoi;


        }


        protected void onPreExecute(){}




        protected String doInBackground(String... arg0) {
            HttpURLConnection urlConnection;
            String url;
            //   String data = json;
            String result = null;
            try {
                String username = "api_user";
                String password = "@p1_u$2r";

                String auth = new String(username + ":" + password);
                byte[] data1 = auth.getBytes(UTF_8);
                String base64 = Base64.encodeToString(data1, Base64.NO_WRAP);
                //Connect
                urlConnection = (HttpURLConnection) ((new URL("https://www.baridcash.ma/api/Authorize").openConnection()));
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Basic " + base64);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(10000);
                urlConnection.connect();
                JSONObject obj = new JSONObject();

                obj.put("user_login", "mebtoul");
                obj.put("user_pwd", "123");
                String data = obj.toString();
                //Write
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(data);
                writer.close();
                outputStream.close();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            urlConnection.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
          /* Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();*/
            new SendPostRequest1(result,tel,envoi).execute();
        }
    }




    /********************************/


    public class SendPostRequest1 extends AsyncTask<String, Void, String> {

        String res;
        String tel;
        Agency3 envoi;
        SendPostRequest1(String rs,String telephone,Agency3 env)
        {
            res=rs;
            tel= telephone;
            envoi = env ;
            envoi = env ;

        }
        protected void onPreExecute(){}




        protected String doInBackground(String... arg0) {
            HttpURLConnection urlConnection;
            String url;
            //   String data = json;
            String result = null;
            try {
                String username = "api_user";
                String password = "@p1_u$2r";

                String auth = new String(username + ":" + password);
                byte[] data1 = auth.getBytes(UTF_8);
                String base64 = Base64.encodeToString(data1, Base64.NO_WRAP);
                //Connect
                urlConnection = (HttpURLConnection) ((new URL("https://www.baridcash.ma/api/RequestPayment").openConnection()));
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Basic " + base64);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(10000);
                urlConnection.connect();
                JSONObject obj = new JSONObject();


                String[] str = envoi.getNom_client().split("\\s+");;


                obj.put("user_token", res.substring(10,23));
                obj.put("amount",envoi.getCrbt());
                obj.put("customer_tel",tel);
                obj.put("customer_first_name",str[0]);
                obj.put("customer_last_name",str[1]);
                obj.put("product_reference", "REF-0000000A23");
                obj.put("product_name","AMANA");
                obj.put("partner_app_request_date",date);

                String data = obj.toString();
                //Write
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(data);
                writer.close();
                outputStream.close();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            urlConnection.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result.substring(24, 66),
                    Toast.LENGTH_LONG).show();

        }
    }










}
