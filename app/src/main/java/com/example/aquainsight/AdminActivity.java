package com.example.aquainsight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.aquainsight.Fragments.BottomSheetFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    BottomSheetFragment dialog;
    ActionBarDrawerToggle toggle;
    DrawerLayout dl;
    NavigationView nv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        dl=findViewById(R.id.Admin_Activty);
        toggle =new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv=findViewById(R.id.anv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if (id == R.id.Home) {
                    Log.d("tag", "Home");
                } else if (id==R.id.Logout) {
                    FirebaseAuth auth;
                    auth=FirebaseAuth.getInstance();
                    auth.signOut();
                    Intent i=new Intent(AdminActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                } else if (id==R.id.ProFile) {

                }
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

}