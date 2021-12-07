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
import com.example.karim.applicationfacteur.types.Agency3;
import com.example.karim.applicationfacteur.ui.online.AgencyListActivity2;
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

public class QRcode extends myActivity {
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
    Agency3 agc;
    Button btn;
    EditText code;
    Context ctx;

    String[] finalStr = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);
       Bundle bundle = getIntent().getExtras();
        final String  sep = bundle.getString("sep");

        iv = (ImageView) findViewById(R.id.iv);
        btnn = (Button) findViewById(R.id.btn);
        code = (EditText) findViewById(R.id.code);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        ctx=this;


        final ArrayList<Agency3> myList = (ArrayList<Agency3>) getIntent().getSerializableExtra("AgencySelected");

        ;

        final ArrayList<String> myList1 = new ArrayList<String>();


        if (myList.size() == 0) {
            Toast.makeText(getApplicationContext(), "aucun code d'envoi n'est trouvé", Toast.LENGTH_SHORT).show();

        } else {


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
                Toast.makeText(QRcode.this, "QRCode saved to -> " + path, Toast.LENGTH_SHORT).show();
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }



        btnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ctx);


                if (sep.equalsIgnoreCase(",")) {
                    final String[] str = code.getText().toString().split(",");

                    String Value = "";
                    for (int j = 0 ; j<myList.size();j++) {
                        for (int i = 0; i< str.length; i++)
                        {
                            if(myList.get(j).getCode_envoi().equalsIgnoreCase(str[i]))
                            {
                                myList1.add(str[i]);


                            }else
                            {

                            }

                        }


                        // Value += agc.getCode_envoi() + "\n";
                    }

                    if(myList1.size()==myList.size() && myList1.size()== str.length) {

                        builderSingle.setTitle("Code correcte");
                    }
                    else
                    {
                        builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                        builderSingle.setTitle("Code erroné");

                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            ctx, android.R.layout.simple_dropdown_item_1line);
                    //String str[]= code.split("\n");
                    for(int i = 0; i< str.length; i++)
                    {
                        arrayAdapter.add(str[i]);
                    }

                    builderSingle.setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });



                    builderSingle.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), Testt.class);
                                    intent.putExtra("myList",myList);
                                    intent.putExtra("str",str);
                                    // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();

                                }
                            });




                    builderSingle.
                            setAdapter(arrayAdapter,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String strName = arrayAdapter.getItem(which);



                     /*   AlertDialog.Builder builderInner = new AlertDialog.Builder(ctx);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();*/
                                        }
                                    });
                    builderSingle.create();
                    builderSingle.show();










            } else if (sep.equalsIgnoreCase(";")) {
                    final String[] str = code.getText().toString().split(";");

                    String Value = "";
                    for (int j = 0 ; j<myList.size();j++) {
                        for (int i = 0; i< str.length; i++)
                        {
                            if(myList.get(j).getCode_envoi().equalsIgnoreCase(str[i]))
                            {
                                myList1.add(str[i]);


                            }else
                            {

                            }

                        }


                        // Value += agc.getCode_envoi() + "\n";
                    }

                    if(myList1.size()==myList.size() && myList1.size()== str.length) {

                        builderSingle.setTitle("Code correcte");
                    }
                    else
                    {
                        builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                        builderSingle.setTitle("Code erroné");

                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            ctx, android.R.layout.simple_dropdown_item_1line);
                    //String str[]= code.split("\n");
                    for(int i = 0; i< str.length; i++)
                    {
                        arrayAdapter.add(str[i]);
                    }

                    builderSingle.setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });



                    builderSingle.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), Testt.class);
                                    intent.putExtra("myList",myList);
                                    intent.putExtra("str", str);
                                    // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();

                                }
                            });




                    builderSingle.
                            setAdapter(arrayAdapter,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String strName = arrayAdapter.getItem(which);



                     /*   AlertDialog.Builder builderInner = new AlertDialog.Builder(ctx);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();*/
                                        }
                                    });
                    builderSingle.create();
                    builderSingle.show();









            } else if (sep.equalsIgnoreCase(""))

                {

                    final String[] str = code.getText().toString().split("");

                    String Value = "";
                    for (int j = 0 ; j<myList.size();j++) {
                        for (int i = 0; i< str.length; i++)
                        {
                            if(myList.get(j).getCode_envoi().equalsIgnoreCase(str[i]))
                            {
                                myList1.add(str[i]);


                            }else
                            {

                            }

                        }


                        // Value += agc.getCode_envoi() + "\n";
                    }

                    if(myList1.size()==myList.size() && myList1.size()== str.length) {

                        builderSingle.setTitle("Code correcte");
                    }
                    else
                    {
                        builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                        builderSingle.setTitle("Code erroné");

                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            ctx, android.R.layout.simple_dropdown_item_1line);
                    //String str[]= code.split("\n");
                    for(int i = 0; i< str.length; i++)
                    {
                        arrayAdapter.add(str[i]);
                    }

                    builderSingle.setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });



                    builderSingle.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), Testt.class);
                                    intent.putExtra("myList",myList);
                                    intent.putExtra("str", str);
                                    // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();

                                }
                            });




                    builderSingle.
                            setAdapter(arrayAdapter,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String strName = arrayAdapter.getItem(which);



                     /*   AlertDialog.Builder builderInner = new AlertDialog.Builder(ctx);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();*/
                                        }
                                    });
                    builderSingle.create();
                    builderSingle.show();










            } else if (sep.equalsIgnoreCase(":"))

                {


                    final String[] str = code.getText().toString().split(":");

                    String Value = "";
                    for (int j = 0 ; j<myList.size();j++) {
                        for (int i = 0; i< str.length; i++)
                        {
                            if(myList.get(j).getCode_envoi().equalsIgnoreCase(str[i]))
                            {
                                myList1.add(str[i]);


                            }else
                            {

                            }

                        }


                        // Value += agc.getCode_envoi() + "\n";
                    }

                    if(myList1.size()==myList.size() && myList1.size()== str.length) {

                        builderSingle.setTitle("Code correcte");
                    }
                    else
                    {
                        builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                        builderSingle.setTitle("Code erroné");

                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            ctx, android.R.layout.simple_dropdown_item_1line);
                    //String str[]= code.split("\n");
                    for(int i = 0; i< str.length; i++)
                    {
                        arrayAdapter.add(str[i]);
                    }

                    builderSingle.setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });



                    builderSingle.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), Testt.class);
                                    intent.putExtra("myList",myList);
                                    intent.putExtra("str", str);
                                    // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();

                                }
                            });




                    builderSingle.
                            setAdapter(arrayAdapter,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String strName = arrayAdapter.getItem(which);



                     /*   AlertDialog.Builder builderInner = new AlertDialog.Builder(ctx);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();*/
                                        }
                                    });
                    builderSingle.create();
                    builderSingle.show();










            } else if (sep.equalsIgnoreCase("rtr"))

                {


                    final String[] str = code.getText().toString().split("\n");

                    String Value = "";
                    for (int j = 0 ; j<myList.size();j++) {
                        for (int i = 0; i< str.length; i++)
                        {
                            if(myList.get(j).getCode_envoi().equalsIgnoreCase(str[i]))
                            {
                                myList1.add(str[i]);


                            }else
                            {

                            }

                        }


                        // Value += agc.getCode_envoi() + "\n";
                    }

                    if(myList1.size()==myList.size() && myList1.size()== str.length) {

                        builderSingle.setTitle("Code correcte");
                    }
                    else
                    {
                        builderSingle.setIcon(R.drawable.sap_uex_alert_dialog_icon_gold);
                        builderSingle.setTitle("Code erroné");

                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            ctx, android.R.layout.simple_dropdown_item_1line);
                    //String str[]= code.split("\n");
                    for(int i = 0; i< str.length; i++)
                    {
                        arrayAdapter.add(str[i]);
                    }

                    builderSingle.setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });



                    builderSingle.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), Testt.class);
                                    intent.putExtra("myList",myList);
                                    intent.putExtra("str", str);
                                    // Toast.makeText(getContext(),agencies.size()+"taille",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();

                                }
                            });




                    builderSingle.
                            setAdapter(arrayAdapter,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String strName = arrayAdapter.getItem(which);



                     /*   AlertDialog.Builder builderInner = new AlertDialog.Builder(ctx);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();*/
                                        }
                                    });
                    builderSingle.create();
                    builderSingle.show();

            }


            }


        });




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

    private Bitmap ListToImageEncode(List<Agency3> list,String sep) throws WriterException {

        if(list.size() == 0)
            return null;

        String Value = "";
        for(Agency3 agc: list) {
            Value += agc.getCode_envoi() + sep;
        }

        return TextToImageEncode(Value);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, AgencyListActivity2.class);
            startActivity(intent);
            finish();
        }

        return false;
        // Disable back button..............


    }


}