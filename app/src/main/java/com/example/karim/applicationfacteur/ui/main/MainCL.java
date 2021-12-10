package com.example.karim.applicationfacteur.ui.main;


import static android.Manifest.permission.CAMERA;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.types.Envoi;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity2;
import com.example.karim.applicationfacteur.ui.online.CollecteListActivity;
import com.example.karim.applicationfacteur.ui.online.CollecteListFragment;
import com.example.karim.applicationfacteur.ui.online.QRcollecteliv;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.example.karim.applicationfacteur.utils.Constant;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.sap.maf.tools.logon.core.LogonCoreContext;
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
import com.sap.smp.client.odata.store.ODataStore;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainCL extends AppCompatActivity {
    public static String url_g = "http://testpda.barid.ma/sap/opu/odata/sap/Z_ODATA_BAM2_SRV";
    protected SessionManager session;
    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    private boolean netSpeed;
    private static SQLiteHandler db;
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
    List<ODataEntity> entities, entities1;
    Agency3 agc;
    Collecte cl;
    String adresse;
    Calendar calandrier = Calendar.getInstance();
    String minutes = null;

    Context context = this;
    static ArrayList<Agency3> agencyList = new ArrayList<>();
    static ArrayList<String> agencyList1 = new ArrayList<>();
    static ArrayList<Collecte> CollecteList = new ArrayList<>();
    public static boolean OnPause = false;
    public static boolean OnResume = false;


    String currentDateTimeString = currentDateTimeString = DateFormat.getDateInstance().format(new Date());
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());

        final String hour = String.valueOf(calandrier.get(Calendar.HOUR_OF_DAY));
        final String minute = String.valueOf(calandrier.get(Calendar.MINUTE));

        db = new SQLiteHandler(this);
        cursor = db.getCL_OFFLINE(session.getUsername());


        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                //FDR_notif();

                if (registerInternetCheckReceiver(getApplicationContext()) && cursor.getCount() != 0) {

                    Insertoffline();
                }
            }

        };
        timer.schedule(hourlyTask, 0l, 1000 * 1 * 60);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);


        SpannableStringBuilder text = new SpannableStringBuilder();

        text.setSpan(new ForegroundColorSpan(Color.WHITE),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        if (activity.equals("CollecteListActivity")) {
            getMenuInflater().inflate(R.menu.menu_collecte, menu);
        } else if (activity.equals("CreerCollecte")) {
            getMenuInflater().inflate(R.menu.menu_chargm_cl, menu);
        } else if (activity.equals("Motif_CL")) {
            getMenuInflater().inflate(R.menu.menu_chargm_motif_cl, menu);
        } else if (activity.equals("CollecteTraitmActivity")) {
            // getMenuInflater().inflate(R.menu.menu_qr_collecte, menu);}
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.CAMERA_PERMISSION_REQUEST_CODE
                && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.willStartCameraIntent();
        }
    }

    private boolean checkCameraPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, Constant.CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void willStartCameraIntent() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_dec)
            return deconnexion(this);

        else if (item.getItemId() == R.id.menu_item_photo) {
            if (this.checkCameraPermission()) {
                this.willStartCameraIntent();
            } else // request camera permission
                this.requestCameraPermission();

            return true;


        } else if (item.getItemId() == R.id.menu_item_chargm) {


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    FDR(context);
                }
            });
        } else if (item.getItemId() == R.id.menu_item_FDR) {


            Intent intent = new Intent(this, CreerCollecte.class);
            startActivity(intent);
            return true;
//HHA 09/06/2020

        } else if (item.getItemId() == R.id.menu_item_qr) {


            Intent intent = new Intent(this, QRcollecteliv.class);
            startActivity(intent);
            return true;
            //HHA 09/06/2020
        } else if (item.getItemId() == R.id.menu_chargm_cl) {


            Cursor cr = db.getnomclient();

// 22/04/2020
            // if (cr.getCount() == 0)

            //{

            if (registerInternetCheckReceiver(getApplication())) {


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
                    //     url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                    //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");

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


                if (store != null) {


                    /************web service pr recupérer les clients de la colecte******/


                    ODataProperty property1;
                    ODataPropMap properties1;


                    ODataResponseSingle resp1 = null;


                    try {
                        resp1 = store.executeReadEntitySet(
                                "ZSD_FDR_CLIENTSet", null);
                    } catch (ODataNetworkException e) {
                        e.printStackTrace();
                    } catch (ODataParserException e) {
                        e.printStackTrace();


                    } catch (ODataContractViolationException e) {
                        e.printStackTrace();

                    }
                    //Get the response payload


                    //Get the response payload


                    if (resp1 == null) {


                    } else {


                        ODataEntitySet feed1 = (ODataEntitySet) resp1.getPayload();
                        //Get the list of ODataEntity

                        entities1 = feed1.getEntities();
                        if (entities1.size() != 0) {


                            for (ODataEntity entity1 : entities1) {

                                properties1 = entity1.getProperties();
                                SQLiteHandler db = new SQLiteHandler(getApplication());

                                db.addData_client((String) properties1.get("Kunnr").getValue(),
                                        (String) properties1.get("Name").getValue(),
                                        (String) properties1.get("ZnameCt").getValue(),
                                        (String) properties1.get("AdrCl").getValue(),
                                        (String) properties1.get("ZtelCt").getValue(),
                                        (String) properties1.get("ZagenceId").getValue(), (String) properties1.get("ZagenceDes").getValue(), (String) properties1.get("Zregion").getValue(), (String) properties1.get("Zcode2d").getValue());

                            }


                            Intent intent = new Intent(getApplication(), CreerCollecte.class);
                            //  intent.putExtra("code",String.valueOf(obj));
                            startActivity(intent);
                            finish();


                        }


                    }


                }


            } else {
                Toast.makeText(getApplication(), "Connectez-vous à internet et réessayez", Toast.LENGTH_SHORT).show();

            }
            //}


        } else if (item.getItemId() == R.id.menu_chargm_motif_cl) {


            Cursor cr = db.getAllmotif_cl();


            if (cr.getCount() == 0) {

                if (registerInternetCheckReceiver(getApplication())) {


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
                        // url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                        //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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


                    if (store != null) {


                        /************web service pr recupérer les clients de la colecte******/


                        ODataProperty property1;
                        ODataPropMap properties1;


                        ODataResponseSingle resp1 = null;


                        try {
                            resp1 = store.executeReadEntitySet(
                                    "ZSD_FDR_MOTIFSet", null);
                        } catch (ODataNetworkException e) {
                            e.printStackTrace();
                        } catch (ODataParserException e) {
                            e.printStackTrace();


                        } catch (ODataContractViolationException e) {
                            e.printStackTrace();

                        }
                        //Get the response payload


                        //Get the response payload


                        if (resp1 == null) {


                        } else {


                            ODataEntitySet feed1 = (ODataEntitySet) resp1.getPayload();
                            //Get the list of ODataEntity

                            entities1 = feed1.getEntities();
                            if (entities1.size() != 0) {


                                for (ODataEntity entity1 : entities1) {

                                    properties1 = entity1.getProperties();
                                    SQLiteHandler db = new SQLiteHandler(getApplication());

                                    db.addmotif_cl((String) properties1.get("ZfdrMotif").getValue(),
                                            (String) properties1.get("ZfdrDesignation").getValue()
                                    );

                                }


                                Intent intent = new Intent(getApplication(), Motif_CL.class);
                                //  intent.putExtra("code",String.valueOf(obj));
                                startActivity(intent);
                                finish();


                            }


                        }


                    }


                } else {
                    Toast.makeText(getApplication(), "Connectez-vous à internet et réessayez", Toast.LENGTH_SHORT).show();

                }
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteAppData() {
        try {
            // clearing app data

            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            //restart(getApplicationContext(),0);


            runtime.exec("pm clear " + packageName);
            Intent intent = new Intent(getApplicationContext(), LogonActivity.class);

            startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restart(Context context, int delay) {
        if (delay == 0) {
            delay = 1;
        }
        Log.e("", "restarting app");
        Intent restartIntent = new Intent(getApplicationContext(), LogonActivity.class);


        PendingIntent intent = PendingIntent.getActivity(
                context, 0,
                restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(2);
    }

    public boolean registerInternetCheckReceiver(Context ctx) {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        String status = getConnectivityStatusString(ctx);
        a = setSnackbarMessage(status, false);
        ctx.registerReceiver(broadcastReceiver, internetFilter);
        return a;

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            if (!prevStatus.equals(status)) {
                a = setSnackbarMessage(status, false);
                //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                Log.e("Conn", status);
                prevStatus = status;
                if (a)
                    testNetworkSpeed();
            }

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

        db = new SQLiteHandler(this);
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

    protected boolean deconnexion(Context context) {
        TextView myMsg = new TextView(context);
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

        myMsg.setTextColor(Color.WHITE);
        myMsg.setText("Voulez vous vraiment fermer votre session");
        myMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 4);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(myMsg.getText().toString());


        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //deleteAppData();
                Intent intent = new Intent(getApplicationContext(), Deconnexion.class);

                startActivity(intent);
                session = new SessionManager(getApplicationContext());
                session.clearSession();
                db = new SQLiteHandler(getApplicationContext());
                db.deletediv();
                //(new SQLiteHandler(getApplicationContext())).deleteData2();

                restart(getApplicationContext(), 0);
            /*        Intent intent = new Intent(getApplicationContext(),LogonActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
            */

            }
        });

        builder.setNeutralButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //        Intent intent = new Intent(Acceuil1.this,Acceuil1.class);
                //        startActivity(intent);

            }
        });

        AlertDialog alert1 = builder.create();
        alert1.show();


        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        OnResume = true;
        registerInternetCheckReceiver(context);

    }

    @Override
    protected void onPause() {
        super.onPause();
        OnPause = true;
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (OnPause == true && OnResume == false) {
            Toast.makeText(this, "home", Toast.LENGTH_LONG).show();
            //Do Your Home press code Here.

        }

        OnPause = false;
        OnResume = false;

    }


    protected void testNetworkSpeed() {
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
                            Toast.makeText(getApplicationContext(), "Timed out", Toast.LENGTH_SHORT).show();
                        }
                    });
                    netSpeed = false;

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
                        //   Toast.makeText(getApplicationContext(),"Connecté",Toast.LENGTH_SHORT).show();
                        //  InsertBatch("");
                        //  Insertoffline();
                        Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();

                    }
                });
                netSpeed = true;
            }
        });

    }


    public void Notification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("un nouvelle DNL est crée");
        mBuilder.setContentText("Hello World!");
        // mBuilder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }

    public void envoiss() {


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
                            // url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                            // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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


                            if (resp == null) {
                                //Toast.makeText(getApplicationContext()," Vous ne pouvez pas charger DNL ,une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();

                                Log.e("Not", "0");


                            } else {

                                Log.e("Not", "1");

                                ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                                //Get the list of ODataEntity

                                entities = feed.getEntities();

                                if (entities.size() == 0) {

                                } else {
                                    int i = 1;
                                    for (ODataEntity entity : entities) {


                                        properties = entity.getProperties();

                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(getApplication());

                                        //Create the intent that’ll fire when the user taps the notification//

                                        Intent intent = new Intent(getApplicationContext(), AgencyListActivity1.class);
                                        startActivity(intent);
                                        // PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent, 0);

                                        // mBuilder.setContentIntent(pendingIntent);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            mBuilder.setColor(getApplication().getResources().getColor(R.color.blue));
                                            mBuilder.setSmallIcon(R.drawable.ic_action_not);
                                            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_launcher));
                                        }
                                        //mBuilder.setSmallIcon(R.mipmap.ic_launcher);


                                        mBuilder.setVibrate(new long[]{100, 250});
                                        mBuilder.setDefaults(0).setAutoCancel(true);


                                        mBuilder.setContentTitle("DNL NO" + (String) properties.get("Num_obj_dnl").getValue() + "est crée");
                                        mBuilder.setContentText((String) properties.get("code_envoi").getValue());
                                        // mBuilder.setPriority(Notification.PRIORITY_MAX);

                                        NotificationManager mNotificationManager =

                                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        mNotificationManager.notify(i++, mBuilder.build());


                                    }


                                }


                            }


                            Log.e("Not", "2");
                            // Notification();


                        }


                    }

                });

            }


        });

    }


    ////************************* List  des collectes de la FDR**************************////


    public static List<Collecte> getFDR(Context context) {

        return CollecteList;

    }


