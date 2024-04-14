package com.example.aquainsight.Fragments;

import static com.example.aquainsight.NewRaiseActivty.RISSUES;
import static com.example.aquainsight.NewRaiseActivty.USERS;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aquainsight.Files.Adapter;
import com.example.aquainsight.Files.progressbarAdapter;
import com.example.aquainsight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;


public class Profile extends Fragment {
    FirebaseAuth auth;
    RecyclerView rv;
    TextView Uname, Count;
    FirebaseFirestore db;
    progressbarAdapter progressDialog;
    ArrayList<Map<String,Object>> data;
    Adapter adapter;
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
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        rv=view.findViewById(R.id.activity_list);
        Uname=view.findViewById(R.id.UserName);
        Count =view.findViewById(R.id.count);
        data=new ArrayList<>();
        adapter=new Adapter(data);
        progressDialog=new progressbarAdapter(getContext(),"Please Wait ... \n ","Fetching Data ");
        progressDialog.show();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        FetchingData();
        Uname.setText(auth.getCurrentUser().getDisplayName());
        return view;
    }

    private void FetchingData() {
        db.collection(USERS)
                .document(auth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document =task.getResult();
                        Map<String,Object> map=document.getData();
                        ArrayList<DocumentReference> arrays= (ArrayList<DocumentReference>) map.get(RISSUES);
                        for (int i=0;i<arrays.size();i++) {
                            DocumentReference hashMapRef =  arrays.get(i);
                            fetchHashMapData(hashMapRef);
                        }
                        progressDialog.dismiss();

                    }
                });
    }
    private void fetchHashMapData(DocumentReference hashMapRef) {
        hashMapRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot hashMapDoc = task.getResult();
                    if (hashMapDoc.exists()) {
                        // Get the data from the HashMap document
                        Map<String, Object> hashMapData = hashMapDoc.getData();
                        data.add(hashMapData);
                        adapter.notifyDataSetChanged();
                        Count.setText(String.valueOf(data.size()));

                        // Process the data as needed
                    }

                }
            }
        });
    }
}