package com.example.recorrido_buses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Clases.Bus;
import Clases.Driver;

public class newConductor extends AppCompatActivity {

    private Button btnRegistrar;

    private EditText edtName;
    private EditText edtEdad;
    private EditText edtPlaca;
    private EditText edtCapacidad;

    private String name="";
    private String edad="";
    private String placa="";
    private String capacidad="";


    private String idConductor="";
    private String idBus="";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conductor);

        mAuth =FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        idConductor=mAuth.getCurrentUser().getUid();

        edtName = (EditText) findViewById(R.id.edtName);
        edtEdad = (EditText) findViewById(R.id.edtEdad);
        edtPlaca = (EditText) findViewById(R.id.edtPlaca);
        edtCapacidad = (EditText) findViewById(R.id.edtCapacidad);

        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        mDatabase.child("Users").child(idConductor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Driver driver = dataSnapshot.getValue(Driver.class);

                name=noNull(driver.getName());
                edad=noNull(driver.getEdad());

                mDatabase.child("Bus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            Bus bus = snapshot.getValue(Bus.class);
                            if ( noNull(bus.getConductor()).equals(idConductor)){
                                idBus=snapshot.getKey();
                                placa=noNull(bus.getPlaca());
                                capacidad=noNull(bus.getCapacidad());

                                edtName.setText(name);
                                edtEdad.setText(edad);
                                edtCapacidad.setText(capacidad);
                                edtPlaca.setText(placa);
                                return;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(newConductor.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(newConductor.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void registrar(View view) {
        name =edtName.getText().toString();
        edad =edtEdad.getText().toString();
        placa =edtPlaca.getText().toString();
        capacidad=edtCapacidad.getText().toString();

        if(!name.isEmpty() && !edad.isEmpty() && !placa.isEmpty()  && !capacidad.isEmpty() ){
            registerConductor();
        }
        else {
            Toast.makeText(newConductor.this,"Debe rellenar todos los campos",Toast.LENGTH_LONG).show();

        }

    }

    private void registerConductor(){

        Map<String, Object> map= new HashMap<>();
        map.put("name",name);
        map.put("edad",Integer.valueOf(edad));



        Map<String, Object> mapBus= new HashMap<>();
        mapBus.put("placa",placa);
        mapBus.put("capacidad",Integer.valueOf(capacidad));

        mDatabase.child("Bus").child(idBus).setValue(mapBus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){


                    mDatabase.child("Users").child(idConductor).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){



                                Intent intent = new Intent(newConductor.this, Conductor.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(newConductor.this, "No se pudo editar correctamente", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
                else {
                    Toast.makeText(newConductor.this, "No se pudo editar correctamente", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public String noNull(Object obj){
        if(obj==null){
            return "";
        }
        else {
            return String.valueOf(obj);
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(newConductor.this, Conductor.class);
        startActivity(intent);
        finish();
    }
}







