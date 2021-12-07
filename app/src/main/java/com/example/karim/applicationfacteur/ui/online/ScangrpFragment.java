package com.example.karim.applicationfacteur.ui.online;

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
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.TraceLog;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResult1;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.ui.main.Livraison_entrep1;
import com.example.karim.applicationfacteur.ui.main.Loading1;
import com.example.karim.applicationfacteur.ui.main.QRcode;
import com.example.karim.applicationfacteur.ui.main.Scanpda;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
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

import static com.example.karim.applicationfacteur.ui.main.myActivity.getConnectivityStatus;
import static com.example.karim.applicationfacteur.ui.main.myActivity.url_g;
import static com.kyanogen.signatureview.SignatureView.TAG;

/**
 * Created by hanane on 11/06/2020.
 */


public class ScangrpFragment extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResult1<List<Agency3>>>, UIListener {
    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }

    /* Declaration des variables*/
    private View myView;
    private boolean netSpeed;
    String activity = this.getClass().getSimpleName();
    String prevStatus = "";
    Cursor cursor;
    String barcode;
    protected ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    protected ConnectionClassManager mConnectionClassManager;
    protected DeviceBandwidthSampler mDeviceBandwidthSampler;
    protected int mTries;
    List<ODataEntity> entities, entities1;
    Collecte cl;
    String adresse;
    String designation ;
    String currentDateTimeString = currentDateTimeString = DateFormat.getDateInstance().format(new Date());
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private SQLiteHandler db;
    String text = "";
    static LogonCoreContext lgtx;
    static List<Agency3> agencies;
    SessionManager session;
    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    String obj = "" ;
    String login , pwd,des;
    ODataResponse oDataResponse = null;
    UIListener ui= this;
    /*declaration liste */
    static ArrayList<Agency3> agencyList     = new ArrayList<>();
    static ArrayList<Agency3> agencyList1    = new ArrayList<>();
    static ArrayList<Agency3> agencyList2    = new ArrayList<>();
    static ArrayList<String > agencyListpere = new ArrayList<>();
    static ArrayList<Agency3> agencyListpr   = new ArrayList<>();
    static ArrayList<String>  list           = new ArrayList<>();
    static ArrayList<String > test           = new ArrayList<>();
    public EditText childViewAgenciesTitle;
    private ListView childViewAgenciesList;
    private LayoutInflater inflater;
    private Loader<AsyncResult1<List<Agency3>>> loader;
    private Agency agency;
    private Agency1 s;
    private  Agency3 agc;
    String code1,str;
    ScangrpFragment frag;
    private EditText searchBarcode;
    private ScangrpAdapater adapter1;
    public Button showSearch;
    public Button livraison;
    public ImageButton qr;
    EditText code_envoi;
    Integer total = 0 ;
    Integer prix = 0 ;
    String pere,size,pere1,code,size1,mode_liv;
    RadioButton cb,cb2;
    RadioGroup rd;

/*Declaration des variables*/

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        if (myView == null) {
            myView                 = this.inflater.inflate(R.layout.scangrplayout, null);
            childViewAgenciesTitle = (EditText) myView.findViewById(R.id.agencies_title);
            childViewAgenciesList  = (ListView) myView.findViewById(R.id.agencies_list);
            showSearch             = (Button) myView.findViewById(R.id.btn_show_search);
            rd                     = (RadioGroup) myView.findViewById(R.id.rdg);
            cb                     = (RadioButton) myView.findViewById(R.id.radioButton);
            loader                 = getLoaderManager().initLoader(0, savedInstanceState, this);
            code_envoi             = (EditText) myView.findViewById(R.id.code);
            agencyList.clear();
            agencyList1.clear();
            agencyListpere.clear();
            db = new SQLiteHandler(getActivity());
            db.deletepr();
            agencyListpr.clear();

        }
