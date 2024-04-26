package com.example.aquainsight.Fragments;

import static com.example.aquainsight.NewRaiseActivty.LAT;
import static com.example.aquainsight.NewRaiseActivty.LONG;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Map;

public class Adminbottomsheet extends BottomSheetDialogFragment {
    Button delete,showImage;
    TextView headline,address,issue;
    String is;
    ArrayList<Map<String,Object>> data;

    public Adminbottomsheet(Marker marker, ArrayList<Map<String,Object>> mapArrayList) {
        is= (String) marker.getTag();
        this.data=mapArrayList;
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
        return view;
    }
}
