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
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import com.example.karim.applicationfacteur.ui.main.Avise1;
import com.example.karim.applicationfacteur.ui.main.Livraison_entrep1;
import com.example.karim.applicationfacteur.ui.main.Loading1;
import com.example.karim.applicationfacteur.ui.main.QRcode;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.SignatureActivity;
import com.example.karim.applicationfacteur.ui.main.Testt;
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

import static com.example.karim.applicationfacteur.ui.main.myActivity.url_g;


public class AgencyListFragment2 extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResult1<List<Agency3>>>, UIListener  {


    private View myView;



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


    String login , pwd,des;

    UIListener ui= this;
    static ArrayList<Agency3> agencyList = new ArrayList<>();
    static ArrayList<Agency3> agencyList1 = new ArrayList<>();
    static ArrayList<String > agencyListpere = new ArrayList<>();
    static ArrayList<Agency3> agencyListpr = new ArrayList<>();
    static ArrayList<String> list = new ArrayList<>();
    static ArrayList<String > test = new ArrayList<>();



    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }

    public  EditText childViewAgenciesTitle;
    private ListView childViewAgenciesList;
    private LayoutInflater inflater;
    private Loader<AsyncResult1<List<Agency3>>> loader;
    private  Agency agency;
    private  Agency1 s;
    private  Agency3 agc;
    String code1,str;
    AgencyListFragment1 frag;
    private EditText searchBarcode;
    private AgencyListAdapter2 adapter1;
    public Button showSearch;

    public Button livraison;
    public ImageButton qr;
    EditText code_envoi;
    Integer total = 0 ;
    Integer prix = 0 ;
    String pere,size,pere1,code,size1,mode_liv;
    RadioButton cb,cb2;
    RadioGroup rd;
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


      //  ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

        this.inflater = inflater;




        if (myView == null) {


            myView = this.inflater.inflate(R.layout.agency_list_fragment2, null);
            childViewAgenciesTitle = (EditText) myView.findViewById(R.id.agencies_title);
            childViewAgenciesList = (ListView) myView.findViewById(R.id.agencies_list);
            showSearch = (Button) myView.findViewById(R.id.btn_show_search);
           // livraison = (Button) myView.findViewById(R.id.btn);
            rd= (RadioGroup) myView.findViewById(R.id.rdg);
            cb = (RadioButton) myView.findViewById(R.id.radioButton);
            cb2= (RadioButton) myView.findViewById(R.id.radioButton2);
            qr = (ImageButton) myView.findViewById(R.id.btn2);
            loader = getLoaderManager().initLoader(0, savedInstanceState, this);

            code_envoi =  (EditText) myView.findViewById(R.id.code);


            agencyList1.clear();
            agencyListpere.clear();
            db = new SQLiteHandler(getActivity());
            db.deletepr();
            agencyListpr.clear();

        }


        db = new SQLiteHandler(getActivity());



        agencyList.clear();
       if(agencyList.size()==0) {

           db = new SQLiteHandler(getActivity());
           Cursor cursor = db.getUserCmd2((new SessionManager(getActivity())).getUsername());

           if(cursor.getCount()==0)
           {

           }

           else
           {



           for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
               Agency3 agc = new Agency3(cursor.getString(1));
               agc.setNom_client(cursor.getString(2));
               agc.setTelephone_client(cursor.getString(3));
               agc.setAdresse_client(cursor.getString(4));
               agc.setCrbt(cursor.getString(5));
               agc.setStat(cursor.getString(6));
               agc.setPod(cursor.getString(10));
               agc.setService(cursor.getString(11));
               agc.setDesignation(cursor.getString(9));
               agc.setMode_liv(cursor.getString(12));

               agencyList.add(agc);
               agencies = agencyList;

               childViewAgenciesList.setAdapter(new AgencyListAdapter2(this,
                       childViewAgenciesList, inflater, agencies, getActivity()));
           }

           }
       }






  /*      agencyList.clear();

        refresh();

        db = new SQLiteHandler(getActivity());
        Cursor cursor = db.getUserCmd2((new SessionManager(getActivity())).getUsername());

        Toast.makeText(getActivity(),"size"+cursor.getCount(),Toast.LENGTH_SHORT).show();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Agency3 agc = new Agency3(cursor.getString(1));
            agc.setNom_client(cursor.getString(2));
            agc.setTelephone_client(cursor.getString(3));
            agc.setAdresse_client(cursor.getString(4));
            agc.setCrbt(cursor.getString(5));
            agc.setStat(cursor.getString(6));
            agc.setPod(cursor.getString(10));
            agc.setService(cursor.getString(11));
            agc.setDesignation(cursor.getString(9));
            agc.setMode_liv(cursor.getString(12));

            agencyList.add(agc);
            agencies=agencyList;

            childViewAgenciesList.setAdapter(new AgencyListAdapter2(this,
                    childViewAgenciesList, inflater, agencies,getActivity()));

        }*/



        // Toast.makeText(getActivity(), "bnjr", Toast.LENGTH_SHORT).show();



        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton:



                      /*  agencyListpere.clear();

                        if (agencyList1.size() == 0)
                        {
                            Toast.makeText(getActivity(),"Selectionez un envoi",Toast.LENGTH_SHORT).show();

                            cb.setChecked(false);

                        }

                        else {*/

                      if(agencyList.size()!=0) {


                          db = new SQLiteHandler(getActivity());

                          for (int j = 0; j < agencyList.size(); j++)

                          {
                              agencyListpr.add(agencyList.get(j));
                              Cursor cr = db.getpere(agencyList.get(j).getCode_envoi());


                              for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                  pere1 = cr.getString(0);

                              }


                              if (!pere1.isEmpty())

                              {


                                  Cursor crr = db.getsize(pere1);

                                  for (crr.moveToFirst(); !crr.isAfterLast(); crr.moveToNext()) {

                                      db.addDatapere(agencyList.get(j).getCode_envoi(), pere1, crr.getString(0));


                                  }

                              }


                          }


                          for (int k = 0; k < agencyList.size(); k++)

                          {
                              Cursor cr = db.getper(agencyList.get(k).getCode_envoi());
                              Log.e("envoi", agencyList.get(k).getCode_envoi());


                              if (cr.getCount() != 0) {

                                  for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                      pere = cr.getString(2);
                                      size = cr.getString(3);
                                      code = cr.getString(1);

                                      Log.e("code", cr.getString(1));
                                      Log.e("pere", cr.getString(2));


                                  }

                                  if (!pere.isEmpty()) {


                                      Cursor crr = db.getsize1(pere);


                                      for (crr.moveToFirst(); !crr.isAfterLast(); crr.moveToNext()) {

                                          size1 = crr.getString(0);

                                      }


                                      size1 = size1.replace(" ", "");
                                      size = size.replace(" ", "");


                                      if (!size1.equalsIgnoreCase(size)) {

                                          agencyListpr.remove(agencyList.get(k));

                                          if (!agencyListpere.contains(code)) {
                                              agencyListpere.add(code);

                                          }


                                      }


                                  }

                              }
                          }


                          if (agencyListpere.size() != 0) {


                              AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());


                              builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                              builderSingle.setTitle("Les envois groupés associés sont manquants");
                              //    builderSingle.T("ces envois ne peuvent pas être traité ,Les envois groupés associés sont manquants\"");


                              final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                      getActivity(), android.R.layout.simple_dropdown_item_1line);
                              //String str[]= code.split("\n");
                              for (int j = 0; j < agencyListpere.size(); j++) {

                                  arrayAdapter.add(agencyListpere.get(j));


                              }


                              builderSingle.setPositiveButton("Ok",
                                      new DialogInterface.OnClickListener() {

                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {


                                              if (agencyList.size() == agencyListpere.size())


                                              {


                                                  dialog.dismiss();


                                              } else {


                                                  if (agencyListpr.get(0).getMode_liv().equalsIgnoreCase("Point de relais")) {


                                                      if (registerInternetCheckReceiver()) {
                                                          InsertBatch(agencyListpr, "Point de relais");
                                                          Intent intent = new Intent(getActivity(), AgencyListActivity1.class);
                                                          startActivity(intent);
                                                          getActivity().finish();

                                                      }
                                                      else
                                                      {

                                                          db = new SQLiteHandler(getActivity());

                                                          for(int i = 0;i<agencyListpr.size();i++)

                                                          {

                                                              if (agencyListpr.get(i).getDesignation().equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || agencyListpr.get(i).getDesignation().equalsIgnoreCase("POD")) {
                                                                  des = "ZSD_SPOD";

                                                              } else {
                                                                  des = "ZSD_STAT";

                                                              }


                                                              db.addData3(agencyListpr.get(i).getCode_envoi(), login, "Livraison", "liv", des, "E0006", "E0010", "", "", "", "Point de relais", "", agencyListpr.get(i).getNom_client(), "", "", "");
                                                              db.updateDataglobal(agencyListpr.get(i).getCode_envoi(),"livré");

                                                      }

                                                          Intent intent = new Intent(getActivity(), AgencyListActivity1.class);
                                                          startActivity(intent);
                                                          getActivity().finish();

                                                  }} else {






                                                      Intent intent = new Intent();
                                                      intent.setClass(getActivity(), Livraison_entrep1.class);
                                                      intent.putExtra("siz", String.valueOf(size));
                                                      intent.putExtra("code", String.valueOf(""));
                                                      intent.putExtra("client", String.valueOf(agencyListpr.get(0).getNom_client()));
                                                      intent.putExtra("mode_liv", String.valueOf(agencyListpr.get(0).getMode_liv()));

                                                      intent.putExtra("designation", String.valueOf(""));
                                                      intent.putExtra("listagc", agencyListpr);
                                                      ;

                                                      startActivity(intent);
                                                      getActivity().finish();


                                                  }


                                              }


                                          }
                                      });


                              builderSingle.setNegativeButton("Annuler",
                                      new DialogInterface.OnClickListener() {

                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              // cb.setChecked(false);
                                              dialog.dismiss();


                                          }
                                      });


                              builderSingle.
                                      setAdapter(arrayAdapter,
                                              new DialogInterface.OnClickListener() {

                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which) {
                                                      String strName = arrayAdapter.getItem(which);


                                                  }
                                              });
                              builderSingle.create();
                              builderSingle.show();


                              ;

                          } else {


                              if (agencyListpr.get(0).getMode_liv().equalsIgnoreCase("Point de relais")) {
                                  InsertBatch(agencyListpr, "Point de relais");


                              } else

                              {
                                  Intent intent = new Intent();
                                  intent.setClass(getActivity(), Livraison_entrep1.class);
                                  intent.putExtra("siz", String.valueOf(size));
                                  intent.putExtra("code", String.valueOf(""));
                                  intent.putExtra("client", String.valueOf(agencyListpr.get(0).getNom_client()));
                                  intent.putExtra("mode_liv", String.valueOf(agencyListpr.get(0).getMode_liv()));
                                  intent.putExtra("designation", String.valueOf(""));
                                  intent.putExtra("listagc", agencyListpr);
                                  ;

                                  startActivity(intent);
                                  getActivity().finish();


                              }


                          }


                          //}
                      }

                      else
                      {
                          Toast.makeText(getActivity(), "Veuillez saisir des envois", Toast.LENGTH_SHORT).show();
                      }


                        // Toast.makeText(getActivity(), myList.get(0)+"hanane",Toast.LENGTH_SHORT).show();

                        rd.clearCheck();


                        break;

                    case R.id.radioButton2:



                     /*   agencyListpere.clear();

                        if (agencyList.size() == 0)
                        {

                            cb2.setChecked(false);
                            Toast.makeText(getActivity(),"Selectionez un envoi",Toast.LENGTH_SHORT).show();

                        }

                        else {*/



                 if(agencyList.size()!=0) {

                     for (int j = 0; j < agencyList.size(); j++)

                     {
                         agencyListpr.add(agencyList.get(j));
                         Cursor cr = db.getpere(agencyList.get(j).getCode_envoi());


                         for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                             pere1 = cr.getString(0);

                         }


                         if (!pere1.isEmpty())

                         {


                             Cursor crr = db.getsize(pere1);

                             for (crr.moveToFirst(); !crr.isAfterLast(); crr.moveToNext()) {

                                 db.addDatapere(agencyList.get(j).getCode_envoi(), pere1, crr.getString(0));


                             }

                         }


                     }


                     for (int k = 0; k < agencyList.size(); k++)

                     {
                         Cursor cr = db.getper(agencyList.get(k).getCode_envoi());
                         Log.e("envoi", agencyList.get(k).getCode_envoi());


                         if (cr.getCount() != 0) {

                             for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                 pere = cr.getString(2);
                                 size = cr.getString(3);
                                 code = cr.getString(1);

                                 Log.e("code", cr.getString(1));
                                 Log.e("pere", cr.getString(2));


                             }

                             if (!pere.isEmpty()) {


                                 Cursor crr = db.getsize1(pere);


                                 for (crr.moveToFirst(); !crr.isAfterLast(); crr.moveToNext()) {

                                     size1 = crr.getString(0);

                                 }


                                 size1 = size1.replace(" ", "");
                                 size = size.replace(" ", "");


                                 if (!size1.equalsIgnoreCase(size)) {

                                     agencyListpr.remove(agencyList.get(k));

                                     if (!agencyListpere.contains(code)) {
                                         agencyListpere.add(code);

                                     }


                                 }


                             }

                         }
                     }


                     if (agencyListpere.size() != 0) {


                         AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());


                         builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                         builderSingle.setTitle("Les envois groupés associés sont manquants");
                         //    builderSingle.T("ces envois ne peuvent pas être traité ,Les envois groupés associés sont manquants\"");


                         final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                 getActivity(), android.R.layout.simple_dropdown_item_1line);
                         //String str[]= code.split("\n");
                         for (int j = 0; j < agencyListpere.size(); j++) {
                             {
                                 arrayAdapter.add(agencyListpere.get(j));

                             }
                         }


                         builderSingle.setPositiveButton("Ok",
                                 new DialogInterface.OnClickListener() {

                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {


                                         if (agencyList.size() == agencyListpere.size())


                                         {

                                             dialog.dismiss();


                                         } else {


                                             Intent intent = new Intent();
                                             intent.setClass(getActivity(), Loading1.class);
                                             intent.putExtra("siz", String.valueOf(size));
                                             intent.putExtra("code", String.valueOf(""));
                                             intent.putExtra("client", String.valueOf(agencyListpr.get(0).getNom_client()));
                                             intent.putExtra("mode_liv", String.valueOf(agencyListpr.get(0).getMode_liv()));

                                             intent.putExtra("designation", String.valueOf(""));
                                             intent.putExtra("listagc", agencyListpr);
                                             ;

                                             startActivity(intent);
                                             getActivity().finish();


                                         }


                                     }
                                 });


                         builderSingle.setNegativeButton("Annuler",
                                 new DialogInterface.OnClickListener() {

                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {


                                         dialog.dismiss();

                                     }
                                 });


                         builderSingle.
                                 setAdapter(arrayAdapter,
                                         new DialogInterface.OnClickListener() {

                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 String strName = arrayAdapter.getItem(which);


                                             }
                                         });
                         builderSingle.create();
                         builderSingle.show();


                         ;

                     } else {


                         Intent intent = new Intent();
                         intent.setClass(getActivity(), Loading1.class);
                         intent.putExtra("siz", String.valueOf(size));
                         intent.putExtra("code", String.valueOf(""));
                         intent.putExtra("client", String.valueOf(agencyListpr.get(0).getNom_client()));
                         intent.putExtra("mode_liv", String.valueOf(agencyListpr.get(0).getMode_liv()));

                         intent.putExtra("designation", String.valueOf(""));
                         intent.putExtra("listagc", agencyListpr);
                         ;

                         startActivity(intent);
                         getActivity().finish();


                     }


                     //  }

                 }

                      else
                     {
                         Toast.makeText(getActivity(), "Veuillez saisir des envois", Toast.LENGTH_SHORT).show();
                     }

                        rd.clearCheck();

                        break;

                    case R.id.radioButton3:

                       if(agencyList.size()!=0) {

                           for (int j = 0; j < agencyList.size(); j++)

                           {
                               agencyListpr.add(agencyList.get(j));
                               Cursor cr = db.getpere(agencyList.get(j).getCode_envoi());


                               for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                   pere1 = cr.getString(0);

                               }


                               if (!pere1.isEmpty())

                               {


                                   Cursor crr = db.getsize(pere1);

                                   for (crr.moveToFirst(); !crr.isAfterLast(); crr.moveToNext()) {

                                       db.addDatapere(agencyList.get(j).getCode_envoi(), pere1, crr.getString(0));


                                   }

                               }


                           }


                           for (int k = 0; k < agencyList.size(); k++)

                           {
                               Cursor cr = db.getper(agencyList.get(k).getCode_envoi());
                               Log.e("envoi", agencyList.get(k).getCode_envoi());


                               if (cr.getCount() != 0) {

                                   for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                       pere = cr.getString(2);
                                       size = cr.getString(3);
                                       code = cr.getString(1);

                                       Log.e("code", cr.getString(1));
                                       Log.e("pere", cr.getString(2));


                                   }

                                   if (!pere.isEmpty()) {


                                       Cursor crr = db.getsize1(pere);


                                       for (crr.moveToFirst(); !crr.isAfterLast(); crr.moveToNext()) {

                                           size1 = crr.getString(0);

                                       }


                                       size1 = size1.replace(" ", "");
                                       size = size.replace(" ", "");


                                       if (!size1.equalsIgnoreCase(size)) {

                                           agencyListpr.remove(agencyList.get(k));

                                           if (!agencyListpere.contains(code)) {
                                               agencyListpere.add(code);

                                           }


                                       }


                                   }

                               }
                           }


                           if (agencyListpere.size() != 0) {


                               AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());


                               builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                               builderSingle.setTitle("Les envois groupés associés sont manquants");
                               //    builderSingle.T("ces envois ne peuvent pas être traité ,Les envois groupés associés sont manquants\"");


                               final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                       getActivity(), android.R.layout.simple_dropdown_item_1line);
                               //String str[]= code.split("\n");
                               for (int j = 0; j < agencyListpere.size(); j++) {
                                   {
                                       arrayAdapter.add(agencyListpere.get(j));

                                   }
                               }


                               builderSingle.setPositiveButton("Ok",
                                       new DialogInterface.OnClickListener() {

                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {


                                               if (agencyList.size() == agencyListpere.size())


                                               {

                                                   dialog.dismiss();


                                               } else {


                                                   Intent intent = new Intent();
                                                   intent.setClass(getActivity(),Avise1.class);
                                                   intent.putExtra("siz", String.valueOf(size));
                                                   intent.putExtra("code", String.valueOf(""));
                                                   intent.putExtra("client", String.valueOf(agencyListpr.get(0).getNom_client()));
                                                   intent.putExtra("mode_liv", String.valueOf(agencyListpr.get(0).getMode_liv()));

                                                   intent.putExtra("designation", String.valueOf(""));
                                                   intent.putExtra("listagc", agencyListpr);
                                                   ;

                                                   startActivity(intent);
                                                   getActivity().finish();


                                               }


                                           }
                                       });


                               builderSingle.setNegativeButton("Annuler",
                                       new DialogInterface.OnClickListener() {

                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {


                                               dialog.dismiss();

                                           }
                                       });


                               builderSingle.
                                       setAdapter(arrayAdapter,
                                               new DialogInterface.OnClickListener() {

                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       String strName = arrayAdapter.getItem(which);


                                                   }
                                               });
                               builderSingle.create();
                               builderSingle.show();


                               ;

                           } else {


                               Intent intent = new Intent();
                               intent.setClass(getActivity(), Avise1.class);
                               intent.putExtra("siz", String.valueOf(size));
                               intent.putExtra("code", String.valueOf(""));
                               intent.putExtra("client", String.valueOf(agencyListpr.get(0).getNom_client()));
                               intent.putExtra("mode_liv", String.valueOf(agencyListpr.get(0).getMode_liv()));

                               intent.putExtra("designation", String.valueOf(""));
                               intent.putExtra("listagc", agencyListpr);
                               ;

                               startActivity(intent);
                               getActivity().finish();


                           }


                           //  }

                       }

                       else
                       {
                           Toast.makeText(getActivity(), "Veuillez saisir des envois", Toast.LENGTH_SHORT).show();
                       }

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






        showSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();

               for(int i =0;i<agencyList.size();i++)

                {
                    if (!list.contains(agencyList.get(i).getCode_envoi().toUpperCase())) {

                        list.add(agencyList.get(i).getCode_envoi().toUpperCase());

                    }

                }

                str = code_envoi.getText().toString().replace(" " , "").replace("#", "").toUpperCase();

                db.deleteper(str);

                db = new SQLiteHandler(getActivity());

                Cursor cursor1 = db.getAllCode(str);

