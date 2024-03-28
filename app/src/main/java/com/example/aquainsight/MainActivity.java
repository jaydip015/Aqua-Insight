package com.example.aquainsight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

// Implement OnMapReadyCallback
public class MainActivity extends AppCompatActivity  {

    ActionBarDrawerToggle toggle;
    DrawerLayout dl;
    NavigationView nv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for Navigation Drawer
        dl=findViewById(R.id.activity_main);
        toggle =new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv=findViewById(R.id.nv);
        loadFragment(new MapFragment());
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if (id == R.id.Home) {
                    loadFragment(new MapFragment());
                } else if (id==R.id.Logout) {
                    FirebaseAuth auth;
                    auth=FirebaseAuth.getInstance();
                    auth.signOut();
                    Intent i=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                } else if (id==R.id.ProFile) {
                    loadFragment(new Profile());

                }
                dl.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    //for navigation drawer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    public void loadFragment(Fragment fragment){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction ft=manager.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}

