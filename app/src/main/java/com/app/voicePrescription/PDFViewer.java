package com.app.voicePrescription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        File file = new File("/storage/emulated/0/"+Prescribe.getName()+".pdf");
        PDFView pdfView = findViewById(R.id.pdfView1);
        pdfView.fromFile(file).load();
        mail();
    }
    private void mail(){
//        String filename=name.getText().toString()+".pdf";
        String mail = Prescribe.getEmail();
        File file = new File("/storage/emulated/0/"+Prescribe.getName()+".pdf");
        Uri path = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
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
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}