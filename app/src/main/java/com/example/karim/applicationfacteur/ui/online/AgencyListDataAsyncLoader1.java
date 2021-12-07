package com.example.karim.applicationfacteur.ui.online;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;

import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResult1;
import com.example.karim.applicationfacteur.ui.main.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class AgencyListDataAsyncLoader1 extends AsyncTaskLoader<AsyncResult1<List<Agency3>>>  implements UIListener {


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

    public AgencyListDataAsyncLoader1(Context context) {
        super(context);
        ctx = context;
    }

    @Override
    public AsyncResult1<List<Agency3>> loadInBackground() {

        AsyncResult1<List<Agency3>> asyncResult1;
            db = new SQLiteHandler(getContext());
            Cursor cursor = db.getUserCmd((new SessionManager(getContext())).getUsername());
            List<Agency3> agency3List = new ArrayList<Agency3>();
            List<String> agency3List1 = new ArrayList<String>();



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
                agc.setAdr_relais(cursor.getString(17));
                agc.setRelais(cursor.getString(18));
                agc.setAgc(cursor.getString(19));
                agc.setAdr_agc(cursor.getString(20));
                agc.setObj1(cursor.getString(21));
                agc.setTelephone_exp(cursor.getString(22));


                if(agency3List.size()==0)
                {
                    agc.setObj(cursor.getString(21));

                    agency3List.add(agc);
                    agency3List1.add(agc.getObj());

                }
                else {


                 /*   for (int i = 0; i < agency3List.size(); i++) {

                        if (cursor.getString(21).equalsIgnoreCase(agency3List.get(i).getObj())) {
                            agc.setObj("");

                        } else {
                            agc.setObj(cursor.getString(21));
                        }
                    }*/

                 if(agency3List1.contains(cursor.getString(21)))

                 {


                   /*  if(agency3List.get(agency3List.size()-1).getObj().equalsIgnoreCase(cursor.getString(21)))
                     {
                         agc.setObj("");


                     }

                     else
                     {
                         agc.setObj(cursor.getString(21));
                     }*/
                     agc.setObj("");

                 }
                 else{
                     Log.e("han","1");
                     agc.setObj(cursor.getString(21));

                 }


                     agency3List.add(agc);
                     agency3List1.add(agc.getObj());


                }



            }
            asyncResult1 = new AsyncResult1<List<Agency3>>(agency3List);




        return asyncResult1;


    }


}
