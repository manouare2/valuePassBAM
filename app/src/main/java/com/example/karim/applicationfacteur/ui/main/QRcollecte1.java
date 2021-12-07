package com.example.karim.applicationfacteur.ui.main;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;

import com.example.karim.applicationfacteur.types.Collecte;
import com.example.karim.applicationfacteur.types.Envoi;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity2;
import com.example.karim.applicationfacteur.ui.online.CollecteListActivity;
import com.example.karim.applicationfacteur.ui.online.CollecteTraitmActivity;
import com.example.karim.applicationfacteur.ui.online.CollecteTraitmFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;




public class QRcollecte1 extends MainCL {
    public final static int QRcodeWidth = 500 ;
    private static final String IMAGE_DIRECTORY = "/QRcodeDemonuts";
    Bitmap bitmap ;
    private EditText etqr;
    private ImageView iv;
    private Button btnn;
    String DIRECTORY;
    String pic_name;
    String StoredPath;
    File file;
    Envoi envoi;
    Button btn;
    EditText code;
    Context ctx;
     Collecte CL;

    String[] finalStr = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode1);
        Bundle bundle = getIntent().getExtras();
        final String  sep = bundle.getString("sep");

      CL  = bundle.getParcelable("CollecteSelected");




        iv = (ImageView) findViewById(R.id.iv);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        ctx=this;



        final ArrayList<Envoi> myList = (ArrayList<Envoi>) getIntent().getSerializableExtra("list");


        final ArrayList<String> myList1 = new ArrayList<String>();





            //  Toast.makeText(this,myList.get(0).getCode_envoi(),Toast.LENGTH_SHORT).show();
            try {


                if(sep.equalsIgnoreCase(","))
                {


                    bitmap = ListToImageEncode(myList,",");
                    // str= code.getText().toString().split(",");


                }
                else if (sep.equalsIgnoreCase(";"))
                {
                    Log.e("tt","2");
                    bitmap = ListToImageEncode(myList,";");


                }
                else if (sep.equalsIgnoreCase(""))

                {
                    Log.e("tt","3");
                    bitmap = ListToImageEncode(myList,"");



                }
                else if (sep.equalsIgnoreCase(":"))

                {
                    Log.e("tt","4");
                    bitmap = ListToImageEncode(myList,":");



                }
                else if (sep.equalsIgnoreCase("rtr"))

                {


                    bitmap = ListToImageEncode(myList,"\n");



                }


                iv.setImageBitmap(bitmap);
                String path = saveImage(bitmap);  //give read write permission
                Toast.makeText(QRcollecte1.this, "QRCode saved to -> " + path, Toast.LENGTH_SHORT).show();
            } catch (WriterException e) {
                e.printStackTrace();
            }







    }

    public String saveImage(Bitmap myBitmap) {


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.cardview_dark_background):getResources().getColor(R.color.cardview_light_background);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private Bitmap ListToImageEncode(List<Envoi> list, String sep) throws WriterException {

        if(list.size() == 0)
            return null;

        String Value = "";
        for(Envoi envoi: list) {
            Value += envoi.getCode_envoi() + sep;
        }

        return TextToImageEncode(Value);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, CollecteTraitmActivity.class);
            intent.putExtra("CollecteSelected",CL);
            startActivity(intent);
            finish();
        }

        return false;
        // Disable back button..............


    }


}