///////////////////////*********** web service charger FDR ******************////////////////


    public void FDR(final Context ctx) {


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
                            Toast.makeText(ctx, "Vérifiez votre connexion internet", Toast.LENGTH_SHORT).show();

                            session = new SessionManager(ctx);
                            login = session.getUsername();
                            pwd = session.getPassword();


                            db = new SQLiteHandler(ctx);


                            Cursor cr = db.getFDR(login);


                            if (cr.getCount() != 0) {


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
                                    cl.setHeuretr(cr.getString(15));

                                    CollecteList.add(cl);


                                }

                            }
                            Intent intent = new Intent(ctx, CollecteListActivity.class);
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
                        Toast.makeText(ctx, "Connecté", Toast.LENGTH_SHORT).show();

                        session = new SessionManager(ctx);
                        login = session.getUsername();
                        pwd = session.getPassword();

                        CredentialsProvider1 credProvider = CredentialsProvider1
                                .getInstance(lgtx, login, pwd);

                        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                                ctx).supportBasicAuthUsing(credProvider).configure(
                                new HttpConversationManager(ctx));


                        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
                        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx,
                                requestFilter);
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
                        //Method to open a new online store asynchronously


                        //Check if OnlineODataStore opened successfully
                        OnlineODataStore store = null;

                        try {
                            store = OnlineODataStore.open(ctx, url, manager, null);
                        } catch (ODataException e) {
                            e.printStackTrace();


                        }


                        if (store != null) {

                            ODataProperty property;
                            ODataPropMap properties;


                            ODataResponseSingle resp = null;
                            try {
                                resp = store.executeReadEntitySet(
                                        "ZSD_FDR_COLLECTESet", null);
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
                                Toast.makeText(ctx, " Vous ne pouvez pas charger la feuille de route ,une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ctx, CollecteListActivity.class);
                                //  intent.putExtra("code",String.valueOf(obj));
                                startActivity(intent);
                                finish();


                            } else {


                                ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                                //Get the list of ODataEntity

                                entities = feed.getEntities();
                                if (entities.size() != 0) {
                                    int i = 1;


                                    for (ODataEntity entity : entities) {

                                        properties = entity.getProperties();

                                        db = new SQLiteHandler(ctx);


                                        Log.e("kim", "1");
                                        /**** conversion date to string ******/
                                /*    GregorianCalendar date =  (GregorianCalendar)properties.get("ZdateEch").getValue();

                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                                    java.util.Date dateDate = date.getTime();

                                    String date_cl = dateFormat.format(dateDate);



                                                    /******************************************/


                                        //    ODataDurationDefaultImpl time = (ODataDurationDefaultImpl) (properties.get("ZheureEch").getValue());

                                        /*************changer le statut de la fDR*********/

                                        String ID = (String) properties.get("ZfdrId").getValue();
                                        String client = (String) properties.get("Kunnr").getValue();

                                        ODataEntity newEntity = null;
                                        try {
                                            newEntity = createAgencyEntityCL(store, ID, client);
                                        } catch (ODataParserException e) {
                                            e.printStackTrace();
                                        }


                                        try {
                                            store.executeUpdateEntity(newEntity, null);
                                        } catch (ODataNetworkException e) {
                                            e.printStackTrace();
                                        } catch (ODataParserException e) {
                                            e.printStackTrace();
                                        } catch (ODataContractViolationException e) {
                                            e.printStackTrace();
                                        }


                                        db.addData_FDR((String) properties.get("ZfdrId").getValue(),
                                                (String) properties.get("Zposnr").getValue(),
                                                client.replaceAll("^0+(?=.)", ""), (String) properties.get("Name").getValue(),
                                                (String) properties.get("ZnameCt").getValue(),
                                                (String) properties.get("AdrCl").getValue(),
                                                (String) properties.get("ZtelCt").getValue(),
                                                (String) properties.get("ZdateEch").getValue(),
                                                (String) (properties.get("ZheureEch").getValue()), session.getUsername(), "En cours", (String) properties.get("Agent").getValue(), (String) properties.get("Agence").getValue(), (String) properties.get("ZtypeCl").getValue(), "", (String) properties.get("Zcode2d").getValue(), "0", "", "");


                                        Cursor cr = db.getFDR(session.getUsername());


                                        Log.e("size", String.valueOf(cr.getCount()));
                                        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                            cl = new Collecte();
                                            cl.setNum_client(cr.getString(3));
                                            cl.setNum_fdr(cr.getString(1));
                                            cl.setNum_poste(cr.getString(2));
                                            cl.setNom_client(cr.getString(4));
                                            cl.setHeure(cr.getString(9));
                                            cl.setInterlocuteur(cr.getString(5));
                                            cl.setTelephone_client(cr.getString(6));
                                            cl.setAdresse_client(cr.getString(7));
                                            cl.setDate(cr.getString(8));
                                            cl.setStat(cr.getString(11));
                                            cl.setAgent(cr.getString(12));
                                            cl.setAgence(cr.getString(13));

                                            cl.setType_cl(cr.getString(14));
                                            cl.setHeuretr(cr.getString(15));
                                            cl.setCode_2D(cr.getString(16));


                                            CollecteList.add(cl);


                                        }


                                    }
                                }

                                /************web service pr recupérer les clients de la colecte******/


                           /*    ODataProperty property1;
                                ODataPropMap properties1;


                                ODataResponseSingle resp1 = null;
                                try {
                                    resp1 = store.executeReadEntitySet(
                                            "ZSD_FDR_CLIENTSet", null);
                                } catch (ODataNetworkException e) {
                                    e.printStackTrace();
                                } catch (ODataParserException e) {
                                    e.printStackTrace();


                                } catch (ODataContractViolationException e) {
                                    e.printStackTrace();

                                }
                                //Get the response payload


                                //Get the response payload


                                if (resp1 == null) {


                                } else {


                                    ODataEntitySet feed1 = (ODataEntitySet) resp1.getPayload();
                                    //Get the list of ODataEntity

                                    entities1 = feed1.getEntities();
                                    if (entities1.size() != 0) {


                                        for (ODataEntity entity1 : entities1) {

                                            properties1 = entity1.getProperties();


                                            db.addData_client((String) properties1.get("Kunnr").getValue(),
                                                    (String) properties1.get("Name").getValue(),
                                                    (String) properties1.get("ZnameCt").getValue(),
                                                    (String) properties1.get("AdrCl").getValue(),
                                                    (String) properties1.get("ZtelCt").getValue(),
                                                    (String) properties1.get("ZagenceId").getValue(), (String) properties1.get("ZagenceDes").getValue(), (String) properties1.get("Zregion").getValue(), (String) properties1.get("Zcode2d").getValue());

                                        }

                                    }


                                }*/


                            }


                        }
                        Intent intent = new Intent(ctx, CollecteListActivity.class);
                        //  intent.putExtra("code",String.valueOf(obj));

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                        finish();

                    }

                });


            }


        });

    }

    /////////////*************************Notification collecte NON programé**************************//////////////////

    public void FDR_notif() {

/*
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


                        }
                    });

                    //
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                mDeviceBandwidthSampler.stopSampling();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {*/


/*******************************************/

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
            //    url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
            //   url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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


        if (store != null) {

            ODataProperty property;
            ODataPropMap properties;


            ODataResponseSingle resp = null;
            try {
                resp = store.executeReadEntitySet(
                        "ZSD_FDR_COLLECTESet", null);
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


            } else {


                ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                //Get the list of ODataEntity

                entities = feed.getEntities();

                if (entities.size() != 0) {
                    int i = 1;
                    for (ODataEntity entity : entities) {

                        properties = entity.getProperties();

                        db = new SQLiteHandler(getApplicationContext());


                        Log.e("kim", "1");

                        String type_cl = (String) properties.get("ZtypeCl").getValue();
                        if (type_cl.equalsIgnoreCase("D")) {


                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication());


                            Intent intentAction = new Intent(getApplicationContext(), ActionReceiver.class);

//This is optional if you have more than one buttons and want to differentiate between two
                            intentAction.putExtra("action", "actionName");

                            PendingIntent pIntentlogin = PendingIntent.getBroadcast(getApplicationContext(), 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplication())

                                    .setColor(getApplication().getResources().getColor(R.color.blue))
                                    .setSmallIcon(R.drawable.ic_action_not)
                                    .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_launcher))


                                    .setContentTitle("Vous avez une nouvelle collecte")
                                    .setContentText((String) properties.get("Name").getValue())

                                    .setVibrate(new long[]{100, 250})
                                    .setAutoCancel(true)
                                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))


                                    //Using this action button I would like to call logTest
                                    .addAction(R.drawable.ic_action_synn, "Charger FDR", pIntentlogin)
                                    .setOngoing(true);

                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            mNotificationManager.notify(i++, mBuilder.build());
                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                        }

                    }


                }

            }

        }

    }

    //  });

    //  }


    // });


    private static ODataEntity createAgencyEntityCL(OnlineODataStore store, String ID, String num_client) throws ODataParserException {
        //BEGIN
        Log.e("1", "1");
        ODataEntity newEntity = null;
        if (store != null) {

            newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ZSD_FDR_COLLECTE");

            try {
                store.allocateProperties(newEntity, ODataStore.PropMode.All);
            } catch (ODataException e) {
                e.printStackTrace();
            }
            //If available, it populates the navigation properties of an OData Entity
            store.allocateNavigationProperties(newEntity);
            Log.e("2", "2");
            /* newEntity.getProperties().put("ZnumObj",new ODataPropertyDefaultImpl("ZnumObj",0000000655));*/

            newEntity.getProperties().put("ZfdrId", new ODataPropertyDefaultImpl("ZfdrId", ID));


            //String resourcePath =("ZSD_FDR_COLLECTESet('" + ID + "')");

            String resourcePath = ("ZSD_FDR_COLLECTESet(Kunnr='" + num_client + "',ZheureEch='" + "',ZfdrId='" + ID + "')");


            newEntity.setResourcePath(resourcePath, resourcePath);


        }
        return newEntity;
        //END

    }


    /*************Traitement de la collecte*********/


    public static void InsertENVOI(final Context ctx, Collecte CL, List<Envoi> list, String heure, String motifID, String designation, Activity ac, String statut_cl) {


        db = new SQLiteHandler(ctx);

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
            url = new URL(url_g);
            // url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
            //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ZSD_FDR_ENVOIS");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("ZSD_FDR_ENVOISSet");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");

        if (list == null) {

            newEntity.getProperties().put("ZfdrId", new ODataPropertyDefaultImpl("ZfdrId", CL.getNum_fdr()));
            newEntity.getProperties().put("Zposnr", new ODataPropertyDefaultImpl("Zposnr", CL.getNum_poste()));
            newEntity.getProperties().put("Kunnr", new ODataPropertyDefaultImpl("Kunnr", CL.getNum_client()));
            newEntity.getProperties().put("ZcodeEnvoi", new ODataPropertyDefaultImpl("ZcodeEnvoi", ""));
            newEntity.getProperties().put("ZheureTr", new ODataPropertyDefaultImpl("ZheureTr", heure));
            newEntity.getProperties().put("ZnbEnvois", new ODataPropertyDefaultImpl("ZnbEnvois", ""));
            newEntity.getProperties().put("ZmotifAutres", new ODataPropertyDefaultImpl("ZmotifAutres", designation));
            newEntity.getProperties().put("Zstatut_cl", new ODataPropertyDefaultImpl("Zstatut_cl", statut_cl));


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


        } else {
            for (int i = 0; i < list.size(); i++) {


                newEntity.getProperties().put("ZfdrId", new ODataPropertyDefaultImpl("ZfdrId", CL.getNum_fdr()));
                newEntity.getProperties().put("Zposnr", new ODataPropertyDefaultImpl("Zposnr", CL.getNum_poste()));
                newEntity.getProperties().put("Kunnr", new ODataPropertyDefaultImpl("Kunnr", CL.getNum_client()));
                newEntity.getProperties().put("ZcodeEnvoi", new ODataPropertyDefaultImpl("ZcodeEnvoi", list.get(i).getCode_envoi()));
                newEntity.getProperties().put("ZheureTr", new ODataPropertyDefaultImpl("ZheureTr", heure));
                newEntity.getProperties().put("ZnbEnvois", new ODataPropertyDefaultImpl("ZnbEnvois", String.valueOf(list.size())));
                newEntity.getProperties().put("ZmotifAutres", new ODataPropertyDefaultImpl("ZmotifAutres", ""));
                newEntity.getProperties().put("Zstatut_cl", new ODataPropertyDefaultImpl("Zstatut_cl", statut_cl));

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
                            Toast.makeText(ctx, "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();


                            if ((!designation.isEmpty()) && list == null) {
                                db.updateStatut(CL.getNum_client(), "Non Traité", heure, CL.getNum_fdr());
                            } else if (designation.isEmpty() && list.size() != 0) {
                                db.updateStatut(CL.getNum_client(), "Traité", heure, CL.getNum_fdr());

                            }

                            Intent intent = new Intent();
                            intent.setClass(ctx, CollecteListActivity.class);
                            ac.startActivity(intent);

                            ac.finish();


                        } else {
                            Toast.makeText(ctx, "une Erreur est survenue ", Toast.LENGTH_SHORT).show();


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

    /*****************************************************************************/


    public static void InsertENVOI1(final Context ctx, Collecte CL, String heure, String motifID, String designation, String nbenvoi, Context ac) {


        db = new SQLiteHandler(ctx);

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
            url = new URL(url_g);
            //   url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
            //    url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ZSD_FDR_ENVOIS");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("ZSD_FDR_ENVOISSet");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");


        newEntity.getProperties().put("ZfdrId", new ODataPropertyDefaultImpl("ZfdrId", CL.getNum_fdr()));
        newEntity.getProperties().put("Zposnr", new ODataPropertyDefaultImpl("Zposnr", CL.getNum_poste()));
        newEntity.getProperties().put("Kunnr", new ODataPropertyDefaultImpl("Kunnr", CL.getNum_client()));
        newEntity.getProperties().put("ZcodeEnvoi", new ODataPropertyDefaultImpl("ZcodeEnvoi", ""));
        newEntity.getProperties().put("ZheureTr", new ODataPropertyDefaultImpl("ZheureTr", heure));
        newEntity.getProperties().put("ZnbEnvois", new ODataPropertyDefaultImpl("ZnbEnvois", nbenvoi));
        newEntity.getProperties().put("ZmotifAutres", new ODataPropertyDefaultImpl("ZmotifAutres", designation));
//MODIF 27/01/2021{
        newEntity.getProperties().put("Zstatut_cl", new ODataPropertyDefaultImpl("Zstatut_cl", "T"));
//MODIF 27/01/2021}

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
                            Toast.makeText(ctx, "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();


                            db.updateStatut(CL.getNum_client(), "Traité", heure, CL.getNum_fdr());


                            Intent intent = new Intent();
                            intent.setClass(ctx, CollecteListActivity.class);
                            ac.startActivity(intent);


                        } else {
                            Toast.makeText(ctx, "une Erreur est survenue ", Toast.LENGTH_SHORT).show();


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

    /********************* insert off line*************************/


    public void Insertoffline() {


        db = new SQLiteHandler(getApplicationContext());


                /*db = new SQLiteHandler(getApplicationContext());

        Cursor cursor = db.getAll2();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            login = cursor.getString(1);
            pwd = cursor.getString(2);

        }
        */
        session = new SessionManager(getApplicationContext());
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

        ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ZSD_FDR_ENVOIS");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("ZSD_FDR_ENVOISSet");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");

        cursor = db.getCL_OFFLINE(session.getUsername());

        if (cursor.getCount() != 0) {


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {


                newEntity.getProperties().put("ZfdrId", new ODataPropertyDefaultImpl("ZfdrId", cursor.getString(1)));
                newEntity.getProperties().put("Zposnr", new ODataPropertyDefaultImpl("Zposnr", cursor.getString(2)));
                newEntity.getProperties().put("Kunnr", new ODataPropertyDefaultImpl("Kunnr", cursor.getString(3)));
                newEntity.getProperties().put("ZcodeEnvoi", new ODataPropertyDefaultImpl("ZcodeEnvoi", cursor.getString(18)));
                newEntity.getProperties().put("ZnbEnvois", new ODataPropertyDefaultImpl("ZnbEnvois", cursor.getString(19)));
                newEntity.getProperties().put("ZheureTr", new ODataPropertyDefaultImpl("ZheureTr", cursor.getString(15)));
                newEntity.getProperties().put("ZmotifAutres", new ODataPropertyDefaultImpl("ZmotifAutres", cursor.getString(17)));

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


                            if (code.equalsIgnoreCase("201")) {
                                db.delete_offline_CL(session.getUsername());


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


        }


    }


    /************** web service pour chargement des clients de la collecte ***************/////
    public void Client_collecte() {


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
                            //     url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                            // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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


                        if (store != null) {


                            /************web service pr recupérer les clients de la colecte******/


                            ODataProperty property1;
                            ODataPropMap properties1;


                            ODataResponseSingle resp1 = null;


                            try {
                                resp1 = store.executeReadEntitySet(
                                        "ZSD_FDR_CLIENTSet", null);
                            } catch (ODataNetworkException e) {
                                e.printStackTrace();
                            } catch (ODataParserException e) {
                                e.printStackTrace();


                            } catch (ODataContractViolationException e) {
                                e.printStackTrace();

                            }
                            //Get the response payload


                            //Get the response payload


                            if (resp1 == null) {


                            } else {


                                ODataEntitySet feed1 = (ODataEntitySet) resp1.getPayload();
                                //Get the list of ODataEntity

                                entities1 = feed1.getEntities();
                                if (entities1.size() != 0) {


                                    for (ODataEntity entity1 : entities1) {

                                        properties1 = entity1.getProperties();
                                        SQLiteHandler db = new SQLiteHandler(getApplication());

                                        db.addData_client((String) properties1.get("Kunnr").getValue(),
                                                (String) properties1.get("Name").getValue(),
                                                (String) properties1.get("ZnameCt").getValue(),
                                                (String) properties1.get("AdrCl").getValue(),
                                                (String) properties1.get("ZtelCt").getValue(),
                                                (String) properties1.get("ZagenceId").getValue(), (String) properties1.get("ZagenceDes").getValue(), (String) properties1.get("Zregion").getValue(), (String) properties1.get("Zcode2d").getValue());

                                    }


                                }


                            }


                            /*******************************/


                            try {
                                resp1 = store.executeReadEntitySet(
                                        "ZSD_FDR_MOTIFSet", null);
                            } catch (ODataNetworkException e) {
                                e.printStackTrace();
                            } catch (ODataParserException e) {
                                e.printStackTrace();


                            } catch (ODataContractViolationException e) {
                                e.printStackTrace();

                            }
                            //Get the response payload


                            //Get the response payload


                            if (resp1 == null) {


                            } else {


                                ODataEntitySet feed1 = (ODataEntitySet) resp1.getPayload();
                                //Get the list of ODataEntity

                                entities1 = feed1.getEntities();
                                if (entities1.size() != 0) {


                                    for (ODataEntity entity1 : entities1) {

                                        properties1 = entity1.getProperties();
                                        SQLiteHandler db = new SQLiteHandler(getApplication());

                                        db.addmotif_cl((String) properties1.get("ZfdrMotif").getValue(),
                                                (String) properties1.get("ZfdrDesignation").getValue()
                                        );

                                    }


                                }


                            }


                        }

                    }

                });


            }


        });

    }


}
//}


