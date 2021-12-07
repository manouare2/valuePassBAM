package com.example.karim.applicationfacteur.ui.online;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Envoi;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.sap.maf.tools.logon.core.LogonCoreContext;

import java.util.List;

/**
 * Created by hanane on 02/04/2019.
 */

public class QRCollectListadaptermoins  extends BaseAdapter implements UIListener {


    final private LayoutInflater inflater;

    UIListener ui= this;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected=true;
    private  boolean a;

    boolean insert= false;
    int taille;
    String digits;
    ImageView supp;

    Envoi cl;
    Context ctx;
    QRcollectelistfragment fragment;
    protected SessionManager session;
    String code_envoi,nb_envoi,minutes;
    LogonCoreContext lgtx;





    private List<Envoi> ctls;
    private List<Envoi> displayedclts;
    final private ListView myList;
    TextView client,heure,telephone,interlocuteur,ID,envoi;
    SQLiteHandler db = new SQLiteHandler(ctx);





    public QRCollectListadaptermoins(QRcollectelistfragment fragment, ListView myList, LayoutInflater inflater, List<Envoi> cls, Context ctx) {
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
            view = inflater.inflate(R.layout.qrlistitemmoins, parent, false);
        }

        final Envoi cl = displayedclts.get(position);


        client = (TextView) view.findViewById(R.id.client);
        client.setText(cl.getCode_envoi());


        view.setOnClickListener(new QRCollectListadaptermoins.AgencyListClickListener(displayedclts.get(position)));
        view.setOnLongClickListener(new QRCollectListadaptermoins.AgencyListLongClickListener(position));

        final View finalView = view;


        return view;
    }



    public void deleteItem(int i) {

        myList.setItemChecked(i, false);
        db.delete_envoi(displayedclts.get(i).getCode_envoi());
        displayedclts.remove(i);

        notifyDataSetChanged();
        taille =displayedclts.size()+1;
        fragment.changeToggleText();
        QRcollectelistfragment frag = fragment;


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


    private class AgencyListClickListener implements View.OnClickListener {

        final private Envoi item;

        private AgencyListClickListener(Envoi item) {
            this.item = item;
        }

        @Override
        public void onClick(final View view) {
            //  fragment.onFDRSelected(item);

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


