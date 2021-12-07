package com.example.karim.applicationfacteur;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.testfairy.TestFairy;


public class AgencyApplication extends MultiDexApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		//Initialize logging for debugging
        TraceLog.initialize(this);
        TraceLog.scoped(this).d("onCreate");
		TestFairy.begin(this, "SDK-Gt2ba5r1");
	}

}
