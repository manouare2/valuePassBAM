package com.example.karim.applicationfacteur.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity1;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SignatureActivity extends myActivity {
    Bitmap bitmap;
    Button clear,save;
    SignatureView signatureView;
    String path;
    private static final String IMAGE_DIRECTORY = "/Signature";
    String  client;
    private Context mContext= SignatureActivity.this;

    private static final int REQUEST = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);


       /* Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                envoiss();
            }

        };
        timer.schedule (hourlyTask, 0l, 1000*1*60);*/



        signatureView =  (SignatureView) findViewById(R.id.signature_view);
        clear = (Button) findViewById(R.id.clear);
        save = (Button) findViewById(R.id.save);
        Bundle bundle = getIntent().getExtras();

        //Extract the data…

       // client= bundle.getString("client");

     /*   clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });*/

      /*  save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
    }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90, bytes);
        File wallpaperDirectory =Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh",wallpaperDirectory.toString());
        }

        try {
            File f = new File(wallpaperDirectory,Calendar.getInstance()
                    .getTimeInMillis()+"test.jpg");
            f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);

            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(SignatureActivity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);

            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
             Intent intent = new Intent(SignatureActivity.this, AgencyListActivity1.class);
             startActivity(intent);
              finish();

            //  return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();

        }
        return "";

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_Annuler) {

            signatureView.clearCanvas();
            return true;
        }
        else if (item.getItemId() == R.id.menu_item_valid) {

           /* if (isStoragePermissionGranted()) {

            }
            else {
                view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath);
                // Calling the same class
                recreate();
            }*/


            if (Build.VERSION.SDK_INT >= 23) {
                String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (!hasPermissions(mContext, PERMISSIONS)) {
                    ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                } else {
                    //do here
                    bitmap = signatureView.getSignatureBitmap();
                    path = saveImage(bitmap);




                }
            } else {
                //do here
            }


        }

        else if (item.getItemId() == R.id.menu_item_dec)

            return deconnexion(this);


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(getApplicationContext(), "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(SignatureActivity.this, AgencyListActivity1.class);
            startActivity(intent);
            finish();
        }

        return false;
        // Disable back button..............
    }



    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}