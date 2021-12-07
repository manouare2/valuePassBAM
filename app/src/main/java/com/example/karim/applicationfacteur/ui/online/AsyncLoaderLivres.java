package com.example.karim.applicationfacteur.ui.online;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResult1;
import com.example.karim.applicationfacteur.ui.main.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AsyncLoaderLivres extends AsyncTaskLoader<AsyncResult1<List<Agency3>>> {

    private SQLiteHandler db;

    public AsyncLoaderLivres(Context context) {
        super(context);
    }

    @Override
    public AsyncResult1<List<Agency3>> loadInBackground() {

        db = new SQLiteHandler(getContext());
        Cursor cursor =  db.getUserCmd((new SessionManager(getContext())).getUsername());
        List<Agency3> agency3List = new ArrayList<Agency3>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Agency3 agc = new Agency3(cursor.getString(1));
            agc.setNom_client(cursor.getString(2));
            agc.setTelephone_client(cursor.getString(3));
            agc.setAdresse_client(cursor.getString(4));
            agc.setCrbt(cursor.getString(5));
            agc.setPod(cursor.getString(10));
            agc.setService(cursor.getString(11));
            agc.setDesignation(cursor.getString(9));
            agc.setMode_liv(cursor.getString(12));
            agc.setAdr_relais(cursor.getString(17));
            agc.setRelais(cursor.getString(18));
            agc.setAgc(cursor.getString(19));
            agc.setAdr_agc(cursor.getString(20));
            agc.setObj(cursor.getString(21));
            agc.setTelephone_exp(cursor.getString(22));
            agency3List.add(agc);
        }

        return new AsyncResult1<List<Agency3>>(agency3List);
    }
}
