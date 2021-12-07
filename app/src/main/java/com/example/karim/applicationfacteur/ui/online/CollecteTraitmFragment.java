package com.example.karim.applicationfacteur.ui.online;

import android.app.Fragment;
import android.app.LoaderManager;
import java.lang.reflect.Type;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.Loader;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.TraceLog;
import com.example.karim.applicationfacteur.services.UIListener;

import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResultEnvoi;

import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.types.Envoi;

import com.example.karim.applicationfacteur.ui.main.Acceuil1;
import com.example.karim.applicationfacteur.ui.main.MainCL;
import com.example.karim.applicationfacteur.ui.main.QRcollecelist;
import com.example.karim.applicationfacteur.ui.main.QRcollecte;
import com.example.karim.applicationfacteur.ui.main.QRcollecte1;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.Testt;
import com.example.karim.applicationfacteur.ui.main.myActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by hanane on 06/03/2019.
 */

public class CollecteTraitmFragment extends  Fragment  implements LoaderManager.LoaderCallbacks<AsyncResultEnvoi<List<Envoi>>>, UIListener {

    @Override
    public void onLoaderReset(Loader<AsyncResultEnvoi<List<Envoi>>> loader) {

    }

    private View myView;


    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private SQLiteHandler db;
    String text = "";
    static LogonCoreContext lgtx;
    static List<Envoi> envs;
    SessionManager session;
    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    private Collecte CL;
    CollecteListAdapter adapter;
    final ArrayList<String> myList1 = new ArrayList<String>();

    String sp;
    MainCL activity ;


    String login, pwd, des;

