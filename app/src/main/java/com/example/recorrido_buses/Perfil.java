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
import com.squareup.picasso.Transformation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Clases.Bus;
import Clases.Driver;
import Clases.User;

public class Perfil extends AppCompatActivity {

    private DatabaseReference db_reference;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    private String userId;

    private TextView tvtName;
    private TextView tvtEdad;
    private TextView tvtEmail;
    private ImageView btnPhoto;

    private String name;
    private String edad;
    private String email;
    private String photo;


    private FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        //userId="NjCqTX1WCWQKt4PigN8ATigN02i2";

        mAuth =FirebaseAuth.getInstance();
        db_reference= FirebaseDatabase.getInstance().getReference();

        user= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        userId=user.getUid();
        tvtName = (TextView) findViewById(R.id.tvtNombre);
        tvtEdad = (TextView) findViewById(R.id.tvtEdad);
        tvtEmail = (TextView) findViewById(R.id.tvtEmail);
        btnPhoto=(ImageView)findViewById(R.id.btnPhoto);


       db_reference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                name=noNull(user.getName());
                edad=noNull(user.getEdad());
                email=noNull(user.getEmail());

                try {
                    photo = noNull(user.getPhoto());
                    Picasso.with(Perfil.this).load(photo).transform(new CircleTransform()).into(btnPhoto);
                } catch (Exception e) {
                    System.out.println("No hay foto");


                }
                tvtName.setText(name);
                tvtEdad.setText(edad);
                tvtEmail.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Perfil.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
            }
        }
        );

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

        Intent intent = new Intent(Perfil.this, Mapa.class);
        startActivity(intent);
        finish();
    }

}


