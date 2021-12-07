package com.example.karim.applicationfacteur.ui.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.impl.ODataEntityDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataStore;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {




        String action=intent.getStringExtra("action");

        if(action.equals("actionName")){
            performAction2(context);

        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }



    public void performAction2(Context ctx){

       myActivity activity = new myActivity();
              activity.FDR(ctx);




    }





    private static ODataEntity createAgencyEntityCL(OnlineODataStore store, String ID) throws ODataParserException {
        //BEGIN
        Log.e("1","1");
        ODataEntity newEntity = null;
        if (store!=null) {

            newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM2_SRV.ZSD_FDR_COLLECTE");

            try {
                store.allocateProperties(newEntity, ODataStore.PropMode.All);
            } catch (ODataException e) {
                e.printStackTrace();
            }
            //If available, it populates the navigation properties of an OData Entity
            store.allocateNavigationProperties(newEntity);
            Log.e("2","2");
           /* newEntity.getProperties().put("ZnumObj",new ODataPropertyDefaultImpl("ZnumObj",0000000655));*/

            newEntity.getProperties().put("ZfdrId",new ODataPropertyDefaultImpl("ZfdrId",ID));





            //String resourcePath =("ZSD_FDR_COLLECTESet('" + ID + "')");

            String resourcePath =("ZSD_FDR_COLLECTESet(Kunnr='" +"',ZheureEch='" + "',ZfdrId='" + ID + "')");



            newEntity.setResourcePath(resourcePath, resourcePath);


        }
        return newEntity;
        //END

    }




}