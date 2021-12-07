package com.example.karim.applicationfacteur.ui.online;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.karim.applicationfacteur.ui.main.Livraison_entrep1;
import com.example.karim.applicationfacteur.ui.main.Loading1;
import com.example.karim.applicationfacteur.ui.main.MapsActivity;
import com.example.karim.applicationfacteur.ui.main.Raison;
import com.example.karim.applicationfacteur.ui.main.Raison1;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
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


public class AgencyFragment2 extends Fragment implements UIListener {


    private boolean internetConnected = true;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean a;
    private EditText telephone, adresse,service,pod,designation,cab,mode_liv,crbt,nom,relais,adr_relais;
    SessionManager session;
    LogonCoreContext lgtx;

    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }

    private View myView;

    private Button btn1,btn2;
    private LayoutInflater inflater;
    private Agency3 agc;
    Button liv,nonliv;
    static  int nbliv=0 ;
    int  nbnonliv = 0;
    String login,pwd;
    String size;
    SQLiteHandler db;
    RadioButton cb,cb2;
    RadioGroup rd;
    List<Agency3> agencyListt = new ArrayList<>();



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
            myView = this.inflater.inflate(R.layout.agency_fragment1, null);

            nom = (EditText) myView.findViewById(R.id.des);
            telephone= (EditText) myView.findViewById(R.id.teleph);
            relais = (EditText) myView.findViewById(R.id.relais);
            adr_relais = (EditText) myView.findViewById(R.id.adr_relais);




            adresse = (EditText) myView.findViewById(R.id.adr);
            rd= (RadioGroup) myView.findViewById(R.id.rdg);
            cb = (RadioButton) myView.findViewById(R.id.radioButton);
            cb2= (RadioButton) myView.findViewById(R.id.radioButton2);
           /* liv = (Button) myView.findViewById(R.id.liv);
            nonliv = (Button) myView.findViewById(R.id.nonliv);*/


        }



        //When user select an agency, the agency will be passed as an intent
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            agc = bundle.getParcelable("AgencySelected");
            //agencyListt = bundle.getParcelable("listagc");

            initializeViews();
        } else {
            //initializeAgencyId();
        }

        Bundle bundle1 =  getActivity().getIntent().getExtras();

        //Extract the data…
        size = bundle1.getString("siz");

        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton:


                        final ArrayList<Agency3> myList = (ArrayList<Agency3>) getActivity().getIntent().getSerializableExtra("listagc");





                            Intent intent = new Intent();
                            intent.setClass(getActivity(), Livraison_entrep1.class);
                            intent.putExtra("val", String.valueOf(nbliv));
                            intent.putExtra("val1", String.valueOf(nbnonliv));
                            intent.putExtra("siz", String.valueOf(size));
                            intent.putExtra("code", String.valueOf(agc.getCode_envoi()));
                            intent.putExtra("client", String.valueOf(agc.getNom_client()));
                            intent.putExtra("mode_liv", String.valueOf(agc.getMode_liv()));

                            intent.putExtra("designation", String.valueOf(agc.getDesignation()));
                            intent.putExtra("listagc", myList);

                            startActivity(intent);
                            getActivity().finish();




                        // Toast.makeText(getActivity(), myList.get(0)+"hanane",Toast.LENGTH_SHORT).show();


                        break;
                    case R.id.radioButton2:
                        nbnonliv++;

                        final ArrayList<Agency3> myList1 = (ArrayList<Agency3>) getActivity().getIntent().getSerializableExtra("listagc");



                        intent = new Intent();
                        intent.setClass(getActivity(),Raison1.class);
                        intent.putExtra("listagc",myList1);

                        intent.putExtra("val", String.valueOf(nbliv));
                        intent.putExtra("val1", String.valueOf(nbnonliv));
                        intent.putExtra("siz", String.valueOf(size));
                        intent.putExtra("code", String.valueOf(agc.getCode_envoi()));
                        intent.putExtra("client", String.valueOf(agc.getNom_client()));
                        intent.putExtra("mode_liv", String.valueOf(agc.getMode_liv()));

                        intent.putExtra("designation", String.valueOf(agc.getDesignation()));

                        startActivity(intent);
                        getActivity().finish();

                        break;

                }
            }
        });


      /*  Button val = (Button) myView.findViewById(R.id.button2);


        val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((myActivity)getActivity()).registerInternetCheckReceiver())
                    Toast.makeText(getActivity(), "Connecté", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Non Connecté", Toast.LENGTH_SHORT).show();

                if(cb.isChecked())
                {



                   nbliv++;
                    Toast.makeText(getActivity(),"val"+nbliv,Toast.LENGTH_SHORT).show();
                    db = new SQLiteHandler(getActivity());
                    Cursor cursor1 = db.getAll();

                    for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                        login = cursor1.getString(1);
                        pwd = cursor1.getString(2);

                    }


                    db.addData3(agency.getCode_envoi(),login,"LIVRAISON","Liv", "ZSD_STAT","E0006","E0010","");

                    //((myActivity)getActivity()).InsertBatch("");


                    Intent intent = new Intent();
                    intent.setClass(getActivity(),Livraison_entrep.class);
                    intent.putExtra("val",String.valueOf(nbliv));
                    intent.putExtra("val1",String.valueOf(nbnonliv));
                    intent.putExtra("siz",String.valueOf(size));
                    intent.putExtra("code",String.valueOf(agency.getCode_envoi()));
                    intent.putExtra("client",String.valueOf(agency.getNom_client()));

                    startActivity(intent);
                    getActivity().finish();




                }

                else if(cb2.isChecked()) {
                    nbnonliv++;
                    db = new SQLiteHandler(getActivity());
                    Cursor cursor1 = db.getAll();

                    for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                        login = cursor1.getString(1);
                        pwd = cursor1.getString(2);

                    }

                    db.addData3(agency.getCode_envoi(), login, "AFFECTE GUICHET", "affg", "ZSD_STAT", "E0006", "E0005", "");


                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Raison.class);

                    intent.putExtra("val", String.valueOf(nbliv));
                    intent.putExtra("val1", String.valueOf(nbnonliv));
                    intent.putExtra("siz", String.valueOf(size));
                    intent.putExtra("code",String.valueOf(agency.getCode_envoi()));
                    intent.putExtra("client",String.valueOf(agency.getNom_client()));

                    startActivity(intent);
                    getActivity().finish();


                }


            }
        });*/
        return myView;
    }


    /*private void initializeAgencyId(){
        Random rand = new Random();;
        int min = 10000000, max = 99999999;

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt(max - min + 1) + min;
        childViewAgencyID.setText(String.valueOf(randomNum));

    }
    */
    private void initializeViews(){


        if (agc!=null) {









            nom.setText(agc.getNom_client());
            nom.setFocusable(false);
            telephone.setText(agc.getTelephone_client());
            telephone.setFocusable(false);
            adresse.setText(agc.getAdresse_client());
            adresse.setFocusable(false);




          /*  if(agc.getMode_liv().equalsIgnoreCase("Point de relais"))
            {
                relais.setText(agc.getRelais());
                relais.setFocusable(false);
                adr_relais.setText(agc.getAdr_relais());
                adr_relais.setFocusable(false);
            }
            else
            {
                relais.setVisibility(View.GONE);
                adr_relais.setVisibility(View.GONE);
            }

*/
            adresse.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent();
                    intent.putExtra("AgencySelected",agc);
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


                  /*  Intent intent = new Intent();
                    intent.setClass(getActivity(), MapsActivity.class);
                    startActivity(intent);
                    getActivity().finish();*/


                }




            });






         /*   liv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Toast.makeText(getContext(),agency.getCode_envoi(),Toast.LENGTH_SHORT).show();
                    db = new SQLiteHandler(getContext());
                    Cursor cursor1 = db.getAll();

                    for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                         login = cursor1.getString(1);
                         pwd = cursor1.getString(2);

                    }


                    db.addData3(agency.getCode_envoi(),login,"LIVRAISON","Liv", "ZSD_STAT","E0006","E0010","");
                    nbliv++;


                    Intent intent = new Intent();
                    intent.setClass(getActivity(), SignatureActivity.class);
                    intent.putExtra("val",String.valueOf(nbliv));
                    intent.putExtra("val1",String.valueOf(nbnonliv));
                    intent.putExtra("siz",String.valueOf(size));
                    startActivity(intent);
                    getActivity().finish();




                }
            });

            nonliv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {




                    db = new SQLiteHandler(getContext());
                    Cursor cursor1 = db.getAll();

                    for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                        login = cursor1.getString(1);
                        pwd = cursor1.getString(2);

                    }

                    db.addData3(agency.getCode_envoi(),login,"AFFECTE GUICHET","affg", "ZSD_STAT","E0006","E0005","");

                    nbnonliv++;
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),Raison.class);

                    intent.putExtra("val",String.valueOf(nbliv));
                    intent.putExtra("val1",String.valueOf(nbnonliv));
                    intent.putExtra("siz",String.valueOf(size));

                    startActivity(intent);
                    getActivity().finish();

*/
            //       }
            //});



        }

     /*   btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapsActivity.class);
                startActivity(intent);

            }
        });*/



        isNew = false;
    }



    public void onSaveRequested() {
		/*if (isNew) {
			agency = new Agency(childViewAgencyID.getText().toString());
		}
		agency.setAgencyName(childViewAgencyName.getText().toString());

		try {
			if (isNew)
				OnlineManager.createAgency(agency, this);
			else
				OnlineManager.updateAgency(agency, this);
		} catch (OnlineODataStoreException e) {
			TraceLog.e("AgencyFragment::onSaveRequest", e);
		}*/

    }


    @Override
    public void onRequestError(int operation, Exception e) {
			/*Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected, e.getMessage()),
				Toast.LENGTH_LONG).show();*/
	/*	try {
			OfflineManager.createAgency(agency,this);
		} catch (OfflineODataStoreException e1) {
			e1.printStackTrace();
		}
		//getActivity().finish();*/
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



//hanane

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
