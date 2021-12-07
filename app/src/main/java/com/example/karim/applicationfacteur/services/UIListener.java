package com.example.karim.applicationfacteur.services;


import android.view.KeyEvent;

public interface UIListener {
	//boolean dispatchKeyEvent(KeyEvent e);

	void onRequestError(int operation, Exception e);
	void onRequestSuccess(int operation, String key);

    void onODataRequestError(Exception e);

	void onODataRequestSuccess(String info);
}
