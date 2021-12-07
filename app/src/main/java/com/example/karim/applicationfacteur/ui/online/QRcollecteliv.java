package com.example.karim.applicationfacteur.ui.online;

/**
 * Created by hanane on 09/06/2020.
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import java.lang.reflect.Type;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.Loader;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.TraceLog;
import com.example.karim.applicationfacteur.services.UIListener;

import com.example.karim.applicationfacteur.services.online.CredentialsProvider1;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenRequestFilter;
import com.example.karim.applicationfacteur.services.online.XCSRFTokenResponseFilter;
import com.example.karim.applicationfacteur.types.Agency1;
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.types.AsyncResultEnvoi;

import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.types.Envoi;

import com.example.karim.applicationfacteur.ui.main.Acceuil1;
import com.example.karim.applicationfacteur.ui.main.MainCL;
import com.example.karim.applicationfacteur.ui.main.QRcollecelist;
import com.example.karim.applicationfacteur.ui.main.QRcollecte;
import com.example.karim.applicationfacteur.ui.main.QRcollecte1;
import com.example.karim.applicationfacteur.ui.main.SessionManager;
import com.example.karim.applicationfacteur.ui.main.Testt;
import com.example.karim.applicationfacteur.ui.main.myActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.exception.ODataContractViolationException;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataNetworkException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.impl.ODataEntityDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataRequestChangeSet;
import com.sap.smp.client.odata.store.ODataRequestParamBatch;
import com.sap.smp.client.odata.store.ODataRequestParamSingle;
import com.sap.smp.client.odata.store.ODataResponse;
import com.sap.smp.client.odata.store.ODataResponseBatch;
import com.sap.smp.client.odata.store.ODataResponseBatchItem;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.sap.smp.client.odata.store.impl.ODataRequestChangeSetDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataRequestParamBatchDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataRequestParamSingleDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataResponseBatchDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataResponseChangeSetDefaultImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.karim.applicationfacteur.ui.online.CollecteTraitmFragment.EnvoiList;
import static java.sql.Types.NULL;

/**
 * Created by hanane on 06/03/2019.
 */

public class QRcollecteliv  extends  MainCL {

    public TextView code2D, txt;
    public EditText nombre;
    private ListView envoiliste;
    private LayoutInflater inflater;
    private Loader<AsyncResultEnvoi<List<Envoi>>> loader;
    private Envoi envoi;
    String code1, str;
    AgencyListFragment1 frag;
    private EditText searchBarcode;
    private CollecteAdapter adapter1;
    public Button scan;

    public Button validerr;
    public ImageButton qr;
    EditText code_envoi;
    Integer total = 0;
    Integer prix = 0;
    String pere, size, pere1, code, size1, mode_liv;
    RadioButton cb, cb2;
    RadioGroup rd;
    SharedPreferences prefs;
    Calendar calandrier = Calendar.getInstance();
    String minutes = null;
    private SQLiteHandler db;
    private Collecte CL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collectetrqr);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        nombre = (EditText) findViewById(R.id.nombre);
        validerr = (Button) findViewById(R.id.validerr);
        session = new SessionManager(getApplicationContext());

        final String hour = String.valueOf(calandrier.get(Calendar.HOUR_OF_DAY));
        final String minute = String.valueOf(calandrier.get(Calendar.MINUTE));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            CL = bundle.getParcelable("CollecteSelected");

        }

        final SQLiteHandler db = new SQLiteHandler(getApplicationContext());


        validerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (nombre.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "veuillez renseigner le nombre des envois", Toast.LENGTH_SHORT).show();
                } else {


                    String regexString = "^[0-9]*$";
                    EnvoiList  = null ;

                    if (nombre.getText().toString().trim().matches(regexString)) {
                        //db.updateStatut(CL.getNum_client(), "Traité", hour + minute, CL.getNum_fdr());

                        MainCL.InsertENVOI1(getApplicationContext(),CL, hour + minute,"","",nombre.getText().toString(),getApplicationContext());
                        db.updatenbenvoi(nombre.getText().toString(),CL.getNum_fdr(),CL.getNum_client(),session.getUsername());

                    /*    Intent intent = new Intent();
                        intent.setClass(getApplicationContext(),CollecteListActivity.class);
                        //intent.putExtra("nbenvoi",nombre.getText().toString());


                        startActivity(intent);
                        finish();
*/

                    } else {
                        Toast.makeText(getApplicationContext(), "entrez une valeur valide" , Toast.LENGTH_SHORT).show();


                    }


                }

            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {  Intent intent = new Intent(this,CollecteListActivity.class);
            startActivity(intent);
            finish();

        }

        return false;
        // Disable back button..............
    }
}
