package com.example.aquainsight;

import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    List<Address> list;
    TextView heading,address,pincode;

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
        address.setText(list.get(0).getAddressLine(0));
        heading.setText(list.get(0).getFeatureName());
        pincode.setText(list.get(0).getPostalCode());
        return view;
    }
}