package com.example.aquainsight.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aquainsight.Files.Adapter;
import com.example.aquainsight.R;
import com.google.firebase.auth.FirebaseAuth;


public class Profile extends Fragment {
    FirebaseAuth auth;
    RecyclerView rv;
    TextView Uname;
    public Profile() {
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
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        rv=view.findViewById(R.id.activity_list);
        Uname=view.findViewById(R.id.UserName);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new Adapter());
        auth=FirebaseAuth.getInstance();
        Uname.setText(auth.getCurrentUser().getDisplayName());
        return view;
    }
}