package com.example.recorrido_buses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import Clases.Bus;
import Clases.Driver;

public class Conductor extends AppCompatActivity {


    private DatabaseReference db_reference;
    private FirebaseAuth mAuth;

    private String userId;


    private FirebaseUser driverRef;

    private TextView tvtName;
    private TextView tvtEdad;
    private TextView tvtPlaca;
    private TextView tvtCapacidad;
    private ImageView btnPhoto;

    private String name;
    private String edad;
    private String photo;
    private String placa;
    private String capacidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor);

        driverRef = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        db_reference = FirebaseDatabase.getInstance().getReference();

        tvtName = (TextView) findViewById(R.id.tvtNombre);
        tvtEdad = (TextView) findViewById(R.id.tvtEdad);
        tvtPlaca = (TextView) findViewById(R.id.tvtPlaca);
        tvtCapacidad = (TextView) findViewById(R.id.tvtCapacidad);
        btnPhoto = (ImageView) findViewById(R.id.btnPhoto);

        userId = driverRef.getUid();

        //userId="NjCqTX1WCWQKt4PigN8ATigN02i2";
        if (getIntent().hasExtra("id")) {

            placa = getIntent().getStringExtra("id");


            db_reference.child("Buses").child(placa).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Bus bus = dataSnapshot.getValue(Bus.class);


                    capacidad = noNull(bus.getCapacidad());


                    db_reference.child("Users").child(bus.getConductor()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Driver driver = dataSnapshot.getValue(Driver.class);

                            name = noNull(driver.getName());
                            edad = noNull(driver.getEdad());

                            try {
                                photo = noNull(driver.getPhoto());
                                Picasso.with(Conductor.this).load(photo).transform(new CircleTransform()).into(btnPhoto);
                            } catch (Exception e) {


                            }
                            tvtName.setText(name);
                            tvtEdad.setText(edad);
                            tvtCapacidad.setText(capacidad);
                            tvtPlaca.setText(placa);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        else {
            getUser();
        }
    }

    private void getUser(){

        db_reference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Driver driver = dataSnapshot.getValue(Driver.class);

                name = noNull(driver.getName());
                edad = noNull(driver.getEdad());

                try {
                    photo = noNull(driver.getPhoto());
                    Picasso.with(Conductor.this).load(photo).transform(new CircleTransform()).into(btnPhoto);
                } catch (Exception e) {


                }


                tvtName.setText(name);
                tvtEdad.setText(edad);


                db_reference.child("Buses").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Bus bus = snapshot.getValue(Bus.class);
                            if (noNull(bus.getConductor()).equals(userId)) {

                                placa = noNull(bus.getPlaca());
                                capacidad = noNull(bus.getCapacidad());

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

    public void toParadas(View view) {
        Intent intent = new Intent(Conductor.this, Parada.class);
        startActivity(intent);
        finish();
    }

    public void toMapa(View view) {
        Intent intent = new Intent(Conductor.this, Mapa.class);
        startActivity(intent);
        finish();
    }

    public String noNull(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Conductor.this, Mapa.class);
        startActivity(intent);
        finish();
    }

}