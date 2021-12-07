package com.example.karim.applicationfacteur.ui.main;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.online.OnlineODataStore;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by karim on 12/06/2018.
 */

public class LogonActivity extends myActivity{


    private final String TAG = LogonActivity.class.getSimpleName();

    private Button logonBtn;
    private EditText hostEdit;
    private EditText portEdit;
    private static EditText usernameEdit;
    private EditText passwordEdit;
    private ProgressDialog progressDialog;
    private String appConnId;
    private LogonCore lgCore;
    String login,pwd;
    boolean islogin = false;
    LogonCoreContext lgCtx = null;
    OnlineODataStore store = null;
    CredentialsProvider1 credProvider;
    CookieSyncManager cm;
    HttpConversationManager manager;
    IReceiveEvent rcv;
    ISendEvent sen;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private View myView;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected=true;
    private  boolean a=false;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);




        logonBtn = (Button) findViewById(R.id.btn);
        usernameEdit = (EditText) findViewById(R.id.txt1);
        passwordEdit = (EditText) findViewById(R.id.txt2);
        usernameEdit.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        passwordEdit.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
       if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getApplicationContext(),Acceuil1.class);
            startActivity(intent);
            finish();


        }

        logonBtn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {



                login = usernameEdit.getText().toString().trim();


                pwd = passwordEdit.getText().toString().trim();



                // Check for empty data in the form
                if (!login.isEmpty() && !pwd.isEmpty()) {

                    if(registerInternetCheckReceiver()) {

                        URL url = null;
                        try {
                            url = new URL(url_g);
                         // url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                         //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        credProvider = CredentialsProvider1.getInstance(lgCtx, login, pwd);



                        manager = new CommonAuthFlowsConfigurator(getApplicationContext()).supportBasicAuthUsing(credProvider).configure(new HttpConversationManager(getApplicationContext()));



                        try {
                            // credProvider = CredentialsProvider1.getInstance(lgCtx,);
                            store = OnlineODataStore.open(getApplicationContext(),url, manager, null);





                        } catch (ODataException e) {
                            e.printStackTrace();
                        }



                        if (store!=null) {
                            //db.addData2(login, pwd);
                            session.setLogin(true);
                            session.setUsername(login);
                            session.setPassword(pwd);

                            Toast.makeText(getApplicationContext(),
                                    "Bienvenue!", Toast.LENGTH_LONG)
                                    .show();

                           /* Intent intent = new Intent(LogonActivity.this,
                                    Acceuil1.class);*/
                            Intent intent = new Intent(getApplicationContext(),Acceuil1.class);
                            startActivity(intent);
                            finish();




                        } else {


                           /* deleteAppData();

                            Intent intent = new Intent(LogonActivity.this,LogonActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            System.exit(0);*/
                            Toast.makeText(getApplicationContext(),"nom d'utilisateur ou mot de passe incorrecte, réessayez",Toast.LENGTH_SHORT).show();
                            usernameEdit.setText("");
                            passwordEdit.setText("");



                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Connectez-vous à internet et réessayez",Toast.LENGTH_SHORT).show();


                    }





                }

                else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Entrez le nom d'utilisateur et le mot de passe", Toast.LENGTH_LONG)
                            .show();
                }




            }


        });



    }


    private boolean registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
        String status = getConnectivityStatusString(this);
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



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            finish();
        }

        return false;
        // Disable back button..............
    }


}

