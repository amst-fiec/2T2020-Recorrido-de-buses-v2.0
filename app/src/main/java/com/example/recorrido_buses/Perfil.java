package com.example.recorrido_buses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Clases.Bus;
import Clases.Driver;
import Clases.User;

public class Perfil extends AppCompatActivity {

    private DatabaseReference db_reference;
    private FirebaseAuth mAuth;

    private String userId;

    private TextView tvtName;
    private TextView tvtEdad;
    private TextView tvtEmail;


    private String name;
    private String edad;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        userId="NjCqTX1WCWQKt4PigN8ATigN02i2";

        mAuth =FirebaseAuth.getInstance();
        db_reference= FirebaseDatabase.getInstance().getReference();

        tvtName = (TextView) findViewById(R.id.tvtNombre);
        tvtEdad = (TextView) findViewById(R.id.tvtEdad);
        tvtEmail = (TextView) findViewById(R.id.tvtEmail);

        db_reference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                name=noNull(user.getName());
                edad=noNull(user.getEdad());
                email=noNull(user.getEmail());


                tvtName.setText(name);
                tvtEdad.setText(edad);
                tvtEmail.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Perfil.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void toNewConductor(View view) {
        Intent intent = new Intent(Perfil.this, newConductor.class);
        startActivity(intent);
        finish();
    }

    public void toParadas(View view) {
        Intent intent = new Intent(Perfil.this, Parada.class);
        startActivity(intent);
        finish();
    }

    public void toMapa(View view) {
        Intent intent = new Intent(Perfil.this, Mapa.class);
        startActivity(intent);
        finish();
    }

    public String noNull(Object obj){
        if(obj==null){
            return "";
        }
        else {
            return String.valueOf(obj);
        }
    }



}


