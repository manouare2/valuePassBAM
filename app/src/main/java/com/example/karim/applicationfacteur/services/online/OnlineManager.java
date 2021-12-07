package com.example.karim.applicationfacteur.services.online;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.example.karim.applicationfacteur.services.AgencyRequestListener;
import com.example.karim.applicationfacteur.services.Collections;
import com.example.karim.applicationfacteur.services.Operation;
import com.example.karim.applicationfacteur.services.UIListener;
import com.example.karim.applicationfacteur.types.Agency;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataContractViolationException;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataNetworkException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.impl.ODataEntityDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataRequestChangeSet;
import com.sap.smp.client.odata.store.ODataRequestParamBatch;
import com.sap.smp.client.odata.store.ODataRequestParamSingle;
import com.sap.smp.client.odata.store.ODataResponse;
import com.sap.smp.client.odata.store.ODataResponseBatch;
import com.sap.smp.client.odata.store.ODataResponseBatchItem;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.sap.smp.client.odata.store.ODataStore;
import com.sap.smp.client.odata.store.impl.ODataRequestChangeSetDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataRequestParamBatchDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataRequestParamSingleDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataResponseBatchDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataResponseChangeSetDefaultImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineManager {
	public static final String TAG = OnlineManager.class.getSimpleName();

	public static LogonCoreContext lgCtx = null;
	private static SQLiteHandler db;
	static String cmd;
	static String client;
	static String telephone;
	static String adresse;
	static String crbt, objet, envoi, agent, agence, zsd;
	static String login;
	static String pwd;

	public static boolean openOnlineStore(Context context) throws OnlineODataStoreException {
		//AgencyOpenListener implements OpenListener interface
		//Listener to be invoked when the opening process of an OnlineODataStore object finishes
		/*AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		if (openListener.getStore()==null){
			LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();*/


		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx, "HHAROUY", "123456789");

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				context).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(context));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
				requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.e("e8",e.toString());
		}
		//Method to open a new online store asynchronously


		//Check if OnlineODataStore opened successfully
		OnlineODataStore store = null;
		try {
			store = OnlineODataStore.open(context, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
			Log.e("error1", e.toString());
		}
		if (store != null) {
			return true;
		} else {
			return false;
		}

	}

	//Affcihage

	public static List<Agency> getAgencies1(Context context) throws OnlineODataStoreException {

		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

         login= cursor.getString(1);
			pwd= cursor.getString(1);

		}

		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				context).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(context));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
				requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//Method to open a new online store asynchronously


		//Check if OnlineODataStore opened successfully
		OnlineODataStore store = null;

		try {
			store = OnlineODataStore.open(context, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
		}

		ArrayList<Agency> agencyList = new ArrayList<Agency>();

	/*	AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();*/

		if (store != null) {

			Agency agency;
			ODataProperty property;
			ODataPropMap properties;
			try {

				ODataResponseSingle resp = store.executeReadEntitySet(
						"EnvoiSet", null);
				//Get the response payload
				ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
				//Get the list of ODataEntity
				List<ODataEntity> entities = feed.getEntities();
				//Loop to retrieve the information from the response
				for (ODataEntity entity : entities) {
					properties = entity.getProperties();
					property = properties.get("ZnumEnvoi");
					agency = new Agency((String) property.getValue());
					property = properties.get("Commande");
					agency.setCommande((String) property.getValue());

					property = properties.get("Nom_Client");
					agency.setNom_client((String) property.getValue());
					property = properties.get("Adresse_client");
					agency.setAdresse_client((String) property.getValue());
					property = properties.get("CRBT");
					agency.setCrbt((String) property.getValue());
					property = properties.get("Telephone_client");
					agency.setTelephone_client((String) property.getValue());

					property = properties.get("Code_objet");
					agency.setCode_objet((String) property.getValue());
					property = properties.get("AGENCE");
					agency.setAgence((String) property.getValue());
					/*property = properties.get(String.valueOf("num_prec"));
					agency.setNum_prec(Integer.toString((Integer) property.getValue()));
					property = properties.get("Num_modif");
					agency.setNum_modif((String)property.getValue());*/
					property = properties.get("statut_ZSD");
					agency.setStatut_ZSD((String) property.getValue());
					property = properties.get("Matricule_Agent");
					agency.setMat_agent((String) property.getValue());


					//Obtain the edit resource path from the ODataEntity
					agency.setEditResourceURL(entity.getEditResourcePath());

					agencyList.add(agency);


				}

			} catch (Exception e) {
				Log.e("error", e.toString());
				throw new OnlineODataStoreException(e);


			}
		}
		return agencyList;
		//END
	}

	public static List<Agency> getAgencies(Context context) throws OnlineODataStoreException {
		db = new SQLiteHandler(context);
		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(1);

		}

		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				context).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(context));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
				requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//Method to open a new online store asynchronously


		//Check if OnlineODataStore opened successfully
		OnlineODataStore store = null;

		try {
			store = OnlineODataStore.open(context, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
		}

		ArrayList<Agency> agencyList = new ArrayList<Agency>();

	/*	AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();*/

		if (store != null) {

			Agency agency;
			ODataProperty property;
			ODataPropMap properties;
			try {

				ODataResponseSingle resp = store.executeReadEntitySet(
						"EnvoiSet", null);
				//Get the response payload
				ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
				//Get the list of ODataEntity"
				List<ODataEntity> entities = feed.getEntities();
				//Loop to retrieve the information from the response
				for (ODataEntity entity : entities) {
					properties = entity.getProperties();
					property = properties.get("ZnumEnvoi");
					agency = new Agency((String) property.getValue());
					envoi = (String) property.getValue();
					property = properties.get("Commande");
					agency.setCommande((String) property.getValue());
					cmd = (String) property.getValue();

					property = properties.get("Nom_Client");
					agency.setNom_client((String) property.getValue());
					client = (String) property.getValue();
					property = properties.get("Adresse_client");
					agency.setAdresse_client((String) property.getValue());
					adresse = (String) property.getValue();
					property = properties.get("CRBT");
					agency.setCrbt((String) property.getValue());
					crbt = (String) property.getValue();

					property = properties.get("Telephone_client");
					agency.setTelephone_client((String) property.getValue());
					telephone = (String) property.getValue();

					property = properties.get("Code_objet");
					agency.setCode_objet((String) property.getValue());
					objet = (String) property.getValue();
					property = properties.get("AGENCE");
					agency.setAgence((String) property.getValue());
					agence = (String) property.getValue();
					property = properties.get("statut_ZSD");
					agency.setStatut_ZSD((String) property.getValue());
					zsd = (String) property.getValue();
					property = properties.get("Matricule_Agent");
					agency.setMat_agent((String) property.getValue());
					agent = (String) property.getValue();

					agency.setEditResourceURL(entity.getEditResourcePath());

					agencyList.add(agency);
					//db.addData(cmd, client, telephone, adresse, crbt);
					db.addData1(objet, envoi, agent, agence, zsd);


				}

			} catch (Exception e) {
				Log.e("error", e.toString());
				throw new OnlineODataStoreException(e);


			}
		}
		return agencyList;
		//END
	}

	public static List<Agency> offline(Context context) {

		ArrayList<Agency> agencyList = new ArrayList<Agency>();

		db = new SQLiteHandler(context);
		Agency agency;

		Cursor cursor = db.getAll();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			agency = new Agency(cursor.getString(0));

			agency.setCommande(cursor.getString(1));


			agency.setNom_client(cursor.getString(2));
			agency.setTelephone_client(cursor.getString(3));

			agency.setAdresse_client(cursor.getString(4));

			agency.setCrbt(cursor.getString(5));

			agencyList.add(agency);


		}


		return agencyList;
	}

	/**
	 * Send a POST request to create an agency
	 *
	 * @param agency          agency information
	 * @param uiListenerClass
	 * @return the agency ID
	 * @throws OnlineODataStoreException
	 */
	public static String createAgency(Context context, Agency agency, Class<UIListener> uiListenerClass) throws OnlineODataStoreException {
		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(1);

		}

		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				context).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(context));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
				requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}


		//Method to open a new online store asynchronously


		//Check if OnlineODataStore opened successfully
		OnlineODataStore store = null;


		try {
			store = OnlineODataStore.open(context, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
		}

		Log.e("mimi", "mimi");

			Log.e("4", "4");

			if (store == null) return null;
			Log.e("test", "test");


				Log.e("5", "5");
		ODataEntity newEntity = null;
		try {
			newEntity = createAgencyEntity(store, agency);
		} catch (ODataParserException e) {
			e.printStackTrace();
		}
		Log.e("6", "6");

		ODataResponseSingle resp = null;
		try {
			resp = store.executeCreateEntity(newEntity,
                    "LivraisonSet", null);
		} catch (ODataNetworkException e) {
			e.printStackTrace();
		} catch (ODataParserException e) {
			e.printStackTrace();
		} catch (ODataContractViolationException e) {
			e.printStackTrace();
		}
		Log.e("7", "7");
				ODataEntity createdEntity = (ODataEntity) resp.getPayload();
				Log.e("8", "8");
				ODataPropMap properties = createdEntity.getProperties();
				Log.e("9", "9");
				ODataProperty property = properties
						.get("Code_Envoi");
				Log.e("10", "10");
				return (String) property.getValue();

		}



	public static void createAgency(Context context,Agency agency, UIListener uiListener) throws OnlineODataStoreException{
		//BEGIN
		//Get the open online store
		Log.e("kim","kim");
		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(1);

		}

		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				context).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(context));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
				requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		//Method to open a new online store asynchronously



		//Check if OnlineODataStore opened successfully
		OnlineODataStore store = null;


		try {
			store = OnlineODataStore.open(context, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
		}


		if (store==null) return;

			//The travel agency entity to be created
		ODataEntity newEntity = null;
		try {
			newEntity = createAgencyEntity(store, agency);
		} catch (ODataParserException e) {
			e.printStackTrace();
		}
		//AgencyRequestListener implements ODataRequestListener,
			// which receives the response from the server and notify the activity that shows
			//the message to the user
			AgencyRequestListener agencyListener = new AgencyRequestListener(
					Operation.CreateAgency.getValue(), uiListener);
			//Scheduling method for creating an Entity asynchronously
		try {
			store.scheduleCreateEntity(newEntity,"LivraisonSet",
					agencyListener, null);
		} catch (ODataContractViolationException e) {
			e.printStackTrace();
		}

		//END
	}

