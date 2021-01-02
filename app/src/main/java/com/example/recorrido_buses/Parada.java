package com.example.recorrido_buses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Parada extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parada);

    }

    public void toNewParada(View view) {
        Intent intent = new Intent(Parada.this, newParada.class);
        startActivity(intent);
        finish();
    }

}