    UIListener ui = this;
    static ArrayList<Envoi> EnvoiList = new ArrayList<>();
    static ArrayList<Envoi> EnvoiList1 = new ArrayList<>();
    static ArrayList<String> list = new ArrayList<>();


    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }


    public  TextView code2D,txt;
    public EditText nombre;
    private ListView envoiliste;
    private LayoutInflater inflater;
    private Loader<AsyncResultEnvoi<List<Envoi>>> loader;
    private Envoi envoi;
    String code1, str;
    AgencyListFragment1 frag;
    private EditText searchBarcode;
    private CollecteAdapter adapter1;
    public Button scan;

    public Button validerr,suite;
    public ImageButton qr;
    EditText code_envoi;
    Integer total = 0;
    Integer prix = 0;
    String pere, size, pere1, code, size1, mode_liv;
    RadioButton cb, cb2;
    RadioGroup rd;
    SharedPreferences prefs;
    Calendar calandrier=Calendar.getInstance();
    String minutes = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = new MainCL();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //  ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);





        this.inflater = inflater;


        if (myView == null) {


            myView = this.inflater.inflate(R.layout.scancollecte, null);
            envoiliste = (ListView) myView.findViewById(R.id.envoiliste);
            nombre = (EditText) myView.findViewById(R.id.nombre);
            scan = (Button) myView.findViewById(R.id.scan);
            validerr = (Button) myView.findViewById(R.id.validerr);
            qr = (ImageButton) myView.findViewById(R.id.qr);
            txt = (TextView) myView.findViewById(R.id.txt);
            suite = (Button) myView.findViewById(R.id.suite);

            loader = getLoaderManager().initLoader(0, savedInstanceState, this);

            code_envoi = (EditText) myView.findViewById(R.id.code);

                            if (EnvoiList != null)

                            {
                                for (int i = 0; i < EnvoiList.size(); i++)

                                {


                                    if (!list.contains(EnvoiList.get(i).getCode_envoi().toUpperCase())) {

                                        list.add(EnvoiList.get(i).getCode_envoi());


                                    }
                                }
                                  //nombre.setText(EnvoiList.size());

                                EnvoiList.clear();

                            }

          Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null) {
                CL = bundle.getParcelable("CollecteSelected");

            }


            SQLiteHandler db = new SQLiteHandler(getActivity());

            session = new SessionManager(getActivity());
            login = session.getUsername();
            pwd = session.getPassword();
            if (envoiliste.getCount() == 0) {
                Cursor cursor = db.getenvoicl(CL.getNum_fdr(), CL.getNum_client(), session.getUsername());


                if (cursor.getCount() != 0) {

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        Envoi envoi = new Envoi(cursor.getString(0));

                        EnvoiList.add(envoi);

                        envs = EnvoiList;


                        envoiliste.setAdapter(new CollecteAdapter(this,
                                envoiliste, inflater, envs, getActivity()));

                    }

                    nombre.setText((String.valueOf(envs.size())));
                }

            }


        }


        code2D = (TextView) myView.findViewById(R.id.code2D);

        SpannableString content = new SpannableString("Générer Code 2D");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        code2D.setText(content);
        code2D.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (EnvoiList.size() == 0) {
                    Toast.makeText(getActivity(), "Vous ne pouvez pas générer le code 2D ", Toast.LENGTH_SHORT).show();

                } else {


                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    CharSequence items[] = new CharSequence[]{"virgule", "point virgule", "espace", "deux point", "retour à la ligne"};
                    adb.setTitle("choisissez le séparateur");
                    AlertDialog.Builder builder;

                    adb.setSingleChoiceItems(
                            items,
                            -1, // does not select anything
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int index) {
                                    switch (index) {
                                        case 0:

                                    prefs = getActivity().getSharedPreferences("sep", Context.MODE_PRIVATE);
                             prefs.edit().putString("sp",",").commit();

                                            Intent intent = new Intent();
                                            intent.setClass(getActivity(), QRcollecte1.class);
                                            intent.putExtra("CollecteSelected",CL);
                                            intent.putExtra("list", EnvoiList);
                                            intent.putExtra("sep", ",");
                                            startActivity(intent);
                                            getActivity().finish();

                                            break;
                                        case 1:

                                            prefs = getActivity().getSharedPreferences("sep", Context.MODE_PRIVATE);
                                            prefs.edit().putString("sp",",").commit();


                                            Intent intent1 = new Intent();
                                            intent1.setClass(getActivity(), QRcollecte1.class);
                                            intent1.putExtra("CollecteSelected",CL);
                                            intent1.putExtra("list", EnvoiList);
                                            intent1.putExtra("sep", ",");
                                            // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                            startActivity(intent1);
                                            getActivity().finish();

                                        case 2: // remind me
                                            //


                                            prefs = getActivity().getSharedPreferences("sep", Context.MODE_PRIVATE);
                                            prefs.edit().putString("sp",";").commit();
                                            Intent intent2 = new Intent();
                                            intent2.setClass(getActivity(), QRcollecte1.class);
                                            intent2.putExtra("list", EnvoiList);
                                            intent2.putExtra("sep", ";");
                                            intent2.putExtra("CollecteSelected",CL);
                                            startActivity(intent2);
                                            getActivity().finish();


                                            break;
                                        case 3: // add to calendar
                                            //

                                            prefs = getActivity().getSharedPreferences("sep", Context.MODE_PRIVATE);
                                            prefs.edit().putString("sp","espace").commit();

                                            Intent intent3 = new Intent();
                                            intent3.setClass(getActivity(), QRcollecte1.class);
                                            intent3.putExtra("list", EnvoiList);
                                            intent3.putExtra("sep", "");
                                            intent3.putExtra("CollecteSelected",CL);
                                            // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                            startActivity(intent3);
                                            getActivity().finish();


                                            break;
                                        case 4:

                                            prefs = getActivity().getSharedPreferences("sep", Context.MODE_PRIVATE);
                                            prefs.edit().putString("sp",":").commit();

                                            Intent intent4 = new Intent();
                                            intent4.setClass(getActivity(), QRcollecte1.class);
                                            intent4.putExtra("list", EnvoiList);
                                            intent4.putExtra("sep", ":");
                                            intent4.putExtra("CollecteSelected",CL);
                                            // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                            startActivity(intent4);
                                            getActivity().finish();

                                            // add to calendar
                                            //
                                            break;
                                        case 5: // add to calendar


                                            prefs = getActivity().getSharedPreferences("sep", Context.MODE_PRIVATE);
                                            prefs.edit().putString("sp","rtr").commit();
                                            Intent intent5 = new Intent();
                                            intent5.setClass(getActivity(), QRcollecte1.class);
                                            intent5.putExtra("list", EnvoiList);
                                            intent5.putExtra("sep", "rtr");
                                            intent5.putExtra("CollecteSelected",CL);
                                            // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                            startActivity(intent5);
                                            getActivity().finish();


                                            //
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            });
                    adb.setCancelable(true);
                    adb.setNeutralButton("NON" +
                            "", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

                    AlertDialog alert = adb.create();
                    alert.show();


                }
            }
        });


        db = new SQLiteHandler(getActivity());

        //**********button de sanner les envois*******/
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String code = code_envoi.getText().toString().replaceAll("[^0-9 A-Z a-z]+", "");
                if (!code.isEmpty()) {

                    refresh();

                    if (!list.contains(code)) {


                        envoi = new Envoi(code);
                        EnvoiList.add(envoi);
                        db.addData_envoi_CL(CL.getNum_fdr(), CL.getNum_client(), code, session.getUsername());

                        for (int i = 0; i < EnvoiList.size(); i++) {
                            list.add(EnvoiList.get(i).getCode_envoi());

                        }

                    }


                    else
                    {
                        Toast.makeText(getActivity(),"ce code d'envoi existe déja",Toast.LENGTH_SHORT).show();
                    }

                    code_envoi.setText("");


                }

               nombre.setText((String.valueOf(list.size())));
                list.clear();
            }

        });