/*	public static String createAgency1(Context context, Agency1 agency, Class<UIListener> uiListenerClass) throws OnlineODataStoreException {
		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(1);

		}

		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				context).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(context));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
				requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}


		//Method to open a new online store asynchronously


		//Check if OnlineODataStore opened successfully
		OnlineODataStore store = null;


		try {
			store = OnlineODataStore.open(context, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
		}

		Log.e("mimi", "mimi");

		Log.e("4", "4");

		if (store == null) return null;
		Log.e("test", "test");


		Log.e("5", "5");
		ODataEntity newEntity = null;
		try {
			newEntity = createAgencyEntity(store, agency);
		} catch (ODataParserException e) {
			e.printStackTrace();
		}
		Log.e("6", "6");

		ODataResponseSingle resp = null;
		try {
			resp = store.executeCreateEntity(newEntity,
					"LivraisonSet", null);
		} catch (ODataNetworkException e) {
			e.printStackTrace();
		} catch (ODataParserException e) {
			e.printStackTrace();
		} catch (ODataContractViolationException e) {
			e.printStackTrace();
		}
		Log.e("7", "7");
		ODataEntity createdEntity = (ODataEntity) resp.getPayload();
		Log.e("8", "8");
		ODataPropMap properties = createdEntity.getProperties();
		Log.e("9", "9");
		ODataProperty property = properties
				.get("Code_Envoi");
		Log.e("10", "10");
		return (String) property.getValue();

	}*/



	/*public static void createAgency1(Context context,Agency1 agency, UIListener uiListener) throws OnlineODataStoreException{
		//BEGIN
		//Get the open online store
		Log.e("kim","kim");
		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(1);

		}

		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				context).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(context));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
				requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		//Method to open a new online store asynchronously



		//Check if OnlineODataStore opened successfully
		OnlineODataStore store = null;


		try {
			store = OnlineODataStore.open(context, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
		}


		if (store==null) return;

		//The travel agency entity to be created
		ODataEntity newEntity = null;
		try {
			newEntity = createAgencyEntity1(store, agency);
		} catch (ODataParserException e) {
			e.printStackTrace();
		}
		//AgencyRequestListener implements ODataRequestListener,
		// which receives the response from the server and notify the activity that shows
		//the message to the user
		AgencyRequestListener agencyListener = new AgencyRequestListener(
				Operation.CreateAgency.getValue(), uiListener);
		//Scheduling method for creating an Entity asynchronously
		try {
			store.scheduleCreateEntity(newEntity,"AffectationSet",
					agencyListener, null);
		} catch (ODataContractViolationException e) {
			e.printStackTrace();
		}

		//END
	}

*/
	public static void updateAgency(Agency1 agency, UIListener uiListener,Context ctx) throws OnlineODataStoreException{

		db = new SQLiteHandler(ctx);

		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(2);

		}



		CredentialsProvider1 credProvider = CredentialsProvider1
                .getInstance(lgCtx,login,pwd);

        HttpConversationManager manager = new CommonAuthFlowsConfigurator(
                ctx).supportBasicAuthUsing(credProvider).configure(
                new HttpConversationManager(ctx));


        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx,requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        URL url = null;


        try {
            url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        OnlineODataStore store = null;


        try {
            store = OnlineODataStore.open(ctx, url, manager, null);
        } catch (ODataException e) {
            e.printStackTrace();
            Log.e("e0",e.toString());
        }

		if (store==null) return;
		try {
			//The travel agency entity to be updated
			ODataEntity newEntity = createAgencyEntity1(store,agency);
			//AgencyRequestListener implements ODataRequestListener, 
			// which receives the response from the server and notify the activity that shows
			//the message to the user
			/*AgencyRequestListener agencyListener = new AgencyRequestListener(
					Operation.UpdateAgency.getValue(), uiListener);*/
			//Scheduling method for updating an Entity asynchronously
			store.executeUpdateEntity(newEntity, null);
		} catch (Exception e) {
			throw new OnlineODataStoreException(e);
		}
		//END

	}

	public static void updateAgency1(Agency1 agency, UIListener uiListener,Context ctx) throws OnlineODataStoreException{

		db = new SQLiteHandler(ctx);

		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(2);

		}



		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				ctx).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(ctx));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx,requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		OnlineODataStore store = null;


		try {
			store = OnlineODataStore.open(ctx, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
			Log.e("e0",e.toString());
		}

		if (store==null) return;
		try {
			//The travel agency entity to be updated
			ODataEntity newEntity = createAgencyEntity2(store,agency);
			//AgencyRequestListener implements ODataRequestListener,
			// which receives the response from the server and notify the activity that shows
			//the message to the user
			/*AgencyRequestListener agencyListener = new AgencyRequestListener(
					Operation.UpdateAgency.getValue(), uiListener);*/
			//Scheduling method for updating an Entity asynchronously
			store.executeUpdateEntity(newEntity, null);
		} catch (Exception e) {
			throw new OnlineODataStoreException(e);
		}
		//END

	}



	public static void deleteAgency(Agency agency) throws OnlineODataStoreException{
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();

		if (store==null) return;

		try {
			String resourcePath = agency.getEditResourceURL();
			store.executeDeleteEntity(resourcePath, null, null);
		} catch (Exception e) {
			throw new OnlineODataStoreException(e);
		}
	}



	public static void deleteAgency(Agency agency, UIListener uiListener) throws OnlineODataStoreException{
		//BEGIN
		//Get the open online store
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();

		if (store==null) return;
		
		try {
			//get resource path required to send DELETE requests
			String resourcePath = agency.getEditResourceURL();
			if (!TextUtils.isEmpty(resourcePath)) {
				//AgencyRequestListener implements ODataRequestListener, 
				// which receives the response from the server and notify the activity that shows
				//the message to the user
				AgencyRequestListener agencyListener = new AgencyRequestListener(
						Operation.DeleteAgency.getValue(), uiListener);
				//Scheduling method for deleting an Entity asynchronously
				store.scheduleDeleteEntity(resourcePath, null, agencyListener, null);
			} else {
				throw new OnlineODataStoreException("Resource path is null");
			}

		} catch (Exception e) {
			throw new OnlineODataStoreException(e);
		}
		//END
	}

	/**
	 * Create Travel agency entity type with the corresponding value
	 * @param store online store
	 * @param agency agency information
	 * @return ODataEntity with the agency information
	 * @throws ODataParserException
	 */
	private static ODataEntity createAgencyEntity(OnlineODataStore store, Agency agency) throws ODataParserException {
		//BEGIN
		Log.e("1","1");
		ODataEntity newEntity = null;
		if (store!=null && agency!=null && agency.isInitialized()) {	
			//Use default implementation to create a new travel agency entity type
			newEntity = new ODataEntityDefaultImpl(Collections.TRAVEL_AGENCY_ENTITY_TYPE);
			//If available, it will populates those properties of an OData Entity 
			//which are defined by the allocation mode 
			try {
				store.allocateProperties(newEntity, ODataStore.PropMode.All);
			} catch (ODataException e) {
				e.printStackTrace();
			}
			//If available, it populates the navigation properties of an OData Entity
			store.allocateNavigationProperties(newEntity);
			Log.e("2","2");
			String code_envoi = agency.getCode_envoi();
			newEntity.getProperties().put("ZnumEnvoi",
					new ODataPropertyDefaultImpl("ZnumEnvoi",agency.getCode_envoi()));
			newEntity.getProperties().put("Objnr",
					new ODataPropertyDefaultImpl("Objnr",agency.getCode_objet()));
			newEntity.getProperties().put("Txt30",
					new ODataPropertyDefaultImpl("Txt30","LIVRAISON"));

			newEntity.getProperties().put("Txt04",
					new ODataPropertyDefaultImpl("Txt04", "liv"));

			newEntity.getProperties().put("Stsma",
					new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

			newEntity.getProperties().put("NumPrec",
					new ODataPropertyDefaultImpl("NumPrec", agency.getNum_prec()));
			newEntity.getProperties().put("StatPrec",
					new ODataPropertyDefaultImpl("StatPrec", "E0006"));
			newEntity.getProperties().put("ZcodeAgence",
					new ODataPropertyDefaultImpl("ZcodeAgence", agency.getAgence()));
			newEntity.getProperties().put("Num",
					new ODataPropertyDefaultImpl("Num", agency.getNum_modif()));
			newEntity.getProperties().put("Stat",
					new ODataPropertyDefaultImpl("Stat", "E0010"));
			newEntity.getProperties().put("ZmatAgent",
					new ODataPropertyDefaultImpl("ZmatAgent", agency.getMat_agent()));
			Log.e("3","3");
			//assigned resource path required for DELETE and PUT requests
			String resourcePath = agency.getEditResourceURL();
			if (!TextUtils.isEmpty(resourcePath)){
				newEntity.setResourcePath(resourcePath, resourcePath);
			}
		}
		return newEntity;
		//END
		
	}

	private static ODataEntity createAgencyEntity1(OnlineODataStore store, Agency1 agency) throws ODataParserException {
		//BEGIN
		Log.e("1","1");
		ODataEntity newEntity = null;
		if (store!=null && agency!=null && agency.isInitialized()) {
			//Use default implementation to create a new travel agency entity type
			newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM_SRV_01.Affectation");
			//If available, it will populates those properties of an OData Entity
			//which are defined by the allocation mode
			try {
				store.allocateProperties(newEntity, ODataStore.PropMode.All);
			} catch (ODataException e) {
				e.printStackTrace();
			}
			//If available, it populates the navigation properties of an OData Entity
			store.allocateNavigationProperties(newEntity);
			Log.e("2","2");
			String envoi = agency.getCode();
			newEntity.getProperties().put("CodeBarre",
					new ODataPropertyDefaultImpl("CodeBarre",agency.getCode()));
			newEntity.getProperties().put("Facteur",
					new ODataPropertyDefaultImpl("Facteur","HHAROUY"));
			newEntity.getProperties().put("Txt30",
					new ODataPropertyDefaultImpl("Txt30","LIVRAISON"));

			newEntity.getProperties().put("Txt04",
					new ODataPropertyDefaultImpl("Txt04", "liv"));

			newEntity.getProperties().put("Stsma",
					new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

			newEntity.getProperties().put("Statprec",
					new ODataPropertyDefaultImpl("Statprec", "E0006"));

			newEntity.getProperties().put("Stat",
					new ODataPropertyDefaultImpl("Stat", "E0010"));
			Log.e("3","3");
			String resourcePath =("AffectationSet('"+agency.getCode()+"')");

				newEntity.setResourcePath(resourcePath, resourcePath);


		}
		return newEntity;
		//END

	}
	private static ODataEntity createAgencyEntity2(OnlineODataStore store, Agency1 agency) throws ODataParserException {
		//BEGIN
		Log.e("1","1");
		ODataEntity newEntity = null;
		if (store!=null && agency!=null && agency.isInitialized()) {
			//Use default implementation to create a new travel agency entity type
			newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM_SRV_01.Affectation");
			//If available, it will populates those properties of an OData Entity
			//which are defined by the allocation mode
			try {
				store.allocateProperties(newEntity, ODataStore.PropMode.All);
			} catch (ODataException e) {
				e.printStackTrace();
			}
			//If available, it populates the navigation properties of an OData Entity
			store.allocateNavigationProperties(newEntity);
			Log.e("2","2");
			String envoi = agency.getCode();
			newEntity.getProperties().put("CodeBarre",
					new ODataPropertyDefaultImpl("CodeBarre",agency.getCode()));
			newEntity.getProperties().put("Facteur",
					new ODataPropertyDefaultImpl("Facteur",login));
			newEntity.getProperties().put("Txt30",
					new ODataPropertyDefaultImpl("Txt30","AFFECTE GUICHET"));

			newEntity.getProperties().put("Txt04",
					new ODataPropertyDefaultImpl("Txt04", "affg"));

			newEntity.getProperties().put("Stsma",
					new ODataPropertyDefaultImpl("Stsma", "ZSD_STAT"));

			newEntity.getProperties().put("Statprec",
					new ODataPropertyDefaultImpl("Statprec", "E0006"));

			newEntity.getProperties().put("Stat",
					new ODataPropertyDefaultImpl("Stat", "E0004"));
			Log.e("3","3");
			String resourcePath =("AffectationSet('"+agency.getCode()+"')");

			newEntity.setResourcePath(resourcePath, resourcePath);


		}
		return newEntity;
		//END

	}

	public static void InsertBatch(Context ctx,String code_barre)

	{

		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(2);

		}

		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				ctx).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(ctx));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx,requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		OnlineODataStore store = null;


		try {
			store = OnlineODataStore.open(ctx, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
			Log.e("e0",e.toString());
		}

		ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
		ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM_SRV_01.Affectation");

		ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

		batchItem.setResourcePath("AffectationSet");


		batchItem.setMode(ODataRequestParamSingle.Mode.Create);

		batchItem.setCustomTag("something to identify the request");

			newEntity.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre",code_barre));


		//newEntity2.getProperties().put("CodeBarre", new ODataPropertyDefaultImpl("CodeBarre","30"));

		batchItem.setPayload(newEntity);
		//batchItem.setPayload(newEntity2);


		Map<String, String> createHeaders = new HashMap<String, String>();

		createHeaders.put("accept","application/atom+xml");

		createHeaders.put("content-type","application/atom+xml");

		batchItem.setOptions(createHeaders);

// Create change set

		ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();

// Add batch item to change set.

// You can add more batch items to the same change set as long as they are CUD operations

		try {
			changeSetItem.add(batchItem);

		} catch (ODataException e) {
			e.printStackTrace();
			Log.e("e1",e.toString());
		}

// Add batch item to batch request

		try {

			requestParamBatch.add(changeSetItem);

		} catch (ODataException e) {
			e.printStackTrace();
			Log.e("e2",e.toString());
		}
		ODataResponse oDataResponse = null;
		try {
			oDataResponse = store.executeRequest(requestParamBatch);
			Log.e("1","15");
		} catch (ODataNetworkException e) {
			e.printStackTrace();
			Log.e("e3",e.toString());

		} catch (ODataParserException e) {
			e.printStackTrace();
			Log.e("e4",e.toString());
		} catch (ODataContractViolationException e) {
			e.printStackTrace();
			Log.e("e5",e.toString());
		}

		Map<ODataResponse.Headers, String> headerMap = oDataResponse.getHeaders();
		Log.e("1","16");

		if (headerMap != null) {
			Log.e("1","16");
			String code = headerMap.get(ODataResponse.Headers.Code);
			Log.e("1","17");
		}

// Get batch response

		if (oDataResponse instanceof ODataResponseBatchDefaultImpl) {
			Log.e("1","18");
			ODataResponseBatch batchResponse = (ODataResponseBatch) oDataResponse;
			Log.e("1","19");
			List<ODataResponseBatchItem> responses = batchResponse.getResponses();
			Log.e("1","20");
			for (ODataResponseBatchItem response : responses) {
				Log.e("1","21");
				// Check if batch item is a change set

				if (response instanceof ODataResponseChangeSetDefaultImpl) {
					Log.e("1","22");
					ODataResponseChangeSetDefaultImpl changesetResponse = (ODataResponseChangeSetDefaultImpl) response;
					Log.e("1","23");
					List<ODataResponseSingle> singles = changesetResponse.getResponses();
					Log.e("1","24");
					for (ODataResponseSingle singleResponse : singles) {
						Log.e("1","25");
						// Get Custom tag

						String customTag = singleResponse.getCustomTag();

						// Get http status code for individual responses

						headerMap = singleResponse.getHeaders();

						String code = headerMap.get(ODataResponse.Headers.Code);
						Log.e("1","26");
						// Get individual response

						ODataPayload payload = singleResponse.getPayload();

						if (payload != null) {

							if (payload instanceof ODataError) {

								ODataError oError = (ODataError) payload;

								String uiMessage = oError.getMessage();

							} else {

								// TODO do something with payload

							}

						}

					}

				} else {

					// TODO Check if batch item is a single READ request

				}

			}
			Log.e("1","27");
		}
	}

	public static void UpdateBatch(Context ctx, String code_barre,String name,String txt04, String txt30, String stsma,String stat,String statprec)

	{    db = new SQLiteHandler(ctx);

		Cursor cursor=db.getAll2();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			login= cursor.getString(1);
			pwd= cursor.getString(2);

		}

		CredentialsProvider1 credProvider = CredentialsProvider1
				.getInstance(lgCtx,login,pwd);

		HttpConversationManager manager = new CommonAuthFlowsConfigurator(
				ctx).supportBasicAuthUsing(credProvider).configure(
				new HttpConversationManager(ctx));


		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx,requestFilter);
		manager.addFilter(requestFilter);
		manager.addFilter(responseFilter);
		URL url = null;


		try {
			url = new URL("http://194.204.220.65:8001/sap/opu/odata/sap/Z_ODATA_BAM_SRV_01");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		OnlineODataStore store = null;


		try {
			store = OnlineODataStore.open(ctx, url, manager, null);
		} catch (ODataException e) {
			e.printStackTrace();
			Log.e("e0",e.toString());
		}
		ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
		ODataEntity newEntity = new ODataEntityDefaultImpl("Z_ODATA_BAM_SRV_01.Affectation");

		newEntity.getProperties().put("CodeBarre",
				new ODataPropertyDefaultImpl("CodeBarre",code_barre));
		newEntity.getProperties().put("Facteur",
				new ODataPropertyDefaultImpl("Facteur",name));
		newEntity.getProperties().put("Txt30",
				new ODataPropertyDefaultImpl("Txt30",txt30));

		newEntity.getProperties().put("Txt04",
				new ODataPropertyDefaultImpl("Txt04",txt04));

		newEntity.getProperties().put("Stsma",
				new ODataPropertyDefaultImpl("Stsma",stsma));

		newEntity.getProperties().put("Statprec",
				new ODataPropertyDefaultImpl("Statprec", statprec));

		newEntity.getProperties().put("Stat",
				new ODataPropertyDefaultImpl("Stat",stat));


		ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();

// Allocate OData Entity


			batchItem.setResourcePath("AffectationSet('"+code_barre+"')");


		batchItem.setMode(ODataRequestParamSingle.Mode.Update);

		batchItem.setCustomTag("something to identify the request");

		batchItem.setPayload(newEntity);

// Add headers

		Map<String, String> createHeaders = new HashMap<String, String>();

		createHeaders.put("accept","application/atom+xml");

		createHeaders.put("content-type", "application/atom+xml");

		batchItem.setOptions(createHeaders);

// Create change set

		ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();

// Add batch item to change set.

// You can add more batch items to the same change set as long as they are CUD operations

		try {
			changeSetItem.add(batchItem);
		} catch (ODataException e) {
			e.printStackTrace();
		}

// Add batch item to batch request

		try {
			requestParamBatch.add(changeSetItem);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		ODataResponse oDataResponse = null;
		try {
			oDataResponse = store.executeRequest(requestParamBatch);
		} catch (ODataNetworkException e) {
			e.printStackTrace();
			Log.e("han1",e.toString());
		} catch (ODataParserException e) {
			e.printStackTrace();
			Log.e("han2",e.toString());
		} catch (ODataContractViolationException e) {
			e.printStackTrace();
			Log.e("han3",e.toString());
		}

// Check http status response for batch request.

// Status code should be “202 Accepted”

		Map<ODataResponse.Headers, String> headerMap = oDataResponse.getHeaders();

		if (headerMap != null) {

			String code = headerMap.get(ODataResponse.Headers.Code);

		}

// Get batch response

		if (oDataResponse instanceof ODataResponseBatchDefaultImpl) {

			ODataResponseBatch batchResponse = (ODataResponseBatch) oDataResponse;

			List<ODataResponseBatchItem> responses = batchResponse.getResponses();

			for (ODataResponseBatchItem response : responses) {

				// Check if batch item is a change set

				if (response instanceof ODataResponseChangeSetDefaultImpl) {

					ODataResponseChangeSetDefaultImpl changesetResponse = (ODataResponseChangeSetDefaultImpl) response;

					List<ODataResponseSingle> singles = changesetResponse.getResponses();

					for (ODataResponseSingle singleResponse : singles) {

						// Get Custom tag

						String customTag = singleResponse.getCustomTag();

						// Get http status code for individual responses

						headerMap = singleResponse.getHeaders();

						String code = headerMap.get(ODataResponse.Headers.Code);

						// Get individual response

						ODataPayload payload = singleResponse.getPayload();

						if (payload != null) {

							if (payload instanceof ODataError) {

								ODataError oError = (ODataError) payload;

								String uiMessage = oError.getMessage();

							} else {

								// TODO do something with payload

							}

						}

					}

				} else {

					// TODO Check if batch item is a single READ request

				}

			}

		}

	}


	
}
