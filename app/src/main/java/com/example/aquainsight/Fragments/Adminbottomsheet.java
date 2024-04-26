package com.example.aquainsight.Fragments;

import static com.example.aquainsight.NewRaiseActivty.ADD;
import static com.example.aquainsight.NewRaiseActivty.LAT;
import static com.example.aquainsight.NewRaiseActivty.LONG;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.aquainsight.NewRaiseActivty;
import com.example.aquainsight.R;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Adminbottomsheet extends BottomSheetDialogFragment {
    Button delete,showImage;
    TextView headline,address,issue;
    String is;
    Map<String,Object> data;

    public Adminbottomsheet(Marker marker, Map<String,Object> map) {
        is= (String) marker.getTag();
        this.data=map;
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
        delete=view.findViewById(R.id.delete);
        showImage=view.findViewById(R.id.Show_image);
        headline=view.findViewById(R.id.headingA);
        address=view.findViewById(R.id.addressA);
        issue=view.findViewById(R.id.issueA);
        issue.setText(is);
        address.setText(data.get(ADD).toString());
        geo_locate();
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
