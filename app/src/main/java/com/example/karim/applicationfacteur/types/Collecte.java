package com.example.karim.applicationfacteur.types;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

public class Collecte implements Parcelable {


    private String num_fdr;
    private String num_poste;
    private String num_client;
    private String nom_client;
    private String interlocuteur;
    private String adresse_client;
    private String telephone_client;
    private String date;
    private String heure;
    private String heuretr;
    private String stat;

    public String getCode_2D() {
        return code_2D;
    }

    public void setCode_2D(String code_2D) {
        this.code_2D = code_2D;
    }

    private String code_2D;


    public String getHeuretr() {
        return heuretr;
    }

    public void setHeuretr(String heuretr) {
        this.heuretr = heuretr;
    }




    public String getNum_fdr() {
        return num_fdr;
    }

    public void setNum_fdr(String num_fdr) {
        this.num_fdr = num_fdr;
    }

    public String getNum_poste() {
        return num_poste;
    }

    public void setNum_poste(String num_poste) {
        this.num_poste = num_poste;
    }

    public String getNum_client() {
        return num_client;
    }

    public void setNum_client(String num_client) {
        this.num_client = num_client;
    }


    public String getType_cl() {
        return type_cl;
    }

    public void setType_cl(String type_cl) {
        this.type_cl = type_cl;
    }

    private String type_cl;

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    private String agent;

    private String agence;
    private String editResourceURL;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }




    public Collecte() {
        super();
       // this.num_client = num_client;
    }

    public Collecte(Parcel in){
        readFromParcel(in);
    }

    public boolean isInitialized(){
        return (!TextUtils.isEmpty(this.num_client));
    }



    public String getNom_client() {
        return nom_client;
    }

    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }

    public String getInterlocuteur() {
        return interlocuteur;
    }

    public void setInterlocuteur(String interlocuteur) {
        this.interlocuteur = interlocuteur;
    }

    public String getAdresse_client() {
        return adresse_client;
    }

    public void setAdresse_client(String adresse_client) {
        this.adresse_client = adresse_client;
    }

    public String getTelephone_client() {
        return telephone_client;
    }

    public void setTelephone_client(String telephone_client) {
        this.telephone_client = telephone_client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
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


        dest.writeString(this.num_fdr);
        dest.writeString(this.num_poste);
        dest.writeString(this.num_client);
        dest.writeString(this.nom_client);
        dest.writeString(this.interlocuteur);
        dest.writeString(this.telephone_client);
        dest.writeString(this.adresse_client);
        dest.writeString(this.date);
        dest.writeString(this.heure);
        dest.writeString(this.stat);
        dest.writeString(this.agent);
        dest.writeString(this.agence);
        dest.writeString(this.type_cl);
        dest.writeString(this.heuretr);
        dest.writeString(this.code_2D);
        dest.writeString(this.editResourceURL);



    }

    public void readFromParcel(Parcel in) {


        this.num_fdr = in.readString();
        this.num_poste = in.readString();
        this.num_client = in.readString();

        this.nom_client = in.readString();
        this.interlocuteur = in.readString();
        this.adresse_client = in.readString();

        this.telephone_client = in.readString();
        this.date = in.readString();
        this.heure = in.readString();


        this.agent = in.readString();
        this.agence = in.readString();
        this.stat = in.readString();
        this.type_cl = in.readString();
        this.heuretr = in.readString();
        this.code_2D = in.readString();

        this.editResourceURL = in.readString();
    }

    public static final Creator<Collecte> CREATOR = new Creator<Collecte>()
    {
        @Override
        public Collecte createFromParcel(Parcel in)
        {
            return new Collecte(in);
        }

        @Override
        public Collecte[] newArray(int size)
        {
            return new Collecte[size];
        }
    };


}