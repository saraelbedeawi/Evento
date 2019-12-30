package com.sf.evento.Activites;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sf.evento.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    Marker mMarker;
    Button button;
    double longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = (Button)findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LatLng position = new LatLng(latitude,longitude);
                String x = String.valueOf(position.longitude);
                String y = String.valueOf(position.latitude);
                Intent intent = new Intent(MapsActivity.this, CreateEvent.class);
                intent.putExtra("x",x);
                intent.putExtra("y",y);
                startActivity(intent);



            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng coordinate = new LatLng(30.1978108, 31.467544599999997);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 11.0f);

        // Add a marker in Sydney and move the camera
        //LatLng me = new LatLng(30.1978108, 31.467544599999997);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coordinate);
        markerOptions.title("Meeeeeee");
        mMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        mMap.animateCamera(yourLocation);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMarker.setPosition(mMap.getCameraPosition().target);//to center in map
                LatLng position = mMarker.getPosition(); //

                latitude=position.latitude;
                longitude=position.longitude;

            }
        });
    }








}