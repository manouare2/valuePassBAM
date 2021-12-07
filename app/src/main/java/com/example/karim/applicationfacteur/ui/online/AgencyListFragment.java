package com.example.karim.applicationfacteur.ui.online;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.TraceLog;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.services.online.OnlineManager;
import com.example.karim.applicationfacteur.services.online.OnlineODataStoreException;
import com.example.karim.applicationfacteur.types.Agency;
import com.example.karim.applicationfacteur.types.AsyncResult;
import com.sap.maf.tools.logon.core.LogonCoreContext;

import java.util.List;


public class AgencyListFragment extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResult<List<Agency>>>, UIListener {
	private View myView;



	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	private Snackbar snackbar;
	private CoordinatorLayout coordinatorLayout;
	private boolean internetConnected=true;
	private  boolean a;
	private SQLiteHandler db;
	String text = "";
	LogonCoreContext lgtx;

	UIListener ui;


	@Override
	public void onODataRequestError(Exception e) {

	}

	@Override
	public void onODataRequestSuccess(String info) {

	}

	public TextView childViewAgenciesTitle;
    private ListView childViewAgenciesList;
    private LayoutInflater inflater;
    private Loader<AsyncResult<List<Agency>>> loader;
	private  Agency agency;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {



		this.inflater = inflater;

		if (myView == null) {
			myView = this.inflater.inflate(R.layout.agency_list_fragment, null);
			childViewAgenciesTitle = (TextView) myView.findViewById(R.id.agencies_title);
			childViewAgenciesList = (ListView) myView.findViewById(R.id.agencies_list);

				loader = getLoaderManager().initLoader(0, savedInstanceState, this);
				refresh();


		}
		return myView;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	public void onSaveRequested(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AgencyActivity.class);
        startActivity(intent);
	}



	public void onAgencySelected(Agency agency){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AgencyActivity.class);
        intent.putExtra("AgencySelected", agency);
        startActivity(intent);
	}
	public void onRefreshRequested(){

	}
	
	@Override
	public Loader<AsyncResult<List<Agency>>> onCreateLoader(int id, Bundle args) {
		return new AgencyListDataAsyncLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<AsyncResult<List<Agency>>> listLoader,
			AsyncResult<List<Agency>> result) {
		if (result.getException() != null || result.getData() == null) {
			Toast.makeText(getActivity(), R.string.err_odata_unexpected, Toast.LENGTH_SHORT).show();
			TraceLog.e("Error loading agencies", result.getException());

		} else {
			if(registerInternetCheckReceiver()) {
				final List<Agency> agencies = result.getData();
				childViewAgenciesList.setAdapter(new AgencyListAdapter(this,
						childViewAgenciesList, inflater, agencies,getContext()));
				childViewAgenciesTitle.setText(getString(R.string.title_agency_total, agencies.size()));
			}
			else
			{
				final List<Agency> agencies = result.getData();
				childViewAgenciesList.setAdapter(new AgencyListAdapteroff(this,
						childViewAgenciesList, inflater, agencies,getContext()));
				childViewAgenciesTitle.setText(getString(R.string.title_agency_total, agencies.size()));

			}
		}
	}

	@Override
	public void onLoaderReset(Loader<AsyncResult<List<Agency>>> listLoader) {
	}
	
	 /**
     * Refresh the data displayed in the view.
     */
    public void refresh() {
        loader.forceLoad();
    }
	public void onPause() {
		super.onPause();

	}
	
	@Override
	public void onRequestError(int operation, Exception e) {
		Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected, e.getMessage()),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRequestSuccess(int operation, String key) {
	refresh();
		String message;
		/*if (TextUtils.isEmpty(key))
			message = getString(R.string.msg_success_delete_agency);
		else
			message = getString(R.string.msg_success_delete_agency_param, key);
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}
		String message = getString(R.string.err_odata_unexpected);
		 if (operation == Operation.OfflineFlush.getValue()){
			message = getString(R.string.msg_success_flush_agency);


		} else if (operation == Operation.OfflineRefresh.getValue()){
			message = getString(R.string.msg_success_refresh_agency);
			refresh();
		}
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();*/
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

		db = new SQLiteHandler(getContext());
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
				Cursor cursor=db.getAll1();

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					agency = new Agency(cursor.getString(0));

					agency.setCode_objet(cursor.getString(1));
					agency.setCode_envoi(cursor.getString(2));
					agency.setAgence(cursor.getString(4));
					agency.setMat_agent(cursor.getString(3));
					agency.setStatut_ZSD(cursor.getString(5));
					try {
						OnlineManager.createAgency(getContext(),agency,ui);
						db.getData1();
					} catch (OnlineODataStoreException e) {
						e.printStackTrace();
					}


				}


				Toast.makeText(getContext(),"yes",Toast.LENGTH_SHORT).show();
			}

		}
		return  internetConnected;
	}




}
