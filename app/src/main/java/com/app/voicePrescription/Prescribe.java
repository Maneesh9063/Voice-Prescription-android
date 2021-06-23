package com.app.voicePrescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.voicePrescription.model.Prescription;
import com.app.voicePrescription.R;
import com.github.barteksc.pdfviewer.PDFView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Prescribe extends AppCompatActivity {

    private static EditText name,age,mobile,symptoms,email,med;
    Button generate;
    int pageHeight = 1120;
    int pagewidth = 792;

    Bitmap bmp, scaledbmp;
    static Prescribe INSTANCE;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribe);
        name = findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        symptoms = findViewById(R.id.symptoms);
        med = findViewById(R.id.med);

        generate = findViewById(R.id.generate);
        INSTANCE = this;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.presc_bg);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 792, 1120, false);
        generate.setOnClickListener(v -> {
            generatePDF();
        });

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
    }

    private void generatePDF() {
//        Log.i("TAG", "generatePDF: button clciked");
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        if(myPage == null) Log.i("TAG", "generatePDF: mypage");
        Canvas canvas = myPage.getCanvas();
        if( canvas != null) {
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawBitmap(scaledbmp,0,0,  paint);
            int titleBaseLine = 162;
            int leftMargin = 184;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String d = formatter.format(date);
            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

            title.setTextSize(20);
            title.setTextAlign(Paint.Align.LEFT);
            title.setColor(ContextCompat.getColor(this, R.color.color_black));

            canvas.drawText(getName(), leftMargin+1,titleBaseLine,  title);
            canvas.drawText(d, leftMargin+410,titleBaseLine,  title);
            canvas.drawText(getAge(), leftMargin+140, titleBaseLine+65, title);

            canvas.drawText(getMobile(), leftMargin+260, titleBaseLine+186, title);

            canvas.drawText(getSymptoms(), leftMargin, titleBaseLine+228, title);
            canvas.drawText(med.getText().toString(), leftMargin-100, titleBaseLine+401, title);

            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            title.setColor(ContextCompat.getColor(this, R.color.color_black));
            title.setTextSize(5);

            pdfDocument.finishPage(myPage);
            File file = new File(Environment.getExternalStorageDirectory(), name.getText().toString()+".pdf");

            try {

                pdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(Prescribe.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();

                Intent intent= new Intent(Prescribe.this,PDFViewer.class);
                startActivity(intent);

            } catch (IOException e) {

                e.printStackTrace();
            }
            pdfDocument.close();
        }
    }
    private void mail(String mail,File f){
        String filename=name.getText().toString()+".pdf";

        File file = new File("/storage/emulated/0/"+Prescribe.getName()+".pdf");
        Uri path = Uri.fromFile(file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
    // set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        if(!isValidEmail(mail)) {
            mail = "chinu.sma@gmail.com";
        }
        String to[] = {mail};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
    // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
    // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Medical Prescription");
        emailIntent .putExtra(Intent.EXTRA_TEXT, "Here is your E-Prescription");

        startActivity(Intent.createChooser(emailIntent , "Send email..."));
}
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {


                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

    }

    public static Prescribe getActivityInstance()
    {
        return INSTANCE;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String getName() {
        return name.getText().toString();
    }

    public String getAge() {
        return age.getText().toString();
    }

    public String getMobile() {
        return mobile.getText().toString();
    }

    public String getSymptoms() {
        return symptoms.getText().toString();
    }

    public static String getEmail() {
        return email.getText().toString();
    }

    public String getMed() {
        return med.getText().toString();
    }
}