////////////////////****** button valider********/////////////////


        final String hour = String.valueOf(calandrier.get(Calendar.HOUR_OF_DAY));
        final String minute = String.valueOf(calandrier.get(Calendar.MINUTE));




        validerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (nombre.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "veuillez renseigner le nombre des envois", Toast.LENGTH_SHORT).show();
                } else {


                    String regexString = "^[0-9]*$";

                    if (nombre.getText().toString().trim().matches(regexString)) {

                        if (EnvoiList.size() == Integer.valueOf(nombre.getText().toString().replaceAll("[^0-9]+", ""))) {
                            if(minute.length()==1)
                            {
                                minutes ="0"+minute;
                                   if( activity.registerInternetCheckReceiver(getActivity()))
                                   {
                                       myActivity.InsertENVOI(getActivity(),CL, EnvoiList, hour + minutes,"","",getActivity());
                                       db.updatenbenvoi(nombre.getText().toString(),CL.getNum_fdr(),CL.getNum_client(),session.getUsername());


                                   }

                                   else
                                   {
                                       for(int j = 0;j< EnvoiList.size();j++)
                                       {
                                           db.addData_CL_offline(CL.getNum_fdr(),CL.getNum_poste(), CL.getNum_client(), CL.getNom_client(), CL.getInterlocuteur(), CL.getTelephone_client(), CL.getAdresse_client(), CL.getDate(), CL.getHeure(),session.getUsername(), CL.getStat(),CL.getAgent(),CL.getAgence(), CL.getType_cl(),hour+minutes, CL.getCode_2D(),"",EnvoiList.get(j).getCode_envoi(),nombre.getText().toString());
                                       }

                                       db.updateStatut(CL.getNum_client(),"Traité",hour + minutes,CL.getNum_fdr());

                                       Intent intent = new Intent();
                                       intent.setClass(getActivity(),CollecteListActivity.class);
                                       startActivity(intent);
                                       getActivity().finish();

                                   }

                            }

                            else
                            {

                                if(activity.registerInternetCheckReceiver(getActivity()))
                                {
                                    MainCL.InsertENVOI(getActivity(),CL, EnvoiList, hour + minute,"","",getActivity(),"T");
                                    db.updatenbenvoi(nombre.getText().toString(),CL.getNum_fdr(),CL.getNum_client(),session.getUsername());


                                }

                                else
                                {



                                    for(int j = 0;j< EnvoiList.size();j++)
                                    {
                                        db.addData_CL_offline(CL.getNum_fdr(),CL.getNum_poste(), CL.getNum_client(), CL.getNom_client(), CL.getInterlocuteur(), CL.getTelephone_client(), CL.getAdresse_client(), CL.getDate(), CL.getHeure(),session.getUsername(), CL.getStat(),CL.getAgent(),CL.getAgence(), CL.getType_cl(),hour + minute, CL.getCode_2D(),"",EnvoiList.get(j).getCode_envoi(),nombre.getText().toString());

                                    }

                                          db.updateStatut(CL.getNum_client(),"Traité",hour + minute,CL.getNum_fdr());



                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(),CollecteListActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();


                            }

                            }






                     /*   SharedPreferences prefs = getActivity().getSharedPreferences("nb_envoi", Context.MODE_PRIVATE);
                        prefs.edit().putString("envoi",String.valueOf(EnvoiList.size())).commit();

*/
                        } else {
                            Toast.makeText(getActivity(), "le nombre des envois scannés est different du nombre des envois saisi", Toast.LENGTH_SHORT).show();
                        }







//it is digit
                    } else {

                        Toast.makeText(getActivity(), "entrez une valeur valide", Toast.LENGTH_SHORT).show();
                    }

                }


            }


        });

        /*******************************Suite******************/
           suite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), QRcollecteliv.class);
                intent.putExtra("CollecteSelected",CL);
                startActivity(intent);
                getActivity().finish();

            }

        });

