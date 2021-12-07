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
import com.example.karim.applicationfacteur.types.Agency;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResult1;
import com.example.karim.applicationfacteur.ui.main.Livraison_entrep;
import com.example.karim.applicationfacteur.ui.main.Raison1;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.SignatureActivity;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.karim.applicationfacteur.ui.main.myActivity.url_g;


public class AgencyListFragment1 extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResult1<List<Agency3>>>, UIListener {
    private View myView;



    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private SQLiteHandler db;
    String text = "";

    static List<Agency3> agencies;
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
    String str ;

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

    public  TextView childViewAgenciesTitle;
    private ListView childViewAgenciesList;
    private LayoutInflater inflater;
    private Loader<AsyncResult1<List<Agency3>>> loader;
    private  Agency agency;
    private  Agency1 s;
    private  Agency3 agc;
    String code1;
    AgencyListFragment1 frag;
    private EditText searchBarcode;
    private AgencyListAdapter1 encoursAdapter,livresAdapter,nonlivresAdapter,aviseAdapter;
    private Button showSearch;
    List<Agency3> listEncours,listLivres,listNonlivres,listavisé;
    ListView encoursView, livresView, nonlivresView,aviseVieuw;
    ToggleButton btnEncours, btnLivres, btnNonLivres,btnavisé;
    String code,val1;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

        this.inflater = inflater;

        db=new SQLiteHandler(getActivity());

        session = new SessionManager(getActivity());

