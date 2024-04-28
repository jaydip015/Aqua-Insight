package com.example.aquainsight.Fragments;

import static com.example.aquainsight.NewRaiseActivty.ADD;
import static com.example.aquainsight.NewRaiseActivty.DOCID;
import static com.example.aquainsight.NewRaiseActivty.ICOLLECTION;
import static com.example.aquainsight.NewRaiseActivty.ID;
import static com.example.aquainsight.NewRaiseActivty.LAT;
import static com.example.aquainsight.NewRaiseActivty.LINK;
import static com.example.aquainsight.NewRaiseActivty.LONG;
import static com.example.aquainsight.NewRaiseActivty.RISSUES;
import static com.example.aquainsight.NewRaiseActivty.USERS;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.aquainsight.R;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Adminbottomsheet extends BottomSheetDialogFragment {
    Button delete,showImage;
    ImageView img;
    TextView headline,address,issue;
    String is;
    Map<String,Object> data;
    String id;
    FirebaseFirestore db;
    Marker marker;

    public Adminbottomsheet(Marker marker, Map<String,Object> map, FirebaseFirestore db) {
        is= (String) marker.getTag();
        this.data=map;
        this.db=db;
        this.marker=marker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.admin_bottom_sheet, container, false);
        id=data.get(DOCID).toString();
        delete=view.findViewById(R.id.delete);
        showImage=view.findViewById(R.id.Show_image);
        headline=view.findViewById(R.id.headingA);
        address=view.findViewById(R.id.addressA);
        issue=view.findViewById(R.id.issueA);
        img=view.findViewById(R.id.rimage);
        img.setVisibility(View.GONE);
        issue.setText(is);
        address.setText(data.get(ADD).toString());
        geo_locate();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(ICOLLECTION).document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            db.collection(USERS).document(data.get(ID).toString()).update(RISSUES, FieldValue.arrayRemove(db.collection(ICOLLECTION).document(id)));
                            marker.remove();
                            dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link=(String) data.get(LINK);
                int i=link.compareTo("link");
                if(i==0){
                    Toast.makeText(getContext(), "Image not provided by reporting user", Toast.LENGTH_SHORT).show();
                }else{
                    String  check=showImage.getText().toString();
                    int c=check.compareTo("Show Image");
                    if (c==0){
                        showImage.setText("Hide Image");
                        img.setVisibility(View.VISIBLE);
                        Glide.with(getContext()).load(data.get(LINK)).into(img);
                    }else {
                        showImage.setText("Show Image");
                        img.setVisibility(View.GONE);
                    }
                }
            }
        });
        return view;
    }
    private void geo_locate(){
        Geocoder geocoder=new Geocoder(getContext());
        List<Address> list;
        try {
            list=geocoder.getFromLocation((double)data.get(LAT),(double)data.get(LONG),3);
            headline.setText(list.get(0).getFeatureName());
            Log.d("test",list.get(0).toString());
        }catch (IOException e){
            Log.w("war",e.getMessage());
        }
    }
}
