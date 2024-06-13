package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Donor_page extends AppCompatActivity {
    private Button Register,Details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_page);

        Register =findViewById(R.id.btn_donor_reg);
        Details = findViewById(R.id.btn_donor_detail);

//.....................press donor register button........................................................
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Donor_page.this,Donor_register.class));
                finish();
            }
        });

//....................press donor details button....................................................
        Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Donor_page.this,Donor_detail.class));
                finish();
            }
        });


//.........................set navigation bar..................................................
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        Intent int_logout = new Intent(Donor_page.this,LogOut.class);
                        startActivity(int_logout);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        Intent int_home = new Intent(Donor_page.this,Home.class);
                        startActivity(int_home);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}