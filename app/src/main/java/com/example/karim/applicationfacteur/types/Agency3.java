package com.example.karim.applicationfacteur.types;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Agency3 implements Parcelable{

    private String nom_client;
    private String crbt;
    private String adresse_client;
    private String telephone_client;
    private String telephone_exp;
    private String editResourceURL;
    private String code_envoi;
    private String pod;
    private String designation;
    private String service;
    private String  mode_liv;
    private String rue;
    private String code_postal;
    private String adr_relais;
    private String relais;
    private String agc;
    private String adr_agc;
    private String obj;

    public String getObj1() {
        return obj1;
    }

    public void setObj1(String obj1) {
        this.obj1 = obj1;
    }

    private String obj1;




    private String stat;


    public String getTelephone_exp() {
        return telephone_exp;
    }

    public void setTelephone_exp(String telephone_exp) {
        this.telephone_exp = telephone_exp;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getAgc() {
        return agc;
    }

    public void setAgc(String agc) {
        this.agc = agc;
    }

    public String getAdr_agc() {
        return adr_agc;
    }

    public void setAdr_agc(String adr_agc) {
        this.adr_agc = adr_agc;
    }

    public String getPod() {
        return pod;
    }

    public String getAdr_relais() {
        return adr_relais;
    }

    public void setAdr_relais(String adr_relais) {
        this.adr_relais = adr_relais;
    }

    public String getRelais() {
        return relais;
    }

    public void setRelais(String relais) {
        this.relais = relais;
    }

    public String getMode_liv() {
        return mode_liv;
    }

    public void setMode_liv(String mode_liv) {
        this.mode_liv = mode_liv;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getDesignation() {

        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Agency3(String code_envoi) {
        super();
        this.code_envoi = code_envoi;
    }

    public Agency3(Parcel in){
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


    public String getAdresse_client() {
        return adresse_client;
    }
    public void setAdresse_client(String adr) {
        this.adresse_client =adr;
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

        dest.writeString(this.adresse_client);
        dest.writeString(this.nom_client);
        dest.writeString(this.telephone_client);
        dest.writeString(this.crbt);
        dest.writeString(this.code_envoi);
        dest.writeString(this.service);
        dest.writeString(this.pod);
        dest.writeString(this.designation);
        dest.writeString(this.mode_liv);
        dest.writeString(this.rue);
        dest.writeString(this.code_postal);
        dest.writeString(this.adr_relais);
        dest.writeString(this.relais);
        dest.writeString(this.agc);
        dest.writeString(this.adr_agc);
        dest.writeString(this.obj);
        dest.writeString(this.obj1);
        dest.writeString(this.telephone_exp);






        dest.writeString(this.editResourceURL);

    }

    public void readFromParcel(Parcel in)
    {

        this.adresse_client = in.readString();
        this.nom_client = in.readString();
        this.telephone_client = in.readString();
        this.crbt = in.readString();
        this.code_envoi = in.readString();
        this.designation = in.readString();
        this.service = in.readString();
        this.pod = in.readString();
        this.mode_liv = in.readString();
        this.rue= in.readString();
        this.code_postal = in.readString();
        this.adr_relais = in.readString();
        this.relais = in.readString();
        this.agc = in.readString();
        this.adr_agc = in.readString();
        this.obj = in.readString();
        this.obj1 = in.readString();
        this.telephone_exp= in.readString();


        this.editResourceURL = in.readString();

    }

    public static final Creator<Agency3> CREATOR = new Creator<Agency3>()
    {
        @Override
        public Agency3 createFromParcel(Parcel in)
        {
            return new Agency3(in);
        }

        @Override
        public Agency3[] newArray(int size)
        {
            return new Agency3[size];
        }
    };

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }


}
