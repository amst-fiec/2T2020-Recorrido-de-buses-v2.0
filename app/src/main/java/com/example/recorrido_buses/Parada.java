package com.example.recorrido_buses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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




    }



    public void toNewParada(View view) {
        Intent intent = new Intent(Parada.this, newParada.class);
        startActivity(intent);
        finish();
    }



/*
    private class AttemptEquipos extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... args)
        {
            int success;
            try {


                // json success tag
                success = jsonEmp.getInt(TAG_SUCCESS);
                if (success == 1)
                {

                    JSONObject subclases=jsonEmp.getJSONObject("subclase");
                    JSONObject ids=jsonEmp.getJSONObject("id");
                    JSONObject codigos=jsonEmp.getJSONObject("codigo");
                    JSONObject areas=jsonEmp.getJSONObject("area");
                    listaEquipos= new ArrayList<>();
                    if (subclases != null) {

                                    for (int i = 0; i < subclases.length(); i++) {
                                        subclase=subclases.getString(String.valueOf(i + 1));
                                        id=ids.getString(String.valueOf(i + 1));
                                        codigo=codigos.getString(String.valueOf(i + 1));
                                        area=areas.getString(String.valueOf(i + 1));
                                        listaEquipos.add(new DatosListView(id,subclase, "Código: "+codigo ,"Área: "+area));
                                    }

                    }



                    return jsonEmp.getString(TAG_MESSAGE);

                }
                else
                {
                    Log.d("Proyectos Failure!", jsonEmp.getString(TAG_MESSAGE));
                    return jsonEmp.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute()
        {
            //super.onPreExecute();
        }

        @Override

        protected void onPostExecute(String file_url)
        { // if(listaEquipos!=null) {
            adaptador=new Adaptador(getApplicationContext(),listaEquipos);
            listEquipo.setAdapter(adaptador);
        }//else Toast.makeText(selEmpres.this,"No hay equipo registrado",Toast.LENGTH_LONG).show();
        //}
    }
    */


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Parada.this, Mapa.class);
        startActivity(intent);
        finish();
    }


}