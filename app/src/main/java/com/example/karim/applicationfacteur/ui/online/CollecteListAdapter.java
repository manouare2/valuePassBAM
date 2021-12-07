package com.example.karim.applicationfacteur.ui.online;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollecteListAdapter extends BaseAdapter implements UIListener, Filterable {


    final private LayoutInflater inflater;
    UIListener ui= this;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected=true;
    private  boolean a;
    private SQLiteHandler db;
    boolean insert= false;
    int taille;
    String digits,nbenvoi;

    Collecte cl;
    Context ctx;
    CollecteListFragment fragment;
    protected SessionManager session;
    String code_envoi,nb_envoi,minutes;
    LogonCoreContext lgtx;




    private List<Collecte> ctls;
    private List<Collecte> displayedclts;
    final private ListView myList;
    TextView client,heure,telephone,interlocuteur,ID,envoi;

    public CollecteListAdapter(CollecteListFragment fragment, ListView myList, LayoutInflater inflater, List<Collecte> cls, Context ctx) {
        this.inflater = inflater;
        this.myList = myList;
        this.ctls = cls;
        this.displayedclts = cls;
        this.ctx=ctx;
        this.fragment = fragment;

    }

    @Override
    public int getCount() {
        return (displayedclts!=null)?displayedclts.size():0;
    }

    @Override
    public Object getItem(int position) {
        return (displayedclts!=null)?displayedclts.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        db = new SQLiteHandler(ctx);
        if (view == null) {
            view = inflater.inflate(R.layout.collecte_list_item, parent, false);
        }
        nbenvoi =((Activity)view.getContext()).getIntent().getStringExtra("nbenvoi");


        final Collecte cl = displayedclts.get(position);


        telephone = (TextView) view.findViewById(R.id.telephone);
        telephone.setText(cl.getAdresse_client());

        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = telephone.getText().toString();

                String dial = "tel:" + phoneNo;


                Intent phoneCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNo, null));

                Intent sms = new Intent(Intent.ACTION_VIEW);
                sms.setType("vnd.android-dir/mms-sms");
                sms.putExtra("address", phoneNo);
                sms.putExtra("sms_body", "Bonjour! nous vous avertirons du passage du facteur");
                Intent chooser = Intent.createChooser(sms, "");//default action
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{phoneCall});//additional actions
                ctx.startActivity(chooser);
            }
        });


        interlocuteur = (TextView) view.findViewById(R.id.interlocuteur);
        interlocuteur.setText(cl.getInterlocuteur());

        ID = (TextView) view.findViewById(R.id.ID);
        ID.setText(cl.getNum_fdr());
        client = (TextView) view.findViewById(R.id.client);
        client.setText(cl.getNom_client());
        envoi = (TextView) view.findViewById(R.id.envoi);
        envoi.setText(nbenvoi);


        String r = "";
        heure = (TextView) view.findViewById(R.id.heure);


        if (cl.getStat().equalsIgnoreCase("Traité")) {

            for (int i = 0; i < cl.getHeuretr().length(); i++) {
                r = cl.getHeuretr().substring(0, 2) + ":" + cl.getHeuretr().substring(2);
            }
            heure.setText(r);



            session = new SessionManager(ctx);

            // nombre d 'envoi d'une collecte recupéré de la table SQL
            SQLiteHandler db = new SQLiteHandler(ctx);
            Cursor cr = db.getNbEnvoiCL(cl.getNum_fdr(),cl.getNum_client(), session.getUsername());

           /* Cursor cr = db.getnbenvoi(cl.getNum_client(), cl.getNum_fdr(), session.getUsername());*/


         //  if (cr.getCount() != 0) {   //cas de scanner le nombre d 'envoi

                envoi = (TextView) view.findViewById(R.id.envoi);
                for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                    {
                        envoi.setText(String.valueOf(cr.getString(0)));

                    }

                }

           /* }else { //cas saisir seulement le nombre d'envoi

                envoi = (TextView) view.findViewById(R.id.envoi);
                envoi.setText(nbenvoi);



            }*/

            }else if (cl.getStat().equalsIgnoreCase("Non Traité"))
                {

                    for (int i = 0; i < cl.getHeuretr().length(); i++) {
                        r = cl.getHeuretr().substring(0, 2) + ":" + cl.getHeuretr().substring(2);
                    }
                    heure.setText(r);


                }


                else {

            String tim = null;
                digits = cl.getHeure().replaceAll("[^0-9.]", "");

                for (int i = 0; i < digits.length(); i++) {
                    r = digits.substring(0, 2) + ":" + digits.substring(2);
                    tim = digits.substring(0, 2) +  digits.substring(2);

                }

                r = r.substring(0, r.length() - 2);
                final  String heure1 = r.substring(0, 2);
                final  String minute1 = r.substring(r.length() - 2);

                 tim = heure1+minute1;
                Calendar calandrier = Calendar.getInstance();
                final String hour = String.valueOf(calandrier.get(Calendar.HOUR_OF_DAY));
                final String minute = String.valueOf(calandrier.get(Calendar.MINUTE));


         if( minute.length() == 1) {

                   minutes = "0" + minute;

                   final String time = hour + minutes;

             if (tim.compareToIgnoreCase(time) < 0) {


                 heure.setTextColor(Color.RED);
                 client.setTextColor(Color.RED);
             } else if (tim.compareToIgnoreCase(time) >= 0) {

                 heure.setTextColor(Color.BLUE);
                 client.setTextColor(Color.BLUE);


             }


         }

         else
         {



             final String time = hour + minute;

             if (tim.compareToIgnoreCase(time) < 0) {


                 heure.setTextColor(Color.RED);
                 client.setTextColor(Color.RED);
             } else if (tim.compareToIgnoreCase(time) >= 0) {

                 heure.setTextColor(Color.BLUE);
                 client.setTextColor(Color.BLUE);


             }

         }




                heure.setText(r);

            }



            if (cl.getType_cl().equalsIgnoreCase("P")) {

                client.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_p, 0, 0, 0);
            } else if (cl.getType_cl().equalsIgnoreCase("D"))

            {
                client.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_d, 0, 0, 0);
            }


            view.setOnClickListener(new CollecteListAdapter.AgencyListClickListener(displayedclts.get(position)));
            view.setOnLongClickListener(new CollecteListAdapter.AgencyListLongClickListener(position));

            final View finalView = view;


            return view;
        }



    /**
     * Delete an item from the list.
     *
     * @param i the index of the item to delete
     */
    public void deleteItem(int i) {



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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                List<Collecte> filteredList = new ArrayList<>();

                if (ctls == null)
                    ctls = new ArrayList<Collecte>(displayedclts);

                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.count = ctls.size();
                    filterResults.values =ctls;
                }
                else {
                    charSequence = charSequence.toString().toLowerCase();
                    for(int i = 0; i < ctls.size(); i++) {
                        String data = ctls.get(i).getNom_client();
                        String telephone = ctls.get(i).getAdresse_client();
                        String interlocuteur = ctls.get(i).getInterlocuteur();
                        if (data.toLowerCase().contains(charSequence.toString()) || telephone.toLowerCase().contains(charSequence.toString()) || interlocuteur.toLowerCase().contains(charSequence.toString() )) {
                            filteredList.add(ctls.get(i));
                        }
                    }
                    filterResults.count = filteredList.size();
                    filterResults.values = filteredList;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayedclts = (List<Collecte>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    /**
     * This listener handles clicks on a travel agency in the list.
     */
    private class AgencyListClickListener implements View.OnClickListener {

        final private Collecte item;

        private AgencyListClickListener(Collecte item) {
            this.item = item;
        }

        @Override
        public void onClick(final View view) {
            fragment.onFDRSelected(item);

        }
    }


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




























}
