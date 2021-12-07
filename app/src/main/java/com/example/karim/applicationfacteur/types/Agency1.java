package com.example.karim.applicationfacteur.types;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Agency1 implements Parcelable{
    private String code;
    private String editResourceURL;
    private String facteur;
    private String txt04;
    private String txt30;
    private String stsma;
    private String stat;
    private  String statprec;





    public Agency1(String code, String facteur,String txt04,String txt30,String stsma,String stat,String statprec) {
        super();
        this.code = code;
        this.facteur = facteur;
        this.txt04=txt04;
        this.txt30=txt30;
        this.stsma=stsma;
        this.stat=stat;
        this.statprec=statprec;
    }

    public Agency1(Parcel in){
        readFromParcel(in);
    }

    public Agency1(String code) {
        super();
        this.code=code;
    }

    public boolean isInitialized(){
        return (!TextUtils.isEmpty(this.code));
    }

    public String getFacteur() {
        return facteur;
    }

    public void setFacteur(String facteur) {
        this.facteur = facteur;
    }

    public String getTxt04() {
        return txt04;
    }

    public void setTxt04(String txt04) {
        this.txt04 = txt04;
    }

    public String getTxt30() {
        return txt30;
    }

    public void setTxt30(String txt30) {
        this.txt30 = txt30;
    }

    public String getStsma() {
        return stsma;
    }

    public void setStsma(String stsma) {
        this.stsma = stsma;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getStatprec() {
        return statprec;
    }

    public void setStatprec(String statprec) {
        this.statprec = statprec;
    }

    public String getCode() {
        return code;

    }
    public void setCode(String code) {
        this.code = code;
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

        dest.writeString(this.code);
        dest.writeString(this.editResourceURL);
        dest.writeString(this.facteur);
        dest.writeString(this.txt04);
        dest.writeString(this.txt30);
        dest.writeString(this.stsma);
        dest.writeString(this.stat);
        dest.writeString(this.statprec);






    }

    public void readFromParcel(Parcel in)
    {

        this.code = in.readString();
        this.editResourceURL = in.readString();
        this.facteur = in.readString();
        this.txt04 = in.readString();
        this.txt30 = in.readString();
        this.stsma = in.readString();
        this.stat = in.readString();
        this.statprec = in.readString();


    }

    public static final Creator<Agency1> CREATOR = new Creator<Agency1>()
    {
        @Override
        public Agency1 createFromParcel(Parcel in)
        {
            return new Agency1(in);
        }

        @Override
        public Agency1[] newArray(int size)
        {
            return new Agency1[size];
        }
    };
}
