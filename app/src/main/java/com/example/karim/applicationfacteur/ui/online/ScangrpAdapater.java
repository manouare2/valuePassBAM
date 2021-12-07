package com.example.karim.applicationfacteur.ui.online;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Agency3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanane on 11/06/2020.
 */

public class ScangrpAdapater extends BaseAdapter implements UIListener {
    /*déclaration des variable*/
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
    ScangrpFragment fragment;
    private List<Agency3> agencies;
    private List<Agency3> displayedAgencies;
    final private ListView myList;
    private ImageButton agency_id;
    TextView txt;
  /* Déclaration des variables*/

    public ScangrpAdapater(ScangrpFragment fragment, ListView myList, LayoutInflater inflater, List<Agency3> agencies, Context ctx) {
        this.inflater = inflater;
        this.fragment = fragment;
        this.myList   = myList;
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
    /*Alimenter les données de la liste */
    public View getView(final int position, View view, ViewGroup parent) {
        db = new SQLiteHandler(ctx);
        if (view == null) {
            view = inflater.inflate(R.layout.agency_list_item2, parent, false);
        }
        final Agency3 agency = displayedAgencies.get(position);
     /* affecter le code d'envoi au txt*/
        txt =   ((TextView) view.findViewById(R.id.agency_name));
        txt.setText(agency.getCode_envoi());
        /* boutton supprimer*/
        agency_id= (ImageButton) view.findViewById(R.id.agency_id);
        final View finalView = view;

        agency_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                deleteItem(position);
                fragment.onResume();

            }
        });



        return view;
    }




/*suppression des envois depuis la liste */
    public void deleteItem(int i) {
        myList.setItemChecked(i, false);
        displayedAgencies.remove(i);
        notifyDataSetChanged();
        taille =displayedAgencies.size()+1;
        ScangrpFragment frag =fragment;
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



}





