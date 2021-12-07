package com.example.karim.applicationfacteur.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity2;
import com.example.karim.applicationfacteur.ui.online.SQLiteHandler;
import com.google.zxing.Result;
import com.sap.maf.tools.logon.core.LogonCoreContext;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;


public class Scann2 extends myActivity implements ZXingScannerView.ResultHandler {


    static ArrayList<Agency3> agencyList = new ArrayList<>();

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    Button btn ;
    private  static String code;
    String login,pwd;
    SQLiteHandler db;
    String code1;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    Agency3 agc;
    LogonCoreContext lgtx;
    String myResult;
    String code_resultat;
    String code_retour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        scannerView = new ZXingScannerView(getApplicationContext());
        setContentView(scannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;

       // if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
               // Toast.makeText(getApplicationContext(), "Vous pouvez commencer le scann", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
      //  }


    }









    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = Build.VERSION.SDK_INT;
        //if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
        if (checkPermission()) {
            if(scannerView == null) {
                scannerView = new ZXingScannerView(this);
                setContentView(scannerView);
            }
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        } else {
            requestPermission();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                      //  Toast.makeText(getApplicationContext(), "permission accordée, Maintenant, vous pouvez accéder à la caméra", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"permission refusée, vous ne pouvez pas accéder et caméra", Toast.LENGTH_LONG).show();
                        //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            showMessageOKCancel("Vous devez autoriser l'accès aux permission",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{CAMERA},
                                                        REQUEST_CAMERA);
                                            }
                                        }
                                    });
                            return;
                        }

                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Scann2.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        code=result.getText();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Code Envoi :"+code);
        builder.setCancelable(false);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                Intent intent = new Intent(Scann2.this,AgencyListActivity2.class);
                intent.putExtra("code", String.valueOf(code));

                startActivity(intent);
                finish();




            }


        });

        builder.setNeutralButton("Annuler" +
                "", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Scann2.this,Scann2.class);
                startActivity(intent);

            }
        });




        AlertDialog alert1 = builder.create();


        alert1.show();
    }










    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Scann2.this, AgencyListActivity2.class);
            startActivity(intent);
            finish();



        }

        return false;
        // Disable back button..............
    }






}