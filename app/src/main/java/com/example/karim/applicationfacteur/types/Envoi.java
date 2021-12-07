package com.example.karim.applicationfacteur.types;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by hanane on 06/03/2019.
 */

public class Envoi implements Parcelable, List<Envoi> {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Envoi> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return null;
    }

    @Override
    public boolean add(Envoi envoi) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Envoi> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends Envoi> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Envoi get(int index) {
        return null;
    }

    @Override
    public Envoi set(int index, Envoi element) {
        return null;
    }

    @Override
    public void add(int index, Envoi element) {

    }

    @Override
    public Envoi remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Envoi> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<Envoi> listIterator(int index) {
        return null;
    }

    @NonNull
    @Override
    public List<Envoi> subList(int fromIndex, int toIndex) {
        return null;
    }

    private String code_envoi;

    private String editResourceURL;



    public Envoi(String code_envoi) {
        super();
        this.code_envoi = code_envoi;
    }

    public Envoi(Parcel in) {
        readFromParcel(in);
    }

    public boolean isInitialized() {
        return (!TextUtils.isEmpty(this.code_envoi));
    }


    public String getCode_envoi() {
        return code_envoi;
    }

    public void setCode_envoi(String nom_client) {
        this.code_envoi = nom_client;
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


        dest.writeString(this.code_envoi);
        dest.writeString(this.editResourceURL);


    }

    public void readFromParcel(Parcel in) {


        this.code_envoi = in.readString();

        this.editResourceURL = in.readString();
    }

    public static final Creator<Envoi> CREATOR = new Creator<Envoi>() {
        @Override
        public Envoi createFromParcel(Parcel in) {
            return new Envoi(in);
        }

        @Override
        public Envoi[] newArray(int size) {
            return new Envoi[size];
        }
    };
}