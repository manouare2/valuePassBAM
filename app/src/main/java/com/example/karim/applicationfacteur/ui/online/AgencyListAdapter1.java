package com.example.karim.applicationfacteur.ui.online;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.OnlineODataStoreException;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.ui.main.Livraison_entrep1;
import com.example.karim.applicationfacteur.ui.main.Scanpda;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.SignatureActivity;
import com.example.karim.applicationfacteur.ui.main.Testt;
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

import static com.example.karim.applicationfacteur.ui.main.myActivity.url_g;


public class AgencyListAdapter1 extends BaseAdapter implements UIListener, Filterable {

    final private LayoutInflater inflater;
    UIListener ui = this;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;
    private boolean a;
    private SQLiteHandler db;
    boolean insert = false;
    int taille;

    Agency3 agency;
    Context ctx;
    AgencyListFragment1 fragment;
    protected SessionManager session;
    String code_envoi;
    LogonCoreContext lgtx;


    private List<Agency3> agencies;
    private List<Agency3> displayedAgencies;
    final private ListView myList;
    private ImageButton agency_id;
    private TextView agency_name, agency_location;

    public AgencyListAdapter1(AgencyListFragment1 fragment, ListView myList, LayoutInflater inflater, List<Agency3> agencies, Context ctx) {
        this.inflater = inflater;
        this.myList = myList;
        this.agencies = agencies;
        this.displayedAgencies = new ArrayList<>(agencies);
        this.ctx = ctx;
        this.fragment = fragment;


    }

    @Override
    public int getCount() {
        return (displayedAgencies != null) ? displayedAgencies.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (displayedAgencies != null) ? displayedAgencies.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        db = new SQLiteHandler(ctx);
        if (view == null) {
            view = inflater.inflate(R.layout.agency_list_item1, parent, false);
        }

        final Agency3 agency = displayedAgencies.get(position);


        agency_location = (TextView) view.findViewById(R.id.agency_location);
        agency_location.setText(agency.getObj());


        agency_name = (TextView) view.findViewById(R.id.agency_name);
        agency_name.setText(agency.getCode_envoi());


        agency_name = (TextView) view.findViewById(R.id.agency_name);
        agency_name.setText(agency.getCode_envoi());


        agency_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


        if (agency.getMode_liv().equalsIgnoreCase("Point de relais") && (agency.getDesignation().equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || agency.getDesignation().equalsIgnoreCase("POD"))) {


            agency_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.abc, R.drawable.abb);
        }

