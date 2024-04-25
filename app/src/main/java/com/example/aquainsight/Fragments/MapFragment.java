package com.example.aquainsight.Fragments;

import static com.example.aquainsight.NewRaiseActivty.ICOLLECTION;
import static com.example.aquainsight.NewRaiseActivty.LAT;
import static com.example.aquainsight.NewRaiseActivty.LONG;

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

import com.example.aquainsight.Files.progressbarAdapter;
import com.example.aquainsight.NewRaiseActivty;
import com.example.aquainsight.R;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private final int REQUEST_CODE = 100;
    private static final String PERMISSION_LOCATION_FINE = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String DATA="data";
    public static final String WARN="warning";
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean PermissionGranted;
    GoogleMap mMap;
    float DEFAULT_ZOOM = 18f;
    ImageView mylocation,pin;
    EditText search;
    Marker previousMarker,rmarker;
    List<Address> list;
    BottomSheetFragment dialog;
    public static final String TAG = "ModalBottomSheet";
    Geocoder geocoder;
    FirebaseFirestore db;
    progressbarAdapter progressDialog;
    public MapFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        requestPermission();
        db=FirebaseFirestore.getInstance();
        fetchfromALlissue();
        geocoder=new Geocoder(getContext());
        SupportMapFragment mapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        mylocation=view.findViewById(R.id.gps);
        pin=view.findViewById(R.id.pin);
        search= view.findViewById(R.id.search);
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropMarkerAtCenter();
            }
        });
        mapFragment.getMapAsync(this);
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
    private void dropMarkerAtCenter() {
        if (mMap != null) {
//            mMap.clear();
            LatLng center = mMap.getCameraPosition().target;
//            marker = mMap.addMarker(new MarkerOptions().position(center).title("Marker at Center").draggable(true));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
            try {
                list=new ArrayList<>();
                list=geocoder.getFromLocation(center.latitude, center.longitude,1);
                if(list.size()>0){
                    Address address=list.get(0);
                    moveCamera(center,DEFAULT_ZOOM, address.getAddressLine(0));
                }else{
                    moveCamera(center,17f,center.latitude+" "+ center.longitude);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(getContext(), "Map is not ready yet", Toast.LENGTH_SHORT).show();
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
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                        list=new ArrayList<>();
                    try {
                        list=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                        if(list.size()>0){
                            Log.d(DATA,list.toString());
                            moveCamera(latLng,DEFAULT_ZOOM, list.get(0).getAddressLine(0));

                        }
                    } catch (IOException e) {
                        Log.w(WARN,e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }
    private void fetchfromALlissue(){
        progressDialog=new progressbarAdapter(getContext(),"Please Wait ... \n ","Fetching Reported Issues ");
        progressDialog.show();
        db.collection(ICOLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("Maps", document.getId() + " => " + document.getData());
                    Map<String,Object> map=document.getData();
                    LatLng l=new LatLng((Double) map.get(LAT),(Double)map.get(LONG));
                    rmarker=mMap.addMarker(new MarkerOptions().position(l));
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("war",e.getMessage());
                progressDialog.dismiss();

            }
        });
    }

    private void geo_locate(){
//        mMap.clear();
        String sresult=search.getText().toString();
        list=new ArrayList<>();
        try {
            list=geocoder.getFromLocationName(sresult,3);
        }catch (IOException e){
            Log.w("war",e.getMessage());
            if(!sresult.isEmpty()){
                Log.w("war","no input text");
            }

        }
        if(list.size()>0){
            Address address=list.get(0);
            Log.d("data",list.toString());
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
        Log.d("data",list.toString());
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