/*******************************QR code******************/


        if (CL.getCode_2D().equalsIgnoreCase("1")) {


            qr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getActivity());

                    inputAlert.setTitle("Scanner le code 2D");
                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText code2D = new EditText(getActivity());
                    code2D.setHint("Code 2D");
                   // clientt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cl, 0, 0, 0);
                    layout.addView(code2D);

                    final Button btn = new Button(getActivity());
                    btn.setText("Valider");
                    layout.addView(btn);




                    inputAlert.setView(layout);

                    AlertDialog alertDialog = inputAlert.create();
                    alertDialog.show();



                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            myList1.clear();
                          /*  prefs = getActivity().getSharedPreferences("sep", Context.MODE_PRIVATE);
                            String sep = prefs.getString("sp", null);*/
                            String sep = ",";
                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                            if (!code2D.getText().toString().isEmpty()) {


                                if (sep.equalsIgnoreCase(",")) {
                                    final String[] str = code2D.getText().toString().split(",");

                                    String Value = "";
                                    for (int j = 0; j < EnvoiList.size(); j++) {
                                        for (int i = 0; i < str.length; i++) {
                                            if (EnvoiList.get(j).getCode_envoi().equalsIgnoreCase(str[i])) {
                                                myList1.add(str[i]);


                                            } else {

                                            }

                                        }


                                        // Value += agc.getCode_envoi() + "\n";
                                    }

                                    if (myList1.size() == EnvoiList.size() && myList1.size() == str.length) {

                                        builderSingle.setTitle("Valide");
                                        builderSingle.setIcon(R.drawable.ic_action_chk);
                                    } else {

                                        builderSingle.setTitle("Rejeté");
                                        builderSingle.setIcon(R.drawable.ic_action_ook);

                                        builderSingle.setNegativeButton("Suivant",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent intent = new Intent();
                                                        intent.setClass(getActivity(), QRcollectelist1.class);
                                                        intent.putExtra("myList", EnvoiList);
                                                        intent.putExtra("str", str);
                                                        intent.putExtra("CollecteSelected",CL);
                                                        // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                                        startActivity(intent);
                                                        getActivity().finish();


                                                    }
                                                });

                                    }






                        /*       builderSingle.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                dialog.dismiss();



                                            }
                                        });
*/


                                    builderSingle.create();
                                    builderSingle.show();


                                } else if (sep.equalsIgnoreCase(";")) {
                                    {
                                        final String[] str = code2D.getText().toString().split(";");

                                        String Value = "";
                                        for (int j = 0; j < EnvoiList.size(); j++) {
                                            for (int i = 0; i < str.length; i++) {
                                                if (EnvoiList.get(j).getCode_envoi().equalsIgnoreCase(str[i])) {
                                                    myList1.add(str[i]);


                                                } else {

                                                }

                                            }


                                            // Value += agc.getCode_envoi() + "\n";
                                        }

                                        if (myList1.size() == EnvoiList.size() && myList1.size() == str.length) {

                                            builderSingle.setTitle("Valide");
                                            builderSingle.setIcon(R.drawable.ic_action_chk);
                                        } else {

                                            builderSingle.setTitle("Rejeté");
                                            builderSingle.setIcon(R.drawable.ic_action_ook);

                                        }


                                        builderSingle.setNegativeButton("Annuler",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });


                                        builderSingle.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                             /*   Intent intent = new Intent();
                                                intent.setClass(getApplicationContext(), QRcollecelist.class);
                                                intent.putExtra("myList",myList);
                                                intent.putExtra("str",str);
                                                // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                finish();*/

                                                    }
                                                });


                                        builderSingle.create();
                                        builderSingle.show();


                                    }


                                } else if (sep.equalsIgnoreCase("espace"))

                                {

                                    {
                                        final String[] str = code2D.getText().toString().split("");

                                        String Value = "";
                                        for (int j = 0; j < EnvoiList.size(); j++) {
                                            for (int i = 0; i < str.length; i++) {
                                                if (EnvoiList.get(j).getCode_envoi().equalsIgnoreCase(str[i])) {
                                                    myList1.add(str[i]);


                                                } else {

                                                }

                                            }


                                            // Value += agc.getCode_envoi() + "\n";
                                        }

                                        if (myList1.size() == EnvoiList.size() && myList1.size() == str.length) {

                                            builderSingle.setTitle("Valide");
                                            builderSingle.setIcon(R.drawable.ic_action_chk);
                                        } else {

                                            builderSingle.setTitle("Rejeté");
                                            builderSingle.setIcon(R.drawable.ic_action_ook);

                                        }

                                 /*   final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                            getActivity(), android.R.layout.simple_dropdown_item_1line);
                                    //String str[]= code.split("\n");
                                    for(int i = 0; i< str.length; i++)
                                    {
                                        arrayAdapter.add(str[i]);
                                    }
*/
                                        builderSingle.setNegativeButton("Annuler",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });


                                        builderSingle.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                         /*      Intent intent = new Intent();
                                                intent.setClass(getActivity(),CollecteTraitmActivity.class);
                                                intent.putExtra("myList",myList);
                                                intent.putExtra("str",str);
                                                // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                finish();*/

                                                    }
                                                });




                                  /*  builderSingle.
                                            setAdapter(arrayAdapter,
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String strName = arrayAdapter.getItem(which);


                                                        }
                                                    });*/
                                        builderSingle.create();
                                        builderSingle.show();


                                    }


                                } else if (sep.equalsIgnoreCase(":"))

                                {


                                    {
                                        final String[] str = code2D.getText().toString().split(":");

                                        String Value = "";
                                        for (int j = 0; j < EnvoiList.size(); j++) {
                                            for (int i = 0; i < str.length; i++) {
                                                if (EnvoiList.get(j).getCode_envoi().equalsIgnoreCase(str[i])) {
                                                    myList1.add(str[i]);


                                                } else {

                                                }

                                            }


                                            // Value += agc.getCode_envoi() + "\n";
                                        }

                                        if (myList1.size() == EnvoiList.size() && myList1.size() == str.length) {

                                            builderSingle.setTitle("Valide");
                                            builderSingle.setIcon(R.drawable.ic_action_chk);
                                        } else {

                                            builderSingle.setTitle("Rejeté");
                                            builderSingle.setIcon(R.drawable.ic_action_ook);

                                        }


                                        builderSingle.setNegativeButton("Annuler",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });


                                        builderSingle.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                                        dialog.dismiss();
                                             /*   Intent intent = new Intent();
                                                intent.setClass(getApplicationContext(), QRcollecelist.class);
                                                intent.putExtra("myList",myList);
                                                intent.putExtra("str",str);
                                                // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                finish();*/

                                                    }
                                                });


                                        builderSingle.create();
                                        builderSingle.show();


                                    }


                                } else if (sep.equalsIgnoreCase("rtr"))

                                {


                                    {
                                        final String[] str = code2D.getText().toString().split("\n");

                                        String Value = "";
                                        for (int j = 0; j < EnvoiList.size(); j++) {
                                            for (int i = 0; i < str.length; i++) {
                                                if (EnvoiList.get(j).getCode_envoi().equalsIgnoreCase(str[i])) {
                                                    myList1.add(str[i]);


                                                } else {

                                                }

                                            }


                                            // Value += agc.getCode_envoi() + "\n";
                                        }

                                        if (myList1.size() == EnvoiList.size() && myList1.size() == str.length) {

                                            builderSingle.setTitle("Valide");
                                            builderSingle.setIcon(R.drawable.ic_action_chk);
                                        } else {

                                            builderSingle.setTitle("Rejeté");
                                            builderSingle.setIcon(R.drawable.ic_action_ook);

                                        }

                                        builderSingle.setNegativeButton("Annuler",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });


                                        builderSingle.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                             /*   Intent intent = new Intent();
                                                intent.setClass(getApplicationContext(), QRcollecelist.class);
                                                intent.putExtra("myList",myList);
                                                intent.putExtra("str",str);
                                                // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                finish();*/

                                                    }
                                                });


                                        builderSingle.create();
                                        builderSingle.show();
                                    }


                                }

                            } else {



                                Toast.makeText(getActivity(),"veillez scanner le code 2D",Toast.LENGTH_SHORT);
                            }


                        }

                    });









                /*    Intent intent5 = new Intent();
                                            intent5.setClass(getActivity(), QRcollecte.class);
                                            intent5.putExtra("list", EnvoiList);
                                            intent5.putExtra("sep", "rtr");
                                            // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                            startActivity(intent5);
                                            getActivity().finish();*/





                }

            });

        }

        else
        {
            qr.setVisibility(View.GONE);
            txt.setVisibility(View.GONE);
        }





            return myView;
        }



    @Override
    public void onResume() {
        super.onResume();
        adapter1 = new CollecteAdapter(this, envoiliste, inflater, envs, getActivity());
        envoiliste.setAdapter(adapter1);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    public void onSaveRequested() {
    }


    public void onAgencySelected(final Envoi agc, View v) {

    }

    public void onAgencySelected1(final Envoi agc, View v) {


    }


    public void onRefreshRequested() {
        refresh();

    }

    @Override
    public Loader<AsyncResultEnvoi<List<Envoi>>> onCreateLoader(int id, Bundle args) {

        return new CollecteAsyncLoaderTr(getActivity());

    }

    @Override
    public void onLoadFinished(Loader<AsyncResultEnvoi<List<Envoi>>> listLoader,
                               AsyncResultEnvoi<List<Envoi>> result) {
        if (result.getException() != null || result.getData() == null) {
            Toast.makeText(getActivity(), "une erreur est survenue. veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();
            TraceLog.e("Error loading agencies", result.getException());

        } else {


            envs = result.getData();


            envoiliste.setAdapter(new CollecteAdapter(this,
                    envoiliste, inflater, envs, getActivity()));

            onResume();





        }

        // refresh();


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


    public static List<Envoi> getenvois(Context context) {



        return EnvoiList;


    }



}
