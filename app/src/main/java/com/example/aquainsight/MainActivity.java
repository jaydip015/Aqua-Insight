package com.example.aquainsight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

// Implement OnMapReadyCallback
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int REQUEST_CODE = 100;
    private static final String PERMISSION_LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean PermissionGranted;
    GoogleMap mMap;
    float DEFAULT_ZOOM = 18f;
    ImageView mylocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mylocation=findViewById(R.id.gps);
        requestPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLocationEnabled()){
                    getDeviceLocation();
                }
            }
        });

    }

    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, PERMISSION_LOCATION_FINE) == PackageManager.PERMISSION_GRANTED) {
            PermissionGranted = true;
//            Toast.makeText(this, "Permission Granted all feature will work properly ", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, PERMISSION_LOCATION_FINE)) {

            //why this app need this permission explaining to user
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app need location permission to work some feature to work properly.").setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION_LOCATION_FINE, PERMISSION_LOCATION_COARSE}, REQUEST_CODE);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_LOCATION_FINE, PERMISSION_LOCATION_COARSE}, REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted all feature will work properly ", Toast.LENGTH_SHORT).show();

            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, PERMISSION_LOCATION_FINE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setTitle("Permission Required").setCancelable(false)
                        .setMessage("One feature unavailable to you Unable to that feature You have to go to setting and allow permission for Location")
                        .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_LOCATION_FINE, PERMISSION_LOCATION_COARSE}, REQUEST_CODE);

            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (PermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }


//        GoogleMapOptions options=new GoogleMapOptions();
//        options.mapToolbarEnabled(true);
////        options.camera(new CameraPosition(new LatLng(0,0),0,40,10));
//        LatLngBounds BOUNDS = new LatLngBounds(
////                new LatLng(7.798000, 68.14712), new LatLng(37.090000, 97.34466)
//                new LatLng(6.4626999, 68.1097), new LatLng(35.513327, 97.39535869999999)
////                new LatLng(22.4626999, 68.1471), new LatLng(29.513327, 97.34466)
////                new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466)
//
//        );
//        googleMap.setLatLngBoundsForCameraTarget(BOUNDS);
    }
    private boolean checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGpsEnabled = false;
        boolean isNetworkEnabled = false;

        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isGpsEnabled && !isNetworkEnabled) {
            Toast.makeText(this, "Unable to get your Current Location", Toast.LENGTH_SHORT).show();

            // You can prompt the user to enable location services here
        }
        return false;
    }
    private void moveCamera(LatLng latLng, float zoom,String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if(!title.equals("My Location")){
            MarkerOptions options=new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);

        }
    }
    private void getDeviceLocation(){

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try {
            if(PermissionGranted){
                Task location =fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){

                            Location currentlocation= (Location) location.getResult();


                            moveCamera(new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude()),DEFAULT_ZOOM,"My Location" );
                        }else {

                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.d("Mactivity",e.getMessage());
        }

    }
}

