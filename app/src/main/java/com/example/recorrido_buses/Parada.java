package com.example.recorrido_buses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Parada extends AppCompatActivity {


    private DatabaseReference db_reference;
    private ListView simpleList;
    List<String> listParadas = new ArrayList<String>();
    //private String listParadas[] = {"India", "China", "australia", "Portugle", "America", "NewZealand","Caracas","Petare"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parada);

        db_reference = FirebaseDatabase.getInstance().getReference();

        simpleList = (ListView)findViewById(R.id.listaParadas);

        db_reference.child("Parada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    listParadas.add(snapshot.getKey());
                }

                if(listParadas!=null || listParadas.size()!=0) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Parada.this, R.layout.activity_listview, R.id.textView, listParadas);
                    simpleList.setAdapter(arrayAdapter);
                }
                else {
                    Toast.makeText(Parada.this, "Lista vacía", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Parada.this, "Error al obtener lista", Toast.LENGTH_SHORT).show();
            }
        });


        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String parada = adapterView.getItemAtPosition(i).toString();

                Intent intent = new Intent(Parada.this, newParada.class);
                intent.putExtra("isNew",false);
                intent.putExtra("parada",parada);
                startActivity(intent);
                finish();

                /*
                idEquipo=obj.getId();
                if(isInst){
                    new CompruebaInstalacion().execute();
                }else{
                    new CompruebaReporte().execute();
                }
*/
            }
        });

    }


    public void toNewParada(View view) {
        Intent intent = new Intent(Parada.this, newParada.class);
        startActivity(intent);
        intent.putExtra("isNew",true);
        finish();
    }



    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Parada.this, Mapa.class);
        startActivity(intent);
        finish();
    }


}