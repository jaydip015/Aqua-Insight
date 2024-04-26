package com.example.aquainsight.Fragments;

import static com.example.aquainsight.NewRaiseActivty.ADD;
import static com.example.aquainsight.NewRaiseActivty.ICOLLECTION;
import static com.example.aquainsight.NewRaiseActivty.ISSUE;
import static com.example.aquainsight.NewRaiseActivty.LAT;
import static com.example.aquainsight.NewRaiseActivty.LONG;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aquainsight.Files.progressbarAdapter;
import com.example.aquainsight.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminMap extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private float DEFAULT_ZOOM = 18f;
    private List<Address> list;
    private EditText search;
    private FirebaseFirestore db;
    private Marker previousMarker,rmarker;
    private progressbarAdapter progressDialog;
    public static final String ATAG = "ModalBottomSheet";
    private Geocoder geocoder;
    Adminbottomsheet dialog;
    ArrayList<Map<String,Object>> data;
    public AdminMap() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_admin_map, container, false);
        db=FirebaseFirestore.getInstance();
        data=new ArrayList<>();
        fetchfromALlissue();
        geocoder=new Geocoder(getContext());
        SupportMapFragment mapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        search= view.findViewById(R.id.search);
        mapFragment.getMapAsync(this);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i== EditorInfo.IME_ACTION_SEARCH){
                    geo_locate();
                    if(list.size()>0){
                        Address address=list.get(0);
                        Log.d("data",list.toString());
                        moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));

                    }
                }
                return false;
            }
        });
        return view;
    }
    private void fetchfromALlissue(){
        progressDialog=new progressbarAdapter(getContext(),"Please Wait ... \n ","Fetching Reported Issues ");
        progressDialog.show();
        db.collection(ICOLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String,Object> map=document.getData();
                    data.add(map);
                    LatLng l=new LatLng((Double) map.get(LAT),(Double)map.get(LONG));
                    rmarker=mMap.addMarker(new MarkerOptions().position(l));
                    rmarker.setIcon(bitmapDescriptorFromVector(R.drawable.reported_pin, 100, 100));
                    rmarker.setTag(map.get(ISSUE));
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
    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId, int width, int height) {
        Drawable vectorDrawable = getResources().getDrawable(vectorResId);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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

    }
    private void moveCamera(LatLng latLng, float zoom,String title) {
        if (previousMarker != null) {
            previousMarker.remove();
        }
        Log.d("data",list.toString());
        // Add new marker for the searched location
        previousMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        mMap.animateCamera(update);
        //opening bottom sheet fragment
//        dialog=new BottomSheetFragment(list);
//        dialog.show(getActivity().getSupportFragmentManager(),TAG);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int index=findIndexByIssue(data,marker.getTag().toString());
                dialog=new Adminbottomsheet(marker,data.get(index));
                dialog.show(getActivity().getSupportFragmentManager(),ATAG);
                return true; // Return true to indicate that the click event is consumed
            }
        });
    }
    int findIndexByIssue(ArrayList<Map<String,Object>> data, String issueToFind) {
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            Object issue = map.get(ISSUE);
            if (issue != null && issue.equals(issueToFind)) {
                return i; // Return the index if the issue is found
            }
        }
        return -1; // Return -1 if the issue is not found
    }

}