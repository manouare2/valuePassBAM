package com.example.karim.applicationfacteur.ui.online;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.TraceLog;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.OnlineODataStoreException;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResultEnvoi;
import com.example.karim.applicationfacteur.types.AsyncResultFDR;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.types.Envoi;
import com.example.karim.applicationfacteur.ui.main.QRcollecte;
import com.example.karim.applicationfacteur.ui.main.QRcollecte1;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.online.AgencyActivity1;
import com.example.karim.applicationfacteur.ui.online.CollecteActivity;
import com.example.karim.applicationfacteur.ui.online.CollecteAsyncLoader;
import com.example.karim.applicationfacteur.ui.online.CollecteListAdapter;
import com.example.karim.applicationfacteur.ui.online.CollecteListFragment;
import com.example.karim.applicationfacteur.ui.online.QRAsyncloader;
import com.example.karim.applicationfacteur.ui.online.QRCollectelistadapter;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.impl.ODataEntityDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataStore;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hanane on 29/03/2019.
 */

public class QRcollectelistfragment extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResultEnvoi<List<Envoi>>>, UIListener {


    private View myView;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private SQLiteHandler db;
    String text = "";
    Collecte CL;

    static List<Envoi> clts;


    static ArrayList<String> list = new ArrayList<String>();
    String login , pwd;
    int total;
    protected ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    protected ConnectionClassManager mConnectionClassManager;
    protected DeviceBandwidthSampler mDeviceBandwidthSampler;
    protected int mTries;
    Button btn,btn1;
    LogonCoreContext lgtx;
    List<ODataEntity> entities ,entities1,entities2,entities3;
    final ArrayList<String> envoi_list = new ArrayList<String>();


    protected SessionManager session;
    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    private boolean netSpeed;
    Context context ;
    String num_obj,num_obj1;

    String activity = this.getClass().getSimpleName();
    String prevStatus = "";
    Cursor cursor;

