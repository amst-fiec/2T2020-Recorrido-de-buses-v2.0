package com.example.recorrido_buses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class Conductor extends AppCompatActivity {


    private DatabaseReference db_reference;
    private FirebaseAuth mAuth;

    private String userId;



    private TextView tvtName;
    private TextView tvtEdad;
    private TextView tvtPlaca;
    private TextView tvtCapacidad;

    private String name;
    private String edad;
    private String placa;
    private String capacidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor);


        userId="NjCqTX1WCWQKt4PigN8ATigN02i2";


        mAuth =FirebaseAuth.getInstance();
        db_reference= FirebaseDatabase.getInstance().getReference();


        tvtName = (TextView) findViewById(R.id.tvtNombre);
        tvtEdad = (TextView) findViewById(R.id.tvtEdad);
        tvtPlaca = (TextView) findViewById(R.id.tvtPlaca);
        tvtCapacidad = (TextView) findViewById(R.id.tvtCapacidad);


        db_reference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Driver driver = dataSnapshot.getValue(Driver.class);

                name=noNull(driver.getName());
                edad=noNull(driver.getEdad());


                db_reference.child("Bus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            Bus bus = snapshot.getValue(Bus.class);
                            if ( noNull(bus.getConductor()).equals(userId)){

                                placa=noNull(bus.getPlaca());
                                capacidad=noNull(bus.getCapacidad());

                                tvtName.setText(name);
                                tvtEdad.setText(edad);
                                tvtCapacidad.setText(capacidad);
                                tvtPlaca.setText(placa);

                                return;
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(Conductor.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Conductor.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void toNewConductor(View view) {
        Intent intent = new Intent(Conductor.this, newConductor.class);
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