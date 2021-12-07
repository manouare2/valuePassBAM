package com.example.karim.applicationfacteur.ui.online;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.services.online.OnlineManager;
import com.example.karim.applicationfacteur.services.online.OnlineODataStoreException;
import com.example.karim.applicationfacteur.types.Agency;
import com.example.karim.applicationfacteur.types.AsyncResult;

import java.util.List;


public class AgencyListDataAsyncLoader extends AsyncTaskLoader<AsyncResult<List<Agency>>>  implements UIListener {
	Context ctx;


	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	private Snackbar snackbar;
	private CoordinatorLayout coordinatorLayout;
	private boolean internetConnected=true;
	private  boolean a;
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

	public AgencyListDataAsyncLoader(Context context) {
		super(context);
		ctx = context;
	}

	@Override
	public AsyncResult<List<Agency>> loadInBackground() {

		db = new SQLiteHandler(getContext());
		try {
		    if(registerInternetCheckReceiver())
			{
				Log.e("com","yes");
				db.deleteData();
			return new AsyncResult<List<Agency>>(OnlineManager.getAgencies(ctx));}
			else
			{
				return new AsyncResult<List<Agency>>(OnlineManager.offline(ctx));

			}
		} catch (OnlineODataStoreException e) {
			return new AsyncResult<List<Agency>>(e);
		}


	}





	private boolean registerInternetCheckReceiver() {
		IntentFilter internetFilter = new IntentFilter();
		internetFilter.addAction("android.net.wifi.STATE_CHANGE");
		internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		getContext().registerReceiver(broadcastReceiver, internetFilter);
		String status = getConnectivityStatusString(getContext());
		a= setSnackbarMessage(status,false);
		return a;

	}

	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			String status = getConnectivityStatusString(context);
			setSnackbarMessage(status,false);

		}
	};

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static String getConnectivityStatusString(Context context) {
		int conn = getConnectivityStatus(context);
		String status = null;
		if (conn == TYPE_WIFI) {
			status = "Wifi enabled";
		} else if (conn == TYPE_MOBILE) {
			status = "Mobile data enabled";
		} else if (conn == TYPE_NOT_CONNECTED) {
			status = "Not connected to Internet";
		}
		return status;
	}
	private boolean setSnackbarMessage(String status,boolean showBar) {
		String internetStatus="";
		if(status.equalsIgnoreCase("Wifi enabled")||status.equalsIgnoreCase("Mobile data enabled")){
			internetStatus="Internet Connected";
		}else {
			internetStatus="Lost Internet Connection";
		}

		if(internetStatus.equalsIgnoreCase("Lost Internet Connection")){
			if(internetConnected){

				internetConnected=false;



			}
		}else{

			if(!internetConnected){
				internetConnected=true;


				Toast.makeText(getContext(),"yes",Toast.LENGTH_SHORT).show();
			}

		}
		return  internetConnected;
	}


}
