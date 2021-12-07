package com.example.karim.applicationfacteur.services;

public class Collections {
	
	public static final String TRAVEL_AGENCY_COLLECTION = "EnvoiSet";
	public static final String TRAVEL_AGENCY_ENTITY_TYPE = "Z_ODATA_BAM_SRV_01.Livraison";
	public static final String nom_client = "nom_client";

	public static final String adresse_client = "adresse_client";
	public static final String telephone_client = "telephone_client";
	public static final String crbt = "crbt";
	public static final String code_envoi = "code_envoi";
	public static final String commande = "commande";
	

	
	public static String getEditResourcePath(String collection, String key){
		return new String(collection + "('"+ key + "')");
	}

}
