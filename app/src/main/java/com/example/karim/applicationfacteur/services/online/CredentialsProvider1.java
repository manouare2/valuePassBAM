package com.example.karim.applicationfacteur.services.online;

import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;


/**
 * Created by karim on 29/03/2018.
 */

public class CredentialsProvider1 implements UsernamePasswordProvider {

	private static CredentialsProvider1 instance;
	private  String username;
	private  String password;


	private LogonCoreContext lgCtx;

	private CredentialsProvider1(LogonCoreContext logonContext, String  username, String password) {

		lgCtx = logonContext;
		this.username=username;
		this.password= password;


	}

	public static CredentialsProvider1 getInstance(LogonCoreContext logonContext, String username, String password) {

		/*if (instance == null) {

			instance = new CredentialsProvider1(logonContext,username,password);

		}*/

		return new CredentialsProvider1(logonContext,username,password);

	}

	@Override

	public Object onCredentialsNeededForChallenge(IReceiveEvent arg0) {



		return null;


	}


	@Override

	public Object onCredentialsNeededUpfront(ISendEvent arg0) {


		return new UsernamePasswordToken(username,password);




	}
}