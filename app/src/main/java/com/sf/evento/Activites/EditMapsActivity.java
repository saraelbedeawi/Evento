package com.sf.evento.Activites;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
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

public class EditMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker mMarker;
    LatLng position;
    Button save;

    double latt;
    double longg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latt = Double.parseDouble(getIntent().getStringExtra("latt"));
        longg = Double.parseDouble(getIntent().getStringExtra("long"));
        position = new LatLng(latt, longg);

        save= findViewById(R.id.Save);
                save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latt", latt);
                resultIntent.putExtra("long", longg);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title("Meeeeeee");
        mMarker =  mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(position, 7.0f);
        mMap.animateCamera(yourLocation);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMarker.setPosition(mMap.getCameraPosition().target);//to center in map
                LatLng position = mMarker.getPosition(); //

                latt = position.latitude;
                longg = position.longitude;
            }
        });


    }

}
