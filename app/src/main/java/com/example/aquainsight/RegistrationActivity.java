package com.example.aquainsight;

import static com.example.aquainsight.NewRaiseActivty.RISSUES;
import static com.example.aquainsight.NewRaiseActivty.USERS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailET,passwordET,nameET;
    Button submit;
    TextView back;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth=FirebaseAuth.getInstance();
        emailET=findViewById(R.id.emailETR);
        passwordET=findViewById(R.id.PasswordETR);
        nameET=findViewById(R.id.nameETR);
        submit=findViewById(R.id.submitR);
        back=findViewById(R.id.havit);
        db=FirebaseFirestore.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication();
            }
        });
    }
    public void authentication(){
        String name=nameET.getText().toString(),email=emailET.getText().toString(),password=passwordET.getText().toString();
        if(!name.isEmpty() && !email.isEmpty()&& !password.isEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    UserProfileChangeRequest request=new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    FirebaseUser user=authResult.getUser();
                    user.updateProfile(request);
                    Map<String ,Object> UerData=new HashMap<>();
                    UerData.put("Name",name);
                    UerData.put("Email",email);
                    UerData.put(RISSUES,null);
                    db.collection(USERS).document(auth.getUid()).set(UerData).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("War",e.getMessage());
                        }
                    });
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}
