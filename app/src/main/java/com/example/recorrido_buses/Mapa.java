package com.example.recorrido_buses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Mapa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


    }

    public void toParadas(View view) {

        Intent intent = new Intent(Mapa.this, Parada.class);
        startActivity(intent);
        finish();

    }
}