        if (agency.getMode_liv().equalsIgnoreCase("Point de relais")) {


            agency_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.abc, 0);


        } else if (agency.getService().equalsIgnoreCase("PREUVE D'OBJET DISTRIBUÉ") || agency.getDesignation().equalsIgnoreCase("POD")) {


            agency_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.abb, 0);
            Log.e(agency.getCode_envoi(), agency.getService());


        } else {
            agency_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }


        //((TextView) view.findViewById(R.id.agency_id)).setText(agency.getAgencyId());


        view.setOnClickListener(new AgencyListClickListener(displayedAgencies.get(position)));
        view.setOnLongClickListener(new AgencyListLongClickListener(position));
        agency_id = (ImageButton) view.findViewById(R.id.agency_id);
        final View finalView = view;


        agency_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                final Cursor cursor1 = db.getFlag(agency.getCode_envoi());

                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

                    if (cursor1.getString(15).equalsIgnoreCase("1")) {
                        TextView myMsg = new TextView(v.getRootView().getContext());
                        myMsg.setText("Etes vous sur de  supprimer l'envoi:");
                        myMsg.setGravity(Gravity.NO_GRAVITY);


                        myMsg.setTextColor(Color.WHITE);

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                        builder.setTitle(myMsg.getText().toString());

                        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Cursor cr = db.getenvoipr(agency.getCode_envoi(), "");


                                Log.e("h1", String.valueOf(cr.getCount()));


                                for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                    Log.e("h2", cr.getString(0));


                                }


                                if (cr.getCount() != 0) {
                                    supp(agency.getCode_envoi(), position);

                                    fragment.onResume();

                                } else {


                                    Cursor cursor = null;


                                    /* for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                                          cursor = db.getenvoipere(cr.getString(0));

                                      }*/

                                    cursor = db.getenvoipere(agency.getCode_envoi());

                                    final ArrayList<String> str = new ArrayList<String>();

                                    Log.e("han", String.valueOf(cr.getCount()));
                                    Log.e("han1", String.valueOf(cursor.getCount()));


                                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {


                                        Log.e("han3", "");


                                        str.add(cursor.getString(1));

                                    }


//                                  Log.e("size", String.valueOf(str.length));
                                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(ctx);


                                    builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                                    builderSingle.setTitle("supprimer tous les envois groupés ?");


                                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                            ctx, android.R.layout.simple_dropdown_item_1line);


                                    for (int j = 0; j < str.size(); j++) {
                                        arrayAdapter.add(str.get(j));


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

                                                    for (int j = 0; j < arrayAdapter.getCount(); j++) {

                                                        supp(arrayAdapter.getItem(j).toString(), position);
                                                        fragment.onResume();
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


                                }


                            }
                        });

                        builder.setNeutralButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                        AlertDialog alert1 = builder.create();

                        alert1.show();

                    } else {

                        TextView myMsg = new TextView(v.getRootView().getContext());
                        myMsg.setText("Attention");
                        myMsg.setGravity(Gravity.NO_GRAVITY);

                        myMsg.setTextColor(Color.WHITE);

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                        builder.setTitle(myMsg.getText().toString());
                        builder.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                        builder.setMessage("Vous ne pouvez pas supprimer cet envoi");


                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                dialog.dismiss();


                            }
                        });
                        builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });


                        AlertDialog alert1 = builder.create();

                        alert1.show();


                    }


                }


            }
        });


        return view;
    }


    /**
     * Delete an item from the list.
     *
     * @param i the index of the item to delete
     */
    public void deleteItem(int i) {

        myList.setItemChecked(i, false);
        displayedAgencies.remove(i);
        notifyDataSetChanged();
        taille = displayedAgencies.size() + 1;
        fragment.changeToggleText();
        AgencyListFragment1 frag = fragment;


    }

    @Override
    public void onRequestError(int operation, Exception e) {

    }

    @Override
    public void onRequestSuccess(int operation, String key) {

    }

    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }

    public ArrayList<Agency3> willGetDisplayedData() {
        return (ArrayList<Agency3>) this.displayedAgencies;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                List<Agency3> filteredList = new ArrayList<Agency3>();

                if (agencies == null)
                    agencies = new ArrayList<Agency3>(displayedAgencies);

                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.count = agencies.size();
                    filterResults.values = agencies;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < agencies.size(); i++) {
                        String data = agencies.get(i).getCode_envoi();
                        if (data.toLowerCase().startsWith(charSequence.toString())) {
                            filteredList.add(agencies.get(i));
                        }
                    }
                    filterResults.count = filteredList.size();
                    filterResults.values = filteredList;
                }

                displayedAgencies = (List<Agency3>) filterResults.values;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    public void filter(String needle) {
        displayedAgencies.clear();
        if (needle == null || needle.length() == 0) {
            displayedAgencies.addAll(agencies);
        } else {
            needle = needle.toLowerCase();
            for (int i = 0; i < agencies.size(); i++) {
                String data = agencies.get(i).getCode_envoi();
                if (data.toLowerCase().startsWith(needle)) {
                    displayedAgencies.add(agencies.get(i));
                }
            }
        }

        notifyDataSetChanged();
    }
    /**
     * This listener handles clicks on a travel agency in the list.
     */
    private class AgencyListClickListener implements View.OnClickListener {

        final private Agency3 item;

        private AgencyListClickListener(Agency3 item) {
            this.item = item;
        }

        @Override
        public void onClick(final View view) {
            fragment.onAgencySelected(item);

        }
    }


    /**
     * This listener handles long clicks on a travel agency in the list. Long clicking
     * on an agency makes it available for deleting.
     */
    private class AgencyListLongClickListener implements View.OnLongClickListener {

        final private int i;

        private AgencyListLongClickListener(int i) {
            this.i = i;
        }

        @Override
        public boolean onLongClick(View view) {
            myList.setItemChecked(i, true);
            return true;
        }
    }


    private void suppenvoi(LogonCoreContext lgtx, String code_envoi, int position) {

        session = new SessionManager(ctx);
        Agency1 agen = null;
        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx, session.getUsername(), session.getPassword());

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
            //  url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
            //  url = new URL("http://172.10.10.116:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        OnlineODataStore store = null;

        try {
            store = OnlineODataStore.open(ctx, url, manager, null);
        } catch (ODataException e) {
            e.printStackTrace();
        }


        if (store != null) {


            String str = code_envoi;
            str = str.replace(" ", "");
            ///
            ODataResponseSingle resp = null;


            try {
                resp = store.executeDeleteEntity("DNLPOSTESet(NumEnvoi='" + str + "')", null, null);
                // deleteItem(position);
                //db.supprimer(str);
            } catch (Exception e) {
                try {
                    throw new OnlineODataStoreException(e);
                } catch (OnlineODataStoreException e1) {
                    e1.printStackTrace();
                    // Toast.makeText(ctx,"une erreur est survenue, réssayer plus tard",Toast.LENGTH_LONG).show();


                }
            }

            if (resp == null) {
                Toast.makeText(ctx, "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

            } else {
                deleteItem(position);
                db.supprimer(str);

            }


        } else {
            Toast.makeText(ctx, "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

        }


    }


    private void supp(String code_envoi, int pos) {


        session = new SessionManager(ctx);


        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx, session.getUsername(), session.getPassword());

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
            //  url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
            url = new URL(url_g);
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

        // ArrayList<Agency> agencyList = new ArrayList<Agency>();

	/*	AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();*/

        if (store != null) {

            ODataProperty property;
            ODataPropMap properties;


            ODataResponseSingle resp = null;
            try {
                              /*  resp = store.executeReadEntity(
                                        "VerificationSet('" + code1 + "')", null);

*/


                String str = code_envoi;

                str = str.replace(" ", "");
                resp = store.executeReadEntity("Zsd_statut_userSet('" + str + "')", null);

            } catch (ODataNetworkException e3) {
                e3.printStackTrace();
            } catch (ODataParserException e2) {
                e2.printStackTrace();

            } catch (ODataContractViolationException e3) {
                e3.printStackTrace();

            }


            if (resp == null) {

                Toast.makeText(ctx, "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();
            } else {
                ODataEntity feed = (ODataEntity) resp.getPayload();
                //Get the list of ODataEntity

                //Loop to retrieve the information from the response

                properties = feed.getProperties();

                property = properties.get("Stat");

                if (property.getValue().equals("OK")) {
                    SQLiteHandler db = new SQLiteHandler(ctx);
                    String str = code_envoi;

                    str = str.replace(" ", "");
                    db.supprimer(str);
                    deleteItem(pos);

                }


                //Get the response payload


                else {

                    Toast.makeText(ctx, "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();
                }


            }
        }
    }


    public void suppr_envoi(LogonCoreContext lgtx, String code_envoi, int pos) {

        session = new SessionManager(ctx);

        Agency1 agen = null;
        ODataResponse oDataResponse = null;

        CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgtx, session.getUsername(), session.getPassword());

        HttpConversationManager manager = new CommonAuthFlowsConfigurator(ctx).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(ctx));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx, requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
            //  url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM2_SRV");
            url = new URL(url_g);
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


        if (store != null) {


            ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
            ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.Zsd_statut_user");

            ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

            batchItem.setResourcePath("Zsd_statut_userSet");


            batchItem.setMode(ODataRequestParamSingle.Mode.Create);

            batchItem.setCustomTag("something to identify the request");


            newEntity.getProperties().put("Objnr", new ODataPropertyDefaultImpl("Objnr", "12"));

            newEntity.getProperties().put("Stat", new ODataPropertyDefaultImpl("Stat", "HH"));


            newEntity.getProperties().put("Num", new ODataPropertyDefaultImpl("Num", "1"));


            // newEntity.getProperties().put("Zdate", new ODataPropertyDefaultImpl("Zdate",DateFormat.getDateInstance().format(new Date())));


            //  newEntity.getProperties().put("Zheure", new ODataPropertyDefaultImpl("Zheure",""));

            newEntity.getProperties().put("ZnumEnvoi", new ODataPropertyDefaultImpl("ZnumEnvoi", "AN123456719MA"));

            newEntity.getProperties().put("ZmatAgent", new ODataPropertyDefaultImpl("ZmatAgent", "hh"));


            newEntity.getProperties().put("ZcodeAgence", new ODataPropertyDefaultImpl("ZcodeAgence", "B100"));


            newEntity.getProperties().put("ZcodeRelais", new ODataPropertyDefaultImpl("ZcodeRelais", "MA67"));


            newEntity.getProperties().put("Zmotif", new ODataPropertyDefaultImpl("Zmotif", "hh"));

            newEntity.getProperties().put("Zmesure", new ODataPropertyDefaultImpl("Zmesure", "hh"));


            newEntity.getProperties().put("ZprochAgence", new ODataPropertyDefaultImpl("ZprochAgence", ""));


            newEntity.getProperties().put("StatPrec", new ODataPropertyDefaultImpl("StatPrec", "E003"));


            newEntity.getProperties().put("NumPrec", new ODataPropertyDefaultImpl("NumPrec", "3"));


            newEntity.getProperties().put("Stsma", new ODataPropertyDefaultImpl("Stsma", "ZSD_stat"));


            newEntity.getProperties().put("Txt04", new ODataPropertyDefaultImpl("Txt04", "liv"));
            newEntity.getProperties().put("Txt30", new ODataPropertyDefaultImpl("Txt30", "liv"));


            //  newEntity.getProperties().put("ZdateAnnul", new ODataPropertyDefaultImpl("ZdateAnnul", DateFormat.getDateInstance().format(new Date())));


            newEntity.getProperties().put("ZheureAnnul", new ODataPropertyDefaultImpl("ZheureAnnul", "hh001"));


            //  newEntity.getProperties().put("ZmatAgentAnnul", new ODataPropertyDefaultImpl("ZmatAgentAnnul",""));


            newEntity.getProperties().put("ZtypeBord", new ODataPropertyDefaultImpl("ZtypeBord", "pod"));

            newEntity.getProperties().put("Annule", new ODataPropertyDefaultImpl("Annule", " ="));


            newEntity.getProperties().put("Zvaguemestre", new ODataPropertyDefaultImpl("Zvaguemestre", ""));


            newEntity.getProperties().put("Procuration", new ODataPropertyDefaultImpl("Procuration", ""));


            batchItem.setPayload(newEntity);
            //batchItem.setPayload(newEntity2);


            Log.e("test", "1");

            Map<String, String> createHeaders = new HashMap<String, String>();

            createHeaders.put("accept", "application/atom+xml");

            createHeaders.put("content-type", "application/atom+xml");

            Log.e("test", "2");
            batchItem.setOptions(createHeaders);


            ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();

            Log.e("test", "3");


            try {
                changeSetItem.add(batchItem);
                Log.e("test", "4");

            } catch (ODataException e) {
                e.printStackTrace();
                Log.e("e1", e.toString());
            }

// Add batch item to batch request

            Log.e("test", "5");
            try {

                requestParamBatch.add(changeSetItem);
                Log.e("test", "6");

            } catch (ODataException e) {
                e.printStackTrace();
                Log.e("e2", e.toString());
            }

            try {
                Log.e("test", "7");
                oDataResponse = store.executeRequest(requestParamBatch);
                Log.e("1", "15");
            } catch (ODataNetworkException e) {
                e.printStackTrace();
                Log.e("test8", e.toString());


            } catch (ODataParserException e) {
                e.printStackTrace();
                Log.e("e4", e.toString());
            } catch (ODataContractViolationException e) {
                e.printStackTrace();
                Log.e("e5", e.toString());
            }


            Log.e("test", "8");
            Map<ODataResponse.Headers, String> headerMap = oDataResponse.getHeaders();
            Log.e("1", "16");

            if (headerMap != null) {
                Log.e("1", "16");
                String code = headerMap.get(ODataResponse.Headers.Code);
                Log.e("code", code);
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

                            Toast.makeText(ctx, "code" + code, Toast.LENGTH_SHORT).show();

                            if (code.equalsIgnoreCase("201")) {
                                Toast.makeText(ctx, "Les envois sont envoyés avec succès", Toast.LENGTH_SHORT).show();
                                db.supprimer(code_envoi);
                                deleteItem(pos);

                            } else {

                                Toast.makeText(ctx, "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();


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


            Toast.makeText(ctx, "une erreur est survenue, réssayer plus tard", Toast.LENGTH_LONG).show();

        }
    }


}
