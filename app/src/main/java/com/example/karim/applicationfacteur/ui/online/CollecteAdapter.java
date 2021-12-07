package com.example.karim.applicationfacteur.ui.online;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.types.Envoi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanane on 06/03/2019.
 */

public class CollecteAdapter extends BaseAdapter implements UIListener {
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
    Context ctx;

    CollecteTraitmFragment fragment;


    private List<Envoi> displayedenvoi;
    private List<Envoi> envois;
    final private ListView myList;
    private ImageButton agency_id;
    TextView txt;

    public CollecteAdapter(CollecteTraitmFragment fragment, ListView myList, LayoutInflater inflater, List<Envoi> envois, Context ctx) {
        this.inflater = inflater;
        this.fragment = fragment;
        this.myList = myList;
        this.envois = envois;
        this.displayedenvoi = envois;
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return (displayedenvoi!=null)?displayedenvoi.size():0;
    }

    @Override
    public Object getItem(int position) {
        return (displayedenvoi!=null)?displayedenvoi.get(position):null;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        db = new SQLiteHandler(ctx);
        if (view == null) {
            view = inflater.inflate(R.layout.scancollecteitem, parent, false);
        }

        final Envoi envoi = displayedenvoi.get(position);

        txt =   ((TextView) view.findViewById(R.id.envoi));
        txt.setText(envoi.getCode_envoi());

        //((TextView) view.findViewById(R.id.agency_id)).setText(agency.getAgencyId());


         view.setOnClickListener(new CollecteAdapter.CollectListClickListener(displayedenvoi.get(position)));
        view.setOnLongClickListener(new CollecteAdapter.CollecteListLongClickListener(displayedenvoi.get(position)));

        // agency_id= (ImageButton) view.findViewById(R.id.agency_id);
        final View finalView = view;









        return view;
    }



    public void deleteItem(int i) {

        myList.setItemChecked(i, false);
        displayedenvoi.remove(i);
        notifyDataSetChanged();
        taille =displayedenvoi.size()+1;
        CollecteTraitmFragment frag =fragment;

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



    private class CollectListClickListener implements View.OnClickListener {

        final private Envoi item;

        private CollectListClickListener(Envoi item) {
            this.item = item;
        }

        @Override
        public void onClick(final View view) {

            fragment.onAgencySelected1(item,view);



        }
    }





    private class CollecteListLongClickListener implements View.OnLongClickListener {
        final private Envoi item;

        private CollecteListLongClickListener(Envoi item) {
            this.item = item;
        }

        @Override
        public boolean onLongClick(final View view) {

            fragment.onAgencySelected(item,view);


            return true;
        }



    }
}
