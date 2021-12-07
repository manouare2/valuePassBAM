package com.example.karim.applicationfacteur.ui.online;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;

import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.AsyncResult1;
import com.example.karim.applicationfacteur.types.AsyncResultFDR;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.ui.main.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class CollecteAsyncLoader extends AsyncTaskLoader<AsyncResultFDR<List<Collecte>>> implements UIListener {


    Context ctx;
    private SQLiteHandler db;
    boolean insert= false;



    @Override
    public void onODataRequestError(Exception e) {

    }

    @Override
    public void onODataRequestSuccess(String info) {

    }

    @Override
    public void onRequestError(int operation, Exception e) {

    }

    @Override
    public void onRequestSuccess(int operation, String key) {

    }

    public CollecteAsyncLoader(Context context) {
        super(context);
        ctx = context;
    }

    @Override
    public AsyncResultFDR<List<Collecte>> loadInBackground() {

        AsyncResultFDR<List<Collecte>> asyncResultcol;
        db = new SQLiteHandler(getContext());
        Cursor cr= db.getFDR((new SessionManager(getContext())).getUsername());
        List<Collecte> FDRList = new ArrayList<Collecte>();




        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
           Collecte  cl = new Collecte();
            cl.setNum_client(cr.getString(3));
            cl.setNum_fdr(cr.getString(1));
            cl.setNum_poste(cr.getString(2));
            cl.setNom_client(cr.getString(4));
            cl.setInterlocuteur(cr.getString(5));
            cl.setHeure(cr.getString(9));
            cl.setTelephone_client(cr.getString(6));
            cl.setAdresse_client(cr.getString(7));
            cl.setDate(cr.getString(8));
            cl.setStat(cr.getString(11));
            cl.setAgent(cr.getString(12));
            cl.setAgence(cr.getString(13));

            cl.setType_cl(cr.getString(14));
            cl.setHeuretr(cr.getString(15));
            cl.setCode_2D(cr.getString(16));


            FDRList.add(cl);


            }




    asyncResultcol = new AsyncResultFDR<List<Collecte>>(FDRList);




        return asyncResultcol;


    }


}
