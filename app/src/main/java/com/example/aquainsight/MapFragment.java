package com.example.aquainsight;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.provider.Settings;
public class MapFragment extends Fragment implements OnMapReadyCallback{
    private final int REQUEST_CODE = 100;
    private static final String PERMISSION_LOCATION_FINE = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean PermissionGranted;
    GoogleMap mMap;
    float DEFAULT_ZOOM = 18f;
    ImageView mylocation;
    EditText search;
    Marker previousMarker;
    List<Address> list=new ArrayList<>();
    BottomSheetFragment dialog;
    public static final String TAG = "ModalBottomSheet";
    public MapFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        mylocation=view.findViewById(R.id.gps);
        search= view.findViewById(R.id.search);
        mapFragment.getMapAsync(this);
        requestPermission();
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLocationEnabled()){
                    getDeviceLocation();
                }else{
                    Toast.makeText(getContext(), "Your Location is turned off", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i== EditorInfo.IME_ACTION_SEARCH){
                    geo_locate();
                }
                return false;
            }
        });
        return view;
    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        // When map is loaded
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                // When clicked on map
//                // Initialize marker options
//                MarkerOptions markerOptions=new MarkerOptions();
//                // Set position of marker
//                markerOptions.position(latLng);
//                // Set title of marker
//                markerOptions.title(latLng.latitude+" : "+latLng.longitude);
//                // Remove all marker
//                googleMap.clear();
//                // Animating to zoom the marker
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//                // Add marker on map
//                googleMap.addMarker(markerOptions);
//            }
//        });
//    }
    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), PERMISSION_LOCATION_FINE) == PackageManager.PERMISSION_GRANTED) {
            PermissionGranted = true;
//            Toast.makeText(this, "Permission Granted all feature will work properly ", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_LOCATION_FINE)) {

            //why this app need this permission explaining to user
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("This app need location permission to work some feature to work properly.").setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{PERMISSION_LOCATION_FINE, PERMISSION_LOCATION_COARSE}, REQUEST_CODE);
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{PERMISSION_LOCATION_FINE, PERMISSION_LOCATION_COARSE}, REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission Granted all feature will work properly ", Toast.LENGTH_SHORT).show();

            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_LOCATION_FINE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                Uri uri = Uri.fromParts("package",getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{PERMISSION_LOCATION_FINE, PERMISSION_LOCATION_COARSE}, REQUEST_CODE);

            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (PermissionGranted) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }

    }
    private void geo_locate(){
        String sresult=search.getText().toString();
        Geocoder geocoder=new Geocoder(getContext());

        try {
            list=geocoder.getFromLocationName(sresult,3);
        }catch (IOException e){

        }
        if(list.size()>0){
            Address address=list.get(0);
//            for(int i=0;i<list.size();i++){
//            Log.d("MainActivity",list.get(i).toString());}
//            Log.d("MainActivity",list.get(0).getPostalCode());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));

        }
    }
    private boolean checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        boolean isGpsEnabled = false;
        boolean isNetworkEnabled = false;

        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return isGpsEnabled;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isGpsEnabled && !isNetworkEnabled) {
            Toast.makeText(getContext(), "Your location is turned off, \n please Turn On", Toast.LENGTH_SHORT).show();
            // You can prompt the user to enable location services here
        }
        return false;
    }
    private void moveCamera(LatLng latLng, float zoom,String title) {
        if (previousMarker != null) {
            previousMarker.remove();
        }

        // Add new marker for the searched location
        previousMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        CameraUpdate update=CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        mMap.animateCamera(update);
        //opening bottom sheet fragment
        dialog=new BottomSheetFragment(list);
        dialog.show(getActivity().getSupportFragmentManager(),TAG);



    }
    private void getDeviceLocation(){

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());
        try {
            if(PermissionGranted){
                Task location =fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){

                            Location currentlocation= (Location) location.getResult();

                            CameraUpdate update=CameraUpdateFactory.newLatLngZoom(new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude()),DEFAULT_ZOOM);
                            mMap.animateCamera(update);
//                            moveCamera(new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude()),DEFAULT_ZOOM,"My Location" );
                        }else {

                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.d("Mactivity",e.getMessage());
        }

    }
}