package com.example.karim.applicationfacteur.ui.online;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.view.KeyEvent;

import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResult1;

import java.util.List;

/**
 * Created by hanane on 11/06/2020.
 */


public class  ScangrpAsyncLoader extends AsyncTaskLoader<AsyncResult1<List<Agency3>>> implements UIListener {

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

    public ScangrpAsyncLoader(Context context) {
        super(context);
        ctx = context;
    }

    @Override
    public AsyncResult1<List<Agency3>> loadInBackground() {

        AsyncResult1<List<Agency3>> asyncResult1 = new AsyncResult1<List<Agency3>>(ScangrpFragment.getAgencies2(ctx));


        return asyncResult1;


    }

}
