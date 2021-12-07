package com.example.karim.applicationfacteur.ui.online;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.services.Operation;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Agency;


public abstract class AgencyFragment extends Fragment implements UIListener {
	@Override
	public void onODataRequestError(Exception e) {

	}

	@Override
	public void onODataRequestSuccess(String info) {

	}

	private View myView;
    private EditText childViewAgencyID, childViewAgencyName, childViewURL;
    private EditText childViewStreet, childViewCity, childViewCountry;
	private Button btn1,btn2;
    private LayoutInflater inflater;
    private Agency agency;
    
    private boolean isNew = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		if (myView == null) {
			myView = this.inflater.inflate(R.layout.agency_fragment, null);
			childViewAgencyID = (EditText) myView.findViewById(R.id.crbt);
			childViewAgencyName = (EditText) myView.findViewById(R.id.des);
			childViewStreet= (EditText) myView.findViewById(R.id.teleph);
			childViewCity = (EditText) myView.findViewById(R.id.adr);

			//btn2 = (Button) myView.findViewById(R.id.call);
		}

		//When user select an agency, the agency will be passed as an intent
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			agency = bundle.getParcelable("AgencySelected");
			initializeViews();
		} else {
			//initializeAgencyId();
		}
		return myView;
	}

	/*private void initializeAgencyId(){
		Random rand = new Random();;
		int min = 10000000, max = 99999999;
	
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt(max - min + 1) + min;
		childViewAgencyID.setText(String.valueOf(randomNum));

	}
	*/
	private void initializeViews(){
		if (agency!=null) {
			childViewAgencyID.setText(agency.getCrbt());
			childViewAgencyName.setText(agency.getNom_client());
			childViewStreet.setText(agency.getTelephone_client());
			childViewCity.setText(agency.getAdresse_client());
			btn2.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
							String phoneNo = childViewStreet.getText().toString();

								String dial = "tel:" + phoneNo;
								startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));

						}
					});
				}

		/*	btn1.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent();
					intent.setClass(getContext(), MapsActivity.class);
					startActivity(intent);

				}
			});
*/


			isNew = false;
		}


	
	public void onSaveRequested() {
		/*if (isNew) {
			agency = new Agency(childViewAgencyID.getText().toString());
		}
		agency.setAgencyName(childViewAgencyName.getText().toString());

		try {
			if (isNew) 
				OnlineManager.createAgency(agency, this);
			else
				OnlineManager.updateAgency(agency, this);
		} catch (OnlineODataStoreException e) {
			TraceLog.e("AgencyFragment::onSaveRequest", e);
		}*/

	}


	@Override
	public void onRequestError(int operation, Exception e) {
			/*Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected, e.getMessage()),
				Toast.LENGTH_LONG).show();*/
	/*	try {
			OfflineManager.createAgency(agency,this);
		} catch (OfflineODataStoreException e1) {
			e1.printStackTrace();
		}
		//getActivity().finish();*/
	}


	@Override
	public void onRequestSuccess(int operation, String key) {
		String message= "";
		if (operation == Operation.CreateAgency.getValue()){
			message = getString(R.string.msg_success_create_agency_param, key);
		} else if (operation == Operation.UpdateAgency.getValue()){
			if (TextUtils.isEmpty(key))
				message = getString(R.string.msg_success_update_agency);
			else
				message = getString(R.string.msg_success_update_agency_param, key);
		}
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		getActivity().finish();
	}
	
}
