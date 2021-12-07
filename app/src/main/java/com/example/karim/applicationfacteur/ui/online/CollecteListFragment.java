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
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.karim.applicationfacteur.types.Agency;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResult1;
import com.example.karim.applicationfacteur.types.AsyncResultFDR;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.types.Envoi;
import com.example.karim.applicationfacteur.ui.main.Acceuil1;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.myActivity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.karim.applicationfacteur.ui.main.MainCL.url_g;

public class CollecteListFragment extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResultFDR<List<Collecte>>>, UIListener {
    private View myView;



    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private SQLiteHandler db;
    String text = "";

    static List<Collecte> clts;
    static List<Agency3> searchResult;
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
    private Loader<AsyncResultFDR<List<Collecte>>> loader;
    private Collecte cl;

    String code1;
    CollecteListFragment frag;
    private EditText searchBarcode;
    private CollecteListAdapter encoursAdapter,livresAdapter,nonlivresAdapter;
    private Button showSearch;
    List<Collecte> listEncours,listTrait,listNonTrait;
    ListView encoursView, livresView, nonlivresView;
    ToggleButton btnEncours, btnLivres, btnNonLivres;
    String code,val1;
    ODataResponse oDataResponse = null;
    String nbenvoi ;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         nbenvoi = getActivity().getIntent().getStringExtra("nbenvoi");
        Toast.makeText(getActivity(), "tt"+nbenvoi , Toast.LENGTH_SHORT).show();
        session = new SessionManager(getActivity());

        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

        this.inflater = inflater;

        db=new SQLiteHandler(getActivity());