// si l'envoi n'existe pas dans la liste encours des utilisateurs "un msg s'affiche ENVOI indisponible
                if(cursor1.getCount()==0) {

                    Toast.makeText(getActivity(), "Envoi indisponible", Toast.LENGTH_SHORT).show();
                    code_envoi.setText("");

                }

                else

                {
                    if (agencyList.size() == 0) {


                       Cursor cr =  db.getmode_liv(str);
                        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                            mode_liv = cr.getString(0);

                        }
                            SharedPreferences prefs = getActivity().getSharedPreferences("mode_liv", Context.MODE_PRIVATE);
                        prefs.edit().putString("liv",String.valueOf(mode_liv)).commit();



                        Cursor cursor = db.getpere(str);
                        if (cursor.getCount() != 0)


                        {

                            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                if (cursor1.getString(1).equalsIgnoreCase(str))

                                {


                                    if (agencyList.size() == 0)

                                    {
                                        agc = new Agency3(str.toUpperCase());
                                        agc.setNom_client(cursor1.getString(2));
                                        agc.setTelephone_client(cursor1.getString(3));
                                        agc.setAdresse_client(cursor1.getString(4));
                                        agc.setCrbt(cursor1.getString(5));
                                        agc.setPod(cursor1.getString(9));
                                        agc.setService(cursor1.getString(10));
                                        agc.setDesignation(cursor1.getString(11));
                                        agc.setMode_liv(cursor1.getString(12));
                                        agc.setAdr_relais(cursor1.getString(17));
                                        agc.setRelais(cursor1.getString(18));
                                        agencyList.add(agc);

                                        for (int i = 0; i < agencyList.size(); i++) {
                                            list.add(agencyList.get(i).getCode_envoi());
                                            db.updateDataglobal2(str, "LOT");

                                        }


                                    } else {
                                        agc = new Agency3(str.toUpperCase());
                                        agc.setNom_client(cursor1.getString(2));
                                        agc.setTelephone_client(cursor1.getString(3));
                                        agc.setAdresse_client(cursor1.getString(4));
                                        agc.setCrbt(cursor1.getString(5));
                                        agc.setPod(cursor1.getString(9));
                                        agc.setService(cursor1.getString(10));
                                        agc.setDesignation(cursor1.getString(11));
                                        agc.setMode_liv(cursor1.getString(12));
                                        agc.setAdr_relais(cursor1.getString(17));
                                        agc.setRelais(cursor1.getString(18));


                                        if (list.contains(str)) {


                                        } else {


                                            agencyList.add(agc);
                                            db.updateDataglobal2(str, "LOT");

                                            for (int i = 0; i < agencyList.size(); i++) {
                                                list.add(agencyList.get(i).getCode_envoi());

                                            }

                                        }
                                    }


                                    code_envoi.setText("");

                                }


                            }


                        } else {

                            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                if (cursor1.getString(1).equalsIgnoreCase(str))

                                {


                                    agc = new Agency3(cursor1.getString(1).toUpperCase());
                                    agc.setNom_client(cursor1.getString(2));
                                    agc.setTelephone_client(cursor1.getString(3));
                                    agc.setAdresse_client(cursor1.getString(4));
                                    agc.setCrbt(cursor1.getString(5));
                                    agc.setPod(cursor1.getString(9));
                                    agc.setService(cursor1.getString(10));
                                    agc.setDesignation(cursor1.getString(11));
                                    agc.setMode_liv(cursor1.getString(12));
                                    agc.setAdr_relais(cursor1.getString(17));
                                    agc.setRelais(cursor1.getString(18));

       //si l'envoi a un service POD ou crbt(ccp /courrier) : un msg s'affiche  cet envoi ne peut être traité en lot
                                    // agc.getService : service crbt
                                    //agc.getPod : service POD

                                    if (agc.getService().isEmpty() && agc.getPod().isEmpty())

                                    {


                                        if (agencyList.size() == 0)

                                        {
                                            agc = new Agency3(str.toUpperCase());
                                            agc.setNom_client(cursor1.getString(2));
                                            agc.setTelephone_client(cursor1.getString(3));
                                            agc.setAdresse_client(cursor1.getString(4));
                                            agc.setCrbt(cursor1.getString(5));
                                            agc.setPod(cursor1.getString(9));
                                            agc.setService(cursor1.getString(10));
                                            agc.setDesignation(cursor1.getString(11));
                                            agc.setMode_liv(cursor1.getString(12));
                                            agc.setAdr_relais(cursor1.getString(17));
                                            agc.setRelais(cursor1.getString(18));
                                            agencyList.add(agc);
                                            for (int i = 0; i < agencyList.size(); i++) {
                                                list.add(agencyList.get(i).getCode_envoi());
                                                db.updateDataglobal2(str, "LOT");

                                            }

                                        } else {
                                            agc = new Agency3(str.toUpperCase());
                                            agc.setNom_client(cursor1.getString(2));
                                            agc.setTelephone_client(cursor1.getString(3));
                                            agc.setAdresse_client(cursor1.getString(4));
                                            agc.setCrbt(cursor1.getString(5));
                                            agc.setPod(cursor1.getString(9));
                                            agc.setService(cursor1.getString(10));
                                            agc.setDesignation(cursor1.getString(11));
                                            agc.setMode_liv(cursor1.getString(12));
                                            agc.setAdr_relais(cursor1.getString(17));
                                            agc.setRelais(cursor1.getString(18));
                                            if (list.contains(str)) {


                                            } else {


                                                agencyList.add(agc);
                                                db.updateDataglobal2(str, "LOT");

                                                for (int i = 0; i < agencyList.size(); i++) {
                                                    list.add(agencyList.get(i).getCode_envoi());

                                                }

                                            }
                                        }


                                    } else {

                                        Toast.makeText(getActivity(), "cet envoi ne peut être traité en lot ", Toast.LENGTH_SHORT).show();


                                    }


                                    code_envoi.setText("");

                                }


                            }


                        }


                    }




                    else
                    {

                        Cursor cr =  db.getmode_liv(str);
                        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                            mode_liv = cr.getString(0);

                        }



                        SharedPreferences sp = getActivity().getSharedPreferences("mode_liv", Context.MODE_PRIVATE);
                        sp.getString("liv", null);

                        Log.e("mode_liv",mode_liv);
                        Log.e("liv",sp.getString("liv", null));


                       if(mode_liv.equalsIgnoreCase(sp.getString("liv", null)))
                        {

                            Cursor cursor = db.getpere(str);
                            if (cursor.getCount() != 0)


                            {

                                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                    if (cursor1.getString(1).equalsIgnoreCase(str))

                                    {


                                        if (agencyList.size() == 0)

                                        {
                                            agc = new Agency3(str.toUpperCase());
                                            agc.setNom_client(cursor1.getString(2));
                                            agc.setTelephone_client(cursor1.getString(3));
                                            agc.setAdresse_client(cursor1.getString(4));
                                            agc.setCrbt(cursor1.getString(5));
                                            agc.setPod(cursor1.getString(9));
                                            agc.setService(cursor1.getString(10));
                                            agc.setDesignation(cursor1.getString(11));
                                            agc.setMode_liv(cursor1.getString(12));
                                            agc.setAdr_relais(cursor1.getString(17));
                                            agc.setRelais(cursor1.getString(18));
                                            agencyList.add(agc);

                                            for (int i = 0; i < agencyList.size(); i++) {
                                                list.add(agencyList.get(i).getCode_envoi());
                                                db.updateDataglobal2(str, "LOT");

                                            }


                                        } else {
                                            agc = new Agency3(str.toUpperCase());
                                            agc.setNom_client(cursor1.getString(2));
                                            agc.setTelephone_client(cursor1.getString(3));
                                            agc.setAdresse_client(cursor1.getString(4));
                                            agc.setCrbt(cursor1.getString(5));
                                            agc.setPod(cursor1.getString(9));
                                            agc.setService(cursor1.getString(10));
                                            agc.setDesignation(cursor1.getString(11));
                                            agc.setMode_liv(cursor1.getString(12));
                                            agc.setAdr_relais(cursor1.getString(17));
                                            agc.setRelais(cursor1.getString(18));


                                            if (list.contains(str)) {


                                            } else {


                                                agencyList.add(agc);
                                                db.updateDataglobal2(str, "LOT");

                                                for (int i = 0; i < agencyList.size(); i++) {
                                                    list.add(agencyList.get(i).getCode_envoi());

                                                }

                                            }
                                        }


                                        code_envoi.setText("");

                                    }


                                }


                            } else {

                                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                                    if (cursor1.getString(1).equalsIgnoreCase(str))

                                    {


                                        agc = new Agency3(cursor1.getString(1).toUpperCase());
                                        agc.setNom_client(cursor1.getString(2));
                                        agc.setTelephone_client(cursor1.getString(3));
                                        agc.setAdresse_client(cursor1.getString(4));
                                        agc.setCrbt(cursor1.getString(5));
                                        agc.setPod(cursor1.getString(9));
                                        agc.setService(cursor1.getString(10));
                                        agc.setDesignation(cursor1.getString(11));
                                        agc.setMode_liv(cursor1.getString(12));
                                        agc.setAdr_relais(cursor1.getString(17));
                                        agc.setRelais(cursor1.getString(18));

                                        if (agc.getService().isEmpty() && agc.getPod().isEmpty())

                                        {


                                            if (agencyList.size() == 0)

                                            {
                                                agc = new Agency3(str.toUpperCase());
                                                agc.setNom_client(cursor1.getString(2));
                                                agc.setTelephone_client(cursor1.getString(3));
                                                agc.setAdresse_client(cursor1.getString(4));
                                                agc.setCrbt(cursor1.getString(5));
                                                agc.setPod(cursor1.getString(9));
                                                agc.setService(cursor1.getString(10));
                                                agc.setDesignation(cursor1.getString(11));
                                                agc.setMode_liv(cursor1.getString(12));
                                                agc.setAdr_relais(cursor1.getString(17));
                                                agc.setRelais(cursor1.getString(18));
                                                agencyList.add(agc);
                                                for (int i = 0; i < agencyList.size(); i++) {
                                                    list.add(agencyList.get(i).getCode_envoi());
                                                    db.updateDataglobal2(str, "LOT");

                                                }

                                            } else {
                                                agc = new Agency3(str);
                                                agc.setNom_client(cursor1.getString(2));
                                                agc.setTelephone_client(cursor1.getString(3));
                                                agc.setAdresse_client(cursor1.getString(4));
                                                agc.setCrbt(cursor1.getString(5));
                                                agc.setPod(cursor1.getString(9));
                                                agc.setService(cursor1.getString(10));
                                                agc.setDesignation(cursor1.getString(11));
                                                agc.setMode_liv(cursor1.getString(12));
                                                agc.setAdr_relais(cursor1.getString(17));
                                                agc.setRelais(cursor1.getString(18));
                                                if (list.contains(str)) {


                                                } else {


                                                    agencyList.add(agc);
                                                    db.updateDataglobal2(str, "LOT");

                                                    for (int i = 0; i < agencyList.size(); i++) {
                                                        list.add(agencyList.get(i).getCode_envoi());

                                                    }

                                                }
                                            }


                                        } else {

                                            Toast.makeText(getActivity(), "cet envoi ne peut être traité en lot ", Toast.LENGTH_SHORT).show();


                                        }


                                        code_envoi.setText("");

                                    }


                                }


                            }







                       }


                        else
                        {
                            Toast.makeText(getActivity(),"cet envoi est rejeté, mode de livraison est different",Toast.LENGTH_SHORT).show();
                        }






                    }


                }






            }




        });



        /*


            livraison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (agencyList1.size() == 0)
                {
                    Toast.makeText(getActivity(),"Selectionez un envoi",Toast.LENGTH_SHORT).show();

                }

                else {





                    agc = new Agency3(agencyList.get(0).getCode_envoi());
                    agc.setNom_client(agencyList1.get(0).getNom_client());

                    agc.setTelephone_client(agencyList1.get(0).getTelephone_client());
                    agc.setAdresse_client(agencyList1.get(0).getAdresse_client());
                    agc.setDesignation(agencyList1.get(0).getDesignation());
                    agc.setCrbt("");
                    agc.setMode_liv(agencyList1.get(0).getMode_liv());
                    agc.setAdr_relais(agencyList1.get(0).getAdr_relais());
                    agc.setRelais(agencyList1.get(0).getRelais());
                    agc.setAgc(agencyList1.get(0).getAgc());
                    agc.setAdr_agc(agencyList1.get(0).getAdr_agc());




                    Intent intent = new Intent();
                    intent.setClass(getActivity(), AgencyActivity2.class);
                    intent.putExtra("AgencySelected", agc);
                    intent.putExtra("listagc", agencyList1);



                    // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    getActivity().finish();
                }

            }




        });






         */





















       /* livraison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                agencyListpere.clear();

                if (agencyList1.size() == 0)
                {
                    Toast.makeText(getActivity(),"Selectionez un envoi",Toast.LENGTH_SHORT).show();

                }

                else {

                    for(int i =0;i<agencyList1.size();i++)

                    {
                           agencyListpr.add(agencyList1.get(i));

                    }




                        for (int i = 0 ; i <agencyList1.size();i++)

                    {
                        Cursor cr = db.getper(agencyList1.get(i).getCode_envoi());
                        Log.e("envoi", agencyList1.get(i).getCode_envoi());



                        if(cr.getCount()!=0) {

                            for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                pere = cr.getString(2);
                                size = cr.getString(3);
                                code = cr.getString(1);

                               Log.e("code",cr.getString(1));
                               Log.e("pere",cr.getString(2));



                            }

                            if (!pere.isEmpty()) {


                                Cursor crr = db.getsize1(pere);


                                for (crr.moveToFirst(); !crr.isAfterLast(); crr.moveToNext()) {

                                    size1 = crr.getString(0);

                                }


                                size1 = size1.replace(" ", "");
                                size = size.replace(" ", "");


                                if (!size1.equalsIgnoreCase(size)) {

                                     agencyListpr.remove(agencyList1.get(i));

                                    if (!agencyListpere.contains(code)) {
                                        agencyListpere.add(code);

                                    }






                                }


                            }

                        }
                    }



                    if(agencyListpere.size()!=0) {


                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());


                        builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                        builderSingle.setTitle("Les envois groupés associés sont manquants");
                    //    builderSingle.T("ces envois ne peuvent pas être traité ,Les envois groupés associés sont manquants\"");


                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_dropdown_item_1line);
                        //String str[]= code.split("\n");
                        for (int j = 0; j < agencyListpere.size(); j++) {
                            {
                                arrayAdapter.add(agencyListpere.get(j));

                            }
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



                                            if(agencyList1.size()==agencyListpere.size())



                                            {
                                                dialog.dismiss();

                                            }



                                            else {


                                                agc = new Agency3("");
                                                agc.setNom_client("");

                                                agc.setTelephone_client("");
                                                agc.setAdresse_client("");
                                                agc.setDesignation("");
                                                agc.setCrbt("");
                                                agc.setMode_liv("");
                                                agc.setAdr_relais("");
                                                agc.setRelais("");
                                                agc.setAgc("");
                                                agc.setAdr_agc("");


                                                Intent intent = new Intent();
                                                intent.setClass(getActivity(), AgencyActivity2.class);
                                                intent.putExtra("AgencySelected", agc);
                                                intent.putExtra("listagc", agencyListpr);
                                                startActivity(intent);
                                                getActivity().finish();


                                            }




                                        }
                                    });


                            builderSingle.
                                    setAdapter(arrayAdapter,
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String strName = arrayAdapter.getItem(which);


                                                }
                                            });
                            builderSingle.create();
                            builderSingle.show();



                        ;

                    }

                    else
                    {

                        agc = new Agency3("");
                        agc.setNom_client("");

                        agc.setTelephone_client("");
                        agc.setAdresse_client("");
                        agc.setDesignation("");
                        agc.setCrbt("");
                        agc.setMode_liv("");
                        agc.setAdr_relais("");
                        agc.setRelais("");
                        agc.setAgc("");
                        agc.setAdr_agc("");


                                       Intent intent = new Intent();
                                            intent.setClass(getActivity(), AgencyActivity2.class);
                                            intent.putExtra("AgencySelected", agc);
                                            intent.putExtra("listagc", agencyListpr);
                                            startActivity(intent);
                                            getActivity().finish();



                        for (int i = 0; i < agencyList1.size(); i++) {
                           // Log.e("hann",agencyList1.get(i).getCode_envoi());

                        }



                    }







                }

            }




        });*/


        qr.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {

                                      AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                                      CharSequence items[] = new CharSequence[] {"virgule", "point virgule", "espace","deux point","retour à la ligne"};
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


       Intent intent = new Intent();
                intent.setClass(getActivity(), QRcode.class);
                intent.putExtra("AgencySelected", agencyList);
                intent.putExtra("sep",",");
                // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                getActivity().finish();

                                                              break;
                                                          case 1:


                                                              Intent intent1 = new Intent();
                intent1.setClass(getActivity(), QRcode.class);
                intent1.putExtra("AgencySelected", agencyList);
                intent1.putExtra("sep",",");
                // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                startActivity(intent1);
                getActivity().finish();

                                                          case 2: // remind me
                                                              //

                                                              Intent intent2 = new Intent();
                                                              intent2.setClass(getActivity(), QRcode.class);
                                                              intent2.putExtra("AgencySelected", agencyList);
                                                              intent2.putExtra("sep",";");
                                                              startActivity(intent2);
                                                              getActivity().finish();




                                                              break;
                                                          case 3: // add to calendar
                                                              //

                                                              Intent intent3 = new Intent();
                                                              intent3.setClass(getActivity(), QRcode.class);
                                                              intent3.putExtra("AgencySelected", agencyList);
                                                              intent3.putExtra("sep","");
                                                              // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                                              startActivity(intent3);
                                                              getActivity().finish();


                                                              break;
                                                          case 4:

                                                              Intent intent4 = new Intent();
                                                              intent4.setClass(getActivity(), QRcode.class);
                                                              intent4.putExtra("AgencySelected", agencyList);
                                                              intent4.putExtra("sep",":");
                                                              // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                                              startActivity(intent4);
                                                              getActivity().finish();

                                                              // add to calendar
                                                              //
                                                              break;
                                                          case 5: // add to calendar
                                                              Intent intent5 = new Intent();
                                                              intent5.setClass(getActivity(), QRcode.class);
                                                              intent5.putExtra("AgencySelected", agencyList);
                                                              intent5.putExtra("sep","rtr");
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






     /*   Intent intent = new Intent();
                intent.setClass(getActivity(), QRcode.class);
                intent.putExtra("AgencySelected", agencyList);
                // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                getActivity().finish();*/

            }




        });






        return myView;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter1 = new AgencyListAdapter2(this, childViewAgenciesList, inflater, agencies, getActivity());
        childViewAgenciesList.setAdapter(adapter1);

       /* db = new SQLiteHandler(getActivity());
        db.deletepr();
*/







        // refresh();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        // refresh();

    }



    public void onSaveRequested(){
       // adapter1.txt.setTextColor(Color.RED);
       /* Intent intent = new Intent();
        intent.setClass(getActivity(), AgencyActivity1.class);

        startActivity(intent);
        getActivity().finish();
*/
    }



    public void onAgencySelected(final Agency3 agc,View v) {

    /*    v.setBackgroundColor(Color.WHITE);
        if(!agencyList1.contains(agc))
        {
            agencyList1.add(agc);

        }

       // list.clear();







        Cursor cr = db.getpere(agc.getCode_envoi());


        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                      pere1=cr.getString(0);

            }


        if(!pere1.isEmpty())

        {


                Cursor crr = db.getsize(pere1);

                for (crr.moveToFirst(); !crr.isAfterLast(); crr.moveToNext()) {

                    db.addDatapere(agc.getCode_envoi(),pere1,crr.getString(0));


                }

        }



*/


    }

    public void onAgencySelected1(final Agency3 agc,View v) {


     /*   v.setBackgroundColor(Color.TRANSPARENT);
        agencyList1.remove(agc);
        db.deleteper(agc.getCode_envoi());


*/
    }





    public void onRefreshRequested(){
        refresh();

    }

    @Override
    public Loader<AsyncResult1<List<Agency3>>> onCreateLoader(int id, Bundle args) {

        return new AgencyListDataAsyncLoader2(getActivity());

    }

    @Override
    public void onLoadFinished(Loader<AsyncResult1<List<Agency3>>> listLoader,
                               AsyncResult1<List<Agency3>> result) {
        if (result.getException() != null || result.getData() == null) {
            Toast.makeText(getActivity(), "une erreur est survenue. veuillez réessayer ultérieurement", Toast.LENGTH_SHORT).show();
            TraceLog.e("Error loading agencies", result.getException());

        } else {




            agencies = result.getData();




            childViewAgenciesList.setAdapter(new AgencyListAdapter2(this,
                    childViewAgenciesList, inflater, agencies,getActivity()));
            //childViewAgenciesTitle.setText("Envois En Cours: "+ agencies.size());
            onResume();
            // refresh();

        }

        // refresh();


    }



    @Override
    public void onLoaderReset(Loader<AsyncResult1<List<Agency3>>> listLoader) {

        //  refresh();

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











    public void InsertBatch(List<Agency3> list, String ModeLiv)

    {

        /*db = new SQLiteHandler(getApplicationContext());

        Cursor cursor = db.getAll2();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            login = cursor.getString(1);
            pwd = cursor.getString(2);

        }
        */
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
         //   url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
          //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
            url = new URL(url_g);
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
        ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.Affectation");

        ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

        batchItem.setResourcePath("AffectationSet");


        batchItem.setMode(ODataRequestParamSingle.Mode.Create);

        batchItem.setCustomTag("something to identify the request");


        for (int i = 0; i<list.size();i++) {


            //Toast.makeText(getApplicationContext(), "Code Envoi :" + cursor.getString(1), Toast.LENGTH_SHORT).show();

            newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre",list.get(i).getCode_envoi()));
            newEntity.getProperties().put("Facteur", new ODataPropertyDefaultImpl("Facteur", login));
            newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "Livraison"));
            newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("txt04", "liv"));


            if(list.get(i).getDesignation().equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || list.get(i).getDesignation().equalsIgnoreCase("POD"))
            {
                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_SPOD"));
            }

            else
            {
                newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

            }
            newEntity.getProperties().put("Statprec", new ODataPropertyDefaultImpl("Statprec", "E0006"));

            newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat","E0010"));
            newEntity.getProperties().put("Motif", new ODataPropertyDefaultImpl("Motif",""));
            newEntity.getProperties().put("Mesure", new ODataPropertyDefaultImpl("Mesure",""));
            newEntity.getProperties().put("ModePaiement", new ODataPropertyDefaultImpl("ModePaiement",""));
            newEntity.getProperties().put("TypePid", new ODataPropertyDefaultImpl("TypePid",""));
            newEntity.getProperties().put("Pid", new ODataPropertyDefaultImpl("Pid",""));
            newEntity.getProperties().put("Destinataire", new ODataPropertyDefaultImpl("Destinataire",list.get(i).getNom_client()));
            newEntity.getProperties().put("ModeLiv", new ODataPropertyDefaultImpl("ModeLiv",ModeLiv));


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

                            db = new SQLiteHandler(getActivity());

                            for (int i =0; i<list.size();i++) {


                                db.updateDataglobal(list.get(i).getCode_envoi(),"livré");
                                db.deleteper(list.get(i).getCode_envoi());
                            }
                            db.deleteData3();




                            Intent intent = new Intent();
                            intent.setClass(getActivity(),AgencyListActivity1.class);
                            startActivity(intent);
                            getActivity().finish();





                        } else {

                            Toast.makeText(getActivity(),"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();

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













}
