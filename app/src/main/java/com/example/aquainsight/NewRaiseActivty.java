package com.example.aquainsight;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class NewRaiseActivty extends AppCompatActivity {
    TextView headline;
    ImageButton select;
    ImageView prev;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_raise_activty);
        i=getIntent();
        headline=findViewById(R.id.headlineNR);
        select=findViewById(R.id.simage);
        prev=findViewById(R.id.prevIMG);
        headline.setText(i.getStringExtra("Headline"));
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                i.addCategory(Intent.CATEGORY_OPENABLE);

                pick.launch(i);

            }
        });
    }
    ActivityResultLauncher<Intent> pick=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent data=result.getData();
                        Uri musicUri=data.getData();
                        prev.setImageURI(musicUri);

                    }

                }
            });
}