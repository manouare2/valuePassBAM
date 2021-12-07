package com.example.karim.applicationfacteur.ui.online;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.view.KeyEvent;

import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResult1;
import com.example.karim.applicationfacteur.types.AsyncResultEnvoi;
import com.example.karim.applicationfacteur.types.AsyncResultFDR;
import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.types.Envoi;

import java.util.List;

/**
 * Created by hanane on 06/03/2019.
 */

public class CollecteAsyncLoaderTr extends AsyncTaskLoader<AsyncResultEnvoi<List<Envoi>>> implements UIListener {


    Context ctx;
    private SQLiteHandler db;
    boolean insert = false;


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

    public CollecteAsyncLoaderTr(Context context) {
        super(context);
        ctx = context;
    }

    @Override
    public AsyncResultEnvoi<List<Envoi>> loadInBackground() {

        AsyncResultEnvoi<List<Envoi>> asyncResult1 = new AsyncResultEnvoi<List<Envoi>>(CollecteTraitmFragment.getenvois(ctx));


        return asyncResult1;


    }



}

