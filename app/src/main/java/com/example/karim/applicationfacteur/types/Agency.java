package com.example.karim.applicationfacteur.types;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Agency implements Parcelable{
	private String commande;
	private String nom_client;
	private String crbt;
	private String adresse_client;
	private String telephone_client;
	private String website;
	private String editResourceURL;
	private String code_envoi;
	private String statut_nom;
	private String statut_com;
	private String statut_ZSD;
	private String num_prec;
	private String stat_prec;
	private String agence;
	private String num_modif;
	private String statut;
	private String code_objet;
	private String mat_agent;



	
	public Agency(String code_envoi) {
		super();
		this.code_envoi = code_envoi;
	}
	
	public Agency(Parcel in){
		readFromParcel(in);
	}
	
	public boolean isInitialized(){
		return (!TextUtils.isEmpty(this.code_envoi));
	}
	public String getCode_envoi() {
		return code_envoi;
	}
	public void setCode_envoi(String code_envoi) {
		this.code_envoi = code_envoi;
	}
	
	public String getCommande() {
		return commande;
	}
	public void setCommande(String commande) {
		this.commande = commande;
	}
	public String getAdresse_client() {
		return adresse_client;
	}
	public void setAdresse_client(String adr) {
		this.adresse_client = adresse_client;
	}
	public String getCrbt() {
		return crbt;
	}
	public void setCrbt(String crbt) {
		this.crbt= crbt;
	}
	public String getTelephone_client() {
		return telephone_client;
	}
	public void setTelephone_client(String telephone_client) {
		this.telephone_client = telephone_client;
	}
	public String getNom_client() {
		return nom_client;
	}
	public void setNom_client(String nom_client) {
		this.nom_client = nom_client;
	}
	public String getStatut_nom() {
		return statut_nom;
	}
	public void setStatut_nom(String statut_nom) {
		this.statut_nom = statut_nom;
	}
	public String getStatut_com() {
		return statut_com;
	}
	public void setStatut_com(String statut_com) {
		this.statut_com= statut_com;
	}
	public String getStatut_ZSD() {
		return statut_ZSD;
	}
	public void setStatut_ZSD(String statut_ZSD) {
		this.statut_ZSD = statut_ZSD;
	}
	public String getStat_prec() {
		return stat_prec;
	}
	public void setStat_prec(String stat_prec) {
		this.stat_prec = stat_prec;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatutt(String statut) {
		this.statut = statut;
	}
	public String getNum_prec(){
		return num_prec;
	}
	public void setNum_prec(String num_prec) {
		this.num_prec = num_prec;
	}
	public String getNum_modif() {
		return num_modif;
	}
	public void setNum_modif(String num_modif) {
		this.num_modif = num_modif;
	}
	public String getAgence() {
		return agence;
	}
	public void setAgence(String agence) {
		this.agence = agence;
	}

	public String getCode_objet() {
		return code_objet;
	}
	public void setCode_objet(String code_objet) {
		this.code_objet= code_objet;
	}

	public String getMat_agent() {
		return mat_agent;
	}
	public void setMat_agent(String mat_agent) {
		this.mat_agent= mat_agent;
	}



	public String getEditResourceURL() {
		return editResourceURL;
	}

	public void setEditResourceURL(String editResourceURL) {
		this.editResourceURL = editResourceURL;
	}



	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.commande);
		dest.writeString(this.adresse_client);
		dest.writeString(this.nom_client);
		dest.writeString(this.telephone_client);
		dest.writeString(this.crbt);
		dest.writeString(this.code_envoi);
		dest.writeString(this.stat_prec);
		dest.writeString(this.statut);
		dest.writeString(this.statut_com);
		dest.writeString(this.statut_nom);
		dest.writeString(this.statut_ZSD);
		dest.writeString(this.num_modif);
		dest.writeString(this.num_prec);
		dest.writeString(this.agence);
		dest.writeString(this.code_objet);
		dest.writeString(this.mat_agent);



		dest.writeString(this.editResourceURL);

	}

   public void readFromParcel(Parcel in)
   {
       this.commande = in.readString();
       this.adresse_client = in.readString();
	   this.nom_client = in.readString();
	   this.telephone_client = in.readString();
	   this.crbt = in.readString();
	   this.code_envoi = in.readString();
	   this.stat_prec= in.readString();
	   this.statut = in.readString();
	   this.statut_com = in.readString();
	   this.statut_nom = in.readString();
	   this.statut_ZSD = in.readString();
	   this.num_modif = in.readString();
	   this.num_prec = in.readString();
	   this.agence = in.readString();
	   this.code_objet= in.readString();

	   this.mat_agent= in.readString();
	   this.editResourceURL = in.readString();

   }
	
   public static final Creator<Agency> CREATOR = new Creator<Agency>()
   {
       @Override
       public Agency createFromParcel(Parcel in)
       {
           return new Agency(in);
       }

       @Override
       public Agency[] newArray(int size)
       {
           return new Agency[size];
       }
   };
}
