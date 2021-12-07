package com.example.karim.applicationfacteur;

import android.app.Application;


public class AgencyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		//Initialize logging for debugging
        TraceLog.initialize(this);
        TraceLog.scoped(this).d("onCreate");

	}

}
