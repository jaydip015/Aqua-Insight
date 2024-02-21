package com.example.aquainsight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
// Implement OnMapReadyCallback
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        GoogleMapOptions options=new GoogleMapOptions();
        options.mapToolbarEnabled(true);
//        options.camera(new CameraPosition(new LatLng(0,0),0,40,10));
        LatLngBounds BOUNDS = new LatLngBounds(
//                new LatLng(7.798000, 68.14712), new LatLng(37.090000, 97.34466)
                new LatLng(6.4626999, 68.1097), new LatLng(35.513327, 97.39535869999999)
//                new LatLng(22.4626999, 68.1471), new LatLng(29.513327, 97.34466)
//                new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466)

        );
        googleMap.setLatLngBoundsForCameraTarget(BOUNDS);
    }
}

