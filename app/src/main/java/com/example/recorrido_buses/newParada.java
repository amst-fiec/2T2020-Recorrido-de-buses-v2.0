package com.example.recorrido_buses;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.HashMap;
        import java.util.Map;

public class newParada extends AppCompatActivity {

    private EditText edtName;
    private EditText edtLat;
    private EditText edtLon;

    private String name="";
    private String  latitud ="";
    private String longitud ="";
    private Double  lat =0.0;
    private Double lon =0.0;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_parada);

        mAuth =FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();


        edtName = (EditText) findViewById(R.id.edtName);
        edtLat = (EditText) findViewById(R.id.edtLat);
        edtLon = (EditText) findViewById(R.id.edtLon);

    }


    public void registrar(View view) {
        name =edtName.getText().toString();

        latitud =edtLat.getText().toString();
        longitud =edtLon.getText().toString();

        if(!name.isEmpty() && !latitud.isEmpty() && !longitud.isEmpty()){


            lat =Double.valueOf(edtLat.getText().toString());
            lon =Double.valueOf(edtLon.getText().toString());

            registerStation();
        }
        else {
            Toast.makeText(newParada.this,"Debe rellenar todos los campos",Toast.LENGTH_LONG).show();

        }

    }

    private void registerStation(){

        Map<String, Object> map= new HashMap<>();
        map.put("lat",lat);
        map.put("lon",lon);

        mDatabase.child("Parada").child(name).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()){
                    Toast.makeText(newParada.this, "Parada registrada", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(newParada.this, Mapa.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(newParada.this, "No se pudo crear la parada correctamente", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}