// scan sans passer par le boutton blanc
        code_envoi.setInputType(InputType.TYPE_NULL); // ne peas permetre à l'utilisateur de saisir les données
        code_envoi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(getActivity(),s.toString()+"test",Toast.LENGTH_SHORT).show();
                refresh();
                // enlever les espaces et # du code envoi scané
                str = s.toString().replace(" ", "").replace("#", "").toUpperCase();
                if (!str.isEmpty()) {
                    Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();

// si l'envoi n'existe pas dans la liste encours des utilisateurs "un msg s'affiche ENVOI indisponible

                    agc = new Agency3(str.toUpperCase());
                    agc.setNom_client("tesr");
                    agc.setTelephone_client("89999");
                    agc.setAdresse_client("hhh");
                    agc.setCrbt("76");
                    agc.setPod("");
                    agc.setService("");
                    agc.setDesignation("YYY");
                    agc.setMode_liv("HH");
                    agc.setAdr_relais("JJ");
                    agc.setRelais("JJ");
                    agencyList.add(agc);

                    for (int i = 0; i < agencyList.size(); i++) {
                        list.add(agencyList.get(i).getCode_envoi());
                    }
                    code_envoi.setText("");
                    s.clear();
                }
            }

        });

        /* boutton ajouter à la liste des envois en-cours */
        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton:

                        agencyList1             = agencyList ;
                        mConnectionClassManager = ConnectionClassManager.getInstance();
                        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
                        mTries                  = 0;
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
                                            Toast.makeText(getActivity(),"Vérifiez votre connexion internet ",Toast.LENGTH_SHORT).show();
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
                          //  AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
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
                                        //   url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
                                        // url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
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
                                    }

                                    if (store != null) {

                                        ODataProperty property;
                                        ODataPropMap properties;
                                        ODataResponseSingle resp = null;

                                       for (int j = 0; j < agencyList.size(); j++) {
                                            try {

                                                String str = agencyList.get(j).getCode_envoi();

                                                str = str.replace(" ", "");
                                                /*supprimer l'espace du numéro d'obj*/
                                                obj = obj.trim();
                                              //  resp = store.executeReadEntity("VerificationSet(code_envoi='" + str + "',Num_obj='" + obj + "')", null);
                                                resp = store.executeReadEntity("VerificationSet(code_envoi='" + str + "',Num_obj='" + obj + "')", null);

                                            } catch (ODataNetworkException e3) {
                                                e3.printStackTrace();
                                            } catch (ODataParserException e2) {
                                                e2.printStackTrace();

                                            } catch (ODataContractViolationException e3) {
                                                e3.printStackTrace();

                                            }

                                            if (resp == null) {

                                                Toast.makeText(getActivity(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();
                                            } else {
                                                ODataEntity feed = (ODataEntity) resp.getPayload();
                                                //Get the list of ODataEntity

                                                //Loop to retrieve the information from the response

                                                properties = feed.getProperties();

                                                property = properties.get("Statut_veri");

                                                if (property.getValue().equals("OK")) {
                                                    /* si le statut de l'envoi est ok donc il peut etre affecté au facteur */
                                                    String code_envoi_pere = (String) properties.get("CODE_ENVOI_PERE").getValue();
                                                    obj = (String) properties.get("Num_obj_dnl").getValue();
                                                    String oobj1 = obj.replace(" ", "");
                                                    if (code_envoi_pere.isEmpty()) {
                                                        /*gestion des envois sans code envoi pére */
                                                        designation = (String) properties.get("Designation").getValue();
                                                        if (designation.equalsIgnoreCase("POD")) {
                                                            /*cas des envois POD*/
                                                            adresse = (String) properties.get("ADRESSE").getValue();
                                                        } else {
                                                            /*cas des envois avec POD*/
                                                            adresse = (String) properties.get("Quartier").getValue() + ", " + (String) properties.get("Rue").getValue() + ", " + (String) properties.get("point_remise").getValue() + ", " + (String) properties.get("point_geo").getValue();
                                                        }
                                                     /* Ajout des données des envois au niveau de la base de donnée local SQLITE pour affichage hors connex */
                                                        db.addData(agencyList1.get(j).getCode_envoi(),
                                                                (String) properties.get("Client").getValue(),          //nom client
                                                                (String) properties.get("Telephone_dest").getValue(),  //Tel destinataire
                                                                adresse,                                             // adresse
                                                                (String) properties.get("Montant").getValue(),        // Montant crbt
                                                                "En cours", "DNL", session.getUsername(),             // Statut de l'envoi
                                                                (String) properties.get("POD").getValue(),            // Service POD
                                                                (String) properties.get("Service").getValue(),        //Service crbt
                                                                (String) properties.get("Designation").getValue(),    //Designation de l'envoi
                                                                (String) properties.get("Mode_liv").getValue(),       //Mode de livraison (à l'adresse..)
                                                                (String) properties.get("Rue").getValue(),            //Rue
                                                                (String) properties.get("Code_postal").getValue(),    //Code postal
                                                                "1", DateFormat.getDateInstance().format(new Date()),
                                                                (String) properties.get("RUE_RELAIS").getValue() + ", " + (String) properties.get("NUM_RUE_RELAIS").getValue() + ", " + (String) properties.get("LOCALITE_RELAIS").getValue() + ", " + (String) properties.get("CODE_POSTAL_RELAIS").getValue(),
                                                                (String) properties.get("NOM_RELAIS").getValue(),     //point relais
                                                                (String) properties.get("AGENCE").getValue(),         //agence
                                                                (String) properties.get("RUE_AGENCE").getValue() + ", " + (String) properties.get("CODE_POSTAL_AGENCE").getValue() + ", " + (String) properties.get("LOCALITE_AGENCE").getValue(),
                                                                oobj1, (String) properties.get("TELEPHONE_EXP").getValue(), // Télephone exp
                                                                (String) properties.get("CODE_ENVOI_PERE").getValue());     // code envoi pére
                                         /* récupérer les envois avec numobj: oobj1 */
                                                        Cursor cursorr = db.getobj(oobj1);
                                         /* Ajouter numobj à la table de la base de donn local  SQLITE*/
                                                        db.add_object_table_DNL(String.valueOf(oobj1), session.getUsername());
                                         /* Récupérer tous les envois  depuis la bd sqdlite pour les afficher dans la liste encours  */
                                                        Cursor cursor1 = db.getAllDNL(DateFormat.getDateInstance().format(new Date()));

                                                        for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                                            agc = new Agency3(cursor1.getString(1));
                                                            agc.setNom_client(cursor1.getString(2));           // nom client
                                                            agc.setTelephone_client(cursor1.getString(3));    // Téléph destinataire
                                                            agc.setAdresse_client(cursor1.getString(4));     // adresse
                                                            agc.setCrbt(cursor1.getString(5));              // crbt
                                                            agc.setPod(cursor1.getString(9));              // POD
                                                            agc.setService(cursor1.getString(10));        // designation du service crbt cheque / espéce
                                                            agc.setDesignation(cursor1.getString(11));    // Designation de l article
                                                            agc.setAdr_relais(cursor1.getString(17));     //  adresse point relais
                                                            agc.setRelais(cursor1.getString(18));         // point relais
                                                            agc.setAgc(cursor1.getString(19));            // agence
                                                            agc.setAdr_agc(cursor1.getString(20));        // adr agence
                                                            agc.setObj(cursor1.getString(21));
                                                            agc.setTelephone_exp(cursor1.getString(22));  // téléphone expéditeur
                                                            //  Toast.makeText(getApplicationContext(),cursor1.getString(21)+"obbj",Toast.LENGTH_SHORT).show();
                                                        }

                                                        agencyList2.add(agc);
                                                    } else {/*gestion des envois avec code envoi pére */
                                                        if (designation != null) {

                                                            if (designation.equalsIgnoreCase("POD")) {
                                                                adresse = (String) properties.get("ADRESSE").getValue();
                                                            } else {
                                                                adresse = (String) properties.get("Quartier").getValue() + ", " + (String) properties.get("Rue").getValue() + ", " + (String) properties.get("point_remise").getValue() + ", " + (String) properties.get("point_geo").getValue();

                                                            }
                                                        }

                                                        db.addData_pere(agencyList1.get(j).getCode_envoi(),
                                                                (String) properties.get("Client").getValue(),
                                                                (String) properties.get("Telephone_dest").getValue(),
                                                                adresse,
                                                                (String) properties.get("Montant").getValue(),
                                                                "En cours", "DNL", session.getUsername(),
                                                                (String) properties.get("POD").getValue(),
                                                                (String) properties.get("Service").getValue(),
                                                                (String) properties.get("Designation").getValue(),
                                                                (String) properties.get("Mode_liv").getValue(),
                                                                (String) properties.get("Rue").getValue(),
                                                                (String) properties.get("Code_postal").getValue(), "1",
                                                                DateFormat.getDateInstance().format(new Date()),
                                                                (String) properties.get("RUE_RELAIS").getValue() + ", " + (String) properties.get("NUM_RUE_RELAIS").getValue() + ", " + (String) properties.get("LOCALITE_RELAIS").getValue() + ", " + (String) properties.get("CODE_POSTAL_RELAIS").getValue(),
                                                                (String) properties.get("NOM_RELAIS").getValue(),
                                                                (String) properties.get("AGENCE").getValue(),
                                                                (String) properties.get("RUE_AGENCE").getValue() + ", " + (String) properties.get("CODE_POSTAL_AGENCE").getValue() + ", " + (String) properties.get("LOCALITE_AGENCE").getValue(),
                                                                oobj1, (String) properties.get("TELEPHONE_EXP").getValue(),
                                                                (String) properties.get("CODE_ENVOI_PERE").getValue());

                                                        String tail = (String) properties.get("ENVOIS_PERE").getValue();
                                                        String taill = tail + "";
                                                        String size = tail.replace(" ", "");
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

                                                            db.add_object_table_DNL(String.valueOf(oobj1), session.getUsername());

                                                            for (cursorr.moveToFirst(); !cursorr.isAfterLast(); cursorr.moveToNext()) {

                                                                db.addData(cursorr.getString(1),
                                                                        cursorr.getString(2),
                                                                        cursorr.getString(3),
                                                                        cursorr.getString(4),
                                                                        cursorr.getString(5),
                                                                        cursorr.getString(6),
                                                                        cursorr.getString(7),
                                                                        cursorr.getString(8),
                                                                        cursorr.getString(9),
                                                                        cursorr.getString(10),
                                                                        cursorr.getString(11),
                                                                        cursorr.getString(12),
                                                                        cursorr.getString(13),
                                                                        cursorr.getString(14),
                                                                        cursorr.getString(15),
                                                                        cursorr.getString(16),
                                                                        cursorr.getString(17),
                                                                        cursorr.getString(18),
                                                                        cursorr.getString(19),
                                                                        cursorr.getString(20),
                                                                        cursorr.getString(21),
                                                                        cursorr.getString(22),
                                                                        cursorr.getString(23));

                                                                newEntity.getProperties().put("NumEnvoi", new ODataPropertyDefaultImpl("NumEnvoi", cursorr.getString(1)));
                                                                newEntity.getProperties().put("ZnumObj", new ODataPropertyDefaultImpl("ZnumObj", obj));

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

                                                                                    agencyList2.add(agc);

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

                                                            Intent intent = new Intent(getActivity(), AgencyListActivity1.class);
                                                            //  intent.putExtra("code",String.valueOf(obj));
                                                            startActivity(intent);
                                                            getActivity().finish();

                                                        } else {


                                                            db.add_object_table_DNL(String.valueOf(obj), session.getUsername());


                                                        }


                                                    }


                                                } else if (property.getValue().equals("NON OK")) {
                                                    Toast.makeText(getActivity(), "Envoi: " + str + "non valide", Toast.LENGTH_LONG).show();

                                                } else if (property.getValue().equals("DOUBLE")) {
                                                    Toast.makeText(getActivity(), "Envoi: " + str + " déja affecté ", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                            //Get the response payload
                                        }

                                        Intent intent = new Intent(ScangrpFragment.this.getActivity(), AgencyListActivity1.class);
                                        //  intent.putExtra("code",String.valueOf(obj));
                                        startActivity(intent);
                                        getActivity().finish();
                                    } else {

                                        Toast.makeText(getActivity(), "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();
                                    }

                                }

                                });
                        }
                        });



                                            rd.clearCheck();


                                            break;
                }
            }
        });




        Bundle bundle = getActivity().getIntent().getExtras();

        if(bundle !=null)
        {
            //Extract the data…
            String code_barre = bundle.getString("code");
            code_envoi.setText(code_barre);
        }

   /*boutton scan */
     /*   showSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
  // enlever les espaces et # du code envoi scané
                str = code_envoi.getText().toString().replace(" ", "").replace("#", "").toUpperCase();
               if (!str.isEmpty()) {
                    Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();

// si l'envoi n'existe pas dans la liste encours des utilisateurs "un msg s'affiche ENVOI indisponible

                    agc = new Agency3(str.toUpperCase());
                    agc.setNom_client("tesr");
                    agc.setTelephone_client("89999");
                    agc.setAdresse_client("hhh");
                    agc.setCrbt("76");
                    agc.setPod("");
                    agc.setService("");
                    agc.setDesignation("YYY");
                    agc.setMode_liv("HH");
                    agc.setAdr_relais("JJ");
                    agc.setRelais("JJ");
                    agencyList.add(agc);

                    for (int i = 0; i < agencyList.size(); i++) {
                        list.add(agencyList.get(i).getCode_envoi());
                    }
                    code_envoi.setText("");
                }
            }


        });
 /*boutton scan */


        return myView;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter1 = new ScangrpAdapater(this, childViewAgenciesList, inflater, agencies, getActivity());
        childViewAgenciesList.setAdapter(adapter1);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onRefreshRequested(){
        refresh();

    }

    @Override
    public Loader<AsyncResult1<List<Agency3>>> onCreateLoader(int id, Bundle args) {

        return new ScangrpAsyncLoader(getActivity());

    }

    @Override
    public void onLoadFinished(Loader<AsyncResult1<List<Agency3>>> listLoader,
                               AsyncResult1<List<Agency3>> result) {
        if (result.getException() != null || result.getData() == null) {
            Toast.makeText(getActivity(), "une erreur est survenue. veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();
            TraceLog.e("Error loading agencies", result.getException());

        } else {

            agencies = result.getData();
            childViewAgenciesList.setAdapter(new ScangrpAdapater(this,
                    childViewAgenciesList, inflater, agencies,getActivity()));
            //childViewAgenciesTitle.setText("Envois En Cours: "+ agencies.size());
            onResume();
            // refresh();
        }
    }


    @Override
    public void onLoaderReset(Loader<AsyncResult1<List<Agency3>>> listLoader) {
    }


    public void refresh() {
        loader.forceLoad();
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


    public static List<Agency3> getAgencies2(Context context)  {

        return agencyList;

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


    public static List<Agency3> getAgencies1(Context context)  {

        return agencyList2;

    }

}