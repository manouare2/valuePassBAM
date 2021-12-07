package com.example.karim.applicationfacteur.ui.main;



        import android.os.Build;
        import android.os.Environment;
        import android.support.annotation.RequiresApi;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.pdf.PdfDocument;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.example.karim.applicationfacteur.R;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;


public class PDF extends AppCompatActivity {

    Button btnCreate;
    EditText editText,editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdflayout);

        btnCreate = (Button)findViewById(R.id.create);
        editText =(EditText) findViewById(R.id.edittext);
        editText2 =(EditText) findViewById(R.id.edittext2);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                createPdf(editText.getText().toString(),editText2.getText().toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(String title,String description){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText(title, 20, 40, paint);
        canvas.drawText(description, 20, 60, paint);
        canvas.drawText("test", 20, 80, paint);
        canvas.drawText("*****************************************", 20, 100, paint);
        // canvas.drawText(description,1,20,20.0f,30.0f,paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"DATA"+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }
}
