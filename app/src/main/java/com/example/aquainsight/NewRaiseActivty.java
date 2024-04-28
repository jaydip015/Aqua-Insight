package com.example.aquainsight;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aquainsight.Files.ImageUtil;
import com.example.aquainsight.Files.progressbarAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewRaiseActivty extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    public static final String ID = "Uid",
            ISSUE = "issue",
            LINK = "Img_Url",
            LAT = "latitude",
            LONG = "longitude",
            ADD="address",
            FILEPATH="FilePath",
            ICOLLECTION="All Issues",
            RISSUES="Reported Issue",
            USERS="Users",
            SEEN="seen";
    FirebaseAuth auth;
    FirebaseFirestore db;
    StorageReference reference;
    String CUserID;
    Button submit;
    EditText et;
    TextView headline;
    ImageButton select, open_camera;
    ImageView prev;
    Intent i,intent;
    File file;
    Uri link;
    String path,Mainheadline;
    progressbarAdapter progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_raise_activty);
        i=getIntent();

        //initialization
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        reference= FirebaseStorage.getInstance().getReference().child("Reported Images");
        headline=findViewById(R.id.headlineNR);
        select=findViewById(R.id.simage);
        prev=findViewById(R.id.prevIMG);
        open_camera =findViewById(R.id.ocamera);
        et=findViewById(R.id.issueET);
        submit=findViewById(R.id.submit);
        //setting data and listener
        CUserID=auth.getUid();
        setingImage();
        Mainheadline=i.getStringExtra("Headline");
        if(getIntent().getStringExtra("EditText")!=null){
            et.setText(getIntent().getStringExtra("EditText"));
        }
        headline.setText(Mainheadline);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new progressbarAdapter(NewRaiseActivty.this,"Please Wait ... \n ","Submitting Data ");
                progressDialog.show();
                checking();
            }
        });
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(NewRaiseActivty.this, CameraActivty.class);
                intent.putExtra("EditText",et.getText().toString());
                intent.putExtra(LAT,i.getDoubleExtra(LAT,0));
                intent.putExtra(LONG,i.getDoubleExtra(LONG,0));
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

    private void checking() {
        Geocoder geocoder=new Geocoder(this);
        String issue=et.getText().toString();
        double lattitude=i.getDoubleExtra(LAT,0);
        double longitude=i.getDoubleExtra(LONG,0);
        if(lattitude!=0 &&longitude!=0){
            try {

                List<Address> list=geocoder.getFromLocation(lattitude,longitude,1);
                Log.d("data",list.toString());
                if(!issue.isEmpty()){
                    AddToDataBase(issue,list);
                }else{
                    Toast.makeText(this, "Issue is not written", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.w("war",e.getMessage());
                throw new RuntimeException(e);
            }
        }else {
            Log.w("war","somekind of problem!!");
        }

    }

    private void AddToDataBase(String issue, List<Address> list) {
        Map<String , Object> map=new HashMap<>();
        map.put(ISSUE,issue);
        map.put(LAT,list.get(0).getLatitude());
        map.put(LONG,list.get(0).getLongitude());
        map.put(ID,CUserID);
        map.put(ADD,list.get(0).getAddressLine(0));
        map.put(SEEN,false);
        if(link==null){
            map.put(LINK,"NA");
        }else {
            map.put(LINK,link);
        }
        db.collection(ICOLLECTION).add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference hashMapRef = task.getResult();
                            // Reference the HashMap document in child nodes
                            String hashMapDocId = hashMapRef.getId();
                            AddreferenceInUser(hashMapDocId);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("war",e.getMessage());
                    }
                });
    }

    private void AddreferenceInUser(String hashMapDocId) {

        db.collection(USERS)
                .document(auth.getUid())
                .update(RISSUES, FieldValue.arrayUnion(db.collection(ICOLLECTION).document(hashMapDocId)))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        },500);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("war",e.getMessage());
                    }
                });
    }


    private void setingImage() {
        path=i.getStringExtra(FILEPATH);
        if(path != null){
            try{
                file= new File(path);
                Log.d("tad",path);
                Uri ri= Uri.fromFile(file);
                uploadPhoto(ri);
            }catch (Exception e){
                e.printStackTrace();
            }
            Bitmap bitmap = ImageUtil.decodeBitmapFromFile(file.getAbsolutePath(), 400, 400);
//            prev.setImageBitmap(bitmap);
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
                        Uri ImageUri=data.getData();
                        Log.d("taf",ImageUri.toString());
//                        prev.setImageURI(ImageUri);
                        uploadPhoto(ImageUri);

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
    private File getFileFromUri(Uri uri) {
        Context context = getApplicationContext();
        String scheme = uri.getScheme();
        if (scheme != null) {
            if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                // Handle content URIs
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    return new File(filePath);
                }
            } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
                // Handle file URIs
                return new File(uri.getPath());
            } else {
                // Handle other URIs
                // For example, if the URI is a Firebase Storage download URL, you might need to download the file first
            }
        }
        return null;
    }
    public void uploadPhoto(Uri u){
        File f=getFileFromUri(u);
        progressDialog=new progressbarAdapter(NewRaiseActivty.this,"Please Wait ... \n ","Uploading Photo  ");
        progressDialog.show();
        StorageReference storageReference=reference.child(f.getName());
        storageReference.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        link=uri;
                        Glide.with(getApplicationContext()).load(uri).into(prev);
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("war",e.getMessage());
            }
        });
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