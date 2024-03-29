package com.example.aquainsight;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class NewRaiseActivty extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    EditText et;
    TextView headline;
    ImageButton select,opencmaera;
    ImageView prev;
    Intent i,intent;
    File file;
    String path,Mainheadline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_raise_activty);
        i=getIntent();

        //initialization
        headline=findViewById(R.id.headlineNR);
        select=findViewById(R.id.simage);
        prev=findViewById(R.id.prevIMG);
        opencmaera=findViewById(R.id.ocamera);
        et=findViewById(R.id.issueET);

        //setting data and listener
        setingImage();
        Mainheadline=i.getStringExtra("Headline");
        if(getIntent().getStringExtra("EditText")!=null){
            et.setText(getIntent().getStringExtra("EditText"));
        }
        headline.setText(Mainheadline);
        opencmaera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(NewRaiseActivty.this, CameraActivty.class);
                intent.putExtra("EditText",et.getText().toString());
                intent.putExtra("Headline",Mainheadline);
                if(allPermissionsGranted()){
                    startActivity(intent);
                }else {
                    ActivityCompat.requestPermissions(NewRaiseActivty.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                }

            }
        });
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

    private void setingImage() {
        path=i.getStringExtra("FilePath");
        try{

            file= new File(path);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(path != null){
            Bitmap bitmap = ImageUtil.decodeBitmapFromFile(file.getAbsolutePath(), 400, 400);
            prev.setImageBitmap(bitmap);
        }else {
            prev.setImageResource(R.drawable.logo);
        }
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
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}