    UIListener ui= this;



    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }

    public TextView childViewAgenciesTitle,Total,agent,agence,code2D;
    private ListView childViewAgenciesList;
    private LayoutInflater inflater;
    private Loader<AsyncResultEnvoi<List<Envoi>>> loader;
    private Envoi envoi;

    String code1;
    CollecteListFragment frag;
    private EditText searchBarcode;
    private QRCollectelistadapter entropsAdapter;
    private QRCollectListadaptermoins enmoinsAdapter;
    private Button showSearch;
    List<Envoi> listentrops,listenmoins;
    ListView entropView, enmoinsView;
    ToggleButton btnEntrop, btnenmoins;
    String code,val1;


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //  final String  sep = bundle.getString("sep");


        session = new SessionManager(getActivity());

        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

        this.inflater = inflater;

        db=new SQLiteHandler(getActivity());


        if (myView == null) {


            final ArrayList<Envoi> myList = (ArrayList<Envoi>) getActivity().getIntent().getSerializableExtra("myList");

         //   final String str[] = (String[]) getActivity().getIntent().getSerializableExtra("str");
                       clts=myList;


            myView = this.inflater.inflate(R.layout.qrcollectelist, null);




            btnEntrop  = (ToggleButton) myView.findViewById(R.id.toggleButton);
            btnenmoins = (ToggleButton) myView.findViewById(R.id.toggleButton2);


            listentrops = new ArrayList<>();
            listenmoins = new ArrayList<>();


            entropView    = (ListView) myView.findViewById(R.id.en_cours_list);
            btn           = (Button) myView.findViewById(R.id.ok);
            enmoinsView   = (ListView) myView.findViewById(R.id.liv_list);

            loader = getLoaderManager().initLoader(0, savedInstanceState, this);



            btnEntrop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        entropView.setVisibility(View.VISIBLE);
                        enmoinsView.setVisibility(View.GONE);
                        btnenmoins.setChecked(false);

                    }
                    else {
                        entropView.setVisibility(View.GONE);
                    }
                }
            });

            btnenmoins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        entropView.setVisibility(View.GONE);
                        enmoinsView.setVisibility(View.VISIBLE);

                        btnEntrop.setChecked(false);
                    }
                    else {
                        enmoinsView.setVisibility(View.GONE);
                    }
                }
            });






        }




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = getActivity().getIntent().getExtras();
                if (bundle != null) {
                 CL = bundle.getParcelable("CollecteSelected");

                }


                Intent intent = new Intent();

                intent.putExtra("CollecteSelected",CL);
                intent.setClass(getActivity(),CollecteTraitmActivity.class);

                startActivity(intent);
                getActivity().finish();


            }
        });



        return myView;
    }


    @Override
    public void onResume() {
        super.onResume();




        entropsAdapter = new QRCollectelistadapter(this,entropView, inflater,listentrops,getActivity());


        entropView.setAdapter(entropsAdapter);




        enmoinsAdapter = new QRCollectListadaptermoins(this,enmoinsView, inflater, listenmoins,getActivity());

        enmoinsView.setAdapter(enmoinsAdapter);







        changeToggleText();
        btnEntrop.setChecked(true);
        btnenmoins.setChecked(false);



    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        // refresh();

    }



    public void onSaveRequested(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AgencyActivity1.class);

        startActivity(intent);
        getActivity().finish();

    }



    public void onFDRSelected(final Envoi cl){
        Intent intent = new Intent();
        intent.setClass(getActivity(), CollecteActivity.class);
        intent.putExtra("CollecteSelected",cl);
        intent.putExtra("siz",String.valueOf(clts.size()));
        startActivity(intent);
        getActivity().finish();


    }

    public void onRefreshRequested(){
        refresh();

    }

    @Override
    public Loader<AsyncResultEnvoi<List<Envoi>>> onCreateLoader(int id, Bundle args) {
        return new QRAsyncloader((getActivity()));

    }

    @Override
    public void onLoadFinished(Loader<AsyncResultEnvoi<List<Envoi>>> listLoader,
                               AsyncResultEnvoi<List<Envoi>> result) {
      /*  if (result.getException() != null || result.getData() == null) {
            Toast.makeText(getActivity(), "une erreur est survenue. veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();
            TraceLog.e("Error loading agencies", result.getException());

        } else {
            clts = result.getData();


            for(Envoi cl: clts) {


                    listentrops.add(cl);



            }

*/

        final String str[] = (String[]) getActivity().getIntent().getSerializableExtra("str");

        final ArrayList<String> list  =  new ArrayList<String>();




        for (int i = 0;i<str.length;i++) {

            list.add(str[i]);

        }






        for (int i = 0;i<clts.size();i++) {

           envoi_list.add(clts.get(i).getCode_envoi());

        }



        for (int i = 0;i<str.length;i++) {


          if (!envoi_list.contains(str[i])) {


              Envoi envoi = new Envoi(str[i]);

             listenmoins.add(envoi);


          }


      }



        for (int i = 0;i<envoi_list.size();i++) {


            if (!list.contains(envoi_list.get(i))) {


                Envoi envoi = new Envoi(envoi_list.get(i));

                listentrops.add(envoi);



            }


        }



            onResume();


        //refresh();


    }



    @Override
    public void onLoaderReset(Loader<AsyncResultEnvoi<List<Envoi>>> listLoader) {

        //refresh();

    }



    public void refresh() {
        loader.forceLoad();


    }
    public void onPause() {
        super.onPause();
        // refresh();

        //getLoaderManager().getLoader(0).forceLoad();

    }


    @Override
    public void onRequestError(int operation, Exception e) {
        Toast.makeText(getActivity(),getString(R.string.err_odata_unexpected, e.getMessage()),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestSuccess(int operation, String key) {

        String message;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //  refresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // refresh();
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // refresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        // refresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // refresh();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }


    public void changeToggleText() {

       // total = listEncours.size()+listTrait.size()+listNonTrait.size();

        btnEntrop.setTextOff("Colis en Trop:  "+listentrops.size());
        btnEntrop.setTextOn("Colis en Trop: "+listentrops.size());
        btnenmoins.setTextOff("Colis en moins: "+listenmoins.size());
        btnenmoins.setTextOn("Colis en moins: "+listenmoins.size());
       /* btnNonLivres.setTextOff("Non Traités "+listNonTrait.size()+"/"+total);
        btnNonLivres.setTextOn("Non Traités "+listNonTrait.size()+"/"+total);*/
//



    }



    public static List<Envoi> getenvois(Context context)  {

            return clts;



    }












}
