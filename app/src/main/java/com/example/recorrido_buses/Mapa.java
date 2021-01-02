package com.example.recorrido_buses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {
    ToggleButton tgbtn;
    private GoogleMap mMap;
    private DatabaseReference db_reference;
    private ArrayList<Marker> tmpRealTimeMarkers=new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers=new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        tgbtn=(ToggleButton) findViewById(R.id.tgBtn1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db_reference = FirebaseDatabase.getInstance().getReference();
    }

    public void toParadas(View view) {
        Intent intent = new Intent(Mapa.this, Parada.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db_reference.child("Parada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Marker marker:realTimeMarkers){
                    marker.remove();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MapsCoor mc = snapshot.getValue(MapsCoor.class);
                    Double lat = mc.getLat();
                    Double lon = mc.getLon();
                    MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.punto2)).anchor(0.0f,1.0f).title(snapshot.getKey());
                    markerOptions.position(new LatLng(lat, lon));

                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));
                }
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void switchButton(View view) {
        if(tgbtn.isChecked())
        {
            Toast.makeText(Mapa.this,"SigFox",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(Mapa.this,"GSM",Toast.LENGTH_SHORT).show();
        }
    }
}

