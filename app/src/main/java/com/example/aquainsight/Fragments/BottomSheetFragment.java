package com.example.aquainsight.Fragments;

import static com.example.aquainsight.NewRaiseActivty.LAT;
import static com.example.aquainsight.NewRaiseActivty.LONG;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aquainsight.NewRaiseActivty;
import com.example.aquainsight.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    List<Address> list;
    TextView heading,address,pincode;
    Button raiseissue;
    public BottomSheetFragment(List<Address> list) {
        // Required empty public constructor
        this.list=list;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        heading=view.findViewById(R.id.mainheading);
        address=view.findViewById(R.id.address);
        pincode=view.findViewById(R.id.postalcode);
        raiseissue=view.findViewById(R.id.addrv);
        raiseissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), NewRaiseActivty.class);
                i.putExtra("Headline",list.get(0).getFeatureName());
                i.putExtra(LAT,list.get(0).getLatitude());
                i.putExtra(LONG,list.get(0).getLongitude());
                startActivity(i);
            }
        });
        address.setText(list.get(0).getAddressLine(0));
        heading.setText(list.get(0).getFeatureName());
        pincode.setText(list.get(0).getPostalCode());
        return view;
    }
}