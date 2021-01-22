package com.example.recorrido_buses;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Clases.User;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    ToggleButton tgbtn;
    private ImageButton mButtonSignOut;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private DatabaseReference db_reference;
    private ArrayList<Marker> tmpRealTimeMarkers=new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers=new ArrayList<>();
    private ArrayList<Marker> tmpRealTimeMarkersBus=new ArrayList<>();
    private ArrayList<Marker> realTimeMarkersBus=new ArrayList<>();
    public  ArrayList<String> listSigfox=new ArrayList<>();
    public  ArrayList<String> listGSM=new ArrayList<>();
    public  ArrayList<String> listSigfoxHora=new ArrayList<>();
    public  ArrayList<String> listGSMHora=new ArrayList<>();
    private View popup = null;
    private int userType=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        tgbtn=(ToggleButton) findViewById(R.id.tgBtn1);
        int status= GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status== ConnectionResult.SUCCESS){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            db_reference = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            isUser();



        }else{
            Dialog dialog =GooglePlayServicesUtil.getErrorDialog(status,(Activity)getApplicationContext(),10);
            dialog.show();
        }

    }

    public void toParadas(View view) {
        Intent intent = new Intent(Mapa.this, Parada.class);
        startActivity(intent);
        finish();
    }

    public void toConductor(View view) {
        Intent intent;
        if (userType==0){
            intent = new Intent(Mapa.this, Conductor.class);
        }
        else {

            intent = new Intent(Mapa.this, Perfil.class);
        }

        startActivity(intent);
        finish();
    }
    public void cerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(Mapa.this, Login.class);
        intent.putExtra("msg", "cerrarSesion");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    Boolean actualPosition = true;

    Double longitudOrigen, latitudOrigen;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (popup == null) {
                    popup = getLayoutInflater().inflate(R.layout.popupmaps, null);
                }


                TextView tvTitle = (TextView) popup.findViewById(R.id.title);
                TextView tvSnippet = (TextView) popup.findViewById(R.id.snippet);
                ImageView iv = (ImageView) popup.findViewById(R.id.icon);

                if(tmpRealTimeMarkersBus.contains(marker)){

                    Intent intent = new Intent(Mapa.this, Conductor.class);
                    intent.putExtra("id",marker.getTitle());
                    startActivity(intent);
                    finish();
                }

                tvTitle.setText(marker.getTitle());
                tvSnippet.setText(marker.getSnippet());

                return (popup);
            }
        });

        mostrarBus("GSM");

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location location) {

                if (actualPosition){
                    latitudOrigen = location.getLatitude();
                    longitudOrigen = location.getLongitude();
                    actualPosition=false;

                    LatLng miPosicion = new LatLng(latitudOrigen,longitudOrigen);

                    mMap.addMarker(new MarkerOptions().position(miPosicion).title("Aqui estoy yo"));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitudOrigen,longitudOrigen))
                            .zoom(15)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                }
            }
        });


    }

    private void mostrarBus(String tipo){
        db_reference.child("HistorialBuses").child("GYE2021").child(tipo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Marker marker:tmpRealTimeMarkersBus){
                    marker.remove();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

                String fechaI="";
                String key="";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    MapsCoor mc = snapshot.getValue(MapsCoor.class);
                    try {
                        if (fechaI.equals("")){
                            fechaI=mc.getFecha();
                            key=snapshot.getKey();
                        }
                        else {
                            Date fechaA = sdf.parse(fechaI);
                            Date fechaN = sdf.parse( mc.getFecha());

                            if(fechaA.compareTo(fechaN) < 0){

                                fechaI= mc.getFecha();
                                key=snapshot.getKey();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                assert key != null;
                MapsCoor mc = dataSnapshot.child(key).getValue(MapsCoor.class);
                Double lat = mc.getLat();
                Double lon = mc.getLon();
                MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.bus2)).anchor(0.0f,1.0f).title("GYE2021").snippet("Cap: 25");
                markerOptions.position(new LatLng(lat, lon));
                System.out.print(lat);
                tmpRealTimeMarkersBus.clear();
                mMap.clear();
                tmpRealTimeMarkersBus.add(mMap.addMarker(markerOptions));

                realTimeMarkersBus.addAll(tmpRealTimeMarkersBus);

                mostrarParadas(lat,lon);
                trazarRuta();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void mostrarParadas(Double latB, Double lonB){
        db_reference.child("Rutas").child("Alban Borja").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Marker marker : realTimeMarkers) {
                    marker.remove();
                }
                try{

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MapsCoor mc = snapshot.getValue(MapsCoor.class);
                        Double lat = mc.getLat();
                        Double lon = mc.getLon();


                        getDistance(latB,lonB,lat,lon,snapshot.getKey());

                    }

                    realTimeMarkers.clear();
                    realTimeMarkers.addAll(tmpRealTimeMarkers);
                }
                catch (Exception e){



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Mapa.this, "ALERTAAAAAAAAAA", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getDistance(double latI,double lonI,double latF,double lonF,String key) {

        String url ="https://maps.googleapis.com/maps/api/directions/json?origin="+latI+","+lonI+"&destination="+latF+","+lonF+"&mode=DRIVING&key=AIzaSyBFUUDV1Z6mQSMYWOSaJds8dU_gRs9b7EY";

        RequestQueue queue = Volley.newRequestQueue(Mapa.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                    try {
                        JSONObject jso = new JSONObject(response);
                        JSONArray jRoutes = jso.getJSONArray("routes");
                        JSONArray jLegs = ((JSONObject)(jRoutes.get(0))).getJSONArray("legs");
                        int jSteps = (int) ((JSONObject) ((JSONObject)jLegs.get(0)).get("duration")).get("value");

                        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.punto2)).anchor(0.0f, 1.0f).title(key).snippet(calcularTiempo(jSteps));
                        markerOptions.position(new LatLng(latF, lonF));
                        tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));


                            } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error","NO SALE EL MENSAJEEEE HELP!");
            }
        });

        queue.add(stringRequest);

    }

    public String calcularTiempo(int num) {

        int hor = (int) num/3600;
        int min = (int) (num/60 - hor*60);;

        return  String.valueOf(hor)+" Horas y "+String.valueOf(min)+" Minutos";
    }

    private void trazarRuta() {

        String url ="https://maps.googleapis.com/maps/api/directions/json?origin=-2.144610446888712,-79.96498202864169&destination=-2.1702576319691707,-79.91816793723682&mode=DRIVING&key=AIzaSyBFUUDV1Z6mQSMYWOSaJds8dU_gRs9b7EY";

        RequestQueue queue = Volley.newRequestQueue(Mapa.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jso = new JSONObject(response);

                    try {
                        JSONArray jRoutes = jso.getJSONArray("routes");
                        for (int i=0; i<jRoutes.length();i++){
                            JSONArray jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");
                            for (int j=0; j<jLegs.length();j++){

                                JSONArray jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                                for (int k = 0; k<jSteps.length();k++){

                                    String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                                    Log.i("end",""+polyline);
                                    List<LatLng> list = PolyUtil.decode(polyline);
                                    mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.BLUE).width(10));
                                }

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i("jsonRuta: ",""+response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error","NO SALE EL MENSAJEEEE HELP!");
            }
        });

        queue.add(stringRequest);

    }

    public void switchButton(View view) {
        if(tgbtn.isChecked())
        {
            setColorToggle(1);
            Toast.makeText(Mapa.this,"SigFox",Toast.LENGTH_SHORT).show();
            mostrarBus("SIGFOX");
        }
        else
        {
            setColorToggle(2);
            Toast.makeText(Mapa.this,"GSM",Toast.LENGTH_SHORT).show();
            mostrarBus("GSM");
        }
    }


    public void setColorToggle(int i){
        int draw=0;
        if (i==1){
            draw=R.drawable.edit_button_red;
        }
        else {
            draw=R.drawable.edit_button;
        }
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tgbtn.setBackgroundDrawable(ContextCompat.getDrawable(this, draw ));
        } else {
            tgbtn.setBackground(ContextCompat.getDrawable(this, draw));
        }
    }


    public void isUser() {

        FirebaseUser usuario = mAuth.getCurrentUser();
        db_reference.child("Users").child(usuario.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    userType = user.getTipo();
                }
                Toast.makeText(Mapa.this, "El usuario es "+userType, Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {

    }
}

