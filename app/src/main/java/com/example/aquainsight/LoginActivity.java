package com.example.aquainsight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText loginET,passwordET;
    private Button submit;
    FirebaseAuth auth;
    TextView jump,forgot;
    progressbarAdapter progressDialog;


    //UserProfileChangeRequest request=new UserProfileChangeRequest.Builder().setDisplayName("faculty 1").build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.PasswordET);
        submit=findViewById(R.id.submit);
        auth=FirebaseAuth.getInstance();
        jump=findViewById(R.id.gotoregister);
        forgot=findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog=new progressbarAdapter(LoginActivity.this,"Please Wait ... \n ","Login ");
                progressDialog.show();
                authenticateUser();
                            }
        });
    }
    public void authenticateUser(){
        String email=loginET.getText().toString();
        String password=passwordET.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.dismiss();
                    int i=auth.getUid().compareTo("Tha7aDZ7oXUvxEeexkqmFXRwvbv1");
                    if(i==0){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else {
                        //change with other one
//                        startActivity(new Intent(LoginActivity.this,FacultyActivity.class));
//                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() !=null){
            int i=auth.getUid().compareTo("Tha7aDZ7oXUvxEeexkqmFXRwvbv1");
            if(i==0){
//                        Toast.makeText(LoginActivity.this, "true", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }else {

//change it with other one
//                startActivity(new Intent(LoginActivity.this,FacultyActivity.class));
//                finish();
            }
        }
    }
}






































