package com.example.aquainsight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

public class RegistrationActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailET,passwordET,nameET;
    Button submit;
    TextView back;

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
