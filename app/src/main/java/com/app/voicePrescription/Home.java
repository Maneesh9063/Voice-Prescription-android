package com.app.voicePrescription;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.app.voicePrescription.R;

public class Home extends AppCompatActivity {

    private Context context;
    CardView card;
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        card = findViewById(R.id.card);
        card.setOnClickListener(view -> {
            Intent intent= new Intent(Home.this,Prescribe.class);
            startActivity(intent);
        });

    }

}


