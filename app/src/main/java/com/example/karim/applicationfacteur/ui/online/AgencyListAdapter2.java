package com.example.karim.applicationfacteur.ui.online;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Agency3;

import java.util.ArrayList;
import java.util.List;


public class AgencyListAdapter2 extends BaseAdapter implements UIListener {
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
    static ArrayList<Agency3> agencyList = new ArrayList<>();
    int taille;
    private  Agency3 agc;

    Agency3 agency;
    Context ctx;

    AgencyListFragment2 fragment;


    private List<Agency3> agencies;
    private List<Agency3> displayedAgencies;
    final private ListView myList;
    private ImageButton agency_id;
    TextView txt;

    public AgencyListAdapter2(AgencyListFragment2 fragment, ListView myList, LayoutInflater inflater, List<Agency3> agencies, Context ctx) {
        this.inflater = inflater;
        this.fragment = fragment;
        this.myList = myList;
        this.agencies = agencies;
        this.displayedAgencies = agencies;
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return (displayedAgencies!=null)?displayedAgencies.size():0;
    }

    @Override
    public Object getItem(int position) {
        return (displayedAgencies!=null)?displayedAgencies.get(position):null;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        db = new SQLiteHandler(ctx);
        if (view == null) {
            view = inflater.inflate(R.layout.agency_list_item2, parent, false);
        }

        final Agency3 agency = displayedAgencies.get(position);

    txt =   ((TextView) view.findViewById(R.id.agency_name));
           txt.setText(agency.getCode_envoi());

        //((TextView) view.findViewById(R.id.agency_id)).setText(agency.getAgencyId());


        view.setOnClickListener(new AgencyListClickListener(displayedAgencies.get(position)));
        view.setOnLongClickListener(new AgencyListLongClickListener(displayedAgencies.get(position)));

       // agency_id= (ImageButton) view.findViewById(R.id.agency_id);
        final View finalView = view;





     /*   agency_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {



                TextView myMsg = new TextView(v.getRootView().getContext());
                myMsg.setText("Etes vous sur de  supprimer l'envoi :");
                myMsg.setGravity(Gravity.NO_GRAVITY);


                myMsg.setTextColor(Color.WHITE);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle(myMsg.getText().toString());


                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteItem(position);
                       // db.supprimer(agency.getCode_envoi());
                        db.updateDataglobal2(agency.getCode_envoi(),"DNL");
                        db.deleteper(agency.getCode_envoi());


                    }
                });
                builder.setNeutralButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(v.getRootView().getContext(),AgencyListActivity2.class);
                        v.getRootView().getContext().startActivity(intent);






                    }
                });


                AlertDialog alert1 = builder.create();

                alert1.show();











            }
        });*/






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
        taille =displayedAgencies.size()+1;
        AgencyListFragment2 frag =fragment;
       // frag.childViewAgenciesTitle.setText("Envoi Encours :  "+ Integer.toString(taille-1));
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

            fragment.onAgencySelected1(item,view);



        }
    }



   /* private class AgencyListLongClickListener implements View.OnLongClickListener {

        final private int i;

        private AgencyListLongClickListener(int i) {
            this.i = i;
        }

        @Override
        public boolean onLongClick(View view) {

            myList.setItemChecked(i, true);


            return true;

        }
    }*/

    private class AgencyListLongClickListener implements View.OnLongClickListener {
        final private Agency3 item;

        private AgencyListLongClickListener(Agency3 item) {
            this.item = item;
        }

        @Override
        public boolean onLongClick(final View view) {

            fragment.onAgencySelected(item,view);


            return true;
        }



    }
    }





