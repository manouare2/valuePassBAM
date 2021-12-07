package com.example.karim.applicationfacteur.ui.online;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.view.KeyEvent;

import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResult1;

import java.util.List;


public class  AgencyListDataAsyncLoader2 extends AsyncTaskLoader<AsyncResult1<List<Agency3>>> implements UIListener {


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

    public AgencyListDataAsyncLoader2(Context context) {
        super(context);
        ctx = context;
    }

    @Override
    public AsyncResult1<List<Agency3>> loadInBackground() {

      AsyncResult1<List<Agency3>> asyncResult1 = new AsyncResult1<List<Agency3>>(AgencyListFragment2.getAgencies2(ctx));


        return asyncResult1;


    }

   /* @Override
    public AsyncResult1<List<Agency3>> loadInBackground() {

        AsyncResult1<List<Agency3>> asyncResult1;
        db = new SQLiteHandler(getContext());
        Cursor cursor = db.getUserCmd2((new SessionManager(getContext())).getUsername());
        List<Agency3> agency3List = new ArrayList<Agency3>();

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
            agency3List.add(agc);
        }
        asyncResult1 = new AsyncResult1<List<Agency3>>(agency3List);




        return asyncResult1;*/





}