        if (myView == null) {



            myView = this.inflater.inflate(R.layout.collecte_list_fragment, null);
            childViewAgenciesTitle = (TextView) myView.findViewById(R.id.agencies_title);
            Total = (TextView) myView.findViewById(R.id.Total);
            agent = (TextView) myView.findViewById(R.id.Agent);
            agence = (TextView) myView.findViewById(R.id.Agence);
            searchBarcode = (EditText) myView.findViewById(R.id.search_barcode);
            showSearch = (Button) myView.findViewById(R.id.btn_show_search);
            btn= (Button) myView.findViewById(R.id.jrne);



            btnEncours = (ToggleButton) myView.findViewById(R.id.toggleButton);
            btnLivres = (ToggleButton) myView.findViewById(R.id.toggleButton2);
            btnNonLivres = (ToggleButton) myView.findViewById(R.id.toggleButton3);

            listEncours = new ArrayList<>();
            listTrait = new ArrayList<>();
            listNonTrait = new ArrayList<>();

            encoursView = (ListView) myView.findViewById(R.id.en_cours_list);
            livresView = (ListView) myView.findViewById(R.id.liv_list);
            nonlivresView = (ListView) myView.findViewById(R.id.non_liv_list);







            loader = getLoaderManager().initLoader(0, savedInstanceState, this);



            btnEncours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        encoursView.setVisibility(View.VISIBLE);
                        livresView.setVisibility(View.GONE);
                        nonlivresView.setVisibility(View.GONE);

                        btnNonLivres.setChecked(false);
                        btnLivres.setChecked(false);
                    }
                    else {
                        encoursView.setVisibility(View.GONE);
                    }
                }
            });

            btnLivres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        encoursView.setVisibility(View.GONE);
                        livresView.setVisibility(View.VISIBLE);
                        nonlivresView.setVisibility(View.GONE);

                        btnNonLivres.setChecked(false);
                        btnEncours.setChecked(false);
                    }
                    else {
                        livresView.setVisibility(View.GONE);
                    }
                }
            });

            btnNonLivres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        encoursView.setVisibility(View.GONE);
                        livresView.setVisibility(View.GONE);
                        nonlivresView.setVisibility(View.VISIBLE);

                        btnEncours.setChecked(false);
                        btnLivres.setChecked(false);
                    }
                    else {
                        nonlivresView.setVisibility(View.GONE);
                    }



                   Timer timer = new Timer ();
                    TimerTask hourlyTask = new TimerTask () {
                        @Override
                        public void run () {

                         //   String  time = hour+minute ;

                            for (int i = 0 ; i <listEncours.size();i++)
                            {
                             //   if(listEncours.get(i).getHeure().compareToIgnoreCase(time)<0)
                                {



                                }

                            }


                        }

                    };


                    timer.schedule (hourlyTask, 0l, 10*1*60);









                }
            });




            btn.setOnClickListener(new View.OnClickListener() {



                public void onClick(View view) {




                    db = new SQLiteHandler(getActivity());

                    cursor = db.getAll3(session.getUsername());
                    if (cursor.getCount() != 0)

                    {

                      //  InsertBatch(cursor);


                    }

                    else
                    {




                        db = new SQLiteHandler(getActivity());

                        final ArrayList<String> selectedList1 = new ArrayList<>();



                        Cursor cursor1 = db.getALLFDR(new SessionManager(getActivity()).getUsername());
                        if(cursor1.getCount()==0)
                        {
                            Toast.makeText(getActivity(),"aucune FDR à cloturer",Toast.LENGTH_SHORT).show();

                        }

                        else {

                            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                selectedList1.add(cursor1.getString(0));


                            }


                            final String[] items = new String[selectedList1.size()];

                            for (int i = 0; i < selectedList1.size(); i++) {
                                items[i] = selectedList1.get(i);
                                        //replaceAll("^0+", "");

                            }

                            final ArrayList<Integer> selectedList = new ArrayList<>();

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle("la liste des FDR");
                            builder.setMultiChoiceItems(items, null,
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                            if (isChecked) {
                                                selectedList.add(which);
                                            } else if (selectedList.contains(which)) {
                                                selectedList.remove(which);
                                            }
                                        }
                                    });

                            builder.setPositiveButton("cloturer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ArrayList<String> selectedStrings = new ArrayList<>();
                                    ArrayList<String> clotur = new ArrayList<>();
                                    ArrayList<String> nclotur = new ArrayList<>();

                                    for (int j = 0; j < selectedList.size(); j++) {
                                        selectedStrings.add(items[selectedList.get(j)]);
                                    }

                                    for (int k = 0; k < selectedStrings.size(); k++) {

                                        Cursor cr = db.getAllIDFDR(selectedStrings.get(k));


                                        if (cr.getCount() == 0) {



                                            String val = selectedStrings.get(k) + "";
                                            String str = val;

                                            str = str.replace(" ", "");

                                            if(registerInternetCheckReceiver())
                                            {

                                               cloturerFDR(getActivity(),val);


                                                Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(),"Vérifiez votre connexion internet puis réssayer",Toast.LENGTH_SHORT).show();
                                            }





                                            clotur.add(selectedStrings.get(k));


                                        } else {
                                            nclotur.add(selectedStrings.get(k));
                                            Toast.makeText(getActivity(), "traitez les envois encours puis cloturer", Toast.LENGTH_LONG).show();

                                        }


                                    }





                                }
                            });

                            builder.show();







                        }



                    }




















                }


            });







        }

        searchBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                encoursAdapter.getFilter().filter(charSequence.toString());
                livresAdapter.getFilter().filter(charSequence.toString());
                nonlivresAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        showSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchBarcode.getVisibility() == View.GONE) {
                    childViewAgenciesTitle.setVisibility(View.GONE);
                    searchBarcode.setVisibility(View.VISIBLE);
                    searchBarcode.setSelected(true);
                }
                else {
                    searchBarcode.setVisibility(View.GONE);
                    childViewAgenciesTitle.setVisibility(View.VISIBLE);
                    searchBarcode.setText("");
                }
            }
        });




        return myView;
    }


    @Override
    public void onResume() {
        super.onResume();

        Calendar calandrier=Calendar.getInstance();
        final String  hour = String.valueOf(calandrier.get(Calendar.HOUR_OF_DAY));
        final String minute = String.valueOf(calandrier.get(Calendar.MINUTE));


        final String  time = hour+minute ;


        Collections.sort(listEncours,new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Collecte p1 = (Collecte) o1;
                Collecte p2 = (Collecte) o2;
                return p1.getHeure().compareToIgnoreCase(p2.getHeure());
            }
        });



        encoursAdapter = new CollecteListAdapter(this,encoursView, inflater, listEncours,getActivity());
   /*   {

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

               // TextView ListItemShow = (TextView) view.findViewById(android.R.id.client);




                        for (int i = 0 ; i <listEncours.size();i++) {
                            if (listEncours.get(i).getHeure().compareToIgnoreCase(time) < 0) {


                                encoursView.getAdapter().getView(i,view, parent).findViewById(R.id.client).setBackgroundColor(Color.RED);


                            }

                            else if (listEncours.get(i).getHeure().compareToIgnoreCase(time) >=0) {


                                encoursView.getAdapter().getView(i,view, parent).findViewById(R.id.client).setBackgroundColor(Color.BLUE);





                            }


                        }

                return view;
            }

        };
*/

        encoursView.setAdapter(encoursAdapter);


        Collections.sort(listTrait,new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Collecte p1 = (Collecte) o1;
                Collecte p2 = (Collecte) o2;
                return p2.getHeure().compareToIgnoreCase(p1.getHeure());
            }
        });

        livresAdapter = new CollecteListAdapter(this,livresView, inflater, listTrait,getActivity());
        livresView.setAdapter(livresAdapter);



        Collections.sort(listNonTrait,new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Collecte p1 = (Collecte) o1;
                Collecte p2 = (Collecte) o2;
                return p2.getHeure().compareToIgnoreCase(p1.getHeure());
            }
        });
        nonlivresAdapter = new CollecteListAdapter(this,nonlivresView, inflater, listNonTrait,getActivity());
        nonlivresView.setAdapter(nonlivresAdapter);

        changeToggleText();
        btnEncours.setChecked(true);
        btnLivres.setChecked(false);
        btnNonLivres.setChecked(false);










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



    public void onFDRSelected(final Collecte cl){
        Intent intent = new Intent();
        intent.setClass(getActivity(), CollecteActivity.class);
        intent.putExtra("CollecteSelected",cl);
        intent.putExtra("siz",String.valueOf(clts.size()));
        intent.putExtra("nbenvoi",nbenvoi);

        startActivity(intent);
        getActivity().finish();


    }

    public void onRefreshRequested(){
        refresh();

    }

    @Override
    public Loader<AsyncResultFDR<List<Collecte>>> onCreateLoader(int id, Bundle args) {
        return new CollecteAsyncLoader((getActivity()));

    }

    @Override
    public void onLoadFinished(Loader<AsyncResultFDR<List<Collecte>>> listLoader,
                               AsyncResultFDR<List<Collecte>> result) {
        if (result.getException() != null || result.getData() == null) {
            Toast.makeText(getActivity(), "une erreur est survenue. veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();
            TraceLog.e("Error loading agencies", result.getException());

        } else {
            clts = result.getData();


            for(Collecte cl: clts) {



                if (cl.getStat().equalsIgnoreCase("En cours")) {
                    listEncours.add(cl);
                } else if (cl.getStat().equalsIgnoreCase("Traité")) {
                    listTrait.add(cl);
                    list.clear();
                } else {
                    listNonTrait.add(cl);
                    list.clear();
                }


                agence.setText("Agence : " +cl.getAgence());
                agent.setText("Agent: Mr "+cl.getAgent());


            }





            onResume();
        }

        //refresh();


    }



    @Override
    public void onLoaderReset(Loader<AsyncResultFDR<List<Collecte>>> listLoader) {

        //refresh();

    }

    /**
     * Refresh the data displayed in the view.
     */
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
        Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected, e.getMessage()),
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

        total = listEncours.size()+listTrait.size()+listNonTrait.size();

        btnEncours.setTextOff("En cours "+listEncours.size()+"/"+total);
        btnEncours.setTextOn("En cours "+listEncours.size()+"/"+total);
        btnLivres.setTextOff("Traités "+listTrait.size()+"/"+total);
        btnLivres.setTextOn("Traités "+listTrait.size()+"/"+total);
        btnNonLivres.setTextOff("Non Traités "+listNonTrait.size()+"/"+total);
        btnNonLivres.setTextOn("Non Traités "+listNonTrait.size()+"/"+total);

        Total.setText("Total visites : "+ String.valueOf(total));


    }






    public boolean registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        String status = getConnectivityStatusString(getActivity());
        a = setSnackbarMessage(status, false);
        getActivity().registerReceiver(broadcastReceiver, internetFilter);
        return a;

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            if(!prevStatus.equals(status)) {
                a = setSnackbarMessage(status, false);
                //   Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();
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

        // db = new SQLiteHandler(this);
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



    private void testNetworkSpeed() {
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Timed out",Toast.LENGTH_SHORT).show();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Connecté",Toast.LENGTH_SHORT).show();
                        // InsertBatch("");

                    }
                });
                netSpeed = true;
            }
        });

    }





    /*********** cloturer FDR*******************/


    private static ODataEntity createAgencyEntityCL(OnlineODataStore store, String ID) throws ODataParserException {
        //BEGIN
        Log.e("1","1");
        ODataEntity newEntity = null;
        if (store!=null) {

            newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ZSD_FDR_HD");

            try {
                store.allocateProperties(newEntity, ODataStore.PropMode.All);
            } catch (ODataException e) {
                e.printStackTrace();
            }
            //If available, it populates the navigation properties of an OData Entity
            store.allocateNavigationProperties(newEntity);
            Log.e("2","2");
           /* newEntity.getProperties().put("ZnumObj",new ODataPropertyDefaultImpl("ZnumObj",0000000655));*/

            newEntity.getProperties().put("ZfdrId",new ODataPropertyDefaultImpl("ZfdrId",ID));





            //String resourcePath =("ZSD_FDR_COLLECTESet('" + ID + "')");

            String resourcePath =("ZSD_FDR_HDSet(ZfdrId='" + ID + "')");



            newEntity.setResourcePath(resourcePath, resourcePath);


        }
        return newEntity;
        //END

    }







    public void cloturerFDR(final Context ctx, final String ID)
    {
        session = new SessionManager(ctx);
        login = session.getUsername();
        pwd = session.getPassword();



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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Verifiez votre connexion internet",Toast.LENGTH_SHORT).show();

                        }
                    });

                    //
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                mDeviceBandwidthSampler.stopSampling();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(getActivity(),"Connecté",Toast.LENGTH_SHORT).show();
*/
                        CredentialsProvider1 credProvider = CredentialsProvider1
                                .getInstance(lgtx,login,pwd);

                        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                                ctx).supportBasicAuthUsing(credProvider).configure(
                                new HttpConversationManager(ctx));


                        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
                        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx,requestFilter);
                        manager.addFilter(requestFilter);
                        manager.addFilter(responseFilter);
                        URL url = null;


                        try {
                            url = new URL(url_g);
                        //    url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                           // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();

                        }
                        OnlineODataStore store = null;


                        try {
                            store = OnlineODataStore.open(ctx, url, manager, null);

                        } catch (ODataException e) {
                            e.printStackTrace();
                            Toast.makeText(ctx,"une erreur est surevnue réessayer plus tard",Toast.LENGTH_SHORT).show();
                            Log.e("e0",e.toString());
                        }

                        // if (store==null) return;

                        if(store!=null)

                        {

                            try {

                                /**********************************************************************/

                                cursor = db.getCL_OFFLINE(session.getUsername());

                                if (cursor.getCount() != 0) {
                                ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
                                ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ZSD_FDR_ENVOIS");

                                ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

                                batchItem.setResourcePath("ZSD_FDR_ENVOISSet");


                                batchItem.setMode(ODataRequestParamSingle.Mode.Create);

                                batchItem.setCustomTag("something to identify the request");




                                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                                        newEntity.getProperties().put("ZfdrId", new ODataPropertyDefaultImpl("ZfdrId", cursor.getString(1)));
                                        newEntity.getProperties().put("Zposnr", new ODataPropertyDefaultImpl("Zposnr", cursor.getString(2)));
                                        newEntity.getProperties().put("Kunnr", new ODataPropertyDefaultImpl("Kunnr", cursor.getString(3)));
                                        newEntity.getProperties().put("ZcodeEnvoi", new ODataPropertyDefaultImpl("ZcodeEnvoi", cursor.getString(18)));
                                        newEntity.getProperties().put("ZnbEnvois", new ODataPropertyDefaultImpl("ZnbEnvois",cursor.getString(19)));
                                        newEntity.getProperties().put("ZheureTr", new ODataPropertyDefaultImpl("ZheureTr",cursor.getString(15)));
                                        newEntity.getProperties().put("ZmotifAutres", new ODataPropertyDefaultImpl("ZmotifAutres",cursor.getString(17)));

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

                                                        db.deleteoffline(session.getUsername());

                                                        /********cloturer la journée*******/


                                                        newEntity = createAgencyEntityCL(store, ID);


                                                        store.executeUpdateEntity(newEntity, null);

                                                        Cursor cursorr = db.getID(ID);


                                                        if (listTrait.size() == 0) {

                                                        } else {


                                                            Log.e("test1", "test1");


                                                            for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {
                                                                for (int m = 0; m < listTrait.size(); m++) {


                                                                    if (listTrait.get(m).getNum_client().equalsIgnoreCase(cursorr.getString(0)))


                                                                    {


                                                                        Log.e("test", "test");

                                                                        listTrait.remove(listTrait.get(m));
                                                                        changeToggleText();
                                                                        onResume();

                                                                    }

                                                                }

                                                            }


                                                        }


                                                        if (listNonTrait.size() == 0) {

                                                        } else {


                                                            for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {

                                                                for (int n = 0; n < listNonTrait.size(); n++) {

                                                                    if (listNonTrait.get(n).getNum_client().equalsIgnoreCase(cursorr.getString(0)))


                                                                    {

                                                                        listNonTrait.remove(listNonTrait.get(n));
                                                                        changeToggleText();
                                                                        onResume();
                                                                    }

                                                                }

                                                            }


                                                        }

                                                        db.deleteIDCL(ID);
                                                        db.deleteenvois(ID);
                                                        db.delete_offline_CL(session.getUsername());



                                                        SharedPreferences sp = getActivity().getSharedPreferences("ID_CL", Context.MODE_PRIVATE);


                                                        String ID_FDR  = String.valueOf(sp.getString("ID", null));

                                                        if (ID_FDR.equalsIgnoreCase(ID)) {
                                                            SharedPreferences.Editor editor = sp.edit();
                                                            editor.clear();
                                                            editor.commit();

                                                        }










/*********************/
                                                    } else {
                                                        Toast.makeText(getActivity(), "une Erreur est survenue ", Toast.LENGTH_SHORT).show();


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


                                    }
                                }


                                else
                                {





                                    ODataEntity  newEntity = createAgencyEntityCL(store, ID);


                                    store.executeUpdateEntity(newEntity, null);

                                    Cursor cursorr = db.getID(ID);


                                    if (listTrait.size() == 0) {

                                    } else {


                                        Log.e("test1", "test1");


                                        for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {
                                            for (int m = 0; m < listTrait.size(); m++) {


                                                if (listTrait.get(m).getNum_client().equalsIgnoreCase(cursorr.getString(0)))


                                                {


                                                    Log.e("test", "test");

                                                    listTrait.remove(listTrait.get(m));
                                                    changeToggleText();
                                                    onResume();

                                                }

                                            }

                                        }


                                    }


                                    if (listNonTrait.size() == 0) {

                                    } else {


                                        for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {

                                            for (int n = 0; n < listNonTrait.size(); n++) {

                                                if (listNonTrait.get(n).getNum_client().equalsIgnoreCase(cursorr.getString(0)))


                                                {

                                                    listNonTrait.remove(listNonTrait.get(n));
                                                    changeToggleText();
                                                    onResume();
                                                }

                                            }

                                        }


                                    }

                                    db.deleteIDCL(ID);
                                    db.deleteenvois(ID);
                                    db.deleteobject(session.getUsername());


                                   /* SharedPreferences sp = getActivity().getSharedPreferences("ID_CL", Context.MODE_PRIVATE);


                                    String ID_FDR  = String.valueOf(sp.getString("ID", null));

                                    if (ID_FDR.equalsIgnoreCase(ID)) {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.clear();
                                        editor.commit();

                                    }

*/







                                }











                                    /**************************************************/




                              /*  Cursor cursor1 = db.getobj(obj);
                                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                    db.deletecode(cursor1.getString(0));
                                }
                                db.deleteData3();
*/

                            } catch (Exception e) {
                                //  Toast.makeText(ctx, "une erreur est surevnue réessayer plus tard", Toast.LENGTH_SHORT).show();

                                try {
                                    throw new OnlineODataStoreException(e);
                                } catch (OnlineODataStoreException e1) {
                                    e1.printStackTrace();
                                    // Toast.makeText(ctx, "une erreur est surevnue réessayer plus tard", Toast.LENGTH_SHORT).show();

                                }
                            }
                            //END

                        }


                        else
                        {

                         //   Toast.makeText(getActivity(), "DNL № "+obj+"n'est pas cloturée,une erreur est surevnue réssayer plus tard", Toast.LENGTH_SHORT).show();

                        }





             //       }

                //});

       // }



       // });


    }









}