        motif();
        if (myView == null) {





            myView = this.inflater.inflate(R.layout.agency_list_fragment, null);
            childViewAgenciesTitle = (TextView) myView.findViewById(R.id.agencies_title);
            searchBarcode = (EditText) myView.findViewById(R.id.search_barcode);
            showSearch = (Button) myView.findViewById(R.id.btn_show_search);
            btn= (Button) myView.findViewById(R.id.jrne);


            btnEncours = (ToggleButton) myView.findViewById(R.id.toggleButton);
            btnLivres = (ToggleButton) myView.findViewById(R.id.toggleButton2);
            btnNonLivres = (ToggleButton) myView.findViewById(R.id.toggleButton3);
            btnavisé = (ToggleButton) myView.findViewById(R.id.toggleButton4);

            listEncours = new ArrayList<>();
            listLivres = new ArrayList<>();
            listNonlivres = new ArrayList<>();
            listavisé = new ArrayList<>();


            encoursView = (ListView) myView.findViewById(R.id.en_cours_list);
            livresView = (ListView) myView.findViewById(R.id.liv_list);
            nonlivresView = (ListView) myView.findViewById(R.id.non_liv_list);
            aviseVieuw = (ListView) myView.findViewById(R.id.avisé);




            loader = getLoaderManager().initLoader(0, savedInstanceState, this);

            btnEncours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        encoursView.setVisibility(View.VISIBLE);
                        livresView.setVisibility(View.GONE);
                        nonlivresView.setVisibility(View.GONE);
                        aviseVieuw.setVisibility(View.GONE);

                        btnNonLivres.setChecked(false);
                        btnLivres.setChecked(false);
                        btnavisé.setChecked(false);
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
                        aviseVieuw.setVisibility(View.GONE);

                        btnNonLivres.setChecked(false);
                        btnEncours.setChecked(false);
                        btnavisé.setChecked(false);
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
                        aviseVieuw.setVisibility(View.GONE);
                        nonlivresView.setVisibility(View.VISIBLE);

                        btnEncours.setChecked(false);
                        btnLivres.setChecked(false);
                        btnavisé.setChecked(false);
                    }
                    else {
                        nonlivresView.setVisibility(View.GONE);
                    }
                }
            });


            btnavisé.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        encoursView.setVisibility(View.GONE);
                        livresView.setVisibility(View.GONE);
                        nonlivresView.setVisibility(View.GONE);
                        aviseVieuw.setVisibility(View.VISIBLE);

                        btnEncours.setChecked(false);
                        btnLivres.setChecked(false);
                        btnNonLivres.setChecked(false);
                    }
                    else {
                        encoursView.setVisibility(View.GONE);
                    }
                }
            });










         /*   String val=val1+"";
            str = val;

            str= str.replace(" " , "");*/


        /*    Bundle bundle = getArguments();
            if (bundle != null) {
              code=bundle.getString("code");
                Toast.makeText(getActivity(),code+"code",Toast.LENGTH_SHORT).show();

            }

*/
            btn.setOnClickListener(new View.OnClickListener() {



                public void onClick(View view) {

                    db = new SQLiteHandler(getActivity());
                    cursor = db.getAll3(session.getUsername());
                    if (cursor.getCount() != 0)

                    {

                        InsertBatch(cursor);


                    }

                    else
                    {




                        db = new SQLiteHandler(getActivity());

                        final ArrayList<String> selectedList1 = new ArrayList<>();



                        Cursor cursor1 = db.getAlldist(new SessionManager(getActivity()).getUsername());
                        if(cursor1.getCount()==0)
                        {
                            Toast.makeText(getActivity(),"aucune DNL à cloturer",Toast.LENGTH_SHORT).show();

                        }

                        else {

                            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                selectedList1.add(cursor1.getString(0));


                            }


                            final String[] items = new String[selectedList1.size()];

                            for (int i = 0; i < selectedList1.size(); i++) {
                                items[i] = selectedList1.get(i);
                            }

                            final ArrayList<Integer> selectedList = new ArrayList<>();

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle("la liste des DNLs");
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

                                        Cursor cr = db.getAllobj(selectedStrings.get(k));


                                        if (cr.getCount() == 0) {



                                            String val = selectedStrings.get(k) + "";
                                             str = val;

                                            str = str.replace(" ", "");

                                            if(registerInternetCheckReceiver())
                                            {
                                                //cloturerjrne(getActivity(), str);
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

                                                                CredentialsProvider1 credProvider = CredentialsProvider1
                                                                        .getInstance(lgtx,login,pwd);

                                                                HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                                                                        getContext()).supportBasicAuthUsing(credProvider).configure(
                                                                        new HttpConversationManager(getContext()));


                                                                XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
                                                                XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getContext(),requestFilter);
                                                                manager.addFilter(requestFilter);
                                                                manager.addFilter(responseFilter);
                                                                URL url = null;


                                                                try {
                                                                 //   url = new URL(url_g);
                                                                    url = new URL("http://testpda.barid.ma/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                                                                   // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                                                                } catch (MalformedURLException e) {
                                                                    e.printStackTrace();

                                                                }
                                                                OnlineODataStore store = null;


                                                                try {
                                                                    store = OnlineODataStore.open(getContext(), url, manager, null);

                                                                } catch (ODataException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getContext(),"une erreur est surevnue réessayer plus tard",Toast.LENGTH_SHORT).show();
                                                                    Log.e("e0",e.toString());
                                                                }

                                                                // if (store==null) return;

                                                                if(store!=null)

                                                                {

                                                                    try {

                                                                        ODataEntity newEntity = createAgencyEntity2(store,str);


                                                                        store.executeUpdateEntity(newEntity, null);

                                                                        Cursor cursorr = db.getobj(str);

                                                                        Log.e("t0", "t0");
                                                                        if (listLivres.size() == 0) {
                                                                            Log.e("t1", "t1");
                                                                        } else {


                                                                            Log.e("test1", String.valueOf(cursorr.getCount()));


                                                                            for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {
                                                                                for (int m = 0; m < listLivres.size(); m++) {

                                                                                    Log.e("t2",cursorr.getString(0).toString() );
                                                                                    Log.e("t3",listLivres.get(m).getCode_envoi().toString() );

                                                                                    if (listLivres.get(m).getCode_envoi().equalsIgnoreCase(cursorr.getString(0)))


                                                                                    {
                                                                                        Log.e("test", "test");

                                                                                        listLivres.remove(listLivres.get(m));
                                                                                        changeToggleText();
                                                                                        onResume();

                                                                                    }

                                                                                }

                                                                            }


                                                                        }


                                                                        if (listNonlivres.size() == 0) {

                                                                        } else {


                                                                            for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {

                                                                                for (int n = 0; n < listNonlivres.size(); n++) {

                                                                                    if (listNonlivres.get(n).getCode_envoi().equalsIgnoreCase(cursorr.getString(0)))


                                                                                    {

                                                                                        listNonlivres.remove(listNonlivres.get(n));
                                                                                        changeToggleText();
                                                                                        onResume();
                                                                                    }

                                                                                }

                                                                            }


                                                                        }


                                                                        if (listavisé.size() == 0) {

                                                                        } else {


                                                                            for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {

                                                                                for (int n = 0; n < listavisé.size(); n++) {

                                                                                    if (listavisé.get(n).getCode_envoi().equalsIgnoreCase(cursorr.getString(0)))


                                                                                    {

                                                                                        listavisé.remove(listavisé.get(n));
                                                                                        changeToggleText();
                                                                                        onResume();
                                                                                    }

                                                                                }

                                                                            }


                                                                        }



                                                                        db.deleteobj(str);
                                                                        db.deletepere1(str);
                                                                        db.deleteobject_dnl(session.getUsername());

                                                                        SharedPreferences sp = getActivity().getSharedPreferences("num_obj", Context.MODE_PRIVATE);


                                                                        String obj1 = String.valueOf(sp.getString("obj", null));

                                                                        if (obj1.equalsIgnoreCase(str)) {
                                                                            SharedPreferences.Editor editor = sp.edit();
                                                                            editor.clear();
                                                                            editor.commit();

                                                                        }


                                                                        Cursor cursor1 = db.getobj(str);
                                                                        for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                                                            db.deletecode(cursor1.getString(0));
                                                                        }
                                                                        db.deleteData3();


                                                                    } catch (Exception e) {
                                                                        //  Toast.makeText(ctx, "une erreur est surevnue réessayer plus tard", Toast.LENGTH_SHORT).show();

                                                                        try {
                                                                            throw new OnlineODataStoreException(e);
                                                                        } catch (OnlineODataStoreException e1) {
                                                                            e1.printStackTrace();


                                                                        }
                                                                    }


                                                                }


                                                                else
                                                                {

                                                                    Toast.makeText(getActivity(), "DNL № "+str+"n'est pas cloturée,une erreur est surevnue réssayer plus tard", Toast.LENGTH_SHORT).show();

                                                                }





                                                            }

                                                        });

                                                    }



                                                });

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



          /*  btn1.setOnClickListener(new View.OnClickListener() {



                public void onClick(View view) {

                    if(registerInternetCheckReceiver())
                    {
                        SharedPreferences sp = getActivity().getSharedPreferences("objj", Context.MODE_PRIVATE);
                        String code1 = sp.getString("obj",null);
                        Toast.makeText(getActivity(),"obj"+code1,Toast.LENGTH_SHORT).show();
                        chargerDNL(getActivity(),code1);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), AgencyListActivity1.class);

                        startActivity(intent);
                        getActivity().finish();

                    }
                    else
                    {

                        Toast.makeText(getActivity(), "Connectez vous à internet puis réessayer", Toast.LENGTH_SHORT).show();
                    }
*














                }


            });*/





        }

        searchBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // recherche
               livresAdapter.getFilter().filter(charSequence.toString());
                nonlivresAdapter.getFilter().filter(charSequence.toString());
                aviseAdapter.getFilter().filter(charSequence.toString());
                encoursAdapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        showSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encoursView.setVisibility(View.VISIBLE);
                livresView.setVisibility(View.VISIBLE);
                nonlivresView.setVisibility(View.VISIBLE);
              //  aviseVieuw.setVisibility(View.);
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


        Collections.sort(listEncours, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Agency3 p1 = (Agency3) o1;
                Agency3 p2 = (Agency3) o2;
                return p1.getObj1().compareToIgnoreCase(p2.getObj1());
            }
        });

        encoursAdapter = new AgencyListAdapter1(this,encoursView, inflater, listEncours,getActivity());
        encoursView.setAdapter(encoursAdapter);
        livresAdapter = new AgencyListAdapter1(this,livresView, inflater, listLivres,getActivity());
        livresView.setAdapter(livresAdapter);
        nonlivresAdapter = new AgencyListAdapter1(this,nonlivresView, inflater, listNonlivres,getActivity());
        nonlivresView.setAdapter(nonlivresAdapter);
        aviseAdapter = new AgencyListAdapter1(this,aviseVieuw, inflater, listavisé,getActivity());
        aviseVieuw.setAdapter(aviseAdapter);

        changeToggleText();
        btnEncours.setChecked(true);
        btnLivres.setChecked(false);
        btnNonLivres.setChecked(false);
        btnavisé.setChecked(false);





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



    public void onAgencySelected(final Agency3 agency){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AgencyActivity1.class);
        intent.putExtra("AgencySelected", agency);
        intent.putExtra("siz",String.valueOf(agencies.size()));
        // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
        startActivity(intent);
        getActivity().finish();

     /*   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Commande :"+ agency.getCode_envoi());
        builder.setPositiveButton("Livraison validé", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if( registerInternetCheckReceiver())
                {

                    InsertBatch2(agency.getCode_envoi(),"HHAROUY","LIVRAISON","liv","ZSD_STAT","E0010","E0006","");
                    Toast.makeText(getActivity(),"con",Toast.LENGTH_LONG).show();
                }



                else {

                    Toast.makeText(getActivity(),"décon",Toast.LENGTH_LONG).show();

                    db = new SQLiteHandler(getActivity());

                    Cursor cursor1 = db.getAll3();
                    for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                        if (cursor1.getString(1) == agency.getCode_envoi()) {
                            code1 = cursor1.getString(1);
                        }

                    }


                    a = db.updateData(agency.getCode_envoi(), "hharouy", "LIVRAISON", "LIV", "ZSD_STAT", "E0006", "E0010", "");


                    if (a = true) {


                        Toast.makeText(getActivity(), "votre commande est bien validée", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(getActivity(), SignatureActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("code", agency.getCode_envoi());
                        intent.putExtras(bundle);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getActivity(), "Une erreur est survenue,veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();

                    }

                }
                }





        });
        builder.setNeutralButton("Livraison non valide  ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                db = new SQLiteHandler(getActivity());

                Cursor cursor1 = db.getAll3();
                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                    if(cursor1.getString(1)==agency.getCode_envoi())
                    {
                        code1=cursor1.getString(1);
                    }

                }



                a= db.updateData(agency.getCode_envoi(),login, "AFFECTE GUICHET", "affg", "ZSD_STAT", "E0006", "E0004","");





                    if(a=true) {

                        Toast.makeText(getActivity(), "votre commande n'est pas validée,veuillez remplir la raison", Toast.LENGTH_SHORT).show();




                        Intent intent = new Intent(getActivity(),Raison.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("code",agency.getCode_envoi());
                        intent.putExtras(bundle);
                        startActivity(intent);





                    }

                    else
                    {
                        Toast.makeText(getActivity(), "Une erreur est survenue,veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();

                    }



            }
        });


        AlertDialog alert1 = builder.create();
        alert1.show();*/
    }

    public void onRefreshRequested(){
        refresh();

    }

    @Override
    public Loader<AsyncResult1<List<Agency3>>> onCreateLoader(int id, Bundle args) {
        return new AgencyListDataAsyncLoader1(getActivity());

    }

    @Override
    public void onLoadFinished(Loader<AsyncResult1<List<Agency3>>> listLoader,
                               AsyncResult1<List<Agency3>> result) {
        if (result.getException() != null || result.getData() == null) {
            Toast.makeText(getActivity(), "une erreur est survenue. veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();
            TraceLog.e("Error loading agencies", result.getException());

        } else {
            agencies = result.getData();

            for(Agency3 agc: agencies) {
                if (agc.getStat().equalsIgnoreCase("En cours")) {
                    if (list.size() == 0) {
                        agc.setObj(agc.getObj1());
                        list.add(agc.getObj());
                    }

                    else{
                        for(int i =0;i<list.size();i++)
                        {

                            SharedPreferences sp = getActivity().getSharedPreferences("ob", Context.MODE_PRIVATE);
                            String obj  = sp.getString("ob1", null);
                            if(obj==null)
                            {

                            }
                            else {

                                obj  = String.valueOf(sp.getString("ob1", null));

                                String val=obj+"";
                                String  str = val;
                                str= str.replace(" " , "");
                                if(agc.getObj().equalsIgnoreCase(str))
                                {    agc.setObj("");

                                }
                                else{

                                    agc.setObj(str);
                                }




                            }



                        }

                    }

                    listEncours.add(agc);
                } else if (agc.getStat().equalsIgnoreCase("livré")) {
                    listLivres.add(agc);
                    list.clear();
                } else if (agc.getStat().equalsIgnoreCase("Avisé")) {
                    listavisé.add(agc);
                    list.clear();
                }else
                {
                    listNonlivres.add(agc);
                    list.clear();
                }

            }



            onResume();
        }

        //refresh();


    }



    @Override
    public void onLoaderReset(Loader<AsyncResult1<List<Agency3>>> listLoader) {

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

    /* public  void InsertBatch()

     {
         db = new SQLiteHandler(getActivity());

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
                 getActivity()).supportBasicAuthUsing(credProvider).configure(
                 new HttpConversationManager(getActivity()));


         XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
         XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getActivity(), requestFilter);
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
             store = OnlineODataStore.open(getActivity(), url, manager, null);
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
             Toast.makeText(getActivity(),"aucun envoi n'est touvé",Toast.LENGTH_SHORT).show();

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


             db.deleteData3();


         }


     }



     public  void InsertBatch2(String code1, String facteur, String txt30,String txt04,String stsma,String statprec,String stat,String raison)

     {
         db = new SQLiteHandler(getActivity());

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
                 getActivity()).supportBasicAuthUsing(credProvider).configure(
                 new HttpConversationManager(getActivity()));


         XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
         XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getActivity(), requestFilter);
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
             store = OnlineODataStore.open(getActivity(), url, manager, null);
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




                 newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre",code1));
                 newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur",facteur));
                 newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30",txt30));
                 newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04",txt04));

                 newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", stsma));
                 newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec",statprec));

                 newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", stat));
                 newEntity.getProperties().put("Raison", new ODataPropertyDefaultImpl("Raison",raison));


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


            // db.deleteData3();


     }








     public void offlineInsert() {
         Agency3 agen;
         db = new SQLiteHandler(getActivity());
         Cursor cursor=db.getAll2();

         for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

             login= cursor.getString(1);
             pwd= cursor.getString(2);

         }


         Iterator<Agency3> it = agencies.iterator();

         while (it.hasNext()) {
             agen = it.next();
            boolean a = db.addData3(agen.getCode_envoi(),login,"AFFECTE FACTEUR","aff","ZSD_STAT","","E0006","");
             if(a)
             {
                 Toast.makeText(getActivity(), "commandes affectées" , Toast.LENGTH_SHORT).show();

             }
             else
             {
                 Toast.makeText(getActivity(), "Une erreur est survenue,veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();

             }



         }

     }
 */
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // refresh();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //refresh();
    }
    /* public static List<Agency1> getAgencies1(Context context)  {
        Cursor cursor1=db.getAll3();

        for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

            code1= cursor1.getString(1);

        }
        agc = new Agency1((String)code1);
        agencyList.add(agc);


        return  agencyList;
    }*/

    public void changeToggleText() {

        total = listEncours.size()+listLivres.size()+listNonlivres.size()+listavisé.size();

        btnEncours.setTextOff("En cours "+listEncours.size()+"/"+total);
        btnEncours.setTextOn("En cours "+listEncours.size()+"/"+total);
        btnLivres.setTextOff("Livrés "+listLivres.size()+"/"+total);
        btnLivres.setTextOn("Livrés "+listLivres.size()+"/"+total);
        btnNonLivres.setTextOff("Non livrés "+listNonlivres.size()+"/"+total);
        btnNonLivres.setTextOn("Non livrés "+listNonlivres.size()+"/"+total);
        btnavisé.setTextOff("Avisé "+listavisé.size()+"/"+total);
        btnavisé.setTextOn("Avisé "+listavisé.size()+"/"+total);
    }

    public void InsertBatch(Cursor cursor)

    {

        /*db = new SQLiteHandler(getApplicationContext());

        Cursor cursor = db.getAll2();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            login = cursor.getString(1);
            pwd = cursor.getString(2);

        }
        */
        session = new SessionManager(getContext());
        login = session.getUsername();
        pwd = session.getPassword();
        Agency1 agen = null;
        ODataResponse oDataResponse = null;

        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx, login, pwd);

        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                getContext()).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(getContext()));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getContext(), requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
           url = new URL("http://testpda.barid.ma/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
           // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        OnlineODataStore store = null;


        try {
            store = OnlineODataStore.open(getContext(), url, manager, null);
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
                newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement",cursor.getString(10)));
                newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid",cursor.getString(11)));
                newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid", cursor.getString(12)));
                newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire", cursor.getString(13)));
                newEntity.getProperties().put("ModeLiv", new ODataPropertyDefaultImpl("ModeLiv", cursor.getString(14)));


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
                                Toast.makeText(getActivity(), "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();


                                db = new SQLiteHandler(getActivity());

                                final ArrayList<String> selectedList1 = new ArrayList<>();



                                Cursor cursor1 = db.getAlldist(new SessionManager(getActivity()).getUsername());
                                if(cursor1.getCount()==0)
                                {
                                    Toast.makeText(getActivity(),"aucune DNL à cloturer",Toast.LENGTH_SHORT).show();

                                }

                                else {

                                    for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                        selectedList1.add(cursor1.getString(0));


                                    }


                                    final String[] items = new String[selectedList1.size()];

                                    for (int i = 0; i < selectedList1.size(); i++) {
                                        items[i] = selectedList1.get(i);
                                    }

                                    final ArrayList<Integer> selectedList = new ArrayList<>();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                    builder.setTitle("la liste des DNLs");
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

                                                Cursor cr = db.getAllobj(selectedStrings.get(k));


                                                if (cr.getCount() == 0) {



                                                    String val = selectedStrings.get(k) + "";
                                                    String str = val;

                                                    str = str.replace(" ", "");

                                                    if(registerInternetCheckReceiver())
                                                    {
                                                        cloturerjrne(getActivity(), str);
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


            else {


                Toast.makeText(getActivity(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();


            }

        }

    }



   /* private void cloturer() {
        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mTries = 0;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.youtube.com/")
                .build();

        mDeviceBandwidthSampler.onSampling();
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

                    //
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
               getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InsertBatch("Clic");
                    }
                });

            }
        });
    }*/


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




    private static ODataEntity createAgencyEntity3(OnlineODataStore store, String code1) throws ODataParserException {
        //BEGIN
        Log.e("1","1");
        ODataEntity newEntity = null;
        if (store!=null) {

            newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.DNLHEAD");

            try {
                store.allocateProperties(newEntity, ODataStore.PropMode.All);
            } catch (ODataException e) {
                e.printStackTrace();
            }
            //If available, it populates the navigation properties of an OData Entity
            store.allocateNavigationProperties(newEntity);
            Log.e("2","2");
           /* newEntity.getProperties().put("ZnumObj",new ODataPropertyDefaultImpl("ZnumObj",0000000655));*/

            newEntity.getProperties().put("StatutTournee",new ODataPropertyDefaultImpl("StatutTournee","C"));





            String resourcePath =("DNLHEADSet('" + code1 + "')");

            newEntity.setResourcePath(resourcePath, resourcePath);


        }
        return newEntity;
        //END

    }


    private static ODataEntity createAgencyEntity2(OnlineODataStore store, String code1) throws ODataParserException {
        //BEGIN
        Log.e("1","1");
        ODataEntity newEntity = null;
        if (store!=null) {

            newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.DNLHEAD");

            try {
                store.allocateProperties(newEntity, ODataStore.PropMode.All);
            } catch (ODataException e) {
                e.printStackTrace();
            }
            //If available, it populates the navigation properties of an OData Entity
            store.allocateNavigationProperties(newEntity);
            Log.e("2","2");
           /* newEntity.getProperties().put("ZnumObj",new ODataPropertyDefaultImpl("ZnumObj",0000000655));*/

            newEntity.getProperties().put("StatutTournee",new ODataPropertyDefaultImpl("StatutTournee","T"));





            String resourcePath =("DNLHEADSet('" + code1 + "')");

            newEntity.setResourcePath(resourcePath, resourcePath);


        }
        return newEntity;
        //END

    }










    public void chargerDNL(Context ctx,String code1)
    {
      /*  session = new SessionManager(ctx);
        login = session.getUsername();
        pwd = session.getPassword();
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
            url = new URL("http://testpda.barid.ma/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
           // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        OnlineODataStore store = null;


        try {
            store = OnlineODataStore.open(ctx, url, manager, null);
        } catch (ODataException e) {
            e.printStackTrace();
            Log.e("e0",e.toString());
        }

        if (store==null) return;
        try {

            ODataEntity newEntity = createAgencyEntity3(store,code1);

            store.executeUpdateEntity(newEntity, null);
            // Toast.makeText(getActivity(),"Votre DNL est chargée",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Toast.makeText(ctx,"une erreur est surevnue réessayer plus tard",Toast.LENGTH_SHORT).show();

            try {
                throw new OnlineODataStoreException(e);
            } catch (OnlineODataStoreException e1) {
                //  Toast.makeText(ctx,"une erreur est surevnue réessayer plus tard",Toast.LENGTH_SHORT).show();


            }
        }
        //END


    }


    public void cloturerjrne(final Context ctx, final String obj)
    {

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
                            url = new URL("http://testpda.barid.ma/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                          //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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

                                ODataEntity newEntity = createAgencyEntity2(store, obj);


                                store.executeUpdateEntity(newEntity, null);

                                Cursor cursorr = db.getobj(obj);

                                Log.e("t0", "t0");
                                if (listLivres.size() == 0) {
                                    Log.e("t1", "t1");
                                } else {


                                    Log.e("test1", String.valueOf(cursorr.getCount()));


                                    for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {
                                        for (int m = 0; m < listLivres.size(); m++) {

                                            Log.e("t2",cursorr.getString(0).toString() );
                                            Log.e("t3",listLivres.get(m).getCode_envoi().toString() );

                                            if (listLivres.get(m).getCode_envoi().equalsIgnoreCase(cursorr.getString(0)))


                                            {
                                                Log.e("test", "test");

                                                listLivres.remove(listLivres.get(m));
                                                changeToggleText();
                                                onResume();

                                            }

                                        }

                                    }


                                }


                                if (listNonlivres.size() == 0) {

                                } else {


                                    for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {

                                        for (int n = 0; n < listNonlivres.size(); n++) {

                                            if (listNonlivres.get(n).getCode_envoi().equalsIgnoreCase(cursorr.getString(0)))


                                            {

                                                listNonlivres.remove(listNonlivres.get(n));
                                                changeToggleText();
                                                onResume();
                                            }

                                        }

                                    }


                                }


                                if (listavisé.size() == 0) {

                                } else {


                                    for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {

                                        for (int n = 0; n < listavisé.size(); n++) {

                                            if (listavisé.get(n).getCode_envoi().equalsIgnoreCase(cursorr.getString(0)))


                                            {

                                                listavisé.remove(listavisé.get(n));
                                                changeToggleText();
                                                onResume();
                                            }

                                        }

                                    }


                                }



                                db.deleteobj(obj);
                                db.deletepere1(obj);
                                db.deleteobject_dnl(session.getUsername());

                                SharedPreferences sp = getActivity().getSharedPreferences("num_obj", Context.MODE_PRIVATE);


                                String obj1 = String.valueOf(sp.getString("obj", null));

                                if (obj1.equalsIgnoreCase(obj)) {
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();

                                }


                                Cursor cursor1 = db.getobj(obj);
                                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                    db.deletecode(cursor1.getString(0));
                                }
                                db.deleteData3();


                            } catch (Exception e) {
                                //  Toast.makeText(ctx, "une erreur est surevnue réessayer plus tard", Toast.LENGTH_SHORT).show();

                                try {
                                    throw new OnlineODataStoreException(e);
                                } catch (OnlineODataStoreException e1) {
                                    e1.printStackTrace();


                                }
                            }


                        }


                        else
                        {

                            Toast.makeText(getActivity(), "DNL № "+obj+"n'est pas cloturée,une erreur est surevnue réssayer plus tard", Toast.LENGTH_SHORT).show();

                        }





                    }

                });

            }



        });


    }


    public  void motif() {
        Log.e("han2","han2");




        db=new SQLiteHandler(getActivity());

        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mTries = 0;


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.youtube.com/")
                .build();
//        Toast.makeText(getApplicationContext(), "une erreur est survenue réssayer plus tard", Toast.LENGTH_SHORT).show();


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
                            Toast.makeText(getActivity(), "Connectez vous à internet puis réssayer", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                mDeviceBandwidthSampler.stopSampling();
                 if (getActivity() != null) {
                     getActivity().runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             //Toast.makeText(getApplicationContext(), "Connecté", Toast.LENGTH_SHORT).show();

                             session = new SessionManager(getActivity());
                             login = session.getUsername();
                             pwd = session.getPassword();

                             CredentialsProvider1 credProvider = CredentialsProvider1
                                     .getInstance(lgtx, login, pwd);

                             HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                                     getActivity()).supportBasicAuthUsing(credProvider).configure(
                                     new HttpConversationManager(getActivity()));


                             XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
                             XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(getActivity(),
                                     requestFilter);
                             manager.addFilter(requestFilter);
                             manager.addFilter(responseFilter);
                             URL url = null;

                             try {
                                 url = new URL("http://testpda.barid.ma/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                                 //    url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                             } catch (MalformedURLException e) {
                                 e.printStackTrace();
                             }
                             //Method to open a new online store asynchronously


                             //Check if OnlineODataStore opened successfully
                             OnlineODataStore store = null;

                             try {
                                 store = OnlineODataStore.open(getActivity(), url, manager, null);
                             } catch (ODataException e) {
                                 e.printStackTrace();
                                 Toast.makeText(getActivity(), " une erreur est survenue réssayer plus tard", Toast.LENGTH_SHORT).show();
                             }

                             // ArrayList<Agency> agencyList = new ArrayList<Agency>();

	/*	AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();*/

                             if (store != null) {

                                 Cursor cr = db.getAllmotif();
                                 if (cr.getCount() == 0) {
                                     ODataProperty property;
                                     ODataPropMap properties;


                                     ODataResponseSingle resp = null;
                                     try {
                                         resp = store.executeReadEntitySet(
                                                 "MOTIFSet", null);
                                     } catch (ODataNetworkException e) {
                                         e.printStackTrace();
                                     } catch (ODataParserException e) {
                                         e.printStackTrace();
                                     } catch (ODataContractViolationException e) {
                                         e.printStackTrace();
                                     }
                                     //Get the response payload


                                     //Get the response payload
                                     ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                                     //Get the list of ODataEntity

                                     entities = feed.getEntities();

                                     for (ODataEntity entity : entities) {
                                         properties = entity.getProperties();


                               /* motif = (String) properties.get("ZsdMotif").getValue();
                                designation = (String) properties.get("ZsdDesignation").getValue();*/
                                         db.addmotif((String) properties.get("ZsdMotif").getValue(), (String) properties.get("ZsdDesignation").getValue());

                                         //  Toast.makeText(getApplicationContext(), "hanane", Toast.LENGTH_SHORT).show();


                                         ////////////////////////////////////////////////////////


                                     }

                                 }


                                 Cursor crr = db.getAllmesure();
                                 if (crr.getCount() == 0) {
                                     ODataProperty property1;
                                     ODataPropMap properties1;


                                     ODataResponseSingle resp1 = null;
                                     try {
                                         resp1 = store.executeReadEntitySet(
                                                 "MESURESet", null);
                                     } catch (ODataNetworkException e) {
                                         e.printStackTrace();
                                     } catch (ODataParserException e) {
                                         e.printStackTrace();
                                     } catch (ODataContractViolationException e) {
                                         e.printStackTrace();
                                     }
                                     //Get the response payload


                                     //Get the response payload
                                     ODataEntitySet feed1 = (ODataEntitySet) resp1.getPayload();
                                     //Get the list of ODataEntity

                                     entities1 = feed1.getEntities();

                                     for (ODataEntity entity1 : entities1) {
                                         properties1 = entity1.getProperties();


                                /*motiff = (String) properties1.get("ZsdMotif").getValue();
                                mesure = (String) properties1.get("ZsdDesignation").getValue();*/
                                         db.addmesure((String) properties1.get("ZsdMotif").getValue(), (String) properties1.get("ZsdDesignation").getValue(), (String) properties1.get("Stat").getValue());

                                         //Toast.makeText(getApplicationContext(), mesure + "mesure", Toast.LENGTH_SHORT).show();


                                         ////////////////////////////////////////////////////////


                                     }

                                 }

                                 Cursor cr1 = db.getAlldiv();
                                 if (cr1.getCount() == 0) {

                                     ODataProperty property2;
                                     ODataPropMap properties2;


                                     ODataResponseSingle resp2 = null;
                                     try {
                                         resp2 = store.executeReadEntitySet(
                                                 "DivisonSet", null);
                                     } catch (ODataNetworkException e) {
                                         e.printStackTrace();
                                     } catch (ODataParserException e) {
                                         e.printStackTrace();
                                     } catch (ODataContractViolationException e) {
                                         e.printStackTrace();
                                     }
                                     //Get the response payload


                                     //Get the response payload
                                     ODataEntitySet feed2 = (ODataEntitySet) resp2.getPayload();
                                     //Get the list of ODataEntity

                                     entities2 = feed2.getEntities();

                                     for (ODataEntity entity2 : entities2) {
                                         properties2 = entity2.getProperties();


                                /*motiff = (String) properties1.get("ZsdMotif").getValue();
                                mesure = (String) properties1.get("ZsdDesignation").getValue();*/
                                         db.adddivision((String) properties2.get("Werks").getValue() + " " + (String) properties2.get("Name1").getValue());


                                     }
                                 }

                                 Cursor cr2 = db.getAllrelais();
                                 if (cr2.getCount() == 0) {

                                     ODataProperty property3;
                                     ODataPropMap properties3;

                                     Log.e("han3", "han3");

                                     ODataResponseSingle resp3 = null;
                                     try {
                                         resp3 = store.executeReadEntitySet(
                                                 "RelaisSet", null);
                                     } catch (ODataNetworkException e) {
                                         e.printStackTrace();
                                         Log.e("han4", e.toString());
                                     } catch (ODataParserException e) {
                                         e.printStackTrace();

                                         Log.e("han5", e.toString());
                                     } catch (ODataContractViolationException e) {
                                         e.printStackTrace();
                                         Log.e("han6", e.toString());
                                     }
                                     //Get the response payload


                                     //Get the response payload
                                     ODataEntitySet feed3 = (ODataEntitySet) resp3.getPayload();
                                     //Get the list of ODataEntity

                                     entities3 = feed3.getEntities();

                                     for (ODataEntity entity3 : entities3) {
                                         properties3 = entity3.getProperties();


                                /*motiff = (String) properties1.get("ZsdMotif").getValue();
                                mesure = (String) properties1.get("ZsdDesignation").getValue();*/
                                         db.addrelais((String) properties3.get("Pudoid").getValue() + "  " + (String) properties3.get("Designation").getValue());


                                     }
                                 }

                             }


                         }

                     });
                 }

            }


        });







    }










}










